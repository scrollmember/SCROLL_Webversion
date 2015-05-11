package jp.ac.tokushima_u.is.ll.controller;

import jp.ac.tokushima_u.is.ll.service.PropertyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("qrcodeprint")
public class QrcodePrintController {
	
	@Autowired
	private PropertyService propertyService;
	
    @ModelAttribute("systemUrl")
    public String systemUrl(){
    	return propertyService.getSystemUrl();
    }
	
	@RequestMapping(method=RequestMethod.GET)
	public String print(String content, ModelMap model){
		model.addAttribute("content", content);
		return "qrcodeprint/print";
	}
}
