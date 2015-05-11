package jp.ac.tokushima_u.is.ll.util;

public class CourseUtil {

	public static Integer converCourseIdFromString(String lanCode){
		if("en".equals(lanCode))
			return Constants.EnglishCourse;
		else if("ja".equals(lanCode))
			return Constants.JapaneseCourse;
		else if("zh".equals(lanCode))
			return Constants.ChineseCourse;
		else
			return null;
	}
	
	public static String convertCodeFromId(Integer courseid){
		if(Constants.EnglishCourse.equals(courseid))
			return "en";
		else if(Constants.JapaneseCourse.equals(courseid))
			return "ja";
		else if(Constants.ChineseCourse.equals(courseid))
			return "zh";
		else
			return "";
	}
	
	public static String convertLanguageFromId(Integer courseid){
		if(Constants.EnglishCourse.equals(courseid))
			return "English";
		else if(Constants.JapaneseCourse.equals(courseid))
			return "Japanese";
		else if(Constants.ChineseCourse.equals(courseid))
			return "Chinese";
		else
			return "";
	}
}
