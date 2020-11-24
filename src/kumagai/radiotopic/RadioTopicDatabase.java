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
	 * @param host DBサーバアドレス
	 * @return DBコネクション
	 */
	static public Connection getConnection(String host)
		throws SQLException
	{
		String sql =
			String.format(
				"jdbc:sqlserver://%s:2144;DatabaseName=RadioTopic;User=sa;Password=p@ssw0rd;",
				host);
		return DriverManager.getConnection(sql);
	}

	/**
	 * DBコネクションを取得
	 * @return DBコネクション
	 */
	static public Connection getConnection()
		throws SQLException
	{
		return getConnection("localhost");
	}
}
