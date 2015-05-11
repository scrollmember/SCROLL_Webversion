package jp.ac.tokushima_u.is.ll.service;

import java.util.List;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.ItemTag;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItemTagService {

	private HibernateDao<ItemTag, String> itemTagDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		itemTagDao = new HibernateDao<ItemTag, String>(sessionFactory, ItemTag.class);
	}

	public List<ItemTag> search(String q) {
		return itemTagDao.find("from ItemTag t where t.tag like ?", q+"%");
	}
}
