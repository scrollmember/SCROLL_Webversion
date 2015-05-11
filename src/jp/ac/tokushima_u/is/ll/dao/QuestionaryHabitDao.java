package jp.ac.tokushima_u.is.ll.dao;

import java.util.Date;
import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.QuestionaryHabit;

import org.apache.ibatis.annotations.Param;

public interface QuestionaryHabitDao {

	void insert(QuestionaryHabit habit);

	void update(QuestionaryHabit habit);

	List<QuestionaryHabit> findListForEvaluation(@Param("userId")String userId, @Param("createTime")Date createTime);

}
