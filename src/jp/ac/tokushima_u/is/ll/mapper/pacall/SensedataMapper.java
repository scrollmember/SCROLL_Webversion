package jp.ac.tokushima_u.is.ll.mapper.pacall;

import java.util.Date;

import jp.ac.tokushima_u.is.ll.entity.pacall.SenseData;

public interface SensedataMapper {
	void insert(SenseData senseData);

	Date findFirstDataByFolder(String folderId);

	Date findLastDataByFolder(String folderId);
}
