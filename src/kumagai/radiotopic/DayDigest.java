package kumagai.radiotopic;

import java.sql.*;
import java.text.*;

/**
 * 視聴途中リスト表示用回情報
 * @author kumagai
 */
public class DayDigest
{
	static private final SimpleDateFormat format = new SimpleDateFormat();

	static
	{
		format.applyPattern("yyyy/MM/dd");
	}

	public final String programName;
	public final Date date;
	public final String no;

	/**
	 * 日付を文字列で取得
	 * @return 日付文字列
	 */
	public String getDate()
	{
		return format.format(date);
	}

	/**
	 * 指定の値をメンバーに割り当てる
	 * @param programName 番組名
	 * @param date 日付
	 * @param no 番号
	 */
	public DayDigest(ResultSet results)
		throws SQLException
	{
		this.programName = results.getString("name");
		this.date = results.getDate("date");
		this.no = results.getString("no");
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return String.format("%s %s %s", programName, getDate(), no);
	}
}
