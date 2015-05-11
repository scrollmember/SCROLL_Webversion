package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.Message;
import jp.ac.tokushima_u.is.ll.entity.MymessageList;

public interface MessageDao {

	Long countAll();

	List<Message> findListByExample(Message message);
	
	
	List<MymessageList> searchmylist(String id);

	void insert(Message message);

}
