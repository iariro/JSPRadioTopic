package kumagai.radiotopic.test;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import kumagai.radiotopic.DayCollection;
import kumagai.radiotopic.struts2.TrimImageException;

public class UploadImageActionTest
	extends TestCase
{
	public void testTrimNiconicoImage()
		throws IOException
	{
		assertTrue(DayCollection.trimNiconicoImage(
			new File("testdata/ノルカソルカ22.png"),
			new File("testdata/ノルカソルカ22-2.png"),
			"png"));
		assertTrue(DayCollection.trimNiconicoImage(
			new File("testdata/ノルカソルカ30.png"),
			new File("testdata/ノルカソルカ30-2.png"),
			"png"));
		assertTrue(DayCollection.trimNiconicoImage(
			new File("testdata/ノルカソルカ34.png"),
			new File("testdata/ノルカソルカ34-2.png"),
			"png"));
		assertTrue(DayCollection.trimNiconicoImage(
			new File("testdata/ノルカソルカ83.png"),
			new File("testdata/ノルカソルカ83-2.png"),
			"png"));
		assertTrue(DayCollection.trimNiconicoImage(
			new File("testdata/ふたりはシンパシー11-2.png"),
			new File("testdata/ふたりはシンパシー11-2-2.png"),
			"png"));
		assertTrue(DayCollection.trimNiconicoImage(
			new File("testdata/ふたりはシンパシー17-1.png"),
			new File("testdata/ふたりはシンパシー17-1-2.png"),
			"png"));
	}

	public void testTrimNiconicoImage21()
		throws IOException, TrimImageException
	{
		assertTrue(DayCollection.trimBorderImage(
			new File("testdata/巽悠衣子の下も向いて歩こう＼(^o^)／40.png"),
			new File("testdata/巽悠衣子の下も向いて歩こう＼(^o^)／40-2.png"),
			"png"));
	}

	public void testTrimNiconicoImage22()
		throws IOException, TrimImageException
	{
		assertTrue(DayCollection.trimBorderImage(
			new File("testdata/佐倉としたい大西072-1.png"),
			new File("testdata/佐倉としたい大西072-1-2.png"),
			"png"));
	}

	public void testTrimNiconicoImage23()
		throws IOException, TrimImageException
	{
		assertTrue(DayCollection.trimBorderImage(
			new File("testdata/佳村はるかのひみつきち103.png"),
			new File("testdata/佳村はるかのひみつきち103-2.png"),
			"png"));
	}

	public void testTrimNiconicoImage24()
		throws IOException, TrimImageException
	{
		assertTrue(DayCollection.trimBorderImage(
			new File("testdata/巽悠衣子の下も向いて歩こう＼(^o^)／27-1.png"),
			new File("testdata/巽悠衣子の下も向いて歩こう＼(^o^)／27-1-2.png"),
			"png"));
	}

	public void testTrimNiconicoImage25()
		throws IOException, TrimImageException
	{
		assertTrue(DayCollection.trimBorderImage(
			new File("testdata/巽悠衣子の下も向いて歩こう＼(^o^)／27-3.png"),
			new File("testdata/巽悠衣子の下も向いて歩こう＼(^o^)／27-3-2.png"),
			"png"));
	}

	public void testTrimNiconicoImage26()
		throws IOException, TrimImageException
	{
		assertTrue(DayCollection.trimBorderImage(
			new File("testdata/巽悠衣子の下も向いて歩こう＼(^o^)／27-5.png"),
			new File("testdata/巽悠衣子の下も向いて歩こう＼(^o^)／27-5-2.png"),
			"png"));
	}
}
