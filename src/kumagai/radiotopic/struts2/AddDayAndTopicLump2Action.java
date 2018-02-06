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

	public String nos = new String();
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
					String [] fields = lines[i].split(" ");

					String no = fields[0];
					if (nos.length() > 0)
					{
						nos += ", ";
					}
					nos += no;

					// Dayエントリ作成
					int newDayId = DayCollection.insertDay(connection, programid, null, no);

					// トピック追加
					for(int j=1 ; j<fields.length ; j++)
					{
						TopicCollection.insertTopic(connection, newDayId, fields[j]);
					}
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
