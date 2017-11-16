<%@ page contentType="text/html; charset=utf-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv=Content-Style-Type content=text/css>
		<link rel="stylesheet" type="text/css" href="hatena.css">
		<title>ラジオ番組内容記録 - キーワード検索</title>
	</head>

	<body>
		<h1>ラジオ番組一覧 - キーワード検索</h1>

		<div class=hatena-body>
		<div class=main>
		<div class=day>

		<table>
			<tr>
				<th>番組名</th>
				<th>回</th>
				<th>放送日</th>
				<th>トピック</th>
				<th>更新日</th>
			</tr>
			<s:iterator value="searchTopicResults">
				<tr>
					<td><s:property value="name" /></td>
					<td><s:property value="no" /></td>
					<td><s:property value="date" /></td>
					<td><s:property value="text" /></td>
					<td><s:property value="updatedate" /></td>
				</tr>
			</s:iterator>
		</table>

		</div>
		</div>
		</div>

	</body>
</html>
