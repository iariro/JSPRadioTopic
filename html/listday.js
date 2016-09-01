
/**
 * 指定のキーワードで検索する
 */
function searchKeyword(keywordControl)
{
	var i;
	var update;
	var text;
	var table = document.getElementById('sorter');
	var keyword = keywordControl.value;

	for (i=1 ; i < table.rows.length ; i++)
	{
		update = false;
		text = table.rows[i].cells[2].innerHTML;

		if (text.indexOf('<span class=\"highlight\">') >= 0 &&
			text.indexOf('</span>') >= 0)
		{
			text = text.replace(new RegExp('<span class=\"highlight\">', 'g'), '');
			text = text.replace(new RegExp('</span>', 'g'), '');
			update = true;
		}

		if (text.indexOf(keyword) >= 0)
		{
			text = text.replace(new RegExp(keyword, 'g'), '<span class=\"highlight\">' + keyword + '</span>');
			update = true;
		}
		console.log(update + ' ' + table.rows[i].cells[2].innerHTML);

		if (update)
		{
			table.rows[i].cells[2].innerHTML = text;
		}
	}
}
