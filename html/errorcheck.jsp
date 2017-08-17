<%@ page contentType="text/html; charset=utf-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv=Content-Style-Type content=text/css>
		<link rel="stylesheet" type="text/css" href="hatena.css">
		<title>ラジオ番組内容記録 - エラーチェック</title>
		<link rel="stylesheet" type="text/css" href="jquery/style.css">
	</head>

	<body>
		<h1>エラーチェック</h1>

		<div class=hatena-body>
		<div class=main>
		<div class=day>

			<s:iterator value="invalidDates">
				<li><s:property value="date" />
			</s:iterator>

		</div>
		</div>
		</div>

	</body>
</html>
