package kumagai.radiotopic;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.sql.*;
import java.text.*;
import javax.imageio.*;
import com.microsoft.sqlserver.jdbc.*;

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
				new ChronologyGraphData(programCollection, 800, 600));

		ImageIO.write(readImage, "png", new File("Chronology.png"));
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

		graphics.setPaint(Color.white);
		graphics.fillRect(0, 0, chronologyGraphData.width, chronologyGraphData.height);
		graphics.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);

		BasicStroke wideStroke = new BasicStroke(4.0f);
		graphics.setStroke(wideStroke);
		graphics.setPaint(Color.blue);
		Font font = graphics.getFont();
		graphics.setFont(new Font(font.getName(), font.getStyle(), 20));

		for (ChronologyGraphDataElement element : chronologyGraphData)
		{
			graphics.drawString(element.name, element.x + 10, element.y + chronologyGraphData.scaleY / 2);
			graphics.drawRect(element.x, element.y, element.width, element.height);
		}
	}
}
