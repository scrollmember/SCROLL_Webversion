package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;

//import jp.ac.tokushima_u.is.ll.entity.UsersStudyLangs;

import org.apache.ibatis.annotations.Param;

public interface UsersStudyLangsDao {
	
	void insert(@Param("userId")String userId, @Param("languageId")String languageId, @Param("order")int order);

	void deleteAllByUserId(String userId);

//	List<UsersStudyLangs> findListByUserId(String userId);
}
