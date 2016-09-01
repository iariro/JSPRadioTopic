package kumagai.radiotopic;

/**
 * 現状値と（その時点での）総数
 * @author kumagai
 */
public class CountAndMax
{
	public final int count;
	public final int total;

	/**
	 * 指定の値をメンバーに割り当て。
	 * @param count 現状値
	 * @param total 総数
	 */
	public CountAndMax(int count, int total)
	{
		this.count = count;
		this.total = total;
	}
}
