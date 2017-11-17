package kumagai.radiotopic.struts2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import kumagai.radiotopic.ProgramCollection;

/**
 * エラーチェックアクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Results
({
	@Result(name="success", location="/radiotopic/errorcheck.jsp"),
	@Result(name="error", location="/radiotopic/error.jsp")
})
public class ErrorCheckAction
{
	public String message;
	public ArrayList<String> invalidDates = new ArrayList<String>();

	/**
	 * エラーチェックアクション。
	 * @author kumagai
	 */
	@Action("errorcheck")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		DriverManager.registerDriver(new SQLServerDriver());

		String url = context.getInitParameter("RadioTopicSqlserverUrl");

		if (url != null)
		{
			// URL定義あり

			DriverManager.registerDriver(new SQLServerDriver());
			try
			{
				Connection connection = DriverManager.getConnection(url);
				invalidDates = ProgramCollection.checkDateFast(connection);
				return "success";
			}
			catch (SQLException exception)
			{
				message = exception.getMessage();

				return "error";
			}
		}
		else
		{
			// URL定義なし

			message =
				"コンテキストパラメータ「RadioTopicSqlserverUrl」が未定義です";

			return "error";
		}
	}
}
