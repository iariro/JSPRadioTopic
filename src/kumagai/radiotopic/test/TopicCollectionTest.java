package kumagai.radiotopic.test;

import java.sql.*;
import java.util.*;
import kumagai.radiotopic.*;

import com.microsoft.sqlserver.jdbc.*;

public class TopicCollectionTest
{
	public static void main(String[] args)
		throws SQLException
	{
		DriverManager.registerDriver(new SQLServerDriver());

		Connection connection = RadioTopicDatabase.getConnection();

		ArrayList<DayDigest> days = DayCollection.getTochuuTopic(connection);

		for (DayDigest day : days)
		{
			System.out.println(day);
		}

		connection.close();
	}
}
