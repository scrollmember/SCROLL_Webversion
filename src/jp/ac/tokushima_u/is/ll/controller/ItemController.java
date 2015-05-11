package jp.ac.tokushima_u.is.ll.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.ac.tokushima_u.is.ll.common.orm.Page;
import jp.ac.tokushima_u.is.ll.entity.C2DMessage;
import jp.ac.tokushima_u.is.ll.entity.Category;
import jp.ac.tokushima_u.is.ll.entity.Cooccurrence;
import jp.ac.tokushima_u.is.ll.entity.FileData;
import jp.ac.tokushima_u.is.ll.entity.Groupmember;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.ItemTitle;
import jp.ac.tokushima_u.is.ll.entity.Itemlatlng;
import jp.ac.tokushima_u.is.ll.entity.Kasetting;
import jp.ac.tokushima_u.is.ll.entity.KnowledgeRanking;
import jp.ac.tokushima_u.is.ll.entity.Language;
import jp.ac.tokushima_u.is.ll.entity.PlaceCollocation;
import jp.ac.tokushima_u.is.ll.entity.QuestionType;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.entity.Usertest;
import jp.ac.tokushima_u.is.ll.form.AnalysisForm;
import jp.ac.tokushima_u.is.ll.form.ItemCommentForm;
import jp.ac.tokushima_u.is.ll.form.ItemEditForm;
import jp.ac.tokushima_u.is.ll.form.ItemSearchCondForm;
import jp.ac.tokushima_u.is.ll.form.validator.ItemEditFormValidator;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.C2DMessageService;
import jp.ac.tokushima_u.is.ll.service.GoogleMapService;
import jp.ac.tokushima_u.is.ll.service.ItemRatingService;
import jp.ac.tokushima_u.is.ll.service.ItemService;
import jp.ac.tokushima_u.is.ll.service.LanguageService;
import jp.ac.tokushima_u.is.ll.service.LogService;
import jp.ac.tokushima_u.is.ll.service.PropertyService;
import jp.ac.tokushima_u.is.ll.service.UserService;
import jp.ac.tokushima_u.is.ll.util.Constants;
import jp.ac.tokushima_u.is.ll.util.FilenameUtil;
import jp.ac.tokushima_u.is.ll.util.KeyGenerateUtil;
import jp.ac.tokushima_u.is.ll.util.SerializeUtil;
import jp.ac.tokushima_u.is.ll.util.TagCloudConverter;
import jp.ac.tokushima_u.is.ll.visualization.ReviewHistoryService;
import jp.ac.tokushima_u.is.ll.ws.service.model.ItemForm;



import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;
import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.data.attributes.api.AttributeOrigin;
import org.gephi.data.attributes.api.AttributeType;
import org.gephi.filters.api.FilterController;
import org.gephi.filters.api.Query;
import org.gephi.filters.api.Range;
import org.gephi.filters.plugin.graph.DegreeRangeBuilder.DegreeRangeFilter;
import org.gephi.filters.plugin.graph.EgoBuilder.EgoFilter;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.spi.GraphExporter;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.layout.plugin.AutoLayout;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.statistics.plugin.Degree;
import org.gephi.statistics.plugin.GraphDistance;
import org.hibernate.Hibernate;
import org.hibernate.ObjectNotFoundException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openide.util.Lookup;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



import com.google.gson.Gson;

@Controller
@RequestMapping("/item")
public class ItemController {

	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	private static final String DATE_PATTERN = "yyyy/MM/dd";
//	File directory1 = new File(
//			"C:/Users/mouri/work2/learninglogmain/WebContent/js/networkanalysis/");
	 File directory1=new File("/home/learninglog/NetworkGexf/Gexf");
	File checker = new File(
			"C:/Users/mouri/work2/learninglogmain/WebContent/js/networkanalysis/ka.gexf");
	private static final Logger logger = LoggerFactory
			.getLogger(ItemController.class);
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
	private ReviewHistoryService reviewHistoryService;
	@Autowired
	private ItemService itemservice;
	@Autowired
	private C2DMessageService c2dmMessageService;

	// Netwrok analysis recommendation
	ArrayList<Cooccurrence> ListRecommend = new ArrayList<Cooccurrence>();
	ArrayList<String> ListRecommend2 = new ArrayList<String>();
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
	public String systemUrl() {
		return propertyService.getSystemUrl();
	}

	@ModelAttribute("questionTypes")
	public List<QuestionType> searchQuestionTypes() {
		return itemService.searchAllQuestionTypes();
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}

	@RequestMapping
	public String index(@ModelAttribute("searchCond") ItemSearchCondForm form,
			ModelMap model) {
		model.addAttribute("itemList", itemService.searchAllItemsByCond(form));
		model.addAttribute("itemPage", itemService.searchItemPageByCond(form));
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		
		List<Groupmember> groupname = userService.allgroup(user.getId());
		if (groupname.size() != 0 && form.getUsername()==null) {
//			System.out.print(groupname.get(0).getId());
			Page<Item> itemPage = itemService.searchItemPageByCond2(form,
					groupname);
			model.addAttribute("itemPage", itemPage);
		}
		// Page<Item> itemPage = itemService.searchItemPageByCond2(form);
		// model.addAttribute("itemPage", itemPage);
		addTagCloud(model);
		// Page<Item> itemPage = (Page<Item>)model.get("itemPage");
		return "item/list";
	}

	private void addTagCloud(ModelMap model) {
		Map<String, Integer> tagCloud = TagCloudConverter.convert(itemService
				.findTagCloud());
		model.addAttribute("tagCloud", tagCloud);
	}
	@RequestMapping(value = "/place", params = "format=json",method = RequestMethod.GET)
	@ResponseBody
	public String test2(HttpServletRequest request,String key,String location, String sensor, HttpServletResponse res,String lat,String lng,ModelMap model
			) throws URISyntaxException, JsonParseException, IOException, org.json.JSONException {
//		System.out.print("lat:"+lat+"lng:"+lng);
		HttpURLConnection con = null;
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		String types="&types=accounting%7Cairport%7Camusement_park%7Caquarium%7Cart_gallery%7Cbakery%7Cbank%7Cbar%7Cbeauty_salon%7Cbicycle_store%7Cbook_store%7Cbowling_alley%7Cbus_station%7Ccafe%7Ccampground%7Ccar_dealer%7cemetery%7Cchurch%7Ccity_hall%7Cclothing_store%7Cconvenience_store%7Ccourthouse%7Cdentist%7Cdepartment_store%7Cdoctor%7Celectrician%7Celectronics_store%7Cembassy%7Cfinance%7Cfire_station%7Cflorist%7Cfood%7Cfuneral_home%7Cfurniture_store%7Cgas_station%7Cgrocery_or_supermarket%7Cgym%7Chair_care%7Chardware_store%7Chealth%7Chindu_temple%7Chome_goods_store%7Chospital%7Cinsurance_agency%7Cjewelry_store%7Claundry%7Clawyer%7Clibrary%7Cliquor_store%7Clodging%7Cmeal_delivery%7Cmosque%7Cmovie_rental%7Cmovie_theater%7Cmoving_company%7Cmuseum%7Cnight_club%7Cpark%7Cpet_store%7Cpharmacy%7Cphysiotherapist%7Cpolice%7Cpost_office%7Crestaurant%7Crv_park%7Cschool%7Cshoe_store%7Cshopping_mall%7Cspa%7Cstadium%7Cstore%7Csubway_station%7Ctaxi_stand%7Ctrain_station%7Cuniversity%7Czoo";
		String url="https://maps.googleapis.com/maps/api/place/search/json?location="+lat+","+lng+""+"&radius=500&language=ja&sensor=false&key=AIzaSyBnfkXkSQXxxLd7foZ7FK9c13GtoGO23dY"+types;
		
		HttpGet httpGet = new HttpGet(url);
		String j=null;
		try {
			HttpResponse response = client.execute(httpGet);
			
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				j=EntityUtils.toString(entity, "UTF-8"); 
				
//				InputStream content = entity.getContent();
//				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
//				String line;
//				while ((line = reader.readLine()) != null) {
//					builder.append(line);
//				}
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		List<String> listd= new ArrayList<String>();
		Gson gson = new Gson();
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		String json = "{\"screen_name\":\"katty0324\",\"age\":23}";
//		System.out.println(gson.toJson(j));
		
		try {
		    JSONObject jsonObject = new JSONObject(j);
		    JSONArray jsonarray = jsonObject.getJSONArray("results");
		    for(int i = 0; i < jsonarray.length(); i++) {
		    	 JSONObject data = jsonarray.getJSONObject(i);
		    	if(data.has("name")){
		    		listd.add(data.getString("name"));
		    	}
		    	JSONArray jsonarray2=data.getJSONArray("types");
		    	for(int c=0;c<jsonarray2.length();c++){
//		    		listd.add(jsonarray2.getString(c));
		    	}
//		    	map.put(data.getString("name"),listd);
		    	
		    }
//		    for(int i=0;i<listd.size();i++){
//		    System.out.println(listd.get(i));
//		    }
//		    System.out.println(map);
		} catch (JSONException e) {
		    e.printStackTrace();
		}
		model.addAttribute("placeinfo", listd);
	
//		List list = gson.fromJson(gson.toJson(j), List.class);
//		System.out.println(listd.size());
		return gson.toJson(j);
	}
	
	
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String test(@ModelAttribute("searchCond") ItemSearchCondForm form,
			ModelMap model, HttpServletResponse response) {
		model.clear();
		Page<Item> page = itemService.searchItemPageByCond(form);
		Gson gson = new Gson();
		if (page != null && page.getResult() != null
				&& page.getResult().size() > 0) {
			List<Item> results = page.getResult();
			List<ItemForm> forms = new ArrayList<ItemForm>();
			for (Item item : results) {
				// String photoUrl = null;
				// if(item.getImage()!=null)
				// photoUrl =
				// staticServerService.getImageFileURL(item.getImage().getId(),
				// form.getImageLevel());

				ItemForm itemform = new ItemForm(item);
				forms.add(itemform);
			}
			model.addAttribute("items", forms);
			// response.setContentType("text/plain;charset=UTF-8");
			// try{
			// String json = gson.toJson(forms);
			// response.getWriter().print(json);
			// }catch(IOException e){
			// e.printStackTrace();
			// }
		}
		return "index";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(@PathVariable String id, ModelMap model) {
		Item item = itemService.findById(id);
		PlaceCollocation place_content= itemService.place_content(id);
		
		Users user = SecurityUserHolder.getCurrentUser();

		if (item == null
				|| (item.getShareLevel() == Item.ShareLevel.PRIVATE && !item
						.getAuthor().getId().equals(user.getId()))) {
			return "redirect:/item";
		}
		model.addAttribute("item", item);
		model.addAttribute("placeinfo",place_content);
		// FileType{video, image}
		FileData fileData = item.getImage();
		String fileType = "";
		if (fileData != null && !StringUtils.isBlank(fileData.getOrigName())) {
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

		// Category Path
		if (item.getCategory() != null) {
			Category category = item.getCategory();
			List<Category> rvCatList = new ArrayList<Category>();
			Category node = category;
			while (node != null) {
				rvCatList.add(node);
				node = node.getParent();
			}
			List<Category> catList = new ArrayList<Category>();
			for (int i = rvCatList.size() - 1; i >= 0; i--) {
				catList.add(rvCatList.get(i));
			}
			model.addAttribute("categoryPath", catList);
		}
		logService.logUserReadItem(item, user, null, null, null);
		model.addAttribute("readCount", itemService.findReadCount(item.getId()));
		model.addAttribute("relogCount",
				itemService.findRelogCount(item.getId()));

		// model.addAttribute("itemState",
		// reviewHistoryService.searchUserItemState(user, item.getId()));
		model.addAttribute("itemState",
				reviewHistoryService.searchUserItemState(user, item));
		return "item/show";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(@ModelAttribute("item") ItemEditForm form, ModelMap model,String lat,String lng) throws org.json.JSONException {
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
//		System.out.println(lat);
//		System.out.println(lng);
		model.addAttribute("user", user);

		List<Language> langs = new ArrayList<Language>();
		for (Language lang : user.getMyLangs()) {
			if (!langs.contains(lang))
				langs.add(lang);
		}
		for (Language lang : user.getStudyLangs()) {
			if (!langs.contains(lang))
				langs.add(lang);
		}
		Language english = languageService.findUniqueLangByCode("en");
		if (!langs.contains(english))
			langs.add(english);

		model.addAttribute("langs", langs);
		if (user.getDefaultCategory() != null)
			form.setCategoryId(user.getDefaultCategory().getId());

		HttpURLConnection con = null;
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		String types="&types=accounting%7Cairport%7Camusement_park%7Caquarium%7Cart_gallery%7Cbakery%7Cbank%7Cbar%7Cbeauty_salon%7Cbicycle_store%7Cbook_store%7Cbowling_alley%7Cbus_station%7Ccafe%7Ccampground%7Ccar_dealer%7cemetery%7Cchurch%7Ccity_hall%7Cclothing_store%7Cconvenience_store%7Ccourthouse%7Cdentist%7Cdepartment_store%7Cdoctor%7Celectrician%7Celectronics_store%7Cembassy%7Cfinance%7Cfire_station%7Cflorist%7Cfood%7Cfuneral_home%7Cfurniture_store%7Cgas_station%7Cgrocery_or_supermarket%7Cgym%7Chair_care%7Chardware_store%7Chealth%7Chindu_temple%7Chome_goods_store%7Chospital%7Cinsurance_agency%7Cjewelry_store%7Claundry%7Clawyer%7Clibrary%7Cliquor_store%7Clodging%7Cmeal_delivery%7Cmosque%7Cmovie_rental%7Cmovie_theater%7Cmoving_company%7Cmuseum%7Cnight_club%7Cpark%7Cpet_store%7Cpharmacy%7Cphysiotherapist%7Cpolice%7Cpost_office%7Crestaurant%7Crv_park%7Cschool%7Cshoe_store%7Cshopping_mall%7Cspa%7Cstadium%7Cstore%7Csubway_station%7Ctaxi_stand%7Ctrain_station%7Cuniversity%7Czoo";
		String url2="https://maps.googleapis.com/maps/api/place/search/json?location="+lat+","+lng+""+"&radius=500&language=ja&sensor=false&key=AIzaSyBnfkXkSQXxxLd7foZ7FK9c13GtoGO23dY"+types;
		String j=null;
		HttpGet httpGet = new HttpGet(url2);
		HashMap<String,List<String>> map = new HashMap<String,List<String>>();
		try {
			HttpResponse response = client.execute(httpGet);
			
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				j=EntityUtils.toString(entity, "UTF-8"); 

//				}
				List<String> listd= new ArrayList<String>();
				
				
				Gson gson = new Gson();
				try {
				    JSONObject jsonObject = new JSONObject(j);
				    JSONArray jsonarray = jsonObject.getJSONArray("results");
				    for(int i = 0; i < jsonarray.length(); i++) {
				    	List<String> listd2= new ArrayList<String>();
				    	 JSONObject data = jsonarray.getJSONObject(i);
				    	if(data.has("name")){
				    		//listd.add(data.getString("name"));
				    	}
				    	JSONArray jsonarray2=data.getJSONArray("types");
				    	for(int c=0;c<jsonarray2.length();c++){
				    		listd2.add(jsonarray2.getString(c));
				    	}
				    	map.put(data.getString("name"), listd2);

				    }

				} catch (JSONException e) {
				    e.printStackTrace();
				}
				
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		model.addAttribute("placeinfo", map);
		
		//get gps
		
//		HttpURLConnection con = null;
//		StringBuilder builder = new StringBuilder();
//		HttpClient client = new DefaultHttpClient();
//		List<Itemlatlng> itemlatlng= itemService.findlatlng();
//		for(int a=0;a<itemlatlng.size();a++){
//			System.out.println(itemlatlng.get(a).getItem_lat());
//		}
//		System.out.println(itemlatlng.size());
//		String types="&types=accounting%7Cairport%7Camusement_park%7Caquarium%7Cart_gallery%7Cbakery%7Cbank%7Cbar%7Cbeauty_salon%7Cbicycle_store%7Cbook_store%7Cbowling_alley%7Cbus_station%7Ccafe%7Ccampground%7Ccar_dealer%7cemetery%7Cchurch%7Ccity_hall%7Cclothing_store%7Cconvenience_store%7Ccourthouse%7Cdentist%7Cdepartment_store%7Cdoctor%7Celectrician%7Celectronics_store%7Cembassy%7Cfinance%7Cfire_station%7Cflorist%7Cfood%7Cfuneral_home%7Cfurniture_store%7Cgas_station%7Cgrocery_or_supermarket%7Cgym%7Chair_care%7Chardware_store%7Chealth%7Chindu_temple%7Chome_goods_store%7Chospital%7Cinsurance_agency%7Cjewelry_store%7Claundry%7Clawyer%7Clibrary%7Cliquor_store%7Clodging%7Cmeal_delivery%7Cmosque%7Cmovie_rental%7Cmovie_theater%7Cmoving_company%7Cmuseum%7Cnight_club%7Cpark%7Cpet_store%7Cpharmacy%7Cphysiotherapist%7Cpolice%7Cpost_office%7Crestaurant%7Crv_park%7Cschool%7Cshoe_store%7Cshopping_mall%7Cspa%7Cstadium%7Cstore%7Csubway_station%7Ctaxi_stand%7Ctrain_station%7Cuniversity%7Czoo";
//		String url2="https://maps.googleapis.com/maps/api/place/search/json?location="+lat+","+lng+""+"&radius=500&language=ja&sensor=false&key=AIzaSyBnfkXkSQXxxLd7foZ7FK9c13GtoGO23dY"+types;
//		String j=null;
//		for(int a=0;a<itemlatlng.size();a++){
//			
//		url2="https://maps.googleapis.com/maps/api/place/search/json?location="+itemlatlng.get(a).getItem_lat()+","+itemlatlng.get(a).getItem_lng()+"&radius=30&language=ja&sensor=false&key=AIzaSyBnfkXkSQXxxLd7foZ7FK9c13GtoGO23dY"+types;
//			
//			
//			HttpGet httpGet = new HttpGet(url2);
//		
//		
//		
//		try {
//			HttpResponse response = client.execute(httpGet);
//			
//			int statusCode = response.getStatusLine().getStatusCode();
//			if (statusCode == 200) {
//				HttpEntity entity = response.getEntity();
//				j=EntityUtils.toString(entity, "UTF-8"); 
//				
////				InputStream content = entity.getContent();
////				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
////				String line;
////				while ((line = reader.readLine()) != null) {
////					builder.append(line);
////				}
//				List<String> listd= new ArrayList<String>();
//				
//				HashMap<String,List<String>> map = new HashMap<String,List<String>>();
//				Gson gson = new Gson();
//				try {
//				    JSONObject jsonObject = new JSONObject(j);
//				    JSONArray jsonarray = jsonObject.getJSONArray("results");
//				    for(int i = 0; i < jsonarray.length(); i++) {
//				    	List<String> listd2= new ArrayList<String>();
//				    	 JSONObject data = jsonarray.getJSONObject(i);
//				    	if(data.has("name")){
//				    		listd.add(data.getString("name"));
//				    	}
//				    	JSONArray jsonarray2=data.getJSONArray("types");
//				    	for(int c=0;c<jsonarray2.length();c++){
//				    		listd2.add(jsonarray2.getString(c));
//				    	}
//				    	map.put(data.getString("name"), listd2);
////				    	map.put(data.getString("name"),listd);
//				    	
//				    }
//				    itemService.insertplace(itemlatlng.get(a).getId(),listd.get(0),map.get(listd.get(0)));
////				    for(int i=0;i<listd.size();i++){
////				    System.out.println(listd.get(i));
////				    }
//				  
//				} catch (JSONException e) {
//				    e.printStackTrace();
//				}
//				System.out.println(listd.size());
//			}
//			
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		}
//		List<String> listd= new ArrayList<String>();
//		Gson gson = new Gson();
//		//Map<String, List<String>> map = new HashMap<String, List<String>>();
//		System.out.println(gson.toJson(j));
//		
//		try {
//		    JSONObject jsonObject = new JSONObject(j);
//		    JSONArray jsonarray = jsonObject.getJSONArray("results");
//		    for(int i = 0; i < jsonarray.length(); i++) {
//		    	 JSONObject data = jsonarray.getJSONObject(i);
//		    	if(data.has("name")){
//		    		listd.add(data.getString("name"));
//		    	}
//		    	JSONArray jsonarray2=data.getJSONArray("types");
//		    	for(int c=0;c<jsonarray2.length();c++){
////		    		listd.add(jsonarray2.getString(c));
//		    	}
////		    	map.put(data.getString("name"),listd);
//		    	
//		    }
////		    for(int i=0;i<listd.size();i++){
////		    System.out.println(listd.get(i));
////		    }
//		  
//		} catch (JSONException e) {
//		    e.printStackTrace();
//		}
//		model.addAttribute("placeinfo", listd);
//	
////		List list = gson.fromJson(gson.toJson(j), List.class);
//		System.out.println(listd.size());
		
		
		
		
		
		return "item/add";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String create(@ModelAttribute("item") ItemEditForm form,
			BindingResult result, ModelMap model) throws org.json.JSONException {
		new ItemEditFormValidator().validate(form, result);
		if (result.hasErrors()) {
			String lat="";
			String lng="";
			return this.add(form, model,lat,lng);
		}
		try {
			Item item = this.itemService.createByForm(form);

			Users user = userService.getById(SecurityUserHolder
					.getCurrentUser().getId());

			HashMap<String, String[]> params = new HashMap<String, String[]>();
			C2DMessage c2dmessage = new C2DMessage();
			c2dmessage.setCollapse(Constants.COLLAPSE_KEY_SYNC);
			c2dmessage.setIsDelayIdle(new Integer(0));
			try {
				c2dmessage.setParams(SerializeUtil.serialize(params));
			} catch (Exception e) {

			}
			this.c2dmMessageService.addMessage(c2dmessage, user);

			// ■wakebe ULLO登録による経験値取得 (値要修正)
			userService.addExperiencePoint(user.getId(), 10);

			KALens(form);
			// try{
			// Thread.sleep(3000);
			// }catch(InterruptedException e){}

			return "redirect:/item/" + item.getId()
					+ "/related?fromcreated=true";
		} catch (IOException ex) {
			logger.error("Error occurred when create item", ex);
		}

		return "redirect:/item";
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable String id,
			@ModelAttribute("form") ItemEditForm form, ModelMap model) {
		Item item = itemService.findById(id);
		if (item == null
				|| (!SecurityUserHolder.getCurrentUser().getId()
						.equals(item.getAuthor().getId()) && !SecurityUserHolder
						.getCurrentUser().getAuth()
						.equals(Users.UsersAuth.ADMIN))) {
			return "redirect:/item";
		}
		form = new ItemEditForm(item);

		List<ItemTitle> titles = item.getTitles();
		HashMap<String, String> titleMap = new HashMap<String, String>();
		for (ItemTitle title : titles) {
			titleMap.put(title.getLanguage().getCode(), title.getContent());
		}
		form.setTitleMap(titleMap);

		model.addAttribute("item", item);
		model.addAttribute("form", form);

		// FileType{video, image}
		FileData fileData = item.getImage();
		String fileType = "";
		if (fileData != null && !StringUtils.isBlank(fileData.getOrigName())) {
			fileType = FilenameUtil.checkMediaType(fileData.getOrigName());
		}
		model.addAttribute("fileType", fileType);

		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		model.addAttribute("user", user);

		List<Language> langs = new ArrayList<Language>();
		for (ItemTitle t : item.getTitles()) {
			if (!langs.contains(t))
				langs.add(t.getLanguage());
		}
		for (Language lang : user.getMyLangs()) {
			if (!langs.contains(lang))
				langs.add(lang);
		}
		for (Language lang : user.getStudyLangs()) {
			if (!langs.contains(lang))
				langs.add(lang);
		}
		Language english = languageService.findUniqueLangByCode("en");
		if (!langs.contains(english))
			langs.add(english);
		model.addAttribute("langs", langs);

		if (item.getCategory() != null)
			form.setCategoryId(item.getCategory().getId());
		return "item/edit";
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
	public String update(@PathVariable String id,
			@ModelAttribute("form") ItemEditForm form, BindingResult result,
			ModelMap model) {
		Item item = itemService.findById(id);
		if (item == null
				|| (!SecurityUserHolder.getCurrentUser().getId()
						.equals(item.getAuthor().getId()) && !SecurityUserHolder
						.getCurrentUser().getAuth()
						.equals(Users.UsersAuth.ADMIN))) {
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

			HashMap<String, String[]> params = new HashMap<String, String[]>();
			C2DMessage c2dmessage = new C2DMessage();
			c2dmessage.setCollapse(Constants.COLLAPSE_KEY_SYNC);
			c2dmessage.setIsDelayIdle(new Integer(0));
			try {
				c2dmessage.setParams(SerializeUtil.serialize(params));
			} catch (Exception e) {

			}
			this.c2dmMessageService.addMessage(c2dmessage, user);

		} catch (IOException ex) {
			logger.error("Error occurred when create item", ex);
		}
		return "redirect:/item/" + id;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
	public String delete(@PathVariable String id) {
		Item item = itemService.findById(id);
		if (item == null
				|| (!SecurityUserHolder.getCurrentUser().getId()
						.equals(item.getAuthor().getId()) && !SecurityUserHolder
						.getCurrentUser().getAuth()
						.equals(Users.UsersAuth.ADMIN))) {
			return "redirect:/item";
		}
		this.itemService.delete(item);
		return "redirect:/item";
	}

	@RequestMapping(value = "/{id}/related", method = RequestMethod.GET)
	public String related(@PathVariable String id, String fromcreated,
			ModelMap model) {
		Item item = itemService.findById(id);
		if (item == null) {
			return "redirect:/item";
		}
		model.addAttribute("item", item);
		model.addAttribute("relatedItemList",
				itemService.searchRelatedItemList(id));
		model.addAttribute("latestItems",
				itemService.searchLatestItems(item.getId(), null, 5));
		if (item.getItemLat() != null && item.getItemLng() != null) {
			model.addAttribute(
					"nearestItems",
					itemService.searchNearestItems(item.getId(),
							item.getItemLat(), item.getItemLng(), 1, null, 5));
		}
		if (!StringUtils.isBlank(fromcreated)) {
			model.addAttribute("fromcreated", true);
		} else {
			model.addAttribute("fromcreated", false);
		}

		// FileType{video, image}
		FileData fileData = item.getImage();
		String fileType = "";
		if (fileData != null && !StringUtils.isBlank(fileData.getOrigName())) {
			fileType = FilenameUtil.checkMediaType(fileData.getOrigName());
		}
		model.addAttribute("fileType", fileType);

		// Network Model
		model.addAttribute("recommenditem", ListRecommend);
		model.addAttribute("recommenditem2", ListRecommend2);
		Usertest userid = userService.findById(SecurityUserHolder
				.getCurrentUser().getId());
		model.addAttribute("userid",userid.getId());
		return "item/related";
	}

	@RequestMapping(value = "/{id}/comment", method = RequestMethod.POST)
	public String submitComment(@PathVariable String id,
			@ModelAttribute ItemCommentForm form) {
		this.itemService.createCommentByForm(id, form);
		return "redirect:/item/" + id;
	}

	@RequestMapping(value = "/{id}/question", method = RequestMethod.POST)
	public String submitQuestion(@PathVariable String id,
			HttpServletRequest request) {
		String content = request.getParameter("content");
		this.itemService.createAnswerByForm(id, content);
		return "redirect:/item/" + id;
	}

	@RequestMapping(value = "/moretoanswer", method = RequestMethod.GET)
	public String toAnswerMore(
			@ModelAttribute("searchCond") ItemSearchCondForm form,
			ModelMap model) {
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
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
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
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		if (user == null) {
			return "redirect:/item/" + id;
		}
		if (!item.getAuthor().getId().equals(user.getId())
				|| item.getQuestion() == null
				|| item.getQuestion().getAnswerSet() == null
				|| item.getQuestion().getAnswerSet().size() == 0) {
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
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		// if (user == null || user.getAuth() != Users.UsersAuth.ADMIN) {
		// return "redirect:/item/" + id;
		// }
		itemService.teacherConfirm(item, user);
		return "redirect:/item/" + id;
	}

	@RequestMapping(value = "/{id}/teacherreject", method = RequestMethod.POST)
	public String teacherReject(@PathVariable String id) {
		Item item = itemService.findById(id);
		if (item == null) {
			return "redirect:/item/" + id;
		}
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		itemService.teacherReject(item, user);
		return "redirect:/item/" + id;
	}

	@RequestMapping(value = "/{id}/teacherdelcfmstatus", method = RequestMethod.POST)
	public String teacherDeleteStatus(@PathVariable String id) {
		Item item = itemService.findById(id);
		if (item == null) {
			return "redirect:/item/" + id;
		}
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		itemService.teacherDeleteStatus(item, user);
		return "redirect:/item/" + id;
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(String id, Double latitude, Double longitude,
			Float speed, ModelMap model) {
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		model.clear();
		Item item = null;
		if (id != null)
			item = this.itemService.findById(id);
		try {
			if (item != null && item.getId() != null) {
				this.logService.logUserReadItem(item, user, latitude,
						longitude, speed);
			}
		} catch (ObjectNotFoundException e) {

		}
		return null;
	}

	//KA Lens ************************************************************************************************************************************************************************************
	private void KALens(ItemEditForm form) {
		int nodecount = 0;
		// UserID取得
		Usertest userid = userService.findById(SecurityUserHolder
				.getCurrentUser().getId());
		Kasetting ka = userService.getka(SecurityUserHolder.getCurrentUser()
				.getId());

		// KALens network graph
		// create*************************************************************************************************************//
		ProjectController pc = Lookup.getDefault().lookup(
				ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();

		// import file//
		ImportController importController = Lookup.getDefault().lookup(
				ImportController.class);
		Container container = null;

		GraphModel graphModel = Lookup.getDefault()
				.lookup(GraphController.class).getModel();
		GraphModel graphModel2 = Lookup.getDefault()
				.lookup(GraphController.class).getModel();
		AttributeModel attributeModel = Lookup.getDefault()
				.lookup(AttributeController.class).getModel();
		// Add Node column
		AttributeColumn Hyper = attributeModel.getNodeTable().addColumn(
				"Authorid", AttributeType.STRING);
		AttributeColumn node_tagerItem = attributeModel.getNodeTable()
				.addColumn("Targetitem", AttributeType.STRING);
		AttributeColumn node_tagerItemid = attributeModel.getNodeTable()
				.addColumn("Targetitemid", AttributeType.STRING);
		AttributeColumn node_NextItem = attributeModel.getNodeTable()
				.addColumn("Nextitem", AttributeType.STRING);
		AttributeColumn node_NextItemid = attributeModel.getNodeTable()
				.addColumn("Nextitemid", AttributeType.STRING);
		AttributeColumn target_author_name = attributeModel.getNodeTable()
				.addColumn("TAname", AttributeType.STRING);
		AttributeColumn next_author_name = attributeModel.getNodeTable()
				.addColumn("NAname", AttributeType.STRING);
		AttributeColumn knowledgerank = attributeModel.getNodeTable()
				.addColumn("KnowledgeRanking", AttributeType.STRING);
		AttributeColumn knowledgenumber = attributeModel.getNodeTable()
				.addColumn("Knowledgenumber", AttributeType.STRING);
		AttributeColumn nickname = attributeModel.getNodeTable().addColumn(
				"nickname", AttributeType.STRING);
		AttributeColumn userlevel = attributeModel.getNodeTable().addColumn(
				"userlevel", AttributeType.STRING);
		AttributeColumn checknumber = attributeModel.getNodeTable().addColumn(
				"checknumber", AttributeType.STRING);
		AttributeColumn createtime = attributeModel.getNodeTable().addColumn(
				"createtime", AttributeType.STRING);
		// degrees = attributeModel.getNodeTable().addColumn("degree", "Degree",
		// AttributeType.INT, AttributeOrigin.COMPUTED, 0);
		GraphController gc = Lookup.getDefault().lookup(GraphController.class);
		GraphController gc2 = Lookup.getDefault().lookup(GraphController.class);
		graphModel = gc.getModel();
		graphModel2 = gc2.getModel();
		Graph graph = gc.getModel().getDirectedGraph();
		Graph graph2 = gc2.getModel().getDirectedGraph();
		Node[] n = new Node[10000];
		Node[] n2 = new Node[10000];
		Node[] n3 = new Node[10000];
		List<Users> user = userService.findBynetworkuser();
		ArrayList<Cooccurrence> Currence = new ArrayList<Cooccurrence>();
		List<Cooccurrence> coocurrence = new ArrayList<Cooccurrence>();
		// user ranking and knowledge
		List<KnowledgeRanking> knowledgeranking = userService
				.findknowledgeranking();
		
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
//		String datedata = sdf.format(element.getCreateTime());

		for (int u = 0; u < user.size(); u++) {
			// abc userを除く
			if (user.get(u).getId().equals("16bb9432301b850401301bf6abcc0308")) {

			} else {
				coocurrence = itemservice.cooccurrence(user.get(u).getId());
				int j = 1;

				// 共起検索************************************************************************************************************************************//
				for (int i = 0; i < coocurrence.size(); i++) {

					if (coocurrence.size() > j) {
						Cooccurrence currenceentity = new Cooccurrence();
						currenceentity.setTarget_content(coocurrence.get(i)
								.getContent());
						currenceentity.setNext_content(coocurrence.get(j)
								.getContent());
						currenceentity.setTarget_itemid(coocurrence.get(i)
								.getItemid());
						currenceentity.setNext_itemid(coocurrence.get(j)
								.getItemid());
						currenceentity.setLanguage(coocurrence.get(i)
								.getLanguage());
						currenceentity.setTarget_authorid(coocurrence.get(i)
								.getAuthorid());
						currenceentity.setNext_authorid(coocurrence.get(j)
								.getAuthorid());

						currenceentity.setNickname(coocurrence.get(i)
								.getNickname());
						currenceentity.setUser_level(coocurrence.get(i)
								.getUser_level());
						currenceentity.setItemcreate(coocurrence.get(i).getItemcreate());
						// for(int
						// knowledgecount=0;knowledgecount<knowledgeranking.size();knowledgecount++){
						//
						// }
						// System.out.println(coocurrence.get(i).getContent() +
						// "->"
						// + coocurrence.get(j).getContent());
						Currence.add(currenceentity);

						j++;
					}
				}

			}
		}

		for (int i3 = 0; i3 < Currence.size(); i3++) {
			for (int knowledgecount = 0; knowledgecount < knowledgeranking
					.size(); knowledgecount++) {
				if (Currence.get(i3).getTarget_authorid() != null
						&& knowledgeranking.get(knowledgecount).getAuthor_id() != null) {
					if (Currence
							.get(i3)
							.getTarget_authorid()
							.equals(knowledgeranking.get(knowledgecount)
									.getAuthor_id())) {
						Cooccurrence currenceentity = new Cooccurrence();
						currenceentity.setTarget_content(Currence.get(i3)
								.getTarget_content());
						currenceentity.setNext_content(Currence.get(i3)
								.getNext_content());
						currenceentity.setTarget_itemid(Currence.get(i3)
								.getTarget_itemid());
						currenceentity.setNext_itemid(Currence.get(i3)
								.getNext_itemid());
						currenceentity.setLanguage(Currence.get(i3)
								.getLanguage());
						currenceentity.setTarget_authorid(Currence.get(i3)
								.getTarget_authorid());
						currenceentity.setNext_authorid(Currence.get(i3)
								.getNext_authorid());
						currenceentity.setKnowledgeranking(knowledgeranking
								.get(knowledgecount).getRanking());
						currenceentity.setKnowledgnumber(knowledgeranking.get(
								knowledgecount).getN());

						currenceentity.setNickname(Currence.get(i3)
								.getNickname());
						currenceentity.setUser_level(Currence.get(i3)
								.getUser_level());
						currenceentity.setItemcreate(Currence.get(i3).getItemcreate());
						
						Currence.set(i3, currenceentity);
					}
				}
			}

		}

		// Knowledge
		int Yrange = 0;
		int Ycount = 0;
		int Y = 500;
		int Y2 = 500;
		// 共起ネットワーク生成************************************************************************************************************************************//
		// learners 1
		for (int i = 0; i < Currence.size(); i++) {
			// form.getTitleMap().get("en")
			if (Currence.get(i).getTarget_content()
					.equalsIgnoreCase(form.getTitleMap().get("en"))) {
				n[nodecount] = graphModel.factory().newNode(
						String.valueOf(nodecount));
				n[nodecount].getNodeData().setLabel(
						Currence.get(i).getNickname());
				// Bule
				if (Currence.get(i).getKnowledgeranking() == null
						|| Currence.get(i).getKnowledgeranking().equals("null")) {
					n[nodecount].getNodeData().setColor(0.0f, 0.0f, 1.0f);
					n[nodecount].getNodeData().setSize(18f);
				} else if (Integer.parseInt(Currence.get(i)
						.getKnowledgeranking()) <= 5) {
					n[nodecount].getNodeData().setColor(0.0f, 1.0f, 0.0f);
					n[nodecount].getNodeData().setSize(25f);
				} else {
					n[nodecount].getNodeData().setColor(0.0f, 0.0f, 1.0f);
					n[nodecount].getNodeData().setSize(18f);
				}

				n[nodecount].getNodeData().setX(-300);

				Ycount++;
				if ((Ycount % 2) == 0) {
					Y = Y + 100;
					n[nodecount].getNodeData().setY(Y);

				} else {
					Y = Y + 100;
					n[nodecount].getNodeData().setY(Y);
				}
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(Hyper.getIndex(),
								Currence.get(i).getTarget_authorid());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(node_tagerItem.getIndex(),
								Currence.get(i).getTarget_content());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(node_tagerItemid.getIndex(),
								Currence.get(i).getTarget_itemid());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(node_NextItem.getIndex(),
								Currence.get(i).getNext_content());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(node_NextItemid.getIndex(),
								Currence.get(i).getNext_itemid());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(knowledgerank.getIndex(),
								Currence.get(i).getKnowledgeranking());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(knowledgenumber.getIndex(),
								Currence.get(i).getKnowledgnumber());

				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(nickname.getIndex(),
								Currence.get(i).getNickname());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(userlevel.getIndex(),
								Currence.get(i).getUser_level());
				String datedata = sdf.format(Currence.get(i).getItemcreate());
				n[nodecount].getNodeData().getAttributes().setValue(createtime.getIndex(), datedata);
				n[nodecount].getNodeData().getAttributes().setValue(checknumber.getIndex(),"1");
				graph.addNode(n[nodecount]);
//				System.out.println("First:" + nodecount);
				nodecount++;

			}

		}

		// knowledge1 generate
		String targetword = "";
		for (Node no : graph.getNodes().toArray()) {
			targetword = String.valueOf(no.getNodeData().getAttributes()
					.getValue(node_tagerItem.getIndex()));
		}
		n[nodecount] = graphModel.factory().newNode(String.valueOf(nodecount));
		n[nodecount].getNodeData().setLabel(targetword);
		// Yellow
		n[nodecount].getNodeData().setColor(1.0f, 1.0f, 0.0f);
		n[nodecount].getNodeData().setSize(20f);
		n[nodecount].getNodeData().setX(100);
		n[nodecount].getNodeData().setY(500);
		n[nodecount].getNodeData().getAttributes().setValue(checknumber.getIndex(),"2");
		graph.addNode(n[nodecount]);

		// learners1 to knowledges1
		for (int i = 0; i < nodecount; i++) {
			Edge e1 = graphModel.factory().newEdge(
					KeyGenerateUtil.generateIdUUID(), n[i], n[nodecount], 1f,
					true);
//			System.out.println("Second" + nodecount);
			graph.addEdge(e1);
		}

		int knowledge1 = 0;
		// knowledge1 最終ID
		knowledge1 = nodecount;
//		System.out.println("Third:" + knowledge1);
		nodecount++;

		Yrange = 0;
		Ycount = 0;
		Y = 500;
		Y2 = 500;
		// knowledges2 generate
		for (int i = 0; i < Currence.size(); i++) {
			// form.getTitleMap().get("en")
			if (Currence.get(i).getTarget_content()
					.equalsIgnoreCase(form.getTitleMap().get("en"))) {
				n[nodecount] = graphModel.factory().newNode(
						String.valueOf(nodecount));
				n[nodecount].getNodeData().setLabel(
						Currence.get(i).getNext_content());
				n[nodecount].getNodeData().setColor(1.0f, 1.0f, 0.0f);
				n[nodecount].getNodeData().setSize(20f);
				n[nodecount].getNodeData().setX(600);
				Ycount++;
				if ((Ycount % 2) == 0) {
					Y = Y + 100;
					n[nodecount].getNodeData().setY(Y);

				} else {
					Y2 = Y2 - 100;
					n[nodecount].getNodeData().setY(Y2);
				}

				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(Hyper.getIndex(),
								Currence.get(i).getTarget_authorid());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(node_tagerItem.getIndex(),
								Currence.get(i).getTarget_content());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(node_tagerItemid.getIndex(),
								Currence.get(i).getTarget_itemid());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(node_NextItem.getIndex(),
								Currence.get(i).getNext_content());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(node_NextItemid.getIndex(),
								Currence.get(i).getNext_itemid());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(knowledgerank.getIndex(),
								Currence.get(i).getKnowledgeranking());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(knowledgenumber.getIndex(),
								Currence.get(i).getKnowledgnumber());

				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(nickname.getIndex(),
								Currence.get(i).getNickname());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(userlevel.getIndex(),
								Currence.get(i).getUser_level());
				String datedata = sdf.format(Currence.get(i).getItemcreate());
				n[nodecount].getNodeData().getAttributes().setValue(createtime.getIndex(), datedata);
				n[nodecount].getNodeData().getAttributes().setValue(checknumber.getIndex(),"2");

				graph.addNode(n[nodecount]);
				nodecount++;
//				System.out.println("Fourth:" + nodecount);

			}
		}
		// knowledge1 to knowledge2
		// knowledge2 最終ID
		int knowledge1stop = nodecount - 1;
		for (int i = nodecount - 1; i > knowledge1; i--) {
//			System.out.println("Final:" + i + "more" + nodecount + "know"
//					+ knowledge1);
			Edge e1 = graphModel.factory().newEdge(
					KeyGenerateUtil.generateIdUUID(), n[knowledge1], n[i], 1f,
					true);

			graph.addEdge(e1);
		}

		int c = 0;
		for (int i = 0; i < Currence.size(); i++) {
			// form.getTitleMap().get("en")
			if (Currence.get(i).getTarget_content()
					.equalsIgnoreCase(form.getTitleMap().get("en"))) {
				n2[c] = graphModel.factory().newNode(String.valueOf(nodecount));
				n2[c].getNodeData().setLabel(Currence.get(i).getNext_content());
				n2[c].getNodeData().setColor(0.3f, 0.2f, 0.5f);
				n2[c].getNodeData().setSize(20f);
				n2[c].getNodeData().setX(600);
				n2[c].getNodeData().setY(Yrange);
				n2[c].getNodeData()
						.getAttributes()
						.setValue(Hyper.getIndex(),
								Currence.get(i).getTarget_authorid());
				n2[c].getNodeData()
						.getAttributes()
						.setValue(node_tagerItem.getIndex(),
								Currence.get(i).getTarget_content());
				n2[c].getNodeData()
						.getAttributes()
						.setValue(node_tagerItemid.getIndex(),
								Currence.get(i).getTarget_itemid());
				n2[c].getNodeData()
						.getAttributes()
						.setValue(node_NextItem.getIndex(),
								Currence.get(i).getNext_content());
				n2[c].getNodeData()
						.getAttributes()
						.setValue(node_NextItemid.getIndex(),
								Currence.get(i).getNext_itemid());
				n2[c].getNodeData()
						.getAttributes()
						.setValue(knowledgerank.getIndex(),
								Currence.get(i).getKnowledgeranking());
				n2[c].getNodeData()
						.getAttributes()
						.setValue(knowledgenumber.getIndex(),
								Currence.get(i).getKnowledgnumber());

				n2[c].getNodeData()
						.getAttributes()
						.setValue(nickname.getIndex(),
								Currence.get(i).getNickname());
				n2[c].getNodeData()
						.getAttributes()
						.setValue(userlevel.getIndex(),
								Currence.get(i).getUser_level());
				Yrange = Yrange + 300;
				c++;
			}
		}

		Yrange = 0;
		Ycount = 0;
		Y = 500;
		Y2 = 500;
		// knowledge3 generate
		int knowledge2 = nodecount;
		for (int i = 0; i < n2.length; i++) {
			if (n2[i] == null) {
				break;
			} else {
				for (int j = 0; j < Currence.size(); j++) {
					if (n2[i].getNodeData().getAttributes()
							.getValue("Nextitem")
							.equals(Currence.get(j).getTarget_content())) {
						n[nodecount] = graphModel.factory().newNode(
								String.valueOf(nodecount));
						n[nodecount].getNodeData().setLabel(
								Currence.get(j).getNext_content());
						n[nodecount].getNodeData().setColor(1.0f, 1.0f, 0.0f);
						n[nodecount].getNodeData().setSize(20f);
						n[nodecount].getNodeData().setX(1100);
						Ycount++;
						if ((Ycount % 2) == 0) {
							Y = Y + 100;
							n[nodecount].getNodeData().setY(Y);

						} else {
							Y2 = Y2 - 100;
							n[nodecount].getNodeData().setY(Y2);
						}
						n[nodecount]
								.getNodeData()
								.getAttributes()
								.setValue(Hyper.getIndex(),
										Currence.get(j).getTarget_authorid());
						n[nodecount]
								.getNodeData()
								.getAttributes()
								.setValue(node_tagerItem.getIndex(),
										Currence.get(j).getTarget_content());
						n[nodecount]
								.getNodeData()
								.getAttributes()
								.setValue(node_tagerItemid.getIndex(),
										Currence.get(j).getTarget_itemid());
						n[nodecount]
								.getNodeData()
								.getAttributes()
								.setValue(node_NextItem.getIndex(),
										Currence.get(j).getNext_content());
						n[nodecount]
								.getNodeData()
								.getAttributes()
								.setValue(node_NextItemid.getIndex(),
										Currence.get(j).getNext_itemid());
						n[nodecount]
								.getNodeData()
								.getAttributes()
								.setValue(knowledgerank.getIndex(),
										Currence.get(j).getKnowledgeranking());
						n[nodecount]
								.getNodeData()
								.getAttributes()
								.setValue(knowledgenumber.getIndex(),
										Currence.get(j).getKnowledgnumber());

						n[nodecount]
								.getNodeData()
								.getAttributes()
								.setValue(nickname.getIndex(),
										Currence.get(j).getNickname());
						n[nodecount]
								.getNodeData()
								.getAttributes()
								.setValue(userlevel.getIndex(),
										Currence.get(j).getUser_level());
						String datedata = sdf.format(Currence.get(j).getItemcreate());
						n[nodecount].getNodeData().getAttributes().setValue(createtime.getIndex(), datedata);
						n[nodecount].getNodeData().getAttributes().setValue(checknumber.getIndex(),"2");

						graph.addNode(n[nodecount]);
						nodecount++;
						Yrange = Yrange + 100;
						Yrange = Yrange / 2;

					}
				}

			}
		}
//		System.out.println("knowledge2:" + knowledge2 + "nodecount" + nodecount
//				+ "knowledge1" + knowledge1);
		// knowledge 3 最終ID
		int knowledge2stop = nodecount - 1;
		// knowledge2 to knowledge3
		for (int i = knowledge2; i <= nodecount - 1; i++) {
			for (int j = knowledge1; j < knowledge1stop; j++) {
//				System.out.println("nextitem:"
//						+ n[j].getNodeData().getAttributes()
//								.getValue(node_NextItem.getIndex())
//						+ "  targetitem:"
//						+ n[i].getNodeData().getAttributes()
//								.getValue("Targetitem"));
//				System.out.println("nextitem:" + n[j].getNodeData().getLabel());

				if (n[j].getNodeData()
						.getLabel()
						.equals(n[i].getNodeData().getAttributes()
								.getValue("Targetitem"))) {
					Edge e1 = graphModel.factory().newEdge(
							KeyGenerateUtil.generateIdUUID(), n[j], n[i], 1f,
							true);

					graph.addEdge(e1);
				}

			}

		}

		int Y3 = 550;
		// learner2 to knowledge2
		for (Node no : graph.getNodes().toArray()) {

			// System.out.println("NodeID:"+Integer.parseInt(no.getNodeData().getId())+" Knowledge1:"+knowledge1+"knowledgestop1:"+knowledge1stop);
			if (Integer.parseInt(no.getNodeData().getId()) > knowledge1
					&& Integer.parseInt(no.getNodeData().getId()) <= knowledge1stop) {
				n[nodecount] = graphModel.factory().newNode(
						String.valueOf(nodecount));
				n[nodecount].getNodeData().setLabel(
						String.valueOf(no.getNodeData().getAttributes()
								.getValue("nickname")));
				n[nodecount].getNodeData().setColor(0.0f, 0.0f, 1.0f);

				if (no.getNodeData().getAttributes()
						.getValue("KnowledgeRanking") == null
						|| no.getNodeData().getAttributes()
								.getValue("KnowledgeRanking").equals("null")) {
					n[nodecount].getNodeData().setSize(17f);
//					System.out.println("Size:17");
				} else if (Integer.parseInt(String.valueOf(no.getNodeData()
						.getAttributes().getValue("KnowledgeRanking"))) <= 5) {
					n[nodecount].getNodeData().setSize(25f);
					n[nodecount].getNodeData().setColor(0.0f, 1.0f, 0.0f);
//					System.out.println("Size:25");
				} else {
					n[nodecount].getNodeData().setSize(17f);
				}
				n[nodecount].getNodeData().setX(250);
				n[nodecount].getNodeData().setY(Y3);
				Y3 = Y3 + 100;

				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								Hyper.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("Authorid")));
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								node_tagerItem.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("Targetitem")));
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								node_tagerItemid.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("Targetitemid")));
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								node_NextItem.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("Nextitem")));
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								node_NextItemid.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("Nextitemid")));
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								knowledgerank.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("KnowledgeRanking")));
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								knowledgenumber.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("Knowledgenumber")));

				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								nickname.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("nickname")));
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								userlevel.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("userlevel")));
			
				n[nodecount].getNodeData().getAttributes().setValue(createtime.getIndex(), String.valueOf(no.getNodeData().getAttributes().getValue("createtime")));
				n[nodecount].getNodeData().getAttributes().setValue(checknumber.getIndex(),"1");

				graph.addNode(n[nodecount]);

				Edge e1 = graphModel.factory()
						.newEdge(KeyGenerateUtil.generateIdUUID(),
								n[nodecount],
								n[Integer.parseInt(no.getNodeData().getId())],
								1f, true);

				graph.addEdge(e1);
				nodecount++;
			}
		}
		Y3 = 550;
		Set<String> set = new HashSet<String>();
		// learner3 to knowledge3
		for (Node no : graph.getNodes().toArray()) {
			if (Integer.parseInt(no.getNodeData().getId()) > knowledge2
					&& Integer.parseInt(no.getNodeData().getId()) <= knowledge2stop) {
				
					set.add(String.valueOf(no.getNodeData().getAttributes()
							.getValue("nickname")));
					n[nodecount] = graphModel.factory().newNode(
							String.valueOf(nodecount));
					n[nodecount].getNodeData().setLabel(
							String.valueOf(no.getNodeData().getAttributes()
									.getValue("nickname")));
					n[nodecount].getNodeData().setColor(0.0f, 0.0f, 1.0f);

					if (no.getNodeData().getAttributes()
							.getValue("KnowledgeRanking") == null
							|| no.getNodeData().getAttributes()
									.getValue("KnowledgeRanking")
									.equals("null")) {
						n[nodecount].getNodeData().setSize(17f);
//						System.out.println("Size:17");
					} else if (Integer.parseInt(String.valueOf(no.getNodeData()
							.getAttributes().getValue("KnowledgeRanking"))) <= 5) {
						n[nodecount].getNodeData().setSize(25f);
						n[nodecount].getNodeData().setColor(0.0f, 1.0f, 0.0f);
//						System.out.println("Size:25");
					} else {
						n[nodecount].getNodeData().setSize(17f);
					}
					n[nodecount].getNodeData().setX(850);
					n[nodecount].getNodeData().setY(Y3);
					Y3 = Y3 + 100;

					n[nodecount]
							.getNodeData()
							.getAttributes()
							.setValue(
									Hyper.getIndex(),
									String.valueOf(no.getNodeData()
											.getAttributes()
											.getValue("Authorid")));
					n[nodecount]
							.getNodeData()
							.getAttributes()
							.setValue(
									node_tagerItem.getIndex(),
									String.valueOf(no.getNodeData()
											.getAttributes()
											.getValue("Targetitem")));
					n[nodecount]
							.getNodeData()
							.getAttributes()
							.setValue(
									node_tagerItemid.getIndex(),
									String.valueOf(no.getNodeData()
											.getAttributes()
											.getValue("Targetitemid")));
					n[nodecount]
							.getNodeData()
							.getAttributes()
							.setValue(
									node_NextItem.getIndex(),
									String.valueOf(no.getNodeData()
											.getAttributes()
											.getValue("Nextitem")));
					n[nodecount]
							.getNodeData()
							.getAttributes()
							.setValue(
									node_NextItemid.getIndex(),
									String.valueOf(no.getNodeData()
											.getAttributes()
											.getValue("Nextitemid")));
					n[nodecount]
							.getNodeData()
							.getAttributes()
							.setValue(
									knowledgerank.getIndex(),
									String.valueOf(no.getNodeData()
											.getAttributes()
											.getValue("KnowledgeRanking")));
					n[nodecount]
							.getNodeData()
							.getAttributes()
							.setValue(
									knowledgenumber.getIndex(),
									String.valueOf(no.getNodeData()
											.getAttributes()
											.getValue("Knowledgenumber")));

					n[nodecount]
							.getNodeData()
							.getAttributes()
							.setValue(
									nickname.getIndex(),
									String.valueOf(no.getNodeData()
											.getAttributes()
											.getValue("nickname")));
					n[nodecount]
							.getNodeData()
							.getAttributes()
							.setValue(
									userlevel.getIndex(),
									String.valueOf(no.getNodeData()
											.getAttributes()
											.getValue("userlevel")));

				
					n[nodecount].getNodeData().getAttributes().setValue(createtime.getIndex(), String.valueOf(no.getNodeData().getAttributes().getValue("createtime")));
					n[nodecount].getNodeData().getAttributes().setValue(checknumber.getIndex(),"1");
					graph.addNode(n[nodecount]);

					Edge e1 = graphModel.factory().newEdge(
							KeyGenerateUtil.generateIdUUID(), n[nodecount],
							n[Integer.parseInt(no.getNodeData().getId())], 1f,
							true);

					graph.addEdge(e1);
					nodecount++;

				

			}
		}

		// System.out.println("Final:"+i+"more"+nodecount+"know"+knowledge1);

		// Edge e1 = graphModel.factory().newEdge(
		// KeyGenerateUtil.generateIdUUID(), n[knowledge2], n[i], 1f,
		// true);
		//
		// graph.addEdge(e1);

		// edge network generate
		// for (Node no : graph.getNodes().toArray()) {
		//
		// for (Node no2 : graph.getNodes().toArray()) {
		// if (no.getNodeData()
		// .getAttributes()
		// .getValue(Hyper.getIndex())
		// .equals(no2.getNodeData().getAttributes()
		// .getValue(Hyper.getIndex()))) {
		// if (no.getNodeData()
		// .getAttributes()
		// .getValue(node_NextItemid.getIndex())
		// .equals(no2.getNodeData().getAttributes()
		// .getValue(node_tagerItemid.getIndex()))) {
		// Edge e1 = graphModel.factory().newEdge(
		// KeyGenerateUtil.generateIdUUID(), no, no2, 1f,
		// true);
		// graph.addEdge(e1);
		// }
		// }
		// }
		// }
		//
		// // Connecting learners' own knowledge to next others knowledge
		// for (Node no : graph.getNodes().toArray()) {
		// for (Node no2 : graph.getNodes().toArray()) {
		// if (no.getNodeData()
		// .getAttributes()
		// .getValue(node_NextItem.getIndex())
		// .equals(no2.getNodeData().getAttributes()
		// .getValue(node_tagerItem.getIndex()))) {
		// Edge e1 = graphModel.factory()
		// .newEdge(KeyGenerateUtil.generateIdUUID(), no, no2,
		// 1f, true);
		// e1.getEdgeData().setColor(1.0f, 1.0f, 0.0f);
		// graph.addEdge(e1);
		// }
		//
		// }
		//
		// }

		GraphDistance distance = new GraphDistance();
		distance.setDirected(true);
		distance.execute(graphModel, attributeModel);

		AttributeColumn between = attributeModel.getNodeTable().getColumn(
				GraphDistance.BETWEENNESS);
		AttributeColumn closeness = attributeModel.getNodeTable().getColumn(
				GraphDistance.CLOSENESS);
		AttributeColumn degCol = attributeModel.getNodeTable().getColumn(
				"degree");
		AttributeColumn degColin = attributeModel.getNodeTable().getColumn(
				"degreein");
		AttributeColumn degColout = attributeModel.getNodeTable().getColumn(
				"degreeout");
		degCol = attributeModel.getNodeTable().addColumn("degree", "Degree",
				AttributeType.STRING, AttributeOrigin.COMPUTED, "0");
		degColin = attributeModel.getNodeTable().addColumn("degreein", "Degreein",
				AttributeType.STRING, AttributeOrigin.COMPUTED, "0");
		degColout = attributeModel.getNodeTable().addColumn("degreeout", "Degreeout",
				AttributeType.STRING, AttributeOrigin.COMPUTED, "0");
		// AttributeColumn de = attributeModel.getNodeTable().addColumn(
		// "Degree", AttributeType.STRING);
		graph = gc.getModel().getDirectedGraph();
		for (Node no : graph.getNodes().toArray()) {
			Double centrality = (Double) no.getNodeData().getAttributes()
					.getValue(between.getIndex());
//			System.out.println(centrality);
			no.getNodeData().getAttributes()
					.setValue(degCol.getIndex(), graph.getDegree(no));
			no.getNodeData().getAttributes().setValue(degColin.getIndex(),graph.getGraphModel().getDirectedGraph().getInDegree(no));
			no.getNodeData().getAttributes().setValue(degColout.getIndex(),graph.getGraphModel().getDirectedGraph().getOutDegree(no));
			// no.getNodeData().getAttributes().setValue(de.getIndex(),Degree.DEGREE);

		}
		// double[] t1 = new double[20];
		// double id = 0;
		// double id2 = 0;
		// for (Node no : graph.getNodes().toArray()) {
		// if (id < (Double) no.getNodeData().getAttributes()
		// .getValue(between.getIndex())) {
		// id = (Double) no.getNodeData().getAttributes()
		// .getValue(between.getIndex());
		// }
		// }
		// for (Node no : graph.getNodes().toArray()) {
		//
		// if ((Double) no.getNodeData().getAttributes()
		// .getValue(between.getIndex()) >= id) {
		// no.getNodeData().setColor(0.3f, 0.4f, 0.9f);
		// no.getNodeData().setSize(80f);
		// graph.addNode(no);
		// }
		// }
		// // 水色　近接中心性
		// for (Node no : graph.getNodes().toArray()) {
		// if (id2 < (Double) no.getNodeData().getAttributes()
		// .getValue(closeness.getIndex())) {
		// id2 = (Double) no.getNodeData().getAttributes()
		// .getValue(closeness.getIndex());
		// }
		// }
		// for (Node no : graph.getNodes().toArray()) {
		//
		// if ((Double) no.getNodeData().getAttributes()
		// .getValue(closeness.getIndex()) >= id2) {
		// no.getNodeData().setColor(0.2f, 0.9f, 0.9f);
		// no.getNodeData().setSize(80f);
		// graph.addNode(no);
		// }
		// }
		// Recommendation
		ListRecommend = new ArrayList<Cooccurrence>();
		ListRecommend2 = new ArrayList<String>();
		double degreechecker = 0;
		double index = 0;
		for(int i=knowledge1+1;i<=knowledge1stop;i++){
//		for (Node no : graph.getNodes().toArray()) {
			
				Cooccurrence Recomendation = new Cooccurrence();
				Recomendation.setLabel(String.valueOf(n[i].getNodeData().getAttributes().getValue(node_NextItem.getIndex())));
				Recomendation.setTarget_itemid(String.valueOf(n[i].getNodeData().getAttributes().getValue(node_NextItemid.getIndex())));
				Recomendation.setDegreein(String.valueOf(n[i].getNodeData().getAttributes().getValue(degColin.getIndex())));
				Recomendation.setDegreeout(String.valueOf(n[i].getNodeData().getAttributes().getValue(degColout.getIndex())));
//				System.out.println(String.valueOf(n[i].getNodeData().getAttributes().getValue(degColout.getIndex())));
				
				ListRecommend.add(Recomendation);
//			}
		}
//		 Set<String> set2 = new HashSet<String>();
		for (Node no : graph.getNodes().toArray()) {
			if (no.getNodeData().getAttributes()
					.getValue("KnowledgeRanking") == null
					|| no.getNodeData().getAttributes()
							.getValue("KnowledgeRanking")
							.equals("null")) {
			} else if (Integer.parseInt(String.valueOf(no.getNodeData()
					.getAttributes().getValue("KnowledgeRanking"))) <= 5) {
				
				if(ListRecommend2.contains(String.valueOf(no.getNodeData().getAttributes().getValue("nickname")))){
					
				}
				else{
//				Cooccurrence Recomendation = new Cooccurrence();
//				Recomendation.setNickname(String.valueOf(no.getNodeData().getAttributes().getValue("nickname")));
				ListRecommend2.add(String.valueOf(no.getNodeData().getAttributes().getValue("nickname")));
				}
//				Recomendation.setRanking(Integer.parseInt(String.valueOf(no.getNodeData()
//					.getAttributes().getValue("KnowledgeRanking"))));
			} else {
				
			}
		}
//			if (form.getTitleMap()
//					.get("en")
//					.equalsIgnoreCase(
//							String.valueOf(no.getNodeData().getAttributes()
//									.getValue(node_tagerItem.getIndex())))) {
//				for (Node no2 : graph.getNodes().toArray()) {
//					if (no.getNodeData()
//							.getAttributes()
//							.getValue(node_NextItem.getIndex())
//							.equals(no2.getNodeData().getAttributes()
//									.getValue(node_tagerItem.getIndex()))) {
//						Cooccurrence Recomendation = new Cooccurrence();
//						Recomendation.setLabel(String.valueOf(no2.getNodeData()
//								.getAttributes()
//								.getValue(node_tagerItem.getIndex())));
//						Recomendation.setDegree(String.valueOf(no2
//								.getNodeData().getAttributes()
//								.getValue(degCol.getIndex())));
//						Recomendation.setTarget_itemid(String.valueOf(no2
//								.getNodeData().getAttributes()
//								.getValue(node_tagerItemid.getIndex())));
//						System.out.println(String.valueOf(no2.getNodeData()
//								.getAttributes()
//								.getValue(node_tagerItem.getIndex()))
//								+ "///"
//								+ String.valueOf(no2.getNodeData()
//										.getAttributes()
//										.getValue(degCol.getIndex())));
//						ListRecommend.add(Recomendation);
//					}
//				}
//			}


		Collections.sort(ListRecommend, new Comparator<Cooccurrence>() {
			public int compare(Cooccurrence t1, Cooccurrence t2) {
				return +((int) Integer.valueOf(t2.getDegreeout()) - Integer
						.valueOf(t1.getDegreeout()));
				// return
				// t1.getTarget_content().compareTo(t2.getTarget_content());
			}
		});
		for (int i11 = 0; i11 < ListRecommend.size(); i11++) {
			Cooccurrence Recomendation = new Cooccurrence();
			Recomendation.setRanking(i11);
		}

		// for (Node no : graph.getNodes().toArray()) {
		// if(form.getTitleMap().get("en").equals(no.getNodeData().getAttributes().getValue(node_tagerItem.getIndex()))){
		// for (Node no2 : graph.getNodes().toArray()) {
		// if()
		//
		// }
		// }
		//
		//
		// }
		// AutoLayout autoLayout = new AutoLayout(1, TimeUnit.MINUTES);
		// autoLayout.setGraphModel(graphModel);
		// YifanHuLayout firstLayout = new YifanHuLayout(null,
		// new StepDisplacement(1f));
		// ForceAtlasLayout secondLayout = new ForceAtlasLayout(null);
		//
		// AutoLayout.DynamicProperty adjustBySizeProperty = AutoLayout
		// .createDynamicProperty("Adjust by Sizes", Boolean.TRUE,
		// 0.1f);
		// AutoLayout.DynamicProperty repulsionProperty = AutoLayout
		// .createDynamicProperty("Repulsion strength",
		// new Double(500.), 0f);
		//
		// autoLayout.addLayout(firstLayout, 0.5f);
		// // ,new
		// //
		// AutoLayout.DynamicProperty[]{adjustBySizeProperty,repulsionProperty}
		// autoLayout.addLayout(secondLayout, 0.5f);
		// autoLayout.execute();

		// export file//
		ExportController ec = Lookup.getDefault()
				.lookup(ExportController.class);
		try {

			ec.exportFile(new File("ka.gexf"));
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

		GraphExporter exporter = (GraphExporter) ec.getExporter("gexf");
		exporter.setExportVisible(true);
		try {
			File path = new File(Thread.currentThread().getContextClassLoader()
					.getResource("").toString());
			String abPath = path.getParentFile().getPath();
			abPath = abPath.substring(4, abPath.length());
			ec.exportFile(new File(directory1, "ka.gexf"), exporter);

		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

	}

	// 予備ネットワークテスト
	private void KALens2(ItemEditForm form) {
		// TODO Auto-generated method stub

		// UserID取得
		Usertest userid = userService.findById(SecurityUserHolder
				.getCurrentUser().getId());
		Kasetting ka = userService.getka(SecurityUserHolder.getCurrentUser()
				.getId());
		// KALens network graph
		// create*************************************************************************************************************//
		ProjectController pc = Lookup.getDefault().lookup(
				ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();

		// import file//
		ImportController importController = Lookup.getDefault().lookup(
				ImportController.class);
		Container container = null;

		GraphModel graphModel = Lookup.getDefault()
				.lookup(GraphController.class).getModel();
		AttributeModel attributeModel = Lookup.getDefault()
				.lookup(AttributeController.class).getModel();
		// Add Node column
		AttributeColumn Hyper = attributeModel.getNodeTable().addColumn(
				"Authorid", AttributeType.STRING);
		AttributeColumn node_tagerItem = attributeModel.getNodeTable()
				.addColumn("Targetitem", AttributeType.STRING);
		AttributeColumn node_tagerItemid = attributeModel.getNodeTable()
				.addColumn("Targetitemid", AttributeType.STRING);
		AttributeColumn node_NextItem = attributeModel.getNodeTable()
				.addColumn("Nextitem", AttributeType.STRING);
		AttributeColumn node_NextItemid = attributeModel.getNodeTable()
				.addColumn("Nextitemid", AttributeType.STRING);

		// degrees = attributeModel.getNodeTable().addColumn("degree", "Degree",
		// AttributeType.INT, AttributeOrigin.COMPUTED, 0);
		GraphController gc = Lookup.getDefault().lookup(GraphController.class);
		graphModel = gc.getModel();
		Graph graph = gc.getModel().getDirectedGraph();
		// Graph graph = gc.getModel().getDirectedGraph();
		Node[] n = new Node[50000];

		List<Users> user = userService.findBynetworkuser();
		ArrayList<Cooccurrence> Currence = new ArrayList<Cooccurrence>();
		List<Cooccurrence> coocurrence = new ArrayList<Cooccurrence>();
		for (int u = 0; u < user.size(); u++) {
			// abc userを除く
			if (user.get(u).getId().equals("16bb9432301b850401301bf6abcc0308")) {

			} else {
				coocurrence = itemservice.cooccurrence(user.get(u).getId());
				int j = 1;

				// 共起検索************************************************************************************************************************************//
				for (int i = 0; i < coocurrence.size(); i++) {

					if (coocurrence.size() > j) {
						Cooccurrence currenceentity = new Cooccurrence();
						currenceentity.setTarget_content(coocurrence.get(i)
								.getContent());
						currenceentity.setNext_content(coocurrence.get(j)
								.getContent());
						currenceentity.setTarget_itemid(coocurrence.get(i)
								.getItemid());
						currenceentity.setNext_itemid(coocurrence.get(j)
								.getItemid());
						currenceentity.setLanguage(coocurrence.get(i)
								.getLanguage());
						currenceentity.setTarget_authorid(coocurrence.get(i)
								.getAuthorid());
						currenceentity.setNext_authorid(coocurrence.get(j)
								.getAuthorid());
						// System.out.println(coocurrence.get(i).getContent() +
						// "->"
						// + coocurrence.get(j).getContent());
						Currence.add(currenceentity);

						j++;
					}
				}

			}
		}

		// Knowledge
		// 共起ネットワーク生成************************************************************************************************************************************//
		for (int i = 0; i < Currence.size(); i++) {
//			System.out.println(Currence.get(i).getAuthorid() + ":"
//					+ userid.getId());
			if (Currence.get(i).getTarget_authorid().equals(userid.getId())) {
				n[i] = graphModel.factory().newNode(String.valueOf(i));
				n[i].getNodeData()
						.setLabel(Currence.get(i).getTarget_content());
				n[i].getNodeData().setColor(1.0f, 0.0f, 1.0f);
				n[i].getNodeData().setSize(15f);
				n[i].getNodeData()
						.getAttributes()
						.setValue(Hyper.getIndex(),
								Currence.get(i).getTarget_authorid());
				n[i].getNodeData()
						.getAttributes()
						.setValue(node_tagerItem.getIndex(),
								Currence.get(i).getTarget_content());
				n[i].getNodeData()
						.getAttributes()
						.setValue(node_tagerItemid.getIndex(),
								Currence.get(i).getTarget_itemid());
				n[i].getNodeData()
						.getAttributes()
						.setValue(node_NextItem.getIndex(),
								Currence.get(i).getNext_content());
				n[i].getNodeData()
						.getAttributes()
						.setValue(node_NextItemid.getIndex(),
								Currence.get(i).getNext_itemid());
				graph.addNode(n[i]);
			} else {

				n[i] = graphModel.factory().newNode(String.valueOf(i));
				n[i].getNodeData()
						.setLabel(Currence.get(i).getTarget_content());
				n[i].getNodeData().setColor(1.0f, 1.0f, 0.0f);
				n[i].getNodeData().setSize(13f);
				n[i].getNodeData()
						.getAttributes()
						.setValue(Hyper.getIndex(),
								Currence.get(i).getTarget_authorid());
				n[i].getNodeData()
						.getAttributes()
						.setValue(node_tagerItem.getIndex(),
								Currence.get(i).getTarget_content());
				n[i].getNodeData()
						.getAttributes()
						.setValue(node_tagerItemid.getIndex(),
								Currence.get(i).getTarget_itemid());
				n[i].getNodeData()
						.getAttributes()
						.setValue(node_NextItem.getIndex(),
								Currence.get(i).getNext_content());
				n[i].getNodeData()
						.getAttributes()
						.setValue(node_NextItemid.getIndex(),
								Currence.get(i).getNext_itemid());

				graph.addNode(n[i]);
			}
			// getAttributes().setValue(dateColumn.getIndex(), randomDataValue);
			// n[i]=graphModel.factory().newNode("0," +
			// String.valueOf(Currence.get(i).getTarget_itemid()));
			// n[i].getNodeData().setLabel(Currence.get(i).getTarget_content());
			// // if(i==0){
			// // n[i].getNodeData().setColor(0.0f, 0.0f, 0.0f);
			// // n[i].getNodeData().setSize(16f);
			// // }
			//
			//
			// n[i].getNodeData().setColor(1.0f, 0.0f, 1.0f);
			// n[i].getNodeData().setSize(13f);
			// graph.addNode(n[i]);
			// n[40000-i]=graphModel.factory().newNode("0," +
			// String.valueOf(Currence.get(i).getNext_itemid()));
			//
			// n[40000-i].getNodeData().setLabel(Currence.get(i).getNext_content());
			// n[40000-i].getNodeData().setColor(0.0f, 0.0f, 1.0f);
			// n[40000-i].getNodeData().setSize(13f);
			// graph.addNode(n[40000-i]);
			//
			// Edge e1 = graphModel.factory().newEdge(
			// n[i].getNodeData().getId() + "-"
			// + n[40000-i].getNodeData().getLabel(), n[i],
			// n[40000-i], 1f, true);
			// graph.addEdge(e1);
			//
		}
		// edge network generate
		for (Node no : graph.getNodes().toArray()) {

			for (Node no2 : graph.getNodes().toArray()) {
				if (no.getNodeData()
						.getAttributes()
						.getValue(Hyper.getIndex())
						.equals(no2.getNodeData().getAttributes()
								.getValue(Hyper.getIndex()))) {
					if (no.getNodeData()
							.getAttributes()
							.getValue(node_NextItemid.getIndex())
							.equals(no2.getNodeData().getAttributes()
									.getValue(node_tagerItemid.getIndex()))) {
						Edge e1 = graphModel.factory().newEdge(
								KeyGenerateUtil.generateIdUUID(), no, no2, 1f,
								true);
						graph.addEdge(e1);
					}
				}
			}
		}

		// Connecting learners' own knowledge to next others knowledge
		for (Node no : graph.getNodes().toArray()) {
			for (Node no2 : graph.getNodes().toArray()) {
				if (no.getNodeData()
						.getAttributes()
						.getValue(node_NextItem.getIndex())
						.equals(no2.getNodeData().getAttributes()
								.getValue(node_tagerItem.getIndex()))) {
					Edge e1 = graphModel.factory()
							.newEdge(KeyGenerateUtil.generateIdUUID(), no, no2,
									1f, true);
					e1.getEdgeData().setColor(1.0f, 1.0f, 0.0f);
					graph.addEdge(e1);
				}

			}

		}

		// edge linking
		// for (Node no : graph.getNodes().toArray()) {
		// //
		// System.out.println(no.getNodeData().getAttributes().getValue(node_tagerItem.getIndex()));
		// if(userid.getId().equals(no.getNodeData().getAttributes().getValue(Hyper.getIndex()))){
		// for (Node no2 : graph.getNodes().toArray()) {
		// if(no.getNodeData().getAttributes().getValue(node_NextItemid.getIndex()).equals(no2.getNodeData().getAttributes().getValue(node_tagerItemid.getIndex()))){
		// if(userid.getId().equals(no2.getNodeData().getAttributes().getValue(Hyper.getIndex()))){
		// no.getNodeData().setColor(1.0f, 0.0f, 1.0f);
		// no2.getNodeData().setColor(1.0f, 0.0f, 1.0f);
		// Edge e1 = graphModel.factory().newEdge(
		// no.getNodeData().getLabel()+ "-"+ no2.getNodeData().getLabel(),
		// no,no2, 1f, true);
		// graph.addEdge(e1);
		// }
		//
		// }
		// else
		// if(no.getNodeData().getAttributes().getValue(node_tagerItem.getIndex()).equals(no2.getNodeData().getAttributes().getValue(node_tagerItem.getIndex()))){
		// if(!no2.getNodeData().getAttributes().getValue(Hyper.getIndex()).equals(userid.getId())){
		// Edge e1 = graphModel.factory().newEdge(
		// no.getNodeData().getLabel()+ "-"+ no2.getNodeData().getLabel(),
		// no,no2, 1f, true);
		// graph.addEdge(e1);
		// }
		// }
		//
		// }
		// }}
		// Layout setting

		// AutoLayout autoLayout = new AutoLayout(1, TimeUnit.MINUTES);
		// autoLayout.setGraphModel(graphModel);
		// YifanHuLayout firstLayout = new YifanHuLayout(null,
		// new StepDisplacement(1f));
		// ForceAtlasLayout secondLayout = new ForceAtlasLayout(null);
		//
		// AutoLayout.DynamicProperty adjustBySizeProperty = AutoLayout
		// .createDynamicProperty("Adjust by Sizes", Boolean.TRUE,
		// 0.1f);
		// AutoLayout.DynamicProperty repulsionProperty = AutoLayout
		// .createDynamicProperty("Repulsion strength",
		// new Double(500.), 0f);

		// autoLayout.addLayout(firstLayout, 0.5f);
		// // ,new
		// //
		// AutoLayout.DynamicProperty[]{adjustBySizeProperty,repulsionProperty}
		// autoLayout.addLayout(secondLayout, 0.5f);
		//
		try {
			// Degree Filter

			if (ka != null) {
				FilterController filterController2 = Lookup.getDefault()
						.lookup(FilterController.class);
				DegreeRangeFilter degreeFilter = new DegreeRangeFilter();
				degreeFilter.init(graph);
				degreeFilter.setRange(new Range(Integer.parseInt(ka
						.getKaquality()), Integer.MAX_VALUE));
				Query query = filterController2.createQuery(degreeFilter);
				GraphView view = filterController2.filter(query);
				graph = graphModel.getGraph(view);
				graphModel.setVisibleView(view);
			} else {
				FilterController filterController2 = Lookup.getDefault()
						.lookup(FilterController.class);
				DegreeRangeFilter degreeFilter = new DegreeRangeFilter();
				degreeFilter.init(graph);
				degreeFilter.setRange(new Range(Integer.parseInt("1"),
						Integer.MAX_VALUE));
				Query query = filterController2.createQuery(degreeFilter);
				GraphView view = filterController2.filter(query);
				graph = graphModel.getGraph(view);
				graphModel.setVisibleView(view);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		// degreeFilter.setRange(new Range(3,Integer.MAX_VALUE));

		try {
			// Egofileter
			if (form.getTitleMap().get("en") != null)
				for (Node no : graph.getNodes().toArray()) {
					if (no.getNodeData().getAttributes()
							.getValue(node_tagerItem.getIndex())
							.equals(form.getTitleMap().get("en"))) {
						no.getNodeData().setColor(0.1f, 0.1f, 0.9f);
						no.getNodeData().setSize(80f);
						graph.addNode(no);

					}
				}

			FilterController filterController = Lookup.getDefault().lookup(
					FilterController.class);
			EgoFilter egoFilter = new EgoFilter();
			egoFilter.setPattern(form.getTitleMap().get("en"));
			if (ka != null) {
				egoFilter.setDepth(Integer.parseInt(ka.getViewdistance()));
//				System.out.println("Distance" + ka.getViewdistance());
			} else {
				egoFilter.setDepth(Integer.parseInt("3"));
			}
			Query queryEgo = filterController.createQuery(egoFilter);
			GraphView viewEgo = filterController.filter(queryEgo);
			graph = graphModel.getGraph(viewEgo);
			graphModel.setVisibleView(viewEgo);

		} catch (Exception e) {
			e.printStackTrace();

		}
		//
		// if (form.getEgofiltername() != null
		// && form.getEgofilterdistance() != null
		// && !(form.getEgofilterdistance().equals("Target Degree"))
		// && !(form.getEgofiltername().equals(""))) {
		// for (Node no : graph.getNodes().toArray()) {
		// if (no.getNodeData().getAttributes()
		// .getValue(node_tagerItem.getIndex())
		// .equals(form.getEgofiltername())) {
		// no.getNodeData().setColor(0.1f, 0.1f, 0.9f);
		// no.getNodeData().setSize(80f);
		// graph.addNode(no);
		//
		// }
		// }
		//
		// FilterController filterController = Lookup.getDefault().lookup(
		// FilterController.class);
		// EgoFilter egoFilter = new EgoFilter();
		// egoFilter.setPattern(form.getEgofiltername());
		// egoFilter.setDepth(Integer.parseInt(form.getEgofilterdistance()));
		// Query queryEgo = filterController.createQuery(egoFilter);
		// GraphView viewEgo = filterController.filter(queryEgo);
		// graph = graphModel.getGraph(viewEgo);
		// graphModel.setVisibleView(viewEgo);

		GraphDistance distance = new GraphDistance();
		distance.setDirected(true);
		distance.execute(graphModel, attributeModel);

		AttributeColumn between = attributeModel.getNodeTable().getColumn(
				GraphDistance.BETWEENNESS);
		AttributeColumn closeness = attributeModel.getNodeTable().getColumn(
				GraphDistance.CLOSENESS);
		AttributeColumn degCol = attributeModel.getNodeTable().getColumn(
				"degree");
		degCol = attributeModel.getNodeTable().addColumn("degree", "Degree",
				AttributeType.STRING, AttributeOrigin.COMPUTED, "0");
		// AttributeColumn de = attributeModel.getNodeTable().addColumn(
		// "Degree", AttributeType.STRING);
		for (Node no : graph.getNodes().toArray()) {
			Double centrality = (Double) no.getNodeData().getAttributes()
					.getValue(between.getIndex());
//			System.out.println(centrality);
			no.getNodeData().getAttributes()
					.setValue(degCol.getIndex(), graph.getDegree(no));
			// no.getNodeData().getAttributes().setValue(de.getIndex(),Degree.DEGREE);

		}
		double[] t1 = new double[20];
		double id = 0;
		double id2 = 0;
		for (Node no : graph.getNodes().toArray()) {
			if (id < (Double) no.getNodeData().getAttributes()
					.getValue(between.getIndex())) {
				id = (Double) no.getNodeData().getAttributes()
						.getValue(between.getIndex());
			}
		}
		for (Node no : graph.getNodes().toArray()) {

			if ((Double) no.getNodeData().getAttributes()
					.getValue(between.getIndex()) >= id) {
				no.getNodeData().setColor(0.3f, 0.4f, 0.9f);
				no.getNodeData().setSize(80f);
				graph.addNode(no);
			}
		}
		// 水色　近接中心性
		for (Node no : graph.getNodes().toArray()) {
			if (id2 < (Double) no.getNodeData().getAttributes()
					.getValue(closeness.getIndex())) {
				id2 = (Double) no.getNodeData().getAttributes()
						.getValue(closeness.getIndex());
			}
		}
		for (Node no : graph.getNodes().toArray()) {

			if ((Double) no.getNodeData().getAttributes()
					.getValue(closeness.getIndex()) >= id2) {
				no.getNodeData().setColor(0.2f, 0.9f, 0.9f);
				no.getNodeData().setSize(80f);
				graph.addNode(no);
			}
		}
		// Recommendation
		ListRecommend = new ArrayList<Cooccurrence>();
		double degreechecker = 0;
		double index = 0;
		for (Node no : graph.getNodes().toArray()) {
			if (form.getTitleMap()
					.get("en")
					.equalsIgnoreCase(
							String.valueOf(no.getNodeData().getAttributes()
									.getValue(node_tagerItem.getIndex())))) {
				for (Node no2 : graph.getNodes().toArray()) {
					if (no.getNodeData()
							.getAttributes()
							.getValue(node_NextItem.getIndex())
							.equals(no2.getNodeData().getAttributes()
									.getValue(node_tagerItem.getIndex()))) {
						Cooccurrence Recomendation = new Cooccurrence();
						Recomendation.setLabel(String.valueOf(no2.getNodeData()
								.getAttributes()
								.getValue(node_tagerItem.getIndex())));
						Recomendation.setDegree(String.valueOf(no2
								.getNodeData().getAttributes()
								.getValue(degCol.getIndex())));
						Recomendation.setTarget_itemid(String.valueOf(no2
								.getNodeData().getAttributes()
								.getValue(node_tagerItemid.getIndex())));
//						System.out.println(String.valueOf(no2.getNodeData()
//								.getAttributes()
//								.getValue(node_tagerItem.getIndex()))
//								+ "///"
//								+ String.valueOf(no2.getNodeData()
//										.getAttributes()
//										.getValue(degCol.getIndex())));
						ListRecommend.add(Recomendation);
					}
				}
			}
		}

		Collections.sort(ListRecommend, new Comparator<Cooccurrence>() {
			public int compare(Cooccurrence t1, Cooccurrence t2) {
				return +((int) Integer.valueOf(t2.getDegree()) - Integer
						.valueOf(t1.getDegree()));
				// return
				// t1.getTarget_content().compareTo(t2.getTarget_content());
			}
		});
		for (int i = 0; i < ListRecommend.size(); i++) {
			Cooccurrence Recomendation = new Cooccurrence();
			Recomendation.setRanking(i);
		}

		// for (Node no : graph.getNodes().toArray()) {
		// if(form.getTitleMap().get("en").equals(no.getNodeData().getAttributes().getValue(node_tagerItem.getIndex()))){
		// for (Node no2 : graph.getNodes().toArray()) {
		// if()
		//
		// }
		// }
		//
		//
		// }
		// AutoLayout autoLayout = new AutoLayout(1, TimeUnit.MINUTES);
		// autoLayout.setGraphModel(graphModel);
		// YifanHuLayout firstLayout = new YifanHuLayout(null,
		// new StepDisplacement(1f));
		// ForceAtlasLayout secondLayout = new ForceAtlasLayout(null);
		//
		// AutoLayout.DynamicProperty adjustBySizeProperty = AutoLayout
		// .createDynamicProperty("Adjust by Sizes", Boolean.TRUE,
		// 0.1f);
		// AutoLayout.DynamicProperty repulsionProperty = AutoLayout
		// .createDynamicProperty("Repulsion strength",
		// new Double(500.), 0f);
		//
		// autoLayout.addLayout(firstLayout, 0.5f);
		// // ,new
		// //
		// AutoLayout.DynamicProperty[]{adjustBySizeProperty,repulsionProperty}
		// autoLayout.addLayout(secondLayout, 0.5f);
		// autoLayout.execute();

		// export file//
		ExportController ec = Lookup.getDefault()
				.lookup(ExportController.class);
		try {

			ec.exportFile(new File("ka.gexf"));
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

		GraphExporter exporter = (GraphExporter) ec.getExporter("gexf");
		exporter.setExportVisible(true);
		try {
			File path = new File(Thread.currentThread().getContextClassLoader()
					.getResource("").toString());
			String abPath = path.getParentFile().getPath();
			abPath = abPath.substring(4, abPath.length());
			ec.exportFile(new File(directory1, "ka.gexf"), exporter);

		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

	}
	
	public class Myobj {
		private String name;
		public String getName() {
	        return name;
	    }
	    public void setName(String name) {
	        this.name = name;
	    }
		
	}

}
