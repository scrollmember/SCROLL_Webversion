package jp.ac.tokushima_u.is.ll.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.Order;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.Answer;
import jp.ac.tokushima_u.is.ll.entity.Category;
import jp.ac.tokushima_u.is.ll.entity.FileData;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.ItemQuestionType;
import jp.ac.tokushima_u.is.ll.entity.ItemQueue;
import jp.ac.tokushima_u.is.ll.entity.ItemTag;
import jp.ac.tokushima_u.is.ll.entity.Language;
import jp.ac.tokushima_u.is.ll.entity.LogUserReadItem;
import jp.ac.tokushima_u.is.ll.entity.Question;
import jp.ac.tokushima_u.is.ll.entity.QuestionType;
import jp.ac.tokushima_u.is.ll.entity.Task;
import jp.ac.tokushima_u.is.ll.entity.TaskItem;
import jp.ac.tokushima_u.is.ll.entity.TaskScript;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.exception.NotFoundException;
import jp.ac.tokushima_u.is.ll.form.TaskEditForm;
import jp.ac.tokushima_u.is.ll.form.TaskScriptForm;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.util.FilenameUtil;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class TaskService {
	
	@Autowired
	private UserService userService;
	@Autowired
	private StaticServerService staticServerService;
	
	private HibernateDao<Language, String> languageDao;
	private HibernateDao<Item, String> itemDao;
	private HibernateDao<TaskItem, String> taskItemDao;
	private HibernateDao<Task, String> taskDao;
	private HibernateDao<TaskScript, String> taskScriptDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		languageDao = new HibernateDao<Language, String>(sessionFactory,
				Language.class);
		taskDao = new HibernateDao<Task, String>(sessionFactory,
				Task.class);
		itemDao = new HibernateDao<Item, String>(sessionFactory,
				Item.class);
		taskItemDao  = new HibernateDao<TaskItem, String>(sessionFactory,
				TaskItem.class);
		taskScriptDao = new HibernateDao<TaskScript, String>(sessionFactory,
				TaskScript.class);
		
	}


	public List<Task> findAllTasks() {
		List<Task> tasks = new ArrayList<Task>();
		return tasks;
	}

	public Task createTask(TaskEditForm form) throws NotFoundException{
		Language language = this.languageDao.findUniqueBy("id", form.getLanguageId());
		if(language == null)
			throw new NotFoundException();
		Task task = new Task();
		Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
		task.setAuthor(user);
		task.setCreatetime(new Date());
		task.setLanguage(language);
		task.setLevel(form.getLevel());
		task.setIsPublished(form.getIsPublished());
		task.setUpdatetime(new Date());
		task.setTitle(form.getTitle());
		this.taskDao.save(task);
		return task;
	}

	
	public void createTaskScript(TaskScriptForm form)throws NotFoundException, IOException{
		Task task = this.findTaskById(form.getTaskId());
		TaskScript taskScript = new TaskScript();
		taskScript.setTask(task);
		List<TaskScript> scripts = this.findTaskScriptList(task, false);
		int num = 1;
		if(scripts!=null&&scripts.size()>0)
			num = scripts.get(0).getNum()+1;
		taskScript.setNum(num);
		if(form.getLat()!=null && form.getLng()!=null){
			taskScript.setLat(form.getLat());
			taskScript.setLng(form.getLng());
		}
		taskScript.setZoom(form.getZoom());
		taskScript.setScript(form.getScript());
		taskScript.setLocationBased(form.getLocationBased());
		taskScript.setTimeBased(form.getTimeBased());
		taskScript.setEndtime(form.getEndtime());
		taskScript.setStarttime(form.getStarttime());
		
		// Attached File
		if (form.getImage() != null) {
			MultipartFile uploadFile = form.getImage();
			if (!uploadFile.isEmpty() && uploadFile.getSize() != 0) {
				FileData image = new FileData();
				image.setOrigName(uploadFile.getOriginalFilename());
				image.setCreatedAt(new Date());
				image.setMd5(DigestUtils.md5Hex(uploadFile.getInputStream()));

				String fileType = "";
		        if(!StringUtils.isBlank(uploadFile.getOriginalFilename())){
		        	fileType = FilenameUtil.checkMediaType(uploadFile.getOriginalFilename());
		        	image.setFileType(fileType);
		        }

				taskScript.setImage(image);
			}
		}
		
		this.taskScriptDao.save(taskScript);
		
		if (form.getImage() != null && !form.getImage().isEmpty()) {
			staticServerService.uploadFile(taskScript.getImage().getId(), StringUtils.lowerCase(FilenameUtils.getExtension(form.getImage().getOriginalFilename())), form.getImage().getBytes());
			if("image".equals(taskScript.getImage().getFileType())){
				List<FileData> fileDataList = new ArrayList<FileData>();
				fileDataList.add(taskScript.getImage());
			}
		}

	}
	
	public List<TaskItem> findTaskItems(String taskId)throws NotFoundException{
		Task task = this.findTaskById(taskId);
		return this.taskItemDao.findBy("task", task);
	}
	
	
	public void addTaskItems(String taskId, String[]itemIds)throws NotFoundException{
		Task task = this.findTaskById(taskId);
		if(itemIds!=null && itemIds.length>0){
			String hql = "from TaskItem t where t.task=? and t.item=?";
			for(String itemId:itemIds){
				Item item = this.itemDao.findUniqueBy("id", itemId);
				if(item!=null){
					List<TaskItem> taskItems = this.taskItemDao.find(hql, new Object[]{task,item});
					if(taskItems == null || taskItems.size()<=0){
						TaskItem taskItem = new TaskItem();
						taskItem.setItem(item);
						taskItem.setTask(task);
						this.taskItemDao.save(taskItem);
					}
				}
			}
		}
	}
	
	
	public void removeTaskItems(String taskId, String[]itemIds)throws NotFoundException{
		Task task = this.findTaskById(taskId);
		if(itemIds!=null && itemIds.length>0){
			String hql = "from TaskItem t where t.task=? and t.item=?";
			for(String itemId:itemIds){
				Item item = this.itemDao.findUniqueBy("id", itemId);
				if(item!=null){
					List<TaskItem> taskItems = this.taskItemDao.find(hql, new Object[]{task,item});
					for(TaskItem ti:taskItems){
						this.taskItemDao.delete(ti);
					}
				}
			}
			
		}
	}	
	
	public Task findTaskById(String taskId)throws NotFoundException{
		Task task = this.taskDao.findUniqueBy("id", taskId);
		if(task == null)
			throw new NotFoundException();
		else
			return task;
	}
	
	public List<TaskScript> findTaskScriptList(Task task, boolean isAscending){
		String hql = "from TaskScript ts where ts.task = ? ";
		if(isAscending)
			hql += " order by ts.num asc";
		else
			hql += "order by ts.num desc"; 
		return this.taskScriptDao.find(hql, task);
	}
	
}
