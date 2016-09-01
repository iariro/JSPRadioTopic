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
		<h1>トピック追加</h1>

		<div class=hatena-body>
		<div class=main>
		<div class=day>

			<s:property value="methodString" />しました<br>
			topicid=<s:property value="topicid" /><br>
			text=<s:property value="text" /><br>

		</div>
		</div>
		</div>

	</body>
</html>
