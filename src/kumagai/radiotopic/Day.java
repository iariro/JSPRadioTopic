package kumagai.radiotopic;

import java.sql.ResultSet;
import java.sql.SQLException;

import ktool.datetime.DateTime;
import ktool.datetime.TimeSpan;

public class Day
{

	public final String programName;
	public final int id;
	public final int programid;
	public final DateTime date;
	protected final String no;
	public final DateTime createdate;
	public final DateTime updatedate;

	/**
	 * 指定の値をメンバーに割り当てる
	 * @param programName 番組名
	 * @param id 回ID
	 * @param programid 番組ID
	 * @param date 日付
	 * @param no 回
	 * @param createdate 作成日付
	 * @param updatedate 更新日付
	 */
	public Day(String programName, int id, int programid, DateTime date,
		String no, DateTime createdate, DateTime updatedate)
	{
		this.programName = programName;
		this.id = id;
		this.programid = programid;
		this.date = date;
		this.no = no;
		this.createdate = createdate;
		this.updatedate = updatedate;
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
		this.date = results.getDate("date") != null ? new DateTime(results.getDate("date")) : null;
		this.no = results.getString("no");
		this.createdate = results.getTimestamp("createdate") != null ? new DateTime(results.getTimestamp("createdate")) : null;
		this.updatedate = results.getTimestamp("updatedate") != null ? new DateTime(results.getTimestamp("updatedate")) : null;
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
		return createdate.toString();
	}

	/**
	 * 更新日付を文字列形式で取得
	 * @return 文字列形式の更新日付
	 */
	public String getUpdateDateAsString()
	{
		return updatedate.toString();
	}

	/**
	 * 更新日付を文字列形式で取得
	 * @return 文字列形式の更新日付
	 */
	public String getUpdateDateTimeAsString()
	{
		return updatedate.toFullString();
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

}