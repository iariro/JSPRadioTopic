package kumagai.radiotopic;

/**
 * 画像切り出し用矩形情報
 */
public class MovieRectangle
{
	public Integer x1;
	public Integer y1;
	public Integer x2;
	public Integer y2;

	/**
	 * 指定の値をメンバーに割り当てる
	 * @param x1 左端
	 * @param y1 右端
	 * @param x2 上端
	 * @param y2 下端
	 */
	public MovieRectangle(Integer x1, Integer y1, Integer x2, Integer y2)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	/**
	 * いずれかnullであるか判定
	 * @return true=いずれかnullである
	 */
	public boolean isAnyNull()
	{
		return x1 == null || y1 == null || x2 == null || y2 == null;
	}

	/**
	 * 幅を求める
	 * @return 幅
	 */
	public Integer getWidth()
	{
		if (x1 != null && x2 != null)
		{
			return x2 - x1;
		}
		else
		{
			return null;
		}
	}

	/**
	 * 高さを求める
	 * @return 高さ
	 */
	public Integer getHeight()
	{
		if (y1 != null && y2 != null)
		{
			return y2 - y1;
		}
		else
		{
			return null;
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return String.format("%d,%d-%d,%d", x1, y1, x2, y2);
	}
}
