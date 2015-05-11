package jp.ac.tokushima_u.is.ll.entity;

public class GeoPoint{
	private Double lat;
	private Double lng;
	private GeoPoint left;
	private GeoPoint right;
	
	
	public GeoPoint(Double lat, Double lng){
		this.lat = lat;
		this.lng = lng;
	}

	
	public GeoPoint getLeft() {
		return left;
	}



	public void setLeft(GeoPoint left) {
		this.left = left;
	}



	public GeoPoint getRight() {
		return right;
	}



	public void setRight(GeoPoint right) {
		this.right = right;
	}



	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}
	
}
