package kumagai.radiotopic.struts2;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import kumagai.radiotopic.ChronologyGraphData;
import kumagai.radiotopic.ProgramCollection;

/**
 * 年表表示アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Results
({
	@Result(name="success", location="/radiotopic/chronologygraph.jsp"),
	@Result(name="error", location="/radiotopic/error.jsp")
})
public class ChronologyGraphAction
{
	public ChronologyGraphData chronologyGraphData;
	public int startYear;

	/**
	 * 年表表示アクション。
	 * @return 処理結果
	 */
	@Action("chronologygraph")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		String url = context.getInitParameter("RadioTopicSqlserverUrl");

		if (url != null)
		{
			// URL定義あり

			DriverManager.registerDriver(new SQLServerDriver());

			Connection connection = DriverManager.getConnection(url);
			ProgramCollection programCollection =
				new ProgramCollection(connection);
			connection.close();

			chronologyGraphData =
				new ChronologyGraphData(programCollection, 1200, 600, startYear);

			return "success";
		}
		else
		{
			// URL定義なし

			return "error";
		}
	}
}
