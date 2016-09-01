package kumagai.radiotopic.struts2;

import java.sql.*;
import javax.servlet.*;
import com.microsoft.sqlserver.jdbc.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import kumagai.radiotopic.*;

/**
 * 番組情報編集結果ページ表示アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Result(name="success", location="/radiotopic/editprogram2.jsp")
public class EditProgram2Action
{
	public int programid;
	public String name;
	public String shortname;
	public String age;
	public int sortOrder;
	public String originUpdateDate;

	/**
	 * 番組情報編集結果ページ表示アクション。
	 * @return 処理結果
	 */
	@Action("editprogram2")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		DriverManager.registerDriver(new SQLServerDriver());

		Connection connection =
			DriverManager.getConnection
				(context.getInitParameter("RadioTopicSqlserverUrl"));

		ProgramCollection.updateProgram(
			connection,
			programid,
			name,
			shortname,
			age,
			sortOrder,
			originUpdateDate);

		return "success";
	}
}
