package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.entity.StatusWord;

public interface StatusDao {
	List<StatusWord> getUserJPWordlistByAuthorId(String id);
	
	List<StatusWord> getUserJPWordlistByAuthorIdDate(Map<String, Object> param);

	List<String> getUserJPCreatetimeByAuthorId(String id);
	
	List<String> getUserJPCreatetimeByAuthorIdDate(Map<String, Object> param);
	
	
}

