package jp.ac.tokushima_u.is.ll.quiz;

import java.util.Date;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.ItemQuestionType;
import jp.ac.tokushima_u.is.ll.entity.MyQuiz;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.service.ItemQueueService;
import jp.ac.tokushima_u.is.ll.service.helper.QuizCondition;
import jp.ac.tokushima_u.is.ll.util.Constants;
import jp.ac.tokushima_u.is.ll.visualization.ReviewHistoryService;
import jp.ac.tokushima_u.is.ll.ws.service.model.ItemForm;

@Service("yesnoQuizGenerator")
public class YesNoQuizGenerator implements QuizGenerator {
    private HibernateDao<MyQuiz, String> myquizDao;
    private HibernateDao<Users, String> userDao;
    
    private static final Integer RememberAnswer = 1;
    private static final Integer ForgetAnswer = 0;
    
	@Value("${system.staticserverImageUrl}")
	private String staticserverImageUrl;
    private HibernateDao<Item, String> itemDao;
    @Autowired
    private ItemQueueService itemQueueService;
    
	@Autowired
	private ReviewHistoryService reviewHistoryService;

       @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        myquizDao = new HibernateDao<MyQuiz, String> (sessionFactory, MyQuiz.class);
        itemDao = new HibernateDao<Item, String> (sessionFactory, Item.class);
        userDao = new HibernateDao<Users, String> (sessionFactory, Users.class);
    }
	
	@Override
	public MyQuiz generate(Item item, ItemQuestionType questionType, Users user, QuizCondition qc) {
		MyQuiz myquiz = new MyQuiz();
		Users systemUser = this.userDao.findUniqueBy("pcEmail", Constants.SYSTEM_ACCOUNT_EMAIL);
		if(!item.getAuthor().equals(user)&&!item.getAuthor().equals(systemUser))
			return null;
		myquiz.setAuthor(user);
		myquiz.setCreateDate(new Date());
		myquiz.setAlarmtype(qc.getAlarmtype());

	    if(qc.getLng()!=null&&qc.getLat()!=null){
        	myquiz.setLongitude(qc.getLng());
        	myquiz.setLatitude(qc.getLat());
        }
        if(qc.getSpeed()!=null)
        	myquiz.setSpeed(qc.getSpeed());
		myquiz.setItem(item);
		myquiz.setAnswerstate(Constants.NotAnsweredState);
		myquiz.setQuestionType(questionType.getQuestionType());
		myquiz.setUpdateDate(new Date());
		this.myquizDao.save(myquiz);
		return myquiz;
	}
	
	@Override
	public MyQuiz checkAnswer(MyQuiz quiz) {
		Item item = quiz.getItem();
		int queuetype = QuizConstants.QueueTypeRightQuiz;
		int times = 0;
		Integer answerState = null;
		if(!Constants.PassAnsweredState.equals(quiz.getAnswerstate())){
			Integer myanswer = Integer.valueOf(quiz.getMyanswer());
			if(myanswer.equals(RememberAnswer)){
				quiz.setAnswerstate(Constants.CorrectAnsweredState);
				queuetype = QuizConstants.QueueTypeRightQuiz;
				answerState = Constants.CorrectAnsweredState;
				if(item.getRighttimes()==null)
					times = 0;
				else 
					times = item.getRighttimes();
				item.setRighttimes(times+1);
			}else if(myanswer.equals(ForgetAnswer)){
				quiz.setAnswerstate(Constants.WrongAnsweredState);
				queuetype = QuizConstants.QueueTypeWrongQuiz;
				answerState = Constants.WrongAnsweredState;
				if(item.getWrongtimes()==null)
					times = 0;
				else 
					times = item.getWrongtimes();
				item.setWrongtimes(times+1);
			}
		}else{
			queuetype = QuizConstants.QueueTypeObjectPass;
			answerState = Constants.PassAnsweredState;
			if(item.getPass()==null)
				times = 0;
			else 
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

	@Override
	public QuizWrapper convertQuiz(MyQuiz quiz) {
		QuizWrapper wrapper = new QuizWrapper();
		wrapper.setQuizid(quiz.getId());
		wrapper.setAnswerstate(Constants.NotAnsweredState);
//		String photoUrl = null;
//		Item item = quiz.getItem();
//		if(item.getImage()!=null)
//			photoUrl = item.getImage().getId();
		wrapper.setItemform(new ItemForm(quiz.getItem()));
		wrapper.setImgServerUrl(staticserverImageUrl);
		return wrapper;
	}
}
