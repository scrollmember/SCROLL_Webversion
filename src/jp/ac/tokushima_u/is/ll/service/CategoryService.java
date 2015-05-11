package jp.ac.tokushima_u.is.ll.service;

import java.util.List;
import java.util.Set;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.Category;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.form.CategoryEditForm;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryService {

	private HibernateDao<Category, String> categoryDao;
	private HibernateDao<Item, String> itemDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		categoryDao = new HibernateDao<Category, String>(sessionFactory,
				Category.class);
		itemDao = new HibernateDao<Item, String>(sessionFactory, Item.class);
	}

	@Transactional(readOnly = true)
	public List<Category> findRoots() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Category.class);
		criteria.add(Restrictions.isNull("parent"));
		List<Category> result = categoryDao.find(criteria);
		return result;
	}

	public Category createByForm(CategoryEditForm form, Category parent) {
		Category category = new Category();
		category.setName(form.getName());
		category.setNote(category.getNote());
		if(parent==null){
			category.setParent(null);
		}else{
			Category p = categoryDao.get(parent.getId());
			category.setParent(p);
		}
		categoryDao.save(category);
		return category;
	}

	@Transactional(readOnly = true)
	public Category findById(String id) {
		return categoryDao.get(id);
	}

	@Transactional(readOnly = true)
	public List<Category> findByName(String name, Category parent) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Category.class);
		criteria.add(Restrictions.eq("name", name));
		if(parent!=null){
			criteria.add(Restrictions.eq("parent", parent));
		}
		return categoryDao.find(criteria);
	}

	public void delete(Category category) {
		Category c = categoryDao.get(category.getId());
		categoryDao.delete(c);
	}
	
	@Transactional(readOnly = true)
	public Set<Category> getAllSubCategories(Category category){
		return loadAllSubCategories(category);
	}
	
	private Set<Category> loadAllSubCategories(
			Category category) {
		Set<Category> catSet = category.getChildren();
		for(Category cat:catSet){
			catSet.addAll(loadAllSubCategories(cat));
		}
		return catSet;
	}
	
	@Transactional(readOnly = true)
	public int sizeOfCategory(Category category){
		Set<Category> allSubCat = getAllSubCategories(category);
		int size = itemDao.findBy("category", category).size();
		for(Category cat:allSubCat){
			size+=itemDao.findBy("category", cat).size();
		}
		return size;
	}
}