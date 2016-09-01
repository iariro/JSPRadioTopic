<%@ page contentType="text/html; charset=utf-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<style type="text/css">
td.notouch { width:25px; color:black; background-color:white; }
td.update { width:25px; color:white; background-color:blue; }
td.create { width:25px; color:white; background-color:green; }
</style>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv=Content-Style-Type content=text/css>
		<link rel="stylesheet" type="text/css" href="hatena.css">
		<title>ラジオ番組内容記録 - <s:property value="programName" /></title>
	</head>

	<body>
		<script type="text/javascript" src="completetable.js"></script>
		<script type="text/javascript">
		var ids = <s:property value="ids" />;
		var dates = <s:property value="dates" />;
		var maxNo = <s:property value="maxNo" />;
		window.onload = function()
		{
			updateTable();
		}
		</script>
		<h1>ラジオ番組一覧</h1>

		<div class=hatena-body>
		<div class=main>
		<div class=day>

			<s:property value="programName" /><br>

			<input id='range' type='range' min='0' max='<s:property value="stepCount" />' value='<s:property value="stepCount" />' oninput='updateTable()'>
			<div id='date'><s:property value="date" /></div><br>

			<table>
			<s:iterator value="cellArray">
				<tr>
				<s:iterator>
					<td align="right" id="cell<s:property />" class="notouch"><s:property /></td>
				</s:iterator>
				</tr>
			</s:iterator>
			</table>

		</div>
		</div>
		</div>

	</body>
</html>
