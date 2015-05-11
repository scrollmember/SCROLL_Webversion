package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.FileData;

public interface FileDataDao {

	FileData findById(String id);

	void insert(FileData fileData);

	void delete(String id);

	//TODO Temp method
	List<FileData> findListAll();

	//TODO Temp method
	void updateFileId(FileData fileData);

}
