package kumagai.radiotopic.struts2;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import kumagai.radiotopic.DayCollection;
import kumagai.radiotopic.StringTool;
import kumagai.radiotopic.TopicCollection;

/**
 * 一括日・トピック追加ページ表示アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Result(name="success", location="/radiotopic/adddayandtopiclump2.jsp")
public class AddDayAndTopicLump2Action
{
	public int programName;
	public int programid;
	public int sortOrder;
	public String topics;

	public String message;

	/**
	 * 一括日・トピック追加ページ表示アクション。
	 * @return 処理結果
	 */
	@Action("adddayandtopiclump2")
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

				// トピックを登録
				for (int i=0 ; i<lines.length ; i++)
				{
					String date2 = StringTool.parseDate(lines[1]);

					// １行目を回数・２行目を日付として扱う
					String no = lines[0];
					String date = date2;

					date = StringTool.parseDate(date);

					// Dayエントリ作成
					int newDayId = DayCollection.insertDay(connection, programid, date, no);

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
