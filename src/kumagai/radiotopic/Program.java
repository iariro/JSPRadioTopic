package kumagai.radiotopic;

import java.sql.*;
import java.text.*;

/**
 * 番組情報
 * @author kumagai
 */
public class Program
{
	static private final SimpleDateFormat format = new SimpleDateFormat();

	static
	{
		format.applyPattern("yyyy/MM/dd");
	}

	public final int id;
	public final String name;
	public final String shortname;
	public final String age;
	public final int sortOrder;
	public final String exportformat;
	public final Date originUpdateDate;
	public final Date maxupdatedate;

	/**
	 * 指定の値をメンバーに割り当て
	 * @param results DBレコード
	 */
	public Program(ResultSet results)
		throws SQLException
	{
		this.id = results.getInt("id");
		this.name = results.getString("name");
		this.shortname = results.getString("shortname");
		this.age = results.getString("age");
		this.exportformat = results.getString("exportformat");
		this.sortOrder = results.getInt("sortorder");
		this.originUpdateDate = results.getDate("originupdatedate");
		this.maxupdatedate = results.getDate("maxupdatedate");
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return String.format("%d:%s (%d)", id, name, sortOrder);
	}

	/**
	 * 最初期の入力日付に使用する日付を文字列で取得
	 * @return 最初期の入力日付に使用する日付文字列
	 */
	public String getOriginUpdateDate()
	{
		return format.format(originUpdateDate);
	}
}
