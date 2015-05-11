package jp.ac.tokushima_u.is.ll.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;

import jp.ac.tokushima_u.is.ll.common.orm.Page;
import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.dao.ItemDao;
import jp.ac.tokushima_u.is.ll.dao.ItemTagDao;
import jp.ac.tokushima_u.is.ll.dao.ItemTitleDao;
import jp.ac.tokushima_u.is.ll.dao.LanguageDao;
import jp.ac.tokushima_u.is.ll.entity.Answer;
import jp.ac.tokushima_u.is.ll.entity.Category;
import jp.ac.tokushima_u.is.ll.entity.Cooccurrence;
import jp.ac.tokushima_u.is.ll.entity.FileData;
import jp.ac.tokushima_u.is.ll.entity.Groupmember;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.ItemComment;
import jp.ac.tokushima_u.is.ll.entity.ItemQuestionType;
import jp.ac.tokushima_u.is.ll.entity.ItemQueue;
import jp.ac.tokushima_u.is.ll.entity.ItemTag;
import jp.ac.tokushima_u.is.ll.entity.ItemTags;
import jp.ac.tokushima_u.is.ll.entity.ItemTitle;
import jp.ac.tokushima_u.is.ll.entity.ItemTitleBatis;
import jp.ac.tokushima_u.is.ll.entity.Itemlatlng;
import jp.ac.tokushima_u.is.ll.entity.Language;
import jp.ac.tokushima_u.is.ll.entity.LogUserReadItem;
import jp.ac.tokushima_u.is.ll.entity.MqQuiz;
import jp.ac.tokushima_u.is.ll.entity.Mychoice;
import jp.ac.tokushima_u.is.ll.entity.NetworkAnalysis;
import jp.ac.tokushima_u.is.ll.entity.PlaceAnalysis;
import jp.ac.tokushima_u.is.ll.entity.PlaceCollocation;
import jp.ac.tokushima_u.is.ll.entity.Question;
import jp.ac.tokushima_u.is.ll.entity.QuestionType;
import jp.ac.tokushima_u.is.ll.entity.Quizstore;
import jp.ac.tokushima_u.is.ll.entity.Results;
import jp.ac.tokushima_u.is.ll.entity.TDAsecondlayer;
import jp.ac.tokushima_u.is.ll.entity.TDAthirdlayer;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.entity.Usertest;
import jp.ac.tokushima_u.is.ll.entity.WordNet;
import jp.ac.tokushima_u.is.ll.entity.yesquizitemget;
import jp.ac.tokushima_u.is.ll.entity.pacall.SensePic;
import jp.ac.tokushima_u.is.ll.form.ItemCommentForm;
import jp.ac.tokushima_u.is.ll.form.ItemEditForm;
import jp.ac.tokushima_u.is.ll.form.ItemSearchCondForm;
import jp.ac.tokushima_u.is.ll.form.ItemSyncCondForm;
import jp.ac.tokushima_u.is.ll.quiz.QuizConstants;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.util.Constants;
import jp.ac.tokushima_u.is.ll.util.FilenameUtil;
import jp.ac.tokushima_u.is.ll.util.GeoUtils;
import jp.ac.tokushima_u.is.ll.util.KeyGenerateUtil;
import jp.ac.tokushima_u.is.ll.util.PicExifUtil;
import jp.ac.tokushima_u.is.ll.util.TextUtils;
import jp.ac.tokushima_u.is.ll.visualization.ReviewHistoryService;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.gridfs.GridFSDBFile;

import freemarker.template.TemplateException;

@Service
@Transactional
public class ItemService {

	private Logger logger = LoggerFactory.getLogger(ItemService.class);

	private HibernateDao<Item, String> itemDao;
	private HibernateDao<Answer, String> answerDao;
	private HibernateDao<Question, String> questionDao;
	private HibernateDao<Language, String> languageDao;
	private HibernateDao<Category, String> categoryDao;
	private HibernateDao<Users, String> usersDao;
	private HibernateDao<ItemTag, String> itemTagDao;
	private HibernateDao<ItemQueue, String> itemQueueDao;
	private HibernateDao<LogUserReadItem, String> logUserReadItemDao;
	private HibernateDao<QuestionType, String> questionTypeDao;
	private HibernateDao<ItemQuestionType, String> itemQuestionTypeDao;

	@Autowired
	private ItemQueueService itemQueueService;

	@Autowired
	private ReviewHistoryService reviewHistoryService;

	@Autowired
	private LanguageService languageService;
	@Autowired
	private UserService userService;
	@Autowired
	private MailService mailService;
	@Autowired
	private StaticServerService staticServerService;
	@Autowired
	private PropertyService propertyService;

	@Autowired
	private StorageService storageService;

	private SessionFactory sessionFactory;

	@Autowired
	private ItemTitleDao itemTitleDao2;
	@Autowired
	private ItemDao itemDao2;
	@Autowired
	private LanguageDao languageDao2;
	@Autowired
	private ItemTagDao itemtags2;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		itemDao = new HibernateDao<Item, String>(sessionFactory, Item.class);
		answerDao = new HibernateDao<Answer, String>(sessionFactory,
				Answer.class);
		questionDao = new HibernateDao<Question, String>(sessionFactory,
				Question.class);
		languageDao = new HibernateDao<Language, String>(sessionFactory,
				Language.class);
		categoryDao = new HibernateDao<Category, String>(sessionFactory,
				Category.class);
		usersDao = new HibernateDao<Users, String>(sessionFactory, Users.class);
		itemTagDao = new HibernateDao<ItemTag, String>(sessionFactory,
				ItemTag.class);
		logUserReadItemDao = new HibernateDao<LogUserReadItem, String>(
				sessionFactory, LogUserReadItem.class);
		questionTypeDao = new HibernateDao<QuestionType, String>(
				sessionFactory, QuestionType.class);
		itemQueueDao = new HibernateDao<ItemQueue, String>(sessionFactory,
				ItemQueue.class);
		itemQuestionTypeDao = new HibernateDao<ItemQuestionType, String>(
				sessionFactory, ItemQuestionType.class);
	}

	public Map<String, List<Question>> searchToAnswerQuestions() {
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		List<Language> motherlanguages = user.getMyLangs();
		List<Language> studylanguages = user.getStudyLangs();

		Map<String, List<Question>> result = new HashMap<String, List<Question>>();

		if (motherlanguages != null && motherlanguages.size() > 0) {
			Page<Question> toAnswerPage = new Page<Question>(10);
			String hql = "from Question q where q.answerSet is empty and q.language in (:paramlanguages )";
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("paramlanguages", motherlanguages);
			List<Question> answerresults = this.questionDao.findPage(
					toAnswerPage, hql, param).getResult();
			result.put("toAnswerQuestions", answerresults);
		}

		if (studylanguages != null && studylanguages.size() > 0) {
			Page<Question> toStudyPage = new Page<Question>(10);
			String hql = "from Question q where q.answerSet is not empty and q.language in (:paramlanguages )";
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("paramlanguages", studylanguages);
			List<Question> studyresults = this.questionDao.findPage(
					toStudyPage, hql, param).getResult();
			result.put("toStudyQuestions", studyresults);
		}

		Page<Question> answeredPage = new Page<Question>(10);
		String answeredHql = "select q from Question q join q.item i where i.author=? and q.answerSet is not empty order by i.createTime desc";
		this.questionDao.findPage(answeredPage, answeredHql, user);
		result.put("answeredQuestions", answeredPage.getResult());
		return result;
	}

	public List<QuestionType> searchAllQuestionTypes() {
		return this.questionTypeDao.getAll("orderby", true);
	}

	@SuppressWarnings("unchecked")
	public List<Item> searchAllToAnswer() {
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		List<Language> motherlanguages = user.getMyLangs();
		String hql = "from Item item where item.question.language in (:languages)";
		List<Item> toanswermore = null;
		if (motherlanguages != null && motherlanguages.size() > 0) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("languages", motherlanguages);
			Query query = itemDao.createQuery(hql, param);
			toanswermore = query.list();
		}
		return toanswermore;
	}

	public Item uploadImageFirst(File image) throws IOException {
		Date current = new Date(System.currentTimeMillis());

		Item item = new Item();

		double[] position = null;
		position = PicExifUtil.readGps(new BufferedInputStream(
				new FileInputStream(image)));
		if (position != null) {
			item.setItemLat(position[0]);
			item.setItemLng(position[1]);
		}

		item.setItemZoom(10);
		item.setCreateTime(current);
		item.setUpdateTime(current);

		// Author
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		item.setAuthor(user);

		FileData fileData = new FileData();
		fileData.setOrigName(image.getName());
		fileData.setCreatedAt(current);
		fileData.setMd5(DigestUtils.md5Hex(new FileInputStream(image)));
		String fileType = "";
		if (!StringUtils.isBlank(image.getName())) {
			fileType = FilenameUtil.checkMediaType(image.getName());
			fileData.setFileType(fileType);
		}

		item.setImage(fileData);
		item.setDisabled(1);// Disable
		itemDao.save(item);

		staticServerService.uploadFile(item.getImage().getId(), StringUtils
				.lowerCase(FilenameUtils.getExtension(image.getName())),
				FileUtils.readFileToByteArray(image));
		if ("image".equals(fileData.getFileType())) {
			List<FileData> fileDataList = new ArrayList<FileData>();
			fileDataList.add(fileData);
		}
		return item;
	}

	public Item uploadImageFirst(MultipartFile image) throws IOException {

		Date current = new Date(System.currentTimeMillis());

		Item item = new Item();

		double[] position = null;
		position = PicExifUtil.readGps(new BufferedInputStream(image
				.getInputStream()));
		if (position != null) {
			item.setItemLat(position[0]);
			item.setItemLng(position[1]);
		}

		item.setItemZoom(10);
		item.setCreateTime(current);
		item.setUpdateTime(current);

		// Author
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		item.setAuthor(user);

		FileData fileData = new FileData();
		fileData.setOrigName(image.getOriginalFilename());
		fileData.setCreatedAt(current);
		fileData.setMd5(DigestUtils.md5Hex(image.getInputStream()));
		String fileType = "";
		if (!StringUtils.isBlank(image.getOriginalFilename())) {
			fileType = FilenameUtil.checkMediaType(image.getOriginalFilename());
			fileData.setFileType(fileType);
		}

		item.setImage(fileData);
		item.setDisabled(1);// Disable
		itemDao.save(item);
		staticServerService.uploadFile(item.getImage().getId(), StringUtils
				.lowerCase(FilenameUtils.getExtension(image
						.getOriginalFilename())), image.getBytes());
		if ("image".equals(fileData.getFileType())) {
			List<FileData> fileDataList = new ArrayList<FileData>();
			fileDataList.add(fileData);
		}
		return item;
	}

	public Item uploadImageFirst(SensePic pic) throws IOException {
		String fileId = pic.getFileId();

		if (StringUtils.isBlank(fileId)) {
			throw new RuntimeException("fileId is null");
		}
		GridFSDBFile file = storageService.findOneFile(fileId);
		if (file == null) {
			throw new RuntimeException("file is not exist");
		}
		Date current = new Date(System.currentTimeMillis());

		Item item = new Item();

		double[] position = null;
		position = PicExifUtil.readGps(new BufferedInputStream(file
				.getInputStream()));
		if (position != null) {
			item.setItemLat(position[0]);
			item.setItemLng(position[1]);
		}

		item.setItemZoom(10);
		item.setCreateTime(current);
		item.setUpdateTime(current);

		// Author
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		item.setAuthor(user);

		FileData fileData = new FileData();
		fileData.setOrigName(file.getFilename());
		fileData.setCreatedAt(current);
		fileData.setMd5(file.getMD5());
		String fileType = "";
		if (!StringUtils.isBlank(file.getFilename())) {
			fileType = FilenameUtil.checkMediaType(file.getFilename());
			fileData.setFileType(fileType);
		}

		item.setImage(fileData);
		item.setDisabled(1);// Disable
		itemDao.save(item);
		byte[] fileBytes = FileCopyUtils.copyToByteArray(file.getInputStream());
		staticServerService.uploadFile(item.getImage().getId(), StringUtils
				.lowerCase(FilenameUtils.getExtension(file.getFilename())),
				fileBytes);
		if ("image".equals(fileData.getFileType())) {
			List<FileData> fileDataList = new ArrayList<FileData>();
			fileDataList.add(fileData);
		}
		return item;
	}

	public Item createByForm(ItemEditForm form) throws IOException {
		Date current = new Date(System.currentTimeMillis());

		Item item = new Item();
		List<Long> quesTypeIds = form.getQuestionTypeIds();

		boolean imageFlg = false;

		// Attached File
		if (form.getImage() != null) {
			MultipartFile file = form.getImage();
			if (!file.isEmpty() && file.getSize() != 0) {
				FileData image = new FileData();
				MultipartFile uploadFile = form.getImage();
				image.setOrigName(uploadFile.getOriginalFilename());
				image.setCreatedAt(current);
				image.setMd5(DigestUtils.md5Hex(uploadFile.getInputStream()));
				// FileBin bin = new FileBin();
				// bin.setBin(uploadFile.getBytes());
				// image.setFileBin(bin);

				String fileType = "";
				if (!StringUtils.isBlank(uploadFile.getOriginalFilename())) {
					fileType = FilenameUtil.checkMediaType(uploadFile
							.getOriginalFilename());
					image.setFileType(fileType);
					if (FilenameUtil.IMAGE.equals(fileType))
						imageFlg = true;
				}

				item.setImage(image);
			}
		}

		if (quesTypeIds != null
				&& quesTypeIds.contains(Constants.QuizTypeImageMutiChoice)
				&& !imageFlg) {
			quesTypeIds.remove(Constants.QuizTypeImageMutiChoice);
		}

		if (!StringUtils.isBlank(form.getCategoryId())) {
			Category category = categoryDao.get(form.getCategoryId());
			if (category != null) {
				item.setCategory(category);
			} else {
				item.setCategory(null);
			}
		} else {
			item.setCategory(null);
		}

		List<ItemQuestionType> quesTypes = new ArrayList<ItemQuestionType>();
		List<ItemTitle> titleSet = new ArrayList<ItemTitle>();
		for (String key : form.getTitleMap().keySet()) {
			Language lang = this.languageService.findUniqueLangByCode(key);
			if (lang == null
					|| StringUtils.isBlank(form.getTitleMap().get(key)))
				continue;
			ItemTitle title = new ItemTitle();
			title.setContent(form.getTitleMap().get(key));
			title.setLanguage(lang);
			title.setItem(item);
			titleSet.add(title);

			if (quesTypeIds != null
					&& quesTypeIds.contains(Constants.QuizTypeImageMutiChoice)) {
				ItemQuestionType qt = new ItemQuestionType();
				qt.setItem(item);
				qt.setLanguage(lang);
				qt.setQuestionType(this.questionTypeDao.findUniqueBy("id",
						Constants.QuizTypeImageMutiChoice));
				quesTypes.add(qt);
			}
			if (quesTypeIds != null
					&& quesTypeIds.contains(Constants.QuizTypeTextMutiChoice)) {
				ItemQuestionType qt = new ItemQuestionType();
				qt.setItem(item);
				qt.setLanguage(lang);
				qt.setQuestionType(this.questionTypeDao.findUniqueBy("id",
						Constants.QuizTypeTextMutiChoice));
				quesTypes.add(qt);
			}
		}

		if (quesTypeIds != null
				&& quesTypeIds.contains(Constants.QuizTypeYesNoQuestion)) {
			ItemQuestionType qt = new ItemQuestionType();
			qt.setItem(item);
			qt.setQuestionType(this.questionTypeDao.findUniqueBy("id",
					Constants.QuizTypeYesNoQuestion));
			quesTypes.add(qt);
		}

		item.setQuestionTypes(quesTypes);

		item.setTitles(titleSet);

		item.setBarcode(form.getBarcode());
		item.setQrcode(form.getQrcode());
		item.setRfid(form.getRfid());
		double[] position = null;
		if (form.getImage() != null && !form.getImage().isEmpty()) {
			try {
				position = PicExifUtil.readGps(new BufferedInputStream(form
						.getImage().getInputStream()));
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		if (position == null) {
			item.setItemLat(form.getItemLat());
			item.setItemLng(form.getItemLng());
		} else {
			item.setItemLat(position[0]);
			item.setItemLng(position[1]);
		}
		if (form.getSpeed() != null)
			item.setSpeed(form.getSpeed());

		item.setItemZoom(form.getItemZoom());
		item.setNote(form.getNote());
		item.setPlace(form.getPlace());
		item.setCreateTime(current);
		item.setUpdateTime(current);
		item.setShareLevel(form.getShareLevel());

		// Author
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		item.setAuthor(user);

		// Tags
		if (!StringUtils.isBlank(form.getTag())) {
			List<String> tagArray = TextUtils.splitString(form.getTag());
			for (String t : tagArray) {
				ItemTag tag = itemTagDao.findUniqueBy("tag", t.trim());
				if (tag == null) {
					tag = new ItemTag();
					tag.setTag(t.trim());
					tag.setCreateTime(current);
					tag.setUpdateTime(current);
					itemTagDao.save(tag);
				}
				item.getItemTags().add(tag);
			}
		}

		if (!StringUtils.isBlank(form.getQuestion())) {
			Question question = new Question();
			question.setContent(form.getQuestion());
			question.setLanguage(languageDao.findUniqueBy("code",
					form.getQuesLan()));
			item.setQuestion(question);
		}

		item.setDisabled(0);

		boolean locationFlg = false;
		if (form.getLocationBased() != null
				&& form.getLocationBased().booleanValue()) {
			locationFlg = GeoUtils.isHighPrecision(item.getItemLat(),
					item.getItemLng());
		}
		item.setLocationBased(locationFlg);

		// Save to database
		itemDao.save(item);

		if(form.getMap()!=null){
			Iterator entries = form.getMap().entrySet().iterator();
			while(entries.hasNext()) {
				Map.Entry entry = (Map.Entry)entries.next();
				System.out.println((String)entry.getKey());
				System.out.println((List<String>) entry.getValue());
				this.insertplace(item.getId(),(String)entry.getKey(),(List<String>) entry.getValue());
			}
			
		}
		
		
		itemQueueService.updateItemQueue(item, user,
				QuizConstants.QueueTypeNewObject);
		reviewHistoryService.updateItemState(item, user,
				Constants.ExperiencedState, null);

		if (form.getImage() != null && !form.getImage().isEmpty()) {
			staticServerService.uploadFile(item.getImage().getId(), StringUtils
					.lowerCase(FilenameUtils.getExtension(form.getImage()
							.getOriginalFilename())), form.getImage()
					.getBytes());
			if ("image".equals(item.getImage().getFileType())) {
				List<FileData> fileDataList = new ArrayList<FileData>();
				fileDataList.add(item.getImage());
			}
		}

		// this.llquizService.insertQuizzes(item);
		return item;
	}

	public Item updateByForm(String id, ItemEditForm form) throws IOException {
		Date current = new Date(System.currentTimeMillis());
		Item item = this.itemDao.get(id);

		if (!StringUtils.isBlank(form.getCategoryId())) {
			Category category = categoryDao.get(form.getCategoryId());
			if (category != null) {
				item.setCategory(category);
			} else {
				item.setCategory(null);
			}
		} else {
			item.setCategory(null);
		}

		String fileType = "";
		// Attached File
		if (form.getImage() != null) {
			MultipartFile file = form.getImage();
			if (file != null && !file.isEmpty() && file.getSize() != 0) {
				FileData image = new FileData();
				MultipartFile uploadFile = form.getImage();
				image.setOrigName(uploadFile.getOriginalFilename());
				image.setCreatedAt(current);
				image.setMd5(DigestUtils.md5Hex(uploadFile.getInputStream()));
				if (!StringUtils.isBlank(uploadFile.getOriginalFilename())) {
					fileType = FilenameUtil.checkMediaType(uploadFile
							.getOriginalFilename());
					image.setFileType(fileType);
				}

				// FileBin bin = new FileBin();
				// bin.setBin(uploadFile.getBytes());
				// image.setFileBin(bin);

				item.setImage(image);
			}
		} else if (item.getImage() != null)
			fileType = item.getImage().getFileType();

		List<Long> quesTypeIds = form.getQuestionTypeIds();
		if (!FilenameUtil.IMAGE.equals(fileType) && quesTypeIds != null
				&& quesTypeIds.contains(Constants.QuizTypeImageMutiChoice)) {
			quesTypeIds.remove(Constants.QuizTypeImageMutiChoice);
		}

		List<ItemQuestionType> quesTypes = new ArrayList<ItemQuestionType>();

		List<ItemTitle> titleSet = item.getTitles();
		titleSet.clear();
		for (String key : form.getTitleMap().keySet()) {
			Language lang = this.languageService.findUniqueLangByCode(key);
			if (lang == null
					|| StringUtils.isBlank(form.getTitleMap().get(key)))
				continue;
			ItemTitle title = new ItemTitle();
			title.setContent(form.getTitleMap().get(key));
			title.setLanguage(lang);
			title.setItem(item);
			titleSet.add(title);

			if (quesTypeIds != null
					&& quesTypeIds.contains(Constants.QuizTypeImageMutiChoice)) {
				ItemQuestionType qt = new ItemQuestionType();
				qt.setItem(item);
				qt.setLanguage(lang);
				qt.setQuestionType(this.questionTypeDao.findUniqueBy("id",
						Constants.QuizTypeImageMutiChoice));
				quesTypes.add(qt);
			}
			if (quesTypeIds != null
					&& quesTypeIds.contains(Constants.QuizTypeTextMutiChoice)) {
				ItemQuestionType qt = new ItemQuestionType();
				qt.setItem(item);
				qt.setLanguage(lang);
				qt.setQuestionType(this.questionTypeDao.findUniqueBy("id",
						Constants.QuizTypeTextMutiChoice));
				quesTypes.add(qt);
			}
		}

		if (quesTypeIds != null
				&& quesTypeIds.contains(Constants.QuizTypeYesNoQuestion)) {
			ItemQuestionType qt = new ItemQuestionType();
			qt.setItem(item);
			qt.setQuestionType(this.questionTypeDao.findUniqueBy("id",
					Constants.QuizTypeYesNoQuestion));
			quesTypes.add(qt);
		}
		List<ItemQuestionType> oldtypes = item.getQuestionTypes();
		if (oldtypes != null) {
			for (ItemQuestionType quesType : oldtypes) {
				this.itemQuestionTypeDao.delete(quesType);
			}
		}
		item.setQuestionTypes(quesTypes);

		item.setTitles(titleSet);

		item.setBarcode(form.getBarcode());
		item.setQrcode(form.getQrcode());
		item.setRfid(form.getRfid());
		item.setPlace(form.getPlace());
		double[] position = null;
		if (form.getImage() != null) {
			position = PicExifUtil.readGps(new BufferedInputStream(form
					.getImage().getInputStream()));
		}
		if (position == null) {
			item.setItemLat(form.getItemLat());
			item.setItemLng(form.getItemLng());
		} else {
			item.setItemLat(position[0]);
			item.setItemLng(position[1]);
		}
		item.setItemZoom(form.getItemZoom());
		item.setNote(form.getNote());
		item.setUpdateTime(current);
		item.setShareLevel(form.getShareLevel());
		if (form.getQuestion() == null || form.getQuestion().length() <= 0) {
			if (item.getQuestion() != null) {
				this.questionDao.delete(item.getQuestion());
				item.setQuestion(null);
			}
		} else {
			Question question = null;
			if (item.getQuestion() == null) {
				question = new Question();
			} else {
				question = item.getQuestion();
			}
			question.setContent(form.getQuestion());
			Language language = this.languageDao.findUniqueBy("code",
					form.getQuesLan());
			question.setLanguage(language);
			item.setQuestion(question);
			this.questionDao.save(question);
		}

		boolean locationFlg = false;
		if (form.getLocationBased() != null
				&& form.getLocationBased().booleanValue()) {
			locationFlg = GeoUtils.isHighPrecision(item.getItemLat(),
					item.getItemLng());
		}
		item.setLocationBased(locationFlg);
		// if(form.getQuestionTypeIds()!=null){
		// List<String>quesTypeIds = form.getQuestionTypeIds();
		// if(quesTypeIds!=null&&quesTypeIds.size()>0){
		// item.getQuestionTypes().clear();
		// for(String quesTypeId:quesTypeIds){
		// QuestionType qt = this.questionTypeDao.findUniqueBy("id",
		// quesTypeId);
		// if(qt!=null){
		// item.addQuestionType(qt);
		// }
		// }
		// }
		// }else
		// item.setQuestionTypes(new HashSet<QuestionType>());
		item.setDisabled(0);

		this.itemDao.save(item);

		if (form.getImage() != null && !form.getImage().isEmpty()) {
			staticServerService.uploadFile(item.getImage().getId(), StringUtils
					.lowerCase(FilenameUtils.getExtension(form.getImage()
							.getOriginalFilename())), form.getImage()
					.getBytes());
			if ("image".equals(item.getImage().getFileType())) {
				List<FileData> fileDataList = new ArrayList<FileData>();
				fileDataList.add(item.getImage());
			}
		}

		// this.llquizService.updateQuizzes(item);
		return item;
	}

	public void createCommentByForm(String itemId, ItemCommentForm form) {
		Date current = Calendar.getInstance().getTime();
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		// Tags
		Item item = this.findById(itemId);

		if (!StringUtils.isBlank(form.getTag())) {
			List<String> tagArray = TextUtils.splitString(form.getTag());
			for (String t : tagArray) {
				ItemTag tag = itemTagDao.findUniqueBy("tag", t.trim());
				if (tag == null) {
					tag = new ItemTag();
					tag.setTag(t.trim());
					tag.setCreateTime(current);
					tag.setUpdateTime(current);
					itemTagDao.save(tag);
				}
				item.getItemTags().add(tag);
			}
		}

		if (!StringUtils.isBlank(form.getComment())) {
			ItemComment comment = new ItemComment();
			comment.setComment(form.getComment());
			comment.setCreateTime(current);
			comment.setUpdateTime(current);
			comment.setUser(user);
			item.addToCommentList(comment);
			try {
				ModelMap model = new ModelMap();
				model.addAttribute("username", item.getAuthor().getNickname());
				model.addAttribute("url", propertyService.getSystemUrl()
						+ "/item/" + item.getId());
				model.addAttribute("commentusername", user.getNickname());
				mailService.sendSysMail(item.getAuthor().getPcEmail(),
						"Your have a comment", "commentMail", model);
			} catch (MessagingException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (TemplateException ex) {
				ex.printStackTrace();
			}
		}

		item.setUpdateTime(current);
		this.itemDao.save(item);
	}

	public void createAnswerByForm(String itemId, String content) {
		Date current = Calendar.getInstance().getTime();
		Item item = this.findById(itemId);
		Question question = item.getQuestion();
		Answer answer = new Answer();
		answer.setAnswer(content);
		answer.setQuestion(question);
		answer.setCreateDate(new Date());

		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		answer.setAuthor(user);

		if (!user.getId().equals(item.getAuthor().getId())) {
			try {
				ModelMap model = new ModelMap();
				model.addAttribute("username", item.getAuthor().getNickname());
				model.addAttribute("url", propertyService.getSystemUrl()
						+ "/item/" + item.getId());
				model.addAttribute("answerusername", user.getNickname());
				mailService.sendSysMail(item.getAuthor().getPcEmail(),
						"Your question has been answered",
						"questionAnsweredMail", model);
			} catch (MessagingException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (TemplateException ex) {
				ex.printStackTrace();
			}
		}
		item.setUpdateTime(current);
		this.answerDao.save(answer);
		this.itemDao.save(item);
	}

	@Transactional(readOnly = true)
	public List<Item> searchMyTodayItem(Users user, Date todaystart,
			Date todayend) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Item.class);
		criteria.add(Restrictions.eq("author", user));
		criteria.add(Restrictions.ge("createTime", todaystart));
		criteria.add(Restrictions.lt("createTime", todayend));
		criteria.add(Restrictions.ne("disabled", new Integer(1)));
		return this.itemDao.find(criteria);
	}

	@Transactional(readOnly = true)
	public void searchMyItem(ModelMap model) {
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		String hql = "from Item item where item.disabled!=1 and item.author =:user order by item.updateTime desc";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("user", user);
		Query query = itemDao.createQuery(hql, param);
		model.addAttribute("myitems", query.list());
		model.addAttribute("user", user);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Item> searchMyItem(Users user) {
		String hql = "from Item item where item.disabled!=1 and item.author =:user order by item.updateTime desc";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("user", user);
		Query query = itemDao.createQuery(hql, param);
		return query.list();
	}

	// @Transactional(readOnly = true)
	// public List<Item> searchAllItems() {
	// List<Item> list = this.itemDao.getAll("updateTime", false);
	// return list;
	// }
	//
	// @Transactional(readOnly = true)
	// public List<Item> searchAllItemsByTitle(String title) {
	// List<Item> list = this.itemDao
	// .find(
	// "from Item item where item.zhTitle like ? or item.jpTitle like ? or item.enTitle like ? order by item.updateTime",
	// "%" + title + "%", "%" + title + "%", "%" + title + "%");
	// return list;
	// }

	@Transactional(readOnly = true)
	public List<Item> searchAllItemsByCond(ItemSearchCondForm form) {
		List<Item> list = new ArrayList<Item>();
		DetachedCriteria criteria = this.buildCriteriaByForm(form);
		if (criteria == null)
			return list;

		criteria.addOrder(Order.desc("updateTime"));
		list = this.itemDao.find(criteria);
		return list;
	}

	@Transactional(readOnly = true)
	public Page<Item> searchItemPageByCond(ItemSearchCondForm form) {

		DetachedCriteria criteria = buildCriteriaByForm(form);
		Page<Item> itemPage = new Page<Item>(10);
		if (criteria == null)
			return itemPage;
		if (form.getPage() == null || form.getPage() < 1) {
			form.setPage(1);
		}
		itemPage.setPageNo(form.getPage());
		itemPage.setOrder(Page.DESC);
		itemPage.setOrderBy("updateTime");
		return this.itemDao.findPage(itemPage, criteria);
	}

	@Transactional(readOnly = true)
	public List<Item> searchItemsByCond(ItemSearchCondForm form) {
		DetachedCriteria criteria = buildCriteriaByForm(form);
		return this.itemDao.find(criteria);
	}

	@Transactional(readOnly = true)
	public List<Item> searchRelatedItemForTask(String taskId, String querystr,
			Integer limit) {
		String sql = "select i.* " + "from t_item i, ( "
				+ "SELECT MIN(it.item) as item " + "FROM t_item_title it "
				+ "where it.content like '%" + querystr + "%' "
				+ "GROUP BY it.content " + ") title "
				+ "where i.id = title.item " + "and  title.item not in ( "
				+ "select taskitem.item_id " + "from t_task_item taskitem "
				+ "where taskitem.task_id = '" + taskId + "' " + ") ";
		if (limit != null)
			sql += "limit 0, " + limit;
		Session session = this.sessionFactory.getCurrentSession();
		return session.createSQLQuery(sql).addEntity(Item.class).list();
	}

	@Transactional(readOnly = true)
	public List<Item> searchSyncItems(ItemSyncCondForm form) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Item.class);

		// By authorId
		if (!StringUtils.isBlank(form.getUserId())) {
			criteria.createAlias("author", "author").add(
					Restrictions.eq("author.id", form.getUserId()));
		} else if (!StringUtils.isBlank(form.getNotuserId())) {
			criteria.createAlias("author", "author").add(
					Restrictions.ne("author.id", form.getNotuserId()));
		}

		// By update date
		if (form.getUpdateDateFrom() != null) {
			criteria.add(Restrictions.ge("updateTime", form.getUpdateDateFrom()));
		}
		if (form.getUpdateDateTo() != null) {
			criteria.add(Restrictions.le("updateTime", form.getUpdateDateTo()));
		}

		if (form.getNum() != null) {
			Page<Item> itemPage = new Page<Item>(form.getNum());
			itemPage.setPageNo(1);
			itemPage.setOrder(Page.DESC);
			itemPage.setOrderBy("updateTime");
			return this.itemDao.findPage(itemPage, criteria).getResult();
		}
		criteria.addOrder(Order.desc("updateTime"));
		return this.itemDao.find(criteria);
	}

	/**
	 * Find objects around the given coordinate(lat, lng)
	 * 
	 * @param lat
	 *            緯度(-90~90)
	 * @param lng
	 *            　経度(-180~180)
	 * @param distance
	 *            unit:km
	 * @param userId
	 *            Nullable, if it is null, search in all users' items
	 * @param itemSize
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<Item> searchNearestItems(String avoidItemId, double lat,
			double lng, double distance, String userId, int itemSize) {
		if (itemSize == 0)
			itemSize = 1;
		DetachedCriteria criteria = DetachedCriteria.forClass(Item.class);
		criteria.add(Restrictions.ne("disabled", new Integer(1)));
		String userCond = "";
		if (userId != null) {
			userCond = " and i.author_id='" + userId + "'";
		}
		String avoidCond = "";
		if (avoidItemId != null) {
			avoidCond = " and i.id<>'" + avoidItemId + "'";
		}
		criteria.add(Restrictions.isNull("relogItem"));
		String sql = "select "
				+ " t.*"
				+ "	from"
				+ "		("
				+ "		select i.*, get_distance("
				+ lat
				+ ","
				+ lng
				+ ", i.item_lat, i.item_lng) as distance"
				+ "		from t_item i"
				+ "		where i.disabled!=1 and i.item_lat is not null and i.item_lng is not null and i.relog_item is null"
				+ userCond + avoidCond + "		order by distance asc" + "	) as t"
				+ "	where" + "		t.distance>=0 and t.distance<=" + distance
				+ " limit 0, " + itemSize;
		Page<Item> itemPage = new Page<Item>(itemSize);

		Session session = this.sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Item> itemList = session.createSQLQuery(sql).addEntity(Item.class)
				.list();
		itemPage.setResult(itemList);
		return itemPage;
	}

	@SuppressWarnings("unchecked")
	public List<Item> searchNearestItemsWithoutNotified(double lat, double lng,
			double distance, int itemSize) {
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		String sql = "select "
				+ " t.*"
				+ "	from"
				+ "		("
				+ "		select i.*, get_distance("
				+ lat
				+ ","
				+ lng
				+ ", i.item_lat, i.item_lng) as distance"
				+ "		from t_item i"
				+ "		where i.disabled!=1 and i.item_lat is not null and i.item_lng is not null and i.relog_item is null and (i.locationbased is null or i.locationbased =1)"
				+ " 	and i.id not in ( select distinct ia.item  from t_itemalarm ia 		"
				+ "	where ia.author_id = '" + user.getId()
				+ "' and i.id = ia.item and ia.alarm_type = 0)"
				+ "		order by distance asc" + "	) as t" + "	where"
				+ "		t.distance>=0 and t.distance<=" + distance + " limit 0, "
				+ itemSize;
		Session session = this.sessionFactory.getCurrentSession();
		return session.createSQLQuery(sql).addEntity(Item.class).list();
	}

	/**
	 * Search latest uploaded items
	 * 
	 * @param userId
	 *            Nullable, if it is null, search in all users' items
	 * @param itemSize
	 *            more than 0
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<Item> searchLatestItems(String avoidItemId, Long userId,
			int itemSize) {
		if (itemSize == 0)
			itemSize = 1;
		DetachedCriteria criteria = DetachedCriteria.forClass(Item.class);
		criteria.add(Restrictions.ne("disabled", new Integer(1)));
		if (userId != null) {
			criteria.createAlias("author", "author").add(
					Restrictions.eq("author.id", userId));
		}
		criteria.add(Restrictions.isNull("relogItem"));
		if (avoidItemId != null)
			criteria.add(Restrictions.ne("id", avoidItemId));
		Page<Item> itemPage = new Page<Item>(itemSize);
		itemPage.setOrder(Page.DESC);
		itemPage.setOrderBy("createTime");
		return itemDao.findPage(itemPage, criteria);
	}

	private DetachedCriteria buildCriteriaByForm(ItemSearchCondForm form) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Item.class);
		criteria.add(Restrictions.ne("disabled", new Integer(1)));
		// criteria.createAlias("author", "author").add(
		// Restrictions.eq("author.id", "ff8081813d441a99013d81a6cf241ecc"));
		// criteria.add(Restrictions.eq("item_lng",
		// "ff8081813d441a99013d81a6cf241ecc"));
		// By Title
		
		if (!StringUtils.isBlank(form.getTitle())) {
			criteria.createAlias("titles", "title").add(
					Restrictions.like("title.content", form.getTitle(),
							MatchMode.ANYWHERE));
			// if (StringUtils.isBlank(form.getLang())) {
			// criteria.add(Restrictions.or(Restrictions.or(Restrictions.like(
			// "jpTitle", form.getTitle(), MatchMode.ANYWHERE),
			// Restrictions.like("enTitle", form.getTitle(),
			// MatchMode.ANYWHERE)), Restrictions.like(
			// "zhTitle", form.getTitle(), MatchMode.ANYWHERE)));
			// } else if ("en".equals(form.getLang())) {
			// criteria.add(Restrictions.like("enTitle", form.getTitle(),
			// MatchMode.ANYWHERE));
			// } else if ("jp".equals(form.getLang())) {
			// criteria.add(Restrictions.like("jpTitle", form.getTitle(),
			// MatchMode.ANYWHERE));
			// } else if ("zh".equals(form.getLang())) {
			// criteria.add(Restrictions.like("zhTitle", form.getTitle(),
			// MatchMode.ANYWHERE));
			// }
		}

		// By author or authorId
		if (!StringUtils.isBlank(form.getUserId())) {
			criteria.createAlias("author", "author").add(
					Restrictions.eq("author.id", form.getUserId()));
			// criteria.createAlias("author", "author").add(
			// Restrictions.eq("author.id",
			// "ff8081813d441a99013d81a6cf241ecc"));

		} else if (!StringUtils.isBlank(form.getNickname())) {
			criteria.createAlias("author", "author").add(
					Restrictions.like("author.nickname", form.getNickname(),
							MatchMode.ANYWHERE));
		} else if (!StringUtils.isBlank(form.getUsername())) {
			criteria.createAlias("author", "author").add(
					Restrictions.eq("author.nickname", form.getUsername()));
		} else if (!StringUtils.isBlank(form.getNotuserId())) {
			criteria.createAlias("author", "author").add(
					Restrictions.ne("author.id", form.getUserId()));
		}

		// By Map range
		if (form.isMapenabled() && form.getX1() != null && form.getY1() != null
				&& form.getX2() != null && form.getY2() != null) {
			if (form.getY1() > form.getY2()) {
				criteria.add(Restrictions.and(
						Restrictions.between("itemLat", form.getX2(),
								form.getX1()),
						Restrictions.between("itemLng", form.getY2(),
								form.getY1())));
			} else {
				criteria.add(Restrictions.or(Restrictions.and(
						Restrictions.between("itemLat", form.getX2(),
								form.getX1()),
						Restrictions.between("itemLng", form.getY2(), 180d)),
						Restrictions.and(Restrictions.between("itemLat",
								form.getX2(), form.getX1()), Restrictions
								.between("itemLng", -180d, form.getY1()))));
			}
		}

		// By created Date
		if (form.getDateFrom() != null && form.getDateTo() != null) {
			Date dateFrom;
			Date dateTo;
			if (form.getDateFrom().before(form.getDateTo())) {
				dateFrom = form.getDateFrom();
				dateTo = form.getDateTo();
			} else {
				dateFrom = form.getDateTo();
				dateTo = form.getDateFrom();
			}
			criteria.add(Restrictions.between("createTime", dateFrom, dateTo));
		} else if (form.getDateFrom() != null) {
			criteria.add(Restrictions.ge("createTime", form.getDateFrom()));
		} else if (form.getDateTo() != null) {
			criteria.add(Restrictions.le("createTime", form.getDateTo()));
		}

		// By update date
		if (form.getUpdateDate() != null) {
			criteria.add(Restrictions.ge("updateTime", form.getUpdateDate()));
		}

		// By tag
		if (!StringUtils.isBlank(form.getTag())) {
			criteria.createAlias("itemTags", "tag").add(
					Restrictions.eq("tag.tag", form.getTag()));
		}

		// By answerUser or answerUserId
		if (!StringUtils.isBlank(form.getAnsweruserId())) {
			Users answerAuthor = this.userService.getById(form
					.getAnsweruserId());
			if (answerAuthor == null) {
				return null;
			}
			criteria.createAlias("question", "question")
					.createAlias("question.answerSet", "answer")
					.add(Restrictions.eq("answer.author", answerAuthor));
		} else if (!StringUtils.isBlank(form.getAnsweruser())) {
			Users answerAuthor = this.userService.findUniqueBy("nickname",
					form.getAnsweruser());
			if (answerAuthor == null) {
				return null;
			}
			criteria.createAlias("question", "question")
					.createAlias("question.answerSet", "answer")
					.add(Restrictions.eq("answer.author", answerAuthor));
		}

		// By question status
		if (!StringUtils.isBlank(form.getQuestionStatus())) {
			if ("inquestion".equals(form.getQuestionStatus())) {
				criteria.add(Restrictions.and(Restrictions
						.isNotNull("question"), Restrictions.or(
						Restrictions.isNull("questionResolved"),
						Restrictions.eq("questionResolved", Boolean.FALSE))));
			} else if ("resolved".equals(form.getQuestionStatus())) {
				criteria.add(Restrictions.or(
						Restrictions.eq("questionResolved", Boolean.TRUE),
						Restrictions.isNull("question")));
			}
		}

		// By teacher confirm status
		if (!StringUtils.isBlank(form.getTeacherConfirm())) {
			if ("confirmed".equals(form.getTeacherConfirm())) {
				criteria.add(Restrictions.eq("teacherConfirm", Boolean.TRUE));
			} else if ("needfixing".equals(form.getTeacherConfirm())) {
				criteria.add(Restrictions.eq("teacherConfirm", Boolean.FALSE));
			}
		}

		// By to answer quesLangs or quesLangsCode
		if (form.getToAnswerQuesLangs() != null
				&& form.getToAnswerQuesLangs().size() > 0) {
			criteria.createAlias("question", "question").add(
					Restrictions.and(
							Restrictions.isEmpty("question.answerSet"),
							Restrictions.in("question.language",
									form.getToAnswerQuesLangs())));
		} else if (form.getToAnswerQuesLangsCode() != null
				&& form.getToAnswerQuesLangsCode().size() > 0) {
			Set<Language> langSet = new HashSet<Language>();
			for (String code : form.getToAnswerQuesLangsCode()) {
				Language lang = languageService.findUniqueLangByCode(code);
				if (lang != null)
					langSet.add(lang);
			}
			if (langSet.size() > 0) {
				criteria.createAlias("question", "question").add(
						Restrictions.and(
								Restrictions.isEmpty("question.answerSet"),
								Restrictions.in("question.language", langSet)));
			}
		}

		// By to study quesLangs or quesLangsCode
		if (form.getToStudyQuesLangs() != null
				&& form.getToStudyQuesLangs().size() > 0) {
			criteria.createAlias("question", "question").add(
					Restrictions.and(
							Restrictions.isNotEmpty("question.answerSet"),
							Restrictions.in("question.language",
									form.getToStudyQuesLangs())));
		} else if (form.getToStudyQuesLangsCode() != null
				&& form.getToStudyQuesLangsCode().size() > 0) {
			Set<Language> langSet = new HashSet<Language>();
			for (String code : form.getToStudyQuesLangsCode()) {
				Language lang = languageService.findUniqueLangByCode(code);
				if (lang != null)
					langSet.add(lang);
			}
			if (langSet.size() > 0) {
				criteria.createAlias("question", "question").add(
						Restrictions.and(
								Restrictions.isNotEmpty("question.answerSet"),
								Restrictions.in("question.language", langSet)));
			}
		}

		if (form.getCategorySet() != null && form.getCategorySet().size() > 0) {
			criteria.add(Restrictions.in("category", form.getCategorySet()));
		}
		if (form.getItemIds() != null && form.getItemIds().size() > 0) {
			criteria.add(Restrictions.in("id", form.getItemIds()));
		}

		// has answers
		if (form.getHasAnswers() != null && form.getHasAnswers()) {
			criteria.createAlias("question", "question").add(
					Restrictions.isNotEmpty("question.answerSet"));
		}

		if (!form.isIncludeRelog()) {
			criteria.add(Restrictions.isNull("relogItem"));
		}

		if (!StringUtils.isBlank(form.getQrcode())) {
			criteria.add(Restrictions.eq("qrcode", form.getQrcode()));
		}
		if (!StringUtils.isBlank(form.getPlace())) {
			
			criteria.createAlias("placename", "placename").add(
					Restrictions.like("placename.place", form.getPlace(),
							MatchMode.ANYWHERE));
//			criteria.add(Restrictions.eq("place",form.getPlace()));
//			criteria.createAlias("placename", "place").add(
//					Restrictions.eq("place.placename", form.getPlace()));
		}

		return criteria;
	}

	private DetachedCriteria buildCriteriaByForm2(ItemSearchCondForm form,
			List<Groupmember> groupname) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Item.class);
		criteria.add(Restrictions.ne("disabled", new Integer(1)));
		// criteria.createAlias("author", "author").add(
		// Restrictions.eq("author.id","16bb94322ec4005b012edca218230033"));
//		Groupsetting(criteria, groupname);
//		String [] names={"16bb94322ec4005b012edca218230033","ff8081813d441a99013d81a6cf241ecc"};
		String[] g = new String[groupname.size()];
		for(int i=0;i<groupname.size();i++){
			g[i]=groupname.get(i).getOther();
		}
		 criteria.createAlias("author", "author").add(
		 Restrictions.in("author.id",g));

		// criteria.createAlias("author", "author").add(
		// Restrictions.eq("author.id",groupname.get(i).getOther()));
		// System.out.println(groupname.get(i).getOther());

		// criteria.add(Restrictions.eq("item_lng",
		// "ff8081813d441a99013d81a6cf241ecc"));
		// By Title
		if (!StringUtils.isBlank(form.getTitle())) {
			criteria.createAlias("titles", "title").add(
					Restrictions.like("title.content", form.getTitle(),
							MatchMode.ANYWHERE));
			// if (StringUtils.isBlank(form.getLang())) {
			// criteria.add(Restrictions.or(Restrictions.or(Restrictions.like(
			// "jpTitle", form.getTitle(), MatchMode.ANYWHERE),
			// Restrictions.like("enTitle", form.getTitle(),
			// MatchMode.ANYWHERE)), Restrictions.like(
			// "zhTitle", form.getTitle(), MatchMode.ANYWHERE)));
			// } else if ("en".equals(form.getLang())) {
			// criteria.add(Restrictions.like("enTitle", form.getTitle(),
			// MatchMode.ANYWHERE));
			// } else if ("jp".equals(form.getLang())) {
			// criteria.add(Restrictions.like("jpTitle", form.getTitle(),
			// MatchMode.ANYWHERE));
			// } else if ("zh".equals(form.getLang())) {
			// criteria.add(Restrictions.like("zhTitle", form.getTitle(),
			// MatchMode.ANYWHERE));
			// }
		}

		// By author or authorId
		if (!StringUtils.isBlank(form.getUserId())) {
			criteria.createAlias("author", "author").add(
					Restrictions.eq("author.id", form.getUserId()));
			// criteria.createAlias("author", "author").add(
			// Restrictions.eq("author.id",
			// "ff8081813d441a99013d81a6cf241ecc"));

		} else if (!StringUtils.isBlank(form.getNickname())) {
			criteria.createAlias("author", "author").add(
					Restrictions.like("author.nickname", form.getNickname(),
							MatchMode.ANYWHERE));
		} else if (!StringUtils.isBlank(form.getUsername())) {
			criteria.createAlias("author", "author").add(
					Restrictions.eq("author.nickname", form.getUsername()));
		} else if (!StringUtils.isBlank(form.getNotuserId())) {
			criteria.createAlias("author", "author").add(
					Restrictions.ne("author.id", form.getUserId()));
		}

		// By Map range
		if (form.isMapenabled() && form.getX1() != null && form.getY1() != null
				&& form.getX2() != null && form.getY2() != null) {
			if (form.getY1() > form.getY2()) {
				criteria.add(Restrictions.and(
						Restrictions.between("itemLat", form.getX2(),
								form.getX1()),
						Restrictions.between("itemLng", form.getY2(),
								form.getY1())));
			} else {
				criteria.add(Restrictions.or(Restrictions.and(
						Restrictions.between("itemLat", form.getX2(),
								form.getX1()),
						Restrictions.between("itemLng", form.getY2(), 180d)),
						Restrictions.and(Restrictions.between("itemLat",
								form.getX2(), form.getX1()), Restrictions
								.between("itemLng", -180d, form.getY1()))));
			}
		}

		// By created Date
		if (form.getDateFrom() != null && form.getDateTo() != null) {
			Date dateFrom;
			Date dateTo;
			if (form.getDateFrom().before(form.getDateTo())) {
				dateFrom = form.getDateFrom();
				dateTo = form.getDateTo();
			} else {
				dateFrom = form.getDateTo();
				dateTo = form.getDateFrom();
			}
			criteria.add(Restrictions.between("createTime", dateFrom, dateTo));
		} else if (form.getDateFrom() != null) {
			criteria.add(Restrictions.ge("createTime", form.getDateFrom()));
		} else if (form.getDateTo() != null) {
			criteria.add(Restrictions.le("createTime", form.getDateTo()));
		}

		// By update date
		if (form.getUpdateDate() != null) {
			criteria.add(Restrictions.ge("updateTime", form.getUpdateDate()));
		}

		// By tag
		if (!StringUtils.isBlank(form.getTag())) {
			criteria.createAlias("itemTags", "tag").add(
					Restrictions.eq("tag.tag", form.getTag()));
		}

		// By answerUser or answerUserId
		if (!StringUtils.isBlank(form.getAnsweruserId())) {
			Users answerAuthor = this.userService.getById(form
					.getAnsweruserId());
			if (answerAuthor == null) {
				return null;
			}
			criteria.createAlias("question", "question")
					.createAlias("question.answerSet", "answer")
					.add(Restrictions.eq("answer.author", answerAuthor));
		} else if (!StringUtils.isBlank(form.getAnsweruser())) {
			Users answerAuthor = this.userService.findUniqueBy("nickname",
					form.getAnsweruser());
			if (answerAuthor == null) {
				return null;
			}
			criteria.createAlias("question", "question")
					.createAlias("question.answerSet", "answer")
					.add(Restrictions.eq("answer.author", answerAuthor));
		}

		// By question status
		if (!StringUtils.isBlank(form.getQuestionStatus())) {
			if ("inquestion".equals(form.getQuestionStatus())) {
				criteria.add(Restrictions.and(Restrictions
						.isNotNull("question"), Restrictions.or(
						Restrictions.isNull("questionResolved"),
						Restrictions.eq("questionResolved", Boolean.FALSE))));
			} else if ("resolved".equals(form.getQuestionStatus())) {
				criteria.add(Restrictions.or(
						Restrictions.eq("questionResolved", Boolean.TRUE),
						Restrictions.isNull("question")));
			}
		}

		// By teacher confirm status
		if (!StringUtils.isBlank(form.getTeacherConfirm())) {
			if ("confirmed".equals(form.getTeacherConfirm())) {
				criteria.add(Restrictions.eq("teacherConfirm", Boolean.TRUE));
			} else if ("needfixing".equals(form.getTeacherConfirm())) {
				criteria.add(Restrictions.eq("teacherConfirm", Boolean.FALSE));
			}
		}

		// By to answer quesLangs or quesLangsCode
		if (form.getToAnswerQuesLangs() != null
				&& form.getToAnswerQuesLangs().size() > 0) {
			criteria.createAlias("question", "question").add(
					Restrictions.and(
							Restrictions.isEmpty("question.answerSet"),
							Restrictions.in("question.language",
									form.getToAnswerQuesLangs())));
		} else if (form.getToAnswerQuesLangsCode() != null
				&& form.getToAnswerQuesLangsCode().size() > 0) {
			Set<Language> langSet = new HashSet<Language>();
			for (String code : form.getToAnswerQuesLangsCode()) {
				Language lang = languageService.findUniqueLangByCode(code);
				if (lang != null)
					langSet.add(lang);
			}
			if (langSet.size() > 0) {
				criteria.createAlias("question", "question").add(
						Restrictions.and(
								Restrictions.isEmpty("question.answerSet"),
								Restrictions.in("question.language", langSet)));
			}
		}

		// By to study quesLangs or quesLangsCode
		if (form.getToStudyQuesLangs() != null
				&& form.getToStudyQuesLangs().size() > 0) {
			criteria.createAlias("question", "question").add(
					Restrictions.and(
							Restrictions.isNotEmpty("question.answerSet"),
							Restrictions.in("question.language",
									form.getToStudyQuesLangs())));
		} else if (form.getToStudyQuesLangsCode() != null
				&& form.getToStudyQuesLangsCode().size() > 0) {
			Set<Language> langSet = new HashSet<Language>();
			for (String code : form.getToStudyQuesLangsCode()) {
				Language lang = languageService.findUniqueLangByCode(code);
				if (lang != null)
					langSet.add(lang);
			}
			if (langSet.size() > 0) {
				criteria.createAlias("question", "question").add(
						Restrictions.and(
								Restrictions.isNotEmpty("question.answerSet"),
								Restrictions.in("question.language", langSet)));
			}
		}

		if (form.getCategorySet() != null && form.getCategorySet().size() > 0) {
			criteria.add(Restrictions.in("category", form.getCategorySet()));
		}

		// has answers
		if (form.getHasAnswers() != null && form.getHasAnswers()) {
			criteria.createAlias("question", "question").add(
					Restrictions.isNotEmpty("question.answerSet"));
		}

		if (!form.isIncludeRelog()) {
			criteria.add(Restrictions.isNull("relogItem"));
		}

		if (!StringUtils.isBlank(form.getQrcode())) {
			criteria.add(Restrictions.eq("qrcode", form.getQrcode()));
		}

		return criteria;
	}

	private void Groupsetting(DetachedCriteria criteria, List<Groupmember> groupname) {
		// TODO Auto-generated method stub
//		int count=groupname.size();
//		
//		if(count==1){
//			criteria.createAlias("author", "author").add(Restrictions.eq("author.id",
//							groupname.get(0).getOther()));
//		}
//		else if(count==2){
//			criteria.createAlias("author", "author").add(
//					Restrictions.or(Restrictions.eq("author.id",
//							groupname.get(0).getOther()), Restrictions.eq(
//							"author.id", groupname.get(1).getOther())));
//		}

		
	}

	public List<Object[]> uploadRanking() {
		String hql = "select user, count(user) from Item i join i.author user where i.disabled!=1 and i.relogItem is null group by user order by count(user) desc";
		List<Object[]> result = this.itemDao.find(hql);
		return result;
	}
	public List<Object[]> uploadRanking(String userId) {
		String hql = "select user, count(user) from Item i join i.author user where user.id = ? and i.disabled!=1 and i.relogItem is null group by user order by count(user) desc";
		List<Object[]> result = this.itemDao.find(hql,userId);
		return result;
	}

	public List<Object[]> answerRanking() {
		String hql = "select user, count(user) from Answer answer join answer.author user group by user order by count(user) desc";
		List<Object[]> result = this.answerDao.find(hql);
		return result;
	}

	@Transactional(readOnly = true)
	public Item findById(String id) {
		return itemDao.get(id);
	}

	/*
	 * public void delete(Item item) { Item i = itemDao.get(item.getId()); if
	 * (i.getRelogItem() != null) { i.setImage(null); i.setRelogItem(null); }
	 * itemDao.delete(i); }
	 */

	@Transactional
	public void delete(Item i) {
		Item item = this.itemDao.findUniqueBy("id", i.getId());
		item.setDisabled(1);
		this.itemDao.save(item);

		List<ItemQuestionType> types = this.itemQuestionTypeDao.findBy("item",
				item);
		for (ItemQuestionType type : types) {
			this.itemQuestionTypeDao.delete(type);
		}

		// ItemQueue update
		List<ItemQueue> iqs = this.itemQueueDao.findBy("item", item);
		for (ItemQueue iq : iqs) {
			iq.setDisabled(1);
			this.itemQueueDao.save(iq);
		}
	}

	@Transactional(readOnly = true)
	public boolean isRelogable(Item item, Users user) {
		if (user.getId().equals(item.getAuthor().getId())) {
			return false;
		}
		String hql = "from Item item where item.author=? and item.relogItem = ?";
		List<Item> itemList = itemDao.find(hql, user, item);
		if (itemList != null && itemList.size() > 0) {
			return false;
		}
		return true;
	}

	public Item relog(Item it, Users u) {
		Item item = itemDao.get(it.getId());
		Users user = usersDao.get(u.getId());
		if (!this.isRelogable(item, user)) {
			return null;
		}
		Date current = new Date(System.currentTimeMillis());
		Item reItem = new Item();
		reItem.setAuthor(user);
		reItem.setBarcode(item.getBarcode());
		reItem.setCreateTime(current);
		List<ItemTitle> titles = new ArrayList<ItemTitle>();
		for (ItemTitle t : item.getTitles()) {
			ItemTitle covert_it = new ItemTitle();
			covert_it.setContent(t.getContent());
			covert_it.setItem(reItem);
			covert_it.setLanguage(t.getLanguage());
			titles.add(covert_it);
		}
		reItem.setTitles(titles);
		reItem.setDisabled(it.getDisabled());
		reItem.setImage(item.getImage());
		reItem.setItemLat(item.getItemLat());
		reItem.setItemLng(item.getItemLng());
		reItem.setItemZoom(item.getItemZoom());
		reItem.setNote(item.getNote());
		reItem.setPlace(item.getPlace());
		reItem.setQrcode(item.getQrcode());
		reItem.setRfid(item.getRfid());
		reItem.setShareLevel(Item.ShareLevel.PUBLIC);
		reItem.setUpdateTime(current);
		reItem.setRelogItem(item);
		itemDao.save(reItem);
		itemQueueService.updateItemQueue(item, user,
				QuizConstants.QueueTypeNewObject);
		return reItem;
	}

	@Transactional(readOnly = true)
	public Page<Item> searchRelatedItemList(String itemId) {
		// TODO
		Item item = this.itemDao.get(itemId);
		if (item == null) {
			return new Page<Item>();
		}

		List<ItemTitle> titles = item.getTitles();
		List<String> titleListDirty = new ArrayList<String>();
		for (ItemTitle title : titles) {
			String t = title.getContent();
			titleListDirty.addAll(Arrays.asList(t.split(" ")));
		}

		Set<String> titlesCond = new HashSet<String>();
		for (String t : titleListDirty) {
			if (!StringUtils.isBlank(t)) {
				titlesCond.add(t.trim());
			}
		}

		Set<Item> itemResult = new HashSet<Item>();
		for (String t : titlesCond) {
			DetachedCriteria criteria = DetachedCriteria.forClass(Item.class);
			criteria.add(Restrictions.ne("disabled", new Integer(1)));
			criteria.add(Restrictions.ne("id", item.getId()));
			criteria.add(Restrictions.isNull("relogItem"));
			criteria.createAlias("titles", "title");
			criteria.add(Restrictions.like("title.content", t,
					MatchMode.ANYWHERE));
			Page<Item> itemPage = new Page<Item>(5);
			itemPage.setPageNo(1);
			itemPage.setOrder(Page.DESC);
			itemPage.setOrderBy("updateTime");
			itemDao.findPage(itemPage, criteria);
			itemResult.addAll(itemPage.getResult());
		}
		Page<Item> result = new Page<Item>(itemResult.size());
		result.setResult(new ArrayList<Item>(itemResult));
		result.setTotalCount(itemResult.size());
		return result;
	}

	public void questionConfirm(Item it, Users u) {
		Item item = itemDao.get(it.getId());
		item.setQuestionResolved(true);
		item.setUpdateTime(new Date(System.currentTimeMillis()));
		itemDao.save(item);
	}

	public void teacherConfirm(Item it, Users u) {
		Item item = itemDao.get(it.getId());
		item.setTeacherConfirm(Boolean.TRUE);
		itemDao.save(item);
	}

	public void teacherReject(Item it, Users u) {
		Item item = itemDao.get(it.getId());
		item.setTeacherConfirm(Boolean.FALSE);
		itemDao.save(item);
	}

	public void teacherDeleteStatus(Item it, Users u) {
		Item item = itemDao.get(it.getId());
		item.setTeacherConfirm(null);
		itemDao.save(item);
	}

	@Transactional(readOnly = true)
	public List<Item> findByCategory(Category category) {
		return itemDao.findBy("category", category);
	}

	@Transactional(readOnly = true)
	public Map<String, Long> findTagCloud() {
		String hql = "select tag.tag,count(tag.tag) from ItemTag tag inner join tag.items as item group by tag.tag";
		List<Object[]> result = this.itemTagDao.find(hql);
		Map<String, Long> tagcloud = new HashMap<String, Long>();
		for (Object[] m : result) {
			tagcloud.put(String.valueOf(m[0]), Long.valueOf(m[1].toString()));
		}
		return tagcloud;
	}

	@Transactional(readOnly = true)
	public Long findReadCount(String id) {
		String hql = "select count(*) from LogUserReadItem log where log.item.id=?";
		Long count = logUserReadItemDao.findUnique(hql, id);
		return count;
	}

	@Transactional(readOnly = true)
	public Long findRelogCount(String id) {
		String hql = "select count(*) from Item item where item.relogItem.id=? and item.disabled!=1";
		Long count = itemDao.findUnique(hql, id);
		return count;
	}

	public Long findAllItemCount() {
		String hql = "select count(*) from Item item where item.disabled!=1";
		return itemDao.findUnique(hql);
	}

	public Long findAllReadCount() {
		String hql = "select count(*) from LogUserReadItem log";
		return logUserReadItemDao.findUnique(hql);
	}
	@Transactional(readOnly = true)
	public Long findAllReadCount(String user_id) {
		String hql = "select count(*) from LogUserReadItem log where log.user.id=?";
		return logUserReadItemDao.findUnique(hql,user_id);
	}

	public Long findAllTagCount() {
		String hql = "select count(distinct tag.tag) from ItemTag tag inner join tag.items as item";
		return logUserReadItemDao.findUnique(hql);
	}

	/**
	 * monthsヶ月前からinWeeks週間に登録されたLearning Logを検索
	 * 
	 * @param months
	 * @param inWeeks
	 *            Nullable
	 * @param userId
	 *            Nullable
	 * @param size
	 *            Default 20
	 * @return
	 */
	public List<Item> findAllItemsBeforeMonths(int months, Integer inWeeks,
			String userId, Integer size) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Item.class);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
		Date end = cal.getTime();
		if (inWeeks != null) {
			cal.set(Calendar.WEEK_OF_YEAR, cal.get(Calendar.WEEK_OF_YEAR)
					- inWeeks);
			Date start = cal.getTime();
			criteria.add(Restrictions.and(Restrictions.ge("createTime", end),
					Restrictions.le("createTime", start)));
		} else {
			criteria.add(Restrictions.le("createTime", end));
		}
		if (userId != null) {
			criteria.createAlias("author", "author").add(
					Restrictions.eq("author.id", userId));
		}

		criteria.add(Restrictions.ne("disabled", new Integer(1)));

		if (size == null || size == 0)
			size = 20;
		Page<Item> itemPage = new Page<Item>(size);
		itemPage.setOrder(Page.DESC);
		itemPage.setOrderBy("createTime");
		Page<Item> result = itemDao.findPage(itemPage, criteria);
		return result.getResult();
	}

	@SuppressWarnings("unchecked")
	public List<Item> findItemByLocation(Double lat, Double lng,
			Boolean isMyItem) throws Exception {
		if (lat == null || lng == null) {
			return new ArrayList<Item>();
		}

		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());

		Double x1, y1, x2, y2, xt, yt;
		Double m = 360 / 39940.638;
		x1 = lat + m;
		x2 = lat - m;
		if (x1 < -90 || x1 > 90) {
			x1 = 180 * x1 / Math.abs(x1) - x1;
		}
		if (x2 < -90 || x2 > 90) {
			x2 *= 180 * x2 / Math.abs(x2) - x2;
		}
		if (x1 < x2) {
			xt = x1;
			x1 = x2;
			x2 = xt;
		}
		Double clat = 360 / (2 * Math.PI * Math.cos(lat) * 40075.004);
		if (clat < 0) {
			clat *= -1;
		}

		y1 = lng - clat;
		y2 = lng + clat;
		if (y1 < -180 || y1 > 180) {
			y1 = (360 * (y1 / Math.abs(y1)) - y1) * (-1);
		}
		if (y2 < -180 || y2 > 180) {
			y2 = (360 * y2 / Math.abs(y2) - y2) * (-1);
		}
		if (y1 < y2) {
			yt = y1;
			y1 = y2;
			y2 = yt;
		}

		String sql = "";

		if (Boolean.TRUE.equals(isMyItem)) {
			sql = "select i.* from t_llquiz lq, t_myquiz mq, t_item i, "
					+ "( select l.item_id, max(m.update_date) as update_date "
					+ "from t_myquiz m, t_llquiz l " + "where m.author_id ='"
					+ user.getId()
					+ "' "
					+ "and m.llquiz_id = l.id group by l.item_id ) y "
					+ "where mq.update_date = y.update_date and mq.author_id ='"
					+ user.getId()
					+ "' "
					+ "and mq.llquiz_id = lq.id and lq.item_id = y.item_id "
					+ "and mq.answerstate !=1 and mq.answerstate !=2 and i.id = lq.item_id "
					+ "and i.item_lat<='"
					+ x1
					+ "' and i.item_lat>='"
					+ x2
					+ "' "
					+ "and i.item_lng<='"
					+ y1
					+ "' and i.item_lng>='"
					+ y2
					+ "' and i.author_id = '"
					+ user.getId()
					+ "' "
					+ "and i.id not in ( "
					+ "select inm.items "
					+ "from t_itemnotify itn, t_itemnotify_items inm "
					+ "where itn.author_id = '"
					+ user.getId()
					+ "' "
					+ "and itn.id = inm.t_itemnotify  "
					+ "and itn.feedback = 1 )";
		} else if (Boolean.FALSE.equals(isMyItem)) {
			sql = "select i.* from t_llquiz lq, t_myquiz mq, t_item i, "
					+ "( select l.item_id, max(m.update_date) as update_date "
					+ "from t_myquiz m, t_llquiz l " + "where m.author_id ='"
					+ user.getId()
					+ "' "
					+ "and m.llquiz_id = l.id group by l.item_id )y "
					+ "where mq.update_date = y.update_date and mq.author_id ='"
					+ user.getId()
					+ "' "
					+ "and mq.llquiz_id = lq.id and lq.item_id = y.item_id "
					+ "and mq.answerstate !=1 and mq.answerstate !=2 and i.id = lq.item_id "
					+ "and i.item_lat<='"
					+ x1
					+ "' and i.item_lat>='"
					+ x2
					+ "' "
					+ "and i.item_lng<='"
					+ y1
					+ "' and i.item_lng>='"
					+ y2
					+ "' and i.author_id != '"
					+ user.getId()
					+ "' "
					+ "and i.id not in ( "
					+ "select inm.items "
					+ "from t_itemnotify itn, t_itemnotify_items inm "
					+ "where itn.author_id = '"
					+ user.getId()
					+ "' "
					+ "and itn.id = inm.t_itemnotify  "
					+ "and itn.feedback = 1 )";
		}

		if (Constants.usingCategory && user.getMyCategoryList() != null
				&& user.getMyCategoryList().size() > 0) {
			String categoryids = "";
			for (Category c : user.getMyCategoryList()) {
				categoryids += "'" + c.getId() + "',";
			}
			if (categoryids.length() > 0)
				categoryids = categoryids
						.substring(0, categoryids.length() - 1);
			sql = sql + " and i.category in (" + categoryids + ") ";
		}

		Session session = this.sessionFactory.getCurrentSession();

		return session.createSQLQuery(sql).addEntity(Item.class).list();
	}

	@Transactional(readOnly = true)
	public List<Item> searchMyitemsForTimemap(String id) {
		String hql = "from Item item where item.author.id = ? and item.disabled!=1 and item.itemLat is not null and item.itemLng is not null";
		List<Item> itemList = itemDao.find(hql, id);
		return itemList;
	}

	@Transactional(readOnly = true)
	public List<Item> searchItemsByMD5(String md5) {
		String hql = "from Item item where item.image.md5=? and item.disabled!=1";
		return itemDao.find(hql, md5);
	}

	public List<Item> findItemListByFiledataId(String filedataId) {
		String hql = "from Item item where item.image.id=?";
		return itemDao.find(hql, filedataId);
	}

	// ■wakebe 自分の回答数取得
	public Integer getAnswerCount(String id) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			String sql = "select count(author_id) as cnt from t_answer where author_id = :id group by author_id";
			Integer result = ((BigInteger) session.createSQLQuery(sql)
					.setParameter("id", id).uniqueResult()).intValue();

			// 経験値 = 質問の回答数 * 20
			return result * 20;
		} catch (Exception e) {
			logger.debug("Error in GetAnswerCount", e);
			return 0;
		}
	}

	// ■wakebe 自分の全アイテムのリード数取得
	@Transactional(readOnly = true)
	public Long findReadAllCount(String id) {
		String hql = "select count(*) from LogUserReadItem log where log.user.id=?";
		Long count = itemDao.findUnique(hql, id);

		return count;
	}

	@Transactional(readOnly = true)
	public Long findRelogAllCount(String id) {
		String hql = "select count(*) from Item item where item.relogItem.id=? and item.disabled!=1";
		Long count = itemDao.findUnique(hql, id);
		return count;
	}

	public String getItemNote(Item item) {
		String note = "";
		List<ItemTitle> list = itemTitleDao2.findListByItem(item.getId());
		for (ItemTitle t : list) {
			Language lang = languageDao2.findById(t.getLanguage());
			note += lang.getName() + ":" + t.getContent() + "  ";
		}
		return note;
	}

	public List<ItemTitleBatis> findsecondlayer() {
		// TODO Auto-generated method stub
		return this.itemTitleDao2.findsecondlayer();
	}

	public List<NetworkAnalysis> findfirstandsecondedge() {
		// TODO Auto-generated method stub
		return this.itemTitleDao2.findedge();
	}

	public List<ItemTags> finditemtags(String itemid) {
		// TODO Auto-generated method stub
		return this.itemtags2.findnetworktag(itemid);
	}

	// public List<String> findcount(String string, String string2, String
	// string3) {
	// // TODO Auto-generated method stub
	//
	// return this.itemDao2.findcount(string,string2,string3);
	// }
	//
	// public List<String> findcount() {
	// // TODO Auto-generated method stub
	// return this.itemDao2.findcount();
	// }

	public String findcount(Date dstart, String id, Date dend) {
		// TODO Auto-generated method stub
		return this.itemDao2.findcount(dstart, id, dend);
	}

	public List<Cooccurrence> cooccurrence(String string) {
		// TODO Auto-generated method stub
		return this.itemDao2.cooccurrence(string);
	}

	public Page<Item> searchItemPageByCond2(ItemSearchCondForm form,
			List<Groupmember> groupname) {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = buildCriteriaByForm2(form, groupname);
		Page<Item> itemPage = new Page<Item>(10);
		if (criteria == null)
			return itemPage;
		if (form.getPage() == null || form.getPage() < 1) {
			form.setPage(1);
		}
		itemPage.setPageNo(form.getPage());
		itemPage.setOrder(Page.DESC);
		itemPage.setOrderBy("updateTime");
		return this.itemDao.findPage(itemPage, criteria);
	}

	public List<Cooccurrence> getAllTimedata() {
		// TODO Auto-generated method stub
		return this.itemDao2.getAllTimedata();
	}

	public List<Itemlatlng> findlatlng() {
		// TODO Auto-generated method stub
		return this.itemDao2.findlatlng();
	}

//	public void insertplace(String place, List<String> list) {
//		// TODO Auto-generated method stubUUID
//		
//		
//	}

	public void insertplace(String id, String place, List<String> list) {
		// TODO Auto-generated method stub
		String placeid=KeyGenerateUtil.generateIdUUID();
		this.itemDao2.insertplace(placeid,id,place);
		
		
		for(int i=0;i<list.size();i++){
		String placeattributeid=KeyGenerateUtil.generateIdUUID();
		this.itemDao2.insertplaceattribute(placeattributeid,placeid,list.get(i));
		}
	}

	public String findcount2(Date dstart, Date dend) {
		// TODO Auto-generated method stub
		return this.itemDao2.findcount2(dstart,dend);
	}

	public List<Itemlatlng> getPersonalOneday(String string) {
		// TODO Auto-generated method stub
		return this.itemDao2.getPersonalOneday(string);
	}

	public List<Itemlatlng> getAllOneday() {
		// TODO Auto-generated method stub
		return this.itemDao2.getAllOneday();
	}

	public List<Itemlatlng> getAllplace() {
		// TODO Auto-generated method stub
		return this.itemDao2.getAllplace();
	}

	public List<Itemlatlng> getCategoryplace() {
		// TODO Auto-generated method stub
		return this.itemDao2.getCategoryplace();
	}

	public List<Itemlatlng> getRecommendTime() {
		// TODO Auto-generated method stub
		return this.itemDao2.getRecommednTime();
	}

	public List<Cooccurrence> placecooccurrence(String id) {
		// TODO Auto-generated method stub
		return this.itemDao2.placecooccurrence(id);
	}

	public List<PlaceAnalysis> getPlace_attribute(String target_place_id) {
		// TODO Auto-generated method stub
		return this.itemDao2.getPlace_attribute(target_place_id);
	}

	public List<Cooccurrence> place_distinct_cooccurrence(String id) {
		// TODO Auto-generated method stub
		return this.itemDao2.place_distinct_cooccurrence(id);
	}

	public List<PlaceCollocation> p_collocation(String label) {
		// TODO Auto-generated method stub
		return this.itemDao2.p_collocation(label);
	}

	public PlaceCollocation place_content(String id) {
		// TODO Auto-generated method stub
		return this.itemDao2.p_content(id);
	}

	public List<yesquizitemget> getyesquizitems(String itemid) {
		// TODO Auto-generated method stub
		return this.itemDao2.getyesquizitems(itemid);
	}

	public List<TDAsecondlayer> findtdasecondlayer() {
		// TODO Auto-generated method stub
		return this.itemDao2.findtdasecondlayer();
	}

	public List<MqQuiz> getquizdata(String id) {
		// TODO Auto-generated method stub
		return this.itemDao2.getquizdata(id);
	}

	public void insertmqquiz(MqQuiz mqQuiz) {
		// TODO Auto-generated method stub
		this.itemDao2.insertmqquiz(mqQuiz);
	}

	public List<MqQuiz> getgquiz(String string) {
		// TODO Auto-generated method stub
		return this.itemDao2.getgquiz(string);
	}

	public void insertmqc(Mychoice mc) {
		// TODO Auto-generated method stub
		this.itemDao2.insertmqc(mc);
	}



	public void updatemqquiz(Quizstore quizstore) {
		// TODO Auto-generated method stub
		this.itemDao2.updatemqquiz(quizstore);
	}

	public void updatequizdate(Date cdate) {
		// TODO Auto-generated method stub
		this.itemDao2.updatequizdate(cdate);
	}

	public void updateCommonid(String id, String commonid) {
		// TODO Auto-generated method stub
		this.itemDao2.updatecommonid(id,commonid);
	}


	public List<Results> question(String id, String itemid) {
		// TODO Auto-generated method stub
		return this.itemDao2.questions(id,itemid);
	}

	public List<MqQuiz> getimagegquiz(String id) {
		// TODO Auto-generated method stub
		return this.itemDao2.getimagequiz(id);
	}

	public List<TDAthirdlayer> findtdathirdlayer() {
		// TODO Auto-generated method stub
		return this.itemDao2.findthirdlayer();
	}

	public List<TDAsecondlayer> findernationalitycount() {
		// TODO Auto-generated method stub
		return this.itemDao2.findernationalitycount();
	}

	public List<TDAsecondlayer> finderposcount() {
		// TODO Auto-generated method stub
		return this.itemDao2.finderposcount();
	}

	public List<WordNet> getwordnet(String content) {
		// TODO Auto-generated method stub
		return this.itemDao2.getwordnet(content);
	}

	public List<Cooccurrence> acorrence(String id) {
		// TODO Auto-generated method stub
		return this.itemDao2.getitemcorrence(id);
	}










}
