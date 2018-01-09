package kumagai.radiotopic.test;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import kumagai.radiotopic.ImageTrimming;
import kumagai.radiotopic.MovieRectangle;

public class UploadImageActionTest2
	extends TestCase
{
	public void testNiconico()
		throws IOException
	{
		String [] files = { "nolsol22", "nolsol30", "nolsol34", "nolsol83", "futapathy11-2", "futapathy17-1", "shitamuki40" };

		for (String file : files)
		{
			MovieRectangle outline = ImageTrimming.findMovieOutline(new File(String.format("testdata/%s.png", file)));

			System.out.printf("%s = %d %d %d %d\n", file, outline.x1, outline.y1, outline.x2, outline.y2);
		}
	}
}
