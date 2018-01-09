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
}
