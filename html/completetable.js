
var slidevalue;

function updateTable()
{
	var i, j, k;
	var id;
	var cell;
	var draw;
	var count = 0;
	var usedid = {};
	var className;

	var step = document.getElementById('range');
	var date = document.getElementById('date');

	if (slidevalue != step.value)
	{
		// 値が変わった

		for (i=0 ; i<ids.length ; i++)
		{
			for (j=0 ; j<2 ; j++)
			{
				for (k=0 ; k<ids[i][j].length ; k++)
				{
					id = 'cell' + ids[i][j][k];
					cell = document.getElementById(id);

					if (cell != undefined)
					{
						// 要素あり

						draw = true;

						if (i < step.value)
						{
							// スライダーの示す範囲内である

							if (j == 0)
							{
								// 登録日

								className = 'create';
							}
							else if (j == 1)
							{
								// 更新日

								className = 'update';
							}
						}
						else
						{
							// スライダーの示す範囲外である

							className = 'notouch';

							// 既出でなければ対象とする
							draw = !usedid[id];
						}

						if (draw)
						{
							// 色を変える

							cell.className = className;
						}

						if ((i < step.value) && (! usedid[id]))
						{
							// スライダーの示す範囲内であり初出である

							count++;
						}

						usedid[id] = true;
					}
				}
			}
		}

		if (step.value > 0)
		{
			// ゼロではない

			date.innerHTML =
				dates[step.value - 1] + ' ' +
				count + '/' +
				maxNo + '=' +
				((count * 100) / maxNo).toFixed(2) + '%';
		}
		else
		{
			// ゼロである

			date.innerHTML = '-';
		}
	}

	slidevalue = step.value;
}
