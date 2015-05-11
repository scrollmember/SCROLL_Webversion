package jp.ac.tokushima_u.is.ll.dao;

import java.util.Date;
import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.ItemQueue;

import org.apache.ibatis.annotations.Param;

public interface ItemQueueDao {

	List<ItemQueue> findListByUserAndItem(@Param("userId")String userId, @Param("itemId")String itemId);

	int update(ItemQueue queue);

	void insert(ItemQueue itemqueue);

	List<ItemQueue> findListForSyncQuizzes(@Param("userId")String userId, @Param("startTimeCond")Date startTimeCond,
			@Param("updateTimeCond")Date updateTimeCond);

	void deleteAllByItem(String itemId);

}
