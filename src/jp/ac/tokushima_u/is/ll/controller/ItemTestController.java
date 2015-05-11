package jp.ac.tokushima_u.is.ll.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jp.ac.tokushima_u.is.ll.common.orm.Page;
import jp.ac.tokushima_u.is.ll.entity.C2DMessage;
import jp.ac.tokushima_u.is.ll.entity.Category;
import jp.ac.tokushima_u.is.ll.entity.FileData;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.ItemItemTag;
import jp.ac.tokushima_u.is.ll.entity.ItemTitle;
import jp.ac.tokushima_u.is.ll.entity.Language;
import jp.ac.tokushima_u.is.ll.entity.QuestionType;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.entity.wordnet.Sense;
import jp.ac.tokushima_u.is.ll.entity.wordnet.SynsetDef;
//import jp.ac.tokushima_u.is.ll.entity.wordnet.RelatedObject;
import jp.ac.tokushima_u.is.ll.form.ItemCommentForm;
import jp.ac.tokushima_u.is.ll.form.ItemEditForm;
import jp.ac.tokushima_u.is.ll.form.ItemSearchCondForm;
import jp.ac.tokushima_u.is.ll.form.validator.ItemEditFormValidator;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.C2DMessageService;
import jp.ac.tokushima_u.is.ll.service.GoogleMapService;
import jp.ac.tokushima_u.is.ll.service.ItemRatingService;
import jp.ac.tokushima_u.is.ll.service.ItemRelationService;
import jp.ac.tokushima_u.is.ll.service.ItemService;
import jp.ac.tokushima_u.is.ll.service.ItemTagRelatedService;
import jp.ac.tokushima_u.is.ll.service.LanguageService;
import jp.ac.tokushima_u.is.ll.service.LogService;
import jp.ac.tokushima_u.is.ll.service.PropertyService;
import jp.ac.tokushima_u.is.ll.service.UserService;
import jp.ac.tokushima_u.is.ll.service.WordNetService;

import jp.ac.tokushima_u.is.ll.util.Constants;
import jp.ac.tokushima_u.is.ll.util.FilenameUtil;
import jp.ac.tokushima_u.is.ll.util.SerializeUtil;
import jp.ac.tokushima_u.is.ll.util.TagCloudConverter;
import jp.ac.tokushima_u.is.ll.ws.service.model.ItemForm;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
@RequestMapping("/item_t")
public class ItemTestController {

    private static final Logger logger = LoggerFactory.getLogger(ItemTestController.class);
    @Autowired
    private ItemService itemService;
    @Autowired
    private GoogleMapService googleMapService;
    @Autowired
    private LanguageService languageService;
    @Autowired
    private LogService logService;
    @Autowired
    private ItemRatingService itemRatingService;
    @Autowired
    private UserService userService;
	@Autowired
	private PropertyService propertyService;
	@Autowired
	private WordNetService wordnetService;
	@Autowired
	private ItemRelationService itemRelationService;
	
	@Autowired
	private C2DMessageService c2dmMessageService;

	@Autowired
	private ItemTagRelatedService itemTagRelatedService;
	
    @ModelAttribute("langList")
    public List<Language> populateLanguageList() {
    	List<Language> langList = languageService.searchAllLanguage();
    	Hibernate.initialize(langList);
        return langList;
    }

    @ModelAttribute("googleMapApi")
    public String googleApiKey() {
        return googleMapService.getGoogleMapApi();
    }
    @ModelAttribute("systemUrl")
    public String systemUrl(){
    	return propertyService.getSystemUrl();
    }
    
    @ModelAttribute("questionTypes")
    public List<QuestionType> searchQuestionTypes(){
    	return itemService.searchAllQuestionTypes();
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @RequestMapping
    public String index(@ModelAttribute("searchCond") ItemSearchCondForm form, ModelMap model) {
//        model.addAttribute("itemList", itemService.searchAllItemsByCond(form));
        model.addAttribute("itemPage", itemService.searchItemPageByCond(form));
        addTagCloud(model);
//        Page<Item> itemPage = (Page<Item>)model.get("itemPage");
        return "item/list";
    }
    
    private void addTagCloud(ModelMap model) {
    	Map<String, Integer> tagCloud = TagCloudConverter.convert(itemService.findTagCloud());
    	model.addAttribute("tagCloud", tagCloud);
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
    public String test(@ModelAttribute("searchCond") ItemSearchCondForm form, ModelMap model, HttpServletRequest request) {
		model.clear();
    	Page<Item> page = itemService.searchItemPageByCond(form);
    	if(page!=null&&page.getResult()!=null&&page.getResult().size()>0)
    	{
    		List<Item>results = page.getResult();
    		List<ItemForm>forms = new ArrayList<ItemForm>();
    		for(Item item:results){
//    			String photoUrl = null;
//    			if(item.getImage()!=null)
//    				photoUrl = staticServerService.getImageFileURL(item.getImage().getId(), form.getImageLevel());
    			
    			ItemForm itemform = new ItemForm(item);
    			forms.add(itemform);
    		}
    		model.addAttribute("items", forms);
    	}
//    	addTagCloud(model);
        return "item/list";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable String id, ModelMap model) {
        Item item = itemService.findById(id);
        Users user = SecurityUserHolder.getCurrentUser();

        if (item == null || (item.getShareLevel() == Item.ShareLevel.PRIVATE && !item.getAuthor().getId().equals(user.getId()))) {
            return "redirect:/item";
        }
        model.addAttribute("item", item);
        

        // create synset list
		Map<String, Map<String,String>> dataset = new LinkedHashMap<String, Map<String,String>>();
        List<SynsetDef> SynsetList = wordnetService.getSynset(wordnetService.getEnTitle(item.getTitles()));
        for (SynsetDef define : SynsetList) {
			String synset = define.getSynset();
			String lang = define.getLang();
			String def = define.getDef();

			Map<String,String> map;
			if (dataset.containsKey(synset)) {
				map = dataset.get(synset);
				if (map.get(lang).length() != 0) {
					def = map.get(lang) + "; " + def;
				}
			} else {
				map = new LinkedHashMap<String,String>();
				map.put("eng", "");
				map.put("jpn", "");
			}
			if (lang.equals("eng") || lang.equals("jpn")) {
				map.put(lang, def);
			}
			dataset.put(synset, map);
        }
        model.addAttribute("Synset", dataset);
        
        //HashMap<String,Object> wordnet = new HashMap<String,Object>();
        //wordnet.put("syno", wordnetService.getSynonym("dog"));// Word
        //wordnet.put("hype", wordnetService.getHypenym("dog"));// Synlink
        //wordnet.put("hypo", wordnetService.getHyponym("dog"));// Synlink
        //wordnet.put("sumo", wordnetService.getSUMO("dog"));// Xlink
        //model.addAttribute("Syno", wordnetService.getSynonym("dog"));
        //model.addAttribute("Syno", wordnetService.getSynonym(item.getTitles()));
        model.addAttribute("Hype", wordnetService.getHypenym(item.getTitles()));
        model.addAttribute("ItemEntitle", wordnetService.getEnTitle(item.getTitles()));
        //Double aaa = wordnetService.getSynsetSimilarity("05398609-n", "07922955-n");// juice criollo
        //Double bbb = wordnetService.getWordSimilarity("juice", "criollo");
        //"402880c737c0e7cd0137c0ec2029001a"
        
       //ITEM related         
        String RelatedID=id;
        Map<String,String> relatedmap = new LinkedHashMap<String,String>();
       List<String> Related =itemTagRelatedService.getRelatedObject2(RelatedID);
       List<String> related2= new ArrayList<String>();
       if(Related.size()<10){
    	   for(int i=0;i<Related.size();i++){
    		   String memory=Related.get(i);
//    		   List<String> Related2 =itemTagRelatedService.getRelatedItem(memory); 
    		   
    			  related2.add(itemTagRelatedService.getRelatedItem(memory).get(0));
    			  relatedmap.put(Related.get(i),itemTagRelatedService.getRelatedItem(memory).get(0));
    			  if(i==Related.size()-1){
    				  
    				  model.addAttribute("sampletest",related2);
    				  model.addAttribute("sampletest2",relatedmap);
    				  
    			  }
    	   }
    	   
       }
       else{
    	   for(int i=0;i<=9;i++){
    		   String memory=Related.get(i);
//    		   List<String> Related2 =itemTagRelatedService.getRelatedItem(memory); 
    		   
    			  related2.add(itemTagRelatedService.getRelatedItem(memory).get(0));
    			  relatedmap.put(Related.get(i),itemTagRelatedService.getRelatedItem(memory).get(0));
    			  if(i==9){
    				  
    				  model.addAttribute("sampletest",related2);
    				  model.addAttribute("sampletest2",relatedmap);
    				  
    			  }
    			  
    	   } 
       }
       
       
       
       
       model.addAttribute("RelatedObject",Related);
      
//       model.addAttribute("RelatedObject", wordnetService.getEnTitle(item.getTitles()));
      
      //ITEM related END
       
       
//     for(@SuppressWarnings("unused") RelatedObject Relat: Related){
//    	 break;
//     }
        //FileType{video, image}
        FileData fileData = item.getImage();
        String fileType = "";
        if(fileData!=null && !StringUtils.isBlank(fileData.getOrigName())){
        	fileType = FilenameUtil.checkMediaType(fileData.getOrigName());
        }
        model.addAttribute("fileType", fileType);
        
        boolean ratingExist = itemRatingService.ratingExist(id);
        model.addAttribute("ratingExist", ratingExist);
        if (ratingExist) {
            model.put("votes", itemRatingService.countVotesNumber(item));
            model.put("avg", itemRatingService.avgRating(item));
        }
        model.addAttribute("relogable", itemService.isRelogable(item, user));
        
        //Category Path
        if(item.getCategory()!=null){
        	Category category = item.getCategory();
        	List<Category> rvCatList = new ArrayList<Category>();
        	Category node = category;
        	while(node!=null){
        		rvCatList.add(node);
        		node = node.getParent();
        	}
        	List<Category> catList = new ArrayList<Category>();
        	for(int i = rvCatList.size()-1;i>=0;i--){
        		catList.add(rvCatList.get(i));
        	}
        	model.addAttribute("categoryPath", catList);
        }
        logService.logUserReadItem(item, user, null, null,null);
        model.addAttribute("readCount", itemService.findReadCount(item.getId()));
        model.addAttribute("relogCount", itemService.findRelogCount(item.getId()));
        return "item/show_t";
    }


    @RequestMapping(value = "/{id}/wordmap", method = RequestMethod.GET)
    public String wordmap(@PathVariable String id, ModelMap model) {
        Item item = itemService.findById(id);
        if (item == null) {
            return "redirect:/item";
        }
        
        model.addAttribute("ItemEntitle",
        		StringEscapeUtils.escapeHtml(wordnetService.getEnTitle(item.getTitles()).replace("_", " ")) );
    	model.addAttribute("ItemID", StringEscapeUtils.escapeHtml(id) );
        
        return "item/wordmap";
    }

	@RequestMapping(value="/{id}/wordmap", params="format=json")
	@ResponseBody
	public String wordmapJson(@PathVariable String id, HttpServletRequest request){
        Item item = itemService.findById(id);

        String userid = SecurityUserHolder.getCurrentUser().getId();
        
        // Jsonで渡すデータ(dataの束)
		Map<String, Object> dataset = new HashMap<String, Object>();
		// 複数のリスト(datasetに格納)
		Map<String, Object> data = new HashMap<String, Object>();
		// 当該オブジェクトの情報リスト
		Map<String, Object> info = new HashMap<String, Object>();
		// 類似オブジェクトのリスト
		List<Object> children = new ArrayList<Object>();
		
        // English Title
        String word = wordnetService.getEnTitle(item.getTitles())
        	.replace("_", " ").toLowerCase();
		
        //FileType{video, image}
        FileData fileData = item.getImage();
        String fileType = "";
        if(fileData!=null && !StringUtils.isBlank(fileData.getOrigName())){
        	fileType = FilenameUtil.checkMediaType(fileData.getOrigName());
        }

        /************* infoに入れるデータ *************/ 
        // ItemID
    	info.put("id", item.getId());
        // image
        if (fileType.equals("image")) {
        	info.put("image", item.getImage().getId());
        }
        // Synset
        List<Sense> sl = wordnetService.getSynsetList(word);
		List<String> synsetList = new ArrayList<String>();
        for (Sense sd :sl) {
        	synsetList.add(sd.getSynset());
        }
        info.put("Synset", synsetList);
        
        
        /************* childrenに入れるデータ *************/ 
        // 意味が関連するアイテム取得
        List<Object[]> ritem = itemRelationService.getRelatedItemRAW(item, 0, 8);
        if (!ritem.isEmpty()) {
            for (Object[] itemdata : ritem) {
            	Item ri = (Item)itemdata[0];
    			String name = wordnetService.getEnTitle(ri.getTitles())
    				.replace("_", " ").toLowerCase();;
    			String learned = "";
    			if (!word.equals("")) {
	    	        Map<String, Object> child = new HashMap<String, Object>();
	    			child.put("word", name);
	    			child.put("id", ri.getId());
	    			child.put("type", "word");
	    			child.put("flag", learned);
	    			children.add(child);
    			}
            }
        } else {
        	Page<Item> pi = itemService.searchRelatedItemList(id);
            for (Item ri : pi.getResult()) {
    			String name = wordnetService.getEnTitle(ri.getTitles())
    				.replace("_", " ").toLowerCase();;
    			String learned = "";
    			if (!word.equals("")) {
	    	        Map<String, Object> child = new HashMap<String, Object>();
	    			child.put("word", name);
	    			child.put("id", ri.getId());
	    			child.put("type", "word");
	    			child.put("flag", learned);
	    			children.add(child);
    			}
            }
        }
        	
        // 類似画像をもつアイテム取得
        if (fileType.equals("image")) {
        	List<Object[]> si = itemRelationService.getSimilarImageByItersecRAW(id, 0, 8);
            for (Object[] itemdata : si) {
            	Item ri = (Item)itemdata[0];
    			String name = wordnetService.getEnTitle(ri.getTitles())
    				.replace("_", " ").toLowerCase();;
    			String learned = "";
    			if (!word.equals("")) {
	    	        Map<String, Object> child = new HashMap<String, Object>();
	    			child.put("word", name);
	    			child.put("id", ri.getId());
	    			child.put("type", "img");
	    			child.put("flag", learned);
	    			children.add(child);
    			}
            }
        }
        
        // 近くのアイテム取得
        if(item.getItemLat()!=null && item.getItemLng()!=null){
        	Page<Item> pi = itemService.searchNearestItems(item.getId(), item.getItemLat(), item.getItemLng(), 1, null, 8);
            for (Item ri : pi.getResult()) {
    			String name = wordnetService.getEnTitle(ri.getTitles())
    				.replace("_", " ").toLowerCase();;
    			String learned = "";
    			if (!word.equals("")) {
    				Map<String, Object> child = new HashMap<String, Object>();
	    			child.put("word", name);
	    			child.put("id", ri.getId());
	    			child.put("type", "pos");
	    			child.put("flag", learned);
	    			children.add(child);
    			}
            }
        }
        
        // 時間帯が近いアイテム取得
        List<Object[]> titem = itemRelationService.getSimilarTimeRAW(id, 0, 8);
        for (Object[] itemdata : titem) {
        	Item ri = (Item)itemdata[0];
			String name = wordnetService.getEnTitle(ri.getTitles())
				.replace("_", " ").toLowerCase();;
			String learned = "";
			if (!word.equals("")) {
				Map<String, Object> child = new HashMap<String, Object>();
				child.put("word", name);
				child.put("id", ri.getId());
				child.put("type", "time");
				child.put("flag", learned);
				children.add(child);
			}
        }
        
        
        
		
        // アイテム一覧を結合
		data.put("children", children);
		data.put("info", info);
		dataset.put(word, data);
		return new Gson().toJson(dataset);
	}
	
	
	
	
	
	
    
    
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(@ModelAttribute("item") ItemEditForm form, ModelMap model) {
    	Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
    	model.addAttribute("user", user);
    	
    	List<Language> langs = new ArrayList<Language>();
    	for(Language lang: user.getMyLangs()){
    		if(!langs.contains(lang))langs.add(lang);
    	}
    	for(Language lang: user.getStudyLangs()){
    		if(!langs.contains(lang))langs.add(lang);
    	}
    	Language english = languageService.findUniqueLangByCode("en");
    	if(!langs.contains(english))langs.add(english);
    	
    	model.addAttribute("langs", langs);
    	if(user.getDefaultCategory()!=null) form.setCategoryId(user.getDefaultCategory().getId());
        return "item/add";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@ModelAttribute("item") ItemEditForm form, BindingResult result, ModelMap model) {
        new ItemEditFormValidator().validate(form, result);
        if (result.hasErrors()) {
        	return this.add(form, model);
        }
        try {
            Item item = this.itemService.createByForm(form);
            
            Users user = userService.getById(SecurityUserHolder
    				.getCurrentUser().getId());
        	
        	HashMap<String,String[]>params = new HashMap<String,String[]>();
    		C2DMessage c2dmessage = new C2DMessage();
    		c2dmessage.setCollapse(Constants.COLLAPSE_KEY_SYNC);
    		c2dmessage.setIsDelayIdle(new Integer(0));
    		try{
    			c2dmessage.setParams(SerializeUtil.serialize(params));
    		}catch(Exception e){
    			
    		}
    		this.c2dmMessageService.addMessage(c2dmessage,user);
            
            return "redirect:/item/" + item.getId() + "/related?fromcreated=true";
        } catch (IOException ex) {
            logger.error("Error occurred when create item", ex);
        }
        
        return "redirect:/item";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable String id, @ModelAttribute("form") ItemEditForm form, ModelMap model) {
        Item item = itemService.findById(id);
        if (item == null || (!SecurityUserHolder.getCurrentUser().getId().equals(item.getAuthor().getId()) && !SecurityUserHolder.getCurrentUser().getAuth().equals(Users.UsersAuth.ADMIN))) {
            return "redirect:/item";
        }
        form = new ItemEditForm(item);
        
    	List<ItemTitle> titles = item.getTitles();
    	HashMap<String, String> titleMap = new HashMap<String, String>();
    	for(ItemTitle title: titles){
    		titleMap.put(title.getLanguage().getCode(), title.getContent());
    	}
    	form.setTitleMap(titleMap);
        
        model.addAttribute("item", item);
        model.addAttribute("form", form);
        
        //FileType{video, image}
        FileData fileData = item.getImage();
        String fileType = "";
        if(fileData!=null && !StringUtils.isBlank(fileData.getOrigName())){
        	fileType = FilenameUtil.checkMediaType(fileData.getOrigName());
        }
        model.addAttribute("fileType", fileType);
        
    	Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
    	model.addAttribute("user", user);
    	
       	List<Language> langs = new ArrayList<Language>();
    	for(ItemTitle t:item.getTitles()){
    		if(!langs.contains(t))langs.add(t.getLanguage());
    	}
    	for(Language lang: user.getMyLangs()){
    		if(!langs.contains(lang))langs.add(lang);
    	}
    	for(Language lang: user.getStudyLangs()){
    		if(!langs.contains(lang))langs.add(lang);
    	}
    	Language english = languageService.findUniqueLangByCode("en");
    	if(!langs.contains(english))langs.add(english);
    	model.addAttribute("langs", langs);
    	
    	
    	if(item.getCategory()!=null) form.setCategoryId(item.getCategory().getId());
        return "item/edit";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    public String update(@PathVariable String id, @ModelAttribute("form") ItemEditForm form, BindingResult result, ModelMap model) {
        Item item = itemService.findById(id);
        if (item == null || (!SecurityUserHolder.getCurrentUser().getId().equals(item.getAuthor().getId()) && !SecurityUserHolder.getCurrentUser().getAuth().equals(Users.UsersAuth.ADMIN))) {
            return "redirect:/item";
        }
        if (item.getImage() != null) {
            form.setFileExist(true);
        }
        new ItemEditFormValidator().validate(form, result);
        if (result.hasErrors()) {
            return "item/" + id + "/edit";
        }
        try {
            this.itemService.updateByForm(id, form);
            
            Users user = userService.getById(SecurityUserHolder
    				.getCurrentUser().getId());
        	
        	HashMap<String,String[]>params = new HashMap<String,String[]>();
    		C2DMessage c2dmessage = new C2DMessage();
    		c2dmessage.setCollapse(Constants.COLLAPSE_KEY_SYNC);
    		c2dmessage.setIsDelayIdle(new Integer(0));
    		try{
    			c2dmessage.setParams(SerializeUtil.serialize(params));
    		}catch(Exception e){
    			
    		}
    		this.c2dmMessageService.addMessage(c2dmessage,user);
            
        } catch (IOException ex) {
            logger.error("Error occurred when create item", ex);
        }
        return "redirect:/item/" + id;
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public String delete(@PathVariable String id) {
        Item item = itemService.findById(id);
        if (item == null || (!SecurityUserHolder.getCurrentUser().getId().equals(item.getAuthor().getId()) && !SecurityUserHolder.getCurrentUser().getAuth().equals(Users.UsersAuth.ADMIN))) {
            return "redirect:/item";
        }
        this.itemService.delete(item);
        return "redirect:/item";
    }

    @RequestMapping(value = "/{id}/related", method = RequestMethod.GET)
    public String related(@PathVariable String id, String fromcreated, ModelMap model) {
    	// update Histogram
    	itemRelationService.updateImageHistogram();
    	
        Item item = itemService.findById(id);
        if (item == null) {
            return "redirect:/item";
        }
        
        if(!StringUtils.isBlank(fromcreated)){
        	model.addAttribute("fromcreated", true);
        }else{
        	model.addAttribute("fromcreated", false);
        }
        
        // item
        model.addAttribute("item", item);
        
        //FileType{video, image}
        FileData fileData = item.getImage();
        String fileType = "";
        if(fileData!=null && !StringUtils.isBlank(fileData.getOrigName())){
        	fileType = FilenameUtil.checkMediaType(fileData.getOrigName());
        }
        model.addAttribute("fileType", fileType);

        // wordnet
    	model.addAttribute("relatedWordList", wordnetService.getRelatedWord(item, 0, 6));
        
        // related words
        Map<String, List<Object>> ritem = itemRelationService.getRelatedItem(item, 0, 4);
        if (!ritem.get("Item").isEmpty()) {
        	model.addAttribute("similarItemList", ritem);
        } else {
        	model.addAttribute("relatedItemList", itemService.searchRelatedItemList(id));
        }
        	
        // image
        if (fileType.equals("image")) {
        	model.addAttribute("similarImageList", itemRelationService.getSimilarImageByItersec(id, 0, 4));
        }
        
        // nearest object
        if(item.getItemLat()!=null && item.getItemLng()!=null){
        	model.addAttribute("nearestItems", itemService.searchNearestItems(item.getId(), item.getItemLat(), item.getItemLng(), 1, null, 4));
        }
        
        // time
        model.addAttribute("similarTimeList", itemRelationService.getSimilarTime(id, 0, 4));
        
        
        
        return "item/related_t";
    }


    @RequestMapping(value = "/{id}/related/word", method = RequestMethod.GET)
    public String related_word(@PathVariable String id, String fromcreated, ModelMap model) {
    	
        Item item = itemService.findById(id);
        if (item == null) {
            return "redirect:/item";
        }
        
        if(!StringUtils.isBlank(fromcreated)){
        	model.addAttribute("fromcreated", true);
        }else{
        	model.addAttribute("fromcreated", false);
        }
        
        // item
        model.addAttribute("item", item);
        
        //FileType{video, image}
        FileData fileData = item.getImage();
        String fileType = "";
        if(fileData!=null && !StringUtils.isBlank(fileData.getOrigName())){
        	fileType = FilenameUtil.checkMediaType(fileData.getOrigName());
        }
        model.addAttribute("fileType", fileType);
        
        // related words
        Map<String, List<Object>> ritem = itemRelationService.getRelatedItem(item, 0, 24);
        if (!ritem.get("Item").isEmpty()) {
        	model.addAttribute("similarItemList", ritem);
        } else {
        	model.addAttribute("relatedItemList", itemService.searchRelatedItemList(id));
        }
        
        return "item/related_t";
    }

    @RequestMapping(value = "/{id}/related/near", method = RequestMethod.GET)
    public String related_near(@PathVariable String id, String fromcreated, ModelMap model) {
    	
        Item item = itemService.findById(id);
        if (item == null) {
            return "redirect:/item";
        }
        
        if(!StringUtils.isBlank(fromcreated)){
        	model.addAttribute("fromcreated", true);
        }else{
        	model.addAttribute("fromcreated", false);
        }
        
        // item
        model.addAttribute("item", item);
        
        //FileType{video, image}
        FileData fileData = item.getImage();
        String fileType = "";
        if(fileData!=null && !StringUtils.isBlank(fileData.getOrigName())){
        	fileType = FilenameUtil.checkMediaType(fileData.getOrigName());
        }
        model.addAttribute("fileType", fileType);

        // nearest object
        if(item.getItemLat()!=null && item.getItemLng()!=null){
        	model.addAttribute("nearestItems", itemService.searchNearestItems(item.getId(), item.getItemLat(), item.getItemLng(), 1, null, 24));
        }
        
        return "item/related_t";
    }

    @RequestMapping(value = "/{id}/related/image", method = RequestMethod.GET)
    public String related_image(@PathVariable String id, String fromcreated, ModelMap model) {
    	
        Item item = itemService.findById(id);
        if (item == null) {
            return "redirect:/item";
        }
        
        if(!StringUtils.isBlank(fromcreated)){
        	model.addAttribute("fromcreated", true);
        }else{
        	model.addAttribute("fromcreated", false);
        }
        
        // item
        model.addAttribute("item", item);
        
        //FileType{video, image}
        FileData fileData = item.getImage();
        String fileType = "";
        if(fileData!=null && !StringUtils.isBlank(fileData.getOrigName())){
        	fileType = FilenameUtil.checkMediaType(fileData.getOrigName());
        }
        model.addAttribute("fileType", fileType);

        // image
        if (fileType.equals("image")) {
        	model.addAttribute("similarImageList", itemRelationService.getSimilarImageByItersec(id, 0, 24));
        }
        
        return "item/related_t";
    }


    @RequestMapping(value = "/{id}/related/time", method = RequestMethod.GET)
    public String related_time(@PathVariable String id, String fromcreated, ModelMap model) {
    	
        Item item = itemService.findById(id);
        if (item == null) {
            return "redirect:/item";
        }
        
        if(!StringUtils.isBlank(fromcreated)){
        	model.addAttribute("fromcreated", true);
        }else{
        	model.addAttribute("fromcreated", false);
        }
        
        // item
        model.addAttribute("item", item);
        
        //FileType{video, image}
        FileData fileData = item.getImage();
        String fileType = "";
        if(fileData!=null && !StringUtils.isBlank(fileData.getOrigName())){
        	fileType = FilenameUtil.checkMediaType(fileData.getOrigName());
        }
        model.addAttribute("fileType", fileType);

        // time
        model.addAttribute("similarTimeList", itemRelationService.getSimilarTime(id, 0, 24));
        
        return "item/related_t";
    }
    
    
    @RequestMapping(value = "/{id}/comment", method = RequestMethod.POST)
    public String submitComment(@PathVariable String id, @ModelAttribute ItemCommentForm form) {
        this.itemService.createCommentByForm(id, form);
        return "redirect:/item/" + id;
    }

    @RequestMapping(value = "/{id}/question", method = RequestMethod.POST)
    public String submitQuestion(@PathVariable String id, HttpServletRequest request) {
        String content = request.getParameter("content");
        this.itemService.createAnswerByForm(id, content);
        return "redirect:/item/" + id;
    }

    @RequestMapping(value = "/moretoanswer", method = RequestMethod.GET)
    public String toAnswerMore(@ModelAttribute("searchCond") ItemSearchCondForm form, ModelMap model) {
        model.addAttribute("itemList", itemService.searchAllToAnswer());
        addTagCloud(model);
        return "item/list";
    }

    @RequestMapping(value = "/{id}/relog", method = RequestMethod.POST)
    public String relog(@PathVariable String id) {
        Item item = itemService.findById(id);
        if (item == null) {
            return "redirect:/item/" + id;
        }
        Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
        if (user == null) {
            return "redirect:/item/" + id;
        }
        Item reItem = this.itemService.relog(item, user);
        return "redirect:/item/" + reItem.getId();
    }

    @RequestMapping(value = "/{id}/questionconfirm", method = RequestMethod.POST)
    public String questionConfirm(@PathVariable String id) {
        Item item = itemService.findById(id);
        if (item == null) {
            return "redirect:/item/" + id;
        }
        Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
        if (user == null) {
            return "redirect:/item/" + id;
        }
        if (!item.getAuthor().getId().equals(user.getId()) || item.getQuestion() == null || item.getQuestion().getAnswerSet() == null || item.getQuestion().getAnswerSet().size() == 0) {
            return "redirect:/item/" + id;
        }
        itemService.questionConfirm(item, user);
        return "redirect:/item/" + id;
    }

    @RequestMapping(value = "/{id}/teacherconfirm", method = RequestMethod.POST)
    public String teacherConfirm(@PathVariable String id) {
        Item item = itemService.findById(id);
        if (item == null) {
            return "redirect:/item/" + id;
        }
        Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
//        if (user == null || user.getAuth() != Users.UsersAuth.ADMIN) {
//            return "redirect:/item/" + id;
//        }
        itemService.teacherConfirm(item, user);
        return "redirect:/item/" + id;
    }

    @RequestMapping(value = "/{id}/teacherreject", method = RequestMethod.POST)
    public String teacherReject(@PathVariable String id) {
        Item item = itemService.findById(id);
        if (item == null) {
            return "redirect:/item/" + id;
        }
        Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
        itemService.teacherReject(item, user);
        return "redirect:/item/" + id;
    }

    @RequestMapping(value = "/{id}/teacherdelcfmstatus", method = RequestMethod.POST)
    public String teacherDeleteStatus(@PathVariable String id) {
        Item item = itemService.findById(id);
        if (item == null) {
            return "redirect:/item/" + id;
        }
        Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
        itemService.teacherDeleteStatus(item, user);
        return "redirect:/item/" + id;
    }
    
    @RequestMapping(value="/view", method=RequestMethod.GET)
    public String view(String id, Double latitude,Double longitude, Float speed, ModelMap model){
    	Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
    	model.clear();
    	Item item = null;
    	if(id!=null)
    		item = this.itemService.findById(id);
    	try{
    		if(item!=null&&item.getId()!=null){
    			this.logService.logUserReadItem(item, user, latitude, longitude, speed);
    		}
    	}catch(ObjectNotFoundException e){
    		
    	}
    	return null;
    }
    
}
