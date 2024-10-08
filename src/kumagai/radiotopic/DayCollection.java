package kumagai.radiotopic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import ktool.datetime.DateTime;
import ktool.datetime.TimeSpan;

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

		Day nextListenDay = dayCollection.getNextListenDay("a", "2013/04/07-", new DateTime());
		System.out.printf("%s %s %s\n", nextListenDay.programName, nextListenDay.getNo(), nextListenDay.date.toString());

		connection.close();
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
	 * 日の番組ID更新
	 * @param connection DB接続オブジェクト
	 * @param dayId 日ID
	 * @param programId 番組ID
	 */
	static public void updateDayProgram
		(Connection connection, int dayId, int programId)
		throws SQLException
	{
		String sql = "update day set programid=? where id=?";

		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setInt(1, programId);
		statement.setInt(2, dayId);

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
			"select program.name, day.id, day.programid, day.date, day.createdate, day.updatedate, day.no, datediff(day, day.updatedate, getdate()) as diffday,(select COUNT(*) from image where dayid=Day.id) as imagenum from day join program on program.id=day.programid where datediff(day, day.updatedate, getdate()) <= ? order by day.updatedate desc";

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
	 * 画像レコード削除
	 * @param connection DB接続オブジェクト
	 * @param imageId 画像ID
	 */
	static public void deleteImage(Connection connection, int imageId)
		throws SQLException
	{
		PreparedStatement statement =
			connection.prepareStatement("delete image where id=?");

		statement.setInt(1, imageId);

		statement.executeUpdate();
	}

	/**
	 * 画像登録
	 * @param connection DB接続オブジェクト
	 * @param dayid 日ID
	 * @param filename ファイル名
	 * @return 発番ID
	 */
	static public int insertImage(Connection connection, int dayid, String filename)
		throws SQLException
	{
		PreparedStatement statement =
			connection.prepareStatement(
				"insert into Image (dayid, filename) values (?, ?)",
				Statement.RETURN_GENERATED_KEYS);

		statement.setInt(1, dayid);
		statement.setString(2, filename);
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
				// 失敗

				throw new SQLException();
			}
		}
		finally
		{
			keys.close();
			statement.close();
		}
	}

	/**
	 * １日分の画像情報を取得
	 * @param connection DB接続オブジェクト
	 * @param dayid 日ID
	 * @return １日分の画像情報
	 */
	static public ArrayList<Image> getDayImages(Connection connection, int dayid)
		throws SQLException
	{
		String sql = "select image.id, no, filename from image join Day on Day.id=image.dayid where dayid=?";

		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, dayid);
		ResultSet results = statement.executeQuery();

		try
		{
			ArrayList<Image> images = new ArrayList<>();

			while (results.next())
			{
				images.add(new Image(results));
			}

			return images;
		}
		finally
		{
			results.close();
			statement.close();
		}
	}

	/**
	 * １番組分の画像情報を取得
	 * @param connection DB接続オブジェクト
	 * @param programid 日ID
	 * @param sortOrder ソートオーダー
	 * @return １日分の画像情報
	 */
	static public ArrayList<Image> getProgramImages(Connection connection, int programid, SortOrder sortOrder)
		throws SQLException
	{
		String sql = "select image.id, no, filename from image join day on Day.id=image.dayid where programid=?";

		if (sortOrder == SortOrder.NumberByNumeric)
		{
			// 数値回数でソート

			sql += " order by cast(no as float) desc";
		}
		else
		{
			// 文字回数でソート

			sql += " order by no desc";
		}

		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, programid);
		ResultSet results = statement.executeQuery();

		try
		{
			ArrayList<Image> images = new ArrayList<>();

			while (results.next())
			{
				images.add(new Image(results));
			}

			return images;
		}
		finally
		{
			results.close();
			statement.close();
		}
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
	public DayCollection(Connection connection, int programid, SortOrder sortOrder) throws SQLException
	{
		this(connection, programid, sortOrder, "desc");
	}

	/**
	 * 回コレクションを構築
	 * @param connection DB接続オブジェクト
	 * @param programid 番組ID
	 * @param sortOrder ソートオーダー
	 * @param ascDesc ソート昇順／降順
	 */
	public DayCollection
		(Connection connection, int programid, SortOrder sortOrder, String ascDesc)
		throws SQLException
	{
		String sql =
			"select program.name, day.id, programid, date, no, createdate, updatedate,(select COUNT(*) from image where dayid=Day.id) as imagenum from day join program on program.id=day.programid where program.id=? ";

		if (sortOrder == SortOrder.NumberByNumeric)
		{
			sql += "order by convert(NUMERIC, no) " + ascDesc;
		}
		else if (sortOrder == SortOrder.NumberByText)
		{
			sql += "order by no " + ascDesc;
		}
		else if (sortOrder == SortOrder.Date)
		{
			sql += "order by date " + ascDesc;
		}

		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setInt(1, programid);

		ResultSet results = statement.executeQuery();

		while (results.next())
		{
			add(new Day(results));
		}

		results.close();
		statement.close();
	}

	/**
	 * 過去放送日から次回放送日を算出。終了していない番組に限る。
	 * @param dayCollection 放送日コレクション
	 * @param age 放送時期
	 * @param today 今日の日付
	 * @return 次回放送日
	 */
	public Day getNextListenDay(String programName, String age, DateTime today)
	{
		if (age == null)
		{
			// 放送時期の指定なし

			return null;
		}

		String [] ageDates = age.split("-");

		if (ageDates.length >= 2)
		{
			// 既に終了している番組

			return null;
		}

		if (size() < 2)
		{
			// ２件もない

			return null;
		}

		if (get(0).date == null || get(1).date == null)
		{
			// 日付が揃ってない

			return null;
		}

		DateTime day1 = new DateTime(get(0).date);
		DateTime day2 = new DateTime(get(1).date);
		TimeSpan timespan = day1.diff(day2);

		try
		{
			int no1 = Integer.valueOf(get(0).getNo());
			int no2 = Integer.valueOf(get(1).getNo());

			timespan = new TimeSpan(timespan.getTotalMillisecond() / (no1 - no2));

			DateTime day0 = day1.makeAdd(timespan);

			if (day0.compareTo(today) < 0)
			{
				// 予測放送日は過去の日

				return new Day(programName, no1 + 1, day0, null, null);
			}
			else
			{
				// 予測放送日は過去の日ではない

				return null;
			}
		}
		catch (Exception exception)
		{
			return null;
		}
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
}
