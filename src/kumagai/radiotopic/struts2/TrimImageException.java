package kumagai.radiotopic.struts2;

/**
 * 画像トリミングエラー情報
 * @author kumagai
 *
 */
public class TrimImageException
	extends Exception
{
	/**
	 * 基底クラスの初期化
	 * @param message メッセージ
	 */
	public TrimImageException(String message)
	{
		super(message);
	}
}
