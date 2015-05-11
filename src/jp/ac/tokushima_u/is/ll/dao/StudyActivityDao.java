package jp.ac.tokushima_u.is.ll.dao;

import java.util.Date;
import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.StudyActivity;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Sort;

public interface StudyActivityDao {

	List<StudyActivity> findListByAuthorId(String authorId);

	void insert(StudyActivity studyActivity);

	List<StudyActivity> findListByAuthorAndCreatedAfter(@Param("authorId")String authorId,
			@Param("from")Date from, @Param("sort")Sort sort);

}
