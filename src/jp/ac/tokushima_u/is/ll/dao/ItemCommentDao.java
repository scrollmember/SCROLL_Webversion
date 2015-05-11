package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.ItemComment;

public interface ItemCommentDao {

	void insert(ItemComment comment);

	List<ItemComment> findListByItemId(String id);

}
