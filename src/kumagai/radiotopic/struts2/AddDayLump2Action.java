package kumagai.radiotopic.struts2;

import java.util.regex.*;
import java.sql.*;
import javax.servlet.*;
import com.microsoft.sqlserver.jdbc.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import kumagai.radiotopic.*;

/**
 * 一括日追加ページ結果表示アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Results
({
	@Result(name="success", location="/radiotopic/adddaylump2.jsp"),
	@Result(name="error", location="/radiotopic/error.jsp")
})
public class AddDayLump2Action
{
	static private final Pattern dateFormat =
		Pattern.compile("[0-9]{4}/[0-9]{2}/[0-9]{2}");

	public int programName;
	public int programid;
	public String date;
	public String no;
	public String topics;

	public String message;

	/**
	 * 一括日追加ページ結果表示アクション。
	 * @return 処理結果
	 */
	@Action("adddaylump2")
	public String execute()
		throws Exception
	{
		try
		{
			ServletContext context = ServletActionContext.getServletContext();

			String url = context.getInitParameter("RadioTopicSqlserverUrl");

			if (url != null)
			{
				DriverManager.registerDriver(new SQLServerDriver());

				Connection connection = DriverManager.getConnection(url);

				String [] lines = topics.split("\r\n");
				String date2 = StringTool.parseDate(lines[1]);
				Matcher matcher = dateFormat.matcher(date2);

				int index = 0;

				if (matcher.matches())
				{
					// ２行目が日付としてパースされた

					// １行目を回数・２行目を日付として扱う
					no = lines[0];
					date = date2;

					index = 2;
				}
				else
				{
					// ２行目が日付としてパースされなかった＝トピックのみの内容と判断

					Integer.valueOf(no);
					date = StringTool.parseDate(date);
				}

				// Dayエントリ作成
				int newDayId = DayCollection.insertDay(connection, programid, date, no);

				// トピックを登録
				for (int i=index ; i<lines.length ; i++)
				{
					TopicCollection.insertTopic(connection, newDayId, lines[i]);
				}

				connection.close();

				return "success";
			}
			else
			{
				message = "RadioTopicSqlserverUrl設定なし";

				return "error";
			}
		}
		catch (Exception exception)
		{
			message += exception.getMessage();

			return "error";
		}
	}
}
