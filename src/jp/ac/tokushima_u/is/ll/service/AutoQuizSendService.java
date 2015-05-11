package jp.ac.tokushima_u.is.ll.service;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.CycleTime;
import jp.ac.tokushima_u.is.ll.entity.SendTime;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.util.FormatUtil;
import jp.ac.tokushima_u.is.ll.util.Utility;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("autoQuizSendService")
@Transactional
public class AutoQuizSendService {

    private HibernateDao<SendTime, String> sendTimeDao;
    private HibernateDao<CycleTime, String> cycleTimeDao;
    private static int beforeStartMinute = -30;
    @Autowired
    UserService userService;
//    @Autowired
//    private HibernateDao<MyQuizLog, String> myquizlogDao;
//    @Autowired
//    private HibernateDao<MyQuiz, String> myquizDao;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        sendTimeDao = new HibernateDao<SendTime, String>(sessionFactory, SendTime.class);
        cycleTimeDao = new HibernateDao<CycleTime,String>(sessionFactory,CycleTime.class);
//        this.myquizlogDao = new HibernateDao<MyQuizLog,String>(sessionFactory, MyQuizLog.class);
//        this.myquizDao = new HibernateDao<MyQuiz, String>(sessionFactory, MyQuiz.class);
    }

    public List<SendTime> findUserSendTime(Users u){
    	
    	String deletesql = "delete from t_cycletime where author_id = '"+u.getId()+"' and checkday<'"+FormatUtil.formatYYYYMMDD(Utility.getYesterday())+"' ";
    	this.cycleTimeDao.getSession().createSQLQuery(deletesql);
    	
    	Users user = userService.getById(u.getId());
		Date today = Utility.getToday();
		DetachedCriteria criteria = DetachedCriteria.forClass(CycleTime.class);
		criteria.add(Restrictions.eq("checkday", today));
		criteria.add(Restrictions.eq("author", user));
		criteria.addOrder(Order.desc("checktime"));
		List<CycleTime> results = this.cycleTimeDao.find(criteria);
		Calendar cal = Calendar.getInstance();
		Time now = new Time(cal.getTimeInMillis());
		cal.add(Calendar.MINUTE, beforeStartMinute);
		Time from = new Time(cal.getTimeInMillis());
		
		if(results!=null&&results.size()>0)
			from = results.get(0).getChecktime();
		
		Integer weektype = cal.get(Calendar.DAY_OF_WEEK);
		DetachedCriteria sendtimecriteria = DetachedCriteria.forClass(SendTime.class);
		sendtimecriteria.add(Restrictions.eq("typ", weektype));
		sendtimecriteria.add(Restrictions.eq("author", user));
		sendtimecriteria.add(Restrictions.ge("sendtime", from));
		sendtimecriteria.add(Restrictions.lt("sendtime", now));
		List<SendTime> sendTimes = this.sendTimeDao.find(sendtimecriteria);
		
		
		CycleTime ct = new CycleTime();
		ct.setCheckday(today);
		ct.setChecktime(now);
		ct.setAuthor(user);
		this.cycleTimeDao.save(ct);
		
		return sendTimes;
    }
    
    
//    public MyQuizLog findLatestMyQuizLogByQuiz(LLQuiz llquiz, Users user){
//    	DetachedCriteria criteria = DetachedCriteria.forClass(MyQuiz.class);
//    	criteria.add(Restrictions.eq("author", user));
//    	criteria.add(Restrictions.eq("llquiz", llquiz));
//    	List<MyQuiz> myquizzes = this.myquizDao.find(criteria);
//    	if(myquizzes!=null&&myquizzes.size()>0){
//    		MyQuiz myquiz = myquizzes.get(0);
//    		DetachedCriteria logcriteria = DetachedCriteria.forClass(MyQuizLog.class);
//    		logcriteria.add(Restrictions.eq("myquiz", myquiz));
//    		logcriteria.addOrder(Order.desc("createDate"));
//    		List<MyQuizLog> myquizlogs = this.myquizlogDao.find(logcriteria);
//    		if(myquizlogs!=null&&myquizlogs.size()>0)
//    			return myquizlogs.get(0);
//    	}
//    	return null;
//    }
    
}
