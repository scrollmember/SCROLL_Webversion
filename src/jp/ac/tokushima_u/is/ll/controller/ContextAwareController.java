package jp.ac.tokushima_u.is.ll.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.form.ContextAwareForm;
import jp.ac.tokushima_u.is.ll.form.NotifyForm;
import jp.ac.tokushima_u.is.ll.form.QuizForm;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.ContextAwareService;
import jp.ac.tokushima_u.is.ll.service.ItemService;
import jp.ac.tokushima_u.is.ll.service.LLQuizService;
import jp.ac.tokushima_u.is.ll.service.UserService;
import jp.ac.tokushima_u.is.ll.ws.service.model.ItemForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/contextaware")
public class ContextAwareController {
	@Autowired
	private ContextAwareService contextAwareService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private UserService userService;

	@Autowired
	private LLQuizService quizService;

	@RequestMapping(method = RequestMethod.POST)
	public String answer(
			@ModelAttribute("contextform") ContextAwareForm contextform,
			ModelMap model) {
		model.clear();
		System.out.println("  lat=" + contextform.getLatitude() + "  lng="
				+ contextform.getLongitude());
		if (contextform.getLatitude() != null
				&& contextform.getLongitude() != null) {
			Users user = userService.getById(SecurityUserHolder.getCurrentUser()
					.getId());
			Map<String, Integer> map = this.contextAwareService.findAlaramedTimesByLocation(user, contextform.getLatitude(), contextform.getLongitude(), 0.2);
			Integer quiznum = map.get("quiznum");
			Integer itemnum = map.get("itemnum");
			Integer quiz_all_num = map.get("quiz_all_num");
			Integer item_all_num = map.get("item_all_num");
			
			List<QuizForm> quizforms = null;
			List<ItemForm>items = null;
			if(quiznum==null||quiznum<4||quiz_all_num==null||quiz_all_num<6){
				quizforms = this.quizService.findNearestQuiz(contextform.getLatitude(),
						contextform.getLongitude(), 0.06, 20, userService
						.getById(SecurityUserHolder.getCurrentUser()
								.getId()));
			}
//			quizforms = null;
			if(quizforms!=null)
				model.addAttribute("quizzes", quizforms);
			else if(itemnum==null||itemnum<4||item_all_num==null||item_all_num<6){
				List<Item> itemList = this.itemService
				.searchNearestItemsWithoutNotified(
						contextform.getLatitude(),
						contextform.getLongitude(), 0.06, 20);
				items = new ArrayList<ItemForm>();
				for(Item item:itemList){
	    			ItemForm itemform = new ItemForm(item);
	    			items.add(itemform);
	    		}
			}
			if(items!=null&&items.size()>0)
				model.addAttribute("items", items);
			else if((items==null||items.size()==0)&&(quizforms==null||quizforms.size()==0))
				model.addAttribute("result", 0);
		}
		return "";
	}

	@RequestMapping(value = "/feedback", method = RequestMethod.POST)
	public String feedback(@ModelAttribute("notifyform") NotifyForm notifyform) {
		this.contextAwareService.feedBackContextAware(notifyform);
		return "";
	}

}
