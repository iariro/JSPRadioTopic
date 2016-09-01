package kumagai.radiotopic.struts2;

import java.sql.*;
import javax.servlet.*;
import com.microsoft.sqlserver.jdbc.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import kumagai.radiotopic.*;

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
