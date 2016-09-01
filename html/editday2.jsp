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

			更新しました。<br>
			dayid＝<s:property value="dayid" /><br>
			日付＝<s:property value="date" /><br>
			回＝<s:property value="no" /><br>

		</div>
		</div>
		</div>

	</body>
</html>
