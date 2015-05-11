package jp.ac.tokushima_u.is.ll.quiz;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.MyQuiz;
import jp.ac.tokushima_u.is.ll.entity.QuestionType;
import jp.ac.tokushima_u.is.ll.util.Constants;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("quizGeneratorFacotry")
public class QuizGeneratorFacotry {
    private HibernateDao<QuestionType, String> questionTypeDao;
    @Autowired
    private TextChoiceQuizGenerator textQuizGenerator;
    @Autowired
    private ImageChoiceQuizGenerator imageQuizGenerator;
    @Autowired
    private YesNoQuizGenerator yesnoQuizGenerator;
    
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        questionTypeDao = new HibernateDao<QuestionType,String>(sessionFactory, QuestionType.class);
    }

	public QuizGenerator getQuizGenerator(QuestionType questionType){
		if(this.getImageChoiceType().equals(questionType)){
			return  this.imageQuizGenerator;
		}
		if (this.getTextChoiceType().equals(questionType)){
			return this.textQuizGenerator;
		}
		else 
		if(this.getYesNoType().equals(questionType)){
			return this.yesnoQuizGenerator;
		}
		return null;
	}
	
	public QuizGenerator getQuizGenerator(MyQuiz myquiz){
		if(myquiz.getQuestionType().equals(this.getImageChoiceType())){
			return  this.imageQuizGenerator;
		}
		if (myquiz.getQuestionType().equals(this.getTextChoiceType())){
			return this.textQuizGenerator;
		}
		else 
		if(myquiz.getQuestionType().equals(this.getYesNoType())){
			return this.yesnoQuizGenerator;
		}
		return null;
	}
	
	public QuestionType getTextChoiceType(){
		return this.questionTypeDao.findUniqueBy("id", Constants.QuizTypeTextMutiChoice);
	}
	
	public QuestionType getImageChoiceType(){
		return this.questionTypeDao.findUniqueBy("id", Constants.QuizTypeImageMutiChoice);
	}
	
	public QuestionType getYesNoType(){
		return this.questionTypeDao.findUniqueBy("id", Constants.QuizTypeYesNoQuestion);
	}
	
}
