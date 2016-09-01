package kumagai.radiotopic.test;

import java.sql.*;
import kumagai.radiotopic.*;
import com.microsoft.sqlserver.jdbc.*;

public class InsertProgramTest
{
	static public void main(String[] args)
		throws SQLException
	{
		DriverManager.registerDriver(new SQLServerDriver());

		Connection connection = RadioTopicDatabase.getConnection();

		int newId = ProgramCollection.insertProgram(connection, "あどりぶ", 0);

		System.out.println(newId);

		connection.close();
	}
}
