package jp.ac.tokushima_u.is.ll.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.form.ItemEditForm;
import jp.ac.tokushima_u.is.ll.service.GoogleTranslateService;
import jp.ac.tokushima_u.is.ll.service.ItemService;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class WordImportUtils {
	public static void main(String[] args){
		File folder = new File("D:/User/Desktop/englishWord");
		
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		
		GoogleTranslateService translateService = context.getBean(GoogleTranslateService.class);
		ItemService itemService = context.getBean(ItemService.class);
		File file = new File(folder, "svl12000_level12.txt");
		try {
			List<String> lines = FileUtils.readLines(file);
			for(String word: lines){
				word = word.trim();
				if(StringUtils.isBlank(word)){
					continue;
				}
				ItemEditForm form = new ItemEditForm();
				form.setTag("Level 12");
				HashMap<String, String> titleMap = new HashMap<String, String>();
				titleMap.put("en", word);
				titleMap.put("ja", translateService.translate(word, com.google.api.translate.Language.ENGLISH, com.google.api.translate.Language.JAPANESE));
				titleMap.put("zh", translateService.translate(word, com.google.api.translate.Language.ENGLISH, com.google.api.translate.Language.CHINESE));
				form.setTitleMap(titleMap);
				form.setShareLevel(Item.ShareLevel.PUBLIC);
				
				itemService.createByForm(form);
				System.out.println(titleMap.get("en")+","+titleMap.get("ja")+titleMap.get("zh"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
