package kumagai.radiotopic.test;

import java.sql.*;
import com.microsoft.sqlserver.jdbc.*;
import junit.framework.*;
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
	{
		DayCollection dayCollection = new DayCollection();

		dayCollection.add(
			new Day(
				1,
				null,
				Date.valueOf("2015-10-02"),
				Date.valueOf("2015-10-02")));

		UpdateDateAndNoTable updateDateAndNoTable =
			new UpdateDateAndNoTable(dayCollection, "2015/10/01");

		assertEquals(
			"[[{No:'1', Type:'update'}]]",
			updateDateAndNoTable.getIdArrayString());
		assertEquals("['2015/10/02']", updateDateAndNoTable.getDatesString());
	}

	public void test2()
	{
		DayCollection dayCollection = new DayCollection();

		dayCollection.add(
			new Day(
				1,
				null,
				Date.valueOf("2015-10-02"),
				Date.valueOf("2015-10-03")));

		UpdateDateAndNoTable updateDateAndNoTable =
			new UpdateDateAndNoTable(dayCollection, "2015/10/01");

		assertEquals(
			"[[{No:'1', Type:'create'}],[{No:'1', Type:'update'}]]",
			updateDateAndNoTable.getIdArrayString());
		assertEquals("['2015/10/02','2015/10/03']", updateDateAndNoTable.getDatesString());
	}

	public void test3()
	{
		DayCollection dayCollection = new DayCollection();

		dayCollection.add(
			new Day(
				1,
				null,
				Date.valueOf("2015-10-02"),
				Date.valueOf("2015-10-03")));
		dayCollection.add(
			new Day(
				2,
				null,
				Date.valueOf("2015-10-03"),
				Date.valueOf("2015-10-04")));

		UpdateDateAndNoTable updateDateAndNoTable =
			new UpdateDateAndNoTable(dayCollection, "2015/10/01");

		assertEquals(
			"[[{No:'1', Type:'create'}],[{No:'1', Type:'update'},{No:'2', Type:'create'}],[{No:'2', Type:'update'}]]",
			updateDateAndNoTable.getIdArrayString());
		assertEquals("['2015/10/02','2015/10/03','2015/10/04']", updateDateAndNoTable.getDatesString());
	}
}
