package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.ItemTitle;
import jp.ac.tokushima_u.is.ll.entity.Task;
import jp.ac.tokushima_u.is.ll.entity.Taskcollaborative;
import jp.ac.tokushima_u.is.ll.entity.Tasklevel;
import jp.ac.tokushima_u.is.ll.form.TaskEditForm;

public interface TaskcollaborativeDao {

	
	public void insert(Taskcollaborative task);

	public Taskcollaborative findTaskcollaborativeById(String id);

	public List<Taskcollaborative> getalltask();

	public List<Taskcollaborative> getsearchtask(@Param("title")String title,@Param("place")String place,@Param("level")String level);

	public Taskcollaborative findTaskById(String taskId);

	public void delete(@Param("taskid")String id);
	public void detelescript(@Param("taskid")String id);
	public void deleteitem(@Param("taskid")String id);

	
}
