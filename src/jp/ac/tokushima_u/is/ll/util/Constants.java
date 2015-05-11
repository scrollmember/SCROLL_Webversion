package jp.ac.tokushima_u.is.ll.util;

import java.util.Locale;

/**
 *
 * @author lemonrain
 */
public class Constants {

    public static final String EnCode = Locale.ENGLISH.toString();
    public static final String JpCode = Locale.JAPANESE.toString();
    public static final String ZhCode = Locale.CHINESE.toString();

    public static final Integer MutipleChoiceNumber = 4;
 
    public static final Integer QuizNotPass = 0;
    public static final Integer QuizPass = 1;
    public static final Integer QuizEasy = 2;
    public static final Integer QuizDifficult = 3;
    
    
    public static final Integer StudyActivityAddItem = 1;
    public static final Integer StudyActivityDoQuiz = 2;
    public static final Integer StudyActivityViewItem = 3;

    public static final Integer ExperiencedState = 1;
    public static final Integer ExperienceNotState = 0;
    
    public static final Integer NotAnsweredState = -1;
    public static final Integer WrongAnsweredState = 0;
    public static final Integer CorrectAnsweredState = 1;
    public static final Integer PassAnsweredState = 2;
    public static final Integer EasyAnsweredState = 3;
    public static final Integer DifficultAnsweredState = 4;
    
    public static Integer Item_SUCCESS = 1;
    public static Integer Item_USER_EMAIL_EMPTY = 2;
    public static Integer Item_USER_PASSWORD_EMPTY = 3;
    public static Integer Item_USER_NOT_FOUND = 4;
    public static Integer Item_USER_PASSWORD_ERROR = 5;
    public static Integer Item_ITEM_CREATE_FAILED = 6;
    public static Integer Item_DATA_ACCESS_ERROR = 7;
    public static Integer Item_FILE_NAME_EMPTY = 8;
    public static Integer Item_FORM_DATA_EMPTY = 9;
    public static Integer Item_ITEM_NOT_FOUND = 10;

    public static Integer ErrorCode_No_User = 2;
    public static Integer ErrorCode_No_Quiz = 3;


    public final static Integer MailLevel = 1;
    public final static Integer SmartPhoneLevel = 2;
    public final static Integer NormalLevel = 3;
    public final static Integer IconLevel = 4;
    
    public static final Integer EnglishCourse = 1;
    public static final Integer JapaneseCourse = 2;
    public static final Integer ChineseCourse = 3;
    public static final Integer ObjectMemoryCourse = 4;
//    
//    public static final Integer WithImage = 1;
//    public static final Integer WithoutImage = 2;
    
	public static final Integer WithTimeFlg = new Integer(1);
	
	public static final Long QuizTypeTextMutiChoice = 1l;
	public static final Long QuizTypeImageMutiChoice = 2l;
	public static final Long QuizTypeYesNoQuestion= 3l;	
	
//	public static final Integer TimeAlarmType = 1;
//	public static final Integer LocationAlarmType = 2;
	public static final Integer AndroidRequestType = 3;
	public static final Integer LocationRequestType = 4;
	public static final Integer TimeReuestType = 5;
	public static final Integer MailRequestType = 6;
	public static final Integer MailPushType = 7;
	public static final Integer WebRequestType = 8;
	public static final Integer FixLocationRequestType = 9;
	public static final Integer ContextAwareRequestType = 10;
	public static final Integer UnknownType = 0;
	
	public static final String rightAnswerComment = "Congratulations! your answer is right!";
	public static final String wrongAnswerComment = "Sorry, your answer is not right!";
	
	public static final String SYSTEM_ACCOUNT_EMAIL = "learninglogforyou@gmail.com";
	
	public static final Integer AndroidNewVersion = 5;
	
	public static final Integer PriorityMyItemWeight = 1;
	public static final Integer PriorityReferItemWeight = 2;
	public static final Integer PriorityRecommendedItemWeight = 3;
//	public static final Integer PriorityMiddleWeight = 2;
	
	
	public static final boolean usingCategory = false;
	
	public static final Integer NotifyTypeTextQuiz = 1;
	public static final Integer NotifyTypeMessage = 2;
	
	public static final Integer AccessLocked = 1;
	public static final Integer AccessUnLocked = 2;
	public static final Integer MinAccessLockTime = 120;
	
	public static final String MAX_LAT = "maxlat";
	public static final String MAX_LNG = "maxlng";
	public static final String MIN_LAT = "mixlat";
	public static final String MIN_LNG = "minlng";
	public static final String MAX_TIME = "maxtime";
	public static final String MIN_TIME = "mixtime";
	public static final String MAX_SPEED = "maxspeed";
	public static final String MIN_SPEED = "mixspeed";
	
	//C2DM message collpase keys
	public static final String COLLAPSE_KEY_SYNC = "synchronize";
}
