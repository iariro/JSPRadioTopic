package kumagai.radiotopic.struts2;

import java.io.File;
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
 * 画像登録アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Results
({
	@Result(name="success", location="/radiotopic/uploadimage.jsp"),
	@Result(name="error", location="/radiotopic/error.jsp")
})
public class UploadImageAction
{
	public File [] uploadfile;
	public String [] uploadfileContentType;
	public String [] uploadfileFileName;
	public int dayid;
	public ArrayList<String> uploadedFiles = new ArrayList<>();
	public String message;

	/**
	 * 画像登録アクション。
	 * @return 処理結果
	 */
	@Action("uploadimage")
	public String execute()
		throws Exception
	{
		try
		{
			ServletContext context = ServletActionContext.getServletContext();
			String dbUrl = context.getInitParameter("RadioTopicSqlserverUrl");
			String imageFolder = context.getInitParameter("RadioTopicImageFolder");

			if (dbUrl != null && imageFolder != null && uploadfile != null)
			{
				// 必要なパラメータの定義がある

				DriverManager.registerDriver(new SQLServerDriver());
				Connection connection = DriverManager.getConnection(dbUrl);

				for (int i=0 ; i<uploadfile.length ; i++)
				{
					File file = uploadfile[i];
					String [] contentType =uploadfileContentType[i].split("/");
					if (DayCollection.trimNiconicoImage
						(file, new File(imageFolder, uploadfileFileName[i]), contentType[1]) ||
						DayCollection.trimBorderImage
						(file, new File(imageFolder, uploadfileFileName[i]), contentType[1]))
					{
						// トリミング成功

						DayCollection.insertImage
							(connection, dayid, uploadfileFileName[i]);
						uploadedFiles.add(uploadfileFileName[i]);
					}
				}

				return "success";
			}
			else
			{
				// 必要なパラメータの定義がない

				if (uploadfile == null)
				{
					message = "画像の指定がない";
				}
				else
				{
					message = "必要なパラメータの定義がない";
				}
			}
		}
		catch (Exception exception)
		{
			message = exception.toString();
		}

		return "error";
	}
}
