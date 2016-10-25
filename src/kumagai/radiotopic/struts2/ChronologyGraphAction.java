package kumagai.radiotopic.struts2;

import java.sql.*;
import javax.servlet.*;
import com.microsoft.sqlserver.jdbc.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import kumagai.radiotopic.*;

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
				new ChronologyGraphData(programCollection, 1200, 600);

			return "success";
		}
		else
		{
			// URL定義なし

			return "error";
		}
	}
}
