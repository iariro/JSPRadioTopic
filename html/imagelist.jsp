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

			<s:form action="uploadimage" method="post" enctype="multipart/form-data" theme="simple">
				<input type="file" name="uploadfile" multiple="multiple" size="75" />
				<input type="hidden" name="dayid" value="<s:property value='dayid' />">
				<input type="submit" value="画像アップロード" />
			</s:form>

			<s:iterator value="images">
				<img src="/kumagai/image?folder=RadioTopicImageFolder&filename=<s:property value='filename' />"><br>

				<s:form action="deleteimage" theme="simple">
					<input type="hidden" name="imageId" value="<s:property value="id" />">
					<input type="hidden" name="filename" value="<s:property value="filename" />">
					<s:submit value="削除" />
				</s:form>
			</s:iterator>

		</div>
		</div>
		</div>

	</body>
</html>
