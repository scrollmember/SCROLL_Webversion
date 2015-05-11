/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.ac.tokushima_u.is.ll.controller;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jp.ac.tokushima_u.is.ll.entity.Author;
import jp.ac.tokushima_u.is.ll.entity.Category;
import jp.ac.tokushima_u.is.ll.entity.Kasetting;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.entity.Usertest;
import jp.ac.tokushima_u.is.ll.form.AnalysisForm;
import jp.ac.tokushima_u.is.ll.form.SettingForm;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.CategoryService;
import jp.ac.tokushima_u.is.ll.service.SettingService;
import jp.ac.tokushima_u.is.ll.service.UserService;
import jp.ac.tokushima_u.is.ll.service.helper.HandSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author li
 */


@Controller
@RequestMapping("/mysetting")
public class SettingController{
    @Autowired
    private SettingService settingService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/setting", method = RequestMethod.GET)
	public String Set(@ModelAttribute("setting") SettingForm form,
			ModelMap model, HttpServletRequest request) {

		
    	return "setting/set";

	}
    @RequestMapping(value = "/setting", method = RequestMethod.POST)
	public String Set2(@ModelAttribute("setting") SettingForm form,
			ModelMap model, HttpServletRequest request) {
    	Usertest userid = userService.findById(SecurityUserHolder
				.getCurrentUser().getId());
    	userService.setting(form,userid);
//    	userService.addmember(userid.getId(),userid.getId(),form.getGroupname());
    	 Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
         model.addAttribute("user", user);


 		// ■wakebe 次のレベルまでの経験値取得
 		model.addAttribute("nextExperiencePoint", this.userService.getNextExperiencePoint(user.getId()));

 		// ■wakebe 現在の合計経験値取得
 		model.addAttribute("nowExperiencePoint", this.userService.getNowExperiencePoint(user.getId()));
 		
 		Kasetting ka = userService.getka(SecurityUserHolder.getCurrentUser().getId());
 		model.addAttribute("kadata",ka);
 		
    	return "profile/index";

	}
    
    @RequestMapping(value = "/group", method = RequestMethod.GET)
   	public String group(@ModelAttribute("setting") SettingForm form,
   			ModelMap model, HttpServletRequest request) {

   		
       	return "setting/set";

   	}
       @RequestMapping(value = "/group", method = RequestMethod.POST)
   	public String group2(@ModelAttribute("setting") SettingForm form,
   			ModelMap model, HttpServletRequest request,String authorid,String groupname) {
//    	   String groupname="";
       	Usertest userid = userService.findById(SecurityUserHolder
   				.getCurrentUser().getId());
       	userService.groupsetting(form,userid);
   		
       	 Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
            model.addAttribute("user", user);


    		// ■wakebe 次のレベルまでの経験値取得
    		model.addAttribute("nextExperiencePoint", this.userService.getNextExperiencePoint(user.getId()));

    		// ■wakebe 現在の合計経験値取得
    		model.addAttribute("nowExperiencePoint", this.userService.getNowExperiencePoint(user.getId()));
    		
//    		Kasetting ka = userService.getka(SecurityUserHolder.getCurrentUser().getId());
//    		model.addAttribute("kadata",ka);
//    		userService.addmember(userid.getId(),authorid,groupname);
       	return "profile/index";

   	}
       @RequestMapping(value = "/groupadd", method = RequestMethod.POST)
      	public String group3(@ModelAttribute("setting") SettingForm form,
      			ModelMap model, HttpServletRequest request,String authorid,String groupname,String groupid) {
//       	   String groupname="";
          	Usertest userid = userService.findById(SecurityUserHolder
      				.getCurrentUser().getId());
//          	userService.groupsetting(form,userid);
      		
          	 Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
               model.addAttribute("user", user);


       		// ■wakebe 次のレベルまでの経験値取得
       		model.addAttribute("nextExperiencePoint", this.userService.getNextExperiencePoint(user.getId()));

       		// ■wakebe 現在の合計経験値取得
       		model.addAttribute("nowExperiencePoint", this.userService.getNowExperiencePoint(user.getId()));
       		
//       		Kasetting ka = userService.getka(SecurityUserHolder.getCurrentUser().getId());
//       		model.addAttribute("kadata",ka);
       		userService.addmember(userid.getId(),authorid,groupname,groupid);
          	return "profile/index";

      	}
       
       @RequestMapping(value = "/groupdelete", method = RequestMethod.POST)
     	public String group4(@ModelAttribute("setting") SettingForm form,
     			ModelMap model, HttpServletRequest request,String authorid,String groupname,String groupid) {
//      	   String groupname="";
         	Usertest userid = userService.findById(SecurityUserHolder
     				.getCurrentUser().getId());
//         	userService.groupsetting(form,userid);
     		
         	 Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
              model.addAttribute("user", user);


      		// ■wakebe 次のレベルまでの経験値取得
      		model.addAttribute("nextExperiencePoint", this.userService.getNextExperiencePoint(user.getId()));

      		// ■wakebe 現在の合計経験値取得
      		model.addAttribute("nowExperiencePoint", this.userService.getNowExperiencePoint(user.getId()));
      		
//      		Kasetting ka = userService.getka(SecurityUserHolder.getCurrentUser().getId());
//      		model.addAttribute("kadata",ka);
//      		userService.addmember(userid.getId(),authorid,groupname,groupid);
      		userService.deletemember(groupid,userid.getId());
         	return "profile/index";

     	}
       
       @RequestMapping(value = "/getauthor", method = RequestMethod.POST)
    	public String group5(@ModelAttribute("setting") SettingForm form,
    			ModelMap model, HttpServletRequest request,String authorid,String groupname,String groupid) {
//     	   String groupname="";
        	Usertest userid = userService.findById(SecurityUserHolder
    				.getCurrentUser().getId());
//        	userService.groupsetting(form,userid);
    		
        	 Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
             model.addAttribute("user", user);


     		// ■wakebe 次のレベルまでの経験値取得
     		model.addAttribute("nextExperiencePoint", this.userService.getNextExperiencePoint(user.getId()));

     		// ■wakebe 現在の合計経験値取得
     		model.addAttribute("nowExperiencePoint", this.userService.getNowExperiencePoint(user.getId()));
     		
//     		Kasetting ka = userService.getka(SecurityUserHolder.getCurrentUser().getId());
//     		model.addAttribute("kadata",ka);
//     		userService.addmember(userid.getId(),authorid,groupname,groupid);
     		List<Author> author=userService.getauthor(groupid);
     		model.addAttribute("authorgroup",author);
        	return "profile/index";

    	}
       
       
    @ModelAttribute("oneDay")
    public  List<String> initOneDay(){
        return this.getTimeList(6, 24);
    }
//    public  Map<Time,String> initOneDay(){
//        return this.getTimeMap(6, 24);
//    }

    @ModelAttribute("morning")
    public List<String> initMorning(){
        return this.getTimeList(6, 12);
    }
//    public Map<Time,String> initMorning(){
//        return this.getTimeMap(6, 12);
//    }

     @ModelAttribute("afternoon")
    public List<String> initAfternoon(){
        return this.getTimeList(12, 18);
     }
//    public Map<Time,String> initAfternoon(){
//        return this.getTimeMap(12, 18);
//    }

      @ModelAttribute("evening")
    public List<String> initEvening(){
        return this.getTimeList(18, 24);
    }
//    public Map<Time,String> initEvening(){
//        return this.getTimeMap(18, 24);
//    }

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

     @ModelAttribute("handsets")
     public Map<Integer,HandSet> getHandsets(){
        Map<Integer, HandSet> handsets = new HashMap<Integer, HandSet>();
        for(HandSet handset:HandSet.values()){
            handsets.put(handset.getValue(), handset);
        }
         return handsets;
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

    @SuppressWarnings("unused")
	private Map<Time,String> getTimeMap(int start, int end){
        Map<Time,String>  period = new LinkedHashMap<Time,String>();
        Time mytime = null;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, start);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND,0);
        for(int i=start;i<=end;i++){
            mytime= new Time(cal.getTimeInMillis());
            String str = "";
            if(cal.get(Calendar.HOUR_OF_DAY)<10)
                str="0"+cal.get(Calendar.HOUR_OF_DAY)+":";
            else
                str=cal.get(Calendar.HOUR_OF_DAY)+":";
            if(cal.get(Calendar.MINUTE)<10)
                str=str+"0"+cal.get(Calendar.MINUTE);
            else
                str = str + cal.get(Calendar.MINUTE);
            period.put(mytime,str);
            cal.add(Calendar.HOUR_OF_DAY, 1);
        }

        return period;
    }

   

    @RequestMapping(method=RequestMethod.GET)
    public String index(@ModelAttribute("settingform") SettingForm form, ModelMap model){
        this.settingService.initSettingForm(form);
        Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
        List<Category> categoryList = user.getMyCategoryList();
        if(categoryList!=null && categoryList.size()>0){
	        form.setCategoryIdList(new ArrayList<String>());
	        for(Category c: categoryList){
	        	form.getCategoryIdList().add(c.getId());
	        }
        }
        model.addAttribute("categoryRootList", categoryService.findRoots());
        model.addAttribute("user", user);
        return "setting/add";
    }

    @RequestMapping(method=RequestMethod.POST)
    public String add(@ModelAttribute("settingform") SettingForm form, ModelMap model){
        this.settingService.saveSettingForm(form);
//        this.settingService.updateSettingForm(form);
        form=null;
        return "redirect:/mysetting";
    }



}
