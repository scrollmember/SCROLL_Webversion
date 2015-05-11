package jp.ac.tokushima_u.is.ll.dao;

import jp.ac.tokushima_u.is.ll.entity.C2DMConfig;

public interface C2DMConfigDao {

	C2DMConfig findById(Integer id);

	void update(C2DMConfig config);

	void insert(C2DMConfig config);

}
