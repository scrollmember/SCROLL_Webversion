package jp.ac.tokushima_u.is.ll.util;

import java.math.BigDecimal;
import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.GeoPoint;
import jp.ac.tokushima_u.is.ll.entity.StudyArea;

public class GeoUtils {
	private static double Rad(double d) {
		return d * Math.PI / 180.0;
	}

	public static double DistanceOfTwoPoints(GeoPoint p1,GeoPoint p2){
		return DistanceOfTwoPoints(p1.getLng(),p1.getLat(), p2.getLng(), p2.getLat());
	}
	
//	public static double DistanceOfTwoPoints(GeoTimePoint p1,GeoTimePoint p2){
//		return DistanceOfTwoPoints(p1.getLng(),p1.getLat(), p2.getLng(), p2.getLat());
//	}
	
	public static boolean isHighPrecision(Double lat, Double lng){
		if(lat==null||lng==null)
			return false;
		BigDecimal lat_bg = BigDecimal.valueOf(lat);
		BigDecimal lng_bg = BigDecimal.valueOf(lng);
		if(lat_bg.scale()>3&&lng_bg.scale()>3)
			return true;
		else
			return false;
	}
	
	public static boolean validateLatLng(Double lat, Double lng){
		if(lat==null||lng==null)
			return false;
		if(lat>90||lat<-90||lng>180||lng<-180)
			return false;
		else
			return true;
	}
	
	public static GeoPoint findCenterGeo(List<StudyArea> areas){
		if(areas == null || areas.size()==0){
			return new GeoPoint(34.0657179, 134.5593601);
		}
		
		StudyArea sa = areas.get(0);
		Double maxLat = sa.getMaxlat();
		Double maxLng = sa.getMaxlng();
		Double minLat = sa.getMinlat();
		Double minLng = sa.getMinlng();
		
		for(int i=1; i<areas.size(); i++){
			sa = areas.get(i);
			if(sa.getMaxlat() > maxLat)
				maxLat = sa.getMaxlat();
			if(sa.getMaxlng() > maxLng)
				maxLng = sa.getMaxlng();
			if(sa.getMinlat() < minLat)
				minLat = sa.getMinlat();
			if(sa.getMinlng() < minLng)
				minLng = sa.getMinlng();
		}
			
		Double centerLat = (maxLat + minLat)/2;
		Double centerLng = (maxLng + minLng)/2;
		
		return new GeoPoint(centerLat, centerLng);
	}
	
	public static double DistanceOfTwoPoints(double lng1, double lat1, double lng2,
			double lat2) {
		double radLat1 = Rad(lat1);
		double radLat2 = Rad(lat2);
		double a = radLat1 - radLat2;
		double b = Rad(lng1) - Rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * 6378137.0;
		s = Math.round(s * 10000) / 10000;
		return s;
	}
	
	public static void main(String[]args){
		double lng1 = 134.6122;
		double lat1 = 34.1822;
		double lng2 = 	134.59670952;
		double lat2 = 34.16454115;
		System.out.println(isHighPrecision(lat1, lng1));
	}
}
