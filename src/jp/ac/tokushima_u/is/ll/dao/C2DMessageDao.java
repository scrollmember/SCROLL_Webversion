package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.C2DMessage;

public interface C2DMessageDao {

	List<C2DMessage> findListAll();

	int update(C2DMessage message);

	void insert(C2DMessage c2dmessage);
}
