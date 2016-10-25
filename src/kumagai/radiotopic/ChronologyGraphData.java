package kumagai.radiotopic;

import java.text.*;
import java.util.*;
import ktool.datetime.*;

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
	public float scaleX;
	public float scaleY;
	public DateTime max = null;
	public DateTime min = null;
	public TimeSpan dayRange;

	/**
	 * 番組情報コレクションから年表グラフデータを構築。
	 * @param programCollection 番組情報コレクション
	 * @param width 横幅
	 * @param height 縦幅
	 */
	public ChronologyGraphData
		(ProgramCollection programCollection, int width, int height)
		throws ParseException
	{
		this.width = width;
		this.height = height;

		for (Program program : programCollection)
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
		scaleY = (float)(height - headerHeight) / (float)programCollection.size();

		for (int i=0 ; i<programCollection.size() ; i++)
		{
			Program program = programCollection.get(i);

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
