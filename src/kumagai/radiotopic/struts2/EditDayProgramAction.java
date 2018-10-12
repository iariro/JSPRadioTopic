package kumagai.radiotopic.struts2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import kumagai.radiotopic.DayCollection;

/**
 * 日の番組変更アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Results
({
	@Result(name="success", location="/radiotopic/editdayprogram.jsp"),
	@Result(name="error", location="/radiotopic/error.jsp")
})
public class EditDayProgramAction
{
	public String message;
	public int dayid;
	public int programid;

	/**
	 * 日の番組変更アクション。
	 * @return 処理結果
	 */
	@Action("editdayprogram")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		String url = context.getInitParameter("RadioTopicSqlserverUrl");

		if (url != null)
		{
			DriverManager.registerDriver(new SQLServerDriver());

			Connection connection = null;
			try
			{
				connection = DriverManager.getConnection(url);
				DayCollection.updateDayProgram(connection, dayid, programid);

				return "success";
			}
			catch (SQLException exception)
			{
				message = exception.getMessage();

				return "error";
			}
			finally
			{
				connection.close();
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
