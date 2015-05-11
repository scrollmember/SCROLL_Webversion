package jp.ac.tokushima_u.is.ll.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class TextUtils {
	public static List<String> splitString(String str){
		List<String> result = new ArrayList<String>();
		if(str==null)return result;
		String[] strs = str.split(",|、|，");
		for(String s: strs){
			if(!StringUtils.isBlank(s)){
				result.add(s.trim());
			}
		}
		return result;
	}
}
