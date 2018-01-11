package kumagai.radiotopic.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import junit.framework.TestCase;
import kumagai.radiotopic.ImageTrimming;
import kumagai.radiotopic.MovieRectangle;

public class UploadImageActionTest2
	extends TestCase
{
	public void testNiconico()
		throws IOException
	{
		String [] files =
		{
			// ニコニコ
			"futapathy11-2", "futapathy17-1",
			"nolsol22", "nolsol30", "nolsol34", "nolsol83", "adlib124",

			// ニコ生
			"shitamuki27-1", "shitamuki40",

			// ニコニコ
			"toshitai072-1",

			// bilibili
			"himitsukichi103",

			// YouTube
			"shitamuki27-3", "shitamuki27-5"
		};

		for (String file : files)
		{
			File sourceFile = new File(String.format("testdata/%s.png", file));
			File destFile = new File(String.format("testdata/%s-2.png", file));
			MovieRectangle outline = ImageTrimming.findMovieOutline(sourceFile);

			//assertNotNull(outline.x1);
			//assertNotNull(outline.y1);
			//assertNotNull(outline.x2);
			//assertNotNull(outline.y2);

			if (!outline.isAnyNull())
			{
				System.out.printf("%s = %d,%d-%d,%d %dx%d\n", file, outline.x1, outline.y1, outline.x2, outline.y2, outline.x2 - outline.x1, outline.y2 - outline.y1);
				BufferedImage sourceImage = ImageIO.read(sourceFile);

				ImageTrimming.cutImage(sourceImage, outline, destFile, "png");
			}
			else
			{
				System.out.printf("%s = %d,%d-%d,%d\n", file, outline.x1, outline.y1, outline.x2, outline.y2);
			}
		}
	}
}
