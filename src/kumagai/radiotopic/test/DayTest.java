package kumagai.radiotopic.test;

import junit.framework.*;
import kumagai.radiotopic.*;

public class DayTest
	extends TestCase
{
	public void test1()
	{
		assertEquals("0", StringTool.trimZero("0"));
		assertEquals("0", StringTool.trimZero("00"));
		assertEquals("123", StringTool.trimZero("0123"));
		assertEquals("123", StringTool.trimZero("00123"));
	}
}
