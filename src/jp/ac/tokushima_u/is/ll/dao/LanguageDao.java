package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.Language;

public interface LanguageDao {

	List<Language> findAll();
	Language findByCode(String string);
	Language findById(Language language);

	void insert(Language language);
	int update(Language language);
	int delete(String id);
	
	List<Language> findListUsersStudyLangs(String userId);
	List<Language> findListUsersMyLangs(String userId);
}
