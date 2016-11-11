<%@ page contentType="text/html; charset=utf-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<style type="text/css">
td.programNameColumn { width:400px; }
td.noColumn { width:50px; text-align:right; }
</style>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv=Content-Style-Type content=text/css>
		<link rel="stylesheet" type="text/css" href="hatena.css">
		<title>ラジオ番組内容記録 - 最近聞いた回リスト</title>
	</head>

	<body>
		<h1>ラジオ番組一覧 - 最近聞いた回リスト</h1>

		<div class=hatena-body>
		<div class=main>
		<div class=day>

			<s:iterator value="recentUpdateDays">
				<table>
					<tr>
						<th>番組名</th>
						<th>放送日</th>
						<th>回</th>
						<th>更新日</th>
					</tr>
					<s:iterator>
						<tr>
							<td class="programNameColumn"><s:property value="programName" /></td>
							<td><s:property value="date" /></td>
							<td class="noColumn"><s:property value="no" /></td>
							<td><s:property value="updateDateTimeAsString" /></td>
						</tr>
					</s:iterator>
				</table>
			</s:iterator>

		</div>
		</div>
		</div>

	</body>
</html>
