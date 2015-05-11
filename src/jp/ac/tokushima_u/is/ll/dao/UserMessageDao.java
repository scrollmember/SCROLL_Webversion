package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.UserMessage;

public interface UserMessageDao {

	void insert(UserMessage msg);

	Long countNotReadForReceiver(String userId);

	List<UserMessage> findListAllForReceiver(String userId);

	UserMessage findById(String id);

	void update(UserMessage msg);

}
