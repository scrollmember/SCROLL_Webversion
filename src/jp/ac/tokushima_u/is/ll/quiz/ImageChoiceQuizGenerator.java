package jp.ac.tokushima_u.is.ll.quiz;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.ItemQuestionType;
import jp.ac.tokushima_u.is.ll.entity.ItemTitle;
import jp.ac.tokushima_u.is.ll.entity.Language;
import jp.ac.tokushima_u.is.ll.entity.MyQuiz;
import jp.ac.tokushima_u.is.ll.entity.MyQuizChoice;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.service.ItemQueueService;
import jp.ac.tokushima_u.is.ll.service.helper.QuizCondition;
import jp.ac.tokushima_u.is.ll.util.Constants;
import jp.ac.tokushima_u.is.ll.util.ItemUtil;
import jp.ac.tokushima_u.is.ll.util.StaticImageUtil;
import jp.ac.tokushima_u.is.ll.visualization.ReviewHistoryService;

@Service("imageQuizGenerator")
public class ImageChoiceQuizGenerator extends MutipleChoiceQuiz{
    private HibernateDao<ItemTitle, String> itemTitleDao;
    protected HibernateDao<MyQuiz, String> myquizDao;
    private HibernateDao<Item, String> itemDao;
    private SessionFactory sessionFactory;
	@Value("${system.staticserverImageUrl}")
	private String staticserverImageUrl;
    @Autowired
    protected ItemQueueService itemQueueService;
    
	@Autowired
	private ReviewHistoryService reviewHistoryService;

       @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
    	this.sessionFactory = sessionFactory;
        myquizDao = new HibernateDao<MyQuiz, String> (sessionFactory, MyQuiz.class);
        itemTitleDao = new HibernateDao<ItemTitle, String> (sessionFactory, ItemTitle.class);
        itemDao = new HibernateDao<Item, String> (sessionFactory, Item.class);
    }
	
	@Override
	public MyQuiz generate(Item item, ItemQuestionType questionType, Users user, QuizCondition qc) {
		ItemTitle title = this.getItemTitle(item, questionType.getLanguage());
		if(title==null)
			return null;
		MyQuiz myquiz = new MyQuiz();
		myquiz.setContent(title.getContent());
		myquiz.setAlarmtype(qc.getAlarmtype());

	    if(qc.getLng()!=null&&qc.getLat()!=null){
        	myquiz.setLongitude(qc.getLng());
        	myquiz.setLatitude(qc.getLat());
        }
        if(qc.getSpeed()!=null)
        	myquiz.setSpeed(qc.getSpeed());
		
		myquiz.setAuthor(user);
		myquiz.setCreateDate(new Date());
		myquiz.setItem(item);
		myquiz.setAnswerstate(Constants.NotAnsweredState);
		myquiz.setQuestionType(questionType.getQuestionType());
		myquiz.setUpdateDate(new Date());
		myquiz.setLanguage(title.getLanguage());
		List<MyQuizChoice> choices = this.quizChoicesCreater(user, item, myquiz, questionType);
		if(choices!=null&&choices.size()>3){
			myquiz.setQuizChoices(choices);
			this.myquizDao.save(myquiz);
			return myquiz;
		}
		return null;
	}
	
	public ItemTitle getItemTitle(Item item, Language language){
		DetachedCriteria dc = DetachedCriteria.forClass(ItemTitle.class);
		dc.add(Restrictions.eq("item", item));
		dc.add(Restrictions.eq("language",language));
		List<ItemTitle> titles = this.itemTitleDao.find(dc);
		if(titles!=null&&titles.size()>0)
			return titles.get(0);
		else
			return null;
	}
	

	private List<MyQuizChoice> quizChoicesCreater(Users user, Item item, MyQuiz myquiz, ItemQuestionType quesType){
		List<MyQuizChoice> choices = new LinkedList<MyQuizChoice>();
		List<Item> itemchoices = new LinkedList<Item>();
		
		String sql = "select it.*	from t_item it, t_file_data fd  where it.image = fd.id and it.id in ( " +
				"select distinct(item_id) from t_itemqueue " +
				" where author_id = '"+user.getId()+"') and it.image is not null and it.id!= '"+item.getId()+"' " +
				" and fd.file_type='"+item.getImage().getFileType()+"' "+
				"and exists ( select * from t_item_question_type itq " +
					"where itq.item_id = it.id and itq.questiontype_id = "+quesType.getQuestionType().getId()+
					" and itq.language_id = '"+quesType.getLanguage().getId()+"')";
		
		
		Session session = this.sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Item> items = session.createSQLQuery(sql).addEntity(Item.class).list();
		
		while(itemchoices.size()<3&&items.size()>=(3-itemchoices.size())){
			int index = (int)(items.size()*Math.random());
			Item tempitem = items.get(index);
			if(ItemUtil.inItemList(tempitem, itemchoices)||ItemUtil.itemRealted(item,tempitem)){
				items.remove(tempitem);
				continue;
			}else if(!StaticImageUtil.isFileExisting(staticserverImageUrl+tempitem.getImage().getId(), Constants.SmartPhoneLevel)){
				items.remove(tempitem);
				continue;
			}
			
			itemchoices.add(tempitem);
			items.remove(tempitem);
		}
		
		if(itemchoices.size()<3){
			return choices;
		}
	

		int answer = (int)(Math.random()*4)+1;
		myquiz.setAnswer(answer+"");
		int num = 1;
		boolean toAdd = true;
		for(Item itemchoice:itemchoices){
			if(num==answer){
				MyQuizChoice tempchoice = new MyQuizChoice();
				tempchoice.setItem(item);
				tempchoice.setContent(item.getImage().getId());
				tempchoice.setNote(ItemUtil.getItemNote(item));
				tempchoice.setNumber(num);
				tempchoice.setMyquiz(myquiz);
				choices.add(tempchoice);
				num++;
				toAdd = false;
			}
			MyQuizChoice choice = new MyQuizChoice();
			choice.setItem(itemchoice);
			choice.setNote(ItemUtil.getItemNote(itemchoice));
			choice.setMyquiz(myquiz);			
			choice.setNumber(num);
				choice.setContent(choice.getItem().getImage().getId());
			choices.add(choice);
			num++;
		}
		
		if(toAdd){
			MyQuizChoice tempchoice = new MyQuizChoice();
			tempchoice.setItem(item);
			tempchoice.setContent(item.getImage().getId());
			tempchoice.setNote(ItemUtil.getItemNote(item));
			tempchoice.setNumber(answer);
			tempchoice.setMyquiz(myquiz);
			choices.add(tempchoice);
			num++;
		}
		
		return choices;
	}

	public MyQuiz checkAnswer(MyQuiz quiz) {
		return this.checkAnswer(quiz, myquizDao, itemDao, itemQueueService, reviewHistoryService);
	}

	@Override
	public QuizWrapper convertQuiz(MyQuiz quiz) {
		QuizWrapper wrapper = this.convertChoiceQuiz(quiz);
		wrapper.setImgServerUrl(staticserverImageUrl);
		wrapper.setQuestionTypeId(Constants.QuizTypeImageMutiChoice);
		return wrapper;
	}
	
	//	public MyQuiz checkAnswer(MyQuiz quiz) {
//		Item item = quiz.getItem();
//		int queuetype = ItemQueueService.QueueTypeRightQuiz;
//		int times = 0;
//		if(!Constants.PassAnsweredState.equals(quiz.getAnswerstate())){
//			Integer myanswer = Integer.valueOf(quiz.getMyanswer());
//			Integer answer = Integer.valueOf(quiz.getAnswer());
//			if(myanswer.equals(answer)){
//				quiz.setAnswerstate(Constants.CorrectAnsweredState);
//				queuetype = ItemQueueService.QueueTypeRightQuiz;
//				if(item.getRighttimes()==null)
//					times = 0;
//				else 
//					times = item.getRighttimes();
//				item.setRighttimes(times+1);
//			}else{
//				quiz.setAnswerstate(Constants.WrongAnsweredState);
//				queuetype = ItemQueueService.QueueTypeWrongQuiz;
//				if(item.getWrongtimes()==null)
//					times = 0;
//				else 
//					times = item.getWrongtimes();
//				item.setWrongtimes(times+1);
//			}
//		}else{
//			queuetype = ItemQueueService.QueueTypeObjectPass;
//			if(item.getPass()==null)
//				times = 0;
//			else 
//				times = item.getPass();
//			item.setPass(item.getPass()+1);
//		}
//		
//		quiz.setUpdateDate(new Date());
//		this.myquizDao.save(quiz);
//		this.itemQueueService.updateItemQueue(quiz.getItem(), quiz.getAuthor(), queuetype);
//		this.itemDao.save(item);
//		return quiz;
//	}
}
