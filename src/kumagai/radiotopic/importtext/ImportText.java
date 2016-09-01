package kumagai.radiotopic.importtext;

import java.io.*;
import java.sql.*;
import kumagai.radiotopic.*;
import com.microsoft.sqlserver.jdbc.*;

/**
 * テキストファイルのインポート
 * @author kumagai
 */
public class ImportText
{
	/**
	 * テキストファイルのインポート
	 * @param args [0]=番組名 [1]=ファイル名 [2]=-d/-n/-nd
	 */
	public static void main(String[] args)
		throws IOException, SQLException
	{
		if (args.length >= 3)
		{
			DriverManager.registerDriver(new SQLServerDriver());

			Connection connection = RadioTopicDatabase.getConnection();

			int programId = ProgramCollection.getProgramId(connection, args[0]);

			System.out.printf("%d\n", programId);

			FileInputStream fileInputStream =
				new FileInputStream(args[1]);

			InputStreamReader inputStreamReader =
				new InputStreamReader(fileInputStream, "sjis");

			BufferedReader bufferedReader =
				new BufferedReader(inputStreamReader);

			String line;
			while ((line = bufferedReader.readLine()) != null)
			{
				if (line.indexOf("\t") >= 0)
				{
					// タブを含む行である

					String [] field1 = line.split("\t");

					if (args[2].equals("-d") ||
						args[2].equals("-n") ||
						args[2].equals("-nd"))
					{
						int dayid;
						int valueIndex = 1;

						if (args[2].equals("-nd"))
						{
							// -nd

							dayid =
								DayCollection.insertDay(
									connection,
									programId,
									field1[1],
									field1[0]);
							valueIndex = 2;
						}
						else if (args[2].equals("-d"))
						{
							// -d

							dayid =
								DayCollection.insertDay(
									connection,
									programId,
									field1[0],
									null);
						}
						else // if (args[2].equals("-n"))
						{
							// -n

							dayid =
								DayCollection.insertDay(
									connection,
									programId,
									null,
									field1[0]);
						}

						if (field1.length >= valueIndex + 1)
						{
							String [] valueField =
								field1[valueIndex].split(" ");

							for (int i=0 ; i<valueField.length ; i++)
							{
								TopicCollection.insertTopic
									(connection, dayid, valueField[i]);
							}

							System.out.println(line);
						}
					}
				}
			}

			connection.close();

			fileInputStream.close();
		}
		else
		{
			System.out.println("arg error");
		}
	}
}
