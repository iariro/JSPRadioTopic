package kumagai.radiotopic;

import java.sql.*;

/**
 * ラジオ番組内容DB
 * @author kumagai
 */
public class RadioTopicDatabase
{
	/**
	 * DBコネクションを取得
	 * @return DBコネクション
	 */
	static public Connection getConnection()
		throws SQLException
	{
		return
			DriverManager.getConnection
				("jdbc:sqlserver://localhost:2144;DatabaseName=RadioTopic;User=sa;Password=p@ssw0rd;");
	}
}
