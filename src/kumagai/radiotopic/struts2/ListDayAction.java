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

import kumagai.radiotopic.Day;
import kumagai.radiotopic.DayAndTopics;
import kumagai.radiotopic.DayCollection;
import kumagai.radiotopic.SortOrder;

/**
 * 番組各回一覧ページ表示アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Results
({
	@Result(name="success", location="/radiotopic/listday.jsp"),
	@Result(name="error", location="/radiotopic/error.jsp")
})
public class ListDayAction
{
	public int programid;
	public String programName;
	public int sortOrder;

	public String topicsArray;
	public ArrayList<DayAndTopics> dayCollection;
	public int count;
	public String message;

	/**
	 * コンプリート率を求める。
	 * @return コンプリート率
	 */
	public String getCompleteRatio()
	{
		if (dayCollection.size() > 0)
		{
			return
				String.format("%2.2f", (float)(count * 100) / dayCollection.size());
		}
		else
		{
			return "-";
		}
	}

	/**
	 * 番組各回一覧ページ表示アクション。
	 * @return 処理結果
	 */
	@Action("listday")
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

				SortOrder sortOrder2 = SortOrder.values()[sortOrder];

				DayCollection dayCollection =
					new DayCollection(connection, programid, sortOrder2);

				count = dayCollection.size();

				this.dayCollection = new ArrayList<>();

				if (sortOrder2 == SortOrder.NumberByNumeric)
				{
					// 回の列を数字として扱う

					Integer no1 = null;
					Integer no2 = null;

					for (int i=0 ; i<dayCollection.size() ; i++)
					{
						try
						{
							no1 = Integer.valueOf(dayCollection.get(i).getNo());

							// 補間
							if (no1 != null && no2 != null)
							{
								for (int j=no2-1 ; j>no1 ; j--)
								{
									this.dayCollection.add(new DayAndTopics(null, new Day(null, j, null, null, null)));
								}
							}

							this.dayCollection.add(new DayAndTopics(connection, dayCollection.get(i)));

							no2 = no1;
						}
						catch (NumberFormatException exception)
						{
							this.dayCollection.add(new DayAndTopics(connection, dayCollection.get(i)));
						}
					}
				}
				else
				{
					// 回の列を数字として扱う以外

					for (int i=0 ; i<dayCollection.size() ; i++)
					{
						this.dayCollection.add(new DayAndTopics(connection, dayCollection.get(i)));
					}
				}
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
