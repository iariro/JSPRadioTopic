package kumagai.radiotopic;

import java.sql.*;
import java.text.*;
import ktool.datetime.*;

/**
 * 回
 * @author kumagai
 */
public class Day
{
	static public final SimpleDateFormat format = new SimpleDateFormat();
	static public final SimpleDateFormat formatDT = new SimpleDateFormat();

	static
	{
		format.applyPattern("yyyy/MM/dd");
		formatDT.applyPattern("yyyy/MM/dd HH:mm:ss");
	}

	public final String programname;
	public final int id;
	public final int programid;
	public final Date date;
	private final String no;
	public final Timestamp createdate;
	public final Timestamp updatedate;
	public TopicCollection topicCollection;

	/**
	 * 日付を文字列で取得
	 * @return 日付文字列
	 */
	public String getDate()
	{
		if (date != null)
		{
			// 日付あり

			return format.format(date);
		}
		else
		{
			// 日付なし

			return null;
		}
	}

	/**
	 * 回数を、スペース・頭の０を削除し取得。
	 * @return 回数
	 */
	public String getNo()
	{
		String no = null;

		if (this.no != null)
		{
			// 回数指定あり。

			no = this.no.trim();

			no = StringTool.trimZero(no);
		}

		return no;
	}

	/**
	 * 作成日付を文字列形式で取得
	 * @return 文字列形式の作成日付
	 */
	public String getCreateDateAsString()
	{
		return format.format(createdate);
	}

	/**
	 * 更新日付を文字列形式で取得
	 * @return 文字列形式の更新日付
	 */
	public String getUpdateDateAsString()
	{
		return format.format(updatedate);
	}

	/**
	 * 更新日付を文字列形式で取得
	 * @return 文字列形式の更新日付
	 */
	public String getUpdateDateTimeAsString()
	{
		return formatDT.format(updatedate);
	}

	/**
	 * 新規更新分であるか判定
	 * @return true=新規更新分である／false=新規更新分ではない
	 */
	public boolean isNewUpdate()
	{
		DateTime today = new DateTime();
		TimeSpan diff = today.diff(new DateTime(updatedate));

		return diff.getDay() <= 2;
	}

	/**
	 * 指定の値をメンバーに割り当て
	 * @param no 回
	 * @param date 日付
	 * @param createdate 作成日付
	 * @param updatedate 更新日付
	 */
	public Day(int no, Date date, Date createdate, Date updatedate)
	{
		this.programname = null;
		this.id = -1;
		this.programid = 0;
		this.date = date;

		if (createdate != null)
		{
			this.createdate = new Timestamp(createdate.getTime());
		}
		else
		{
			this.createdate = null;
		}

		if (updatedate != null)
		{
			this.updatedate = new Timestamp(updatedate.getTime());
		}
		else
		{
			this.updatedate = null;
		}

		this.no = Integer.toString(no);
	}

	/**
	 * DayCollection構築時、DBレコードから値を取得しフィールドに割り当てる
	 * @param results DBレコード
	 */
	public Day(ResultSet results)
		throws SQLException
	{
		this.programname = results.getString("name");
		this.id = results.getInt("id");
		this.programid = results.getInt("programid");
		this.date = results.getDate("date");
		this.createdate = results.getTimestamp("createdate");
		this.updatedate = results.getTimestamp("updatedate");
		this.no = results.getString("no");
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		if (topicCollection != null)
		{
			// トピックコレクションあり

			return
				String.format(
					"%s %d %d %s %s topic=%d %s",
					programname,
					id,
					programid,
					date,
					no,
					topicCollection.size(),
					updatedate != null ? formatDT.format(updatedate) : null);
		}
		else
		{
			// トピックコレクションなし

			return
				String.format(
					"%s %d %d %s %s %s",
					programname,
					id,
					programid,
					date,
					no,
					updatedate != null ? formatDT.format(updatedate) : null);
		}
	}
}
