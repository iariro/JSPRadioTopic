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
		<h1>日と内容一括追加</h1>

		<div class=hatena-body>
		<div class=main>
		<div class=day>

			<s:property value="programName" /><br>
			第<s:property value="nos" />回を登録しました<br>

		</div>
		</div>
		</div>

	</body>
</html>
