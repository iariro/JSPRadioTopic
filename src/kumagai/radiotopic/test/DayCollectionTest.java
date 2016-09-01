package kumagai.radiotopic.test;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import junit.framework.*;
import com.microsoft.sqlserver.jdbc.*;
import ktool.datetime.*;
import kumagai.radiotopic.*;

public class DayCollectionTest
	extends TestCase
{
	public void testGetCountAtDate()
	{
		DayCollection dayCollection = new DayCollection();

		dayCollection.add(new Day(1, Date.valueOf("2015-01-01"), null, null));
		dayCollection.add(new Day(2, Date.valueOf("2015-02-01"), null, null));
		dayCollection.add(new Day(3, Date.valueOf("2015-03-01"), null, null));

		assertEquals(3, dayCollection.getOverDateCount("2014/12/31"));
		assertEquals(2, dayCollection.getOverDateCount("2015/01/01"));
		assertEquals(1, dayCollection.getOverDateCount("2015/02/01"));
		assertEquals(0, dayCollection.getOverDateCount("2015/03/01"));
		assertEquals(0, dayCollection.getOverDateCount("2015/04/01"));
	}

	public void _testGetRecentUpdateDays()
		throws SQLException
	{
		DriverManager.registerDriver(new SQLServerDriver());

		Connection connection = RadioTopicDatabase.getConnection();

		ArrayList<ArrayList<Day>> daysCollection =
			DayCollection.getRecentUpdateDays(connection, 3);

		for (ArrayList<Day> days : daysCollection)
		{
			for (Day day : days)
			{
				System.out.println(day.getUpdateDateAsString());
			}
		}

		connection.close();
	}

	public void testGetUpdateRange()
	{
		DayCollection dayCollection = new DayCollection();

		dayCollection.add(new Day(1, Date.valueOf("2015-01-01"), null, Date.valueOf("2015-01-01")));
		dayCollection.add(new Day(2, Date.valueOf("2015-02-01"), null, Date.valueOf("2015-01-10")));

		assertEquals(9, dayCollection.getUpdateRange());
		assertEquals("2015-01-10", dayCollection.getLastUpdate().toString().substring(0, 10));
	}

	public void testDateDiff()
	{
		Timestamp timestamp =
			new Timestamp(Date.valueOf("2015-01-10").getTime());
		DateTime today = new DateTime(2015, 1, 20, 0, 0, 0);
		int diff = today.diff(new DateTime(timestamp)).getDay();

		assertEquals(10, diff);
	}

	public void testCreateJavaScriptArray()
	{
		DayCollection dayCollection = new DayCollection();

		Day day = new Day(1, Date.valueOf("2015-01-01"), null, null);
		day.topicCollection = new TopicCollection();
		day.topicCollection.add(new Topic(0, 0, "休みボケ"));
		day.topicCollection.add(new Topic(0, 0, "年末年始"));
		day.topicCollection.add(new Topic(0, 0, "ぬーZAP再び"));
		dayCollection.add(day);
		day = new Day(2, Date.valueOf("2015-02-01"), null, null);
		day.topicCollection = new TopicCollection();
		day.topicCollection.add(new Topic(0, 0, "クリスマスガチプレゼント"));
		day.topicCollection.add(new Topic(0, 0, "だがしかし"));
		day.topicCollection.add(new Topic(0, 0, "くしゃみ"));
		dayCollection.add(day);
		day = new Day(3, Date.valueOf("2015-03-01"), null, null);
		day.topicCollection = new TopicCollection();
		day.topicCollection.add(new Topic(0, 0, "菓子持ち歩き"));
		day.topicCollection.add(new Topic(0, 0, "断乳"));
		day.topicCollection.add(new Topic(0, 0, "乳草ドラマ"));
		dayCollection.add(day);

		assertEquals("[['休みボケ','年末年始','ぬーZAP再び'],['クリスマスガチプレゼント','だがしかし','くしゃみ'],['菓子持ち歩き','断乳','乳草ドラマ']]", dayCollection.createJavaScriptArray());
	}
}
