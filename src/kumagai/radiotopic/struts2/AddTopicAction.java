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

import kumagai.radiotopic.TopicCollection;

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

				newid = TopicCollection.insertTopic(connection, dayid, text.trim());

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
