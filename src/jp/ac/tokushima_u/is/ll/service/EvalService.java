package jp.ac.tokushima_u.is.ll.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.ItemTitle;
import jp.ac.tokushima_u.is.ll.entity.LogUserReadItem;
import jp.ac.tokushima_u.is.ll.entity.MyQuiz;
import jp.ac.tokushima_u.is.ll.entity.Users;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

@Service
@Transactional(readOnly = true)
public class EvalService {
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	private HibernateDao<Users, String> usersDao;
	private HibernateDao<LogUserReadItem, String> logUserReadItemDao;
	private HibernateDao<MyQuiz, String> myQuizDao;
	
	@Autowired
	private ItemService itemService;
	
	private SessionFactory sessionFactory;
	
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		usersDao = new HibernateDao<Users, String>(sessionFactory, Users.class);
		logUserReadItemDao = new HibernateDao<LogUserReadItem, String>(sessionFactory,
				LogUserReadItem.class);
		myQuizDao = new HibernateDao<MyQuiz, String>(sessionFactory,
				MyQuiz.class);
	}
	
	public List<Map<String, Object>> findWordlist(Date dateFrom, Date dateTo) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Users> usersList = usersDao.getAll();
		
		for(Users user: usersList){
			//Search createdItems
			List<Item> myItems = itemService.searchMyItem(user);
			
			for(Item item: myItems){
				if(item.getCreateTime()==null || item.getCreateTime().before(dateFrom) || item.getCreateTime().after(dateTo))continue;
				
				if(item.getTitles()==null || item.getTitles().size()==0)continue;
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("userId", user.getId());
				map.put("userEmail", user.getPcEmail());
				map.put("type", "create");
				map.put("actionDate", item.getCreateTime());
				map.put("username", user.getFirstName()+" "+user.getLastName());
				map.put("title", toJsonString(item.getTitles()));
				map.put("createTime", item.getCreateTime());
				if(item.getAuthor()!=null){
					map.put("authorEmail", item.getAuthor().getPcEmail());
				}
				if(item.getAuthor()!=null){
					map.put("author", item.getAuthor().getFirstName()+" "+item.getAuthor().getLastName());
				}else{
					map.put("author", "");
				}
				result.add(map);
			}
			
			//Search readItems
			List<LogUserReadItem> readLog = logUserReadItemDao.findBy("user", user);
			
			for(LogUserReadItem log: readLog){
				Item item = log.getItem();
				if(log.getCreateTime()==null || log.getCreateTime().before(dateFrom) || log.getCreateTime().after(dateTo))continue;
				if(item.getTitles()==null || item.getTitles().size()==0)continue;
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("userId", user.getId());
				map.put("userEmail", user.getPcEmail());
				map.put("type", "read");
				map.put("actionDate", log.getCreateTime());
				map.put("username", user.getFirstName()+" "+user.getLastName());
				map.put("title", toJsonString(item.getTitles()));
				map.put("createTime", item.getCreateTime());
				if(item.getAuthor()!=null){
					map.put("authorEmail", item.getAuthor().getPcEmail());
				}
				if(item.getAuthor()!=null){
					map.put("author", item.getAuthor().getFirstName()+" "+item.getAuthor().getLastName());
				}else{
					map.put("author", "");
				}
				result.add(map);
			}
			
			//SearchQuizItems
//			List<MyQuiz> quizList = myQuizDao.findBy("author", user);
			List<MyQuiz> quizList = myQuizDao.find("from MyQuiz mq where mq.author=? and mq.answerstate=1", user);
			
			for(MyQuiz quiz: quizList){
				Item item = null;
				if(quiz.getCreateDate()==null || quiz.getCreateDate().before(dateFrom) || quiz.getCreateDate().after(dateTo))continue;
				if(quiz.getItem()!=null){
					item = quiz.getItem();
				}else{
					continue;
				}
				if(item.getTitles()==null || item.getTitles().size()==0)continue;
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("userId", user.getId());
				map.put("userEmail", user.getPcEmail());
				map.put("type", "quiz");
				map.put("actionDate", quiz.getCreateDate());
				map.put("username", user.getFirstName()+" "+user.getLastName());
				map.put("title", toJsonString(item.getTitles()));
				map.put("createTime", item.getCreateTime());
				if(item.getAuthor()!=null){
					map.put("authorEmail", item.getAuthor().getPcEmail());
				}
				if(item.getAuthor()!=null){
					map.put("author", item.getAuthor().getFirstName()+" "+item.getAuthor().getLastName());
				}else{
					map.put("author", "");
				}
				result.add(map);
			}
		}
		//Sorting
		Collections.sort(result, new Comparator<Map<String, Object>>(){
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				if(o1==null){
					return -1;
				}else if(o2 == null){
					return 1;
				}else{
					if(o1.get("userEmail").equals(o2.get("userEmail"))){
						if(o1.get("title").toString().equals(o2.get("title").toString())){
							if(o1.get("type").toString().equals(o2.get("type").toString())){
								return ((Date)o1.get("actionDate")).compareTo((Date)o2.get("actionDate"));
							}else{
								return o1.get("type").toString().compareTo(o2.get("type").toString());
							}
						}else{
							return o1.get("title").toString().compareTo(o2.get("title").toString());
						}
					}else{
						return o1.get("userEmail").toString().compareTo(o2.get("userEmail").toString());
					}
				}
			}
		});
		return result;
	}
	
	
	private String toJsonString(List<ItemTitle> titles) {
		Map<String, String> titleMap = new HashMap<String, String>();
		for(ItemTitle title: titles){
			titleMap.put(title.getLanguage().getCode(), title.getContent());
		}
		Gson gson = new Gson();
		return gson.toJson(titleMap).toString();
	}
	
	public Map<String, Map<String, Integer>> findUseStat(List<Users> userList, Date startDate){
		Map<String, Map<String, Integer>> result = new HashMap<String, Map<String, Integer>>();
		for(Users user: userList){
			result.put(user.getPcEmail()+"_C", new HashMap<String, Integer>());//Create
			result.put(user.getPcEmail()+"_R", new HashMap<String, Integer>());//Read
			result.put(user.getPcEmail()+"_Q", new HashMap<String, Integer>());//Quiz
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		Date current = new Date();
		while(current.after(cal.getTime())){
			String dayItemHql = "select new map(count(item) as itemNum, item.author as author) " +
					"from Item item " +
					"where year(item.createTime)=year(:date) " +
					"and month(item.createTime)=month(:date) " +
					"and day(item.createTime)=day(:date) " +
					"group by item.author";
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(dayItemHql);
			query.setDate("date", cal.getTime());
			List<?> dayItem = query.list();
			for(Object o: dayItem){
				@SuppressWarnings("unchecked")
				Map<String, Object> iMap = (Map<String, Object>)o;
				int num = 0;
				if(iMap.get("itemNum")!=null){
					num = ((Long)iMap.get("itemNum")).intValue();
				}
				result.get(((Users)iMap.get("author")).getPcEmail()+"_C").put(dateFormat.format(cal.getTime()), num);
			}
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}
		
		cal.setTime(startDate);
		while(current.after(cal.getTime())){
			String dayItemHql = "select new map(count(item) as itemNum, item.user as author) " +
					"from LogUserReadItem item " +
					"where year(item.createTime)=year(:date) " +
					"and month(item.createTime)=month(:date) " +
					"and day(item.createTime)=day(:date) " +
					"group by item.user";
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(dayItemHql);
			query.setDate("date", cal.getTime());
			List<?> dayItem = query.list();
			for(Object o: dayItem){
				@SuppressWarnings("unchecked")
				Map<String, Object> iMap = (Map<String, Object>)o;
				int num = 0;
				if(iMap.get("itemNum")!=null){
					num = ((Long)iMap.get("itemNum")).intValue();
				}
				if(null!=result.get(((Users)iMap.get("author")).getPcEmail()+"_R")){
					result.get(((Users)iMap.get("author")).getPcEmail()+"_R").put(dateFormat.format(cal.getTime()), num);
				}
			}
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}
		
		cal.setTime(startDate);
		while(current.after(cal.getTime())){
			String dayItemHql = "select new map(count(item) as itemNum, item.author as author) " +
					"from MyQuiz item " +
					"where year(item.createDate)=year(:date) " +
					"and month(item.createDate)=month(:date) " +
					"and day(item.createDate)=day(:date) " +
					"and item.answerstate<>-1 " +
					"group by item.author";
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(dayItemHql);
			query.setDate("date", cal.getTime());
			List<?> dayItem = query.list();
			for(Object o: dayItem){
				@SuppressWarnings("unchecked")
				Map<String, Object> iMap = (Map<String, Object>)o;
				int num = 0;
				if(iMap.get("itemNum")!=null){
					num = ((Long)iMap.get("itemNum")).intValue();
				}
				if(null!=result.get(((Users)iMap.get("author")).getPcEmail()+"_Q")){
					result.get(((Users)iMap.get("author")).getPcEmail()+"_Q").put(dateFormat.format(cal.getTime()), num);
				}
			}
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}
		return result;
	}
	
	public Map<String, Map<String, Integer>> findUseStatWeek(
			List<Users> userList, Calendar startCal) {
		
		Map<String, Map<String, Integer>> result = new HashMap<String, Map<String, Integer>>();
		for(Users user: userList){
			result.put(user.getPcEmail()+"_C", new HashMap<String, Integer>());//Create
			result.put(user.getPcEmail()+"_R", new HashMap<String, Integer>());//Read
			result.put(user.getPcEmail()+"_Q", new HashMap<String, Integer>());//Quiz
		}
		
		Calendar cal = (Calendar)startCal.clone();
		Date current = new Date();
		while(current.after(cal.getTime())){
			String dayItemHql = "select new map(count(item) as itemNum, item.author as author) " +
					"from Item item " +
					"where item.createTime between :start and :end "+
					"group by item.author";
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(dayItemHql);
			query.setDate("start", cal.getTime());
			Calendar end = (Calendar)cal.clone();
			end.add(Calendar.DAY_OF_YEAR, 7);
			query.setDate("end", end.getTime());
			List<?> dayItem = query.list();
			for(Object o: dayItem){
				@SuppressWarnings("unchecked")
				Map<String, Object> iMap = (Map<String, Object>)o;
				int num = 0;
				if(iMap.get("itemNum")!=null){
					num = ((Long)iMap.get("itemNum")).intValue();
				}
				result.get(((Users)iMap.get("author")).getPcEmail()+"_C").put(dateFormat.format(cal.getTime()), num);
			}
			cal.add(Calendar.DAY_OF_YEAR, 7);
		}
		
		cal = (Calendar)startCal.clone();
		while(current.after(cal.getTime())){
			String dayItemHql = "select new map(count(item) as itemNum, item.user as author) " +
					"from LogUserReadItem item " +
					"where item.createTime between :start and :end "+
					"group by item.user";
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(dayItemHql);
			query.setDate("start", cal.getTime());
			Calendar end = (Calendar)cal.clone();
			end.add(Calendar.DAY_OF_YEAR, 7);
			query.setDate("end", end.getTime());
			List<?> dayItem = query.list();
			for(Object o: dayItem){
				@SuppressWarnings("unchecked")
				Map<String, Object> iMap = (Map<String, Object>)o;
				int num = 0;
				if(iMap.get("itemNum")!=null){
					num = ((Long)iMap.get("itemNum")).intValue();
				}
				if(null!=result.get(((Users)iMap.get("author")).getPcEmail()+"_R")){
					result.get(((Users)iMap.get("author")).getPcEmail()+"_R").put(dateFormat.format(cal.getTime()), num);
				}
			}
			cal.add(Calendar.DAY_OF_YEAR, 7);
		}
		
		cal = (Calendar)startCal.clone();
		while(current.after(cal.getTime())){
			String dayItemHql = "select new map(count(item) as itemNum, item.author as author) " +
					"from MyQuiz item " +
					"where item.createDate between :start and :end "+
					"and item.answerstate<>-1 " +
					"group by item.author";
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(dayItemHql);
			query.setDate("start", cal.getTime());
			Calendar end = (Calendar)cal.clone();
			end.add(Calendar.DAY_OF_YEAR, 7);
			query.setDate("end", end.getTime());
			List<?> dayItem = query.list();
			for(Object o: dayItem){
				@SuppressWarnings("unchecked")
				Map<String, Object> iMap = (Map<String, Object>)o;
				int num = 0;
				if(iMap.get("itemNum")!=null){
					num = ((Long)iMap.get("itemNum")).intValue();
				}
				if(null!=result.get(((Users)iMap.get("author")).getPcEmail()+"_Q")){
					result.get(((Users)iMap.get("author")).getPcEmail()+"_Q").put(dateFormat.format(cal.getTime()), num);
				}
			}
			cal.add(Calendar.DAY_OF_YEAR, 7);
		}
		
		return result;
	}

	public List<Users> findUsersHaveItemsFrom(Date startDate) {
		String hql = "from Users u where maxelement(u.myItemList)>=?";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql);
		query.setDate(0, startDate);
		@SuppressWarnings("unchecked")
		List<Users> userList = query.list();
		return userList;
	}

	public Date findStartDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -3);
		String hql = "from Item item order by item.createTime asc";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql);
		query.setMaxResults(1);
		Item item = (Item)query.uniqueResult();
		Date startDate = cal.getTime();
		if(item!=null && startDate.before(item.getCreateTime())){
			startDate = item.getCreateTime();
		}
		return startDate;
	}
}
