package kumagai.radiotopic.struts2;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.util.*;
import javax.servlet.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import com.microsoft.sqlserver.jdbc.*;
import org.apache.struts2.*;
import org.apache.struts2.convention.annotation.*;
import org.apache.struts2.convention.annotation.Result;
import kumagai.radiotopic.*;

/**
 * コンプリートグラフページ表示アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Results
({
	@Result(name="success", location="/radiotopic/completegraph.jsp"),
	@Result(name="error", location="/radiotopic/error.jsp")
})
public class CompleteGraphAction
{
	/**
	 * テストコード
	 * @param args 未使用
	 */
	public static void main(String[] args)
		throws Exception
	{
		CompleteGraphAction action = new CompleteGraphAction();

		action.programid = 3;
		action.sortOrder = 1;
		action.back = true;
		action.originUpdateDate = "2015/10/01";

		Connection connection = RadioTopicDatabase.getConnection();

		action.action(connection, "abc");

		Transformer transformer =
			TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
		transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

		FileOutputStream stream = new FileOutputStream("../out.svg");

		action.completeGraphDocument.write
			(transformer, new OutputStreamWriter(stream));

		stream.close();
	}

	public int programid;
	public String programName;
	public int sortOrder;
	public String originUpdateDate;
	public boolean back;

	public CompleteGraphDocument completeGraphDocument;
	public String message;

	/**
	 * グラフSVGドキュメントを文字列として取得。
	 * @return 文字列によるグラフSVGドキュメント
	 */
	public String getXml()
		throws TransformerFactoryConfigurationError, TransformerException
	{
		// XML書き出し準備。
		Transformer transformer =
			TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
		transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

		StringWriter writer = new StringWriter();

		// XML書き出し。
		completeGraphDocument.write(transformer, writer);

		return writer.toString();
	}

	/**
	 * コンプリートグラフページ表示アクション。
	 * @return 処理結果
	 */
	@Action("completegraph")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		String url = context.getInitParameter("RadioTopicSqlserverUrl");

		if (url != null)
		{
			// URL指定あり

			DriverManager.registerDriver(new SQLServerDriver());

			Connection connection = DriverManager.getConnection(url);

			return action(connection, programName);
		}
		else
		{
			// URL指定なし

			message =
				"コンテキストパラメータ「RadioTopicSqlserverUrl」が未定義です";

			return "error";
		}
	}

	/**
	 * コンプリートグラフページ表示アクション。
	 * @param connection DB接続オブジェクト
	 * @param programName 番組名
	 * @return 成否文字列
	 */
	private String action(Connection connection, String programName)
		throws SQLException, ParserConfigurationException,
			TransformerConfigurationException,
			TransformerFactoryConfigurationError, ParseException
	{
		try
		{
			SortOrder sortOrder2 = SortOrder.values()[sortOrder];

			DayCollection dayCollection =
				new DayCollection(connection, programid, sortOrder2);

			connection.close();

			int maxNo = 0;
			boolean zero = false;

			for (int i=0 ; i<dayCollection.size() ; i++)
			{
				try
				{
					int intno = Integer.valueOf(dayCollection.get(i).getNo());

					if (maxNo < intno)
					{
						// 最大を上回る

						maxNo = intno;
					}

					if (intno == 0)
					{
						// ゼロ回

						zero = true;
					}
				}
				catch (NumberFormatException exception)
				{
					// 何もしない
				}
			}

			if (zero)
			{
				// ゼロ回あり

				maxNo++;
			}

			UpdateDateAndNoTable updateMap =
				new UpdateDateAndNoTable(dayCollection, originUpdateDate);

			TreeMap<String, CountAndMax> dateAndCount =
				updateMap.getDateAndCount(dayCollection, back);

			completeGraphDocument =
				new CompleteGraphDocument(dateAndCount, maxNo, programName);

			return "success";
		}
		catch (SQLException exception)
		{
			message = exception.getMessage();

			return "error";
		}
	}
}
