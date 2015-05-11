package jp.ac.tokushima_u.is.ll.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/admin", "/admin/index"})
public class AdminIndexController {
	
	@RequestMapping
	public String index(){
		return "/admin/index";
	}
}
