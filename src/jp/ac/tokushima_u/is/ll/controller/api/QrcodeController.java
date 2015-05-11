package jp.ac.tokushima_u.is.ll.controller.api;

import jp.ac.tokushima_u.is.ll.util.KeyGenerateUtil;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/qrcode")
public class QrcodeController {
	
	@RequestMapping("/generate")
	@ResponseBody
	public String generate(){
		return KeyGenerateUtil.generateIdUUID();
	}
	
	public static void main(String[] args){
		System.out.println(new QrcodeController().generate());
	}
}
