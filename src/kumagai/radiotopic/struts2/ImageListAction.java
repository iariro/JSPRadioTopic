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

/**
 * 画像表示アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Results
({
	@Result(name="success", location="/radiotopic/imagelist.jsp"),
	@Result(name="error", location="/radiotopic/error.jsp")
})
public class ImageListAction
{
	public int dayid;

	public String exception;

	public ArrayList<String> images;

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

			if (dbUrl != null)
			{
				// 必要なパラメータの定義がある

				DriverManager.registerDriver(new SQLServerDriver());
				Connection connection = DriverManager.getConnection(dbUrl);
				images = DayCollection.getDayImages(connection, dayid);

				return "success";
			}
			else
			{
				// 必要なパラメータの定義がない

				exception = "必要なパラメータの定義がない";
			}
		}
		catch (Exception exception)
		{
			this.exception = exception.toString();
		}

		return "error";
	}
}
