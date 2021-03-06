package kumagai.radiotopic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;

import javax.imageio.ImageIO;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import ktool.datetime.DateTime;

/**
 * 年表グラフイメージ。
 * @author kumagai
 */
public class ChronologyBitmap
	extends BufferedImage
{
	/**
	 * テストコード。
	 * @param args 指定あり＝DBデータ／なし＝決め打ちデータ
	 */
	public static void main(String[] args)
		throws IOException, SQLException, ParseException
	{
		ProgramCollection programCollection;

		if (args.length > 0)
		{
			// DB

			DriverManager.registerDriver(new SQLServerDriver());

			Connection connection = RadioTopicDatabase.getConnection();
			programCollection = new ProgramCollection(connection);
			connection.close();
		}
		else
		{
			// 決め打ちデータ

			programCollection = new ProgramCollection();
			programCollection.add(new Program(0, "あどりぶ", null, "2014/04/12-", 0, null, null, null));
			programCollection.add(new Program(0, "あか☆ぷろ", null, "2010/06/18-2011/06/03", 0, null, null, null));
			programCollection.add(new Program(0, "aりえしょん", null, "2015/04/09-", 0, null, null, null));
			programCollection.add(new Program(0, "ぼーけんの書", null, "2007/10/05-2007/12/28", 0, null, null, null));
		}

		BufferedImage readImage =
			new ChronologyBitmap(
				new ChronologyGraphData(programCollection, 800, 600, 2000));

		ImageIO.write(readImage, "png", new File("../Chronology.png"));
	}

	/**
	 * 年表グラフイメージ構築。
	 * @param chronologyGraphData グラフデータ
	 */
	public ChronologyBitmap(ChronologyGraphData chronologyGraphData)
		throws ParseException
	{
		super(chronologyGraphData.width, chronologyGraphData.height, BufferedImage.TYPE_INT_BGR);

		Graphics2D graphics = createGraphics();

		// 背景
		graphics.setPaint(new Color(0xee, 0xee, 0xee));
		graphics.fillRect(0, 0, chronologyGraphData.width, chronologyGraphData.height);
		graphics.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);

		Font font = graphics.getFont();
		graphics.setFont(new Font(font.getName(), font.getStyle(), 15));

		// 縦線
		graphics.setPaint(Color.black);
		int i = 0;
		for (DateTime day=chronologyGraphData.min ; day.compareTo(chronologyGraphData.max) < 0 ; day.addDay(1))
		{
			if (day.getMonth() == 1 && day.getDay() == 1)
			{
				int x = (int)(i * chronologyGraphData.scaleX);
				graphics.drawLine(x, 0, x, chronologyGraphData.height);
				graphics.drawString(String.format("%d年", day.getYear()), x + 4, 15);
			}

			i++;
		}

		// 棒グラフ
		Color barColor = new Color(0xcc, 0xcc, 0xff);
		for (ChronologyGraphDataElement element : chronologyGraphData)
		{
			graphics.setPaint(barColor);
			graphics.fillRect(element.x, element.y, element.width, element.height);
			graphics.setPaint(Color.blue);
			graphics.drawRect(element.x, element.y, element.width, element.height);

			graphics.setPaint(Color.black);
			graphics.drawString(element.name, element.x + 5, element.y + (int)chronologyGraphData.scaleY / 2 + 6);
		}
	}
}
