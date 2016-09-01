package kumagai.radiotopic.test;

import java.sql.*;
import kumagai.radiotopic.*;
import com.microsoft.sqlserver.jdbc.*;

public class InsertTopicTest
{
	public static void main(String[] args) throws SQLException
	{
		DriverManager.registerDriver(new SQLServerDriver());

		Connection connection = RadioTopicDatabase.getConnection();

		int newId = TopicCollection.insertTopic(connection, 1, "abc");

		System.out.println(newId);

		connection.close();
	}
}
