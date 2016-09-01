package kumagai.radiotopic;

import java.sql.*;
import com.microsoft.sqlserver.jdbc.*;

/**
 * トピック一覧表示
 */
public class TopicList
{
	/**
	 * トピック一覧表示
	 * @param args 未使用
	 */
	public static void main(String[] args)
		throws SQLException
	{
		DriverManager.registerDriver(new SQLServerDriver());

		Connection connection = RadioTopicDatabase.getConnection();

		TopicCollection topicCollection = new TopicCollection(connection);

		connection.close();

		int id = 1;

		for (Topic topic : topicCollection)
		{
			for (int i=id ; i<topic.id ; i++)
			{
				System.out.printf("%d\n", i);
			}

			System.out.printf("%d %s\n", topic.id, topic.text);

			id = topic.id + 1;
		}
	}

}
