package jp.ac.tokushima_u.is.ll.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {

	public static Map<String, Double> getNearArea(Double lat, Double lng) {
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

		Map<String, Double> result = new HashMap<String, Double>();

		result.put("x1", x1);
		result.put("y1", y1);
		result.put("x2", x2);
		result.put("y2", y2);
		return result;
	}

}
