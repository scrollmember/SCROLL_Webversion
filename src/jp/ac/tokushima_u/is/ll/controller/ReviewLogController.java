package jp.ac.tokushima_u.is.ll.controller;

import java.util.Set;

import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.ItemQueueService;
import jp.ac.tokushima_u.is.ll.util.Constants;
import jp.ac.tokushima_u.is.ll.visualization.ReviewHistoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/logstate")
public class ReviewLogController {

	@Autowired
	private ReviewHistoryService reviewHistoryService;
	@Autowired
	private ItemQueueService itemQueueService;
	
	@RequestMapping
	public String index(ModelMap model){
		Users user = SecurityUserHolder.getCurrentUser();
		model.addAttribute("exper_correct", reviewHistoryService.findUserItemState(user, Constants.ExperiencedState, Constants.CorrectAnsweredState));
		model.addAttribute("exper_wrong", reviewHistoryService.findUserItemState(user, Constants.ExperiencedState, Constants.WrongAnsweredState));
	    return "state/index";
	}
	
	@RequestMapping(value = "/search")
	public String search(@RequestParam("experienced") Integer experienced, ModelMap model){
		Users user = SecurityUserHolder.getCurrentUser();
		model.addAttribute("exper_correct", reviewHistoryService.findUserItemState(user, experienced, Constants.CorrectAnsweredState));
		model.addAttribute("exper_wrong", reviewHistoryService.findUserItemState(user, experienced, Constants.WrongAnsweredState));
	    return "state/index";
	}
	
	@RequestMapping(value = "/recommend")
	public String recommend(ModelMap model){
		Users user = SecurityUserHolder.getCurrentUser();
		Set<Item> recommendItems = itemQueueService.searchRecommendedItems(user);
		model.addAttribute("recommend_items", recommendItems);
		model.addAttribute("isRecommended", 1);
	    return "state/index";
	}
	
}
