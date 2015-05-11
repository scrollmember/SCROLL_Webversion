package jp.ac.tokushima_u.is.ll.visualization;

import java.util.Date;
import java.util.List;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.ItemState;
import jp.ac.tokushima_u.is.ll.entity.MyQuiz;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.service.UserService;
import jp.ac.tokushima_u.is.ll.util.Constants;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author li
 */
@Service("reviewHistoryService")
@Transactional
public class ReviewHistoryService {
	
	    private HibernateDao<Item, String> itemDao;
	    private HibernateDao<Users, String> userDao;
	    private HibernateDao<ItemState, String> itemStateDao;
	    private SessionFactory sessionFactory;
	    @Autowired
	    private UserService userService;
	    
		@Autowired
		public void setSessionFactory(SessionFactory sessionFactory) {
			this.sessionFactory = sessionFactory;
			itemDao = new HibernateDao<Item, String>(sessionFactory, Item.class);
			userDao = new HibernateDao<Users, String>(sessionFactory, Users.class);
			itemStateDao = new HibernateDao<ItemState, String>(sessionFactory, ItemState.class);
			this.sessionFactory = sessionFactory;
		}
	    
	    public MyQuiz findItemState() {
	    	List<Users> users = userService.findAllUsersByCondition("accountNonLocked", Boolean.TRUE);
//	    	List<Users> users = userService.findAllUsersByCondition("id", "ff80818125899fb501258acaadc10001");
	    	for(Users user:users){
	    		List<ItemState> states = itemStateDao.findBy("user", user);
	    		if(states.size()>0)
	    			continue;
	    		searchUserItemHistory(user.getId());
	    	}
	    	
	    	return null;
	    }
	    
	    public ItemState searchUserItemState(Users user, Item item){
	    	DetachedCriteria dc = DetachedCriteria.forClass(ItemState.class); 
	    	dc.add(Restrictions.eq("user", user));
//	    	dc.add(Restrictions.eq("item", itemDao.findUniqueBy("id", itemId)));
	    	dc.add(Restrictions.eq("item", item));
	    	List<ItemState> isList = this.itemStateDao.find(dc);
	    	if(isList != null && isList.size()>0){
	    		return isList.get(0);
	    	}else{
	    		return null;
	    	}
	    }
	    
	    public void updateItemState(Item item, Users user, Integer experState, Integer quizAnswerState){
	    	DetachedCriteria dc = DetachedCriteria.forClass(ItemState.class); 
	    	dc.add(Restrictions.eq("user", user));
	    	dc.add(Restrictions.eq("item", item));
	    	List<ItemState> isList = this.itemStateDao.find(dc);
	    	ItemState is = null;
	    	String quizState = "";
	    	if(isList != null && isList.size()>0){
	    		is = isList.get(0);
	    		quizState = is.getQuizState();
	    	}else{
	    		is = new ItemState();
	    		is.setItem(item);
	    		is.setUser(user);
	    		is.setCreateDate(new Date());
	    	}
	    
	    	is.setUpdateDate(new Date());
	    	if(experState != null)
	    		is.setExperState(experState);
	    	if(quizAnswerState != null){
	    		if(quizState!=null && quizState.length()>0)
		    		quizState = quizAnswerState.toString()+", "+quizState;
		    	else
		    		quizState = quizAnswerState.toString();
		    	
	    		is.setQuizState(quizState);
	    		is.setRememberState(quizAnswerState);
	    	}
	    	is.setUpdateDate(new Date());
	    	itemStateDao.save(is);
	    }
	   
	    
	    public List<ItemState> findUserItemState(Users user, Integer experState, Integer rememberState){
	    	DetachedCriteria dc = DetachedCriteria.forClass(ItemState.class); 
	    	dc.add(Restrictions.eq("user", user));
	    	dc.add(Restrictions.eq("experState", experState));
	    	dc.add(Restrictions.eq("rememberState", rememberState));
	    	return itemStateDao.find(dc);
	    }
	    
	    public void searchUserItemHistory(String authorId){
	    	String sql = "select item_id, q.author_id, i.author_id as itemauthor, group_concat(conv(oct(answerstate),8,10) order by update_date desc ) as state, max(q.update_date) as update_date " +
	    			"from t_myquiz q, t_item i " +
	    			"where q.answerstate != -1 " +
	    			"and q.answerstate != -1 and q.item_id = i.id  and q.item_id is not null ";
	    	String group_sql = " group by q.item_id order by q.item_id ";
	    	if(authorId != null && authorId.length()>0)
	    		sql = sql +" and q.author_id = '"+authorId+"' ";
//	    	if(itemId != null && itemId.length()>0)
//	    		sql = sql +" and i.id = '"+itemId+"' ";
	    	sql = sql + group_sql;
	    	
	    	Session session = this.sessionFactory.getCurrentSession();
//			List<ItemState> itemList = session.createSQLQuery(sql).addEntity(ItemState.class).list();
			@SuppressWarnings("rawtypes")
			List itemList = session.createSQLQuery(sql).list();
			for(Object itemState:itemList){
				Object[] items = (Object[])itemState;
				if(items != null && items.length == 5){
					ItemState state = new ItemState();
					state.setItem(itemDao.findUniqueBy("id", items[0]));
					state.setUser(userDao.findUniqueBy("id", items[1]));
					if(items[1].equals(items[2]))
						state.setExperState(1);
					else
						state.setExperState(0);
					
					Integer answerState = analyzeQuizState((String)items[3]);
					state.setRememberState(answerState);
					state.setQuizState((String)items[3]);
					state.setCreateDate(new Date());
					state.setUpdateDate((Date)items[4]);
					
					itemStateDao.save(state);
				}
			}
//			session.close();
	    }

	    private Integer analyzeQuizState(String state){
	    	Integer result = Constants.NotAnsweredState;
	    	if(state!=null&&state.length()>0){
	    		try{
	    			String[] states = state.split(",");
	    			Integer s = Integer.valueOf(states[0]);
	    			if(Constants.WrongAnsweredState.equals(s))
	    				return Constants.WrongAnsweredState;
	    			else if(Constants.EasyAnsweredState.equals(s))
	    				return Constants.EasyAnsweredState;
	    			else if(Constants.DifficultAnsweredState.equals(s))
	    				return Constants.DifficultAnsweredState;
	    			else if(Constants.PassAnsweredState.equals(s))
	    				return Constants.PassAnsweredState;
	    			else if(Constants.CorrectAnsweredState.equals(s)){
	    				return Constants.CorrectAnsweredState;
	    			}
	    		}catch(Exception e){
	    			e.printStackTrace();
	    		}
	    	}
	    	return result;
	    }
}
