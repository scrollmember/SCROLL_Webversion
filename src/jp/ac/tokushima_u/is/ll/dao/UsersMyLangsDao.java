package jp.ac.tokushima_u.is.ll.dao;

import org.apache.ibatis.annotations.Param;

public interface UsersMyLangsDao {

	public void deleteAllByUserId(String userId);

	public void insert(@Param("userId")String userId, @Param("languageId")String languageId, @Param("order")int order);

}
