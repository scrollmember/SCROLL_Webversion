package jp.ac.tokushima_u.is.ll.service;

import java.util.List;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.Notification;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotificationService {
	private HibernateDao<Notification, String> notificationDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		notificationDao = new HibernateDao<Notification, String>(
				sessionFactory, Notification.class);
	}

	@Transactional(readOnly = true)
	public List<Notification> getAll() {
		return this.notificationDao.getAll();
	}

	@Transactional(readOnly = true)
	public Notification get(String id) {
		return this.notificationDao.get(id);
	}
	
	

}
