package jp.ac.tokushima_u.is.ll.util;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.entity.TimeNode;

public class TimeRankClustering {
	public static List<Map<String, Time>> findRange(List<TimeNode> times) {
		float totalsize = times.size();
		TimeNode[] a = new TimeNode[times.size()];
		a = times.toArray(a);
		quickSort(a, 0, a.length - 1);
		List<List<TimeNode>> results = new LinkedList<List<TimeNode>>();
		List<TimeNode> result = new LinkedList<TimeNode>();
		for (int j = 0; j < a.length - 1; j++) {
			result.add(a[j]);
			if (((a[j + 1].getLength() - a[j].getLength()) < 1200)&&(result.get(result.size()-1).getLength()-result.get(0).getLength())<60*90) {

			} else {
				results.add(result);
				result = new LinkedList<TimeNode>();
			}
		}
		
		List<Map<String, Time>> ranges = new ArrayList<Map<String, Time>>();
		List<TimeNode> largest = new LinkedList<TimeNode>();
		for (int i = 0; i < results.size(); i++) {
			List<TimeNode> tempresult = results.get(i);
			if (tempresult.size() > largest.size())
				largest = tempresult;
			if ((tempresult.size() / totalsize) >= 0.2) {
				Map<String, Time> range = new HashMap<String, Time>();
				Time minTime = tempresult.get(0).getTime();
				Time maxTime = tempresult.get(tempresult.size() - 1).getTime();
				range.put(Constants.MAX_TIME, maxTime);
				range.put(Constants.MIN_TIME, minTime);
				ranges.add(range);
			}
		}

		if (ranges.size() == 0 && largest.size() > 0) {
			Map<String, Time> range = new HashMap<String, Time>();
			Time minTime = largest.get(0).getTime();
			Time maxTime = largest.get(largest.size() - 1).getTime();
			range.put(Constants.MAX_TIME, maxTime);
			range.put(Constants.MIN_TIME, minTime);
			ranges.add(range);
		}
		return ranges;
	}

	static int Partition(TimeNode[] a, int left, int right) {
		TimeNode tmp;

		// 进行一趟快速排序,返回中心记录位置
		TimeNode pivot = a[left];// 把中心置于a[0]
		while (left < right) {
			while (left < right && a[right].getLength() >= pivot.getLength())
				right--;
			// 将比中心记录小的移到低端
			tmp = a[right];
			a[right] = a[left];
			a[left] = tmp;
			while (left < right && a[left].getLength() <= pivot.getLength())
				left++;
			tmp = a[right];
			a[right] = a[left];
			a[left] = tmp;
			// 将比中心记录大的移到高端
		}
		a[left] = pivot; // 中心移到正确位置
		return left; // 返回中心位置
	}

	public static void quickSort(TimeNode[] a, int left, int right) {
		if (left >= right - 1)
			return;

		int pivot = Partition(a, left, right);
		quickSort(a, left, pivot - 1);
		quickSort(a, pivot + 1, right);
	}
}
