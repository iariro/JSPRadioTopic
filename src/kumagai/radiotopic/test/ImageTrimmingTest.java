package kumagai.radiotopic.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import junit.framework.TestCase;
import kumagai.radiotopic.ImageTrimming;
import kumagai.radiotopic.MovieRectangle;

public class ImageTrimmingTest
	extends TestCase
{
	// bilibili
	public void testBilibili1() throws IOException
	{
		trimAndSave("bilibili_himitsukichi103");
	}
	public void testBilibili2() throws IOException
	{
		trimAndSave("bilibili_shitamuki11");
	}

	// nico生
	public void testNiconama1() throws IOException
	{
		trimAndSave("niconama_shitamuki27-1");
	}
	public void testNiconama2() throws IOException
	{
		trimAndSave("niconama_shitamuki40");
	}

	// niconico new
	public void testNiconico1() throws IOException
	{
		trimAndSave("niconico_new_futapathy11-2");
	}
	public void testNiconico2() throws IOException
	{
		trimAndSave("niconico_new_futapathy17-1");
	}
	public void testNiconico3() throws IOException
	{
		trimAndSave("niconico_new_nolsol22");
	}
	public void testNiconico4() throws IOException
	{
		trimAndSave("niconico_new_nolsol30");
	}
	public void testNiconico5() throws IOException
	{
		trimAndSave("niconico_new_nolsol34");
	}
	public void testNiconico6() throws IOException
	{
		trimAndSave("niconico_new_nolsol83");
	}
	public void testNiconico7() throws IOException
	{
		trimAndSave("niconico_new_delicate123");
	}
	public void testNiconico8() throws IOException
	{
		trimAndSave("niconico_new_tesapuru");
	}

	// niconico old
	public void testNiconicoOld1() throws IOException
	{
		trimAndSave("niconico_old_adlib124");
	}

	public void testNiconicoOld2() throws IOException
	{
		trimAndSave("niconico_old_toshitai072-1");
	}

	// YouTube
	public void testYoutube1() throws IOException
	{
		trimAndSave("youtube_shitamuki27-3");
	}
	public void testYoutube2() throws IOException
	{
		trimAndSave("youtube_shitamuki27-5");
	}
	public void testYoutube3() throws IOException
	{
		trimAndSave("youtube_nolsol102");
	}
	public void testYoutube4() throws IOException
	{
		trimAndSave("youtube_nolsol237");
	}
	public void testYoutube5() throws IOException
	{
		trimAndSave("youtube_gamemarket");
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
