package kumagai.radiotopic.test;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import kumagai.radiotopic.DayCollection;

public class UploadImageActionTest
	extends TestCase
{
	public void testTrimNiconicoImage()
		throws IOException
	{
		assertTrue(DayCollection.trimNiconicoImage(
			new File("testdata/ノルカソルカ22.png"),
			new File("testdata/ノルカソルカ22 - コピー.png"),
			"png"));
		assertTrue(DayCollection.trimNiconicoImage(
			new File("testdata/ノルカソルカ30.png"),
			new File("testdata/ノルカソルカ30 - コピー.png"),
			"png"));
		assertTrue(DayCollection.trimNiconicoImage(
			new File("testdata/ノルカソルカ34.png"),
			new File("testdata/ノルカソルカ34 - コピー.png"),
			"png"));
		assertTrue(DayCollection.trimNiconicoImage(
			new File("testdata/ノルカソルカ83.png"),
			new File("testdata/ノルカソルカ83 - コピー.png"),
			"png"));
	}
}
