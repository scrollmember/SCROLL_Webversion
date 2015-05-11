package jp.ac.tokushima_u.is.ll.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jp.ac.tokushima_u.is.ll.common.orm.Page;
import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.dao.UsersDao;
import jp.ac.tokushima_u.is.ll.entity.Author;
import jp.ac.tokushima_u.is.ll.entity.FileData;
import jp.ac.tokushima_u.is.ll.entity.Groupmember;
import jp.ac.tokushima_u.is.ll.entity.Kasetting;
import jp.ac.tokushima_u.is.ll.entity.KnowledgeRanking;
import jp.ac.tokushima_u.is.ll.entity.Language;
import jp.ac.tokushima_u.is.ll.entity.PlaceAnalysis;
import jp.ac.tokushima_u.is.ll.entity.Profile;
import jp.ac.tokushima_u.is.ll.entity.TDAfirstlayer;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.entity.Users.UsersAuth;
import jp.ac.tokushima_u.is.ll.entity.Usertest;
import jp.ac.tokushima_u.is.ll.form.ProfileEditForm;
import jp.ac.tokushima_u.is.ll.form.SettingForm;
import jp.ac.tokushima_u.is.ll.form.UserSearchCondForm;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.util.KeyGenerateUtil;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author houbin
 */
@Service("userService")
@Transactional
public class UserService {
	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	private HibernateDao<Users, String> usersDao;
	@Autowired
	private SignupService signupManager;
	@Autowired
	private LanguageService languageService;
	private HibernateDao<Language, String> languageDao;

	// ■wakebe クイズ情報取得のため
	@Autowired
	private MyQuizService myQuizService;
	// ■wakebe 回答数情報取得のため
	@Autowired
	private ItemService itemService;

	@Autowired
	private UsersDao usersDao2;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		usersDao = new HibernateDao<Users, String>(sessionFactory, Users.class);
		languageDao = new HibernateDao<Language, String>(sessionFactory,
				Language.class);
	}

	@Transactional(readOnly = true)
	public Users validateUser(String email, String password) {
		Users user = this.findByEmail(email);
		if (user != null
				&& user.getPassword().equals(DigestUtils.shaHex(password)))
			return user;
		else
			return null;
	}

	public boolean initSystemUser() {
		Long count = usersDao.findUnique("select count(*) from Users");
		if (count != 0) {
			return false;
		}
		logger.info("Start to init admin user");
		Users user = new Users();
		user.setPcEmail("learninglogforyou@gmail.com");
		user.setPassword(DigestUtils.shaHex("learn492357816"));
		user.setAuth(UsersAuth.ADMIN);
		user.setEnabled(true);
		user.setAccountNonLocked(true);
		Date current = Calendar.getInstance().getTime();
		user.setLastLogin(current);
		user.setCreateTime(current);
		user.setUpdateTime(current);
		usersDao.save(user);
		return true;
	}

	public List<Users> searchAllUsersList() {
		return this.usersDao.getAll();
	}

	@Transactional(readOnly = true)
	public Users getById(String userId) {
		Users user = this.usersDao.get(userId);
		Hibernate.initialize(user.getMyLangs());
		Hibernate.initialize(user.getStudyLangs());
		Hibernate.initialize(user);
		return user;
	}

	public void registerNewUser(String email) {
		Users users = usersDao.findUniqueBy("pcEmail", email);
		if (users == null) {
			Date current = Calendar.getInstance().getTime();
			users = new Users();
			users.setPcEmail(email);
			users.setEnabled(false);
			users.setCreateTime(current);
			users.setUpdateTime(current);
			users.setPassword("");
			users.setAuth(UsersAuth.MEMBER);
			users.setActivecode(UUID.randomUUID().toString()
					.replaceAll("-", ""));
			usersDao.save(users);
		} else {
			// users.setActivecode(UUID.randomUUID().toString().replaceAll("-",
			// ""));
			// usersDao.save(users);
		}
		// Send mail
		signupManager.sendActivationMail(email, users.getActivecode());
	}

	@Transactional(readOnly = true)
	public Users getByActivecode(String activecode) {
		return this.usersDao.findUniqueBy("activecode", activecode);
	}

	public void save(Users users) {
		// Auto encode password
		if (users.getPassword() != null && users.getPassword().length() != 40) {
			users.setPassword(DigestUtils.shaHex(users.getPassword()));
		}
		this.usersDao.save(users);
	}

	@Transactional(readOnly = true)
	Users findUniqueBy(String string, String email) {
		return usersDao.findUniqueBy(string, email);
	}

	@Transactional(readOnly = true)
	public Users findByEmail(String email) {
		return usersDao.findUniqueBy("pcEmail", email);
	}

	@Transactional(readOnly = true)
	public List<Users> findAllUsersByCondition(String propertyName, Object value) {
		return this.usersDao.findBy(propertyName, value);
	}

	public void updateLastLogin(String userId) {
		Users user = usersDao.get(userId);
		user.setLastLogin(Calendar.getInstance().getTime());
		usersDao.save(user);
	}

	@Transactional(readOnly = true)
	public List<Users> searchAllUsers() {
		return this.usersDao.getAll("nickname", true);
	}

	@Transactional(readOnly = true)
	public Page<Users> searchUserPageByCond(UserSearchCondForm form) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Users.class);
		if (!StringUtils.isBlank(form.getNickname())) {
			criteria.add(Restrictions.like("nickname", form.getNickname(),
					MatchMode.ANYWHERE));
		}

		if (!StringUtils.isBlank(form.getEmail())) {
			criteria.add(Restrictions.like("pcEmail", form.getEmail(),
					MatchMode.ANYWHERE));
		}

		if (!StringUtils.isBlank(form.getRealname())) {
			criteria.add(Restrictions.or(
					Restrictions.like("firstName", form.getRealname()),
					Restrictions.like("lastName", form.getRealname())));
		}

		Page<Users> userPage = new Page<Users>(20);
		if (form.getPage() == null || form.getPage() < 1) {
			form.setPage(1);
		}
		userPage.setPageNo(form.getPage());
		userPage.setOrder(Page.ASC);
		userPage.setOrderBy("pcEmail");

		return this.usersDao.findPage(userPage, criteria);
	}

	@Transactional(readOnly = true)
	public List<Users> findAllUserList(String email) {
		if (!StringUtils.isBlank(email)) {
			return usersDao
					.find("from Users u where u.pcEmail like ? order by u.pcEmail asc",
							email + "%");
		} else {
			return usersDao.getAll("pcEmail", true);
		}
	}

	public void createByAdmin(Users user) {
		Users u = new Users();
		u.setPcEmail(user.getPcEmail());
		u.setPassword(DigestUtils.shaHex(user.getPassword()));
		u.setAuth(user.getAuth());
		u.setEnabled(true);
		u.setAccountNonLocked(true);
		u.setLastLogin(null);
		Date current = new Date(System.currentTimeMillis());
		u.setCreateTime(current);
		u.setUpdateTime(current);
		u.setActivecode("");
		u.setNickname(user.getNickname());
		u.setFirstName(user.getFirstName());
		u.setLastName(user.getLastName());
		u.getMyLangs().add(languageService.findUniqueLangByCode("ja"));
		u.getStudyLangs().add(languageService.findUniqueLangByCode("en"));
		usersDao.save(u);
	}

	public void editByAdmin(Users user) {
		Users u = usersDao.get(user.getId());
		u.setPcEmail(user.getPcEmail());
		u.setAuth(user.getAuth());
		u.setEnabled(true);
		u.setAccountNonLocked(true);
		Date current = new Date(System.currentTimeMillis());
		u.setUpdateTime(current);
		u.setNickname(user.getNickname());
		u.setFirstName(user.getFirstName());
		u.setLastName(user.getLastName());
		if (!StringUtils.isBlank(user.getPassword())) {
			u.setPassword(DigestUtils.shaHex(user.getPassword()));
		}
		usersDao.save(u);
	}

	public void initLangTask() {
		List<Users> userList = usersDao.getAll();
		Language japanese = languageDao.findUniqueBy("code", "ja");
		Language english = languageDao.findUniqueBy("code", "en");
		for (Users user : userList) {
			user.addToMyLangs(japanese);
			user.addToStudyLangs(english);
			usersDao.save(user);
		}
	}

	public List<Users> findAllUserListNew() {
		return usersDao.getAll();
	}

	public Long findAllUserCount() {
		String hql = "select count(*) from Users user";
		return this.usersDao.findUnique(hql);
	}

	public List<Users> findAllUserListForWeeklyNotification() {
		List<Users> userList = this.usersDao.findBy(
				"receiveWeeklyNotification", true);
		if (userList.size() == 0) {
			this.usersDao
					.batchExecute(
							"update Users user set user.receiveWeeklyNotification = ? and user.enabled=true and accountNonLocked=true",
							true);
			userList = this.usersDao.findBy("receiveWeeklyNotification", true);
		}
		return userList;
	}

	public List<Users> searchAllUsersNew() {
		return usersDao.findBy("pcEmail", "toma.kunita@gmail.com");
	}

	public void checkFlags(String userId) {
		Users user = this.usersDao.get(userId);
		if (user.getReceiveWeeklyNotification() == null) {
			user.setReceiveWeeklyNotification(true);
			usersDao.save(user);
		}
	}

	public FileData getAvatarByUser(String id) {
		Users user = usersDao.get(id);
		FileData fileData = user.getAvatar();
		Hibernate.initialize(fileData);
		return fileData;
	}

	// ■wakebe 点数の取得
	public Integer getQuizAllScores() {
		Users user = this.getById(SecurityUserHolder.getCurrentUser().getId());
		Map<String, Integer> info = myQuizService.searchOneDayQuiz(new Date(),
				user);

		return (Integer) info.get("allscores");
	}

	// ■wakebe 次のレベルまでの経験値取得
	public int getNextExperiencePoint(String id) {
		Users user = usersDao.get(id);

		// (現在のレベル*5)^1.4 + 5 (仮)
		if (user.getUserLevel() == null) {
			user.setUserLevel(1);
		}
		double result = Math.pow(user.getUserLevel() * 5, 1.4) + 5;
		// double result = user.getUserLevel() * 10;

		return (int) result;
	}

	// ■wakebe 現在の経験値取得
	public Integer getNowExperiencePoint(String id) {
		Users user = usersDao.get(id);

		if (user.getExperiencePoint() == null) {
			user.setExperiencePoint(0);
		}
		return user.getExperiencePoint() + this.getQuizAllScores()
				+ this.itemService.getAnswerCount(id);
	}

	// ■wakebe 経験値の加算
	public void addExperiencePoint(String id, Integer exp) {
		Users user = usersDao.get(id);
		user.setExperiencePoint(user.getExperiencePoint() + exp);

		// レベルアップの処理 next経験値を超えたらレベルアップ レベルアップごとにnext経験値を更新
		while (getNowExperiencePoint(id) >= this.getNextExperiencePoint(id)) {
			user.setUserLevel(user.getUserLevel() + 1);
		}

		usersDao.save(user);
	}

	// ■wakebe レベルソート
	public List<Users> levelRanking() {
		String hql = "from Users user order by user.userLevel desc";
		List<Users> result = this.usersDao.find(hql);
		return result;
	}

	public Usertest findById(String userId) {
		Usertest user = this.usersDao2.findById(userId);
		return user;
	}

	public List<Users> findBynetworkuser() {
		// TODO Auto-generated method stub
		return usersDao2.findByNickname();
	}

	public void setting(SettingForm form, Usertest userid) {
		// TODO Auto-generated method stub
		String checkid=usersDao2.check(userid.getId());
		if(checkid==null){
		usersDao2.set(KeyGenerateUtil.generateIdUUID(),userid.getId(),form.getYifan(),form.getViewdistance(),form.getKaquality());
		}
		else{
			usersDao2.kaupdate(KeyGenerateUtil.generateIdUUID(),userid.getId(),form.getYifan(),form.getViewdistance(),form.getKaquality());
		}
	}

	public Kasetting getka(String id) {
		// TODO Auto-generated method stub
		
		return usersDao2.findka(id);
	}

	public void groupsetting(SettingForm form, Usertest userid) {
		// TODO Auto-generated method stub
		usersDao2.creategroup(KeyGenerateUtil.generateIdUUID(),userid.getId(),form.getGroupname(),form.getPrivacy());
}

	public List<Groupmember> getgroupmember(String id) {
		// TODO Auto-generated method stub
		return usersDao2.findgroupmember(id);
	}

	public void addmember(String id, String authorid, String groupname,String groupid) {
		// TODO Auto-generated method stub
		usersDao2.addmember(KeyGenerateUtil.generateIdUUID(),groupname,id,groupid);
	}

	public List<Groupmember> getgroupname(String id) {
		// TODO Auto-generated method stub
		return usersDao2.getjoingroup(id);
		
	}

	public List<Groupmember> allgroup(String id) {
		// TODO Auto-generated method stub
		return usersDao2.getallgroup(id);
	}

	public void deletemember(String groupid, String id) {
		// TODO Auto-generated method stub
		usersDao2.memberdelete(groupid,id);
	}

	public List<Author> getauthor(String groupid) {
		// TODO Auto-generated method stub
		return usersDao2.getauthor(groupid);
	}

	public List<Author> getallauthorid() {
		// TODO Auto-generated method stub
		return usersDao2.getallauthor();
	}

	public List<KnowledgeRanking> findknowledgeranking() {
		// TODO Auto-generated method stub
		return usersDao2.getknowledgeranking();
		
	}

	public List<PlaceAnalysis> findbymyplace(String id) {
		// TODO Auto-generated method stub
		return usersDao2.findbymayplace(id);
	}

	public void getByability(Profile form) {
		// TODO Auto-generated method stub
		usersDao2.insertability(form);
		
	}

	public List<Profile> checkid(String id) {
		// TODO Auto-generated method stub
		return usersDao2.checkuser(id);
	}

	public void updateability(Profile p) {
		// TODO Auto-generated method stub
		
		usersDao2.updateability(p);
		
	}

	public Profile findability(String id) {
		// TODO Auto-generated method stub
		return usersDao2.findability(id);
	}

	public List<TDAfirstlayer> findtdafirstlayer() {
		// TODO Auto-generated method stub
		return usersDao2.findtdafirstlayer();
	}
}
