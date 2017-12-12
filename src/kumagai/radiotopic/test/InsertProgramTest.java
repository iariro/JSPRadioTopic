package kumagai.radiotopic.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import kumagai.radiotopic.ProgramCollection;
import kumagai.radiotopic.RadioTopicDatabase;

public class InsertProgramTest
{
	static public void main(String[] args)
		throws SQLException
	{
		DriverManager.registerDriver(new SQLServerDriver());

		Connection connection = RadioTopicDatabase.getConnection();

		int newId = ProgramCollection.insertProgram(connection, "あどりぶ", "adlib", "2015/04/01-", 0, "2016/01/01");

		System.out.println(newId);

		connection.close();
	}
}
