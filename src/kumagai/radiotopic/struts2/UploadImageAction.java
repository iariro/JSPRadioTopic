package kumagai.radiotopic.struts2;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import kumagai.radiotopic.DayCollection;

/**
 * 画像登録アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Result(name="success", location="/radiotopic/uploadimage.jsp")
public class UploadImageAction
{
	public File [] uploadfile;
	public String [] uploadfileContentType;
	public String [] uploadfileFileName;
	public int dayid;
	public String uploadedFiles;
	public String exception;

	/**
	 * 全番組対象キーワード検索アクション。
	 * @return 処理結果
	 */
	@Action("uploadimage")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();
		String dbUrl = context.getInitParameter("RadioTopicSqlserverUrl");
		String imageFolder = context.getInitParameter("RadioTopicImageFolder");

		if (dbUrl != null && imageFolder != null)
		{
			// 必要なパラメータの定義がある

			DriverManager.registerDriver(new SQLServerDriver());
			Connection connection = DriverManager.getConnection(dbUrl);

			uploadedFiles = new String();
			for (File file : uploadfile)
			{
				DayCollection.insertImage
					(connection, dayid, file.getName());
				DayCollection.trimNiconicoImage
					(file, new File(imageFolder, file.getName()));
				uploadedFiles += file.getName();
			}

			return "success";
		}
		else
		{
			// 必要なパラメータの定義がない

			exception = "必要なパラメータの定義がない";
		}

		return "error";
	}
}
