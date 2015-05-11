package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.StudyPhase;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Sort;

public interface StudyPhaseDao {

	List<StudyPhase> findByUserId(@Param("userId")String id, @Param("sort") Sort sort);

	void update(StudyPhase phase);

	void insert(StudyPhase phase);

}
