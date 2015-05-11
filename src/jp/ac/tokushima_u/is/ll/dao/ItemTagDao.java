package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.ItemTag;
import jp.ac.tokushima_u.is.ll.entity.ItemTags;

import org.apache.ibatis.annotations.Param;

public interface ItemTagDao {

	ItemTag findByTag(String tag);

	void insert(ItemTag tag);

	void insertRelationWithItem(@Param("item") Item item, @Param("tag")ItemTag tag);

	List<ItemTag> searchByTag(String tag);

	List<ItemTag> findByItemId(String id);

	List<Map<String, Object>> findMapForTagAndContainsNumber();

	Long countAll();
	
	//network analysis tag
	List<ItemTags> findnetworktag(String itemid);
}
