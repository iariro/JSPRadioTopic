<%@ page contentType="text/html; charset=utf-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv=Content-Style-Type content=text/css>
		<link rel="stylesheet" type="text/css" href="hatena.css">
		<title>ラジオ番組内容記録</title>
	</head>

	<body>
		<h1>ラジオ番組一覧</h1>

		<div class=hatena-body>
		<div class=main>
		<div class=day>

			<s:form action="recentupdatelist" theme="simple">
				<input type="hidden" name="dayNum" value="3">
				<s:submit value="最近聞いた回リスト" />
			</s:form>

			<h2>視聴途中</h2>

			<s:if test="%{tochuuDays.size()>0}">
			<table>
				<tr>
					<th>番組名</th>
					<th>日付</th>
					<th>回</th>
				</tr>
				<s:iterator value="tochuuDays">
					<tr>
						<td><s:property value="programName" /></td>
						<td><s:property value="date" /></td>
						<td><s:property value="no" /></td>
					</tr>
				</s:iterator>
			</table>
			</s:if>

			<h2>番組一覧</h2>

			<table>
				<tr>
					<th>ID</th>
					<th>番組名</th>
					<th colspan="5">操作</th>
					<th>年代</th>
				</tr>
			<s:iterator value="programCollection">
				<tr>
				<td align="right"><s:property value="id" /></td>
				<td><s:property value="name" /></td>
				<td>
					<s:form action="listday" theme="simple">
						<input type="hidden" name="programid" value="<s:property value="id" />">
						<input type="hidden" name="programName" value="<s:property value="name" />">
						<input type="hidden" name="sortOrder" value="<s:property value="sortOrder" />">
						<s:submit value="各回リスト" />
					</s:form>
				</td>
				<td>
					<s:form action="completetable" theme="simple">
						<input type="hidden" name="programid" value="<s:property value="id" />">
						<input type="hidden" name="programName" value="<s:property value="name" />">
						<input type="hidden" name="sortOrder" value="<s:property value="sortOrder" />">
						<input type="hidden" name="originUpdateDate" value="<s:property value="originUpdateDate" />">
						<s:submit value="表" />
					</s:form>
				</td>
				<td>
					<s:form action="completegraph" theme="simple">
						<input type="hidden" name="programid" value="<s:property value="id" />">
						<input type="hidden" name="programName" value="<s:property value="name" />">
						<input type="hidden" name="sortOrder" value="<s:property value="sortOrder" />">
						<input type="hidden" name="originUpdateDate" value="<s:property value="originUpdateDate" />">
						<input type="hidden" name="checkbox" value="false">
						<s:submit value="コンプリートグラフ" />
					</s:form>
				</td>
				<td>
					<s:form action="linecountstatgraph" theme="simple">
						<input type="hidden" name="programid" value="<s:property value="id" />">
						<input type="hidden" name="programName" value="<s:property value="name" />">
						<input type="hidden" name="sortOrder" value="<s:property value="sortOrder" />">
						<s:submit value="文字列長グラフ" />
					</s:form>
				</td>
				<td>
					<s:form action="editprogram" theme="simple">
						<input type="hidden" name="programid" value="<s:property value="id" />">
						<input type="hidden" name="programName" value="<s:property value="name" />">
						<input type="hidden" name="shortname" value="<s:property value="shortname" />">
						<input type="hidden" name="age" value="<s:property value="age" />">
						<input type="hidden" name="sortOrder" value="<s:property value="sortOrder" />">
						<input type="hidden" name="originUpdateDate" value="<s:property value="originUpdateDate" />">
						<s:submit value="編集" />
					</s:form>
				</td>
				<td><s:property value="age" /></td>
				</tr>
			</s:iterator>
			</table>

			<s:form action="addprogram" theme="simple">
				番組名：<input type="text" name="name">
				<s:submit value="追加" />
			</s:form>

		</div>
		</div>
		</div>

	</body>
</html>
