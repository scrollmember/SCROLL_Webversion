package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.QuestionType;

public interface QuestionTypeDao {

	List<QuestionType> findListByItemId(String itemId);

	List<QuestionType> findListAll();

	QuestionType findById(Long id);
}
