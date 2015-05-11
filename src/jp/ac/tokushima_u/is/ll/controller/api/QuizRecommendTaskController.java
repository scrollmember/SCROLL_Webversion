package jp.ac.tokushima_u.is.ll.controller.api;

import jp.ac.tokushima_u.is.ll.service.ItemQueueService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/quizrecommendtask")
public class QuizRecommendTaskController {

	private static final String KEY = "74fef6ff9043479efb2315b5103b70c285168a0c";
	
	@Autowired
	private ItemQueueService itemQueueService;

	@RequestMapping
	@ResponseBody
	public String start(String key) {
		if (!KEY.equals(key)) {
			return "key_error";
		}
		itemQueueService.searchRecommendItems();
		return "task_success";
	}
}
