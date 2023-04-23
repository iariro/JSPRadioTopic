package kumagai.radiotopic.struts2;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import kumagai.radiotopic.ChronologyBitmap;
import kumagai.radiotopic.ChronologyGraphData;
import kumagai.radiotopic.ProgramCollection;

/**
 * 年表表示アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Results
({
	@Result(name="success", location="/radiotopic/chronologygraph.jsp"),
	@Result(name="error", location="/radiotopic/error.jsp")
})
public class ChronologyGraphAction
{
	public ChronologyGraphData chronologyGraphData;
	public int startYear;
	public String graphBase64;

	/**
	 * 年表表示アクション。
	 * @return 処理結果
	 */
	@Action("chronologygraph")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		String url = context.getInitParameter("RadioTopicSqlserverUrl");
		String chronologyBitmapSize = context.getInitParameter("RadioTopicChronologyBitmapSize");

		if (url != null)
		{
			// URL定義あり

			DriverManager.registerDriver(new SQLServerDriver());

			Connection connection = DriverManager.getConnection(url);
			ProgramCollection programCollection =
				new ProgramCollection(connection);
			connection.close();

			String [] chronologyBitmapSizeXY = chronologyBitmapSize.split(",");

			// クリッカブルマップデータ生成
			chronologyGraphData =
				new ChronologyGraphData(
					programCollection,
					Integer.valueOf(chronologyBitmapSizeXY[0]),
					Integer.valueOf(chronologyBitmapSizeXY[1]),
					startYear);

			// インライン画像生成
			ChronologyBitmap chronologyBitmap = new ChronologyBitmap(chronologyGraphData);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			BufferedOutputStream bos = new BufferedOutputStream(baos);
			ImageIO.write(chronologyBitmap, "png", bos);
			byte[] bImage = baos.toByteArray();
			graphBase64 = Base64.getEncoder().encodeToString(bImage);

			return "success";
		}
		else
		{
			// URL定義なし

			return "error";
		}
	}
}
