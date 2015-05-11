package jp.ac.tokushima_u.is.ll.controller.admin;

import jp.ac.tokushima_u.is.ll.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin/notification")
public class NotificationController {
	@Autowired
	private NotificationService notificationService;

	@RequestMapping(method = RequestMethod.GET)
	public String list(ModelMap model) {
		model.addAttribute("dataList", notificationService.getAll());
		return "/admin/notification/list";
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(String id, ModelMap model) {
		model.addAttribute("notification", notificationService.get(id));
		return "/admin/notification/show";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		return "/admin/notification/add";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String create(ModelMap model) {
		return "redirect:/admin/notification";
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit() {
		return "/admin/notification/edit";
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
	public String update(String id, ModelMap model) {
		return "redirect:/admin/notification/" + id;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
	public String delete(String id, ModelMap model) {
		return "redirect:/admin/notification";
	}
}
