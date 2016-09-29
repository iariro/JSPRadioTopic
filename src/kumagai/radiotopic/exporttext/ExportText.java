package kumagai.radiotopic.exporttext;

import java.io.*;
import java.sql.*;
import java.util.*;
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
	 * @param args [0] 出力ディレクトリパス [1]=-n/-dn/-d
	 */
	public static void main(String[] args)
		throws Exception
	{
		DriverManager.registerDriver(new SQLServerDriver());

		Connection connection = RadioTopicDatabase.getConnection();

		HashMap<Program, DayCollection> programAndTopic =
			new HashMap<Program, DayCollection>();
		ProgramCollection programCollection = new ProgramCollection(connection);

		for (Program program : programCollection)
		{
			SortOrder sortOrder = SortOrder.values()[program.sortOrder];

			DayCollection dayCollection =
				new DayCollection(connection, program.id, sortOrder);

			programAndTopic.put(program, dayCollection);
		}

		connection.close();

		String argFlag = "-dn";

		if (args.length == 2)
		{
			// オプションあり

			argFlag = args[1];
		}

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

			File path = new File(args[0], entry.getKey().shortname + ".html");

			PrintWriter writer =
				new PrintWriter(
					new OutputStreamWriter(
						new FileOutputStream(path)));

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

			int pno = -1;

			writer.println("<html>");
			writer.println("<head>");
			writer.printf("<title>%s</title>\n", entry.getKey().name);
			writer.println("</head>");
			writer.println("<body>");
			writer.println("<pre>");

			for (Day day : entry.getValue())
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
							dateNoPrinter.printDateNo
								(null, Integer.toString(i));
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

			System.out.printf("%s written.\n", path);
		}
	}
}
