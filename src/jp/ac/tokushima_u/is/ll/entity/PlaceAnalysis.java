package jp.ac.tokushima_u.is.ll.entity;

import java.util.Date;

public class PlaceAnalysis {

	private String id;
	private Date create_time;
	private String item_lat;
	private String item_lng;
	private String author_id;
	private String placeinfo;
	private String attribute;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getItem_lng() {
		return item_lng;
	}
	public void setItem_lng(String item_lng) {
		this.item_lng = item_lng;
	}
	public String getItem_lat() {
		return item_lat;
	}
	public void setItem_lat(String item_lat) {
		this.item_lat = item_lat;
	}
	public String getAuthor_id() {
		return author_id;
	}
	public void setAuthor_id(String author_id) {
		this.author_id = author_id;
	}
	public String getPlaceinfo() {
		return placeinfo;
	}
	public void setPlaceinfo(String placeinfo) {
		this.placeinfo = placeinfo;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	
}
