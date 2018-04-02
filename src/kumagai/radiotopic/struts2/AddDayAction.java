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

import kumagai.radiotopic.DayCollection;
import kumagai.radiotopic.SortOrder;
import kumagai.radiotopic.StringTool;

/**
 * 日追加ページ表示アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Results
({
	@Result(name="success", location="/radiotopic/addday.jsp"),
	@Result(name="error", location="/radiotopic/error.jsp")
})
public class AddDayAction
{
	public int programid;
	public int sortOrder;
	public String date;
	public String no;

	public int newid;
	public String message;

	/**
	 * 日追加ページ表示アクション。
	 * @return 処理結果
	 */
	@Action("addday")
	public String execute()
		throws Exception
	{
		try
		{
			SortOrder sortOrder2 = SortOrder.values()[sortOrder];

			if (sortOrder2 == SortOrder.NumberByNumeric)
			{
				// 回の列を数字として扱う

				if (no == null || no.isEmpty())
				{
					// 回数が空欄

					message = "回数が空欄です";

					return "error";
				}
			}

			ServletContext context = ServletActionContext.getServletContext();
			String sql = context.getInitParameter("RadioTopicSqlserverUrl");
			if (sql != null)
			{
				// パラメータ指定あり

				DriverManager.registerDriver(new SQLServerDriver());

				Connection connection = DriverManager.getConnection(sql);

				date = StringTool.parseDate(date);

				newid = DayCollection.insertDay(connection, programid, date, no);

				return "success";
			}
			else
			{
				// パラメータ指定なし

				message = "コンテキストパラメータ「RadioTopicSqlserverUrl」が未定義です";

				return "error";
			}
		}
		catch (Exception exception)
		{
			message = exception.toString();

			return "error";
		}
	}
}
