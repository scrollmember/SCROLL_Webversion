package jp.ac.tokushima_u.is.ll.mapper.pacall;

import java.util.Date;
import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.pacall.Folder;

import org.apache.ibatis.annotations.Param;

public interface FolderMapper {

	List<Folder> selectAll();

	int deleteByKey(String name);

	Folder selectByKey(String name);
	
	//New
	List<Folder> selectByUserId(String userid);

	void insert(Folder f);

	int updateLastModified(@Param("folderId") String folderId, @Param("lastModified")Date lastModified);

	Folder findByHash(String hash);

	int deleteById(String id);

	Folder selectById(String id);

	void updateStartEndDate(@Param("folderId") String folderId, @Param("startDate")Date start, @Param("endDate")Date end);
	
}
