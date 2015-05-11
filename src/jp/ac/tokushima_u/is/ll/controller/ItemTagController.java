package jp.ac.tokushima_u.is.ll.controller;

import java.util.ArrayList;
import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.ItemTag;
import jp.ac.tokushima_u.is.ll.service.ItemTagService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
@RequestMapping("/itemtag")
public class ItemTagController {

	@Autowired
	private ItemTagService itemTagService;

	@RequestMapping
	@ResponseBody
	public String search(String q){
		List<ItemTag> result = itemTagService.search(q);
		List<String> tagList = new ArrayList<String>();
		for(ItemTag t: result){
			tagList.add(t.getTag());
		}
		Gson gson = new Gson();
		return gson.toJson(tagList);
	}
}
