/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.ac.tokushima_u.is.ll.ws.service;


/**
 * 
 * @author lemonrain
 */
//@Service("llquizRemoteService")
//@Transactional
public class LLQuizRemoteService {
//	private static final Logger logger = LoggerFactory
//			.getLogger(LLQuizRemoteService.class);
//	@Autowired
//	private UserService userService;
//	@Autowired
//	private LLQuizService quizService;
//
//	@Autowired
//	private ItemWebService itemRemoteService;
//
//	@Autowired
//	private StaticServerService staticServerService;
//	@Autowired
//	private AutoQuizSendService autoQuizSendService;
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//
//	
//	public QuizForm composeQuizByUsername(String username, String password,
//			QuizCondition quizCon) {
//		Users user = null;
//		QuizForm quizform = null;
//
//		quizform = new QuizForm();
//		quizform.setErrorCode(Constants.ErrorCode_No_Quiz);
//		return quizform;
//		
//		//TODO
//		
////		try {
////			user = this.validateAuth(username, password);
////			quizCon.setUser(user);
////		} catch (Exception e) {
////			quizform = new QuizForm();
////			quizform.setErrorCode(Constants.ErrorCode_No_User);
////			return quizform;
////		}
////
////		if(quizCon != null && quizCon.getQuizid()!= null && quizCon.getQuizid().length()>0){
////			LLQuizDTO quizdto = this.quizService.findQuizByKey(quizCon.getQuizid());
////			quizform = this.createQuizForm(quizdto, quizCon);
////		}else if (quizCon != null && quizCon.getTimeflg() != null
////				&& Constants.WithTimeFlg.equals(quizCon.getTimeflg())) {
////			List<SendTime> results = this.autoQuizSendService
////					.findUserSendTime(user);
////			if (results != null && results.size() > 0) {
////				quizform = this.createQuizForm(quizCon);
////			}
////		} else {
////			quizform = this.createQuizForm(quizCon);
////		}
////		if (quizCon.getLat() != null && quizCon.getLng() != null) {
////			try {
////				List<ItemModel> itemlist = this.itemRemoteService
////						.findItemByLocationAndStudyLan(quizCon.getLat(),
////								quizCon.getLng(), user.getStudyLangs(),user.getId(),Boolean.FALSE);
////				if (itemlist != null && itemlist.size() > 0) {
////					if (quizform == null){
////						quizform = new QuizForm();
////						quizform.setErrorCode(Constants.ErrorCode_No_Quiz);
////					}
////					quizform.setHasObjectAround(Boolean.TRUE);
////				}
////			} catch (Exception e) {
////
////			}
////		}
////		return quizform;
//	}
//
//	public QuizForm checkQuizAnswerByUsernaeme(String username,
//			String password, QuizCondition quizCon) {
//		Users user = null;
//		QuizForm quizform = new QuizForm();
//		try {
//			user = this.validateAuth(username, password);
//			quizCon.setUser(user);
//			return this.checkQuizAnswer(quizCon);
//		} catch (Exception e) {
//			quizform.setErrorCode(Constants.ErrorCode_No_User);
//		}
//		return quizform;
//	}
//
//	private QuizForm createQuizForm(QuizCondition quizcon) {
//		LLQuizDTO quizdto = null;
//		if (quizcon != null && quizcon.getTimeflg() != null
//				&& Constants.WithTimeFlg.equals(quizcon.getTimeflg())) {
//			quizdto = this.findQuizByTime(quizcon);
//		} else
//			quizdto = this.findQuizByLocation(quizcon);
//
//		return this.createQuizForm(quizdto, quizcon);
//	}
//	
//	
//	private QuizForm createQuizForm(LLQuizDTO quizdto, QuizCondition quizcon) {
//		QuizForm quizform = new QuizForm();
//		if (quizcon.getUser().getMobileEmail() != null
//				&& quizcon.getUser().getMobileEmail().length() > 0)
//			quizform.setMemail(quizcon.getUser().getMobileEmail());
//		else
//			quizform.setMemail(quizcon.getUser().getPcEmail());
//
//		if (quizdto != null&&quizdto.getLlquiz()!=null) {
//			LLQuiz quiz = quizdto.getLlquiz();
//			quizform.setAnswer(Integer.valueOf(quiz.getAnswer()));
//			quizform.setId(quiz.getId());
//			List<LLQuizChoice> choices = quiz.getChoices();
//			if (choices != null && choices.size() >= 4) {
//				if (choices.get(0) != null) {
//					quizform.setChoice1(choices.get(0).getContent());
//					quizform.setNote1(choices.get(0).getNote());
//				}
//				if (choices.get(1) != null) {
//					quizform.setChoice2(choices.get(1).getContent());
//					quizform.setNote2(choices.get(1).getNote());
//				}
//				if (choices.get(2) != null) {
//					quizform.setChoice3(choices.get(2).getContent());
//					quizform.setNote3(choices.get(2).getNote());
//				}
//				if (choices.get(3) != null) {
//					quizform.setChoice4(choices.get(3).getContent());
//					quizform.setNote4(choices.get(3).getNote());
//				}
//
//			}
//			if (quiz.getItem().getImage() != null) {
////				byte[] photos = staticServerService.getImageFileById(quiz.getItem().getImage().getId(), quizcon.getImageLevel());
////				if (photos != null) {
////					quizform.setPhoto(photos);
////				}
//				quizform.setPhotourl(staticServerService.getImageFileURL(quiz.getItem().getImage().getId(), quizcon.getImageLevel()));
//			}
//			quizform.setContent(quiz.getQuizcontent());
//			
//		} else {
//			quizform.setErrorCode(Constants.ErrorCode_No_Quiz);
//		}
//
//		return quizform;
//	}
//
//	private LLQuizDTO findQuizByTime(QuizCondition quizCon) {
//		return this.quizService.findQuiz(quizCon);
//	}
//
//	private LLQuizDTO findQuizByLocation(QuizCondition quizCon) {
//		return this.quizService.findQuiz(quizCon);
//	}
//
//	
//
//	public QuizForm checkQuizAnswer(QuizCondition quizcon) {
//		MyQuiz myquiz = this.quizService.checkAnswer(quizcon);
//		Integer answer = Integer.valueOf(myquiz.getLlquiz().getAnswer());
//		Integer answerstate = null;
//		if (answer.equals(quizcon.getAnswer())) {
//			answerstate = Constants.CorrectAnsweredState;
//		} else
//			answerstate = Constants.WrongAnsweredState;
//
//		QuizForm quizform = null;
//
//		if (myquiz != null) {
//			quizform = new QuizForm();
//			if (quizcon.getUser().getMobileEmail() != null
//					&& quizcon.getUser().getMobileEmail().length() > 0) {
//				quizform.setMemail(quizcon.getUser().getMobileEmail());
//			} else {
//				quizform.setMemail(quizcon.getUser().getPcEmail());
//			}
//
//			if (answer.equals(quizcon.getAnswer())) {
//				quizform.setComment(Constants.rightAnswerComment);
//			} else {
//				quizform.setComment(Constants.wrongAnswerComment);
//			}
//			quizform.setId(myquiz.getId());
//			List<LLQuizChoice> choices = myquiz.getLlquiz().getChoices();
//			if (choices != null && choices.size() >= 4) {
//				if (choices.get(0) != null) {
//					quizform.setChoice1(choices.get(0).getContent());
//					quizform.setNote1(choices.get(0).getNote());
//				}
//				if (choices.get(1) != null) {
//					quizform.setChoice2(choices.get(1).getContent());
//					quizform.setNote2(choices.get(1).getNote());
//				}
//				if (choices.get(2) != null) {
//					quizform.setChoice3(choices.get(2).getContent());
//					quizform.setNote3(choices.get(2).getNote());
//				}
//				if (choices.get(3) != null) {
//					quizform.setChoice4(choices.get(3).getContent());
//					quizform.setNote4(choices.get(3).getNote());
//				}
//			}
//			if (myquiz.getLlquiz().getItem().getImage() != null) {
//				if (myquiz.getLlquiz().getItem().getImage() != null) {
//					quizform.setPhotourl(staticServerService.getImageFileURL(myquiz.getLlquiz().getItem().getImage().getId(), quizcon.getImageLevel()));
//				}
//			}
//			quizform.setContent(myquiz.getLlquiz().getQuizcontent());
//			quizform.setAnswer(answer);
//			quizform.setAnswerstate(answerstate);
//		}
//		return quizform;
//	}
//
//	private Users validateAuth(String userEmail, String password)
//			throws Exception {
//		if (StringUtils.isBlank(userEmail)) {
//			throw new Exception(Constants.Item_USER_EMAIL_EMPTY.toString());
//		}
//
//		if (StringUtils.isBlank(password)) {
//			throw new Exception(Constants.Item_USER_PASSWORD_EMPTY.toString());
//		}
//
//		// Authentication
//		Users user = null;
//		try {
//			user = userService.findByEmail(userEmail);
//		} catch (UsernameNotFoundException usernameNotFoundException) {
//			throw new Exception(Constants.Item_USER_NOT_FOUND.toString(),
//					usernameNotFoundException);
//		} catch (DataAccessException dataAccessException) {
//			logger.error("Data Access Error", dataAccessException);
//			throw new Exception(Constants.Item_DATA_ACCESS_ERROR.toString(),
//					dataAccessException);
//		}
//		if (!passwordEncoder.encodePassword(password, "").equals(
//				user.getPassword())) {
//			throw new Exception(Constants.Item_USER_PASSWORD_ERROR.toString());
//		}
//
//		return user;
//	}

}
