package jp.ac.tokushima_u.is.ll.controller.pacall;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import jp.ac.tokushima_u.is.ll.common.page.Page;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.entity.pacall.Folder;
import jp.ac.tokushima_u.is.ll.entity.pacall.SensePic;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.pacall.FolderService;
import jp.ac.tokushima_u.is.ll.service.pacall.SensepicSearchService;
import jp.ac.tokushima_u.is.ll.service.pacall.SensepicService;
import jp.ac.tokushima_u.is.ll.util.PacallConstants;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/pacall/browse")
public class BrowseController {
	
	public static final String PACALL_BROWSE_LINENUM_KEY = "PACALL_BROWSE_LINENUM";
	
	@Autowired
	private FolderService folderService;
	@Autowired
	private SensepicService sensepicService;
	@Autowired
	private SensepicSearchService sensePicSearchService;
	@RequestMapping
	public String index(@RequestParam("id") String folderId, String type, Integer page, String lines, ModelMap model, HttpSession session){
		if(StringUtils.isBlank(folderId)){
			return error();
		}
		Users user = SecurityUserHolder.getCurrentUser();
		Folder folder = folderService.findById(folderId);
		if(folder == null || !user.getId().equals(folder.getUserId())){
			return error();
		}
		
		if(StringUtils.isBlank(type)){
			type = "";
		}
		
		Integer lineNum = null;
		if(!StringUtils.isBlank(lines)){
			try {
				lineNum = Integer.valueOf(lines);
			} catch (NumberFormatException e) {
				lineNum = null;
			}
		}
		
		if(lineNum!=null && lineNum>0){
			session.setAttribute(PACALL_BROWSE_LINENUM_KEY, lineNum*3);
		}
		
		if(PacallConstants.MANUAL.equals(type) || PacallConstants.NORMAL.equals(type) || PacallConstants.DUPLICATE.equals(type) || PacallConstants.SHAKE.equals(type) || PacallConstants.DARK.equals(type)){
		} else {
			type = null;
		}

		Integer pageSize = 0;
		try {
			pageSize = Integer.valueOf(session.getAttribute(PACALL_BROWSE_LINENUM_KEY).toString());
		} catch (Exception e) {
			pageSize = PacallConstants.DEFAULT_PAGE_SIZE;
			session.setAttribute(PACALL_BROWSE_LINENUM_KEY, pageSize);
		}
		
		if(page==null || page<1) page = 1;
		Page<SensePic> sensePicPage = sensepicService.findPage(folderId, type, page, pageSize);
		
		Map<String, Integer> similarityMap = new HashMap<String, Integer>();
		
		for(SensePic  pic: sensePicPage.getResult()){
			//Count SensePic Similar Level
			LinkedHashMap<Item, Float> searchResult = sensePicSearchService.searchBySensePic(pic);
			int similar=0;
			for(Item item : searchResult.keySet()){
				similar++;
			}
			if(similar>5) similar = 5;
			similarityMap.put(pic.getId(), similar);
		}
		model.addAttribute("similarityMap", similarityMap);
		
		model.addAttribute("folder", folder);
		model.addAttribute("sensePicPage", sensePicPage);
		model.addAttribute("type", type);
		model.addAttribute("typeCount", sensepicService.countEachType(folderId));
		model.addAttribute("lines", pageSize/3);
		return "pacall/browse/index";
	}

	private String error() {
		return "redirect:/pacall/folder";
	}
	
	public static void main(String[]  args){
		File file = new File("/Users/houbin/lucene");
		try {
			System.out.println(file.getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
