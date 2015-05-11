package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import jp.ac.tokushima_u.is.ll.entity.Task;
import jp.ac.tokushima_u.is.ll.entity.TaskScript;
import jp.ac.tokushima_u.is.ll.form.TaskScriptForm;

public interface TaskScriptDao {
	public void insert(TaskScript taskScript);
	public List<TaskScript> findTaskScriptByTaskId(@Param("taskId")String taskId, @Param("isAscending")boolean isAscending);
	public List<TaskScript> findTaskscriptALL();
	
	public List<TaskScript> findTaskscriptselect(@Param("taskId")String taskId);
	public List<TaskScriptForm> findTaskscriptselect2(@Param("taskId")String taskId);
	public String findTaskscriptcount(@Param("taskId")String taskId);
	
	public List<TaskScript>  findid(@Param("related")String related);
//	public List<TaskScript> findtask(@Param("usermail")String usermail);
	public List<TaskScript> findtask(@Param("usermail")String usermail);
	
	public  List<TaskScript> findtaskimageitems(@Param("usermail")String usermail,@Param("taskid")String taskid);
	public List<TaskScript> findTaskscriptcollaborateselect(@Param("taskId")String id);
}
