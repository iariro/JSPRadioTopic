package kumagai.radiotopic;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * トピック検索結果１件
 */
public class SearchTopicResult
{
	public final String name;
	public final String no;
	public final String date;
	public final String text;
	public final String updatedate;

	/**
	 * DB取得内容から検索結果オブジェクトを構築
	 * @param results DB取得内容
	 */
	public SearchTopicResult(ResultSet results)
		throws SQLException
	{
		name = results.getString("name");
		no = results.getString("no");
		date = results.getString("date");
		text = results.getString("text");
		updatedate = results.getString("updatedate");
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return String.format("%s %s %s %s %s", name, no, date, text, updatedate);
	}
}
