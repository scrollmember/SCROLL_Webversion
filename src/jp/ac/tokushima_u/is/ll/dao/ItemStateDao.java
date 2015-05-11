package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.ItemState;

import org.apache.ibatis.annotations.Param;

public interface ItemStateDao {

	List<ItemState> findListByUserId(String userId);

	List<ItemState> findListByUserIdAndItemId(@Param("userId")String userId, @Param("itemId")String itemId);

	void insert(ItemState itemState);

	void update(ItemState itemState);

	List<ItemState> findListByExample(ItemState itemState);

	List<ItemState> findListUserItemHistory(String authorId);

}
