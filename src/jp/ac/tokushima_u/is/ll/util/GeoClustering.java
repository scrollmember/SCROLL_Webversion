package jp.ac.tokushima_u.is.ll.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.ac.tokushima_u.is.ll.entity.GeoPoint;

public class GeoClustering {

	public static List<Map<String,Double>>getRange(List<GeoPoint> geoPoints){
		List<Map<String,Double>>ranges = new ArrayList<Map<String,Double>>();
		if(geoPoints==null||geoPoints.size()==0)
			return ranges;
		float totalsize = geoPoints.size();
		List<GeoPoint>roots = findClosest(geoPoints);
		Set<GeoPoint> largestleaves = new HashSet<GeoPoint>();
		for(GeoPoint root:roots){
			Set<GeoPoint> leaves = new HashSet<GeoPoint>();
			getLeaves(root,leaves);
			
			if(leaves.size()>largestleaves.size())
				largestleaves = leaves;
			
			float rate = (float)leaves.size()/totalsize;
			if(rate>0.3){
				Map<String,Double> result = new HashMap<String,Double>();
				
				Double maxLat = Double.MIN_VALUE;
				Double maxLng = Double.MIN_VALUE;
				Double minLat = Double.MAX_VALUE;
				Double minLng = Double.MAX_VALUE;
				
				for(GeoPoint geopoint:leaves){
					if(maxLat<geopoint.getLat())
						maxLat = geopoint.getLat();
					if(maxLng<geopoint.getLng())
						maxLng = geopoint.getLng();
					if(minLat>geopoint.getLat())
						minLat = geopoint.getLat();
					if(minLng>geopoint.getLng())
						minLng = geopoint.getLng();
				}
				if(maxLat!=Double.MIN_VALUE&&maxLng!=Double.MIN_VALUE&&minLat!=Double.MAX_VALUE&&minLng!=Double.MAX_VALUE){
					result.put(Constants.MAX_LAT, maxLat);
					result.put(Constants.MAX_LNG, maxLng);
					result.put(Constants.MIN_LAT, minLat);
					result.put(Constants.MIN_LNG, minLng);
				}
				ranges.add(result);
			}
		}
		
		if(ranges.size()==0&&largestleaves.size()>0){
			Map<String,Double> result = new HashMap<String,Double>();
			
			Double maxLat = Double.MIN_VALUE;
			Double maxLng = Double.MIN_VALUE;
			Double minLat = Double.MAX_VALUE;
			Double minLng = Double.MAX_VALUE;
			
			for(GeoPoint geopoint:largestleaves){
				if(maxLat<geopoint.getLat())
					maxLat = geopoint.getLat();
				if(maxLng<geopoint.getLng())
					maxLng = geopoint.getLng();
				if(minLat>geopoint.getLat())
					minLat = geopoint.getLat();
				if(minLng>geopoint.getLng())
					minLng = geopoint.getLng();
			}
			if(maxLat!=Double.MIN_VALUE&&maxLng!=Double.MIN_VALUE&&minLat!=Double.MAX_VALUE&&minLng!=Double.MAX_VALUE){
				result.put(Constants.MAX_LAT, maxLat);
				result.put(Constants.MAX_LNG, maxLng);
				result.put(Constants.MIN_LAT, minLat);
				result.put(Constants.MIN_LNG, minLng);
			}
			ranges.add(result);
		}
		
		return ranges;
	}
	
	public static Set<GeoPoint> getLeaves(GeoPoint p, Set<GeoPoint>leaves){
		if(p!=null&&p.getLeft()==null&&p.getRight()==null){
			leaves.add(p);
			return leaves;
		}else{
			if(p!=null&&p.getLeft()!=null){
				Set<GeoPoint> leftleaves =  getLeaves(p.getLeft(), leaves);
				for(GeoPoint leaf:leftleaves){
					leaves.add(leaf);
				}
			}
			if(p!=null&&p.getRight()!=null){
				Set<GeoPoint> rightleaves = getLeaves(p.getRight(), leaves);
				for(GeoPoint leaf:rightleaves){
					leaves.add(leaf);
				}
			}
			return leaves;
		}
			
	}
	
	public static GeoPoint getMidGeoPoint(GeoPoint p1, GeoPoint p2){
		Double lat = (p1.getLat()+p2.getLat())/2;
		Double lng = (p1.getLng()+p2.getLng())/2;
		GeoPoint p = new GeoPoint(lat,lng);
		
		p.setLeft(p1);
		p.setRight(p2);
		return p;
	}
	
	public static List<GeoPoint> findClosest(List<GeoPoint> GeoPoints){
		double closest = 100;
		int a = GeoPoints.size();
		
		GeoPoint p1 = null;
		GeoPoint p2 = null;
 		double distance = Double.MAX_VALUE;
		for(int i=0;i<GeoPoints.size();i++)
			for(int j=i+1;j<GeoPoints.size();j++){
				double tempdistance = GeoUtils.DistanceOfTwoPoints(GeoPoints.get(i),GeoPoints.get(j));
				if(tempdistance<distance){
					distance = tempdistance;
					p1 = GeoPoints.get(i);
					p2 = GeoPoints.get(j);
				}
			}
		if(p1!=null&&p2!=null&&distance<=closest){
			GeoPoints.remove(p1);
			GeoPoints.remove(p2);
			GeoPoint midGeoPoint = getMidGeoPoint(p1,p2);
			GeoPoints.add(midGeoPoint);
		}
		
		int b = GeoPoints.size();
		
		if(a==b)
			return GeoPoints;
		else
			return findClosest(GeoPoints);
	}
}
