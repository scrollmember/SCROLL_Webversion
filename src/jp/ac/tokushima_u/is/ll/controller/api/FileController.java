package jp.ac.tokushima_u.is.ll.controller.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.service.ItemService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
@RequestMapping("/api/file")
public class FileController {
	
	@Autowired
	private ItemService itemService;
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@RequestMapping("/itemsbymd5")
	@ResponseBody
	public String itemsByMd5(String md5){
		
		if(StringUtils.isBlank(md5)){
			return "error_md5_empty";
		}
		
		List<Item> itemList = itemService.searchItemsByMD5(md5);
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		
		for(Item item: itemList){
			Map<String, String> obj = new HashMap<String, String>();
			obj.put("itemid", item.getId());
			obj.put("date", format.format(item.getCreateTime()));
			result.add(obj);
		}
		
		Gson gson = new Gson();
		return gson.toJson(result);
	}
}
