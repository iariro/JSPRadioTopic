package kumagai.radiotopic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ktool.datetime.DateTime;

/**
 * 回情報のコレクション
 * @author kumagai
 */
public class DayAndTopicCollection
	extends ArrayList<DayAndTopic>
{
	/**
	 * テスト用コンストラクタ
	 */
	public DayAndTopicCollection()
	{
		// 何もしない
	}

	/**
	 * 回コレクションを構築
	 * @param connection DB接続オブジェクト
	 * @param programid 番組ID
	 * @param sortOrder ソートオーダー
	 */
	public DayAndTopicCollection
		(Connection connection, int programid, SortOrder sortOrder)
		throws SQLException
	{
		String sql =
			"select program.name, day.id, programid, date, no, createdate, updatedate from day join program on program.id=day.programid where program.id=? ";

		if (sortOrder == SortOrder.NumberByNumeric)
		{
			sql += "order by convert(NUMERIC, no) desc";
		}
		else if (sortOrder == SortOrder.NumberByText)
		{
			sql += "order by no desc";
		}
		else if (sortOrder == SortOrder.Date)
		{
			sql += "order by date desc";
		}

		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setInt(1, programid);

		ResultSet results = statement.executeQuery();

		while (results.next())
		{
			add(new DayAndTopic(results));
		}

		results.close();

		for (DayAndTopic day : this)
		{
			day.topicCollection = new TopicCollection(connection, day.id);
		}

		statement.close();
	}

	/**
	 * 最大回数を取得。０回がある場合１回増やす。
	 * @return 最大回数
	 */
	public int getMaxNo()
	{
		int maxNo = 0;
		boolean zero = false;

		for (int i=0 ; i<size() ; i++)
		{
			try
			{
				int intno = Integer.valueOf(get(i).getNo());

				if (maxNo < intno)
				{
					// 最大を上回る

					maxNo = intno;
				}

				if (intno == 0)
				{
					// ゼロ回

					zero = true;
				}
			}
			catch (NumberFormatException exception)
			{
				// 何もしない
			}
		}

		if (zero)
		{
			// ゼロ回あり

			maxNo++;
		}

		return maxNo;
	}

	/**
	 * 最終更新日時を求める。
	 * @return 最終更新日時
	 */
	public DateTime getLastUpdate()
	{
		DateTime last = null;

		for (Day day : this)
		{
			if (day.updatedate != null)
			{
				// 更新日付あり

				if (last == null || last.compareTo(day.updatedate) < 0)
				{
					// 初回か、上回る日付であったとき

					last = day.updatedate;
				}
			}
		}

		return last;
	}

	/**
	 * 全トピック文字列を内容とするJavaScriptの文字列配列を生成。
	 * @return JavaScriptの文字列配列
	 */
	public String createJavaScriptArray()
	{
		StringBuilder topicsArray = new StringBuilder();
		topicsArray.append("[");

		for (int i=0 ; i<size() ; i++)
		{
			if (i > 0)
			{
				// ２個目以降

				topicsArray.append(",");
			}

			DayAndTopic day = get(i);
			topicsArray.append("[");
			if (day.topicCollection != null)
			{
				for (int j=0 ; j<day.topicCollection.size() ; j++)
				{
					if (j > 0)
					{
						// ２個目以降

						topicsArray.append(",");
					}

					String text = day.topicCollection.get(j).text;
					text = text.replace("'", "\\'");
					topicsArray.append("'" + text + "'");
				}
			}
			topicsArray.append("]");
		}

		topicsArray.append("]");

		return topicsArray.toString();
	}
}
