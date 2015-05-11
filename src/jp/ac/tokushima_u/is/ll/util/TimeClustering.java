package jp.ac.tokushima_u.is.ll.util;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.ac.tokushima_u.is.ll.entity.TimeNode;

public class TimeClustering {
	public static List<Map<String,Time>> findRange(List<TimeNode> times){
		System.out.print("times total length is "+times.size());
		float totalsize = times.size();
		int i = 1;
		for(TimeNode temp:times){
			System.out.print("("+temp.getTime()+")");
			if(i%5==0)
				System.out.println();
			i++;
		}
		System.out.println();
		List<Map<String,Time>>ranges = new ArrayList<Map<String,Time>>();
		
		if(times==null||times.size()==0)
			return ranges;
		
		List<TimeNode> roots = findClosest(times);
		Set<TimeNode>largestNodes = new HashSet<TimeNode>();
		for(TimeNode root:roots){
			Set<TimeNode>nodes = new HashSet<TimeNode>();
			nodes = getLeaves(root,nodes);
			if(nodes.size()>largestNodes.size())
				largestNodes = nodes;
			
			float rate = (float)nodes.size()/totalsize;
			System.out.println("time nodes size is: "+nodes.size()+"  rate is: "+rate+"   total size is:"+totalsize);
			
			if(rate>=0.3){
				Map<String,Time>result = new HashMap<String,Time>();
				Time maxTime = Utility.getMinTime();
				Time minTime = Utility.getMaxTime();
				for(TimeNode td:nodes){
					if(Utility.isBigger(td.getTime(), minTime)<0)
						minTime = td.getTime();
					if(Utility.isBigger(td.getTime(), maxTime)>0)
						maxTime = td.getTime();
				}
				if(!Utility.isEqual(Utility.getMinTime(), maxTime)&&!Utility.isEqual(Utility.getMaxTime(), minTime)){
					result.put(Constants.MAX_TIME, maxTime);
					result.put(Constants.MIN_TIME, minTime);
				}
				ranges.add(result);
			}
			
			System.out.println("time nodes size is "+nodes.size());
		}
		
		if(ranges.size()==0&&largestNodes.size()>0){
			Map<String,Time>result = new HashMap<String,Time>();
			Time maxTime = Utility.getMinTime();
			Time minTime = Utility.getMaxTime();
			for(TimeNode td:largestNodes){
				if(Utility.isBigger(td.getTime(), minTime)<0)
					minTime = td.getTime();
				if(Utility.isBigger(td.getTime(), maxTime)>0)
					maxTime = td.getTime();
			}
			if(!Utility.isEqual(Utility.getMinTime(), maxTime)&&!Utility.isEqual(Utility.getMaxTime(), minTime)){
				result.put(Constants.MAX_TIME, maxTime);
				result.put(Constants.MIN_TIME, minTime);
			}
			ranges.add(result);
		}
		
		
		return ranges;
	}
	
//	public static void printTree(TimeNode t,String sign){
//		PrintStream out = System.out;
//		if(t!=null){
//			out.print(sign+":   "+t.getTime().toLocaleString());
//			out.println();
//			printTree(t.getLeft(),"left");
//			printTree(t.getRight(),"right");
//			out.println( );
//		}else
//			return;
//	}
	
	public static List<TimeNode> findClosest(List<TimeNode> times){
		int closest = 60;
		int a = times.size();

		TimeNode t1 = null;
		TimeNode t2 = null;
 		int distance = Integer.MAX_VALUE;
		for(int i=0;i<times.size();i++)
			for(int j=i+1;j<times.size();j++){
				int difference = Utility.getDifferMinuteTwoTimes(times.get(i).getTime(),times.get(j).getTime());
				if(difference<distance){
					distance = difference;
					t1 = times.get(i);
					t2 = times.get(j);
				}
			}
		if(t1!=null&&t2!=null&&distance<=closest){
			times.remove(t1);
			times.remove(t2);
			TimeNode midtime = getMidTime(t1,t2);
			times.add(midtime);
		}
		
		int b = times.size();
		
		if(a==b)
			return times;
		else
			return findClosest(times);
	}

	
	public static Set<TimeNode> getLeaves(TimeNode tn, Set<TimeNode>leaves){
		if(tn!=null&&tn.getLeft()==null&&tn.getRight()==null){
			leaves.add(tn);
			return leaves;
		}else{
			if(tn!=null&&tn.getLeft()!=null){
				Set<TimeNode> leftleaves =  getLeaves(tn.getLeft(), leaves);
				for(TimeNode leaf:leftleaves){
					leaves.add(leaf);
				}
			}
			if(tn!=null&&tn.getRight()!=null){
				Set<TimeNode> rightleaves = getLeaves(tn.getRight(), leaves);
				for(TimeNode leaf:rightleaves){
					leaves.add(leaf);
				}
			}
			return leaves;
		}
			
	}
	
	public static TimeNode getMidTime(TimeNode t1, TimeNode t2){
		TimeNode tn = new TimeNode(Utility.getMiddleTime(t1.getTime(), t2.getTime()));
		tn.setLeft(t1);
		tn.setRight(t2);
		return tn;
	}
}
