package jp.ac.tokushima_u.is.ll.quiz;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.MyQuiz;
import jp.ac.tokushima_u.is.ll.entity.MyQuizChoice;
import jp.ac.tokushima_u.is.ll.service.ItemQueueService;
import jp.ac.tokushima_u.is.ll.util.Constants;
import jp.ac.tokushima_u.is.ll.visualization.ReviewHistoryService;

public abstract class MutipleChoiceQuiz implements QuizGenerator{
	public MyQuiz checkAnswer(MyQuiz quiz, HibernateDao<MyQuiz, String> myquizDao, HibernateDao<Item, String> itemDao, ItemQueueService itemQueueService, ReviewHistoryService reviewHistoryService){
		Item item = quiz.getItem();
		int queuetype = QuizConstants.QueueTypeRightQuiz;
		int times = 0;
		Integer answerState = null;
		if(!Constants.PassAnsweredState.equals(quiz.getAnswerstate())){
			if(Constants.DifficultAnsweredState.equals(quiz.getAnswerstate())){
				queuetype = QuizConstants.QueueTypeDifficultQuiz;
				answerState = Constants.DifficultAnsweredState;
			}else if(Constants.EasyAnsweredState.equals(quiz.getAnswerstate())){
				queuetype = QuizConstants.QueueTypeEasyQuiz;
				answerState = Constants.EasyAnsweredState;
			}
			Integer myanswer = Integer.valueOf(quiz.getMyanswer());
			Integer answer = Integer.valueOf(quiz.getAnswer());
			if(myanswer.equals(answer)){
				if(!Constants.DifficultAnsweredState.equals(quiz.getAnswerstate())&&!Constants.EasyAnsweredState.equals(quiz.getAnswerstate())){
					queuetype = QuizConstants.QueueTypeRightQuiz;
					answerState = Constants.CorrectAnsweredState;
				}
				quiz.setAnswerstate(Constants.CorrectAnsweredState);
				times = item.getRighttimes();
				item.setRighttimes(times+1);
			}else{
				if(!Constants.DifficultAnsweredState.equals(quiz.getAnswerstate())&&!Constants.EasyAnsweredState.equals(quiz.getAnswerstate())){
					answerState = Constants.WrongAnsweredState;
					queuetype = QuizConstants.QueueTypeWrongQuiz;
					quiz.setAnswerstate(Constants.WrongAnsweredState);
				}
				times = item.getWrongtimes();
				item.setWrongtimes(times+1);
			}
//			}
		}else{
			queuetype = QuizConstants.QueueTypeObjectPass;
			answerState = Constants.PassAnsweredState;
			times = item.getPass();
			item.setPass(times+1);
		}
		
		quiz.setUpdateDate(new Date());
		myquizDao.save(quiz);
		itemQueueService.updateItemQueue(quiz.getItem(), quiz.getAuthor(), queuetype);
		reviewHistoryService.updateItemState(quiz.getItem(), quiz.getAuthor(), null, answerState);
		itemDao.save(item);
		return quiz;
	}
	
	public QuizWrapper convertChoiceQuiz(MyQuiz quiz) {
		QuizWrapper quizWrapper = new QuizWrapper();
		quizWrapper.setAnswer(quiz.getAnswer());
		quizWrapper.setAnswerstate(Constants.NotAnsweredState);
		quizWrapper.setChoices(this.getChoiceContents(quiz.getQuizChoices()));
		quizWrapper.setContent(quiz.getContent());
		quizWrapper.setCreateDate(quiz.getCreateDate());
		quizWrapper.setQuizid(quiz.getId());
		quizWrapper.setNotes(this.getChoiceNotes(quiz.getQuizChoices()));
		return quizWrapper;
	}
	
	public String[] getChoiceContents(List<MyQuizChoice> choices){
		String[]contents = new String[choices.size()];
		for(int i=0;i<choices.size();i++){
			contents[i]=choices.get(i).getContent();
		}
		return contents;
	}
	
	public String[] getChoiceNotes(List<MyQuizChoice> choices){
		String[]notes = new String[choices.size()];
		for(int i=0;i<choices.size();i++){
			notes[i]=choices.get(i).getNote();
		}
		return notes;
	}
}
