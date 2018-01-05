package kumagai.radiotopic.struts2;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import kumagai.radiotopic.DayCollection;

/**
 * 画像削除アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Results
({
	@Result(name="success", location="/radiotopic/deleteimage.jsp"),
	@Result(name="error", location="/radiotopic/error.jsp")
})
public class DeleteImageAction
{
	public int imageId;
	public String filename;

	public String message;

	/**
	 * 画像登録アクション。
	 * @return 処理結果
	 */
	@Action("deleteimage")
	public String execute()
		throws Exception
	{
		try
		{
			ServletContext context = ServletActionContext.getServletContext();
			String dbUrl = context.getInitParameter("RadioTopicSqlserverUrl");
			String imageFolder = context.getInitParameter("RadioTopicImageFolder");

			if (dbUrl != null && imageFolder != null)
			{
				// 必要なパラメータの定義がある

				DriverManager.registerDriver(new SQLServerDriver());
				Connection connection = DriverManager.getConnection(dbUrl);
				DayCollection.deleteImage(connection, imageId);
				new File(imageFolder, filename).delete();

				return "success";
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
