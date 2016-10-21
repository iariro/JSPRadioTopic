package kumagai.radiotopic;

import java.sql.*;
import java.text.*;
import ktool.datetime.*;

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
	 * @param id ID
	 * @param name 番組名
	 * @param shortname ファイル名用番組名
	 * @param age 年代
	 * @param sortOrder ソート順
	 * @param exportformat エクスポート形式
	 * @param originUpdateDate 起点日付
	 * @param maxupdatedate 更新日
	 */
	public Program(int id, String name, String shortname, String age,
		int sortOrder, String exportformat, Date originUpdateDate,
		Date maxupdatedate)
	{
		this.id = id;
		this.name = name;
		this.shortname = shortname;
		this.age = age;
		this.sortOrder = sortOrder;
		this.exportformat = exportformat;
		this.originUpdateDate = originUpdateDate;
		this.maxupdatedate = maxupdatedate;
	}

	/**
	 * DBレコードの値をメンバーに割り当て
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

	/**
	 * 開始日を取得。
	 * @return 開始日／null=開始日なし
	 */
	public DateTime getStartDate()
		throws ParseException
	{
		String [] ageField = age.split("-");

		if (ageField.length >= 1)
		{
			// 始点あり

			return DateTime.parseDateString(ageField[0]);
		}
		else
		{
			return null;
		}
	}

	/**
	 * 終了日を取得。
	 * @return 終了日／null=終了日なし
	 */
	public DateTime getEndDate()
		throws ParseException
	{
		String [] ageField = age.split("-");

		if (ageField.length == 2)
		{
			// 終点あり

			return DateTime.parseDateString(ageField[1]);
		}
		else
		{
			return null;
		}
	}
}
