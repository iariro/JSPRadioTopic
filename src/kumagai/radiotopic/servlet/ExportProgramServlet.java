package kumagai.radiotopic.exporttext;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import ktool.datetime.DateTime;
import kumagai.radiotopic.ChronologyBitmap;
import kumagai.radiotopic.ChronologyGraphData;
import kumagai.radiotopic.ChronologyGraphDataElement;
import kumagai.radiotopic.Day;
import kumagai.radiotopic.DayCollection;
import kumagai.radiotopic.Program;
import kumagai.radiotopic.ProgramCollection;
import kumagai.radiotopic.RadioTopicDatabase;
import kumagai.radiotopic.SortOrder;
import kumagai.radiotopic.TopicCollection;

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
		String [] args =
			new String []
				{
					getInitParameter("RadioTopicSqlserverUrl"),
					null,
					request.getParameter("startYear"),
					request.getParameter("outoutOption")
				};

		exportAndZip(args);
	}

	/**
	 * @param args [0]=DBサーバアドレス [1]=出力ファイルパス [2]=startYear [3]=-n/-dn/-d
	 */
	static public void main(String [] args)
	{
		exportAndZip(args);
	}

	/**
	 * @param args [0]=DBサーバアドレス [1]=出力ディレクトリパス [2]=startYear [3]=-n/-dn/-d
	 */
	static private void exportAndZip(String [] args)
	{
		Integer startYear = null;
		if (!args[2].equals("-"))
		{
			// 無効「-」ではない

			startYear = Integer.valueOf(args[2]);
		}

		String argFlag = "-dn";

		if (args.length == 4)
		{
			// オプションあり

			argFlag = args[3];
		}

		DriverManager.registerDriver(new SQLServerDriver());

		Connection connection = RadioTopicDatabase.getConnection(args[0]);

		ProgramCollection programCollection = new ProgramCollection(connection);

		HashMap<Program, DayCollection> programAndTopic =
			new HashMap<Program, DayCollection>();

		for (Program program : programCollection)
		{
			SortOrder sortOrder = SortOrder.values()[program.sortOrder];

			DayCollection dayCollection =
				new DayCollection(connection, program.id, sortOrder);

			programAndTopic.put(program, dayCollection);
		}

		OutputStream outputStream;
		if (args[1] == null)
		{
			// 引数なし＝Servlet

			outputStream = System.out;
		}
		else
		{
			// 引数あり＝単体テスト実行

			outputStream = new FileOutputStream(args[1]);
		}

		ZipOutputStream zipStream =
			new ZipOutputStream(new BufferedOutputStream(outputStream));

		DateTime today = new DateTime();

		for (Map.Entry<Program, DayCollection> entry
			: programAndTopic.entrySet())
		{
			DateTime lastUpdate = entry.getValue().getLastUpdate();

			if (today.diff(lastUpdate).getDay() >= 10)
			{
				// 最終更新から10日経っている

				continue;
			}

			ZipEntry zip = new ZipEntry(entry.getKey().shortname + ".html");
			zipStream.putNextEntry(zip);

			PrintWriter writer = null;
			try
			{
				writer = new PrintWriter(new OutputStreamWriter(zipStream, "utf-8"));

				DateNoPrinter dateNoPrinter;

				if (entry.getKey().exportformat != null)
				{
					// エクスポート形式指定あり

					argFlag = entry.getKey().exportformat;

					if (argFlag.equals("-dn"))
					{
						dateNoPrinter =
							new DateNoPrinter(writer, entry.getValue().getMaxNo());
					}
					else if (argFlag.equals("-d"))
					{
						dateNoPrinter = new DatePrinter(writer);
					}
					else if (argFlag.equals("-n"))
					{
						dateNoPrinter =
							new NoPrinter(writer, entry.getValue().getMaxNo());
					}
					else
					{
						throw new IllegalArgumentException(argFlag);
					}
				}
				else
				{
					// エクスポート形式指定なし

					dateNoPrinter =
						new DateNoPrinter(writer, entry.getValue().getMaxNo());
				}

				outputProgramHtml
					(connection, entry.getKey(), entry.getValue(), writer, dateNoPrinter);
			}
			finally
			{
				if (writer != null)
				{
					writer.close();
				}
			}
		}

		connection.close();

		outputIndexHtml(zipStream, programCollection, startYear);
	}

	/**
	 * 番組HTMLファイル出力
	 * @param connection DB接続オブジェクト
	 * @param program 番組情報
	 * @param dayCollection 全日ごとの情報
	 * @param writer ファイルオブジェクト
	 * @param dateNoPrinter ファイル出力オブジェクト
	 */
	static protected void outputProgramHtml(Connection connection, Program program,
		DayCollection dayCollection, PrintWriter writer,
		DateNoPrinter dateNoPrinter) throws SQLException
	{
		writer.println("<html>");
		writer.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
		writer.println("<head>");
		writer.printf("<title>%s</title>\n", program.name);
		writer.println("</head>");
		writer.println("<body>");
		writer.println("<pre>");

		int pno = -1;

		for (Day day : dayCollection)
		{
			String date = day.getDate();

			try
			{
				int intNo = Integer.valueOf(day.getNo());

				// 補間
				if (pno >= 0)
				{
					for (int i=pno ; i>intNo ; i--)
					{
						dateNoPrinter.printDateNo(null, Integer.toString(i));
						writer.println();
					}
				}

				pno = intNo - 1;
			}
			catch (NumberFormatException exception)
			{
				// 何もしない
			}

			if (date != null)
			{
				// 日付あり

				dateNoPrinter.printDateNo(date, day.getNo());
			}
			else
			{
				// 日付なし

				dateNoPrinter.printDateNo(null, day.getNo());
			}

			TopicCollection topicCollection = new TopicCollection(connection, day.id);
			for (int i=0 ; i<topicCollection.size() ; i++)
			{
				if (i > 0)
				{
					// ２個目以降

					writer.print(" ");
				}

				writer.print(topicCollection.get(i).text);
			}
			writer.println();
		}

		writer.println("</pre>");
		writer.println("</body>");
		writer.println("</html>");

		writer.close();
	}

	/**
	 * インデックスHTML出力。年表イメージも含む。
	 * @param zipStream 出力ストリーム
	 * @param programCollection 全番組情報
	 * @param startYear 開始年
	 */
	static protected void outputIndexHtml(ZipOutputStream zipStream,
		ProgramCollection programCollection, Integer startYear)
		throws ParseException, IOException
	{
		ChronologyGraphData chronologyGraphData =
			new ChronologyGraphData(programCollection, 1200, 600, startYear);

		BufferedImage readImage = new ChronologyBitmap(chronologyGraphData);

		ZipEntry zip = new ZipEntry("Chronology.png");
		zipStream.putNextEntry(zip);

		ImageIO.write(readImage, "png", zipStream);

		zip = new ZipEntry("index.html");
		zipStream.putNextEntry(zip);

		PrintWriter writer = new PrintWriter(new OutputStreamWriter(zipStream, "utf-8"));

		writer.println("<html>");
		writer.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
		writer.println("<body>");

		writer.println("<img src='Chronology.png' usemap='#menu'>");
		writer.println("<map name='menu'>");
		for (ChronologyGraphDataElement element : chronologyGraphData)
		{
			writer.printf(
				"<area shape='rect' coords='%s' href='%s.html'>",
				element.getCoords(),
				element.shortname);
			writer.println();
		}
		writer.println("</map>");
		writer.println("<br>");

		for (ChronologyGraphDataElement element : chronologyGraphData)
		{
			writer.printf("<li><a href='%s.html'>%s</a>", element.shortname, element.shortname);
			writer.println();
		}
		writer.println("<br>");

		writer.printf("%s<br>", new DateTime().toFullString());
		writer.println();

		writer.println("</body>");
		writer.println("</html>");

		writer.close();
	}
}
