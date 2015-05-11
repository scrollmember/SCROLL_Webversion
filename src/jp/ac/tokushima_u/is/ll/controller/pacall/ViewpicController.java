package jp.ac.tokushima_u.is.ll.controller.pacall;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.entity.pacall.Folder;
import jp.ac.tokushima_u.is.ll.entity.pacall.SensePic;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.pacall.FolderService;
import jp.ac.tokushima_u.is.ll.service.pacall.SensepicSearchService;
import jp.ac.tokushima_u.is.ll.service.pacall.SensepicService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/pacall/viewpic")
public class ViewpicController {
	
	@Autowired
	private SensepicService sensepicService;
	@Autowired
	private FolderService folderService;
	
	@Autowired
	private SensepicSearchService sensePicSearchService;
	
	@RequestMapping
	public String view(@RequestParam("id") String sensepicid, ModelMap model){
		SensePic pic = sensepicService.findById(sensepicid);
		if(pic == null){
			return error();
		}
		
		Users user = SecurityUserHolder.getCurrentUser();
		Folder folder = folderService.findById(pic.getFolderId());
		if(folder == null || !user.getId().equals(folder.getUserId())){
			return error();
		}
		
		model.addAttribute("sensePic", pic);
		List<Item> sameItems = new ArrayList<Item>();
		List<Item> similarItems = new ArrayList<Item>();
		LinkedHashMap<Item, Float> searchResult = sensePicSearchService.searchBySensePic(pic);
		int same = 0, similar=0;
		for(Item item : searchResult.keySet()){
			if(searchResult.get(item)==1){
				if(same<10){
					sameItems.add(item);
					same++;
				}
			}else{
				if(similar<10){
					similarItems.add(item);
					similar++;
				}
			}
		}
		model.addAttribute("sameItems", sameItems);
		model.addAttribute("similarItems", similarItems);
		
		return "/pacall/viewpic/view";
	}

	private String error() {
		return "redirect:/pacall/folder";
	}
}
