package kumagai.radiotopic.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import kumagai.radiotopic.DayCollection;
import kumagai.radiotopic.RadioTopicDatabase;

public class InsertDateTest
{
	public static void main(String[] args)
		throws SQLException
	{
		DriverManager.registerDriver(new SQLServerDriver());

		Connection connection = RadioTopicDatabase.getConnection();

		int newId = DayCollection.insertDay(connection, 1, "2015/08/08", "70");

		System.out.println(newId);

		connection.close();
	}
}
