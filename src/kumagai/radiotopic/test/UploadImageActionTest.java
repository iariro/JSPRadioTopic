package kumagai.radiotopic.test;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import kumagai.radiotopic.ImageTrimming;
import kumagai.radiotopic.struts2.TrimImageException;

public class UploadImageActionTest
	extends TestCase
{
	public void testTrimNiconicoImage()
		throws IOException
	{
		assertTrue(ImageTrimming.trimNiconicoImage(
			new File("testdata/nolsol22.png"),
			new File("testdata/nolsol22-2.png"),
			"png"));
		assertTrue(ImageTrimming.trimNiconicoImage(
			new File("testdata/nolsol30.png"),
			new File("testdata/nolsol30-2.png"),
			"png"));
		assertTrue(ImageTrimming.trimNiconicoImage(
			new File("testdata/nolsol34.png"),
			new File("testdata/nolsol34-2.png"),
			"png"));
		assertTrue(ImageTrimming.trimNiconicoImage(
			new File("testdata/nolsol83.png"),
			new File("testdata/nolsol83-2.png"),
			"png"));
		assertTrue(ImageTrimming.trimNiconicoImage(
			new File("testdata/futapathy11-2.png"),
			new File("testdata/futapathy11-2-2.png"),
			"png"));
		assertTrue(ImageTrimming.trimNiconicoImage(
			new File("testdata/futapathy17-1.png"),
			new File("testdata/futapathy17-1-2.png"),
			"png"));
	}

	public void testTrimNiconicoImage21()
		throws IOException, TrimImageException
	{
		assertTrue(ImageTrimming.trimBorderImage(
			new File("testdata/shitamuki40.png"),
			new File("testdata/shitamuki40-2.png"),
			"png"));
	}

	public void testTrimNiconicoImage22()
		throws IOException, TrimImageException
	{
		assertTrue(ImageTrimming.trimBorderImage(
			new File("testdata/toshitai072-1.png"),
			new File("testdata/toshitai072-1-2.png"),
			"png"));
	}

	public void testTrimNiconicoImage23()
		throws IOException, TrimImageException
	{
		assertTrue(ImageTrimming.trimBorderImage(
			new File("testdata/himitsukichi103.png"),
			new File("testdata/himitsukichi103-2.png"),
			"png"));
	}

	public void testTrimNiconicoImage24()
		throws IOException, TrimImageException
	{
		assertTrue(ImageTrimming.trimBorderImage(
			new File("testdata/shitamuki27-1.png"),
			new File("testdata/shitamuki27-1-2.png"),
			"png"));
	}

	public void testTrimNiconicoImage25()
		throws IOException, TrimImageException
	{
		assertTrue(ImageTrimming.trimBorderImage(
			new File("testdata/shitamuki27-3.png"),
			new File("testdata/shitamuki27-3-2.png"),
			"png"));
	}

	public void testTrimNiconicoImage26()
		throws IOException, TrimImageException
	{
		assertTrue(ImageTrimming.trimBorderImage(
			new File("testdata/shitamuki27-5.png"),
			new File("testdata/shitamuki27-5-2.png"),
			"png"));
	}
}
