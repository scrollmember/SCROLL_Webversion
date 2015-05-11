package jp.ac.tokushima_u.is.ll.controller.api;

import java.util.ArrayList;
import java.util.List;

import jp.ac.tokushima_u.is.ll.common.orm.Page;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.ItemTitle;
import jp.ac.tokushima_u.is.ll.form.ItemSearchCondForm;
import jp.ac.tokushima_u.is.ll.form.WidgetForm;
import jp.ac.tokushima_u.is.ll.service.ItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
@RequestMapping("/api/widget")
public class WidgetController {
	@Autowired
    private ItemService itemService;

	@RequestMapping
	@ResponseBody
	public String latest(@ModelAttribute("searchCond") ItemSearchCondForm form, ModelMap model) {
		Page<Item> itemPage = itemService.searchItemPageByCond(form);
		List<WidgetForm> retitems = new ArrayList<WidgetForm>();
		for (Item item : itemPage.getResult()) {
			WidgetForm frm = new WidgetForm();
			frm.setImage(item.getImage() == null ? "noimage" : item.getImage().getId() + "");
			int k = 0;
			for (ItemTitle title : item.getTitles()) {
				if (k == 0) {
					frm.setLang1name(title.getLanguage().getName());
					frm.setLang1word(title.getContent());
				} else if (k == 1) {
					frm.setLang2name(title.getLanguage().getName());
					frm.setLang2word(title.getContent());
				} else {
					frm.setLang3name(title.getLanguage().getName());
					frm.setLang3word(title.getContent());
					break;
				}
				k++;
			}
			retitems.add(frm);
		}
		Gson gson = new Gson();
		return gson.toJson(retitems);
	}
}