<%@ page contentType="text/html; charset=utf-8" %>

<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv=Content-Style-Type content=text/css>
		<link rel="stylesheet" type="text/css" href="hatena.css">
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
		<script type="text/javascript" src="http://code.highcharts.com/highcharts.js"></script>
		<title>ラジオ番組内容記録 - 年表</title>
	</head>

	<body>
		<h1>ラジオ番組一覧 - 年表</h1>

		<div class=hatena-body>
		<div class=main>
		<div class=day>

			<div id="chart" style="width:1200px; height:600px"></div>
			<script type="text/javascript">
			function draw()
			{
				Highcharts.setOptions({ global: { useUTC: false } });
				options =
				{
					chart: {renderTo: "chart", zoomType:'xy', plotBackgroundColor: 'lightgray'},
					title: {text: 'ラジオ番組一覧 - 視聴日グラフ'},
					xAxis: {title: null, allowDecimals: false},
					yAxis: {type: 'datetime', title: {text: '視聴日'}, },
					plotOptions: {series:{marker:{enabled:true, radius:3}}},
					tooltip: { formatter: function () { const date = new Date(this.y); const y = date.getYear() + 1900; const m = date.getMonth()+1; const d = date.getDate(); return y + "/" + m + "/" + d; } },
					series: [
						{'name': 'オンエア日', 'type': 'line', 'data': <s:property value='onairDateList' escapeHtml='false' /> },
						{'name': '視聴日', 'type': 'scatter', 'lineWidth': 0, 'data': <s:property value='watchDateList' escapeHtml='false' /> }
					]
				};
				chart = new Highcharts.Chart(options);
			};
			document.body.onload = draw();
			</script>

		</div>
		</div>
		</div>

	</body>
</html>
