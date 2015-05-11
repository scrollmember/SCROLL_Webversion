package jp.ac.tokushima_u.is.ll.service.task;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.service.LanguageService;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("titleMigrateTask")
@Transactional
public class TitleMigrateTask {
	@Autowired
	private LanguageService languageService;

	private HibernateDao<Item, String> itemDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		itemDao = new HibernateDao<Item, String>(sessionFactory, Item.class);
	}

	public boolean execute() {
//		Language english = languageService.findUniqueLangByCode("en");
//		Language japanese = languageService.findUniqueLangByCode("ja");
//		Language chinese = languageService.findUniqueLangByCode("zh");
//
//		List<Item> items = itemDao.getAll();
//		for (Item item : items) {
//			List<ItemTitle> titles = item.getTitles();
//			if (titles != null && titles.size() > 0)
//				continue;
//			titles.clear();
//			if (!StringUtils.isBlank(item.getEnTitle())) {
//				ItemTitle enTitle = new ItemTitle();
//				enTitle.setLanguage(english);
//				enTitle.setContent(item.getEnTitle());
//				enTitle.setItem(item);
//				titles.add(enTitle);
//			}
//
//			if (!StringUtils.isBlank(item.getJpTitle())) {
//				ItemTitle jpTitle = new ItemTitle();
//				jpTitle.setLanguage(japanese);
//				jpTitle.setContent(item.getJpTitle());
//				jpTitle.setItem(item);
//				titles.add(jpTitle);
//			}
//
//			if (!StringUtils.isBlank(item.getZhTitle())) {
//				ItemTitle zhTitle = new ItemTitle();
//				zhTitle.setLanguage(chinese);
//				zhTitle.setContent(item.getZhTitle());
//				zhTitle.setItem(item);
//				titles.add(zhTitle);
//			}
//			itemDao.save(item);
//		}
		return true;
	}
}
