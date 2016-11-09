package kumagai.radiotopic;

import java.sql.*;
import java.util.*;
import com.microsoft.sqlserver.jdbc.*;
import ktool.datetime.*;

/**
 * 回情報のコレクション
 * @author kumagai
 */
public class DayCollection
	extends ArrayList<Day>
{
	/**
	 * テストプログラム
	 * @param args 未使用
	 */
	static public void main(String[] args)
		throws SQLException
	{
		DriverManager.registerDriver(new SQLServerDriver());

		Connection connection = RadioTopicDatabase.getConnection();

		DayCollection dayCollection =
			new DayCollection(connection, 29, SortOrder.NumberByNumeric);

		for (Day day : dayCollection)
		{
			System.out.println(day);
		}

		Day nextListenDay = DayCollection.getNextListenDay("a", dayCollection, "2013/04/07-", new DateTime());
		System.out.printf("%s %s %s\n", nextListenDay.programName, nextListenDay.getNo(), nextListenDay.date.toString());

		connection.close();
	}

	/**
	 * 過去放送日から次回放送日を算出。終了していない番組に限る。
	 * @param dayCollection 放送日コレクション
	 * @param age 放送時期
	 * @param today 今日の日付
	 * @return 次回放送日
	 */
	static public Day getNextListenDay
		(String programName, DayCollection dayCollection, String age, DateTime today)
	{
		String [] ageDates = age.split("-");

		if (ageDates.length >= 2)
		{
			// 既に終了している番組

			return null;
		}

		if (dayCollection.size() < 2)
		{
			// ２件もない

			return null;
		}

		if (dayCollection.get(0).date == null || dayCollection.get(1).date == null)
		{
			// 日付が揃ってない

			return null;
		}

		DateTime day1 = new DateTime(dayCollection.get(0).date);
		DateTime day2 = new DateTime(dayCollection.get(1).date);
		TimeSpan timespan = day1.diff(day2);
		DateTime day0 = day1.makeAdd(timespan);

		if (day0.compareTo(today) < 0)
		{
			// 予測放送日は過去の日

			int nextNo = Integer.valueOf(dayCollection.get(0).getNo()) + 1;

			return new Day(programName, nextNo, day0, null, null);
		}
		else
		{
			// 予測放送日は過去の日ではない

			return null;
		}
	}

	/**
	 * 回登録
	 * @param connection DB接続オブジェクト
	 * @param programid 番組ID
	 * @param date 日付
	 * @param no 回数
	 * @return 登録したレコードのID
	 */
	static public int insertDay
		(Connection connection, int programid, String date, String no)
		throws SQLException
	{
		PreparedStatement statement =
			connection.prepareStatement(
				"insert into Day (programid, date, no, updatedate, createdate) values (?, ?, ?, getdate(), getdate())",
				Statement.RETURN_GENERATED_KEYS);

		if (date != null && date.isEmpty())
		{
			// 空文字列

			date = null;
		}

		statement.setInt(1, programid);
		statement.setString(2, date);
		statement.setString(3, no.trim());
		statement.executeUpdate();

		int newId;

		ResultSet keys = statement.getGeneratedKeys();
		try
		{
			if (keys.next())
			{
				// 成功

				newId = keys.getInt(1);

				return newId;
			}
			else
			{
				throw new SQLException();
			}
		}
		finally
		{
			keys.close();
		}
	}

	/**
	 * 日情報更新
	 * @param connection DB接続オブジェクト
	 * @param dayid 日ID
	 * @param no 回
	 * @param date 日付
	 */
	static public void updateDay
		(Connection connection, int dayid, String no, String date)
		throws SQLException
	{
		String sql = "update day set no=?, date=?, updatedate=getdate() where id=?";

		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setString(1, no);
		statement.setString(2, date);
		statement.setInt(3, dayid);

		statement.executeUpdate();

		statement.close();
	}

	/**
	 * 日情報更新
	 * @param connection DB接続オブジェクト
	 * @param dayid 日ID
	 */
	static public void updateUpdateDate(Connection connection, int dayid)
		throws SQLException
	{
		String sql = "update day set updatedate=getdate() where id=?";

		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setInt(1, dayid);

		statement.executeUpdate();

		statement.close();
	}

	/**
	 * 入力途中の日情報を取得
	 * @param connection DB接続オブジェクト
	 * @return 入力途中の日情報
	 */
	static public ArrayList<DayDigest> getTochuuTopic(Connection connection)
		throws SQLException
	{
		String sql = "select program.name, day.date, day.no from topic join day on day.id=topic.dayid join program on program.id=day.programid where Topic.text='途中'";

		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet results = statement.executeQuery();

		String sql2 = "select program.name, day.date, day.no from Day join program on program.id=day.programid where day.id not in (select dayid from Topic)";

		PreparedStatement statement2 = connection.prepareStatement(sql2);

		ResultSet results2 = statement2.executeQuery();

		try
		{
			ArrayList<DayDigest> days = new ArrayList<DayDigest>();

			while (results.next())
			{
				days.add(new DayDigest(results));
			}

			while (results2.next())
			{
				days.add(new DayDigest(results2));
			}

			return days;
		}
		finally
		{
			results.close();
			statement.close();
			results2.close();
			statement2.close();
		}
	}

	/**
	 * 最近の更新を取得
	 * @param connection DB接続オブジェクト
	 * @param dayNum 取得する日数
	 */
	static public ArrayList<ArrayList<Day>> getRecentUpdateDays
		(Connection connection, int dayNum)
		throws SQLException
	{
		String sql =
			"select program.name, day.id, day.programid, day.date, day.createdate, day.updatedate, day.no, datediff(day, day.updatedate, getdate()) as diffday from day join program on program.id=day.programid where datediff(day, day.updatedate, getdate()) <= ? order by day.updatedate desc";

		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setInt(1, dayNum);

		ResultSet results = statement.executeQuery();

		ArrayList<ArrayList<Day>> daysCollection =
			new ArrayList<ArrayList<Day>>();

		int diffDay = -1;
		ArrayList<Day> days = null;

		while (results.next())
		{
			int diffDay2 = results.getInt("diffday");

			if (diffDay != diffDay2)
			{
				// 日の変わり目

				days = new ArrayList<Day>();
				daysCollection.add(days);
			}

			days.add(new Day(results));

			diffDay = diffDay2;
		}

		results.close();
		statement.close();

		return daysCollection;
	}

	/**
	 * テスト用コンストラクタ
	 */
	public DayCollection()
	{
		// 何もしない
	}

	/**
	 * 回コレクションを構築
	 * @param connection DB接続オブジェクト
	 * @param programid 番組ID
	 * @param sortOrder ソートオーダー
	 */
	public DayCollection
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
			add(new Day(results));
		}

		results.close();

		for (Day day : this)
		{
			day.topicCollection = new TopicCollection(connection, day.id);
		}

		statement.close();
	}

	/**
	 * 指定の日付の時点のエントリ数を求める。
	 * @param date 日付
	 * @return 指定の日付の範囲外のエントリ数
	 */
	public int getOverDateCount(String date)
	{
		int count = 0;

		for (int i=0 ; i<size() ; i++)
		{
			if (get(i).getDate() != null)
			{
				// 日付あり

				if (get(i).getDate().compareTo(date) > 0)
				{
					// 日付は指定の範囲内である

					count++;
				}
			}
		}

		return count;
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
	 * 最初・最後の更新の日数を取得。
	 * @return 最初・最後の更新の日数
	 */
	public int getUpdateRange()
	{
		DateTime min = null;
		DateTime max = null;

		for (Day day : this)
		{
			if (day.updatedate != null)
			{
				// 更新日付あり

				if (min == null || (min.compareTo(day.updatedate)) > 0)
				{
					// 初回か、現在の最小を下回る

					min = day.updatedate;
				}

				if (max == null || (max.compareTo(day.updatedate)) < 0)
				{
					// 初回か、現在の最大を上回る

					max = day.updatedate;
				}
			}
		}

		if (min != null && max != null)
		{
			// 最小値・最大値がある

			return max.diff(min).getDay();
		}
		else
		{
			// 最小値・最大値がない

			return 0;
		}
	}

	/**
	 * 最初・最後の更新の日数を取得。
	 * @return 最初・最後の更新の日数
	 */
	public int getUpdateCount()
	{
		int count = 0;

		for (Day day : this)
		{
			if (day.updatedate != null)
			{
				// 更新日付あり

				count++;
			}
		}

		return count;
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

			Day day = get(i);
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
