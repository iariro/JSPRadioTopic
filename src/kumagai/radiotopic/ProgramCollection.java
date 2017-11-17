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

/**
 * 番組データコレクション
 * @author kumagai
 */
public class ProgramCollection
	extends ArrayList<Program>
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

		ProgramCollection programCollection = new ProgramCollection(connection);

		DateTime today = new DateTime();
		for (Program program : programCollection)
		{
			System.out.println(program);
			DayCollection dayCollection =
				new DayCollection
					(connection, program.id, SortOrder.values()[program.sortOrder]);

			Day nextListenDay =
				dayCollection.getNextListenDay(program.name, program.age, today);

			if (nextListenDay != null)
			{
				// 次回放送日表示あり

				System.out.println(nextListenDay);
			}
		}

		connection.close();
	}

	/**
	 * 番組登録
	 * @param connection DB接続オブジェクト
	 * @param name 番組名
	 * @param sortOrder ソートオーダー
	 * @return 登録したレコードのID
	 */
	static public int insertProgram
		(Connection connection, String name, int sortOrder)
		throws SQLException
	{
		PreparedStatement statement =
			connection.prepareStatement(
				"insert into program (name, sortorder) values (?, ?)",
				Statement.RETURN_GENERATED_KEYS);

		statement.setString(1, name);
		statement.setInt(2, sortOrder);
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
	 * 番組情報更新
	 * @param connection DB接続オブジェクト
	 * @param programid 番組ID
	 * @param name 番組名
	 * @param shortname エクスポートファイル名用番組名
	 * @param age オンエア時期情報
	 * @param sortOrder ソートオーダー
	 * @param originUpdateDate 最初期の入力日付に使用する日付
	 */
	static public void updateProgram(Connection connection, int programid,
		String name, String shortname, String age, int sortOrder,
		String originUpdateDate)
		throws SQLException
	{
		String sql = "update program set name=?, shortname=?, age=?, sortOrder=?, originupdatedate=? where id=?";

		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setString(1, name);
		statement.setString(2, shortname);
		statement.setString(3, age);
		statement.setInt(4, sortOrder);
		statement.setString(5, originUpdateDate);
		statement.setInt(6, programid);

		statement.executeUpdate();

		statement.close();
	}

	/**
	 * 番組名で番組ID取得
	 * @param connection DB接続オブジェクト
	 * @param programName 番組名
	 * @return 番組ID
	 */
	static public int getProgramId(Connection connection, String programName)
		throws SQLException
	{
		String sql = "select id from program where name=?";

		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setString(1, programName);

		ResultSet results = statement.executeQuery();

		try
		{
			if (results.next())
			{
				return results.getInt("id");
			}
			else
			{
				return -1;
			}
		}
		finally
		{
			results.close();
			statement.close();
		}
	}

	/**
	 * 全番組対象にキーワード検索
	 * @param connection DB接続オブジェクト
	 * @param keyword 検索キーワード
	 * @param startdate 検索開始日
	 * @return 検索結果コレクション
	 */
	static public ArrayList<SearchTopicResult> searchAllProgram(Connection connection, String keyword, DateTime startdate)
		throws SQLException
	{
		String sql = "select name,no,date,text,updatedate from topic join Day on day.id=Topic.dayid join Program on Program.id=day.programid where text like ? and updatedate>=?";

		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setString(1, String.format("%%%s%%", keyword));
		statement.setDate(2, new java.sql.Date(startdate.getDate().getTime()));

		ResultSet results = statement.executeQuery();

		ArrayList<SearchTopicResult> searchTopicResults = new ArrayList<>();
		try
		{
			while (results.next())
			{
				searchTopicResults.add(new SearchTopicResult(results));
			}
		}
		finally
		{
			results.close();
			statement.close();
		}

		return searchTopicResults;
	}

	/**
	 * 日付チェック。全件１DBアクセスで取得し速い。
	 * @param connection DB接続オブジェクト
	 * @return チェック結果文字列
	 */
	static public ArrayList<String> checkDateFast(Connection connection)
		throws SQLException
	{
		String sql = "select program.name, day.id, programid, date, no, createdate, updatedate from day join program on program.id=day.programid order by programid, convert(NUMERIC, no) desc";

		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet results = statement.executeQuery();

		Day pday = null;
		ArrayList<String> invalidDates = new ArrayList<String>();
		while (results.next())
		{
			Day day = new Day(results);
			if (pday != null)
			{
				// 比較する回あり

				if (day.programid != pday.programid)
				{
					// 違う番組

					pday = null;
				}

				if (day != null && day.date != null && pday != null && pday.date != null)
				{
					// 日情報はそろっている

					if (day.date.compareTo(pday.date) >= 0)
					{
						// 新しいはずの日の方が古い・または同じ日

						invalidDates.add(
							String.format(
								"%sの%s:%sと%s:%sの前後関係が異常",
								day.programName,
								day.getNo(),
								day.getDate(),
								pday.getNo(),
								pday.getDate()));
					}
				}
			}
			pday = day;
		}

		results.close();
		statement.close();

		return invalidDates;
	}

	/**
	 * 空コレクションの構築
	 */
	public ProgramCollection()
	{
		// 何もしない
	}

	/**
	 * 番組コレクションを構築
	 * @param connection DB接続オブジェクト
	 */
	public ProgramCollection(Connection connection)
		throws SQLException
	{
		String sql = "select program.id, program.name, program.shortname, program.sortorder, program.exportformat, program.originupdatedate, max(updatedate) as maxupdatedate, age from program left join day on day.programid=program.id group by program.id, program.name, program.shortname, program.sortorder, program.exportformat, program.originupdatedate, age order by MAX(updatedate) desc";

		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet results = statement.executeQuery();

		while (results.next())
		{
			add(new Program(results));
		}

		results.close();
		statement.close();
	}
}
