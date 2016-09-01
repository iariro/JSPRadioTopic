package kumagai.radiotopic.struts2;

import java.sql.*;
import javax.servlet.*;
import com.microsoft.sqlserver.jdbc.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import kumagai.radiotopic.*;

/**
 * 日情報編集結果表示ページ表示アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Results
({
	@Result(name="success", location="/radiotopic/editday2.jsp"),
	@Result(name="error", location="/radiotopic/error.jsp")
})
public class EditDay2Action
{
	public int dayid;
	public String no;
	public String date;

	public String message;

	/**
	 * 日情報編集ページ表示アクション。
	 * @return 処理結果
	 */
	@Action("editday2")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		String url = context.getInitParameter("RadioTopicSqlserverUrl");

		if (url != null)
		{
			// URL指定あり

			DriverManager.registerDriver(new SQLServerDriver());

			try
			{
				Connection connection = DriverManager.getConnection(url);

				if (date.length() > 0)
				{
					// １文字でも指定されている

					date = StringTool.parseDate(date);
				}
				else
				{
					// 空文字列

					date = null;
				}

				DayCollection.updateDay(connection, dayid, no, date);

				connection.close();

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
			// URL指定なし

			message =
				"コンテキストパラメータ「RadioTopicSqlserverUrl」が未定義です";

			return "error";
		}
	}
}
