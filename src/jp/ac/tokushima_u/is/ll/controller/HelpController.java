package jp.ac.tokushima_u.is.ll.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/help")
public class HelpController {
	@RequestMapping
	public String index() {
		return "/help/index";
	}
}
