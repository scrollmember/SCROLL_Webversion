package jp.ac.tokushima_u.is.ll.controller.api;

import jp.ac.tokushima_u.is.ll.service.PersonalizeSerivce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/personalizetask")
public class PersonalizeTaskController {
	private static final String KEY = "f51167023e1b747c122c155ea903613b5d5ca3a1";
	
	@Autowired
	private PersonalizeSerivce personalizeService;
	
	@RequestMapping
	@ResponseBody
	public String send(String key) {
		if (!KEY.equals(key)){
			return "key_error";
		}
		personalizeService.personlize();
		return "task_success";
	}
}
