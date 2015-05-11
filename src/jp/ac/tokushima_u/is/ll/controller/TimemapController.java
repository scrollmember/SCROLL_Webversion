package jp.ac.tokushima_u.is.ll.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.GoogleMapService;
import jp.ac.tokushima_u.is.ll.service.ItemService;
import jp.ac.tokushima_u.is.ll.service.PropertyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;


@Controller
@RequestMapping("/timemap")
public class TimemapController {
	
	@Autowired
	private ItemService itemService;
	@Autowired
	private PropertyService propertyService;
    @Autowired
    private GoogleMapService googleMapService;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
    @ModelAttribute("googleMapApi")
    public String googleApiKey() {
        return googleMapService.getGoogleMapApi();
    }
	
	@RequestMapping(value="/mylogs", params="format=json")
	@ResponseBody
	public String mylogsJson(HttpServletRequest request){
		Users user = SecurityUserHolder.getCurrentUser();
		List<Item> itemList = itemService.searchMyitemsForTimemap(user.getId());
		List<Map<String, Object>> dataset = new ArrayList<Map<String,Object>>();
		for(Item item: itemList){
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("title", item.getDefaultTitle());
			data.put("start", format.format(item.getCreateTime()));
			Map<String, Double> point = new HashMap<String, Double>();
			point.put("lat", item.getItemLat());
			point.put("lon", item.getItemLng());
			data.put("point", point);
			Map<String, Object> options = new HashMap<String, Object>();
			String url = request.getContextPath()+"/item/"+item.getId();
			String discription = "<a target=\"_blank\"href=\""+url+"\">"+item.getDefaultTitle()+"</a>";
			if(item.getImage()!=null){
				discription+= "<br/><a target=\"_blank\"href=\""+url+"\"><img height=\"70px\" src=\""+propertyService.getStaticserverUrl()+"/"+propertyService.getProjectName()+"/"+item.getImage().getId()+"_160x120.png\" /></a>";
			}
			options.put("description", discription);
			data.put("options", options);
			dataset.add(data);
		}
		Gson gson = new Gson();
		return gson.toJson(dataset);
	}
	
	@RequestMapping(value="/mylogs")
	public String mylogs(){
		return "/timemap/mylogs";
	}
}
