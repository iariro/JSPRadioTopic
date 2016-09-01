package kumagai.radiotopic.struts2;

import java.sql.*;
import java.util.*;
import javax.servlet.*;
import com.microsoft.sqlserver.jdbc.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import kumagai.radiotopic.*;

/**
 * コンプリート表ページ表示アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Results
({
	@Result(name="success", location="/radiotopic/completetable.jsp"),
	@Result(name="error", location="/radiotopic/error.jsp")
})
public class CompleteTableAction
{
	public static void main(String[] args)
		throws SQLException
	{
		DriverManager.registerDriver(new SQLServerDriver());

		Connection connection = RadioTopicDatabase.getConnection();

		SortOrder sortOrder2 = SortOrder.values()[0];

		DayCollection dayCollection =
			new DayCollection(connection, 7, sortOrder2);

		connection.close();

		UpdateDateAndNoTable updateMap =
			new UpdateDateAndNoTable(dayCollection, "2015/10/01");

		System.out.println(updateMap.getIdArrayString());

		for (ArrayList<String> row : updateMap.getCellArray(220))
		{
			for (String cell : row)
			{
				System.out.printf("%s ", cell);
			}
			System.out.println();
		}
	}

	public int programid;
	public String programName;
	public int sortOrder;
	public String originUpdateDate;

	public ArrayList<ArrayList<String>> cellArray;
	public String ids;
	public int stepCount;
	public int maxNo;
	public String dates;
	public String message;

	/**
	 * グラフページ表示アクション。
	 * @return 処理結果
	 */
	@Action("completetable")
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

				SortOrder sortOrder2 = SortOrder.values()[sortOrder];

				DayCollection dayCollection =
					new DayCollection(connection, programid, sortOrder2);

				connection.close();

				for (int i=0 ; i<dayCollection.size() ; i++)
				{
					try
					{
						int intno = Integer.valueOf(dayCollection.get(i).getNo());

						if (maxNo < intno)
						{
							// 最大を上回る

							maxNo = intno;
						}
					}
					catch (NumberFormatException exception)
					{
						// 何もしない
					}
				}

				UpdateDateAndNoTable updateMap =
					new UpdateDateAndNoTable(dayCollection, originUpdateDate);

				stepCount = updateMap.size();
				dates = updateMap.getDatesString();
				ids = updateMap.getIdArrayString();
				cellArray = updateMap.getCellArray(maxNo);

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
