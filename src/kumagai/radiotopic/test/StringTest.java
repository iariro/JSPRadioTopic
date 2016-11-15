package kumagai.radiotopic.test;

import junit.framework.TestCase;
import kumagai.radiotopic.StringTool;

public class StringTest extends TestCase
{
	public void test1()
	{
		String date = "2015年11月25日";

		if (date.indexOf('.') >= 0)
		{
			date = date.replace('.', '/');
		}
		else if (date.indexOf('年') >= 0)
		{
			date = date.replaceAll("[年月]", "/");
			date = date.replaceAll("日", "");
		}

		assertEquals("2015/11/25", date);
	}

	public void testParseDate1()
	{
		assertEquals("2015/02/02", StringTool.parseDate("15/2/2"));
		assertEquals("2015/02/02", StringTool.parseDate("15.2.2"));
		assertEquals("2015/12/22", StringTool.parseDate("15.12.22"));
		assertEquals("2015/02/02", StringTool.parseDate("2015.2.2"));
		assertEquals("2015/12/22", StringTool.parseDate("2015.12.22"));
		assertEquals("2015/12/22", StringTool.parseDate("2015年12月22"));
		assertEquals("2015/12/22", StringTool.parseDate("2015年12月22日"));

		assertEquals("2015/02/02", StringTool.parseDate("(15/2/2)"));
		assertEquals("2015/02/02", StringTool.parseDate("(15.2.2)"));
		assertEquals("2015/12/22", StringTool.parseDate("(15.12.22)"));
		assertEquals("2015/02/02", StringTool.parseDate("(2015.2.2)"));
		assertEquals("2015/12/22", StringTool.parseDate("(2015.12.22)"));
		assertEquals("2015/12/22", StringTool.parseDate("(2015年12月22)"));
		assertEquals("2015/12/22", StringTool.parseDate("(2015年12月22日)"));

		assertEquals("2015/02/02", StringTool.parseDate("（15/2/2）"));
		assertEquals("2015/02/02", StringTool.parseDate("（15.2.2）"));
		assertEquals("2015/12/22", StringTool.parseDate("（15.12.22）"));
		assertEquals("2015/02/02", StringTool.parseDate("（2015.2.2）"));
		assertEquals("2015/12/22", StringTool.parseDate("（2015.12.22）"));
		assertEquals("2015/12/22", StringTool.parseDate("（2015年12月22）"));
		assertEquals("2015/12/22", StringTool.parseDate("（2015年12月22日）"));
		
		assertEquals("2016/09/07", StringTool.parseDate("9/7"));
	}
}
