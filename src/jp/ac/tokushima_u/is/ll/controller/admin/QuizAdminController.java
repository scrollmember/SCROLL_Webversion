package jp.ac.tokushima_u.is.ll.controller.admin;


import java.util.List;

import jp.ac.tokushima_u.is.ll.service.MyQuizService;
import jp.ac.tokushima_u.is.ll.service.helper.UserQuizInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/quizadmin")
public class QuizAdminController {
	@Autowired
	private MyQuizService myquizService;
	
	@RequestMapping
	public String index(ModelMap model) {
		List<UserQuizInfo> quizinfos = this.myquizService.searchAllUsersQuizInfo();
		model.addAttribute("quizinfos", quizinfos);
		return "admin/quiz/index";
	}
}
