package kumagai.radiotopic.test;

import java.sql.*;
import kumagai.radiotopic.*;
import com.microsoft.sqlserver.jdbc.*;

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
