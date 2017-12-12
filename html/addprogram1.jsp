<%@ page contentType="text/html; charset=utf-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv=Content-Style-Type content=text/css>
		<link rel="stylesheet" type="text/css" href="hatena.css">
		<title>ラジオ番組内容記録 - 番組追加</title>
	</head>

	<body>
		<h1>番組追加</h1>

		<div class=hatena-body>
		<div class=main>
		<div class=day>

			<h2>番組追加</h2>

			<br>
			<s:form action="addprogram2" theme="simple">
				番組名：<input type="text" name="name"><br>
				ファイル名：<input type="text" name="shortName"><br>
				年代：<input type="text" name="age" value="<s:property value="age" />"><br>
				最初期の入力日付に使用する日付：<input type="text" name="originUpdateDate" value="<s:property value="originUpdateDate" />"><br>
				ソート順：<s:radio list="#{0:'回（文字）', 1:'回（数値）' , 2:'日付' }" name="sortOrder" value="1"></s:radio><br>
				<s:submit value="番組追加" />
			</s:form>

		</div>
		</div>
		</div>

	</body>
</html>
