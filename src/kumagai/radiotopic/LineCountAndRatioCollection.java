package kumagai.radiotopic;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 文字列長カウント積み上げグラフ用データ
 * @author kumagai
 */
public class LineCountAndRatioCollection
	extends ArrayList<LineCountAndRatio>
{
	public int maxLength;
	public int minLength;

	/**
	 * １番組情報を受け、文字列長カウント積み上げグラフ用データを構築する。
	 * @param connection DB接続オブジェクト
	 * @param dayCollection １番組情報
	 */
	public LineCountAndRatioCollection(Connection connection, DayCollection dayCollection)
		throws SQLException
	{
		Integer maxLength = null;
		Integer minLength = null;

		HashMap<Integer, Integer> lengthStat = new HashMap<Integer, Integer>();

		for (Day day : dayCollection)
		{
			TopicCollection topicCollection = new TopicCollection(connection, day.id);

			int length = topicCollection.getLengthByHalfWidth();

			if (lengthStat.containsKey(length))
			{
				lengthStat.put(length, lengthStat.get(length) + 1);
			}
			else
			{
				lengthStat.put(length, 1);
			}

			if (maxLength == null || maxLength < length)
			{
				maxLength = length;
			}

			if (minLength == null || minLength > length)
			{
				minLength = length;
			}
		}

		float sum = 0;
		int range = maxLength - minLength + 1;

		for (int i=0 ; i<range ; i++)
		{
			int count = 0;

			if (lengthStat.containsKey(maxLength - i))
			{
				count = lengthStat.get(maxLength - i);
				sum += count;
			}

			add(new LineCountAndRatio(count, sum / dayCollection.size()));
		}

		this.maxLength = maxLength;
		this.minLength = minLength;
	}

	/**
	 * 最大のカウント数を求める。
	 * @return 最大のカウント数
	 */
	public int getMaxCount()
	{
		int max = 0;

		for (LineCountAndRatio lineCountAndRatio : this)
		{
			if (max < lineCountAndRatio.count)
			{
				max = lineCountAndRatio.count;
			}
		}

		return max;
	}
}
