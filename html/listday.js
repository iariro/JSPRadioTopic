
/**
 * 指定のHTML文字列の<span>タグによるハイライト加工
 */
function highlight(text, keyword)
{
	if (text.indexOf('<span class=\"highlight\">') >= 0 &&
		text.indexOf('</span>') >= 0)
	{
		text = text.replace(new RegExp('<span class=\"highlight\">', 'g'), '');
		text = text.replace(new RegExp('</span>', 'g'), '');
	}

	if ((keyword.length > 0) && (text.indexOf(keyword) >= 0))
	{
		text = text.replace(new RegExp(keyword, 'g'), '<span class=\"highlight\">' + keyword + '</span>');
	}

	return text;
}

/**
 * 指定のキーワードで検索する
 */
function searchKeyword(keywordControl)
{
	var i;
	var text, text2;
	var table = document.getElementById('sorter');
	var keyword = keywordControl.value;

	for (i=1 ; i < table.rows.length ; i++)
	{
		text = table.rows[i].cells[2].innerHTML;
		text2 = highlight(text, keyword);

		if (text != text2)
		{
			table.rows[i].cells[2].innerHTML = text;
		}
	}
}
