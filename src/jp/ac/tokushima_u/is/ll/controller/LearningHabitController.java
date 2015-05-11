package jp.ac.tokushima_u.is.ll.controller;

import java.util.Date;
import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.GeoPoint;
import jp.ac.tokushima_u.is.ll.entity.QuestionaryHabit;
import jp.ac.tokushima_u.is.ll.entity.StudyArea;
import jp.ac.tokushima_u.is.ll.entity.StudyTime;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.form.LearningHabitForm;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.ItemService;
import jp.ac.tokushima_u.is.ll.service.LearningHabitService;
import jp.ac.tokushima_u.is.ll.service.QuestionaryService;
import jp.ac.tokushima_u.is.ll.service.UserService;
import jp.ac.tokushima_u.is.ll.util.GeoUtils;
import jp.ac.tokushima_u.is.ll.util.Utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
*
* @author lemonrain
*/
@Controller
@RequestMapping("/myhabit")
public class LearningHabitController {
	@Autowired
	private LearningHabitService learningHabitService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private UserService userService;
	@Autowired
	private QuestionaryService questionaryService;
	
    @RequestMapping
	public String index(@ModelAttribute("form") LearningHabitForm form, String userid, ModelMap model){
//		List<StudyArea> areas = this.learningHabitService.searchStudyArea(SecurityUserHolder.getCurrentUser()
//				.getId());
//		List<StudyTime> times = this.learningHabitService.searchStudyTime(SecurityUserHolder.getCurrentUser()
//				.getId());
    	if(userid == null)
    		userid = SecurityUserHolder.getCurrentUser().getId();
    	
    	Users user = userService.getById(userid);
    	List<StudyArea> areas = this.learningHabitService.searchStudyArea(userid);
		List<StudyTime> times = this.learningHabitService.searchStudyTime(userid);
		GeoPoint centerPoint = GeoUtils.findCenterGeo(areas);
		model.addAttribute("centerpoint", centerPoint);
		if(areas.size()>0)
			model.addAttribute("areas", areas);
		if(times.size()>0)
			model.addAttribute("times", times);
		
		model.addAttribute("user", user);
		
		List<Object[]> heros = itemService.uploadRanking();

		model.addAttribute("uploadRanking", heros);
    	return "learninghaibt/show";
	}
    
    @RequestMapping(method = RequestMethod.POST)
    public String evaluate(@ModelAttribute("form") LearningHabitForm form, ModelMap model){
    	Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
    	
    	QuestionaryHabit qh = this.questionaryService.findEvaluation(user, Utility.getToday());
    	if(qh == null){
    		QuestionaryHabit habit = new QuestionaryHabit();
    		habit.setUser(user);
    		habit.setCreateTime(new Date());
    		habit.setGeoscore(form.getGeoscore());
    		habit.setTimescore(form.getTimescore());
    		habit.setGeorecommend(form.getGeorecommend());
    		habit.setTimerecommend(form.getTimerecommend());
    		this.questionaryService.evaluateLearningHabit(habit);
    	}
    	return "redirect:/myhabit";
    }
    
}
