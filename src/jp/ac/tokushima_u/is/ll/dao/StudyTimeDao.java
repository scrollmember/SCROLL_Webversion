package jp.ac.tokushima_u.is.ll.dao;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.StudyTime;

import org.apache.ibatis.annotations.Param;

public interface StudyTimeDao {

	List<StudyTime> findListByTime(@Param("authorId") String id, @Param("time") Time time);

	List<StudyTime> findListByAuthor(String userId);

	List<StudyTime> findListByAuthorAndCreatedAfter(@Param("authorId")String authorId, @Param("from")Date from);

	void updateDisabledByAuthor(@Param("disabled")Integer disabled, @Param("authorId")String authorId);

	void insert(StudyTime studytime);

}
