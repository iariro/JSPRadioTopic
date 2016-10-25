package kumagai.radiotopic.exporttext;

import java.awt.image.*;
import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.util.*;
import javax.imageio.*;
import com.microsoft.sqlserver.jdbc.*;
import ktool.datetime.*;
import kumagai.radiotopic.*;

/**
 * トピックの内容をテキストにエクスポート
 * @author kumagai
 */
public class ExportText
{
	/**
	 * @param args [0]=出力ディレクトリパス [1]=-n/-dn/-d
	 */
	public static void main(String[] args)
		throws Exception
	{
		String argFlag = "-dn";

		if (args.length == 2)
		{
			// オプションあり

			argFlag = args[1];
		}

		DriverManager.registerDriver(new SQLServerDriver());

		Connection connection = RadioTopicDatabase.getConnection();

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

		connection.close();

		DateTime today = new DateTime();

		for (Map.Entry<Program, DayCollection> entry
			: programAndTopic.entrySet())
		{
			Timestamp lastUpdate = entry.getValue().getLastUpdate();

			if (today.diff(new DateTime(lastUpdate)).getDay() >= 10)
			{
				// 最終更新から10日経っている

				continue;
			}

			File htmlFile =
				new File(args[0], entry.getKey().shortname + ".html");

			PrintWriter writer =
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
				(entry.getKey(), entry.getValue(), writer, dateNoPrinter);

			System.out.printf("%s written.\n", htmlFile);
		}

		outputIndexHtml(args[0], programCollection);
	}

	/**
	 * 番組HTMLファイル出力
	 * @param program 番組情報
	 * @param dayCollection 全日ごとの情報
	 * @param writer ファイルオブジェクト
	 * @param dateNoPrinter ファイル出力オブジェクト
	 */
	static protected void outputProgramHtml(Program program,
		DayCollection dayCollection, PrintWriter writer,
		DateNoPrinter dateNoPrinter)
		throws UnsupportedEncodingException, FileNotFoundException
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

			for (int i=0 ; i<day.topicCollection.size() ; i++)
			{
				if (i > 0)
				{
					// ２個目以降

					writer.print(" ");
				}

				writer.print(day.topicCollection.get(i).text);
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
	 * @param outputPath 出力パス
	 * @param programCollection 全番組情報
	 */
	static protected void outputIndexHtml(String outputPath,
		ProgramCollection programCollection)
		throws ParseException, IOException, UnsupportedEncodingException,
			FileNotFoundException
	{
		ChronologyGraphData chronologyGraphData =
			new ChronologyGraphData(programCollection, 1200, 600);

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
		}
		writer.println("</map>");

		writer.println("</body>");
		writer.println("</html>");

		writer.close();
	}
}
