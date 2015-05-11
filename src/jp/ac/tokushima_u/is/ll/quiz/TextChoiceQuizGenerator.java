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
import jp.ac.tokushima_u.is.ll.visualization.ReviewHistoryService;

@Service("textQuizGenerator")
public class TextChoiceQuizGenerator extends MutipleChoiceQuiz{
    private HibernateDao<ItemTitle, String> itemTitleDao;
    protected HibernateDao<MyQuiz, String> myquizDao;
    private SessionFactory sessionFactory;
    private HibernateDao<Item, String> itemDao;
    
	@Value("${system.staticserverImageUrl}")
	private String staticserverImageUrl;
    
    @Autowired
    private ItemQueueService itemQueueService;
    
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
		ItemTitle mtitle = this.getMotherLanTitle(item, user);
		if(title==null||mtitle==null)
			return null;
		MyQuiz myquiz = new MyQuiz();
		myquiz.setAuthor(user);
		
		myquiz.setAlarmtype(qc.getAlarmtype());

	    if(qc.getLng()!=null&&qc.getLat()!=null){
        	myquiz.setLongitude(qc.getLng());
        	myquiz.setLatitude(qc.getLat());
        }
        if(qc.getSpeed()!=null)
        	myquiz.setSpeed(qc.getSpeed());
		
		myquiz.setCreateDate(new Date());
		myquiz.setItem(item);
		myquiz.setAnswerstate(Constants.NotAnsweredState);
		myquiz.setQuestionType(questionType.getQuestionType());
		myquiz.setUpdateDate(new Date());
		myquiz.setLanguage(title.getLanguage());
		int r = (int)(Math.random()*2);
		boolean mLanQues = false;
		if(r==0)
			mLanQues = true;
		if(mLanQues){
			myquiz.setContent(mtitle.getContent());
			myquiz.setLanCode(mtitle.getLanguage().getCode());
			questionType.setLanguage(title.getLanguage());
		}else{
			myquiz.setContent(title.getContent());
			myquiz.setLanCode(title.getLanguage().getCode());
			questionType.setLanguage(mtitle.getLanguage());
		}
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
	
	public ItemTitle getMotherLanTitle(Item item, Users user){
		DetachedCriteria dc = DetachedCriteria.forClass(ItemTitle.class);
		dc.add(Restrictions.eq("item", item));
		dc.add(Restrictions.in("language", user.getMyLangs()));
		List<ItemTitle> titles = this.itemTitleDao.find(dc);
		if(titles!=null&&titles.size()>0)
			return titles.get(0);
		else
			return null;
	}
	

	private List<MyQuizChoice> quizChoicesCreater(Users user, Item item, MyQuiz myquiz, ItemQuestionType quesType){
		List<MyQuizChoice> choices = new LinkedList<MyQuizChoice>();
		List<Item> itemchoices = new LinkedList<Item>();
		
//		String sql = "select *	from t_item it  where it.id in ( " +
//				"select distinct(item_id) from t_itemqueue " +
//				" where author_id = '"+user.getId()+"') and it.id!= '"+item.getId()+"' " +
//				"and exists ( select * from t_item_question_type itq " +
//					"where itq.item_id = it.id and itq.questiontype_id = "+quesType.getQuestionType().getId()+
//					" and itq.language_id = '"+quesType.getLanguage().getId()+"')" +
//							" limit 0, 50 ";
		
		String sql = "select it.* from t_item it " +
				"where 	it.id!= '"+item.getId()+"' " +
				"and exists ( select * from t_itemqueue q where q.queuetype in (1, 2, 3) and q.item_id = it.id ) "+
				"and exists ( select * from t_item_question_type itq where itq.item_id = it.id and itq.questiontype_id = 1 and itq.language_id = '"+quesType.getLanguage().getId()+"') limit 0, 200";
		
		
		Session session = this.sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Item> items = session.createSQLQuery(sql).addEntity(Item.class).list();
		
		while(itemchoices.size()<3&&items.size()>=(3-itemchoices.size())){
			int index = (int)(items.size()*Math.random());
			Item tempitem = items.get(index);
			if(ItemUtil.inItemList(tempitem, itemchoices)||ItemUtil.itemRealted(item,tempitem)){
				items.remove(tempitem);
				continue;
			}
			itemchoices.add(tempitem);
			items.remove(tempitem);
		}
	

		int answer = (int)(Math.random()*4)+1;
		myquiz.setAnswer(answer+"");
		int num = 1;
		boolean toAdd = true;
		for(Item itemchoice:itemchoices){
			if(num==answer){
				MyQuizChoice tempchoice = new MyQuizChoice();
				tempchoice.setItem(item);
				ItemTitle title = this.getItemTitle(item, quesType.getLanguage());
				tempchoice.setContent(title.getContent());
				tempchoice.setLanCode(quesType.getLanguage().getCode());
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
			ItemTitle title = this.getItemTitle(itemchoice, quesType.getLanguage());
			choice.setContent(title.getContent());
			choice.setLanCode(quesType.getLanguage().getCode());
			choices.add(choice);
			num++;
		}
		if(toAdd){
			MyQuizChoice tempchoice = new MyQuizChoice();
			tempchoice.setItem(item);
			ItemTitle title = this.getItemTitle(item, quesType.getLanguage());
			tempchoice.setContent(title.getContent());
			tempchoice.setLanCode(quesType.getLanguage().getCode());
			tempchoice.setNote(ItemUtil.getItemNote(item));
			tempchoice.setNumber(answer);
			tempchoice.setMyquiz(myquiz);
			choices.add(tempchoice);
			num++;
			toAdd = false;
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
		wrapper.setQuestionTypeId(Constants.QuizTypeTextMutiChoice);
		return wrapper;
	}
}
