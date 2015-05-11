package jp.ac.tokushima_u.is.ll.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.LogSendMail;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LogSendMailService {
	private HibernateDao<LogSendMail, String> logSendMailDao;

	private static final String WEEKLY_NOTIFICATION_ID = "weeklyNotification";

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		logSendMailDao = new HibernateDao<LogSendMail, String>(sessionFactory,
				LogSendMail.class);
	}

	@Transactional(readOnly = true)
	public boolean findIsWeeklyNotificationSent(String pcEmail) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LogSendMail.class);
		
		Calendar startCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		startCal.add(Calendar.DAY_OF_YEAR, -2);
		endCal.add(Calendar.DAY_OF_YEAR, +2);
		criteria.add(Restrictions.between("sendTime", startCal.getTime(), endCal.getTime()));
		criteria.add(Restrictions.eq("address", pcEmail));
		criteria.add(Restrictions.eq("sendId", WEEKLY_NOTIFICATION_ID));
		List<LogSendMail> list = logSendMailDao.find(criteria);

		return list.size()>0;
	}

	public void saveSendWeeklyNotification(String pcEmail) {
		LogSendMail log = new LogSendMail();
		log.setAddress(pcEmail);
		log.setSendId(WEEKLY_NOTIFICATION_ID);
		log.setSendTime(new Date());
		logSendMailDao.save(log);
	}
}
