package kumagai.radiotopic;

import java.sql.*;
import java.util.*;
import com.microsoft.sqlserver.jdbc.*;

/**
 * トピック情報のコレクション
 * @author kumagai
 */
public class TopicCollection
	extends ArrayList<Topic>
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

		TopicCollection topicCollection = new TopicCollection(connection, 74);

		for (Topic topic : topicCollection)
		{
			System.out.println(topic.text);
		}

		connection.close();
	}

	/**
	 * 回登録
	 * @param connection DB接続オブジェクト
	 * @param dayid 回ID
	 * @param text テキスト
	 * @return 登録したレコードのID
	 */
	static public int insertTopic(Connection connection, int dayid, String text)
		throws SQLException
	{
		PreparedStatement statement =
			connection.prepareStatement(
				"insert into Topic (dayid, text) values (?, ?)",
				Statement.RETURN_GENERATED_KEYS);

		statement.setInt(1, dayid);
		statement.setString(2, text);
		statement.executeUpdate();

		int newId;

		ResultSet keys = statement.getGeneratedKeys();
		try
		{
			if (keys.next())
			{
				// 成功

				newId = keys.getInt(1);

				DayCollection.updateUpdateDate(connection, dayid);

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
	 * トピック更新
	 * @param connection DB接続オブジェクト
	 * @param dayid 日ID
	 * @param topicid トピックID
	 * @param text テキスト
	 */
	static public void updateTopic
		(Connection connection, int dayid, int topicid, String text)
		throws SQLException
	{
		String sql = "update Topic set text=? where id=?";

		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setString(1, text);
		statement.setInt(2, topicid);

		statement.executeUpdate();

		DayCollection.updateUpdateDate(connection, dayid);

		statement.close();
	}

	/**
	 * トピック削除
	 * @param connection DB接続オブジェクト
	 * @param dayid 日ID
	 * @param topicid トピックID
	 */
	static public void deleteTopic
		(Connection connection, int dayid, int topicid)
		throws SQLException
	{
		String sql = "delete Topic where id=?";

		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setInt(1, topicid);

		statement.executeUpdate();

		DayCollection.updateUpdateDate(connection, dayid);

		statement.close();
	}

	/**
	 * トピックテキスト取得
	 * @param connection DB接続オブジェクト
	 * @param topicid トピックID
	 * @return トピックテキスト
	 */
	static public String getTopic(Connection connection, int topicid)
		throws SQLException
	{
		String sql = "select text from Topic where id=?";

		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setInt(1, topicid);

		ResultSet results = statement.executeQuery();

		try
		{
			if (results.next())
			{
				return results.getString("text");
			}
			else
			{
				throw new SQLException();
			}
		}
		finally
		{
			results.close();
			statement.close();
		}
	}

	/**
	 * デバッグ用。
	 */
	public TopicCollection()
	{
		// 何もしない
	}

	/**
	 * 回コレクションを構築
	 * @param connection DB接続オブジェクト
	 * @param dayid 回ID
	 */
	public TopicCollection(Connection connection, int dayid)
		throws SQLException
	{
		String sql =
			"select topic.id, dayid, text from Topic join day on day.id=topic.dayid where day.id=? order by topic.id";

		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setInt(1, dayid);

		ResultSet results = statement.executeQuery();

		while (results.next())
		{
			add(new Topic(results));
		}

		results.close();
		statement.close();
	}

	/**
	 * 回コレクションを構築
	 * @param connection DB接続オブジェクト
	 * @param dayid 回ID
	 */
	public TopicCollection(Connection connection)
		throws SQLException
	{
		String sql =
			"select topic.id, dayid, text from Topic join day on day.id=topic.dayid order by topic.id";

		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet results = statement.executeQuery();

		while (results.next())
		{
			add(new Topic(results));
		}

		results.close();
		statement.close();
	}

	/**
	 * 半角個数で文字列長を求める
	 * @return 半角個数での文字列長
	 */
	public int getLengthByHalfWidth()
	{
		int length = 0;

		for (int i=0 ; i<size() ; i++)
		{
			if (i > 0)
			{
				// ２個目以降

				length++;
			}

			length += StringTool.getLengthByHalfWidth(get(i).text);
		}

		return length;
	}

	/**
	 * 全トピックをスペース区切りで取得。
	 * @return スペース区切りの全トピック文字列
	 */
	public String concatAllTopics()
	{
		StringBuilder topics = new StringBuilder();

		for (int i=0 ; i<size() ; i++)
		{
			if (i > 0)
			{
				// ２個目以降

				topics.append(' ');
			}

			topics.append(get(i).text);
		}

		return topics.toString();
	}
}
