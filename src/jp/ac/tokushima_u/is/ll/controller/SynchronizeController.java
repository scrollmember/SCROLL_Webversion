package jp.ac.tokushima_u.is.ll.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.MyQuiz;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.form.ItemSyncCondForm;
import jp.ac.tokushima_u.is.ll.form.PersonalSyncCondForm;
import jp.ac.tokushima_u.is.ll.form.QuizForm;
import jp.ac.tokushima_u.is.ll.form.QuizSearchCondForm;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.ItemService;
import jp.ac.tokushima_u.is.ll.service.LLQuizService;
import jp.ac.tokushima_u.is.ll.service.LanguageService;
import jp.ac.tokushima_u.is.ll.service.PersonalizeSerivce;
import jp.ac.tokushima_u.is.ll.service.UserService;
import jp.ac.tokushima_u.is.ll.service.helper.QuizCondition;
import jp.ac.tokushima_u.is.ll.util.Constants;
import jp.ac.tokushima_u.is.ll.ws.service.model.ItemForm;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
@RequestMapping("/sync")
public class SynchronizeController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private LanguageService languageService;
    @Autowired
    private UserService userService;
	@Autowired
	private LLQuizService quizService;
	
	@Autowired
	private PersonalizeSerivce personalizeService;


//    @InitBinder
//    public void initBinder(WebDataBinder binder) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        dateFormat.setLenient(false);
//        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
//    }

	//@ResponseBody  //responsebody返回json乱码， 编码为ISO-8859-1
  	@RequestMapping(value = "/item", method = RequestMethod.POST)
    public String items(@ModelAttribute("searchCond") ItemSyncCondForm form, ModelMap model, HttpServletRequest request) {
		model.clear();
    	List<Item> itemList = itemService.searchSyncItems(form);
    	if(itemList!=null&&itemList.size()>0)
    	{
    		List<ItemForm>forms = new ArrayList<ItemForm>();
    		for(Item item:itemList){
    			ItemForm itemform = new ItemForm(item);
    			forms.add(itemform);
    		}
    		model.addAttribute("items", forms);
    	}
//    	Gson gson = new Gson();
//    	gson.toJson(model);
//        return gson.toJson(model);
    	return "";
    }
  	
 	@RequestMapping(value = "/language", method = RequestMethod.POST)
    public String languages(ModelMap model, HttpServletRequest request) {
		model.clear();
		model.addAttribute("languages", this.languageService.findALl());
        return "";
    }
 	
 	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/personalize", method = RequestMethod.POST)
    public String settings(@ModelAttribute("form")PersonalSyncCondForm form, ModelMap model, HttpServletRequest request) {
 		Users user =  userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
 		Map<String,List> map = personalizeService.syncPersonlize(form, user);
 		if(map.get("sends")!=null)
 			model.addAttribute("sends", map.get("sends"));
 		
 		if(map.get("areas")!=null)
 			model.addAttribute("areas", map.get("areas"));
 		
 		if(map.get("times")!=null)
 			model.addAttribute("times", map.get("times"));
 		
        return "";
    }
 	

	@RequestMapping(value = "/quiz", method = RequestMethod.POST)
 	public String myQuizzes(@ModelAttribute("searchCond") QuizSearchCondForm form, ModelMap model, HttpServletRequest request) {
 		model.clear();
 		Users user =  userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
 		
 		String quizid = form.getQuizid();
 		if(StringUtils.isNotBlank(quizid)){
 			QuizCondition quizCon = new QuizCondition();
 			quizCon.setQuizid(quizid);
 			MyQuiz myquiz = this.quizService.findQuiz(quizCon);
 			if(myquiz!=null){
 				Integer weight = Constants.PriorityReferItemWeight;
 				if(myquiz.getItem().getAuthor().equals(user))
 					weight = Constants.PriorityMyItemWeight;
 				model.addAttribute("quizzes", new ArrayList<QuizForm>().add(new QuizForm(myquiz, weight)));
 			}
 			return "";
 		}
 		
 		Date createDate = form.getCreateDate();
 		QuizCondition quizCon  = new QuizCondition();
 		
 		Integer quizsize = form.getQuizsize();
 		if(quizsize!=-1&&quizsize<10){
 			quizCon.setIsRecommended(Boolean.TRUE);
 		}
 		
 		quizCon.setUser(user);
 		if(createDate!=null)
 			quizCon.setCreateDateFrom(createDate);
 		//get disabled & abled quizzes; 
 		List<QuizForm> quizzes = this.quizService.findAllSyncQuizzes(quizCon);
 		model.addAttribute("quizzes", quizzes);
 		return "";
 	}
 	
 	
}
