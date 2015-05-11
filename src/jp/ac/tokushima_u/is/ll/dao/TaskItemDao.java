package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.TaskItem;

public interface TaskItemDao {

	
//	public void insert(Task task);

	public void insert(TaskItem taskitem);
	public List<TaskItem> findItemid(String taskId);
	public void delete(String itemIds);
	public void collaborativeinsert(TaskItem taskitem);
	public void deletecollaborative(String itemIds);
	
}
