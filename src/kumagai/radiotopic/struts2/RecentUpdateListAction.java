package kumagai.radiotopic.struts2;

import java.sql.*;
import java.util.*;
import javax.servlet.*;
import com.microsoft.sqlserver.jdbc.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import kumagai.radiotopic.*;

/**
 * 最近聞いた回リスト表示アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Result(name="success", location="/radiotopic/recentupdatelist.jsp")
public class RecentUpdateListAction
{
	public int dayNum;

	public ArrayList<ArrayList<Day>> recentUpdateDays;

	/**
	 * 最近聞いた回リスト表示アクション。
	 * @return 処理結果
	 */
	@Action("recentupdatelist")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		DriverManager.registerDriver(new SQLServerDriver());

		Connection connection =
			DriverManager.getConnection
				(context.getInitParameter("RadioTopicSqlserverUrl"));

		recentUpdateDays =
			DayCollection.getRecentUpdateDays(connection, dayNum);

		connection.close();

		return "success";
	}
}
