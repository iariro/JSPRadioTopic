package kumagai.radiotopic.struts2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

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
import kumagai.radiotopic.RadioTopicDatabase;
import kumagai.radiotopic.SortOrder;
import net.arnx.jsonic.JSON;

/**
 * コンプリートグラフページ表示アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Results
({
	@Result(name="success", location="/radiotopic/watchdategraph.jsp"),
	@Result(name="error", location="/radiotopic/error.jsp")
})
public class WatchDateGraphAction
{
	/**
	 * テストコード
	 * @param args 未使用
	 */
	public static void main(String[] args)
		throws Exception
	{
		WatchDateGraphAction action = new WatchDateGraphAction();

		action.programid = 3;

		Connection connection = RadioTopicDatabase.getConnection();

		action.action(connection);
		System.out.println(action.getOnairDateList());
	}

	public int programid;
	public String programName;
	public String originUpdateDate;

	public String message;
	public ArrayList<Object> onairDate;
	public ArrayList<Object> watchDate;

	/**
	 * @return オンエア日リストJSONを取得
	 */
	public String getOnairDateList()
	{
		return JSON.encode(onairDate);
	}

	/**
	 * @return 視聴日リストJSONを取得
	 */
	public String getWatchDateList()
	{
		return JSON.encode(watchDate);
	}

	/**
	 * コンプリートグラフページ表示アクション。
	 * @return 処理結果
	 */
	@Action("watchdategraph")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		String url = context.getInitParameter("RadioTopicSqlserverUrl");

		if (url != null)
		{
			// URL指定あり

			DriverManager.registerDriver(new SQLServerDriver());

			Connection connection = DriverManager.getConnection(url);

			return action(connection);
		}
		else
		{
			// URL指定なし

			message =
				"コンテキストパラメータ「RadioTopicSqlserverUrl」が未定義です";

			return "error";
		}
	}

	/**
	 * コンプリートグラフページ表示アクション。
	 * @param connection DB接続オブジェクト
	 * @param programName 番組名
	 * @return 成否文字列
	 * @throws ParseException
	 */
	private String action(Connection connection) throws ParseException
	{
		try
		{
			DayCollection dayCollection =
				new DayCollection(connection, programid, SortOrder.NumberByNumeric, "asc");

			connection.close();

			onairDate = new ArrayList<Object>();
			watchDate = new ArrayList<Object>();

			for (Day day : dayCollection)
			{
				if (day.getDate() != null)
				{
					onairDate.add(
						new Object [] {
							Float.valueOf(day.getNo()),
							DateTime.parseDateString(day.getDate()).getCalendar().getTimeInMillis()});
				}

				if (day.getUpdateDateAsString() != null)
				{
					watchDate.add(
						new Object [] {
							Float.valueOf(day.getNo()),
							DateTime.parseDateString(day.getUpdateDateAsString()).getCalendar().getTimeInMillis()});
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
}
