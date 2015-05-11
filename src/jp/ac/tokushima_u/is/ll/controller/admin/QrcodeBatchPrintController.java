package jp.ac.tokushima_u.is.ll.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/qrcodebatchprint")
public class QrcodeBatchPrintController {
	
	@RequestMapping
	public String index(ModelMap model){
		return "admin/qrcodebatchprint/print";
	}
}
