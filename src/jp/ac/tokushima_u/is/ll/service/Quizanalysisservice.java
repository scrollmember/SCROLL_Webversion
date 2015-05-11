package jp.ac.tokushima_u.is.ll.service;

import java.util.List;

import jp.ac.tokushima_u.is.ll.dao.ItemTagDao;
import jp.ac.tokushima_u.is.ll.dao.MyQuizChoiceDao;
import jp.ac.tokushima_u.is.ll.entity.ItemTag;
import jp.ac.tokushima_u.is.ll.entity.MyQuiz;
import jp.ac.tokushima_u.is.ll.entity.Quizanalysis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class Quizanalysisservice {

	@Autowired
	private MyQuizChoiceDao myQuizDao;

	public List<Quizanalysis> getwronghistory(String id) {
		// TODO Auto-generated method stub
		return this.myQuizDao.getQuizanalysiswronghistory(id);
	}

	public List<Quizanalysis> getalluserquizdata() {
		// TODO Auto-generated method stub
		return this.myQuizDao.getalluserquizdata();
	}


}