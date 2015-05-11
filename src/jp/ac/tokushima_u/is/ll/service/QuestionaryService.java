package jp.ac.tokushima_u.is.ll.service;

import java.util.Date;
import java.util.List;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.QuestionaryHabit;
import jp.ac.tokushima_u.is.ll.entity.Users;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("questionaryService")
@Transactional
public class QuestionaryService {
	private HibernateDao<QuestionaryHabit, String> questionaryHabitDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		questionaryHabitDao = new HibernateDao<QuestionaryHabit, String>(sessionFactory,
				QuestionaryHabit.class);
	}
	
	public void evaluateLearningHabit(QuestionaryHabit habit){
		this.questionaryHabitDao.save(habit);
	}

	public QuestionaryHabit findEvaluation(Users user, Date date){
		DetachedCriteria dc = DetachedCriteria.forClass(QuestionaryHabit.class);
		dc.add(Restrictions.eq("user", user));
		dc.add(Restrictions.gt("createTime", date));
		dc.addOrder(Order.desc("createTime"));
		List<QuestionaryHabit> qhList = this.questionaryHabitDao.find(dc);
		if(qhList!=null && qhList.size()>0)
			return qhList.get(0);
		else
			return null;
	}

}
