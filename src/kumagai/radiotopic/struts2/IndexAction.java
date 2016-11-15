package kumagai.radiotopic.struts2;

import java.sql.*;
import java.util.*;
import javax.servlet.*;
import com.microsoft.sqlserver.jdbc.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import ktool.datetime.*;
import kumagai.radiotopic.*;

/**
 * トップページ表示アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Results
({
	@Result(name="success", location="/radiotopic/index.jsp"),
	@Result(name="error", location="/radiotopic/error.jsp")
})
public class IndexAction
{
	public ProgramCollection programCollection;
	public ArrayList<DayDigest> tochuuDays;
	public ArrayList<Day> nextListenDays = new ArrayList<Day>();
	public String message;

	/**
	 * トップページ表示アクション。
	 * @return 処理結果
	 */
	@Action("index")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		String url = context.getInitParameter("RadioTopicSqlserverUrl");

		if (url != null)
		{
			DriverManager.registerDriver(new SQLServerDriver());

			try
			{
				Connection connection = DriverManager.getConnection(url);

				programCollection = new ProgramCollection(connection);
				tochuuDays = DayCollection.getTochuuTopic(connection);

				DateTime today = new DateTime();
				for (Program program : programCollection)
				{
					DayCollection dayCollection =
						new DayCollection
							(connection, program.id, SortOrder.values()[program.sortOrder]);

					Day nextListenDay =
						dayCollection.getNextListenDay(program.name, program.age, today);

					if (nextListenDay != null)
					{
						// 次回放送日表示あり

						nextListenDays.add(nextListenDay);
					}
				}
				
				Collections.sort(nextListenDays,
					new Comparator<Day>()
					{
						public int compare(Day day1, Day day2)
						{
							return day1.date.compareTo(day2.date);
						}
					});

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
			message =
				"コンテキストパラメータ「RadioTopicSqlserverUrl」が未定義です";

			return "error";
		}
	}
}
