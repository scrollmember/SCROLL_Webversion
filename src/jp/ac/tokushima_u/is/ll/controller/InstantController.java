package jp.ac.tokushima_u.is.ll.controller;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.form.ItemSearchCondForm;
import jp.ac.tokushima_u.is.ll.service.ItemService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Instant Search by mobile devices, auto detect mobile
 * @author Houbin
 *
 */
@Controller
@RequestMapping("/instant")
public class InstantController {
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item")
	public String item(String qrcode){
		if(StringUtils.isBlank(qrcode)) return "redirect:/item";
		ItemSearchCondForm form = new ItemSearchCondForm();
		form.setQrcode(qrcode.trim());
		List<Item> itemList = itemService.searchAllItemsByCond(form);
		if(itemList==null || itemList.size()==0) return "redirect:/item";
		if(itemList.size()==1){
			return "redirect:/item/"+itemList.get(0).getId();
		}else{
			return "redirect:/item?qrcode="+qrcode;
		}
	}
}
