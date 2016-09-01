package kumagai.radiotopic.test;

import junit.framework.*;
import kumagai.radiotopic.*;

public class TextLengthTest
	extends TestCase
{
	public void test1()
	{
		String string1 = "あいうえお";
		String string2 = "abcde";
		String string3 = "あいうえおabcde";

		Assert.assertEquals(5, string1.length());
		Assert.assertEquals(5, string2.length());
		Assert.assertEquals(10, string3.length());

		Assert.assertEquals(10, StringTool.getLengthByHalfWidth(string1));
		Assert.assertEquals(5, StringTool.getLengthByHalfWidth(string2));
		Assert.assertEquals(15, StringTool.getLengthByHalfWidth(string3));
	}
}
