package kumagai.radiotopic.servlet;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.sql.*;
import javax.imageio.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.microsoft.sqlserver.jdbc.*;
import kumagai.radiotopic.*;

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

			if (url != null)
			{
				// URL定義あり

				Connection connection = DriverManager.getConnection(url);
				ProgramCollection programCollection =
					new ProgramCollection(connection);
				connection.close();

				readImage =
					new ChronologyBitmap(
						new ChronologyGraphData(programCollection, 1200, 600));
			}
			else
			{
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
