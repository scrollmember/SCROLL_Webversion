package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.C2DMCode;

import org.apache.ibatis.annotations.Param;

public interface C2DMCodeDao {

	List<C2DMCode> findByAuthorAndImsi(@Param("authorId")String authorId, @Param("imsi")String imsi);

	List<C2DMCode> findByAuthor(String authorId);

	void update(C2DMCode code);

}
