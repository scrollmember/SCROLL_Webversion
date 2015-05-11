package jp.ac.tokushima_u.is.ll.service;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.Interest;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InterestService {
	
	private HibernateDao<Interest, String> interestDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		interestDao = new HibernateDao<Interest, String>(sessionFactory, Interest.class);
	}
	
	public Interest findOrAdd(String name){
		if(StringUtils.isBlank(name)) return null;
		Interest interest = interestDao.findUniqueBy("name", name.trim());
		if(interest==null){
			interest = new Interest();
			interest.setName(name);
			interestDao.save(interest);
		}
		return interest;
	}
}
