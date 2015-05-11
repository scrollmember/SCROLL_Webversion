package jp.ac.tokushima_u.is.ll.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpeedClustering {
	public static float minSpeed = 3f;
	public static float maxRun = 7f;
	public static float maxBike = 12f;
	public static float minRailway = 50f;

	public static void main(String[] args) {
		float[] values = new float[] { 1, 2.23f, 0.3f, 0.13f, 0.22f, 0.2f,
				4.5f, 22, 4, 1.2f, 0.3f, 100 };
		List<Float> speeds = new ArrayList<Float>();
		for (int i = 0; i < values.length; i++)
			speeds.add(values[i]);
		List<Map<String, Float>> result = findSpeedPhase(speeds);
		if (result != null) {
			for (Map<String, Float> r : result) {
				System.out.println(r.toString());
			}
		}
	}

	public static List<Map<String, Float>> findSpeedPhase(List<Float> speeds) {
		Integer staytimes = 0;
		Integer runtimes = 0;
		Integer biketimes = 0;
		Integer cartimes = 0;
		Integer railtimes = 0;
		for (Float speed : speeds) {
			if (speed <= minSpeed) {
				staytimes++;
			} else if (speed > minSpeed && speed < maxRun) {
				runtimes++;
			} else if (speed >= maxRun && speed <= maxBike) {
				biketimes++;
			} else if (speed > maxBike && speed < minRailway) {
				cartimes++;
			} else if (speed >= minRailway)
				railtimes++;
		}
		float total = speeds.size();
		List<Map<String, Float>> results = new ArrayList<Map<String, Float>>();
		Map<String, Float> tempresult = new HashMap<String, Float>();
		tempresult.put(Constants.MIN_SPEED, 0f);
		tempresult.put(Constants.MAX_SPEED, minSpeed);
		results.add(tempresult);
		if ((runtimes / total) >= 0.3) {
			Map<String, Float> result = new HashMap<String, Float>();
			result.put(Constants.MIN_SPEED, minSpeed);
			result.put(Constants.MAX_SPEED, maxRun);
			results.add(result);
		} else if ((biketimes / total) >= 0.3) {
			Map<String, Float> result = new HashMap<String, Float>();
			result.put(Constants.MIN_SPEED, maxRun);
			result.put(Constants.MAX_SPEED, maxBike);
			results.add(result);
		} else if ((cartimes / total) >= 0.3) {
			Map<String, Float> result = new HashMap<String, Float>();
			result.put(Constants.MIN_SPEED, maxBike);
			result.put(Constants.MAX_SPEED, minRailway);
			results.add(result);
		} else if ((railtimes / total) >= 0.3) {
			Map<String, Float> result = new HashMap<String, Float>();
			result.put(Constants.MIN_SPEED, minRailway);
			result.put(Constants.MAX_SPEED, Float.MAX_VALUE);
			results.add(result);
		}

		return results;
	}
}
