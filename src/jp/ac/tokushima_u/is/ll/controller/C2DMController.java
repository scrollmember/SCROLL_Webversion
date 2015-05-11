package jp.ac.tokushima_u.is.ll.controller;

import java.util.Date;
import jp.ac.tokushima_u.is.ll.entity.C2DMCode;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.form.C2DMForm;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.C2DMService;
import jp.ac.tokushima_u.is.ll.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/c2dm")
public class C2DMController {
	@Autowired
	private C2DMService c2dmService;
	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.POST)
	public String updateC2DM(@ModelAttribute("form") C2DMForm form,
			ModelMap model) {
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		C2DMCode code = new C2DMCode();
		code.setAuthor(user);
		code.setImsi(form.getImsi());
		code.setRegisterId(form.getRegisterId());
		code.setUpdateTime(new Date());
		this.c2dmService.updateC2DMCode(code);
		return "";
	}

}
