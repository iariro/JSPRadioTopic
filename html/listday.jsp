<%@ page contentType="text/html; charset=utf-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv=Content-Style-Type content=text/css>
		<link rel="stylesheet" type="text/css" href="hatena.css">
		<title>ラジオ番組内容記録 - <s:property value="programName" /></title>
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
		<script src="jquery/jquery.tablesorter.min.js"></script>
		<script>
		$(function() { $('#sorter').tablesorter({sortInitialOrder:"desc",headers:{4:{sorter:false},5:{sorter:false},6:{sorter:false},7:{sorter:false}}}); });
		</script>
		<script type="text/javascript" src="listday.js"></script>
		<link rel="stylesheet" type="text/css" href="jquery/style.css">
	</head>

	<body>
		<h1>番組情報編集</h1>

		<div class=hatena-body>
		<div class=main>
		<div class=day>

			<h2><s:property value="programName" /></h2>

			<br>
			<s:form action="adddaylump1" theme="simple">
				<input type="hidden" name="programid" value="<s:property value="programid" />">
				<input type="hidden" name="programName" value="<s:property value="programName" />">
				<input type="hidden" name="sortOrder" value="<s:property value="sortOrder" />">
				<s:submit value="日追加（内容含め一括）" />
			</s:form>

			<s:form action="imagelist" theme="simple">
				<input type="hidden" name="programid" value="<s:property value="programid" />">
				<input type="hidden" name="programName" value="<s:property value="programName" />">
				<input type="hidden" name="sortOrder" value="<s:property value="sortOrder" />">
				<s:submit value="画像一覧" />
			</s:form>

			<table>
			<tr>
				<td>
				<s:property value="count" /> / <s:property value="dayCollection.size" /> =
				<s:property value="completeRatio" />%<br>
				</td>
				<td>
				検索：<input type='text' id='keyword' oninput='searchKeyword(this)'>
				</td>
			</tr>
			</table>

			<table id="sorter" class="tablesorter">
			<thead>
				<tr>
					<th style='width:80px;'>日付</th>
					<th style='width:40px; text-align:left;'>回</th>
					<th>内容</th>
					<th style='width:40px; text-align:left;'>長さ</th>
					<th>操作</th>
					<th>操作</th>
					<th>画像</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
			<s:iterator value="dayCollection">
				<tr>
				<s:if test="%{id>=0}">
					<td><s:property value="date" /></td>
					<td align="right"><s:property value="no" /></td>
					<td style='width:550px;'>
						<s:iterator value="topicCollection">
							<s:property value="text" />
						</s:iterator>
					</td>
					<td style='text-align:right;'><s:property value="topicCollection.lengthByHalfWidth" /></td>
					<td>
						<s:form action="editday" theme="simple">
							<input type="hidden" name="dayid" value="<s:property value="id" />">
							<input type="hidden" name="name" value="<s:property value="programName" />">
							<input type="hidden" name="date" value="<s:property value="date" />">
							<input type="hidden" name="no" value="<s:property value="no" />">
							<s:submit value="編集" />
						</s:form>
					</td>
					<td>
						<s:form action="imagelist" theme="simple">
							<input type="hidden" name="dayid" value="<s:property value="id" />">
							<input type="hidden" name="programName" value="<s:property value="programName" />">
							<input type="hidden" name="no" value="<s:property value="no" />">
							<s:submit value="画像" />
						</s:form>
					</td>
					<td align="right"><s:property value="imagenum" /></td>
				</s:if>
				<s:else>
					<td><div style="color:red;"><s:property value="date" /></div></td>
					<td align="right"><div style="color:red;"><s:property value="no" /></div></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</s:else>

				<td>
					<s:if test="newUpdate">
						<img src="new.png">
					</s:if>
				</td>
				</tr>
			</s:iterator>
			</tbody>
			</table>

			<br>

			<s:form action="addday" theme="simple">
				<input type="hidden" name="programid" value="<s:property value="programid" />">
				<input type="hidden" name="programName" value="<s:property value="name" />">
				<input type="text" name="no">回
				日付：<input type="text" name="date">
				<s:submit value="日追加" />
			</s:form>

		</div>
		</div>
		</div>

	</body>
</html>
