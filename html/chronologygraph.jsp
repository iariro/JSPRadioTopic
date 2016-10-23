<%@ page contentType="text/html; charset=utf-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv=Content-Style-Type content=text/css>
		<link rel="stylesheet" type="text/css" href="hatena.css">
		<title>ラジオ番組内容記録 - 年表</title>
	</head>

	<body>
		<h1>ラジオ番組一覧 - 年表</h1>

		<div class=hatena-body>
		<div class=main>
		<div class=day>

		<img src="/kumagai/RadioTopicChronologyGraph" usemap="#menu">

		<map name="menu">
			<s:iterator value="chronologyGraphData">
				<area shape="rect" coords="<s:property value="coords" />" href="listday.action?programid=<s:property value="id" />&programName=<s:property value="name" />&sortOrder=<s:property value="sortOrder" />">
			</s:iterator>
		</map>

		</div>
		</div>
		</div>

	</body>
</html>
