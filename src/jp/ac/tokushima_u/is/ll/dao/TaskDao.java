package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

//import jp.ac.tokushima_u.is.ll.dto.ItemDTO;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.ItemTitle;
import jp.ac.tokushima_u.is.ll.entity.Task;
import jp.ac.tokushima_u.is.ll.entity.Tasklevel;
import jp.ac.tokushima_u.is.ll.form.ItemSearchCondForm;
import jp.ac.tokushima_u.is.ll.form.TaskEditForm;

public interface TaskDao {

	public Task findTaskById(String taskId);
	public void insert(Task task);
	public List<Task> findTaskALL();
	public List<ItemTitle> findTaskrelated(String taskId);
	public List<Item> findrelatedimage(String taskId);
	public void updateLevelById(Tasklevel tasklevel);
	public void updatetask(@Param("title")String title,@Param("place")String place,@Param("level")String level,@Param("taskid")String id);
	public List<Task> getalltask();
	public Task getsingletask(String id);
	public List<Task> getsearchtask(@Param("title")String title,@Param("place")String place,@Param("level")String level);
	public void updatetask2(@Param("script")String script, @Param("taskid")String taskId, @Param("num")String num);
	public void delete(@Param("taskid")String id);
	public void detelescript(@Param("taskid")String id);
	public void deleteitem(@Param("taskid")String id);
	public List<Task> searchListByCond(@Param("cond")TaskEditForm form, @Param("page")Pageable pageable);
	public Long countByCond(TaskEditForm form);
	
	
	public List<Task> searchListselect(@Param("cond")TaskEditForm form, @Param("page")Pageable pageable,  @Param("title")String title,  @Param("place")String place, @Param("level") String level);
	public Long countByCondselect(TaskEditForm form, @Param("title")String title, @Param("place")String place, @Param("level")String level);
	
}
