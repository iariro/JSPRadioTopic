package kumagai.radiotopic.struts2;

import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import kumagai.radiotopic.DayCollection;
import kumagai.radiotopic.ImageTrimming;
import kumagai.radiotopic.MovieRectangle;

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
	public boolean trimming;
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
					File destFile = new File(imageFolder, uploadfileFileName[i]);
					BufferedImage sourceImage = ImageIO.read(file);
					if (trimming)
					{
						// トリミングする

						MovieRectangle outline = ImageTrimming.findMovieOutline(sourceImage);
						if (!outline.isAnyNull())
						{
							// 境界検出成功

							ImageTrimming.cutImage(sourceImage, outline, destFile, contentType[1]);
							DayCollection.insertImage(connection, dayid, uploadfileFileName[i]);
							uploadedFiles.add(uploadfileFileName[i]);
						}
						else
						{
							// 境界検出失敗

							message = String.format("境界検出失敗 %s", outline.toString());
							return "error";
						}
					}
					else
					{
						// トリミングしない

						ImageIO.write(sourceImage, contentType[1], destFile);
						DayCollection.insertImage(connection, dayid, uploadfileFileName[i]);
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
