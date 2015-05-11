package jp.ac.tokushima_u.is.ll.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

@Service
public class MicrosoftTranslateService {
	private static Logger logger = LoggerFactory.getLogger(MicrosoftTranslateService.class);
	@Autowired
	private PropertyService propertyService;

	public String translate(String text, Language from, Language to) {
		Translate.setKey(propertyService.getMicrosoftApi());
		String translatedText;
		try {
			translatedText = Translate.execute(text, from, to);
			return translatedText;
		} catch (Exception e) {
			logger.error("Google Translate Error", e);
			return "";
		}
	}

	public String translateByCode(String text, String from, String to) {
		Language f = transToLang(from);
		Language t = transToLang(to);
		return translate(text, f, t);
	}

	private Language transToLang(String la) {
		if(la==null)return Language.AUTO_DETECT;
		if("zh-cn".equals(la.toLowerCase()) || "zh_cn".equals(la.toLowerCase())){
			return Language.CHINESE_SIMPLIFIED;
		}else if("zh-tw".equals(la.toLowerCase()) || "zh_tw".equals(la.toLowerCase())){
			return Language.CHINESE_TRADITIONAL;
		}else if("zh".equals(la.toLowerCase())){
			return Language.CHINESE_SIMPLIFIED;
		}else{
			Language lang = Language.fromString(la.toLowerCase());
			if(lang==null){
				return Language.AUTO_DETECT;
			}else{
				return lang;
			}
		}
	}
}
