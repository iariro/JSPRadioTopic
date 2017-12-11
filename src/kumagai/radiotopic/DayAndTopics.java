package kumagai.radiotopic;

import java.sql.Connection;
import java.sql.SQLException;

import ktool.datetime.DateTime;
import ktool.datetime.TimeSpan;

/**
 * トピック情報を含む回情報
 */
public class DayAndTopics
{
	public final String programName;
	public final int id;
	public final int programid;
	public final DateTime date;
	private final String no;
	public final DateTime createdate;
	public final DateTime updatedate;
	public TopicCollection topicCollection;

	/**
	 * 回情報の集約とトピックの構築
	 * @param connection DBコネクションオブジェクト
	 * @param day 回情報
	 */
	public DayAndTopics(Connection connection, Day day)
		throws SQLException
	{
		this.programName = day.programName;
		this.id = day.id;
		this.programid = day.programid;
		this.date = day.date;
		this.no = day.getNo();
		this.createdate = day.createdate;
		this.updatedate = day.updatedate;

		if (connection != null)
		{
			// トピックを取得する場合

			this.topicCollection = new TopicCollection(connection, id);
		}
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
