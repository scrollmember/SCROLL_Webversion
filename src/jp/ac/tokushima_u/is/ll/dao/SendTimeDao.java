package jp.ac.tokushima_u.is.ll.dao;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.SendTime;

import org.apache.ibatis.annotations.Param;

public interface SendTimeDao {

	List<SendTime> findListBetweenTime(@Param("typ") Integer weektype, @Param("authorId") String authorId, @Param("from")Time from,
			@Param("now")Time now);

	SendTime findOneByAuthorAndTyp(@Param("authorId")String userId, @Param("typ")Integer typ);

	void insert(SendTime sendtime);

	void deleteAllByAuthorId(@Param("authorId")String userId);

	List<SendTime> findListByAuthorAndCreatedAfter(@Param("authorId")String userId,
			@Param("from")Date from);

}
