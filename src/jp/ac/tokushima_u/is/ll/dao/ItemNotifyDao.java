package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.ItemNotify;

import org.apache.ibatis.annotations.Param;

public interface ItemNotifyDao {

	List<ItemNotify> findByAuthor(String authorId);

	ItemNotify findById(String id);

	void update(ItemNotify itemnotify);

	void insert(ItemNotify notify);

	void insertNofityItems(@Param("itemId") String itemId, @Param("notifyId")String notifyId);
}
