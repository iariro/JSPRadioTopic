package kumagai.radiotopic.struts2;

import java.sql.*;
import java.util.*;
import javax.servlet.*;
import com.microsoft.sqlserver.jdbc.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import kumagai.radiotopic.*;

/**
 * エラーチェックアクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Result(name="success", location="/radiotopic/errorcheck.jsp")
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
				ProgramCollection programCollection =
					new ProgramCollection(connection);
				for (Program program : programCollection)
				{
					DayCollection days =
						new DayCollection
							(connection, program.id, SortOrder.NumberByNumeric);
					Day pday = null;
					for (Day day : days)
					{
						if (pday != null)
						{
							// 比較する回あり

							if (day != null && day.date != null && pday != null && pday.date != null)
							{
								// 日情報はそろっている

								if (day.date.compareTo(pday.date) > 0)
								{
									// 新しいはずの日の方が古い

									invalidDates.add(
										String.format(
											"%sの%s:%sと%s:%sの前後関係が異常",
											program.name,
											day.getNo(),
											day.getDate(),
											pday.getNo(),
											pday.getDate()));
								}
							}
						}

						if (day.date != null)
						{
							pday = day;
						}
					}
				}
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
