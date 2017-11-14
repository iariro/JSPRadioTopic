package kumagai.radiotopic.test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import kumagai.radiotopic.ProgramCollection;
import kumagai.radiotopic.RadioTopicDatabase;
import kumagai.radiotopic.SearchTopicResult;

public class ProgramCollectionTest
{
	public static void main(String[] args)
		throws SQLException
	{
		DriverManager.registerDriver(new SQLServerDriver());

		Connection connection = RadioTopicDatabase.getConnection();

		ArrayList<SearchTopicResult> searchTopicResults =
			ProgramCollection.searchAllProgram(connection, "髪", Date.valueOf("2017/11/01"));
		for (SearchTopicResult searchTopicResult : searchTopicResults)
		{
			System.out.println(searchTopicResult);
		}
		connection.close();
	}
}
