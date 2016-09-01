package kumagai.radiotopic.struts2;

import java.sql.*;

/**
 * 番号と更新区分の対
 * @author kumagai
 */
public class NoAndUpdateType
{
	public final Date date;
	public final String no;
	public final UpdateType type;

	/**
	 * 指定の値をメンバーに割り当てる
	 * @param date 日付
	 * @param no 番号
	 * @param type 更新区分
	 */
	public NoAndUpdateType(Date date, String no, UpdateType type)
	{
		this.date = date;
		this.no = no;
		this.type = type;
	}
}
