package kumagai.radiotopic.struts2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import ktool.datetime.DateTime;
import kumagai.radiotopic.Day;
import kumagai.radiotopic.DayCollection;
import kumagai.radiotopic.DayDigest;
import kumagai.radiotopic.Program;
import kumagai.radiotopic.ProgramCollection;
import kumagai.radiotopic.SortOrder;

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
	public String searchStartDate;
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

			Connection connection = null;
			try
			{
				connection = DriverManager.getConnection(url);

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

				DateTime today2 = new DateTime();
				today2.addDay(-10);
				today2.setDay(1);
				searchStartDate = today2.toString();

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
