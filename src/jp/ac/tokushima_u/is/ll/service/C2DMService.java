package jp.ac.tokushima_u.is.ll.service;

import java.util.Date;
import java.util.List;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.C2DMCode;
import jp.ac.tokushima_u.is.ll.entity.C2DMConfig;
import jp.ac.tokushima_u.is.ll.entity.Users;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("c2dmService")
@Transactional
public class C2DMService {

	private HibernateDao<C2DMCode,String> c2dmCodeDao;
	private HibernateDao<C2DMConfig,String> c2dmConfigDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.c2dmCodeDao = new HibernateDao<C2DMCode,String>(sessionFactory, C2DMCode.class);
		this.c2dmConfigDao = new HibernateDao<C2DMConfig,String>(sessionFactory, C2DMConfig.class);
	}

//	public void test1(){
//		Query q = this.c2dmConfigDao.createQuery("from C2DMCode", new Object[]{});
//		q.setLockMode("test1", LockMode.UPGRADE_NOWAIT);
//		C2DMConfig tmp = this.c2dmConfigDao.findUniqueBy("id", 1);
//		System.out.println("test1       "+tmp.getAuthToken());
//	}
//	
//	public void test2(){
//		 C2DMConfig tmp = this.c2dmConfigDao.findUniqueBy("id", 1);
//		 System.out.println("test2       "+tmp.getAuthToken());
//	}
	
	public C2DMConfig findC2DMConfig(Integer key){
		return this.c2dmConfigDao.findUniqueBy("id", key);
	}
	
	public void saveC2DMConfig(C2DMConfig config){
		this.c2dmConfigDao.save(config);
	}
	
	public List<C2DMCode> findCodeList(Users author){
		DetachedCriteria criteria = DetachedCriteria.forClass(C2DMCode.class);
		criteria.add(Restrictions.eq("author", author));
		return this.c2dmCodeDao.find(criteria);
	}
	
	public void updateC2DMCode(C2DMCode code){
		DetachedCriteria criteria = DetachedCriteria.forClass(C2DMCode.class);
		criteria.add(Restrictions.eq("author", code.getAuthor()));
		criteria.add(Restrictions.eq("imsi", code.getImsi()));
		List<C2DMCode> codes = this.c2dmCodeDao.find(criteria);
		if(codes!=null&&codes.size()>0){
			for(C2DMCode tempcode:codes){
				tempcode.setRegisterId(code.getRegisterId());
				tempcode.setUpdateTime(new Date());
				this.c2dmCodeDao.save(tempcode);
			}
		}
	}
	
}
