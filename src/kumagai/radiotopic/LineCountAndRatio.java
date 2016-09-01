package kumagai.radiotopic;

/**
 * 文字列長カウント積み上げグラフ用１データ
 * @author kumagai
 */
public class LineCountAndRatio
{
	public int count;
	public float stackRatio;

	/**
	 * 指定の値をメンバーに割り当てる。
	 * @param count カウント
	 * @param stackRatio 全体の割合
	 */
	public LineCountAndRatio(int count, float stackRatio)
	{
		this.count = count;
		this.stackRatio = stackRatio;
	}
}
