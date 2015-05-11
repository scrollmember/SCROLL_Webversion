package jp.ac.tokushima_u.is.ll.controller.pacall;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.entity.pacall.Folder;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.pacall.FolderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pacall/folder")
public class FolderController {
	
	@Autowired
	private FolderService folderService;

	@RequestMapping
	public String index(ModelMap model){
		Users user = SecurityUserHolder.getCurrentUser();
		List<Folder> folderList = folderService.selectByUserid(user.getId());
		model.addAttribute("folderList", folderList);
		return "/pacall/folder/index";
	}
}
