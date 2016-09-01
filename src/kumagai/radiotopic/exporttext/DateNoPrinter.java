package kumagai.radiotopic.exporttext;

import java.io.*;

/**
 * 日付と回数出力オブジェクト。エクスポート処理用。
 * @author kumagai
 */
class DateNoPrinter
{
	protected final PrintWriter writer;
	protected final String numFormat;

	/**
	 * 指定のオブジェクトをメンバーに割り当て
	 * @param writer
	 */
	DateNoPrinter(PrintWriter writer, int maxNo)
	{
		this.writer = writer;
		this.numFormat = maxNo >= 100 ? "%3s " : "%2s ";
	}

	/**
	 * 出力オブジェクト
	 * @param date 日付
	 * @param no 回数
	 */
	public void printDateNo(String date, String no)
	{
		printDate(date);
		printNo(no);
	}

	/**
	 * 日付を出力。null指定の場合はスペースパディングする
	 * @param date 日付
	 */
	protected void printDate(String date)
	{
		if (date != null)
		{
			// 日付指定あり

			writer.printf("%10s ", date);
		}
		else
		{
			// 日付指定なし

			writer.print("           ");
		}
	}

	/**
	 * 回数を出力。null指定の場合はスペースパディングする
	 * @param no
	 */
	protected void printNo(String no)
	{
		if (no != null)
		{
			// 回数指定あり

			writer.printf(numFormat, no);
		}
		else
		{
			// 回数指定なし

			writer.print("   ");
		}
	}
}
