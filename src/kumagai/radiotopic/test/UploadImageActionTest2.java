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
	public void testNiconico1() throws IOException
	{
		trimAndSave("futapathy11-2");
	}
	public void testNiconico2() throws IOException
	{
		trimAndSave("futapathy17-1");
	}
	public void testNiconico3() throws IOException
	{
		trimAndSave("nolsol22");
	}
	public void testNiconico4() throws IOException
	{
		trimAndSave("nolsol30");
	}
	public void testNiconico5() throws IOException
	{
		trimAndSave("nolsol34");
	}
	public void testNiconico6() throws IOException
	{
		trimAndSave("nolsol83");
	}
	public void testNiconico7() throws IOException
	{
		trimAndSave("adlib124");
	}
	public void testNiconicoOld1() throws IOException
	{
		trimAndSave("toshitai072-1");
	}
	public void testNiconama1() throws IOException
	{
		trimAndSave("shitamuki27-1");
	}
	public void testNiconama2() throws IOException
	{
		trimAndSave("shitamuki40");
	}
	public void testBilibili() throws IOException
	{
		trimAndSave("himitsukichi103");
	}
	public void testYoutube1() throws IOException
	{
		trimAndSave("shitamuki27-3");
	}
	public void testYoutube2() throws IOException
	{
		trimAndSave("shitamuki27-5");
	}

	void trimAndSave(String file)
		throws IOException
	{
		File sourceFile = new File(String.format("testdata/%s.png", file));
		File destFile = new File(String.format("testdata/%s-2.png", file));
		BufferedImage sourceImage = ImageIO.read(sourceFile);
		MovieRectangle outline = ImageTrimming.findMovieOutline(sourceImage);

		System.out.printf("%s = %d,%d-%d,%d ", file, outline.x1, outline.y1, outline.x2, outline.y2);
		if (!outline.isAnyNull())
		{
			System.out.printf("%dx%d", outline.x2 - outline.x1, outline.y2 - outline.y1);
			ImageTrimming.cutImage(sourceImage, outline, destFile, "png");
		}
		System.out.println();

		assertNotNull(outline.x1);
		assertNotNull(outline.y1);
		assertNotNull(outline.x2);
		assertNotNull(outline.y2);
	}
}
