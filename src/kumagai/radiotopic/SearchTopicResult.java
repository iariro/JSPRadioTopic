package kumagai.radiotopic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 * トピック検索結果１件
 */
public class SearchTopicResult
{
	static private final SimpleDateFormat formatDate1;
	static private final SimpleDateFormat formatDate2;

	/**
	 * 日付書式オブジェクトを構築。
	 */
	static
	{
		formatDate1 = new SimpleDateFormat();
		formatDate2 = new SimpleDateFormat();

		formatDate1.applyPattern("yyyy/MM/dd");
		formatDate2.applyPattern("yyyy/MM/dd HH:mm:ss");
	}

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
		date = formatDate1.format(results.getDate("date"));
		text = results.getString("text");
		updatedate = formatDate2.format(results.getTimestamp("updatedate"));
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
