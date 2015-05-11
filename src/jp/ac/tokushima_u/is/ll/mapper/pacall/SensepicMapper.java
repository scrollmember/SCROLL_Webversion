package jp.ac.tokushima_u.is.ll.mapper.pacall;

import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.common.page.PageRequest;
import jp.ac.tokushima_u.is.ll.entity.pacall.SensePic;

import org.apache.ibatis.annotations.Param;

public interface SensepicMapper {
	void insert(SensePic sensePic);

	List<SensePic> selectByRequest(PageRequest<SensePic> request);

	Long countTotalByRequest(PageRequest<SensePic> request);

	List<Map<String, Object>> countGroupByType(String folderId);

	long selectCountByFolder(String folderId);

	SensePic selectById(String id);

	SensePic selectByName(@Param("folderId") String folderId, @Param("filename")String filename);

	void updateFileId(@Param("sensePicId")String sensePicId, @Param("fileId")String fileId);
}
