package jp.ac.tokushima_u.is.ll.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.StudyArea;
import jp.ac.tokushima_u.is.ll.entity.StudySpeed;
import jp.ac.tokushima_u.is.ll.entity.StudyTime;
import jp.ac.tokushima_u.is.ll.entity.Users;

@Service("learningHabitService")
@Transactional(readOnly = true)
public class LearningHabitService {
	private HibernateDao<StudyArea, String> studyAreaDao;
	private HibernateDao<StudyTime, String> studyTimeDao;
	private HibernateDao<StudySpeed, String> studySpeedDao;
	private HibernateDao<Users, String> userDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.studyTimeDao = new HibernateDao<StudyTime, String>(sessionFactory,
				StudyTime.class);
		this.studyAreaDao = new HibernateDao<StudyArea, String>(sessionFactory,
				StudyArea.class);
		this.studySpeedDao = new HibernateDao<StudySpeed, String>(sessionFactory,
				StudySpeed.class);
		this.userDao = new HibernateDao<Users, String>(sessionFactory,
				Users.class);
	}
	
	public List<StudyArea> searchStudyArea(String userId){
		Users user = this.userDao.findUniqueBy("id", userId);
		DetachedCriteria dc = DetachedCriteria.forClass(StudyArea.class);
		dc.add(Restrictions.eq("author", user));
		dc.add(Restrictions.eq("disabled", 0));
		List<StudyArea> areas = this.studyAreaDao.find(dc);
		return areas;
	}
	
	public List<StudyTime> searchStudyTime(String userId){
		Users user = this.userDao.findUniqueBy("id", userId);
		DetachedCriteria dc = DetachedCriteria.forClass(StudyTime.class);
		dc.add(Restrictions.eq("author", user));
		dc.add(Restrictions.eq("disabled", 0));
		List<StudyTime> times = this.studyTimeDao.find(dc);
		return times;
	}

}
