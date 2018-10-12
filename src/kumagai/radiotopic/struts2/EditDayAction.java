package kumagai.radiotopic.struts2;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import kumagai.radiotopic.ProgramCollection;
import kumagai.radiotopic.TopicCollection;

/**
 * 日情報編集ページ表示アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Results
({
	@Result(name="success", location="/radiotopic/editday.jsp"),
	@Result(name="error", location="/radiotopic/error.jsp")
})
public class EditDayAction
{
	public int dayid;
	public int programid;
	public String name;
	public String date;
	public String no;
	public ProgramCollection programCollection;

	public TopicCollection topicCollection;
	public String message;

	/**
	 * 日情報編集ページ表示アクション。
	 * @return 処理結果
	 */
	@Action("editday")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		String sqlserverUrl =
			context.getInitParameter("RadioTopicSqlserverUrl");

		if (sqlserverUrl != null)
		{
			try
			{
				DriverManager.registerDriver(new SQLServerDriver());

				Connection connection =
					DriverManager.getConnection(sqlserverUrl);

				topicCollection = new TopicCollection(connection, dayid);
				programCollection = new ProgramCollection(connection);

				connection.close();

				return "success";
			}
			catch (Exception exception)
			{
				message = exception.getMessage();

				return "error";
			}
		}
		else
		{
			message =
				"コンテキストパラメータ「RadioTopicSqlserverUrl」が未定義です";

			return "error";
		}
	}
}
