package kumagai.radiotopic;

import java.awt.Dimension;
import java.awt.Point;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.w3c.dom.Element;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import ktool.xml.KDocument;

/**
 * 文字列長カウント積み上げグラフドキュメント
 * @author kumagai
 */
public class LineCountStatGraphDocument
	extends KDocument
{
	static private final String fontFamily = "MS-Mincho";
	static private final Point origin = new Point(70, 20);
	static private final Dimension screen = new Dimension(1200, 580);
	static private final int xmargin = 10;

	/**
	 * テストコード。
	 * @param args 未使用
	 */
	public static void main(String[] args)
		throws Exception
	{
		DriverManager.registerDriver(new SQLServerDriver());

		Connection connection = RadioTopicDatabase.getConnection();

		DayCollection dayCollection =
			new DayCollection(connection, 1, SortOrder.NumberByNumeric);

		LineCountAndRatioCollection lineCountAndRatioCollection =
			new LineCountAndRatioCollection(connection, dayCollection);

		LineCountStatGraphDocument lineCountStatGraphDocument =
			new LineCountStatGraphDocument("あどりぶ", lineCountAndRatioCollection);

		Transformer transformer =
			TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
		transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

		FileOutputStream stream = new FileOutputStream("../out.svg");

		lineCountStatGraphDocument.write
			(transformer, new OutputStreamWriter(stream));

		stream.close();
	}

	/**
	 * 日ごとの更新件数をカウントを受けグラフを構築する。
	 * @param programName 番組名
	 * @param lineCountAndRatioCollection 文字列長カウントデータ
	 */
	public LineCountStatGraphDocument(String programName,
		LineCountAndRatioCollection lineCountAndRatioCollection)
		throws ParserConfigurationException, TransformerConfigurationException,
			TransformerFactoryConfigurationError, ParseException
	{
		Element top = createElement("svg");
		appendChild(top);

		top.setAttribute("xmlns", "http://www.w3.org/2000/svg");

		Element element = createElement("title");
		top.appendChild(element);
		element.appendChild(createTextNode("文字列長カウント積み上げグラフ"));

		int max = lineCountAndRatioCollection.getMaxCount();

		max = ((max + 5) / 5) * 5;

		float xscale;
		float yscale;

		xscale = (float)screen.width / lineCountAndRatioCollection.size();
		yscale = (float)screen.height / max;

		// 枠線
		element = createElement("rect");
		element.setAttribute
			("x", String.valueOf(origin.x - xmargin));
		element.setAttribute
			("y", String.valueOf(origin.y));
		element.setAttribute
			("width", String.valueOf(xscale * lineCountAndRatioCollection.size() + xmargin * 2));
		element.setAttribute
			("height", String.valueOf(yscale * max));
		element.setAttribute
			("fill", "#dddddd");
		element.setAttribute
			("stroke", "black");
		top.appendChild(element);

		// 件数の縦軸目盛
		for (int i=0 ; i<=max ; i+=5)
		{
			element = createElement("line");
			element.setAttribute(
				"x1",
				String.valueOf(origin.x - xmargin - (i % 10 == 0 ? 10 : 5)));
			element.setAttribute(
				"y1",
				String.valueOf(origin.y + yscale * (max - i)));
			element.setAttribute(
				"x2",
				String.valueOf(origin.x - xmargin));
			element.setAttribute(
				"y2",
				String.valueOf(origin.y + yscale * (max - i)));
			element.setAttribute("stroke", "black");
			top.appendChild(element);

			if (i % 10 == 0)
			{
				// 縦軸ラベル表示のタイミングである

				element = createElement("text");
				element.setAttribute(
					"x",
					String.valueOf(origin.x - xmargin - 15));
				element.setAttribute(
					"y",
					String.valueOf(origin.y + yscale * (max - i) + 5));
				element.setAttribute("font-family", fontFamily);
				element.setAttribute("text-anchor", "end");
				element.appendChild(
					createTextNode(String.valueOf((int)i) + "件"));
				top.appendChild(element);
			}
		}

		// パーセンテージの縦軸目盛
		for (int i=0 ; i<=100 ; i+=10)
		{
			element = createElement("line");
			element.setAttribute(
				"x1",
				String.valueOf(origin.x + xmargin + xscale * lineCountAndRatioCollection.size()));
			element.setAttribute(
				"y1",
				String.valueOf(origin.y + yscale * max * (100 - i) / 100));
			element.setAttribute(
				"x2",
				String.valueOf(origin.x + xmargin + xscale * lineCountAndRatioCollection.size() + 5));
			element.setAttribute(
				"y2",
				String.valueOf(origin.y + yscale * max * (100 - i) / 100));
			element.setAttribute("stroke", "black");
			top.appendChild(element);

			element = createElement("text");
			element.setAttribute(
				"x",
				String.valueOf(origin.x + xmargin + xscale * lineCountAndRatioCollection.size() + 10));
			element.setAttribute(
				"y",
				String.valueOf(origin.y + yscale * max * (100 - i) / 100 + 5));
			element.setAttribute("font-family", fontFamily);
			element.appendChild(
				createTextNode(String.format("%d%%", i)));
			top.appendChild(element);

			if (i >= 10 && i <= 90)
			{
				element = createElement("line");
				element.setAttribute(
					"x1",
					String.valueOf(origin.x - xmargin));
				element.setAttribute(
					"y1",
					String.valueOf(origin.y + yscale * max * (100 - i) / 100));
				element.setAttribute(
					"x2",
					String.valueOf(origin.x + xmargin + xscale * lineCountAndRatioCollection.size()));
				element.setAttribute(
					"y2",
					String.valueOf(origin.y + yscale * max * (100 - i) / 100));
				element.setAttribute("stroke", "black");
				element.setAttribute("stroke-dasharray", "5,5");
				top.appendChild(element);
			}
		}

		int xrange = lineCountAndRatioCollection.maxLength - lineCountAndRatioCollection.minLength + 1;

		// 横軸文字列長目盛り
		for (int i=0 ; i<xrange ; i++)
		{
			if ((i == 0) ||
				(i == xrange-1) ||
				(i >= 5 && (lineCountAndRatioCollection.maxLength - i) % 5 == 0))
			{
				element = createElement("text");
				element.setAttribute(
					"x",
					String.valueOf(origin.x + xscale * i + xscale / 2));
				element.setAttribute(
					"y",
					String.valueOf(origin.y + yscale * max + 25));
				element.setAttribute("font-family", fontFamily);
				element.setAttribute("text-anchor", "middle");
				element.appendChild(
					createTextNode(String.valueOf(lineCountAndRatioCollection.maxLength - i)));
				top.appendChild(element);
			}
		}

		int ratiox = (int)(origin.x - xmargin);
		int ratioy = (int)(origin.y + screen.height);

		StringBuilder points = new StringBuilder();

		points.append(String.format( "%d %d", ratiox, ratioy));

		for (int i=0 ; i<lineCountAndRatioCollection.size() ; i++)
		{
			LineCountAndRatio lineCountAndRatio =
				lineCountAndRatioCollection.get(i);

			element = createElement("rect");
			element.setAttribute
				("x", String.valueOf(origin.x + xscale * i));
			element.setAttribute
				("y", String.valueOf(origin.y + yscale * (max - lineCountAndRatio.count)));
			element.setAttribute
				("width", String.valueOf(xscale));
			element.setAttribute
				("height", String.valueOf(yscale * lineCountAndRatio.count));
			element.setAttribute
				("fill", "#aaaaff");
			element.setAttribute
				("stroke", "black");
			top.appendChild(element);

			points.append(", ");

			ratiox = (int)(origin.x + xscale * i + xscale / 2);
			ratioy = (int)(origin.y + screen.height * (1 - lineCountAndRatio.stackRatio));
			points.append(String.format( "%d %d", ratiox, ratioy));

			element = createElement("rect");
			element.setAttribute("x", String.valueOf(ratiox - 1));
			element.setAttribute("y", String.valueOf(ratioy - 1));
			element.setAttribute("width", "2");
			element.setAttribute("height", "2");
			element.setAttribute("fill", "blue");
			element.setAttribute("stroke", "black");
			top.appendChild(element);
		}

		// 折れ線描画
		element = createElement("polyline");
		element.setAttribute("points", points.toString());
		element.setAttribute("stroke", "blue");
		element.setAttribute("fill", "none");
		element.setAttribute("stroke-width", "2");
		top.appendChild(element);

		// 番組名
		element = createElement("text");
		element.setAttribute(
			"x",
			String.valueOf(origin.x + 5));
		element.setAttribute(
			"y",
			String.valueOf(origin.y + 20));
		element.setAttribute("font-family", fontFamily);
		element.appendChild(
			createTextNode(programName));
		top.appendChild(element);
	}
}
