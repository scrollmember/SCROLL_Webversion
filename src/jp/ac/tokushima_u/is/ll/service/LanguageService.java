package jp.ac.tokushima_u.is.ll.service;

import java.util.List;
import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.Language;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Hou Bin
 */
@Service
@Transactional
public class LanguageService {

    private HibernateDao<Language, String> languageDao;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        languageDao = new HibernateDao<Language, String>(sessionFactory, Language.class);
    }

    public List<Language> searchAllLanguage() {
        List<Language> languageList = languageDao.getAll("name", true);

        //TODO
        if (languageList == null || languageList.size() == 0) {
            Language lang1 = new Language();
            lang1.setCode("en");
            lang1.setName("English");

            Language lang2 = new Language();
            lang2.setCode("ja");
            lang2.setName("Japanese");

            Language lang3 = new Language();
            lang3.setCode("zh");
            lang3.setName("Chinese");
            this.languageDao.save(lang1);
            this.languageDao.save(lang2);
            this.languageDao.save(lang3);
            languageList = this.languageDao.getAll("name", true);
        }


        return languageList;
    }

    @Transactional(readOnly = true)
    public List<Language> searchAllLanguageOrderedBy(String property, boolean isAsc) {
        return languageDao.getAll(property, isAsc);
    }

    @Transactional(readOnly = true)
    public Language findUniqueLangByCode(String code) {
        return languageDao.findUniqueBy("code", code);
    }

    @Transactional(readOnly = true)
	public Language findById(String id) {
		return languageDao.get(id);
	}
    
    public List<Language> findALl(){
    	return this.languageDao.getAll();
    }

	public void createByAdmin(Language language) {
		language.setCode(language.getCode().toLowerCase());
		languageDao.save(language);
	}

	public void editByAdmin(Language language) {
		language.setCode(language.getCode().toLowerCase());
		languageDao.save(language);
	}

	public void delete(String id) {
		languageDao.delete(id);
	}
}
