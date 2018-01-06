package kumagai.radiotopic;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 画像情報
 * @author kumagai
 */
public class Image
{
	public final int id;
	public final String no;
	public final String filename;

	/**
	 * DBレコード情報の値をメンバーに割り当てる
	 * @param results DBレコード情報
	 */
	public Image(ResultSet results)
		throws SQLException
	{
		this.id = results.getInt("id");
		this.no = results.getString("no");
		this.filename = results.getString("filename");
	}
}
