package kumagai.radiotopic.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import kumagai.radiotopic.DayCollection;
import kumagai.radiotopic.DayDigest;
import kumagai.radiotopic.RadioTopicDatabase;

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
