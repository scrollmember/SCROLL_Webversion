package jp.ac.tokushima_u.is.ll.service;

import javax.xml.rpc.Stub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Translation.nlp.nict.servicetype.TranslationServiceProxy;

import com.google.api.translate.Language;

@Service
public class GoogleTranslateService {
	private static Logger logger = LoggerFactory.getLogger(GoogleTranslateService.class);
	@Autowired
	private PropertyService propertyService;

	public String translate(String text, Language from, Language to) {
		
		TranslationServiceProxy service = new TranslationServiceProxy();
		((Stub)service.getTranslationService())._setProperty(Stub.USERNAME_PROPERTY, propertyService.getLangridUsername());
		((Stub)service.getTranslationService())._setProperty(Stub.PASSWORD_PROPERTY, propertyService.getLangridPassword());
		try {
			String result = service.translate(from.toString(), to.toString(), text);
			return result;
		} catch (Exception e) {
			logger.error("Google Translate Error", e);
		}
		return "";
//		GoogleAPI.setHttpReferrer(propertyService.getSystemUrl());
//		GoogleAPI.setKey(propertyService.getGoogleApi());
//		String translatedText;
//		try {
//			translatedText = Translate.DEFAULT.execute(text, from, to);
//			return translatedText;
//		} catch (Exception e) {
//			logger.error("Google Translate Error", e);
//			return "";
//		}
	}

	public String translateByCode(String text, String from, String to) {
		return translate(text, com.google.api.translate.Language.fromString(from), com.google.api.translate.Language.fromString(to));
	}
}
