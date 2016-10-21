package kumagai.radiotopic;

/**
 * 年表グラフデータを構成する１番組分の情報。
 * @author kumagai
 */
public class ChronologyGraphDataElement
{
	String name;
	int x;
	int y;
	int width;
	int height;

	/**
	 * 指定の値をメンバーに割り当てる。
	 * @param name 番組名
	 * @param x X座標
	 * @param y Y座標
	 * @param width 横幅
	 * @param height 縦幅
	 */
	public ChronologyGraphDataElement(String name, int x, int y, int width, int height)
	{
		this.name = name;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
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
