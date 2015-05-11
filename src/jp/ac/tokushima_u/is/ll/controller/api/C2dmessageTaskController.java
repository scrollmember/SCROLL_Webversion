package jp.ac.tokushima_u.is.ll.controller.api;

import jp.ac.tokushima_u.is.ll.service.C2DMessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/c2dmessagetask")
public class C2dmessageTaskController {
	
	private static final String KEY = "12e642fb1b02d45358e533b83ca633edb8a604f1";

	@Autowired
	private C2DMessageService c2DMessageService;

	@RequestMapping
	@ResponseBody
	public String doSend(String key) {
		if (!KEY.equals(key)){
			return "key_error";
		}
		c2DMessageService.doSend();
		return "task_success";
	}
}
