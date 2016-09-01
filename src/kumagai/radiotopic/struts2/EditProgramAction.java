package kumagai.radiotopic.struts2;

import java.sql.*;
import javax.servlet.*;
import com.microsoft.sqlserver.jdbc.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import ktool.datetime.*;
import kumagai.radiotopic.*;

/**
 * 番組情報編集ページ表示アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Result(name="success", location="/radiotopic/editprogram.jsp")
public class EditProgramAction
{
	public int programid;
	public String programName;
	public String shortname;
	public String age;
	public int sortOrder;
	public String originUpdateDate;

	public int maxNo;
	public int updateRange;
	public int updateCount;
	public int listenCount;
	public int remainCount;
	public String estimateDay;
	public String estimateDetail;

	/**
	 * 番組情報編集ページ表示アクション。
	 * @return 処理結果
	 */
	@Action("editprogram")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		DriverManager.registerDriver(new SQLServerDriver());

		Connection connection =
			DriverManager.getConnection
				(context.getInitParameter("RadioTopicSqlserverUrl"));

		SortOrder sortOrder2 = SortOrder.values()[sortOrder];

		DayCollection dayCollection =
			new DayCollection(connection, programid, sortOrder2);
		connection.close();

		maxNo = dayCollection.getMaxNo();
		updateRange = dayCollection.getUpdateRange();
		updateCount = dayCollection.getUpdateCount();
		listenCount = dayCollection.size();

		if (listenCount > maxNo)
		{
			// 小数点回があるなどで、実際の回数が最大値による回数を上回る

			maxNo = listenCount;
		}

		remainCount = maxNo - listenCount;

		if ((updateRange > 0) && (remainCount > 0))
		{
			// １日でも間隔がある・１回でも残りがある

			int remainDay = (updateRange * remainCount) / updateCount;

			DateTime today = new DateTime();

			today.addDay(remainDay);

			estimateDay = today.toString();

			estimateDetail =
				String.format(
					"%d日 × （%d回 ／ %d回） = %d日",
					updateRange,
					remainCount,
					updateCount,
					remainDay);
		}
		else
		{
			// １日も間隔がない・１回も残りがない

			estimateDay = "-";
			estimateDetail = "-";
		}

		return "success";
	}
}
