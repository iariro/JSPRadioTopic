package kumagai.radiotopic.struts2;

import java.util.*;
import kumagai.radiotopic.*;

/**
 * CompleteTableAction用のDayデータの更新日付によるディクショナリー
 * @author kumagai
 */
public class UpdateDateAndNoTable
	extends TreeMap<String, ArrayList<NoAndUpdateType>>
{
	public static final int cellPerRow = 20;

	/**
	 * DayコレクションからDayデータの更新日付によるディクショナリーを構築する
	 * @param dayCollection Dayコレクション
	 * @param originnDate 更新日付開始日
	 */
	public UpdateDateAndNoTable
		(DayCollection dayCollection, String originnDate)
	{
		for (int i=0 ; i<dayCollection.size() ; i++)
		{
			String updateDate = originnDate;
			String createDate = originnDate;

			if (dayCollection.get(i).updatedate != null)
			{
				// 更新日付あり

				updateDate = dayCollection.get(i).getUpdateDateAsString();
			}

			if (dayCollection.get(i).createdate != null)
			{
				// 更新日付あり

				createDate = dayCollection.get(i).getCreateDateAsString();
			}

			ArrayList<NoAndUpdateType> nos;

			if (! containsKey(updateDate))
			{
				// 初登場

				nos = new ArrayList<NoAndUpdateType>();

				put(updateDate, nos);
			}
			else
			{
				// 既存

				nos = get(updateDate);
			}

			nos.add(
				new NoAndUpdateType(
					dayCollection.get(i).date,
					dayCollection.get(i).getNo(),
					UpdateType.update));

			if (! updateDate.equals(createDate))
			{
				// 新規作成日以降に更新がある

				if (! containsKey(createDate))
				{
					// 初登場

					nos = new ArrayList<NoAndUpdateType>();

					put(createDate, nos);
				}
				else
				{
					// 既存

					nos = get(createDate);
				}

				nos.add(
					new NoAndUpdateType(
						dayCollection.get(i).date,
						dayCollection.get(i).getNo(),
						UpdateType.create));
			}
		}
	}

	/**
	 * 日付の配列文字列を取得する
	 * @return CompleteTableAction用の日付の配列文字列
	 */
	public String getDatesString()
	{
		int count = 0;
		StringBuffer dates = new StringBuffer();

		dates.append("[");

		for (Map.Entry<String, ArrayList<NoAndUpdateType>> entry : entrySet())
		{
			if (count > 0)
			{
				// ２個目以降

				dates.append(",");
			}

			dates.append(String.format("'%s'", entry.getKey()));

			count++;
		}
		dates.append("]");

		return dates.toString();
	}

	/**
	 * テーブルID配列文字列を取得する
	 * @return CompleteTableAction用のテーブルID配列文字列
	 */
	public String getIdArrayString()
	{
		int count = 0;
		StringBuffer ids = new StringBuffer();

		ids.append("[");

		for (Map.Entry<String, ArrayList<NoAndUpdateType>> entry : entrySet())
		{
			if (count > 0)
			{
				// ２個目以降

				ids.append(",");
			}

			ids.append("[[");

			int count2 = 0;
			for (NoAndUpdateType notype : entry.getValue())
			{
				if (notype.type == UpdateType.create)
				{
					// create

					try
					{
						String no =
							String.format
								("%d", Integer.valueOf(notype.no.trim()));

						if (count2 > 0)
						{
							// ２個目以降

							ids.append(",");
						}

						ids.append(no);

						count2++;
					}
					catch (NumberFormatException exception)
					{
						// 何もしない
					}
				}
			}

			ids.append("],[");

			count2 = 0;
			for (NoAndUpdateType notype : entry.getValue())
			{
				if (notype.type == UpdateType.update)
				{
					// update

					try
					{
						String no =
							String.format(
								"%d",
								Integer.valueOf(notype.no.trim()));

						if (count2 > 0)
						{
							// ２個目以降

							ids.append(",");
						}

						ids.append(no);

						count2++;
					}
					catch (NumberFormatException exception)
					{
						// 何もしない
					}
				}
			}

			ids.append("]]");

			count++;
		}
		ids.append("]");

		return ids.toString();
	}

	/**
	 * CompleteTableActionで表示するテーブルのセル用の番号の配列を生成
	 * @param maxNo 最大ナンバー
	 * @return テーブルのセル用の番号の配列
	 */
	public ArrayList<ArrayList<String>> getCellArray(int maxNo)
	{
		ArrayList<ArrayList<String>> cellArray =
			new ArrayList<ArrayList<String>>();

		for (int i=0 ; i<maxNo ; i+=cellPerRow)
		{
			ArrayList<String> row = new ArrayList<String>();
			cellArray.add(row);

			for (int j=0 ; j<cellPerRow && (i + j < maxNo) ; j++)
			{
				row.add(String.format("%d", i + j + 1));
			}
		}

		return cellArray;
	}

	/**
	 * 日ごとの更新件数をカウント。
	 * @return 日ごとの更新件数コレクション
	 */
	public TreeMap<String, CountAndMax> getDateAndCount
		(DayCollection dayCollection, boolean back)
	{
		int total = dayCollection.getMaxNo();

		TreeMap<String, CountAndMax> dateAndCount =
			new TreeMap<String, CountAndMax>();

		for (Map.Entry<String, ArrayList<NoAndUpdateType>> entry : entrySet())
		{
			int count = 0;
			ArrayList<NoAndUpdateType> nos = entry.getValue();

			for (int i=0 ; i<nos.size() ; i++)
			{
				if (nos.get(i).type == UpdateType.update)
				{
					// 更新の場合

					try
					{
						Integer.valueOf(nos.get(i).no.trim());

						count++;
					}
					catch (NumberFormatException exception)
					{
						// 何もしない
					}
				}
			}

			if (back)
			{
				// 更新日の時点でのカウントを取る

				total = dayCollection.getMaxNo() - dayCollection.getOverDateCount(entry.getKey());
			}

			dateAndCount.put(entry.getKey(), new CountAndMax(count, total));
		}

		return dateAndCount;
	}
}
