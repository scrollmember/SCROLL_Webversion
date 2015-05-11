package jp.ac.tokushima_u.is.ll.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.entity.Category;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.UserMessage;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.form.ItemSearchCondForm;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.ItemService;
import jp.ac.tokushima_u.is.ll.service.MyQuizService;
import jp.ac.tokushima_u.is.ll.service.UserMessageService;
import jp.ac.tokushima_u.is.ll.service.UserService;
import jp.ac.tokushima_u.is.ll.util.TagCloudConverter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

	@Autowired
	private UserService userService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private MyQuizService myQuizService;

	@RequestMapping
	public String index(ModelMap model) {
		return list(model);
	}

	public String list(ModelMap model) {

		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());

		model.addAttribute("user", user);

	
		model.addAttribute("items", myQuizService.findMyQuizWrongCount(user));
		model.addAttribute("correct_items", myQuizService.findMyQuizCorrectCount(user));
		model.addAttribute("uploadItemRanking",this.itemService.uploadRanking(user.getId()));
		model.addAttribute("numberLogsViews",this.itemService.findAllReadCount(user.getId()));
		model.addAttribute("numberCompletedQuizzes",this.myQuizService.findMyQuizCompletedQuizzesCount(user));

		// ■wakebe 次のレベルまでの経験値取得
		model.addAttribute("nextExperiencePoint",
				this.userService.getNextExperiencePoint(user.getId()));

		// ■wakebe 現在の合計経験値取得
		model.addAttribute("nowExperiencePoint",
				this.userService.getNowExperiencePoint(user.getId()));

		return "dashboard/index";
	}

	@RequestMapping(value = "/{id}/{page}", method = RequestMethod.GET)
	public String show(@PathVariable String id,@PathVariable String page, ModelMap model) {

		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		ItemSearchCondForm form=new ItemSearchCondForm();
		//form.setUsername("Saikhanaa");
		form.setPage(Integer.parseInt(page));		
		List<String> itemIds=myQuizService.getItemIdsByIncorrectCount(Integer.parseInt(id), user);
		if(itemIds.size()==0){
			itemIds.add("-10");
		}
		form.setItemIds(itemIds);
		
		String color="none";
		if(Integer.parseInt(id)==1){
			color="green";
		}else if (Integer.parseInt(id)==2) {
			color="yellow";
		}else if (Integer.parseInt(id)==3) {
			color="red";
		}
		model.addAttribute("itemPage", itemService.searchItemPageByCond(form));
		model.addAttribute("itemType", id);
		model.addAttribute("color", color);

		addTagCloud(model);
		model.addAttribute("itemList", new ArrayList<Item>());
		return "dashboard/logs";
	}

	private void addTagCloud(ModelMap model) {
		Map<String, Integer> tagCloud = TagCloudConverter.convert(itemService
				.findTagCloud());
		model.addAttribute("tagCloud", tagCloud);
	}
}
