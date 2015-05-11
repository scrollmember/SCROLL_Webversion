package jp.ac.tokushima_u.is.ll.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Convert data:Map<TagName:String, Count:Long> to
 * tagCloud:Map<TagName:String,TagLevel:Integer>
 * 
 * @author Houbin
 * 
 */
public class TagCloudConverter {

	public static Map<String, Integer> convert(Map<String, Long> data) {
		Long max = Long.MIN_VALUE;
		for (String key : data.keySet()) {
			if (data.get(key) > max) {
				max = data.get(key);
			}
		}

		// Get i
		int i = 1;
		while (i * 5 < max) {
			i++;
		}

		long level = i * 5;
		long cloud1 = level / 5;
		long cloud2 = level / 5 * 2;
		long cloud3 = level / 5 * 3;
		long cloud4 = level / 5 * 4;

		Map<String, Integer> result = new HashMap<String, Integer>();
		for (String key : data.keySet()) {
			if (data.get(key) <= cloud1) {
				result.put(key, 1);
			} else if (data.get(key) > cloud1 && data.get(key) <= cloud2) {
				result.put(key, 2);
			} else if (data.get(key) > cloud2 && data.get(key) <= cloud3) {
				result.put(key, 3);
			} else if (data.get(key) > cloud3 && data.get(key) <= cloud4) {
				result.put(key, 4);
			} else {
				result.put(key, 5);
			}
		}
		return result;
	}
}
