package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.Interest;

import org.apache.ibatis.annotations.Param;

public interface InterestDao {

	Interest findByName(String trim);

	void insert(Interest interest);

	void deleteAllRelationWithUser(String userId);

	void insertUsersInterest(@Param("userId") String userId, @Param("interestId") String interestId);

	List<Interest> findListByUserId(String userId);

}
