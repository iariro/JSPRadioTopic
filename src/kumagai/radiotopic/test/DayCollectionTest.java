package kumagai.radiotopic.test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import junit.framework.TestCase;
import ktool.datetime.DateTime;
import kumagai.radiotopic.Day;
import kumagai.radiotopic.DayAndTopic;
import kumagai.radiotopic.DayAndTopicCollection;
import kumagai.radiotopic.DayCollection;
import kumagai.radiotopic.RadioTopicDatabase;
import kumagai.radiotopic.Topic;
import kumagai.radiotopic.TopicCollection;

public class DayCollectionTest
	extends TestCase
{
	public void testGetCountAtDate()
		throws ParseException
	{
		DayCollection dayCollection = new DayCollection();

		dayCollection.add(new Day(null, 0, 0, DateTime.parseDateString("2015/01/01"), "1", null, null));
		dayCollection.add(new Day(null, 0, 0, DateTime.parseDateString("2015/02/01"), "2", null, null));
		dayCollection.add(new Day(null, 0, 0, DateTime.parseDateString("2015/03/01"), "3", null, null));

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
		throws ParseException
	{
		DayCollection dayCollection = new DayCollection();

		dayCollection.add(new Day(null, 0, 0, DateTime.parseDateString("2015/01/01"), "1", null, DateTime.parseDateString("2015/01/01")));
		dayCollection.add(new Day(null, 0, 0, DateTime.parseDateString("2015/02/01"), "2", null, DateTime.parseDateString("2015/01/10")));

		assertEquals(9, dayCollection.getUpdateRange());
		assertEquals("2015/01/10", dayCollection.getLastUpdate().toString().substring(0, 10));
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
		throws ParseException
	{
		DayAndTopicCollection dayCollection = new DayAndTopicCollection();

		DayAndTopic day = new DayAndTopic(null, 1, DateTime.parseDateString("2015/01/01"), null, null);
		day.topicCollection = new TopicCollection();
		day.topicCollection.add(new Topic(0, 0, "休みボケ"));
		day.topicCollection.add(new Topic(0, 0, "年末年始"));
		day.topicCollection.add(new Topic(0, 0, "ぬーZAP再び"));
		dayCollection.add(day);
		day = new DayAndTopic(null, 2, DateTime.parseDateString("2015/02/01"), null, null);
		day.topicCollection = new TopicCollection();
		day.topicCollection.add(new Topic(0, 0, "クリスマスガチプレゼント"));
		day.topicCollection.add(new Topic(0, 0, "だがしかし"));
		day.topicCollection.add(new Topic(0, 0, "くしゃみ"));
		dayCollection.add(day);
		day = new DayAndTopic(null, 3, DateTime.parseDateString("2015/03/01"), null, null);
		day.topicCollection = new TopicCollection();
		day.topicCollection.add(new Topic(0, 0, "菓子持ち歩き"));
		day.topicCollection.add(new Topic(0, 0, "断乳"));
		day.topicCollection.add(new Topic(0, 0, "乳草ドラマ"));
		dayCollection.add(day);

		assertEquals("[['休みボケ','年末年始','ぬーZAP再び'],['クリスマスガチプレゼント','だがしかし','くしゃみ'],['菓子持ち歩き','断乳','乳草ドラマ']]", dayCollection.createJavaScriptArray());
	}
	
	public void testGetNextListenDay1()
		throws ParseException
	{
		DayCollection dayCollection = new DayCollection();

		dayCollection.add(new DayAndTopic(null, 3, DateTime.parseDateString("2015/01/15"), null, null));
		dayCollection.add(new DayAndTopic(null, 2, DateTime.parseDateString("2015/01/08"), null, null));
		dayCollection.add(new DayAndTopic(null, 1, DateTime.parseDateString("2015/01/01"), null, null));

		Day next = dayCollection.getNextListenDay("A", "2015/01/01", DateTime.parseDateString("2015/2/1"));
		assertEquals("4", next.getNo());
		assertEquals("2015/01/22", next.date.toString());
	}
	
	public void testGetNextListenDay2()
		throws ParseException
	{
		DayCollection dayCollection = new DayCollection();

		dayCollection.add(new DayAndTopic(null, 3, DateTime.parseDateString("2015/01/15"), null, null));
		dayCollection.add(new DayAndTopic(null, 1, DateTime.parseDateString("2015/01/01"), null, null));

		Day next = dayCollection.getNextListenDay("A", "2015/01/01", DateTime.parseDateString("2015/2/1"));
		assertEquals("4", next.getNo());
		assertEquals("2015/01/22", next.date.toString());
	}
	
	public void testGetNextListenDay3()
		throws ParseException
	{
		DayCollection dayCollection = new DayCollection();

		dayCollection.add(new DayAndTopic(null, 3, DateTime.parseDateString("2015/01/15"), null, null));

		Day next = dayCollection.getNextListenDay("A", "2015/01/01", DateTime.parseDateString("2015/2/1"));
		assertNull(next);
	}
	
	public void testGetNextListenDay4()
		throws ParseException
	{
		DayCollection dayCollection = new DayCollection();
		Day next = dayCollection.getNextListenDay("A", "2015/01/01", DateTime.parseDateString("2015/2/1"));
		assertNull(next);
	}
	
	public void testGetNextListenDay5()
		throws ParseException
	{
		DayCollection dayCollection = new DayCollection();
		Day next = dayCollection.getNextListenDay("A", "", DateTime.parseDateString("2015/2/1"));
		assertNull(next);
	}
	
	public void testGetNextListenDay6()
		throws ParseException
	{
		DayCollection dayCollection = new DayCollection();
		Day next = dayCollection.getNextListenDay("A", null, DateTime.parseDateString("2015/2/1"));
		assertNull(next);
	}
}
