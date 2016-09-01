package kumagai.radiotopic.struts2;

import java.sql.*;
import javax.servlet.*;
import com.microsoft.sqlserver.jdbc.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import kumagai.radiotopic.*;

/**
 * トピック追加ページ表示アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Results
({
	@Result(name="success", location="/radiotopic/addtopic.jsp"),
	@Result(name="error", location="/radiotopic/error.jsp")
})
public class AddTopicAction
{
	public int dayid;

	public String message;
	public String text;
	public int newid;

	/**
	 * トピック追加ページ表示アクション。
	 * @return 処理結果
	 */
	@Action("addtopic")
	public String execute()
		throws Exception
	{
		try
		{
			ServletContext context = ServletActionContext.getServletContext();

			String url = context.getInitParameter("RadioTopicSqlserverUrl");

			if (url != null)
			{
				DriverManager.registerDriver(new SQLServerDriver());

				Connection connection = DriverManager.getConnection(url);

				newid = TopicCollection.insertTopic(connection, dayid, text);

				connection.close();

				return "success";
			}
			else
			{
				message =
					"コンテキストパラメータ「RadioTopicSqlserverUrl」が未定義です";

				return "error";
			}
		}
		catch (Exception exception)
		{
			message = exception.getMessage();

			return "error";
		}
	}
}
