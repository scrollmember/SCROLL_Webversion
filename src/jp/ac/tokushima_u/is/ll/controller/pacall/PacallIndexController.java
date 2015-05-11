package jp.ac.tokushima_u.is.ll.controller.pacall;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pacall")
public class PacallIndexController {
	
	@RequestMapping(value={"", "/", "/index"})
	public String index(){
		return "redirect:/pacall/folder";
	}
}
