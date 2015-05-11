package jp.ac.tokushima_u.is.ll.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jp.ac.tokushima_u.is.ll.entity.Language;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.LanguageService;
import jp.ac.tokushima_u.is.ll.service.PropertyService;
import jp.ac.tokushima_u.is.ll.service.WordNetService;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
@RequestMapping("/wordmap")
public class WordMapController {

    private static final Logger logger = LoggerFactory.getLogger(WordMapController.class);
    @Autowired
    private LanguageService languageService;
	@Autowired
	private PropertyService propertyService;
	@Autowired
	private WordNetService wordnetService;
	

    @ModelAttribute("langList")
    public List<Language> populateLanguageList() {
    	List<Language> langList = languageService.searchAllLanguage();
    	Hibernate.initialize(langList);
        return langList;
    }

    @ModelAttribute("systemUrl")
    public String systemUrl(){
    	return propertyService.getSystemUrl();
    }
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }


	@RequestMapping(value="/{word}", params="format=json")
	@ResponseBody
	public String wordmapJson(@PathVariable String word, HttpServletRequest request){
        String userid = SecurityUserHolder.getCurrentUser().getId();
		Map<String, Object> dataset = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> children = new ArrayList<Object>();
        // Synonym
        List<Object[]> Synonym = wordnetService.getSynonym(word, userid);
        for (Object synonym[] : Synonym) {
			String name = ((String)synonym[0]).replaceAll("_", " ");
			String learned = (String)synonym[3];
	        Map<String, Object> child = new HashMap<String, Object>();
			child.put("word", name);
			child.put("type", "syno");
			child.put("flag", learned);
			children.add(child);
        }
		// Hypenym
        List<Object[]> Hypenym = wordnetService.getSynsetByLink(word, "hype", userid);
		for (Object hypenym[]: Hypenym) {
			String name = ((String)hypenym[0]).replaceAll("_", " ");
			String learned = (String)hypenym[3];
	        Map<String, Object> child = new HashMap<String, Object>();
			child.put("word", name);
			child.put("type", "hype");
			child.put("flag", learned);
			children.add(child);
		}
		// Hyponym
        List<Object[]> Hyponym = wordnetService.getSynsetByLink(word, "hypo", userid);
		for (Object hyponym[]: Hyponym) {
			String name = ((String)hyponym[0]).replaceAll("_", " ");
			String learned = (String)hyponym[3];
	        Map<String, Object> child = new HashMap<String, Object>();
			child.put("word", name);
			child.put("type", "hypo");
			child.put("flag", learned);
			children.add(child);
		}
//        // Synonym
//        List<Word> Synonym = wordnetService.getSynonym(word);
//		for (Word synonym: Synonym) {
//			String name = synonym.getLemma().replaceAll("_", " ");
//	        Map<String, Object> child = new HashMap<String, Object>();
//			child.put("word", name);
//			child.put("type", "syno");
//			children.add(child);
//		}
//		// Hypenym
//        List<Synset> Hypenym = wordnetService.getHypenym(word);
//		for (Synset hypenym: Hypenym) {
//			String name = hypenym.getName().replaceAll("_", " ");
//	        Map<String, Object> child = new HashMap<String, Object>();
//			child.put("word", name);
//			child.put("type", "hype");
//			children.add(child);
//		}
//		// Hyponym
//        List<Synset> Hyponym = wordnetService.getHyponym(word);
//		for (Synset hyponym: Hyponym) {
//			String name = hyponym.getName().replaceAll("_", " ");
//	        Map<String, Object> child = new HashMap<String, Object>();
//			child.put("word", name);
//			child.put("type", "hypo");
//			children.add(child);
//		}
		//Collections.shuffle(children);
		data.put("children", children);
		dataset.put(word.replaceAll("_", " "), data);
		Gson gson = new Gson();
		return gson.toJson(dataset);
	}

    @RequestMapping(value = "/{word}", method = RequestMethod.GET)
    public String show(@PathVariable String word, ModelMap model) {

        //HashMap<String,Object> wordnet = new HashMap<String,Object>();
        //wordnet.put("syno", wordnetService.getSynonym("dog"));// Word
        //wordnet.put("hype", wordnetService.getHypenym("dog"));// Synlink
        //wordnet.put("hypo", wordnetService.getHyponym("dog"));// Synlink
        //wordnet.put("sumo", wordnetService.getSUMO("dog"));// Xlink
        //model.addAttribute("Syno", wordnetService.getSynonym("dog"));
        //model.addAttribute("Syno", wordnetService.getSynonym(word));
        //model.addAttribute("Hype", wordnetService.getHypenym(word));
        //Double aaa = wordnetService.getSynsetSimilarity("05398609-n", "07922955-n");// juice criollo
        //Double bbb = wordnetService.getWordSimilarity("juice", "criollo");
        

        return "item/wn_show";
    }

    @RequestMapping(value = "/synset/{synset}", method = RequestMethod.GET)
    public String synset_show(@PathVariable String synset, ModelMap model) {


        return "item/wn_show";
    }
 

}
