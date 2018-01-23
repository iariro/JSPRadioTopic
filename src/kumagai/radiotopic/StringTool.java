package kumagai.radiotopic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ktool.datetime.DateTime;

/**
 * 文字列関連
 * @author kumagai
 */
public class StringTool
{
	static private final Pattern dateSlash2 =
		Pattern.compile("[\\(（]*([0-9]{2})/([0-9]*)/([0-9]*)[\\)）]*");
	static private final Pattern dateDot0 =
			Pattern.compile("([0-9]*)\\.([0-9]*)");
	static private final Pattern dateDot2 =
		Pattern.compile("[\\(（]*([0-9]{2})\\.([0-9]*)\\.([0-9]*)[\\)）]*");
	static private final Pattern dateDot4 =
		Pattern.compile("[\\(（]*([0-9]{4})\\.([0-9]*)\\.([0-9]*)[\\)）]*");
	static private final Pattern dateJapaneseYMD =
		Pattern.compile("[\\(（]*([0-9]{4})年([0-9]*)月([0-9]*)日*[\\)）]*");
	static private final Pattern dateJapaneseMD =
			Pattern.compile("([0-9]*)月([0-9]*)日*");
	static private final String outFormat =
		"%04d/%02d/%02d";

	static private final Pattern dateYearless =
		Pattern.compile("([0-9]*)/([0-9]*)");

	/**
	 * 日付をyyyy/mm/dd形式に整形する。
	 * @param date 対象の日付文字列
	 * @return 整形済みの日付文字列
	 */
	static public String parseDate(String date)
	{
		Matcher matcher = dateDot4.matcher(date);
		if (matcher.matches())
		{
			// yyyy.mm.dd形式にマッチ

			return
				String.format(
					outFormat,
					Integer.parseInt(matcher.group(1)),
					Integer.parseInt(matcher.group(2)),
					Integer.parseInt(matcher.group(3)));
		}

		matcher = dateJapaneseYMD.matcher(date);
		if (matcher.matches())
		{
			// yyyy年mm月dd形式にマッチ

			return
				String.format(
					outFormat,
					Integer.parseInt(matcher.group(1)),
					Integer.parseInt(matcher.group(2)),
					Integer.parseInt(matcher.group(3)));
		}

		matcher = dateJapaneseMD.matcher(date);
		if (matcher.matches())
		{
			// yyyy年mm月dd形式にマッチ

			return
				String.format(
					outFormat,
					new DateTime().getYear(),
					Integer.parseInt(matcher.group(1)),
					Integer.parseInt(matcher.group(2)));
		}

		matcher = dateDot0.matcher(date);
		if (matcher.matches())
		{
			// mm.dd形式にマッチ

			return
				String.format(
					outFormat,
					new DateTime().getYear(),
					Integer.parseInt(matcher.group(1)),
					Integer.parseInt(matcher.group(2)));
		}

		matcher = dateDot2.matcher(date);
		if (matcher.matches())
		{
			// yy.mm.dd形式にマッチ

			int year = Integer.parseInt(matcher.group(1));
			if (year >= 90)
			{
				year += 1900;
			}
			else
			{
				year += 2000;
			}

			return
				String.format(
					outFormat,
					year,
					Integer.parseInt(matcher.group(2)),
					Integer.parseInt(matcher.group(3)));
		}

		matcher = dateSlash2.matcher(date);
		if (matcher.matches())
		{
			// yy/mm/dd形式にマッチ

			int year = Integer.parseInt(matcher.group(1));
			if (year >= 90)
			{
				year += 1900;
			}
			else
			{
				year += 2000;
			}

			return
				String.format(
					outFormat,
					year,
					Integer.parseInt(matcher.group(2)),
					Integer.parseInt(matcher.group(3)));
		}

		matcher = dateYearless.matcher(date);
		if (matcher.matches())
		{
			// mm/dd形式にマッチ

			return
				String.format(
					outFormat,
					new DateTime().getYear(),
					Integer.parseInt(matcher.group(1)),
					Integer.parseInt(matcher.group(2)));
		}

		return date;
	}

	/**
	 * 半角個数で文字列長を求める
	 * @param string 対象の文字列
	 * @return 半角個数での文字列長
	 */
	static public int getLengthByHalfWidth(String string)
	{
		int length = 0;

		for (int i=0 ; i<string.length() ; i++)
		{
			if (string.charAt(i)>=' ' && string.charAt(i)<='~')
			{
				// 半角

				length++;
			}
			else
			{
				// 全角とみなす

				length += 2;
			}
		}

		return length;
	}

	/**
	 * ゼロパディングを削除する。
	 * @param value 対象の文字列
	 * @return 処理済文字列
	 */
	static public String trimZero(String value)
	{
		int index;
		for (index=0 ; index < value.length()-1 ; index++)
		{
			if (value.charAt(index) != '0')
			{
				// ０以外

				break;
			}
		}

		if (index >= 1)
		{
			// ０あり

			value = value.substring(index);
		}
		return value;
	}
}
