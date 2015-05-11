package jp.ac.tokushima_u.is.ll.dao;

import java.util.Date;
import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.StudyArea;

import org.apache.ibatis.annotations.Param;

public interface StudyAreaDao {

	List<StudyArea> findListByArea(@Param("authorId")String authorId, @Param("lat")Double lat, @Param("lng")Double lng);

	List<StudyArea> findListByAuthor(String userId);

	List<StudyArea> findListByExample(StudyArea example);

	void updateSetDisabledByAuthorId(@Param("disabled")Integer disabled, @Param("authorId")String authorId);

	void insert(StudyArea studyarea);

	List<StudyArea> findListByAuthorAndCreateAfter(@Param("userId")String userId,
			@Param("from")Date from);

}
