package jp.ac.tokushima_u.is.ll.service;

import java.util.Date;
import java.util.List;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.ItemQueue;
import jp.ac.tokushima_u.is.ll.entity.LogLogin;
import jp.ac.tokushima_u.is.ll.entity.LogUserReadItem;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.quiz.QuizConstants;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Houbin
 */
@Service
@Transactional
public class LogService {

    private HibernateDao<LogUserReadItem, String> logUserReadItemDao;
	private HibernateDao<LogLogin, String> logLoginDao;
	private HibernateDao<Item, String> itemDao;
	private HibernateDao<Users, String> usersDao;
	private HibernateDao<ItemQueue,String> itemQueueDao;
	
	@Autowired
	private ItemQueueService itemQueueService;
	
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        logUserReadItemDao = new HibernateDao<LogUserReadItem, String>(sessionFactory, LogUserReadItem.class);
        logLoginDao = new HibernateDao<LogLogin, String>(sessionFactory, LogLogin.class);
        itemDao = new HibernateDao<Item, String>(sessionFactory, Item.class);
        usersDao = new HibernateDao<Users, String>(sessionFactory, Users.class);
        itemQueueDao = new HibernateDao<ItemQueue, String>(sessionFactory, ItemQueue.class);
    }

    public void logUserReadItem(Item item, Users user, Double latitude, Double longitude, Float speed){
        LogUserReadItem logUserReadItem = new LogUserReadItem();
        logUserReadItem.setItem(itemDao.get(item.getId()));
        logUserReadItem.setUser(usersDao.get(user.getId()));
        logUserReadItem.setCreateTime(new Date(System.currentTimeMillis()));
        logUserReadItem.setLatitude(latitude);
        logUserReadItem.setLongitude(longitude);
        logUserReadItem.setSpeed(speed);
        logUserReadItemDao.save(logUserReadItem);
        
    	DetachedCriteria dc = DetachedCriteria.forClass(LogUserReadItem.class);
    	dc.add(Restrictions.eq("user", user));
    	dc.add(Restrictions.eq("item", item));
    	List<LogUserReadItem> readlist = this.logUserReadItemDao.find(dc);
    	int count = readlist.size();
    	DetachedCriteria iq_dc = DetachedCriteria.forClass(ItemQueue.class);
    	iq_dc.add(Restrictions.eq("author", user));
    	iq_dc.add(Restrictions.eq("item", item));
    	List<ItemQueue> queueList = this.itemQueueDao.find(iq_dc);
    	if(count>2&&(queueList==null||queueList.size()==0)){
    		this.itemQueueService.updateItemQueue(itemDao.get(item.getId()), usersDao.get(user.getId()), QuizConstants.QueueTypeViewObject);
//    		ItemQueue iq = new ItemQueue();
//    		iq.setAuthor(user);
//    		iq.setItem(item);
//    		iq.setCreateTime(new Date());
//    		iq.set
    	}
    }
    
    public void logUserLogin(Users user, LogLogin.Device device){
    	LogLogin logLogin = new LogLogin();
    	logLogin.setLoginDevice(device);
    	logLogin.setUser(usersDao.get(user.getId()));
    	logLogin.setLoginTime(new Date(System.currentTimeMillis()));
    	logLoginDao.save(logLogin);
    }
}
