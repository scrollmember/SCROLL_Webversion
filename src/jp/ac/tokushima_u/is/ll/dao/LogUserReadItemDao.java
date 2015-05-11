package jp.ac.tokushima_u.is.ll.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.entity.LogUserReadItem;

import org.apache.ibatis.annotations.Param;

public interface LogUserReadItemDao {

	List<LogUserReadItem> findListByUserId(String userId);
	
	List<LogUserReadItem> findListByUserAndItem(@Param("userId")String userId, @Param("itemId")String itemId);

	List<Map<String, Object>> countReadItemsOnDayByAuthors(Date date);

	List<Map<String, Object>> countReadItemsInWeekByAuthors(@Param("start")Date start, @Param("end")Date end);

	void insert(LogUserReadItem logUserReadItem);

	List<LogUserReadItem> findListByUserIdSelfRead(String userId);

	List<LogUserReadItem> findListByUserAndCreatedAfter(@Param("userId")String userId, @Param("lastDate")Date lastDate);

	Long countByItemId(String itemId);

	Long countAll();

	Long countByUserId(String userId);

}
