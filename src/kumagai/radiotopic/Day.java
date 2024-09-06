package kumagai.radiotopic;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import ktool.datetime.DateTime;

/**
 * 回
 * @author kumagai
 */
public class Day
{
	public final String programName;
	public final int id;
	public final int programid;
	public final DateTime date;
	private final String no;
	public final DateTime createdate;
	public final DateTime updatedate;
	public final int imagenum;

	/**
	 * 日付を文字列で取得
	 * @return 日付文字列
	 */
	public String getDate()
	{
		if (date != null)
		{
			// 日付あり

			return date.toString();
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

			if (this.no.indexOf('.') < 0)
			{
				// 小数を含まない

				no = StringTool.trimZero(no);
			}
		}

		return no;
	}

	/**
	 * 作成日付を文字列形式で取得
	 * @return 文字列形式の作成日付
	 */
	public String getCreateDateAsString()
	{
		return createdate != null ? createdate.toString() : null;
	}

	/**
	 * 更新日付を文字列形式で取得
	 * @return 文字列形式の更新日付
	 */
	public String getUpdateDateAsString()
	{
		return updatedate != null ? updatedate.toString() : null;
	}

	/**
	 * 指定の値をメンバーに割り当て
	 * @param programName 番組名
	 * @param no 回
	 * @param date 日付
	 * @param createdate 作成日付
	 * @param updatedate 更新日付
	 */
	public Day(String programName, int no, DateTime date, DateTime createdate, DateTime updatedate)
	{
		this.programName = programName;
		this.id = -1;
		this.programid = 0;
		this.date = date;

		if (createdate != null)
		{
			this.createdate = createdate;
		}
		else
		{
			this.createdate = null;
		}

		if (updatedate != null)
		{
			this.updatedate = updatedate;
		}
		else
		{
			this.updatedate = null;
		}

		this.no = Integer.toString(no);
		this.imagenum = 0;
	}

	/**
	 * DayCollection構築時、DBレコードから値を取得しフィールドに割り当てる
	 * @param results DBレコード
	 */
	public Day(ResultSet results)
		throws SQLException
	{
		this.programName = results.getString("name");
		this.id = results.getInt("id");
		this.programid = results.getInt("programid");

		Date date = results.getDate("date");
		if (date != null)
		{
			this.date = new DateTime(date);
		}
		else
		{
			this.date = null;
		}

		Timestamp createdate = results.getTimestamp("createdate");
		if (createdate != null)
		{
			this.createdate = new DateTime(createdate);
		}
		else
		{
			this.createdate = null;
		}

		Timestamp updatedate = results.getTimestamp("updatedate");
		if (updatedate != null)
		{
			this.updatedate = new DateTime(updatedate);
		}
		else
		{
			this.updatedate = null;
		}

		this.no = results.getString("no");
		this.imagenum = results.getInt("imagenum");
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return
			String.format(
				"%s %d %d %s %s %s",
				programName,
				id,
				programid,
				date,
				no,
				updatedate != null ? updatedate.toFullString() : null);
	}
}
