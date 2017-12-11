package kumagai.radiotopic;

import java.sql.ResultSet;
import java.sql.SQLException;

import ktool.datetime.DateTime;

/**
 * 回
 * @author kumagai
 */
public class DayAndTopic
	extends Day
{
	public TopicCollection topicCollection;

	/**
	 * 指定の値をメンバーに割り当て
	 * @param programName 番組名
	 * @param no 回
	 * @param date 日付
	 * @param createdate 作成日付
	 * @param updatedate 更新日付
	 */
	public DayAndTopic(String programName, int no, DateTime date, DateTime createdate, DateTime updatedate)
	{
		super(programName, -1, 0, date, Integer.toString(no), createdate, updatedate);
	}

	/**
	 * DayCollection構築時、DBレコードから値を取得しフィールドに割り当てる
	 * @param results DBレコード
	 */
	public DayAndTopic(ResultSet results)
		throws SQLException
	{
		super(
			results.getString("name"),
			results.getInt("id"),
			results.getInt("programid"),
			results.getDate("date") != null ? new DateTime(results.getDate("date")) : null,
			results.getString("no"),
			results.getTimestamp("createdate") != null ? new DateTime(results.getTimestamp("createdate")) : null,
			results.getTimestamp("updatedate") != null ? new DateTime(results.getTimestamp("updatedate")) : null);
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
					programName,
					id,
					programid,
					date,
					no,
					topicCollection.size(),
					updatedate != null ? updatedate.toFullString() : null);
		}
		else
		{
			// トピックコレクションなし

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
}
