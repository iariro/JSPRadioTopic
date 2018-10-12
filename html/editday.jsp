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
		<h1>日情報編集</h1>

		<div class=hatena-body>
		<div class=main>
		<div class=day>

			dayid＝<s:property value="dayid" /><br>
			番組名＝<s:property value="name" /><br>
			<s:form action="editdayprogram" theme="simple">
				<input type="hidden" name="dayid" value="<s:property value="dayid" />">
				<s:select name="programid" list="programCollection" listKey="id" listValue="name" value="%{programid}" />
				<s:submit value="番組変更" />
			</s:form>

			<s:form action="editday2" theme="simple">
				<input type="hidden" name="dayid" value="<s:property value="dayid" />">
				回＝<input type="text" name="no" value="<s:property value="no" />">
				日付＝<input type="text" name="date" value="<s:property value="date" />">
				<s:submit value="更新" />
			</s:form>

			<table>
			<s:iterator value="topicCollection">
				<tr>
				<td>
				<s:form action="edittopic" theme="simple">
					<input type="hidden" name="dayid" value="<s:property value="dayid" />">
					<input type="hidden" name="topicid" value="<s:property value="id" />">
					<input type="text" name="text" value="<s:property value="text" />">
					<s:radio list="#{1:'変更', 2:'削除' , 3:'↑', 4:'↓' }" name="method" value="1"></s:radio>
					<s:submit value="操作" />
				</s:form>
				</td>
				</tr>
			</s:iterator>
			</table>

			<s:form action="addtopic" theme="simple">
				<input type="hidden" name="dayid" value="<s:property value="dayid" />">
				<input type="text" name="text">
				<s:submit value="トピック追加" />
			</s:form>

		</div>
		</div>
		</div>

	</body>
</html>
