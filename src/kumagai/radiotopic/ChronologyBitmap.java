package kumagai.radiotopic;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class ChronologyBitmap
{
	public static void main(String[] args)
		throws IOException
	{
		BufferedImage readImage = createChronologyBitmap();
		
		ImageIO.write(readImage, "png", new File("sample2.png"));
	}

	private static BufferedImage createChronologyBitmap()
	{
		BufferedImage readImage = new BufferedImage(400, 200, BufferedImage.TYPE_INT_BGR);
		Graphics2D off = readImage.createGraphics();
		off.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		BasicStroke wideStroke = new BasicStroke(4.0f);
		off.setStroke(wideStroke);

		off.setPaint(Color.white);
		off.draw(new Ellipse2D.Double(30, 40, 50, 50));

		off.setPaint(Color.blue);
		off.draw(new Ellipse2D.Double(70, 40, 50, 50));

		off.setPaint(Color.red);
		off.draw(new Ellipse2D.Double(110, 40, 50, 50));
		return readImage;
	}
}
