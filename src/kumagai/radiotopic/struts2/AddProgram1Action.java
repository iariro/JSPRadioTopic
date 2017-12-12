package kumagai.radiotopic.struts2;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import ktool.datetime.DateTime;

/**
 * 番組追加ページ表示アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Result(name="success", location="/radiotopic/addprogram1.jsp")
public class AddProgram1Action
{
	public String originUpdateDate;

	/**
	 * 番組追加アクション。
	 * @return 処理結果
	 */
	@Action("addprogram1")
	public String execute()
		throws Exception
	{
		DateTime today = new DateTime();
		today.setDay(1);
		originUpdateDate = today.toString();

		return "success";
	}
}
