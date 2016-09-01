package kumagai.radiotopic.struts2;

import java.sql.*;
import javax.servlet.*;
import com.microsoft.sqlserver.jdbc.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import kumagai.radiotopic.*;

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
	public DayCollection dayCollection;
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
				connection.close();

				count = dayCollection.size();

				if (sortOrder2 == SortOrder.NumberByNumeric)
				{
					// 回の列を数字として扱う

					this.dayCollection = new DayCollection();

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
									this.dayCollection.add(new Day(j, null, null, null));
								}
							}

							this.dayCollection.add(dayCollection.get(i));

							no2 = no1;
						}
						catch (NumberFormatException exception)
						{
							this.dayCollection.add(dayCollection.get(i));
						}
					}
				}
				else
				{
					// 回の列を数字として扱う以外

					this.dayCollection = dayCollection;
				}

				topicsArray = this.dayCollection.createJavaScriptArray();

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
