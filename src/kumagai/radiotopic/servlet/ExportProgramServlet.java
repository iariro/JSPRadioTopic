package kumagai.radiotopic.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kumagai.radiotopic.exporttext.ExportText;

/**
 * トピックの内容をエクスポートするサーブレット
 * @author kumagai
 */
public class ExportProgramServlet
	extends HttpServlet
{
	/**
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		ServletContext context = getServletConfig().getServletContext();

		String [] args =
			new String []
				{
					context.getInitParameter("RadioTopicSqlserverUrl"),
					null,
					request.getParameter("startYear"),
					request.getParameter("outputOption")
				};

		try
		{
			ExportText.exportAndZip(args, response);
		}
		catch (SQLException | ParseException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @param args [0]=DBサーバアドレス [1]=出力ファイルパス [2]=startYear [3]=-n/-dn/-d
	 * @throws ParseException
	 * @throws IOException
	 * @throws SQLException
	 */
	static public void main(String [] args)
		throws SQLException, IOException, ParseException
	{
		ExportText.exportAndZip(args, null);
	}
}
