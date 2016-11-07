package kumagai.radiotopic.test;

import java.sql.*;
import java.text.*;
import com.microsoft.sqlserver.jdbc.*;
import junit.framework.*;
import ktool.datetime.*;
import kumagai.radiotopic.*;
import kumagai.radiotopic.struts2.*;

public class UpdateDateAndNoTableTest
	extends TestCase
{
	public static void main(String[] args)
		throws SQLException
	{
		DriverManager.registerDriver(new SQLServerDriver());

		Connection connection = RadioTopicDatabase.getConnection();

		SortOrder sortOrder2 = SortOrder.values()[0];

		DayCollection dayCollection =
			new DayCollection(connection, 1, sortOrder2);

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
				1,
				null,
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
				1,
				null,
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
				1,
				null,
				DateTime.parseDateString("2015/10/02"),
				DateTime.parseDateString("2015/10/03")));
		dayCollection.add(
			new Day(
				null,
				2,
				null,
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
