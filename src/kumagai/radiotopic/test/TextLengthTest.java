package kumagai.radiotopic.test;

import junit.framework.TestCase;
import kumagai.radiotopic.StringTool;

public class TextLengthTest
	extends TestCase
{
	public void test1()
	{
		String string1 = "あいうえお";
		String string2 = "abcde";
		String string3 = "あいうえおabcde";

		assertEquals(5, string1.length());
		assertEquals(5, string2.length());
		assertEquals(10, string3.length());

		assertEquals(10, StringTool.getLengthByHalfWidth(string1));
		assertEquals(5, StringTool.getLengthByHalfWidth(string2));
		assertEquals(15, StringTool.getLengthByHalfWidth(string3));
	}
}
