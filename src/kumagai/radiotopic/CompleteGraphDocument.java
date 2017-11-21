package kumagai.radiotopic;

import java.awt.Dimension;
import java.awt.Point;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.w3c.dom.Element;

import ktool.datetime.DateTime;
import ktool.xml.KDocument;

/**
 * コンプリートグラフドキュメント。
 * @author kumagai
 */
public class CompleteGraphDocument
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
		TreeMap<String, CountAndMax> dateAndCount =
			new TreeMap<String, CountAndMax>();

		dateAndCount.put("2015/10/01", new CountAndMax(1, 50));
		dateAndCount.put("2015/10/05", new CountAndMax(5, 50));
		dateAndCount.put("2015/10/06", new CountAndMax(6, 50));
		dateAndCount.put("2015/12/10", new CountAndMax(20, 50));

		CompleteGraphDocument completeGraphDocument =
			new CompleteGraphDocument(dateAndCount, 50, "abc");

		Transformer transformer =
			TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
		transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

		FileOutputStream stream = new FileOutputStream("../out.svg");

		completeGraphDocument.write
			(transformer, new OutputStreamWriter(stream));

		stream.close();
	}

	/**
	 * 日ごとの更新件数をカウントを受けグラフを構築する。
	 * @param dateAndCount 日ごとの更新件数カウントコレクション
	 * @param maxNo 最大の回数番号
	 */
	public CompleteGraphDocument(TreeMap<String, CountAndMax> dateAndCount,
		int maxNo, String programName)
		throws ParserConfigurationException, TransformerConfigurationException,
			TransformerFactoryConfigurationError, ParseException
	{
		Element top = createElement("svg");
		appendChild(top);

		top.setAttribute("xmlns", "http://www.w3.org/2000/svg");

		Element element = createElement("title");
		top.appendChild(element);
		element.appendChild(createTextNode("コンプリートグラフ"));

		int total = 0;
		String minDate = null;
		String maxDate = null;

		for (Map.Entry<String, CountAndMax> entry : dateAndCount.entrySet())
		{
			total += entry.getValue().count;

			if (minDate == null || minDate.compareTo(entry.getKey()) >= 0)
			{
				// 初回または現状の最小を上回る

				minDate = entry.getKey();
			}

			if (maxDate == null || maxDate.compareTo(entry.getKey()) <= 0)
			{
				// 初回または現状の最大を下回る

				maxDate = entry.getKey();
			}
		}

		DateTime minDate2 = DateTime.parseDateString(minDate);
		DateTime maxDate2 = DateTime.parseDateString(maxDate);

		int max = total * 100 / maxNo;

		if (max < 100)
		{
			// １００未満

			max = ((max + 5) / 5) * 5;
		}

		int dayRange = maxDate2.diff(minDate2).getDay();

		float xscale;
		float yscale;

		if (dayRange > 0)
		{
			// 更新あり

			xscale = (float)screen.width / dayRange;
			yscale = (float)screen.height / max;
		}
		else
		{
			// 更新なし

			xscale = (float)screen.width;
			yscale = (float)screen.height / max;

			// １件あることにする
			dayRange = 1;
		}

		// 枠線
		element = createElement("rect");
		element.setAttribute
			("x", String.valueOf(origin.x - xmargin));
		element.setAttribute
			("y", String.valueOf(origin.y));
		element.setAttribute
			("width", String.valueOf(xscale * dayRange + xmargin * 2));
		element.setAttribute
			("height", String.valueOf(yscale * max));
		element.setAttribute
			("fill", "#dddddd");
		element.setAttribute
			("stroke", "black");
		top.appendChild(element);

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

		// 縦軸目盛
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
					createTextNode(String.valueOf((int)i) + "%"));
				top.appendChild(element);
			}
		}

		boolean dateUpdown = true; // true：上／false=下
		String points = new String();
		DateTime date2 = null;

		total = 0;

		for (Map.Entry<String, CountAndMax> entry : dateAndCount.entrySet())
		{
			if (points.length() > 0)
			{
				// ２個目以降

				points += ",";
			}

			total += entry.getValue().count;

			DateTime date = DateTime.parseDateString(entry.getKey());

			int diffDay = date.diff(minDate2).getDay();

			date2 = date;

			int y = (int)((origin.y + yscale * (max - ((float)total * 100 / entry.getValue().total))));

			// 折れ線の頂点
			points +=
				String.format("%d %d", (int)(origin.x + xscale * diffDay), y);

			// 頂点のマーク
			element = createElement("rect");
			element.setAttribute(
				"x",
				String.valueOf(origin.x + xscale * diffDay - 2));
			element.setAttribute("y", String.valueOf(y - 2));
			element.setAttribute("width", "4");
			element.setAttribute("height", "4");
			element.setAttribute("fill", "blue");
			element.setAttribute("stroke", "blue");
			top.appendChild(element);
		}

		// 折れ線描画
		element = createElement("polyline");
		element.setAttribute("points", points);
		element.setAttribute("stroke", "blue");
		element.setAttribute("fill", "none");
		element.setAttribute("stroke-width", "2");
		top.appendChild(element);

		XScaleLevel xscaleLevel;
		int datex = 0;

		// 間隔チェック
		if (maxDate2.diff(minDate2).getDay() >= 365)
		{
			// １年を超える

			xscaleLevel = XScaleLevel.Year;
		}
		else
		{
			// １年を超えない

			xscaleLevel = XScaleLevel.OneMonth;

			for (Map.Entry<String, CountAndMax> entry : dateAndCount.entrySet())
			{
				DateTime date = DateTime.parseDateString(entry.getKey());

				int diffDay = date.diff(minDate2).getDay();

				if ((int)(origin.x + xscale * diffDay) - datex < 20)
				{
					// 間隔が狭い

					xscaleLevel = XScaleLevel.SomeMonth;
					break;
				}

				datex = (int)(origin.x + xscale * diffDay);
			}
		}

		if (xscaleLevel == XScaleLevel.SomeMonth || xscaleLevel == XScaleLevel.Year)
		{
			// 間隔が狭い

			for (DateTime date = new DateTime(minDate2) ;
				date.compareTo(maxDate2)<0 ;
				date.addDay(1))
			{
				int diffDay = date.diff(minDate2).getDay();

				if (diffDay == 0 || date.getDay() == 1)
				{
					// 月はじめ

					element = createElement("line");
					element.setAttribute(
						"x1",
						String.valueOf(origin.x + xscale * diffDay));
					element.setAttribute(
						"y1",
						String.valueOf(origin.y + screen.height));
					element.setAttribute(
						"x2",
						String.valueOf(origin.x + xscale * diffDay));
					element.setAttribute(
						"y2",
						String.valueOf(origin.y + screen.height + 10));
					element.setAttribute("stroke", "black");
					top.appendChild(element);

					String dateText;

					if (diffDay == 0 || date.getMonth() == 1)
					{
						// 先頭または月初め

						dateText =
							String.format("%d/%02d/%02d", date.getYear(), date.getMonth(), date.getDay());
					}
					else
					{
						// 先頭でも月初めでもない

						dateText =
							String.format("%02d/%02d", date.getMonth(), date.getDay());
					}

					element = createElement("text");
					element.setAttribute(
						"x",
						String.valueOf(origin.x + xscale * diffDay));
					element.setAttribute(
						"y",
						String.valueOf(origin.y + screen.height + 25 + 20));
					element.setAttribute("font-family", fontFamily);
					element.setAttribute("text-anchor", "middle");
					element.appendChild(createTextNode(dateText));
					top.appendChild(element);
				}
				else if ((xscaleLevel != XScaleLevel.Year) &&
					(date.getDay() == 11) || (date.getDay() == 21))
				{
					// １年以内で１０日刻み

					element = createElement("line");
					element.setAttribute(
						"x1",
						String.valueOf(origin.x + xscale * diffDay));
					element.setAttribute(
						"y1",
						String.valueOf(origin.y + screen.height));
					element.setAttribute(
						"x2",
						String.valueOf(origin.x + xscale * diffDay));
					element.setAttribute(
						"y2",
						String.valueOf(origin.y + screen.height + 5));
					element.setAttribute("stroke", "black");
					top.appendChild(element);

					String dateText =
						String.format("%02d", date.getDay());

					element = createElement("text");
					element.setAttribute(
						"x",
						String.valueOf(origin.x + xscale * diffDay));
					element.setAttribute(
						"y",
						String.valueOf(origin.y + screen.height + 25));
					element.setAttribute("font-family", fontFamily);
					element.setAttribute("text-anchor", "middle");
					element.appendChild(createTextNode(dateText));
					top.appendChild(element);
				}
			}
		}
		else
		{
			// 間隔が狭くない

			datex = 0;
			date2 = null;

			for (Map.Entry<String, CountAndMax> entry : dateAndCount.entrySet())
			{
				DateTime date = DateTime.parseDateString(entry.getKey());

				int diffDay = date.diff(minDate2).getDay();

				String dateText;

				if (diffDay == 0 || date.getMonth() == 1)
				{
					// 先頭または月初め

					dateText =
						String.format(
							"%04d/%02d/%02d",
							date.getYear(),
							date.getMonth(),
							date.getDay());
				}
				else
				{
					// 先頭でも月初めでもない

					dateText =
						String.format(
							"%02d/%02d",
							date.getMonth(),
							date.getDay());
				}

				if ((int)(origin.x + xscale * diffDay) - datex < 50)
				{
					// 間隔が狭い

					if (date2 != null)
					{
						// ２個目以降。

						if (date.getYear() == date2.getYear() &&
							date.getMonth() == date2.getMonth())
						{
							// 年月同じ＝同じ月内である。

							dateText = String.format("%02d", date.getDay());
						}
					}

					dateUpdown = !dateUpdown;
				}
				else
				{
					// 間隔が狭くない

					dateUpdown = true;
				}

				date2 = date;

				// 日付
				element = createElement("text");
				element.setAttribute(
					"x",
					String.valueOf(origin.x + xscale * diffDay));
				element.setAttribute(
					"y",
					String.valueOf(origin.y + screen.height + 20 + (dateUpdown ? 0 : 20)));
				element.setAttribute("font-family", fontFamily);
				element.setAttribute("text-anchor", "middle");
				element.appendChild(createTextNode(dateText));
				top.appendChild(element);

				datex = (int)(origin.x + xscale * diffDay);
			}
		}
	}
}
