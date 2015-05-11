package jp.ac.tokushima_u.is.ll.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import jp.ac.tokushima_u.is.ll.common.orm.Page;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.Language;
import jp.ac.tokushima_u.is.ll.entity.Task;
import jp.ac.tokushima_u.is.ll.entity.TaskItem;
import jp.ac.tokushima_u.is.ll.entity.TaskScript;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.exception.NotFoundException;
import jp.ac.tokushima_u.is.ll.form.ItemEditForm;
import jp.ac.tokushima_u.is.ll.form.ItemSearchCondForm;
import jp.ac.tokushima_u.is.ll.form.TaskEditForm;
import jp.ac.tokushima_u.is.ll.form.TaskScriptForm;
import jp.ac.tokushima_u.is.ll.form.validator.TaskEditFormValidator;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.ItemService;
import jp.ac.tokushima_u.is.ll.service.TaskService;
import jp.ac.tokushima_u.is.ll.service.UserService;
import jp.ac.tokushima_u.is.ll.ws.service.model.ItemForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;


@Controller
@RequestMapping("/task")
public class TaskController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private ItemService itemService;
	
	
    @ModelAttribute("oneDay")
    public  List<String> initOneDay(){
        return this.getTimeList(6, 24);
    }
    
    @ModelAttribute("minutes")
   public List<String> getMinutes(){
        List<String> result = new LinkedList<String>();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        for (int i = 1; i <= 12; i++) {
            String str = "";
            if (cal.get(Calendar.MINUTE) < 10) {
                str = str + "0" + cal.get(Calendar.MINUTE);
            } else {
                str = str + cal.get(Calendar.MINUTE);
            }
            result.add(str);
            cal.add(Calendar.MINUTE, 5);
        }
        return result;
    }
    
    private List<String> getTimeList(int start, int end){
        List<String> result = new LinkedList<String>();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, start);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND,0);
        for(int i=start;i<=end;i++){
            String str = "";
            if(cal.get(Calendar.HOUR_OF_DAY)<10)
                str="0"+cal.get(Calendar.HOUR_OF_DAY);
            else
                str=cal.get(Calendar.HOUR_OF_DAY)+"";
            result.add(str);
            cal.add(Calendar.HOUR_OF_DAY, 1);
        }

        return result;
    }

    @RequestMapping
    public String index(@ModelAttribute("searchCond") ItemSearchCondForm form,  ModelMap model) {
        return "item/list";
    }
    
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(@ModelAttribute("task") TaskEditForm form,  ModelMap model) {
    	Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
    	model.addAttribute("user", user);

    	List<Language> langs = new ArrayList<Language>();
//    	for(Language lang: user.getMyLangs()){
//    		if(!langs.contains(lang))langs.add(lang);
//    	}
    	for(Language lang: user.getStudyLangs()){
    		if(!langs.contains(lang))langs.add(lang);
    	}
    	model.addAttribute("langs", langs);
    	
        return "task/add";
    }

    
	@RequestMapping(value = "/searchItem", method = RequestMethod.GET)
	public String test(String queryvalue, String taskId, Integer num, ModelMap model, HttpServletResponse response) {
		model.clear();
    	List<Item> items = itemService.searchRelatedItemForTask(taskId, queryvalue, num);
    	if(items!=null&&items.size()>0)
    	{
    		List<ItemForm>forms = new ArrayList<ItemForm>();
    		for(Item item:items){
    			ItemForm itemform = new ItemForm(item);
    			forms.add(itemform);
    		}
    		model.addAttribute("items", forms);
    	}
    	return "index";
    }
	
    @RequestMapping(method = RequestMethod.POST)
    public String create(@ModelAttribute("task") TaskEditForm form, BindingResult result, ModelMap model) {
        new TaskEditFormValidator().validate(form, result);
        Task task = null;
        try{
        	task = this.taskService.createTask(form);
        }catch(NotFoundException nfe){
        	result.reject("languageId",nfe.getMessage()+" is not found.");
        }
        
        if (result.hasErrors()) {
        	return this.add(form, model);
        }
        if(task != null&&task.getId()!=null){
        	return "redirect:/task/"+task.getId()+"/addscript";	
        }else
        	return this.add(form, model);
    }

    @RequestMapping(value="/{id}/addscript", method=RequestMethod.GET)
    public String getScriptForm(@PathVariable String id, @ModelAttribute("script") TaskScriptForm form, BindingResult result, ModelMap model){
    	Task task = null;
    	try{
    		task = this.taskService.findTaskById(id);
    	}catch(NotFoundException e){
    		result.reject("title", e.getMessage()+" is not found");
    	}
    	
    	Integer stepNum = 1;
    	
    	if(task != null){
    		List<TaskScript> scripts = this.taskService.findTaskScriptList(task, false);
    		if(scripts!=null && scripts.size()>0)
    			stepNum = scripts.size()+1;
    		form.setTaskId(id);
    		model.addAttribute("task", task);
    	}
    	
    	form.setNum(stepNum);
    	return "task/addstep";
    }
    
    @RequestMapping(value="/{id}/preview", method=RequestMethod.GET)
    public String preview(@PathVariable String id,  ModelMap model){
    	Task task = null;
    	try{
    		task = this.taskService.findTaskById(id);
    	}catch(NotFoundException e){
    	}
    	
    	if(task != null){
    		List<TaskScript> scripts = this.taskService.findTaskScriptList(task, false);
    		model.addAttribute("task", task);
    		model.addAttribute("scripts", scripts);
    	}
    	return "task/preview";
    }
    
    @RequestMapping(value="/{id}/addscript", method=RequestMethod.POST)
    public String addScript(@PathVariable String id, @ModelAttribute("script") TaskScriptForm form, BindingResult result, ModelMap model){
    	try{
    		this.taskService.createTaskScript(form);
    	}catch(IOException ioe){
    		result.reject("image", "File error");
    	}catch(NotFoundException nfe){
    		result.reject("title", nfe.getMessage()+" is not found");
    	}
    	
    	if(!result.hasErrors()){
    		form.clear();
    	}
    	return this.getScriptForm(id, form, result, model);
    }
    

    @RequestMapping(value="/{id}/items", method=RequestMethod.GET)
    public String getRelatedItem(@PathVariable String id, ModelMap model){
    	try{
    		List<TaskItem> taskItems = this.taskService.findTaskItems(id);
    		List<ItemForm> items = new ArrayList<ItemForm>();
    		for(TaskItem ti:taskItems){
    			ItemForm itemform = new ItemForm(ti.getItem());
    			items.add(itemform);
    		}
    		model.addAttribute("items", items);
    	}catch(NotFoundException e){
    		model.put("result", "failed");
    	}
    	
    	model.put("result", "success");
    	return null;
    }
    
    
    @RequestMapping(value="/{id}/additem", method=RequestMethod.POST)
    public String addRelatedItem(@PathVariable String id, String[] itemIds,  ModelMap model){
    	try{
    		this.taskService.addTaskItems(id, itemIds);
    	}catch(NotFoundException e){
    		model.put("result", "failed");
    	}
    	
    	model.put("result", "success");
    	return "";
    }
    
    @RequestMapping(value="/{id}/removeitem", method=RequestMethod.POST)
    public String removeRelatedItem(@PathVariable String id, String[] itemIds){
    	try{
    		this.taskService.removeTaskItems(id, itemIds);
    	}catch(NotFoundException e){
    		
    	}
    	return null;
    }
    
    
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable String id, @ModelAttribute("form") ItemEditForm form, ModelMap model) {
//        Item item = itemService.findById(id);
//        if (item == null || (!SecurityUserHolder.getCurrentUser().getId().equals(item.getAuthor().getId()) && !SecurityUserHolder.getCurrentUser().getAuth().equals(Users.UsersAuth.ADMIN))) {
//            return "redirect:/item";
//        }
//        form = new ItemEditForm(item);
//
//    	List<ItemTitle> titles = item.getTitles();
//    	HashMap<String, String> titleMap = new HashMap<String, String>();
//    	for(ItemTitle title: titles){
//    		titleMap.put(title.getLanguage().getCode(), title.getContent());
//    	}
//    	form.setTitleMap(titleMap);
//
//        model.addAttribute("item", item);
//        model.addAttribute("form", form);
//
//        //FileType{video, image}
//        FileData fileData = item.getImage();
//        String fileType = "";
//        if(fileData!=null && !StringUtils.isBlank(fileData.getOrigName())){
//        	fileType = FilenameUtil.checkMediaType(fileData.getOrigName());
//        }
//        model.addAttribute("fileType", fileType);
//
//    	Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
//    	model.addAttribute("user", user);
//
//       	List<Language> langs = new ArrayList<Language>();
//    	for(ItemTitle t:item.getTitles()){
//    		if(!langs.contains(t))langs.add(t.getLanguage());
//    	}
//    	for(Language lang: user.getMyLangs()){
//    		if(!langs.contains(lang))langs.add(lang);
//    	}
//    	for(Language lang: user.getStudyLangs()){
//    		if(!langs.contains(lang))langs.add(lang);
//    	}
//    	Language english = languageService.findUniqueLangByCode("en");
//    	if(!langs.contains(english))langs.add(english);
//    	model.addAttribute("langs", langs);
//
//
//    	if(item.getCategory()!=null) form.setCategoryId(item.getCategory().getId());
        return "item/edit";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    public String update(@PathVariable String id, @ModelAttribute("form") ItemEditForm form, BindingResult result, ModelMap model) {
//        Item item = itemService.findById(id);
//        if (item == null || (!SecurityUserHolder.getCurrentUser().getId().equals(item.getAuthor().getId()) && !SecurityUserHolder.getCurrentUser().getAuth().equals(Users.UsersAuth.ADMIN))) {
//            return "redirect:/item";
//        }
//        if (item.getImage() != null) {
//            form.setFileExist(true);
//        }
//        new ItemEditFormValidator().validate(form, result);
//        if (result.hasErrors()) {
//            return "item/" + id + "/edit";
//        }
//        try {
//            this.itemService.updateByForm(id, form);
//
//            Users user = userService.getById(SecurityUserHolder
//    				.getCurrentUser().getId());
//
//        	HashMap<String,String[]>params = new HashMap<String,String[]>();
//    		C2DMessage c2dmessage = new C2DMessage();
//    		c2dmessage.setCollapse(Constants.COLLAPSE_KEY_SYNC);
//    		c2dmessage.setIsDelayIdle(new Integer(0));
//    		try{
//    			c2dmessage.setParams(SerializeUtil.serialize(params));
//    		}catch(Exception e){
//
//    		}
//    		this.c2dmMessageService.addMessage(c2dmessage,user);
//
//        } catch (IOException ex) {
//            logger.error("Error occurred when create item", ex);
//        }
        return "redirect:/item/" + id;
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public String delete(@PathVariable String id) {
//        Item item = itemService.findById(id);
//        if (item == null || (!SecurityUserHolder.getCurrentUser().getId().equals(item.getAuthor().getId()) && !SecurityUserHolder.getCurrentUser().getAuth().equals(Users.UsersAuth.ADMIN))) {
//            return "redirect:/item";
//        }
//        this.itemService.delete(item);
        return "redirect:/item";
    }
    
}
