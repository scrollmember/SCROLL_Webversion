package jp.ac.tokushima_u.is.ll.controller.pacall;

import java.io.IOException;

import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.entity.pacall.Folder;
import jp.ac.tokushima_u.is.ll.entity.pacall.SensePic;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.ItemService;
import jp.ac.tokushima_u.is.ll.service.PropertyService;
import jp.ac.tokushima_u.is.ll.service.pacall.FolderService;
import jp.ac.tokushima_u.is.ll.service.pacall.SensepicService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/pacall/uploaditem")
public class UploadItemController {
	
	@Autowired
	private SensepicService sensepicService;
	@Autowired
	private FolderService folderService;
	@Autowired
	private PropertyService propertyService;
	@Autowired
	private ItemService itemService;
	
	@RequestMapping(method=RequestMethod.POST)
	public String upload(@RequestParam("id") String sensepicid, ModelMap model){

		SensePic pic = sensepicService.findById(sensepicid);
		if(pic == null){
			return error();
		}
		
		Users user = SecurityUserHolder.getCurrentUser();
		Folder folder = folderService.findById(pic.getFolderId());
		if(folder == null || !user.getId().equals(folder.getUserId())){
			return error();
		}
		
        try {
        	Item item = itemService.uploadImageFirst(pic);
			return "redirect:"+propertyService.getSystemUrl()+"/item/"+item.getId()+"/edit";
		} catch (IOException e) {
			e.printStackTrace();
			return "pacall/folder?id="+folder.getId();
		}
	}
	
	private String error() {
		return "redirect:/pacall/folder";
	}
}
