package kumagai.radiotopic.struts2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import kumagai.radiotopic.DayCollection;
import kumagai.radiotopic.Image;

/**
 * 画像表示アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Results
({
	@Result(name="dayimagelist", location="/radiotopic/dayimagelist.jsp"),
	@Result(name="programimagelist", location="/radiotopic/programimagelist.jsp"),
	@Result(name="error", location="/radiotopic/error.jsp")
})
public class ImageListAction
{
	public Integer dayid;
	public Integer programid;
	public String programName;
	public String no;

	public ArrayList<Image> images;
	public String message;

	/**
	 * 画像表示アクション。
	 * @return 処理結果
	 */
	@Action("imagelist")
	public String execute()
		throws Exception
	{
		try
		{
			ServletContext context = ServletActionContext.getServletContext();
			String dbUrl = context.getInitParameter("RadioTopicSqlserverUrl");

			if (dbUrl != null && (programid != null || dayid != null))
			{
				// 必要なパラメータの定義がある

				DriverManager.registerDriver(new SQLServerDriver());
				Connection connection = DriverManager.getConnection(dbUrl);

				if (programid != null)
				{
					// 番組指定

					images = DayCollection.getProgramImages(connection, programid);

					return "programimagelist";
				}
				else if (dayid != null)
				{
					// 日指定

					images = DayCollection.getDayImages(connection, dayid);

					return "dayimagelist";
				}
			}
			else
			{
				// 必要なパラメータの定義がない

				message = "必要なパラメータの定義がない";
			}
		}
		catch (Exception exception)
		{
			message = exception.toString();
		}

		return "error";
	}
}
