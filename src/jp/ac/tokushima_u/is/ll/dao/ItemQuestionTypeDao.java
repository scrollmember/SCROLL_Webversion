package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.ItemQuestionType;
import jp.ac.tokushima_u.is.ll.entity.Language;

import org.apache.ibatis.annotations.Param;

public interface ItemQuestionTypeDao {

	void insert(ItemQuestionType qt);

	List<ItemQuestionType> findListByItemAndLangs(@Param("itemId")String itemId,
			@Param("studyLangs")List<Language> studyLangs);

	void deleteAllByItemId(String itemId);

	List<ItemQuestionType> findListByItem(String itemId);

	ItemQuestionType findById(String id);

}
