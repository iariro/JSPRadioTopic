package kumagai.radiotopic.exporttext;

import java.io.*;

/**
 * 回数のみ出力するオブジェクト
 * @author kumagai
 */
class NoPrinter
	extends DateNoPrinter
{
	/**
	 * 基底クラスの初期化
	 * @param writer
	 */
	NoPrinter(PrintWriter writer, int maxNo)
	{
		super(writer, maxNo);
	}

	/**
	 * @see kumagai.radiotopic.exporttext.DateNoPrinter#printDateNo(java.lang.String, java.lang.String)
	 */
	public void printDateNo(String date, String no)
	{
		printNo(no);
	}
}
