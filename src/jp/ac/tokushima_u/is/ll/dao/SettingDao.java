package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.Setting;

public interface SettingDao {

	Setting findByUserId(String id);

	List<Setting> findListByHandsetcd(Integer handsetcd);

	void insert(Setting setting);

	void update(Setting setting);

}
