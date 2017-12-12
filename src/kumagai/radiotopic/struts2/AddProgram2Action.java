package kumagai.radiotopic.struts2;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import kumagai.radiotopic.ProgramCollection;

/**
 * 番組追加アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Result(name="success", location="/radiotopic/addprogram2.jsp")
public class AddProgram2Action
{
	public String name;
	public String shortName;
	public String age;
	public int sortOrder;
	public String originUpdateDate;

	public int newid;

	/**
	 * 番組追加アクション。
	 * @return 処理結果
	 */
	@Action("addprogram2")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		DriverManager.registerDriver(new SQLServerDriver());

		Connection connection =
			DriverManager.getConnection
				(context.getInitParameter("RadioTopicSqlserverUrl"));

		newid = ProgramCollection.insertProgram(connection, name, shortName, age, sortOrder, originUpdateDate);

		return "success";
	}
}
