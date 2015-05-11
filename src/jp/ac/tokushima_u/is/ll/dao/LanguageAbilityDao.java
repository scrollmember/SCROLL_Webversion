package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.entity.LanguageAbility;

import org.apache.ibatis.annotations.MapKey;

public interface LanguageAbilityDao {

	List<LanguageAbility> findListByAuthorId(String authorId);

	int update(LanguageAbility languageAbility);

	@MapKey("language_id")
	Map<String, Integer> findMapForLanMap(String userId);

	List<Map<String, Object>> findMapListForStateMap(String userId);

	void insert(LanguageAbility ability);

}
