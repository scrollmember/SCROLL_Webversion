package jp.ac.tokushima_u.is.ll.dao;

import java.util.Date;
import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.LogSendMail;

import org.apache.ibatis.annotations.Param;

public interface LogSendMailDao {

	void insert(LogSendMail log);

	List<LogSendMail> findListByPeriod(@Param("address")String address,
			@Param("sendId")String sendId, @Param("start")Date start, @Param("end")Date end);

}
