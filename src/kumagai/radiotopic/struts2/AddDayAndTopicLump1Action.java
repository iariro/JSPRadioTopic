package kumagai.radiotopic.struts2;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

/**
 * 一括日・トピック追加ページ表示アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Result(name="success", location="/radiotopic/adddayandtopiclump1.jsp")
public class AddDayAndTopicLump1Action
{
	public int programName;
	public int programid;
	public int sortOrder;

	/**
	 * 一括日・トピック追加ページ表示アクション。
	 * @return 処理結果
	 */
	@Action("adddayandtopiclump1")
	public String execute()
		throws Exception
	{
		return "success";
	}
}
