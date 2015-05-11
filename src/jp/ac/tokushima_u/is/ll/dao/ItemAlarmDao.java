package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.ItemAlarm;

import org.apache.ibatis.annotations.Param;

public interface ItemAlarmDao {

	ItemAlarm findByUnicode(String notifyCode);

	void insert(ItemAlarm itemalarm);

	void update(ItemAlarm itemalarm);

	List<ItemAlarm> findListForQuiz(@Param("authorId")String authorId, @Param("lat")Double lat, @Param("lng")Double lng,
			@Param("distance") Double distance, @Param("alarmType")Integer alarmType, @Param("feedback") Integer feedback);

}
