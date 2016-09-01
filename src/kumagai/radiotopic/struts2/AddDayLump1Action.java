package kumagai.radiotopic.struts2;

import org.apache.struts2.convention.annotation.*;

/**
 * 一括日追加ページ表示アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Result(name="success", location="/radiotopic/adddaylump1.jsp")
public class AddDayLump1Action
{
	public int programName;
	public int programid;

	public String date;
	public String no;
	public String text;
	public int newid;

	/**
	 * 一括日追加ページ表示アクション。
	 * @return 処理結果
	 */
	@Action("adddaylump1")
	public String execute()
		throws Exception
	{
		return "success";
	}
}
