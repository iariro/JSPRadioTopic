package kumagai.radiotopic.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import junit.framework.TestCase;
import kumagai.radiotopic.ChronologyGraphData;
import kumagai.radiotopic.ChronologyGraphDataElement;
import kumagai.radiotopic.Program;
import kumagai.radiotopic.ProgramCollection;
import kumagai.radiotopic.RadioTopicDatabase;

public class ChronologyGraphDataTest
	extends TestCase
{
	public void test1()
		throws ParseException
	{
		ProgramCollection programCollection = new ProgramCollection();
		programCollection.add(new Program(0, "あどりぶ", null, "2014/04/12-", 0, null, null, null));
		programCollection.add(new Program(0, "あか☆ぷろ", null, "2010/06/18-2011/06/03", 0, null, null, null));
		programCollection.add(new Program(0, "aりえしょん", null, "2015/04/09-", 0, null, null, null));
		programCollection.add(new Program(0, "ぼーけんの書", null, "2007/10/05-2007/12/28", 0, null, null, null));
		ChronologyGraphData chronologyGraphData =
			new ChronologyGraphData(programCollection, 800, 600, 2000);
		System.out.printf(
			"min=%s max=%s dayRange=%s\n",
			chronologyGraphData.min,
			chronologyGraphData.max,
			chronologyGraphData.dayRange.getDay());

		for (ChronologyGraphDataElement element : chronologyGraphData)
		{
			System.out.println(element);
		}
	}

	public void test2()
		throws ParseException, SQLException
	{
		Connection connection = RadioTopicDatabase.getConnection();
		ProgramCollection programCollection =
			new ProgramCollection(connection);
		connection.close();

		ChronologyGraphData chronologyGraphData = new ChronologyGraphData(
			programCollection,
			1200,
			800,
			2020);
	}
}
