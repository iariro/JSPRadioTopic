package kumagai.radiotopic.test;

import java.sql.*;
import kumagai.radiotopic.*;
import com.microsoft.sqlserver.jdbc.*;

public class DeleteTopicTest
{
	public static void main(String[] args)
		throws SQLException
	{
		DriverManager.registerDriver(new SQLServerDriver());

		Connection connection = RadioTopicDatabase.getConnection();

		TopicCollection.deleteTopic(connection, 1, 2);

		connection.close();
	}
}
