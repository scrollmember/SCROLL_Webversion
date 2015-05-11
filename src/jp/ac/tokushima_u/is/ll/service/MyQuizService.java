package jp.ac.tokushima_u.is.ll.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.LogUserReadItem;
import jp.ac.tokushima_u.is.ll.entity.MyQuiz;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.helper.UserQuizInfo;
import jp.ac.tokushima_u.is.ll.util.FormatUtil;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MyQuizService {

	@Autowired
	private UserService userService;
	@Autowired
	private HibernateDao<MyQuiz, String> myquizDao;
	@Autowired
	private HibernateDao<Item, String> itemDao;
	@Autowired
	private HibernateDao<LogUserReadItem, String> logUserReadItemDao;

	private SessionFactory sessionFactory;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.myquizDao = new HibernateDao<MyQuiz, String>(sessionFactory,
				MyQuiz.class);
		this.itemDao = new HibernateDao<Item, String>(sessionFactory,
				Item.class);
		logUserReadItemDao = new HibernateDao<LogUserReadItem, String>(
				sessionFactory, LogUserReadItem.class);
	}

	public Map<String, Integer> findMyQuizWrongCount(Users user) {
		Map<String, Integer> results = new HashMap<String, Integer>();
		//
		// String sql = "select a.incorrect, count(a.item_id) as total "+
		// "	from (select item_id, count(item_id) as incorrect "+
		// " from t_myquiz "+
		// " where answerstate='0' and author_id='"+user.getId()+"'"+
		// " group by item_id ) a"+
		// " where 1=1				"+
		// "group by a.incorrect";
		String sql = "select a.incorrect, count(a.item_id) as total "
				+ "	from (select item_id, (count(IF( answerstate='0',1,null))-count(IF( answerstate='1',1,null))) as incorrect "
				+ "	 from t_myquiz " + "	 where author_id='" + user.getId()
				+ "'	 and answerstate IN(0,1) "
				+ "	 group by item_id having incorrect>0) a" + "	 where 1=1	"
				+ "	group by a.incorrect";

		List<Object[]> states = this.sessionFactory.getCurrentSession()
				.createSQLQuery(sql).list();
		Integer wrong1 = 0;
		Integer wrong2 = 0;
		Integer wrong3more = 0;

		for (Object[] state : states) {
			// Integer incorrect = (Integer)state[0];
			Integer incorrect = ((BigInteger) state[0]).intValue();
			Integer total = ((BigInteger) state[1]).intValue();
			if (incorrect == 1) {
				wrong1 = total;
			} else if (incorrect == 2) {
				wrong2 = total;
			} else {
				wrong3more = wrong3more + total;
			}
		}

		results.put("wrong1", wrong1.intValue());
		results.put("wrong2", wrong2.intValue());
		results.put("wrong3", wrong3more.intValue());
		// results.put("wrong3more",
		// wrong3more.intValue()*2+allright.intValue()*5+allpass.intValue()*2);

		return results;
	}

	public Map<String, Integer> findMyQuizCorrectCount(Users user) {
		Map<String, Integer> results = new HashMap<String, Integer>();

		// String sql = "select a.incorrect, count(a.item_id) as total "+
		// "	from (select item_id, count(item_id) as incorrect "+
		// " from t_myquiz "+
		// " where answerstate='1' and author_id='"+user.getId()+"'"+
		// " group by item_id  HAVING count(item_id)>4 ) a"+
		// " where 1=1	";
		String sql = "select a.incorrect, count(a.item_id) as total "
				+ "	from (select item_id, count(IF( answerstate='0',1,null))-count(IF( answerstate='1',1,null)) as incorrect "
				+ " from t_myquiz " + " where author_id='" + user.getId()
				+ "' and answerstate IN(0,1) "
				+ " group by item_id having incorrect<0) a where 1=1";
		
		List<Object[]> states = this.sessionFactory.getCurrentSession()
				.createSQLQuery(sql).list();

		BigInteger total = BigInteger.valueOf(0);
		for (Object[] state : states) {

			total = (BigInteger) state[1];
			// System.console().writer().println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+total);
		}

		results.put("correct", total.intValue());

		return results;
	}

	public BigInteger findMyQuizCompletedQuizzesCount(Users user) {
		Map<String, Integer> results = new HashMap<String, Integer>();

		String sql = "SELECT author_id,count(item_id) as c from t_myquiz"
				+ " WHERE not answerstate='-1' AND author_id='" + user.getId()
				+ "'" + "GROUP BY author_id order by c desc";

		List<Object[]> states = this.sessionFactory.getCurrentSession()
				.createSQLQuery(sql).list();

		BigInteger total = BigInteger.valueOf(0);
		for (Object[] state : states) {
			total = (BigInteger) state[1];
		}

		return total;
	}

	public Map<String, Integer> searchOneDayQuiz(Date day, Users user) {
		Map<String, Integer> results = new HashMap<String, Integer>();
		//
		// String hql = "from MyQuiz myquiz where author=:author ";
		// Map<String, Object> params = new HashMap<String, Object>();
		// params.put("author", user);
		//
		// String allcorrect_hql = hql + " and answerstate = 1";
		// List<MyQuiz> allcorrects = this.myquizDao.find(allcorrect_hql,
		// params);
		// results.put("allcorrecttimes", allcorrects.size());
		//
		// String allwrong_hql = hql + " and answerstate = 0";
		// List<MyQuiz> allwrongs = this.myquizDao.find(allwrong_hql, params);
		// results.put("allwrongtimes", allwrongs.size());
		//
		//
		// String allpass_hql = hql + " and answerstate = 2";
		// List<MyQuiz> allpass = this.myquizDao.find(allpass_hql, params);
		// results.put("allpasstimes", allpass.size());
		//
		// results.put("alltimes", allwrongs.size()+allcorrects.size());
		// results.put("allscores",
		// allwrongs.size()*2+allcorrects.size()*5+allpass.size()*2);
		//
		// if(day!=null){
		Calendar cal = Calendar.getInstance();
		cal.setTime(day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		Date fromday = cal.getTime();
		cal.add(Calendar.DATE, 1);
		Date today = cal.getTime();

		// params.put("toDay", today);
		// }
		//
		// String correct_hql = hql + " and answerstate = 1";
		// List<MyQuiz> corrects = this.myquizDao.find(correct_hql, params);
		// results.put("correcttimes", corrects.size());
		//
		// String wrong_hql = hql + " and answerstate = 0";
		// List<MyQuiz> wrongs = this.myquizDao.find(wrong_hql, params);
		// results.put("wrongtimes", wrongs.size());
		//
		// String pass_hql = hql + " and answerstate = 2";
		// List<MyQuiz> pass = this.myquizDao.find(pass_hql, params);
		// results.put("passtimes", pass.size());
		//
		// results.put("daytimes", wrongs.size()+corrects.size());
		// results.put("scores",
		// wrongs.size()*2+corrects.size()*5+pass.size()*2);

		String sql = "select answerstate, count(*) as num from t_myquiz where author_id = '"
				+ user.getId() + "'  group by answerstate";
		List<Object[]> states = this.sessionFactory.getCurrentSession()
				.createSQLQuery(sql).list();
		BigInteger allwrong = BigInteger.valueOf(0);
		BigInteger allright = BigInteger.valueOf(0);
		BigInteger allpass = BigInteger.valueOf(0);
		for (Object[] state : states) {
			Integer answerstate = (Integer) state[0];
			BigInteger num = (BigInteger) state[1];
			if (answerstate == 0) {
				allwrong = num;
			} else if (answerstate == 1) {
				allright = num;
			} else if (answerstate == 2) {
				allpass = num;
			}
		}

		String sql_today = "select answerstate, count(*) as num from t_myquiz where author_id = '"
				+ user.getId()
				+ "' and update_date > '"
				+ FormatUtil.formatYYYYMMDD(fromday)
				+ "' and update_date< '"
				+ FormatUtil.formatYYYYMMDD(today) + "' group by answerstate";
		List<Object[]> t_states = this.sessionFactory.getCurrentSession()
				.createSQLQuery(sql_today).list();
		BigInteger wrong = BigInteger.valueOf(0);
		BigInteger right = BigInteger.valueOf(0);
		BigInteger pass = BigInteger.valueOf(0);
		for (Object[] state : t_states) {
			Integer answerstate = (Integer) state[0];
			BigInteger num = (BigInteger) state[1];
			if (answerstate == 0) {
				wrong = num;
			} else if (answerstate == 1) {
				right = num;
			} else if (answerstate == 2) {
				pass = num;
			}
		}
		results.put("correcttimes", right.intValue());
		results.put("wrongtimes", wrong.intValue());
		results.put("passtimes", pass.intValue());
		results.put("scores", wrong.intValue() * 2 + right.intValue() * 5
				+ pass.intValue() * 2);

		results.put("alltimes", allwrong.intValue() + allright.intValue());
		results.put("allscores", allwrong.intValue() * 2 + allright.intValue()
				* 5 + allpass.intValue() * 2);

		return results;
	}

	public List<String> getItemIdsByIncorrectCount(int incor, Users user) {

		List<String> itemids = new ArrayList<String>();
		String having = "incorrect>0 and count(item_id)='" + incor + "'";
		if (incor == 3) {
			having = "incorrect>0 and count(item_id)>='" + incor + "'";
		} else if (incor == 4) {
			having = "incorrect<0 ";
		}
		// String sql =
		// "select item_id, count(item_id) as incorrect  from t_myquiz where "+answerstate+" and author_id='"+user.getId()+"' "+
		// " group by item_id having  "+ having;
		String sql = "select item_id, (count(IF( answerstate='0',1,null))-count(IF( answerstate='1',1,null))) as incorrect "
				+ " from t_myquiz "
				+ " where author_id='"
				+ user.getId()
				+ "' and answerstate IN(0,1) "
				+ " group by item_id having "
				+ having;
		List<Object[]> states = this.sessionFactory.getCurrentSession()
				.createSQLQuery(sql).list();

		for (Object[] state : states) {
			itemids.add((String) state[0]);
			// System.out.print("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+state[0]);
		}
		return itemids;
	}

	public List<UserQuizInfo> searchAllUsersQuizInfo() {
		List<Users> users = this.userService.searchAllUsers();
		List<UserQuizInfo> infos = new ArrayList<UserQuizInfo>();
		for (Users user : users) {
			UserQuizInfo info = new UserQuizInfo();
			String hql = "from MyQuiz myquiz where author=:author and answerstate = 1";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("author", user);
			List<MyQuiz> rightquizzes = this.myquizDao.find(hql, params);
			String hql1 = "from MyQuiz myquiz where author=:author and answerstate = 0";
			List<MyQuiz> wrongquizzes = this.myquizDao.find(hql1, params);

			Integer righttimes = 0;
			if (rightquizzes != null)
				righttimes = rightquizzes.size();
			Integer wrongtimes = 0;
			if (wrongquizzes != null)
				wrongtimes = wrongquizzes.size();

			String hql_item = "from Item item where author=:author and relogItem is null";
			List<Item> myitems = this.itemDao.find(hql_item, params);

			String hql_relog = "from Item item where author=:author and relogItem is not null";
			List<Item> myrelogs = this.itemDao.find(hql_relog, params);
			if (myitems != null)
				info.setUploadnumber(myitems.size());

			if (myrelogs != null)
				info.setRelognumber(myrelogs.size());

			String hql_read = "from LogUserReadItem readitem where readitem.user=:author";
			Map<String, Object> tempparams = new HashMap<String, Object>();
			tempparams.put("author", user);
			List<LogUserReadItem> readitems = this.logUserReadItemDao.find(
					hql_read, tempparams);
			info.setReferencenumber(readitems.size());

			String hql_myread = "from LogUserReadItem readitem where readitem.user=:author and readitem.item.author=:author";
			tempparams.put("author", user);
			List<LogUserReadItem> myreaditems = this.logUserReadItemDao.find(
					hql_myread, tempparams);
			info.setMyreferencenumber(myreaditems.size());

			info.setUser(user);
			info.setWrongtimes(wrongtimes);
			info.setRighttimes(righttimes);
			infos.add(info);
		}
		return infos;
	}

	// ■wakebe クイズスコアランキング
	public List<Object[]> getQuizScoreRanking() {
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "	SELECT user.*, SUM(CASE SC.answerstate WHEN 1 THEN SC.cnt*5 ELSE SC.cnt*2 END) AS score "
				+ "	FROM t_users AS user "
				+ "		INNER JOIN ( "
				+ "			SELECT "
				+ "				quiz.author_id, answerstate, count(quiz.answerstate) AS cnt "
				+ "				FROM t_myquiz AS quiz "
				+ "				WHERE quiz.answerstate >= 0 AND quiz.answerstate <= 2 "
				+ "				GROUP BY quiz.author_id,quiz.answerstate "
				+ "		) as SC ON(SC.author_id = user.id) "
				+ "		GROUP BY SC.author_id " + "		ORDER BY score DESC ";

		return session.createSQLQuery(sql).addEntity(Users.class)
				.addScalar("score").list();
	}

}
