package kumagai.radiotopic;

import java.text.ParseException;
import java.util.ArrayList;

import ktool.datetime.DateTime;
import ktool.datetime.TimeSpan;

/**
 * 年表グラフデータ。
 * @author kumagai
 */
public class ChronologyGraphData
	extends ArrayList<ChronologyGraphDataElement>
{
	static private int headerHeight = 20;

	public int width;
	public int height;
	public double scaleX;
	public double scaleY;
	public DateTime max = null;
	public DateTime min = null;
	public TimeSpan dayRange;

	/**
	 * 番組情報コレクションから年表グラフデータを構築。
	 * @param programCollection 番組情報コレクション
	 * @param width 横幅
	 * @param height 縦幅
	 * @param startYear グラフ対象開始年
	 */
	public ChronologyGraphData
		(ProgramCollection programCollection, int width, int height, Integer startYear)
		throws ParseException
	{
		this.width = width;
		this.height = height;

		// 期間で絞込み
		ProgramCollection programCollection2 = new ProgramCollection();
		for (Program program : programCollection)
		{
			String [] ageField = program.age.split("-");
			if (DateTime.parseDateString(ageField[0]).getYear() >= startYear)
			{
				programCollection2.add(program);
			}
		}

		for (Program program : programCollection2)
		{
			DateTime start = null;
			DateTime end = null;

			start = program.getStartDate();

			if (start != null)
			{
				// 開始日あり

				if (min == null || start.compareTo(min) < 0)
				{
					// 最小値なしまたは開始日が下回る

					min = start;
				}
			}

			end = program.getEndDate();

			if (end != null)
			{
				// 終了日あり

				if (max == null || end.compareTo(max) > 0)
				{
					// 最大値なしまたは終了日が上回る

					max = end;
				}
			}
			else
			{
				// 終点なし

				max = new DateTime();
			}
		}

		dayRange = max.diff(min);
		scaleX = (float)width / (float)dayRange.getDay();
		scaleY = Math.floor((double)(height - headerHeight) / (double)programCollection2.size());

		for (int i=0 ; i<programCollection2.size() ; i++)
		{
			Program program = programCollection2.get(i);

			DateTime start = program.getStartDate();
			DateTime end = program.getEndDate();

			if (start != null)
			{
				// 開始日あり

				if (end == null)
				{
					// 終了日なし

					end = new DateTime();
					end.addDay(20);
				}

				add(
					new ChronologyGraphDataElement(
						program.name,
						program.shortname,
						program.id,
						program.sortOrder,
						(int)(start.diff(min).getDay() * scaleX),
						(int)(i * scaleY) + headerHeight,
						(int)(end.diff(start).getDay() * scaleX),
						(int)scaleY));
			}
		}
	}
}
