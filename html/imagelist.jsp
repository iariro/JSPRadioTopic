<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
	<head>
		<title>ラジオ番組内容記録 - 画像一覧</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="hatena.css">
	</head>

	<body>
		<h1>ラジオ番組内容記録 - 画像一覧</h1>

		<div class=hatena-body>
		<div class=main>
		<div class=day>

			<s:iterator value="images">
				<img src="/kumagai/image?folder=RadioTopicImageFolder&filename=<s:property />"><br>
			</s:iterator>

		</div>
		</div>
		</div>

	</body>
</html>
