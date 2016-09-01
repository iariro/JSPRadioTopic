package kumagai.radiotopic;

import java.sql.*;

/**
 * トピック
 * @author kumagai
 */
public class Topic
{
	public final int id;
	public final int dayid;
	public final String text;

	/**
	 * 指定の値をメンバーに割り当て（デバッグ用）
	 * @param id トピックID
	 * @param dayid 回ID
	 * @param text テキスト
	 */
	public Topic(int id, int dayid, String text)
	{
		this.id = id;
		this.dayid = dayid;
		this.text = text;
	}

	/**
	 * 指定の値をメンバーに割り当て
	 * @param results DBレコード
	 */
	public Topic(ResultSet results)
		throws SQLException
	{
		this.id = results.getInt("id");
		this.dayid = results.getInt("dayid");
		this.text = results.getString("text");
	}
}
