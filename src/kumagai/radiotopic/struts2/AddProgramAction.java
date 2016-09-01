package kumagai.radiotopic.struts2;

import java.sql.*;
import javax.servlet.*;
import com.microsoft.sqlserver.jdbc.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import kumagai.radiotopic.*;

/**
 * 番組追加ページ表示アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Result(name="success", location="/radiotopic/addprogram.jsp")
public class AddProgramAction
{
	public String name;
	public int sortOrder;

	public int newid;

	/**
	 * 日追加ページ表示アクション。
	 * @return 処理結果
	 */
	@Action("addprogram")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		DriverManager.registerDriver(new SQLServerDriver());

		Connection connection =
			DriverManager.getConnection
				(context.getInitParameter("RadioTopicSqlserverUrl"));

		newid = ProgramCollection.insertProgram(connection, name, sortOrder);

		return "success";
	}
}
