package jp.ac.tokushima_u.is.ll.service;
//TODO
//
//import java.io.IOException;
//import java.io.StringWriter;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import javax.mail.MessagingException;
//
//import jp.ac.tokushima_u.is.ll.dto.ItemListMailDTO;
//import jp.ac.tokushima_u.is.ll.entity.Item;
//import jp.ac.tokushima_u.is.ll.entity.Users;
//import jp.ac.tokushima_u.is.ll.form.EmailModel;
//import jp.ac.tokushima_u.is.ll.form.ItemSearchCondForm;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.RememberMeAuthenticationProvider;
//import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
//
//import freemarker.template.Configuration;
//import freemarker.template.Template;
//import freemarker.template.TemplateException;
//
//@Service("weeklyNotificationService")
//@Transactional
//public class WeeklyNotificationService {
//	
//	private static Logger logger = LoggerFactory.getLogger(WeeklyNotificationService.class);
//	private static final int ONE_TIME_SEND_NUMBER = 0;
//	private static final long SLEEP_TIME_OUT = 60000;
//	public static final String TEMPLATE_NAME = "weeklyNotification";
//	private static final SimpleDateFormat format = new SimpleDateFormat(
//			"MM/dd/yyyy");
//	
//	@Autowired
//	private PropertyService propertyService;
//
//	@Autowired
//	private RememberMeAuthenticationProvider rememberMeAuthenticationProvider;
//
//	@Autowired
//	private UserService userService;
//
//	@Autowired
//	private FreeMarkerConfigurer freeMarkerConfigurer;
//
//	@Autowired
//	private ItemService itemService;
//	
//	@Autowired
//	private MailService mailService;
//	
//	@Autowired
//	private LogSendMailService logSendMailService;
//
//	public synchronized void sendNotification() throws TemplateException, IOException, MessagingException {
//		if(propertyService.getSendWeeklyFlag()==null || !propertyService.getSendWeeklyFlag())return;
//
//		List<Users> userList = userService
//				.findAllUserListForWeeklyNotification();
//		
//		Set<Users> userSet = new HashSet<Users>(userList);
//		int count=0;
//		for(Users user: userSet){
//				if(count>=ONE_TIME_SEND_NUMBER){
//					count=0;
//					try {
//						Thread.sleep(SLEEP_TIME_OUT);
//					} catch (InterruptedException e) {
//					}
//				}
//				try{
//				if(logSendMailService.findIsWeeklyNotificationSent(user.getPcEmail())){
//					continue;
//				}else{
//					logSendMailService.saveSendWeeklyNotification(user.getPcEmail());
//				}
//				
//				String date = format.format(new Date());
//				String sendTo = user.getPcEmail();
//				String subject = "[Learning Log]Application Updates (" + date + ")";
//				ModelMap model = new ModelMap();
//				// head part link
//				// New object page link
//				model.addAttribute("sendDate", date);
//				model.addAttribute("newObjectPageLink",
//						encodeUrl(propertyService.getSystemUrl() + "/item/add", user));
//				model.addAttribute("quizPageLink", encodeUrl(propertyService.getSystemUrl() + "/quiz", user));
//				model.addAttribute("directLogin", encodeUrl(propertyService.getSystemUrl(), user));
//				model.addAttribute("settingLink", encodeUrl(propertyService.getSystemUrl() + "/mysetting", user));
//		
//				model.addAttribute(
//						"oneMonthList",
//						convertItemListMailDTO(itemService.findAllItemsBeforeMonths(1,
//								1, user.getId(), 3), user));
//				model.addAttribute(
//						"twoMonthsList",
//						convertItemListMailDTO(itemService.findAllItemsBeforeMonths(2,
//								1, user.getId(), 3), user));
//				model.addAttribute(
//						"threeMonthsList",
//						convertItemListMailDTO(itemService.findAllItemsBeforeMonths(3,
//								1, user.getId(), 3), user));
//				model.addAttribute(
//						"fourMonthsList",
//						convertItemListMailDTO(
//								itemService.findAllItemsBeforeMonths(4, null,
//										user.getId(), 3), user));
//				model.addAttribute(
//						"thisWeek",
//						convertItemListMailDTO(itemService.findAllItemsBeforeMonths(0,
//								1, user.getId(), null), user));
//		
//				ItemSearchCondForm awaitCond = new ItemSearchCondForm();
//				awaitCond.setToAnswerQuesLangs(user.getMyLangs());
//				model.addAttribute("toAnswerItems",
//						convertAnswerToDTO(itemService.searchItemPageByCond(awaitCond).getResult(), user));
//		
//				ItemSearchCondForm answeredCond = new ItemSearchCondForm();
//				answeredCond.setHasAnswers(true);
//				answeredCond.setUserId(user.getId());
//				model.addAttribute("answeredItems",
//						convertAnswerToDTO(itemService.searchItemPageByCond(answeredCond).getResult(), user));
//		
//				EmailModel email = new EmailModel();
//				email.setAddress(sendTo);
//				email.setFrom(propertyService.getSystemMailAddress());
//				email.setReplyTo(email.getFrom());
//				email.setSubject(subject);
//				email.setHtml(true);
//				Configuration cfg = freeMarkerConfigurer.getConfiguration();
//				Template t = cfg.getTemplate(TEMPLATE_NAME + ".ftl");
//				StringWriter writer = new StringWriter();
//				t.process(model, writer);
//				email.setContent(writer.toString());
//				mailService.sendNotificationMail(email);
//				count++;
//			} catch(Exception e){
//				logger.error("ERROR_SEND_NOTIFICATION", e);
//			}
//		}
//	}
//
//	private List<ItemListMailDTO> convertAnswerToDTO(
//			List<Item> itemList, Users user) {
//		List<ItemListMailDTO> dtoList = new ArrayList<ItemListMailDTO>();
//		for (Item item : itemList) {
//			ItemListMailDTO dto = new ItemListMailDTO();
//			dto.setUrl(encodeUrl(propertyService.getSystemUrl() + "/item/" + item.getId(), user));
//			String title = "";
//			int size = 0;
//			if(item.getQuestion()!=null){
//				title = item.getQuestion().getContent();
//			}
//			if (item.getQuestion() != null
//					&& item.getQuestion().getAnswerSet() != null) {
//				size = item.getQuestion().getAnswerSet().size();
//			}
//			dto.setTitle(format.format(item.getCreateTime()) + "&nbsp;" + title
//					+ "&nbsp;(" + size + ")");
//			dto.setAuthor(item.getAuthor().getNickname());
//			dtoList.add(dto);
//		}
//		return dtoList;
//	}
//
//	private List<ItemListMailDTO> convertItemListMailDTO(List<Item> itemList,
//			Users user) throws UnsupportedEncodingException {
//		List<ItemListMailDTO> dtoList = new ArrayList<ItemListMailDTO>();
//		for (Item item : itemList) {
//			ItemListMailDTO dto = new ItemListMailDTO();
//			dto.setUrl(encodeUrl(propertyService.getSystemUrl() + "/item/" + item.getId(), user));
//			String title=item.getDefaultTitle();
//			int size = 0;
//			if (item.getQuestion() != null
//					&& item.getQuestion().getAnswerSet() != null) {
//				size = item.getQuestion().getAnswerSet().size();
//			}
//			dto.setTitle(format.format(item.getCreateTime()) + "&nbsp;" + title
//					+ "&nbsp;(" + size + ")");
//			dto.setAuthor(item.getAuthor().getNickname());
//			dtoList.add(dto);
//		}
//		return dtoList;
//	}
//
//	public String encodeUrl(String url, Users user) {
//		try {
//			ShaPasswordEncoder encoder = new ShaPasswordEncoder();
//			String nurl = URLEncoder.encode(url, "UTF-8");
//			String key = encoder.encodePassword(url, user.getCreateTime()
//					.toString()
//					+ user.getId()
//					+ rememberMeAuthenticationProvider.getKey());
//			return propertyService.getSystemUrl() + "/jumpurl?key=" + key + "&url=" + nurl
//					+ "&email=" + user.getPcEmail();
//		} catch (UnsupportedEncodingException e) {
//			return "";
//		}
//	}
//}
