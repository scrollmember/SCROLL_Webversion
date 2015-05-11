package jp.ac.tokushima_u.is.ll.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.ItemQueue;
import jp.ac.tokushima_u.is.ll.entity.Language;
import jp.ac.tokushima_u.is.ll.entity.LanguageAbility;
import jp.ac.tokushima_u.is.ll.entity.LogUserReadItem;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.quiz.QuizConstants;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.util.Constants;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("itemqueueService")
@Transactional
public class ItemQueueService {
	private HibernateDao<ItemQueue, String> itemqueueDao;
	private HibernateDao<Item, String> itemDao;
	private HibernateDao<Language, String> languageDao;
	private HibernateDao<LogUserReadItem, String> readItemDao;
	private HibernateDao<Users, String> userDao;
	private HibernateDao<LanguageAbility, String> languageAbilityDao;
	private SessionFactory sessionFactory;

	public final static Integer DEFAULT_AddObject_FORGET_DAYS = 0;
	public final static Integer DEFAULT_AddObject_FORGET_HOURS = 5;
	public final static Integer DEFAULT_RIGHT_FORGET_DAYS = 5;
	public final static Integer DEFAULT_WRONG_FORGET_DAYS = 2;
	public final static Integer DEFAULT_VIEW_OBJECT_DAYS = 1;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		itemqueueDao = new HibernateDao<ItemQueue, String>(sessionFactory,
				ItemQueue.class);
		readItemDao = new HibernateDao<LogUserReadItem, String>(sessionFactory,
				LogUserReadItem.class);
		userDao = new HibernateDao<Users, String>(sessionFactory, Users.class);
		languageDao = new HibernateDao<Language, String>(sessionFactory,
				Language.class);
		languageAbilityDao = new HibernateDao<LanguageAbility, String>(
				sessionFactory, LanguageAbility.class);
		itemDao = new HibernateDao<Item, String>(
				sessionFactory, Item.class);
	}

	public void updateItemQueue(Item item, Users user, int queuetype) {
		if (item.getQuestionTypes() == null
				|| item.getQuestionTypes().size() == 0)
			return;

		Calendar cal = Calendar.getInstance();
		Date startdate = cal.getTime();

		boolean isPass = false;

		switch (queuetype) {
		case QuizConstants.QueueTypeNewObject:
			cal.add(Calendar.DATE, DEFAULT_AddObject_FORGET_DAYS);
			cal.add(Calendar.HOUR_OF_DAY, DEFAULT_AddObject_FORGET_HOURS);
			startdate = cal.getTime();
			break;
		case QuizConstants.QueueTypeRightQuiz:
			cal.add(Calendar.DATE, DEFAULT_RIGHT_FORGET_DAYS);
			startdate = cal.getTime();
			break;
		case QuizConstants.QueueTypeWrongQuiz:
			cal.add(Calendar.DATE, DEFAULT_WRONG_FORGET_DAYS);
			startdate = cal.getTime();
			break;
		case QuizConstants.QueueTypeViewObject:
			cal.add(Calendar.DATE, DEFAULT_VIEW_OBJECT_DAYS);
			startdate = cal.getTime();
			break;
		case QuizConstants.QueueTypeObjectPass:
			isPass = true;
			break;
		case QuizConstants.QueueTypeDifficultQuiz:
			isPass = true;
			break;
		case QuizConstants.QueueTypeEasyQuiz:
			isPass = true;
			break;
		}

		DetachedCriteria criteria = DetachedCriteria.forClass(ItemQueue.class);
		criteria.add(Restrictions.eq("item", item));
		criteria.add(Restrictions.eq("author", user));
		criteria.add(Restrictions.eq("disabled", new Integer(0)));
		criteria.addOrder(Order.desc("startTime"));
		List<ItemQueue> queues = this.itemqueueDao.find(criteria);
		if (queues != null && queues.size() > 0) {
			for (ItemQueue queue : queues) {
				queue.setDisabled(1);
				queue.setUpdateTime(new Date());
				this.itemqueueDao.save(queue);
			}
		}

		if (isPass)
			return;

		if (queuetype == QuizConstants.QueueTypeRightQuiz && queues != null
				&& queues.size() > 0) {
			ItemQueue iq = queues.get(0);
			if (iq.getQueuetype() == QuizConstants.QueueTypeRightQuiz) {
				Calendar c0 = Calendar.getInstance();
				Calendar c1 = Calendar.getInstance();
				Calendar c2 = Calendar.getInstance();
				c1.setTime(iq.getCreateTime());
				c2.setTime(iq.getStartTime());
				int d = Math.abs(c2.get(Calendar.DATE)
						- c1.get(Calendar.DATE));
				if(d<1);
					d=1;
				c0.add(Calendar.DATE,
						DEFAULT_RIGHT_FORGET_DAYS
								* d);
				startdate = c0.getTime();
			}
		}

		ItemQueue itemqueue = null;
		itemqueue = new ItemQueue();
		itemqueue.setAuthor(user);
		itemqueue.setCreateTime(new Date());
		itemqueue.setUpdateTime(new Date());
		itemqueue.setDisabled(0);
		itemqueue.setItem(item);
		itemqueue.setStartTime(startdate);
		itemqueue.setQueuetype(queuetype);
		if (item.getAuthor().equals(user))
			itemqueue.setWeight(Constants.PriorityMyItemWeight);
		else {
			DetachedCriteria read_dc = DetachedCriteria
					.forClass(LogUserReadItem.class);
			read_dc.add(Restrictions.eq("user", user));
			read_dc.add(Restrictions.eq("item", item));
			List<LogUserReadItem> results = this.readItemDao.find(read_dc);
			if (results.size() > 2) {
				itemqueue.setWeight(Constants.PriorityReferItemWeight);
			} else
				itemqueue.setWeight(Constants.PriorityRecommendedItemWeight);
		}
		this.itemqueueDao.save(itemqueue);
	}

	public void searchRecommendItems() {
		Calendar now = Calendar.getInstance();

		List<Users> users = this.userDao.findBy("accountNonLocked",
				Boolean.TRUE);
//	z	List<Users> users = this.userDao.findBy("id","ff80818125899fb501258acaadc10001");
		for (Users user : users) {
			try {
				this.searchUserRecommendedItems(user);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Calendar stop = Calendar.getInstance();
		System.out.println(" costed "
				+ (stop.getTimeInMillis() - now.getTimeInMillis()) / 1000);
	}
	
	public Set<Item> searchRecommendedItems(Users user){
		Session session = this.sessionFactory.getCurrentSession();

//		DetachedCriteria lan_dc = DetachedCriteria
//				.forClass(LanguageAbility.class);
//		lan_dc.add(Restrictions.eq("author", user));
//		lan_dc.add(Restrictions.eq("disabled", 0));
//		List<LanguageAbility> las = this.languageAbilityDao.find(lan_dc);

		String study_lans = "";
		for (int n = 0; n < user.getStudyLangs().size(); n++) {
			if (n > 0)
				study_lans += ",";
			study_lans = study_lans + " '"
					+ user.getStudyLangs().get(n).getId() + "' ";
		}
		if (study_lans.length() <= 0)
			return null;
		
		String my_lans = "";
		List<Language> myLangs = user.getMyLangs();
		for (int n = 0; n < myLangs.size(); n++) {
			if (n > 0)
				my_lans += ",";
			my_lans = my_lans + " '"
					+ myLangs.get(n).getId() + "' ";
		}
		if (my_lans.length() <= 0)
			return null;

		String motherlan_recommend = " select i.*  from t_item i "
				+ "where i.disabled!=1 and i.author_id in ( "
				+ "select id  from t_users u where exists ( "
				+ "select * "
				+ "from t_users_study_langs lan_study where lan_study.t_users = u.id "
				+ " and lan_study.study_langs in ( select study_langs "
				+ "from t_users_study_langs  where t_users = '"
				+ user.getId()
				+ "') ) "
				+ "and exists ( select * from t_users_my_langs lan_my  where lan_my.t_users = u.id and lan_my.my_langs in ( "
				+ "select my_langs from t_users_my_langs where t_users = '"
				+ user.getId()
				+ "' ) )) "
				+ "and i.id not in (  select distinct(item_id) from t_itemqueue  where author_id = '"
				+ user.getId()
				+ "') "
				+ "and exists (select *  from t_item_question_type where language_id in ("
				+ study_lans + ") and item_id = i.id ) and exists (select * from t_item_title where language in ("+my_lans+") and item = i.id) and i.pass<3 order by i.pass  limit 0, 30 ";

		@SuppressWarnings("unchecked")
		List<Item> items = session.createSQLQuery(motherlan_recommend)
				.addEntity(Item.class).list();

		Set<Item> results = new HashSet<Item>();
		results.addAll(items);
		if (results.size() < 30) {
			int len = 30-results.size();
			String sql = "select i.* "
					+ "from t_item i, "
					+ "(select count(*) as c, h.item_id  "
					+ "from l_user_read_item h group by h.item_id) as y  "
					+ "where i.id = y.item_id and i.disabled!=1 and i.id not in "
					+ "(  select distinct(item_id) from t_itemqueue "
					+ "where author_id = '"
					+ user.getId()
					+ "' ) "
					+ "and exists (select *  from t_item_question_type where language_id in ( "
					+ study_lans
					+ ") and item_id = i.id ) " 
					+ "and exists (select * from t_item_title where language in ("+my_lans+") and item = i.id) " 
					+"order by i.pass, c desc limit 0, "+len;
			@SuppressWarnings("unchecked")
			List<Item> popular_items = session.createSQLQuery(sql)
					.addEntity(Item.class).list();
			results.addAll(popular_items);
		}
		
		return results;
	}

	public List<ItemQueue> searchSystemItemQuiz(){
		Users systemUser = this.userDao.findUniqueBy("pcEmail", Constants.SYSTEM_ACCOUNT_EMAIL);
		Users user = this.userDao.findUniqueBy("id",SecurityUserHolder.getCurrentUser().getId());
		
		String sql = "select i.* from t_item i 		where i.author_id ='"+systemUser.getId()+"' 	and i.id not in ( select item_id from t_itemqueue where author_id = '"+SecurityUserHolder.getCurrentUser().getId()+"' ) order by rand() limit 0, 10";
		Session session = this.sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Item> items = session.createSQLQuery(sql)
				.addEntity(Item.class).list();
		List<ItemQueue> queue = new ArrayList<ItemQueue>();
		
		for(Item item:items){
			ItemQueue itemqueue = new ItemQueue();
			itemqueue.setAuthor(user);
			itemqueue.setCreateTime(new Date());
			itemqueue.setUpdateTime(new Date());
			itemqueue.setDisabled(0);
			itemqueue.setItem(item);
			itemqueue.setStartTime(new Date());
			itemqueue.setQueuetype(QuizConstants.QueueTypeRecommende);
			itemqueue.setWeight(Constants.PriorityRecommendedItemWeight);
			this.itemqueueDao.save(itemqueue);
			queue.add(itemqueue);
		}
		return queue;
	}
	
	public List<ItemQueue> searchUserRecommendedItems(Users user) {
		List<ItemQueue> itemQueues = new ArrayList<ItemQueue>();
		Set<Item> results = this.searchRecommendedItems(user);
		if(results == null || results.size() == 0)
			return itemQueues;
		for (Item item : results) {
			ItemQueue iq = new ItemQueue();
			iq.setAuthor(user);
			iq.setCreateTime(new Date());
			iq.setDisabled(0);
			iq.setItem(item);
			iq.setStartTime(new Date());
			iq.setUpdateTime(new Date());
			iq.setWeight(Constants.PriorityRecommendedItemWeight);
			iq.setQueuetype(QuizConstants.QueueTypeRecommende);
			this.itemqueueDao.save(iq);
			itemQueues.add(iq);
		}
		return itemQueues;
	}

	public void analyzeLanguageAbility() {
		List<Users> users = this.userDao.getAll();
		for (Users user : users) {
			DetachedCriteria dc = DetachedCriteria
					.forClass(LanguageAbility.class);
			dc.add(Restrictions.eq("author", user));
			dc.add(Restrictions.eq("disabled", 0));
			List<LanguageAbility> las = this.languageAbilityDao.find(dc);
			for (LanguageAbility la : las) {
				la.setDisabled(1);
				this.languageAbilityDao.save(la);
			}

			Session session = this.sessionFactory.getCurrentSession();

			String lan_sql = "select count(*), language_id "
					+ "from t_myquiz q left join t_item i on 	q.item_id = i.id 	"
					+ "where q.author_id = '"
					+ user.getId()
					+ "' "
					+ "and q.author_id != i.author_id and q.answerstate != -1  "
					+ "and q.answerstate != 2 and q.language_id is not null "
					+ "group by language_id order by language_id";

			String state_sql = "select count(*), answerstate, language_id "
					+ "from t_myquiz q left join t_item i on 	q.item_id = i.id 	"
					+ "where q.author_id = '"
					+ user.getId()
					+ "' "
					+ "and q.author_id != i.author_id and q.answerstate != -1  "
					+ "and q.answerstate != 2 and q.language_id is not null "
					+ "group by answerstate, language_id order by language_id";

			Map<String, Integer> lan_map = new HashMap<String, Integer>();
			List<Object[]> results = session.createSQLQuery(lan_sql).list();
			for (Object[] objects : results) {
				if (objects[0] != null) {
					BigInteger num = (BigInteger) objects[0];
					String lan_id = (String) objects[1];
					if (num != null && num.intValue() > 15) {
						lan_map.put(lan_id, num.intValue());
					}
				}

			}

			if (lan_map.size() == 0)
				continue;

			Map<String, Map<Integer, Integer>> state_map = new HashMap<String, Map<Integer, Integer>>();

			List<Object[]> sate_results = session.createSQLQuery(state_sql)
					.list();
			for (Object[] objects : sate_results) {
				if (objects[0] != null) {
					BigInteger num = (BigInteger) objects[0];
					Integer state = (Integer) objects[1];
					String lan_id = (String) objects[2];
					Map<Integer, Integer> map = state_map.get(lan_id);
					if (map == null)
						map = new HashMap<Integer, Integer>();
					map.put(state.intValue(), num.intValue());
					state_map.put(lan_id, map);
				}
			}

			Set<String> keys = lan_map.keySet();
			for (String key : keys) {
				Language lan = this.languageDao.findUniqueBy("id", key);
				if (lan == null)
					continue;
				Integer total = lan_map.get(key);
				if (total == 0 || total == null)
					continue;
				Map<Integer, Integer> map = state_map.get(key);
				Integer right = 0;
				if (map.get(Constants.CorrectAnsweredState) != null)
					right = map.get(Constants.CorrectAnsweredState);
				if (map.get(Constants.EasyAnsweredState) != null)
					right += map.get(Constants.EasyAnsweredState);
				LanguageAbility ability = new LanguageAbility();
				ability.setAuthor(user);
				ability.setCreateTime(new Date());
				ability.setTotaltimes(total);
				ability.setRighttimes(right);
				ability.setLanguage(lan);
				ability.setDisabled(0);
				ability.setAbility((double) right / total);
				this.languageAbilityDao.save(ability);
			}

		}
	}

	// public void searchToStudyObject(){
	// Date start = new Date();
	//
	// Session session = null;
	// session = this.sessionFactory.getCurrentSession();
	// DetachedCriteria detachedCriteria =
	// DetachedCriteria.forClass(AccessLock.class);
	// detachedCriteria.setLockMode(LockMode.PESSIMISTIC_WRITE);
	// List<AccessLock> accesslocks = this.accessLockDao.find(detachedCriteria);
	// AccessLock accesslock = null;
	// if(accesslocks!=null&&accesslocks.size()>0)
	// accesslock = (AccessLock)accesslocks.get(0);
	//
	// if(accesslock==null){
	// accesslock = new AccessLock();
	// accesslock.setAccesskey(ItemQueueAccessLockKey);
	// accesslock.setState(Constants.AccessLocked);
	// accesslock.setUpdate_time(new Date());
	// this.accessLockDao.save(accesslock);
	// System.out.println("2 is locked");
	// }else if(Constants.AccessLocked.equals(accesslock.getState())){
	// return;
	// }else if(Constants.AccessUnLocked.equals(accesslock.getState())){
	// accesslock.setState(Constants.AccessLocked);
	// accesslock.setUpdate_time(new Date());
	// this.accessLockDao.save(accesslock);
	// }
	//
	//
	// //Get all users
	// List<Users> users = this.userDao.getAll();
	// // List<Users> users = this.userDao.findBy("id",
	// "ff80818125899fb501258acaadc10001");
	// try{
	//
	// for(Users user:users){
	// Setting setting = user.getSetting();
	// Integer wrongdays = null;
	// Integer rightdays = null;
	// Integer adddays = null;
	// if(setting!=null){
	// wrongdays = setting.getWrongdays();
	// rightdays = setting.getRightdays();
	// adddays = setting.getAdddays();
	// }
	// if(wrongdays==null)
	// wrongdays = DEFAULT_WRONG_FORGET_DAYS;
	// if(rightdays==null)
	// rightdays = DEFAULT_RIGHT_FORGET_DAYS;
	// if(adddays==null)
	// adddays = DEFAULT_AddObject_FORGET_DAYS;
	//
	// Calendar cal = Calendar.getInstance();
	// cal.set(Calendar.HOUR_OF_DAY, 12);
	// cal.set(Calendar.MINUTE, 0);
	// cal.set(Calendar.SECOND, 0);
	// cal.set(Calendar.MILLISECOND, 0);
	//
	// cal.add(Calendar.DATE, wrongdays);
	// Date wrongdate = cal.getTime();
	// cal.add(Calendar.DATE, -wrongdays);
	//
	// cal.add(Calendar.DATE, rightdays);
	// Date rightdate = cal.getTime();
	// cal.add(Calendar.DATE, -rightdays);
	//
	// cal.add(Calendar.DATE, adddays);
	// Date adddate = cal.getTime();
	// cal.add(Calendar.DATE, -adddays);
	//
	// String wrongsql = "select mq.* from t_myquiz mq, t_llquiz lq," +
	// "(select l.item_id, max(m.update_date) as update_date from t_myquiz m, t_llquiz l "+
	// "where m.author_id = '"+user.getId()+"' "+
	// "and m.answerstate !=-1  "+
	// "and m.llquiz_id = l.id "+
	// "group by l.item_id "+
	// "having max(m.update_date) < '"+FormatUtil.formatYYYYMMDD(wrongdate)+"' "+
	// ") y "+
	// "where mq.llquiz_id = lq.id "+
	// "and mq.update_date =y.update_date and mq.author_id = '"+user.getId()+"' "+
	// "and lq.item_id = y.item_id and mq.answerstate = 0 "+
	// "and not exists( "+
	// "select * "+
	// "from t_itemqueue it "+
	// "where it.item_id = y.item_id "+
	// "and it.author_id = '"+user.getId()+"' "+
	// "and it.disabled = 0 "+
	// ") ";
	// List<MyQuiz> wrongquizlist =
	// session.createSQLQuery(wrongsql).addEntity(MyQuiz.class).list();
	// for(MyQuiz wrongquiz:wrongquizlist){
	// ItemQueue itemqueue = new ItemQueue();
	// itemqueue.setItem(wrongquiz.getLlquiz().getItem());
	// itemqueue.setAuthor(user);
	// itemqueue.setCourseid(wrongquiz.getLlquiz().getCourseid());
	// itemqueue.setCreateTime(new Date());
	// itemqueue.setDisabled(0);
	// itemqueue.setWeight(Constants.PriorityHightWeight);
	// itemqueue.setQueuetype(Constants.QueueTypeWrongQuiz);
	// this.itemqueueDao.save(itemqueue);
	// }
	//
	// String rightsql = "select mq.* from t_myquiz mq, t_llquiz lq," +
	// "(select l.item_id, max(m.update_date) as update_date from t_myquiz m, t_llquiz l "+
	// "where m.author_id = '"+user.getId()+"' "+
	// "and m.answerstate !=-1  "+
	// "and m.llquiz_id = l.id "+
	// "group by l.item_id "+
	// "having max(m.update_date) < '"+FormatUtil.formatYYYYMMDD(rightdate)+"' "+
	// ") y "+
	// "where mq.llquiz_id = lq.id "+
	// "and mq.update_date =y.update_date and mq.author_id = '"+user.getId()+"' "+
	// "and lq.item_id = y.item_id and mq.answerstate = 1 "+
	// "and not exists( "+
	// "select * "+
	// "from t_itemqueue it "+
	// "where it.item_id = y.item_id "+
	// "and it.author_id = '"+user.getId()+"' "+
	// "and it.disabled = 0 "+
	// ") ";
	//
	// List<MyQuiz> rightquizlist =
	// session.createSQLQuery(rightsql).addEntity(MyQuiz.class).list();
	// for(MyQuiz rightquiz:rightquizlist){
	// String secondsearch = "select mq.* from t_myquiz mq, t_llquiz lq  " +
	// "where lq.item_id = '"+rightquiz.getLlquiz().getItem().getId()+"' and lq.id = mq.llquiz_id "
	// +
	// "and mq.author_id = '"+rightquiz.getAuthor().getId()+"' and mq.answerstate!=-1 "
	// +
	// "order by mq.update_date desc";
	//
	// List<MyQuiz> templist =
	// session.createSQLQuery(secondsearch).addEntity(MyQuiz.class).list();
	// int i = 0;
	// for(MyQuiz myquiz:templist){
	// if(Constants.CorrectAnsweredState.equals(myquiz.getAnswerstate())){
	// i++;
	// }else
	// break;
	// }
	// int days = Utility.getDifferDayTwoDays(new Date(),
	// rightquiz.getUpdateDate());
	//
	// if(days>Math.abs(DEFAULT_RIGHT_FORGET_DAYS*i)){
	// rightquiz.getUpdateDate();
	// ItemQueue itemqueue = new ItemQueue();
	// itemqueue.setItem(rightquiz.getLlquiz().getItem());
	// itemqueue.setAuthor(user);
	// itemqueue.setCourseid(rightquiz.getLlquiz().getCourseid());
	// itemqueue.setCreateTime(new Date());
	// itemqueue.setDisabled(0);
	// itemqueue.setWeight(Constants.PriorityHightWeight);
	// itemqueue.setQueuetype(Constants.QueueTypeRightQuiz);
	// this.itemqueueDao.save(itemqueue);
	// }
	// }
	//
	// String addsql = "select i.* from t_item i " +
	// "where i.create_time<'"+FormatUtil.formatYYYYMMDD(adddate)+"' " +
	// " and i.author_id= '"+user.getId()+"' and not exists(select * from t_myquiz m "
	// +
	// "where m.author_id='"+user.getId()+"' " +
	// "and m.llquiz_id in " +
	// "(select id from t_llquiz l where l.item_id = i.id)) "+
	// "and not exists ( "+
	// "select * "+
	// "from t_itemqueue iq "+
	// "where iq.item_id = i.id "+
	// "and iq.author_id= '"+user.getId()+"' "+
	// "and iq.disabled =0 "+
	// ") ";
	//
	// List<Language> lans = user.getStudyLangs();
	// String courseids = "";
	// if(lans!=null&&lans.size()>0){
	// for(Language lan:lans){
	// courseids = courseids
	// +CourseUtil.converCourseIdFromString(lan.getCode())+",";
	// }
	// courseids = courseids.substring(0, courseids.length()-1);
	// }
	// if(courseids.length()>0)
	// addsql = addsql +
	// "and exists( "+
	// "select * "+
	// "from t_llquiz lq "+
	// "where lq.item_id = i.id "+
	// "and lq.courseid in ("+courseids+") "+
	// ") ";
	//
	// List<Item> itemlist =
	// session.createSQLQuery(addsql).addEntity(Item.class).list();
	// for(Item item:itemlist){
	// ItemQueue itemqueue = new ItemQueue();
	// itemqueue.setItem(item);
	// itemqueue.setAuthor(user);
	// itemqueue.setCreateTime(new Date());
	// itemqueue.setDisabled(0);
	// itemqueue.setWeight(Constants.PriorityHightWeight);
	// itemqueue.setQueuetype(Constants.QueueTypeNewObject);
	// this.itemqueueDao.save(itemqueue);
	// }
	//
	//
	// String viewsql = "select i.* from t_item i, " +
	// "(select item_id from l_user_read_item " +
	// "where user_id =  '"+user.getId()+"' "+
	// "group by item_id order by count(*) desc ) y " +
	// "where i.id = y.item_id and i.author_id !=  '"+user.getId()+"' "+
	// "and not exists(select * from t_myquiz m, t_llquiz l " +
	// "where m.llquiz_id = l.id and l.item_id = i.id ) " +
	// "and not exists ( select * from t_itemqueue iq " +
	// "where iq.item_id = i.id and iq.author_id= '"+user.getId()+"' "+
	// "and iq.disabled =0) ";
	// if(courseids.length()>0)
	// viewsql = viewsql+"and exists(select * from t_llquiz lq " +
	// "where lq.item_id = i.id and lq.courseid in ("+courseids+") )";
	//
	//
	// List<Item> viewitemlist =
	// session.createSQLQuery(viewsql).addEntity(Item.class).list();
	// for(Item item:viewitemlist){
	// ItemQueue itemqueue = new ItemQueue();
	// itemqueue.setItem(item);
	// itemqueue.setAuthor(user);
	// itemqueue.setCreateTime(new Date());
	// itemqueue.setDisabled(0);
	// itemqueue.setWeight(Constants.PriorityMiddleWeight);
	// itemqueue.setQueuetype(Constants.QueueTypeViewObject);
	// this.itemqueueDao.save(itemqueue);
	// }
	// }
	//
	// }catch(Exception e){
	// e.printStackTrace();
	// }
	//
	// accesslock.setState(Constants.AccessUnLocked);
	// accesslock.setUpdate_time(new Date());
	// this.accessLockDao.save(accesslock);
	//
	// Date stop = new Date();
	// System.out.println("analyze cost "+(stop.getTime()-start.getTime())+" mseconds");
	// }

}
