package kumagai.radiotopic.struts2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import ktool.datetime.DateTime;
import kumagai.radiotopic.ProgramCollection;
import kumagai.radiotopic.SearchTopicResult;

/**
 * 全番組対象キーワード検索アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Result(name="success", location="/radiotopic/searchallprogram.jsp")
public class SearchAllProgramAction
{
	public String startDate;
	public String keyword;
	public ArrayList<SearchTopicResult> searchTopicResults;

	/**
	 * 全番組対象キーワード検索アクション。
	 * @author kumagai
	 */
	@Action("searchallprogram")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		DriverManager.registerDriver(new SQLServerDriver());

		Connection connection =
			DriverManager.getConnection
				(context.getInitParameter("RadioTopicSqlserverUrl"));

		searchTopicResults =
			ProgramCollection.searchAllProgram
				(connection, keyword, DateTime.parseDateString(startDate));

		connection.close();

		return "success";
	}
}
