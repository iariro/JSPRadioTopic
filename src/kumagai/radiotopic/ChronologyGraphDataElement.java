package kumagai.radiotopic;

/**
 * 年表グラフデータを構成する１番組分の情報。
 * @author kumagai
 */
public class ChronologyGraphDataElement
{
	public String name;
	public int id;
	public int sortOrder;
	int x;
	int y;
	int width;
	int height;

	/**
	 * 指定の値をメンバーに割り当てる。
	 * @param name 番組名
	 * @param id 番組ID
	 * @param sortOrder ソートオーダー
	 * @param x X座標
	 * @param y Y座標
	 * @param width 横幅
	 * @param height 縦幅
	 */
	public ChronologyGraphDataElement(String name, int id, int sortOrder, int x,
		int y, int width, int height)
	{
		this.name = name;
		this.id = id;
		this.sortOrder = sortOrder;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * 指定の値をメンバーに割り当てる。
	 * @return クリッカブルマップ用座標文字列を生成・取得
	 */
	public String getCoords()
	{
		return String.format("%d,%d,%d,%d", x, y, x + width, y + height);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return String.format("%s x=%d Y=%d width=%d height=%d", name, x, y, width, height);
	}
}
