package jp.ac.tokushima_u.is.ll.controller.admin;

import jp.ac.tokushima_u.is.ll.service.ItemFileStatusService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/itemfilestatus")
public class ItemFileStatusController {
	
	@Autowired
	private ItemFileStatusService itemFileStatusService;
	
	@RequestMapping
	public String index(ModelMap model){
		model.addAttribute("list", itemFileStatusService.findItemWhereFileHasErrors());
		return "admin/itemfilestatus/index";
	}
}
