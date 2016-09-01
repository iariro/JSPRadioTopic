package kumagai.radiotopic.exporttext;

import java.io.*;

/**
 * 日付のみ出力するオブジェクト
 * @author kumagai
 */
class DatePrinter
	extends DateNoPrinter
{
	/**
	 * 基底クラスの初期化
	 * @param writer
	 */
	DatePrinter(PrintWriter writer)
	{
		super(writer, 0);
	}

	/**
	 * @see kumagai.radiotopic.exporttext.DateNoPrinter#printDateNo(java.lang.String, java.lang.String)
	 */
	public void printDateNo(String date, String no)
	{
		printDate(date);
	}
}
