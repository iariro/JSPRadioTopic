package kumagai.radiotopic;

import java.util.ArrayList;

/**
 * Highchartsグラフ出力用データ型
 */
public class NameAndData
{
	public final String name;
	public final ArrayList<Object> data = new ArrayList<Object>();

	/**
	 * 値割り当て
	 * @param name データ名
	 */
	public NameAndData(String name)
	{
		this.name = name;
	}
}
