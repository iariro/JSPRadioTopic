package kumagai.radiotopic.struts2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import kumagai.radiotopic.Day;
import kumagai.radiotopic.DayCollection;

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

		recentUpdateDays = DayCollection.getRecentUpdateDays(connection, dayNum);

		connection.close();

		return "success";
	}
}
