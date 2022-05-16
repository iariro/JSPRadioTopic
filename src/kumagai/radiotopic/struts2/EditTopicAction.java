package kumagai.radiotopic.struts2;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import kumagai.radiotopic.TopicCollection;

/**
 * トピック編集アクション。
 * @author kumagai
 */
@Namespace("/radiotopic")
@Result(name="success", location="/radiotopic/edittopic.jsp")
public class EditTopicAction
{
	public int dayid;
	public int topicid;
	public int method;
	public String text;
	public String methodString;

	/**
	 * トピック編集アクション。
	 * @return 処理結果
	 */
	@Action("edittopic")
	public String execute()
		throws Exception
	{
		ServletContext context = ServletActionContext.getServletContext();

		DriverManager.registerDriver(new SQLServerDriver());

		Connection connection =
			DriverManager.getConnection
				(context.getInitParameter("RadioTopicSqlserverUrl"));

		if (method == 1)
		{
			// 変更

			TopicCollection.updateTopic(connection, dayid, topicid, text.trim());
			methodString = "変更";
		}
		else if (method == 2)
		{
			// 削除

			TopicCollection.deleteTopic(connection, dayid, topicid);
			methodString = "削除";
		}
		else if (method == 3 || method == 4)
		{
			// 上下操作

			methodString = "上下操作";

			TopicCollection topicCollection =
				new TopicCollection(connection, dayid);

			int index1 = 0;
			int index2 = 0;

			if (method == 3)
			{
				// 上へ

				for ( ; index1 < topicCollection.size() ; index1++)
				{
					if (topicCollection.get(index1).id == topicid)
					{
						index2 = index1 - 1;
						break;
					}
				}
			}
			if (method == 4)
			{
				// 下へ

				for ( ; index1 < topicCollection.size() ; index1++)
				{
					if (topicCollection.get(index1).id == topicid)
					{
						index2 = index1 + 1;
						break;
					}
				}
			}

			if (index1 != index2 &&
				index1 >= 0 &&
				index1 < topicCollection.size() &&
				index2 >= 0 &&
				index2 < topicCollection.size())
			{
				// 範囲内である

				methodString += String.format("(%d<->%d)", index1, index2);

				int id1 = topicCollection.get(index1).id;
				int id2 = topicCollection.get(index2).id;

				String text1 = TopicCollection.getTopic (connection, id1);
				String text2 = TopicCollection.getTopic (connection, id2);

				TopicCollection.updateTopic(connection, dayid, id1, text2);
				TopicCollection.updateTopic(connection, dayid, id2, text1);
			}
			else
			{
				// 範囲外である

				methodString +=
					String.format(
						"(範囲外 size=%d dayid=%d %d<->%d)",
						topicCollection.size(),
						dayid,
						index1,
						index2);
			}
		}

		return "success";
	}
}
