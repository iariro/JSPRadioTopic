package kumagai.radiotopic.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import junit.framework.TestCase;
import ktool.datetime.DateTime;
import kumagai.radiotopic.Day;
import kumagai.radiotopic.DayCollection;
import kumagai.radiotopic.RadioTopicDatabase;
import kumagai.radiotopic.SortOrder;
import kumagai.radiotopic.struts2.UpdateDateAndNoTable;

public class UpdateDateAndNoTableTest
	extends TestCase
{
	public static void main(String[] args)
		throws SQLException
	{
		DriverManager.registerDriver(new SQLServerDriver());

		Connection connection = RadioTopicDatabase.getConnection();

		SortOrder sortOrder2 = SortOrder.values()[0];

		DayCollection dayCollection = new DayCollection(connection, 1, sortOrder2);

		connection.close();

		UpdateDateAndNoTable updateDateAndNoTable =
			new UpdateDateAndNoTable(dayCollection, "2015/10/01");

		String ids = updateDateAndNoTable.getIdArrayString();

		System.out.println(ids);

		String dates = updateDateAndNoTable.getDatesString();

		System.out.println(dates);
	}

	public void test1()
		throws ParseException
	{
		DayCollection dayCollection = new DayCollection();

		dayCollection.add(
			new Day(
				null,
				0,
				0,
				null,
				"1",
				DateTime.parseDateString("2015/10/02"),
				DateTime.parseDateString("2015/10/02")));

		UpdateDateAndNoTable updateDateAndNoTable =
			new UpdateDateAndNoTable(dayCollection, "2015/10/01");

		assertEquals(
			"[[[],[1]]]",
			updateDateAndNoTable.getIdArrayString());
		assertEquals("['2015/10/02']", updateDateAndNoTable.getDatesString());
	}

	public void test2()
		throws ParseException
	{
		DayCollection dayCollection = new DayCollection();

		dayCollection.add(
			new Day(
				null,
				0,
				0,
				null,
				"1",
				DateTime.parseDateString("2015/10/02"),
				DateTime.parseDateString("2015/10/03")));

		UpdateDateAndNoTable updateDateAndNoTable =
			new UpdateDateAndNoTable(dayCollection, "2015/10/01");

		assertEquals(
			"[[[1],[]],[[],[1]]]",
			updateDateAndNoTable.getIdArrayString());
		assertEquals("['2015/10/02','2015/10/03']", updateDateAndNoTable.getDatesString());
	}

	public void test3()
		throws ParseException
	{
		DayCollection dayCollection = new DayCollection();

		dayCollection.add(
			new Day(
				null,
				0,
				0,
				null,
				"1",
				DateTime.parseDateString("2015/10/02"),
				DateTime.parseDateString("2015/10/03")));
		dayCollection.add(
			new Day(
				null,
				0,
				0,
				null,
				"2",
				DateTime.parseDateString("2015/10/03"),
				DateTime.parseDateString("2015/10/04")));

		UpdateDateAndNoTable updateDateAndNoTable =
			new UpdateDateAndNoTable(dayCollection, "2015/10/01");

		assertEquals(
			"[[[1],[]],[[2],[1]],[[],[2]]]",
			updateDateAndNoTable.getIdArrayString());
		assertEquals("['2015/10/02','2015/10/03','2015/10/04']", updateDateAndNoTable.getDatesString());
	}
}
