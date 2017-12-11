package kumagai.radiotopic.struts2;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import kumagai.radiotopic.DayCollection;
import kumagai.radiotopic.StringTool;

/**
 * 日追加ページ表示アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Result(name="success", location="/radiotopic/addday.jsp")
public class AddDayAction
{
	public int programid;
	public String date;
	public String no;

	public int newid;

	/**
	 * 日追加ページ表示アクション。
	 * @return 処理結果
	 */
	@Action("addday")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		DriverManager.registerDriver(new SQLServerDriver());

		Connection connection =
			DriverManager.getConnection
				(context.getInitParameter("RadioTopicSqlserverUrl"));

		date = StringTool.parseDate(date);

		newid = DayCollection.insertDay(connection, programid, date, no);

		return "success";
	}
}
