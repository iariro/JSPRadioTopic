package kumagai.radiotopic.exporttext;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

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
 * トピックの内容をテキストにエクスポート
 * @author kumagai
 */
public class ExportText
{
	/**
	 * @param args [0]=DBサーバアドレス [1]=出力ディレクトリパス [2]=startYear [3]=-n/-dn/-d
	 */
	public static void main(String[] args)
		throws SQLException, ParseException, IOException
	{
		if (args.length < 3)
		{
			return;
		}

		exportFromServlet(args);
	}

	/**
	 * サーブレットからZIP圧縮されたHTMLをエクスポート
	 * @param args [0]=DBサーバアドレス [1]=出力ディレクトリパス [2]=startYear [3]=-n/-dn/-d
	 * @throws IOException
	 */
	static private void exportFromServlet(String [] args)
		throws IOException
	{
		// URLを作成してGET通信を行う
		URL url =
			new URL(
				String.format(
					"http://%d/radiotopic/exportprogram?startYear=%s&outoutOption=%s",
					args[0],
					args[2],
					args[3]));
		HttpURLConnection http = (HttpURLConnection)url.openConnection();
		http.setRequestMethod("GET");
		http.connect();

		ZipInputStream inputStream = new ZipInputStream(new BufferedInputStream(http.getInputStream()));

		ZipEntry zipEntry;
		while ((zipEntry = inputStream.getNextEntry()) !=null)
		{
			BufferedOutputStream stream =
				new BufferedOutputStream(new FileOutputStream(zipEntry.getName()));

			byte[] data = new byte[1024];
			int count;
			while ((count = inputStream.read(data)) > 0)
			{
				stream.write(data,0,count);
			}
			stream.close();
		}

		inputStream.close();
		http.disconnect();
	}

	/**
	 * SQL Serverに接続しエクスポート
	 * @param args [0]=DBサーバアドレス [1]=出力ディレクトリパス [2]=startYear [3]=-n/-dn/-d
	 * @throws SQLException
	 * @throws IOException
	 * @throws ParseException
	 */
	static private void exportFromSQLServer(String [] args) throws SQLException, ParseException, IOException
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

			File htmlFile =
				new File(args[1], entry.getKey().shortname + ".html");

			PrintWriter writer = null;
			try
			{
				writer =
					new PrintWriter(
						new OutputStreamWriter(
							new FileOutputStream(htmlFile), "utf-8"));

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

			// System.out.printf("%s written.\n", htmlFile);
		}

		connection.close();

		outputIndexHtml(args[1], programCollection, startYear);
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
	}

	/**
	 * インデックスHTML出力。年表イメージも含む。
	 * @param outputPath 出力パス
	 * @param programCollection 全番組情報
	 */
	static protected void outputIndexHtml(String outputPath,
		ProgramCollection programCollection, Integer startYear)
		throws ParseException, IOException
	{
		ChronologyGraphData chronologyGraphData =
			new ChronologyGraphData(programCollection, 1200, 600, startYear);

		BufferedImage readImage = new ChronologyBitmap(chronologyGraphData);

		File imageFile = new File(outputPath, "Chronology.png");

		ImageIO.write(readImage, "png", imageFile);

		File indexFile = new File(outputPath, "index.html");

		PrintWriter writer =
			new PrintWriter(
				new OutputStreamWriter(
					new FileOutputStream(indexFile), "utf-8"));

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

	/**
	 * @param args [0]=DBサーバアドレス [1]=出力ディレクトリパス [2]=startYear [3]=-n/-dn/-d
	 * @throws SQLException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void exportAndZip(String [] args)
		throws SQLException, IOException, ParseException
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

		connection.close();

		ExportText.outputIndexHtml(zipStream, programCollection, startYear);
	}

	/**
	 * インデックスHTML出力。年表イメージも含む。
	 * @param zipStream 出力ストリーム
	 * @param programCollection 全番組情報
	 * @param startYear 開始年
	 */
	public static void outputIndexHtml(ZipOutputStream zipStream,
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
