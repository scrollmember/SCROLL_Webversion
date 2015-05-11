package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.Answer;

public interface AnswerDao {

	void insert(Answer answer);

	Long countByUserId(String userId);

	List<Answer> findListByQuestionId(String questionId);

}
