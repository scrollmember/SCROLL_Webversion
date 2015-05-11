package jp.ac.tokushima_u.is.ll.dao;

import java.util.Date;
import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.CycleTime;

import org.apache.ibatis.annotations.Param;

public interface CycleTimeDao {

	void delteByAuthorAndBeforeCheckday(@Param("authorId")String authorId, @Param("checkday")String checkday);

	List<CycleTime> findByAuthorAndCheckday(@Param("authorId")String authorId, @Param("checkday")Date checkday);

	void insert(CycleTime ct);

}
