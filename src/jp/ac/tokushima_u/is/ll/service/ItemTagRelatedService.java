
package jp.ac.tokushima_u.is.ll.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.ItemItemTag;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author mouri
 */
@Service
@Transactional
public class ItemTagRelatedService {

    @Autowired
    private GoogleTranslateService googleTranslateService;
	
	private HibernateDao<ItemItemTag, String> RelatedObjectDao;
	
	
	private Map<String, String>  _translateCache = new HashMap<String, String>();
	
	private SessionFactory sessionFactory;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		
		
		RelatedObjectDao = new HibernateDao<ItemItemTag, String>(sessionFactory, ItemItemTag.class);
	}



	/**
	 * getSUMO
	 * SUMO: Suggested Upper Merged Ontologyを得る（カテゴリ的な）
	 */
	
//	//t_item_item_tagsより関連するitemsを取得(距離1)
//	@SuppressWarnings("unchecked")
//	@Transactional(readOnly = true)
//	public List<String> getRelatedObject(String word) {
//		String sql = "SELECT DISTINCT "
//			+" items"
//			+" from t_item_item_tags"
//			+" WHERE NOT t_item_item_tags.items= :word AND t_item_item_tags.item_tags"
//			+" AND item_tags IN("
//			+" SELECT id"
//			+" from t_item_tag"
//			+" WHERE t_item_tag.id AND id IN("
//			+" SELECT item_tags"
//			+" from t_item_item_tags"
//			+" WHERE items = :word ))";
//		Session session = this.sessionFactory.getCurrentSession();
//		
//		//setParameter：第一引数はSQL文のwordを指しており第二引数はgetRelatedObject(String word)のwordを示している。
//		List<String> list = session.createSQLQuery(sql).setParameter("word", word).list();
//		
//		return list;
//	}

	//t_item_item_tagsより関連するitemsを取得(距離1)
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<String> getRelatedObject(String word) {
		String sql = "SELECT DISTINCT "
			+" items"
			+" from t_item_item_tags"
			+" WHERE NOT t_item_item_tags.items= :word AND t_item_item_tags.item_tags"
			+" AND item_tags IN("
			+" SELECT id"
			+" from t_item_tag"
			+" WHERE t_item_tag.id AND id IN("
			+" SELECT item_tags"
			+" from t_item_item_tags"
			+" WHERE items = :word ))"
			+" GROUP BY items"
			+" ORDER BY Count(items) DESC";
		Session session = this.sessionFactory.getCurrentSession();
		
		//setParameter：第一引数はSQL文のwordを指しており第二引数はgetRelatedObject(String word)のwordを示している。
		List<String> list = session.createSQLQuery(sql).setParameter("word", word).list();
		
		return list;
	}
	
	
	//t_item_item_tagsより関連するitemsを取得（距離2）
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<String> getRelatedObject2(String word) {
		String sql = "select distinct" 
		+" items"
		+" from t_item_item_tags"
		+" where NOT t_item_item_tags.items= :word AND item_tags IN("
		+" select distinct item_tags"
		+" from t_item_item_tags"
		+" where items IN("
		+" select distinct items"
		+" from t_item_item_tags"
		+" where t_item_item_tags.item_tags AND item_tags IN("
		+" select id"
		+" from t_item_tag"
		+" where t_item_tag.id AND id IN("
		+" select item_tags"
		+" from t_item_item_tags"
		+" where items = :word))))"
		+" GROUP BY items"
		+" ORDER BY Count(items) DESC"; 
		
		Session session = this.sessionFactory.getCurrentSession();
		
		//setParameter：第一引数はSQL文のwordを指しており第二引数はgetRelatedObject(String word)のwordを示している。
		List<String> list = session.createSQLQuery(sql).setParameter("word", word).list();
		
		return list;
	}

	
	
	
	
	//t_item_item_tagsよりitemsの数を取得
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<String> getCount(String word) {
		String sql = "SELECT COUNT(DISTINCT items)"
			+" from t_item_item_tags"
			+" WHERE NOT t_item_item_tags.items= :word AND t_item_item_tags.item_tags"
			+" AND item_tags IN("
			+" SELECT id"
			+" from t_item_tag"
			+" WHERE t_item_tag.id AND id IN("
			+" SELECT item_tags"
			+" from t_item_item_tags"
			+" WHERE items = :word ))";
		Session session = this.sessionFactory.getCurrentSession();
		
		//setParameter：第一引数はSQL文のwordを指しており第二引数はgetRelatedObject(String word)のwordを示している。
		List<String> list = session.createSQLQuery(sql).setParameter("word", word).list();
		
		return list;
	}

	//t_item_item_tagsよりitemsの数を取得
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<String> getRelatedItem(String word) {
		String sql = " SELECT content"
		+" from t_item_title"
		+" where item = :word";
		Session session = this.sessionFactory.getCurrentSession();
		
		//setParameter：第一引数はSQL文のwordを指しており第二引数はgetRelatedObject(String word)のwordを示している。
		List<String> list = session.createSQLQuery(sql).setParameter("word", word).list();
		
		return list;
	}
	
	
	

}







