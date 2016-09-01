package kumagai.radiotopic.struts2;

import java.sql.*;
import javax.servlet.*;
import com.microsoft.sqlserver.jdbc.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import kumagai.radiotopic.*;

/**
 * 一括日追加ページ結果表示アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Results
({
	@Result(name="success", location="/radiotopic/adddaylump2.jsp"),
	@Result(name="error", location="/radiotopic/error.jsp")
})
public class AddDayLump2Action
{
	public int programName;
	public int programid;
	public String date;
	public String no;
	public String topics;

	public String message;

	/**
	 * 一括日追加ページ結果表示アクション。
	 * @return 処理結果
	 */
	@Action("adddaylump2")
	public String execute()
		throws Exception
	{
		try
		{
			Integer.valueOf(no);
		}
		catch (Exception exception)
		{
			message = exception.getMessage();

			return "error";
		}

		ServletContext context = ServletActionContext.getServletContext();

		DriverManager.registerDriver(new SQLServerDriver());

		Connection connection =
			DriverManager.getConnection
				(context.getInitParameter("RadioTopicSqlserverUrl"));

		date = StringTool.parseDate(date);

		int newDayId = DayCollection.insertDay(connection, programid, date, no);

		for (String topic : topics.split("\r\n"))
		{
			TopicCollection.insertTopic(connection, newDayId, topic);
		}

		connection.close();

		return "success";
	}
}
