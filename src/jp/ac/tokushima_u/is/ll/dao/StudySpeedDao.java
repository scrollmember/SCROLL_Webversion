package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.StudySpeed;

import org.apache.ibatis.annotations.Param;

public interface StudySpeedDao {

	List<StudySpeed> findListByAuthor(String authorId);

	List<StudySpeed> findListBySpeed(@Param("authorId")String authorId, @Param("speed")Float speed);

	List<StudySpeed> findListByExample(StudySpeed example);

	void updateSetDisabledByAuthorId(@Param("disabled")Integer disabled, @Param("authorId")String authorId);

	void insert(StudySpeed studyspeed);

}
