package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.Notification;

public interface NotificationDao {

	List<Notification> findListAll();

	Notification findById(String id);

}
