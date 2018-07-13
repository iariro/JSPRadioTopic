package kumagai.radiotopic.servlet;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import kumagai.radiotopic.ChronologyBitmap;
import kumagai.radiotopic.ChronologyGraphData;
import kumagai.radiotopic.ProgramCollection;

/**
 * 年表画像出力サーブレット。
 * @author kumagai
 */
public class ChronologyGraphServlet
	extends HttpServlet
{
	/**
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet
		(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		request.setCharacterEncoding("utf-8");

		ServletContext context = getServletConfig().getServletContext();

		BufferedImage readImage;

		try
		{
			DriverManager.registerDriver(new SQLServerDriver());

			String url = context.getInitParameter("RadioTopicSqlserverUrl");
			String startYear = request.getParameter("startYear");

			if (url != null)
			{
				// URL定義あり

				Connection connection = DriverManager.getConnection(url);
				ProgramCollection programCollection =
					new ProgramCollection(connection);
				connection.close();

				readImage =
					new ChronologyBitmap(
						new ChronologyGraphData(programCollection, 1200, 600, startYear != null ? Integer.valueOf(startYear) : null));
			}
			else
			{
				// URL定義なし

				readImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_BGR);
				Graphics2D graphics = readImage.createGraphics();
				graphics.drawString("url not found", 0, 0);
			}
		}
		catch (Exception exception)
		{
			readImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_BGR);
			Graphics2D graphics = readImage.createGraphics();
			graphics.drawString(exception.toString(), 0, 0);
		}

		ImageIO.write(readImage, "png", response.getOutputStream());
	}
}
