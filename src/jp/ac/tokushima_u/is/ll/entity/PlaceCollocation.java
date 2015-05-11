package jp.ac.tokushima_u.is.ll.entity;

import java.util.Date;

public class PlaceCollocation {

	private String id;
	private String content;
	private String nickname;
	private String itemid;
	private String place;
	private String author_id;
	private String item_lat;
	private String item_lng;
	private String image;
	private Date create_time;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getItemid() {
		return itemid;
	}

	public void setItemid(String itemid) {
		this.itemid = itemid;
	}

	public String getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(String author_id) {
		this.author_id = author_id;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getItem_lat() {
		return item_lat;
	}

	public void setItem_lat(String item_lat) {
		this.item_lat = item_lat;
	}

	public String getItem_lng() {
		return item_lng;
	}

	public void setItem_lng(String item_lng) {
		this.item_lng = item_lng;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
