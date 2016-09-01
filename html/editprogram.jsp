<%@ page contentType="text/html; charset=utf-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv=Content-Style-Type content=text/css>
		<link rel="stylesheet" type="text/css" href="hatena.css">
		<title>ラジオ番組内容記録 - <s:property value="programName" /></title>
	</head>

	<body>
		<h1>番組情報編集</h1>

		<div class=hatena-body>
		<div class=main>
		<div class=day>

			<h2><s:property value="programName" /></h2>

			<s:form action="editprogram2" theme="simple">
				<input type="hidden" name="programid" value="<s:property value="programid" />">
				番組名：<input type="text" name="name" value="<s:property value="programName" />"><br>
				ファイル名：<input type="text" name="shortname" value="<s:property value="shortname" />"><br>
				年代：<input type="text" name="age" value="<s:property value="age" />"><br>
				最初期の入力日付に使用する日付：<input type="text" name="originUpdateDate" value="<s:property value="originUpdateDate" />"><br>
				ソート順：<s:radio list="#{0:'回（文字）', 1:'回（数値）' , 2:'日付' }" name="sortOrder" value="sortOrder"></s:radio><br>
				<s:submit value="番組情報編集" />
			</s:form>

			<h3>ステータス</h3>

			<table>
				<tr>
					<td>総数</td>
					<td><s:property value="maxNo" /></td>
				</tr>
				<tr>
					<td>視聴日数</td>
					<td><s:property value="updateRange" /></td>
				</tr>
				<tr>
					<td>視聴日数中の視聴タイトル数</td>
					<td><s:property value="updateCount" /></td>
				</tr>
				<tr>
					<td>視聴済みタイトル数</td>
					<td><s:property value="listenCount" /></td>
				</tr>
				<tr>
					<td>残りタイトル数</td>
					<td><s:property value="remainCount" /></td>
				</tr>
				<tr>
					<td>予測終了日</td>
					<td><s:property value="estimateDay" /></td>
				</tr>
				<tr>
					<td>予測終了日内訳</td>
					<td><s:property value="estimateDetail" /></td>
				</tr>
			</table>

		</div>
		</div>
		</div>

	</body>
</html>
