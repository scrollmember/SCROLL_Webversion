package jp.ac.tokushima_u.is.ll.quiz;

import java.util.List;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.ItemQuestionType;
import jp.ac.tokushima_u.is.ll.entity.ItemTitle;
import jp.ac.tokushima_u.is.ll.entity.QuestionType;
import jp.ac.tokushima_u.is.ll.util.Constants;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("convertWorkerService")
@Transactional
public class ConvertWorkerService {

	private HibernateDao<Item, String> itemDao;
	private HibernateDao<ItemQuestionType, String> itemQuestionTypeDao;
	private HibernateDao<QuestionType, String> questionTypeDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		itemDao = new HibernateDao<Item, String>(sessionFactory, Item.class);
		questionTypeDao = new HibernateDao<QuestionType, String>(
				sessionFactory, QuestionType.class);
		itemQuestionTypeDao = new HibernateDao<ItemQuestionType, String>(
				sessionFactory, ItemQuestionType.class);
	}

	public void convert() {
		List<Item> items = this.itemDao.getAll();
		QuestionType textchoice = getTextChoiceType();
		QuestionType yeno = getYesNoType();
		QuestionType imagechoice = getImageChoiceType();
		for (Item item : items) {
			if(item.getRelogItem()!=null)
				continue;
			List<ItemTitle> titles = item.getTitles();
			
			for(ItemTitle t:titles){
				ItemQuestionType iqt1 = new ItemQuestionType();
				iqt1.setItem(item);
				iqt1.setLanguage(t.getLanguage());
				iqt1.setQuestionType(textchoice);
				this.itemQuestionTypeDao.save(iqt1);
				
				ItemQuestionType iqt2 = new ItemQuestionType();
				iqt2.setItem(item);
				iqt2.setQuestionType(yeno);
				this.itemQuestionTypeDao.save(iqt2);
				
				if(item.getImage()!=null){
					ItemQuestionType iqt3 = new ItemQuestionType();
					iqt3.setItem(item);
					iqt3.setLanguage(t.getLanguage());
					iqt3.setQuestionType(imagechoice);
					this.itemQuestionTypeDao.save(iqt3);
				}
			}
		}
		System.out.println("covert finished");
		
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
