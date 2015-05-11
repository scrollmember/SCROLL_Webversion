package jp.ac.tokushima_u.is.ll.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.Answer;
import jp.ac.tokushima_u.is.ll.entity.FileData;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.ItemTag;
import jp.ac.tokushima_u.is.ll.entity.Language;
import jp.ac.tokushima_u.is.ll.entity.Question;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.form.ItemSearchCondForm;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.ItemService;
import jp.ac.tokushima_u.is.ll.service.LLQuizService;
import jp.ac.tokushima_u.is.ll.service.LogService;
import jp.ac.tokushima_u.is.ll.service.StaticServerService;
import jp.ac.tokushima_u.is.ll.service.UserService;
import jp.ac.tokushima_u.is.ll.ws.service.ItemWebService;
import jp.ac.tokushima_u.is.ll.ws.service.converter.ItemConverter;
import jp.ac.tokushima_u.is.ll.ws.service.model.ItemForm;
import jp.ac.tokushima_u.is.ll.ws.service.model.ItemModel;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author houbin
 */
@Service(value = "itemWebService")
public class ItemWebServiceImpl implements ItemWebService {

	private static final Logger logger = LoggerFactory
			.getLogger(ItemWebServiceImpl.class);
	private HibernateDao<Item, String> itemDao;
	private HibernateDao<Language, String> languageDao;
	private HibernateDao<FileData, String> fileDataDao;
	private HibernateDao<ItemTag, String> itemTagDao; 
	@Autowired
	private UserService userService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private StaticServerService staticServerService;

	@Autowired
	private LogService logService;

	@Autowired
	private LLQuizService llquizService;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		itemDao = new HibernateDao<Item, String>(sessionFactory, Item.class);
		new HibernateDao<Answer, String>(sessionFactory, Answer.class);
		new HibernateDao<Question, String>(sessionFactory, Question.class);
		languageDao = new HibernateDao<Language, String>(sessionFactory,
				Language.class);
		fileDataDao = new HibernateDao<FileData, String>(sessionFactory,
				FileData.class);
		itemTagDao = new HibernateDao<ItemTag, String>(sessionFactory, ItemTag.class);
	}

	public List<ItemModel> findItemByLocation(String userEmail,
			String password, Double lat, Double lng) throws Exception {
		return findItemByLocation(userEmail, password, lat, lng, null);
	}

	public List<ItemModel> findItemByLocation(String userEmail,
			String password, Double lat, Double lng, Boolean onlyForAuthor)
			throws Exception {
		try {
			SecurityUtils.getSubject().login(new UsernamePasswordToken(userEmail, password));
		} catch (AuthenticationException e) {
			throw e;
		}
		Users user = SecurityUserHolder.getCurrentUser();
		return this.findItemByLocationAndStudyLan(lat, lng, user
				.getStudyLangs(), user.getId(), onlyForAuthor);
	}

	public List<ItemModel> findItemByLocationAndStudyLan(Double lat,
			Double lng, List<Language> lanset) throws Exception {
		return findItemByLocationAndStudyLan(lat, lng, lanset, null, null);
	}

	public List<ItemModel> findItemByLocationAndStudyLan(Double lat,
			Double lng, List<Language> lanset, String userId,
			Boolean onlyForAuthor) throws Exception {
		if (lat == null || lng == null) {
			return new ArrayList<ItemModel>();
		}
		Double x1, y1, x2, y2, xt, yt;
		Double m = 360 / 39940.638;
		x1 = lat + m;
		x2 = lat - m;
		if (x1 < -90 || x1 > 90) {
			x1 = 180 * x1 / Math.abs(x1) - x1;
		}
		if (x2 < -90 || x2 > 90) {
			x2 *= 180 * x2 / Math.abs(x2) - x2;
		}
		if (x1 < x2) {
			xt = x1;
			x1 = x2;
			x2 = xt;
		}
		Double clat = 360 / (2 * Math.PI * Math.cos(lat) * 40075.004);
		if (clat < 0) {
			clat *= -1;
		}

		y1 = lng - clat;
		y2 = lng + clat;
		if (y1 < -180 || y1 > 180) {
			y1 = (360 * (y1 / Math.abs(y1)) - y1) * (-1);
		}
		if (y2 < -180 || y2 > 180) {
			y2 = (360 * y2 / Math.abs(y2) - y2) * (-1);
		}
		if (y1 < y2) {
			yt = y1;
			y1 = y2;
			y2 = yt;
		}

		ItemSearchCondForm form = new ItemSearchCondForm();
		form.setX1(x1);
		form.setY1(y1);
		form.setX2(x2);
		form.setY2(y2);
		if (lanset != null)
			form.setTargetlang(lanset);
		form.setMapenabled(true);
		form.setUserId(userId);
		form.setOnlyForAuthor(onlyForAuthor);
		List<Item> itemList = itemService.searchAllItemsByCond(form);
		List<ItemModel> itemModelList = new ArrayList<ItemModel>();
		for (Item item : itemList) {
			itemModelList.add(ItemConverter.convert(item));
		}
		return itemModelList;
	}
	
	@Transactional
	public Integer insertItem(ItemForm form) throws Exception {
//		if (StringUtils.isBlank(form.getUserEmail())) {
//			return Constants.Item_USER_EMAIL_EMPTY;
//		}
//
//		if (StringUtils.isBlank(form.getUserPassword())) {
//			return Constants.Item_USER_PASSWORD_EMPTY;
//		}
//
//		// Authentication
//		Users user = null;
//		try {
//			user = userService.findByEmail(form.getUserEmail());
//		} catch (UsernameNotFoundException usernameNotFoundException) {
//			return Constants.Item_USER_NOT_FOUND;
//		} catch (DataAccessException dataAccessException) {
//			logger.error("Data Access Error", dataAccessException);
//			return Constants.Item_DATA_ACCESS_ERROR;
//		}
//		if (!passwordEncoder.encodePassword(form.getUserPassword(), "").equals(
//				user.getPassword())) {
//			return Constants.Item_USER_PASSWORD_ERROR;
//		}
//		// if (StringUtils.isBlank(form.getFileName())) {
//		// return Constants.Item_FILE_NAME_EMPTY;
//		// }
//		// if (form.getImageFile() == null || form.getImageFile().length == 0) {
//		// return Constants.Item_FORM_DATA_EMPTY;
//		// }
//		Integer result = createByItemForm(user, form);
		return 1;
	}
}
