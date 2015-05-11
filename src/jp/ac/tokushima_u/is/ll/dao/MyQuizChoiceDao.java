package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import jp.ac.tokushima_u.is.ll.entity.MyQuizChoice;
import jp.ac.tokushima_u.is.ll.entity.Quizanalysis;

public interface MyQuizChoiceDao {

	List<MyQuizChoice> findListByMyQuizId(String myQuizId);

	void insert(MyQuizChoice choice);

	List<Quizanalysis> getQuizanalysiswronghistory(@Param("authorid")String id);

	List<Quizanalysis> getalluserquizdata();
	
}
