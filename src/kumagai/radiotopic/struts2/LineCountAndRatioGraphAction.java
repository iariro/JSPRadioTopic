package kumagai.radiotopic.struts2;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletContext;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import kumagai.radiotopic.DayCollection;
import kumagai.radiotopic.LineCountAndRatioCollection;
import kumagai.radiotopic.LineCountStatGraphDocument;
import kumagai.radiotopic.SortOrder;

/**
 * 文字列長カウント積み上げグラフ表示アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Results
({
	@Result(name="success", location="/radiotopic/linecountstatgraph.jsp"),
	@Result(name="error", location="/radiotopic/error.jsp")
})
public class LineCountAndRatioGraphAction
{
	public int programid;
	public String programName;
	public int sortOrder;

	public LineCountStatGraphDocument document;
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
		document.write(transformer, writer);

		return writer.toString();
	}

	/**
	 * 文字列長カウント積み上げグラフ表示アクション。
	 * @return 処理結果
	 */
	@Action("linecountstatgraph")
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

			SortOrder sortOrder2 = SortOrder.values()[sortOrder];

			DayCollection dayCollection =
				new DayCollection(connection, programid, sortOrder2);

			LineCountAndRatioCollection lineCountAndRatioCollection =
				new LineCountAndRatioCollection(connection, dayCollection);

			connection.close();

			document =
				new LineCountStatGraphDocument
					(programName, lineCountAndRatioCollection);

			return "success";
		}
		else
		{
			// URL指定なし

			message =
				"コンテキストパラメータ「RadioTopicSqlserverUrl」が未定義です";

			return "error";
		}
	}
}
