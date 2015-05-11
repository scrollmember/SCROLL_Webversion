package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.Language;
import jp.ac.tokushima_u.is.ll.entity.Question;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface QuestionDao {

	List<Question> findListNotAnsweredForNativeSpeaker(@Param("userId") String userId,
			@Param("pageRequest") Pageable pageRequest);

	List<Question> findListAnsweredForLearner(@Param("userId") String userId,
			@Param("pageRequest") PageRequest pageRequest);

	List<Question> findListAnsweredForAuthor(@Param("userId") String userId,
			@Param("pageRequest") PageRequest pageRequest);

	List<Item> findListAllToAnswer(String userId);

	void insert(Question question);

	void deleteById(String id);

	Question findById(String id);

	List<Map<String, Object>> findMapListForToAnswer(@Param("userId")String userId,
			@Param("langs")List<Language> langs, @Param("page")PageRequest pageRequest);

	List<Map<String, Object>> findMapListForToStudy(@Param("userId")String userId,
			@Param("langs")List<Language> langs, @Param("page")PageRequest pageRequest);

	List<Map<String, Object>> findMapListForLatestAnsweredForAuthor(
			@Param("userId")String userId, @Param("page")PageRequest pageRequest);
}
