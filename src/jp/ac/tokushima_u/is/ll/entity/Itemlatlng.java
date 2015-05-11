package jp.ac.tokushima_u.is.ll.entity;

import java.util.Date;

public class Itemlatlng {

	private String id;
	private String item_lat;
	private String item_lng;
	private Date create_time;
	private String learningplace;
	private int lcount;
	private String attribute;
	private int cc;
	private String content;
	private String nickname;
	private String dashtime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getLearningplace() {
		return learningplace;
	}
	public void setLearningplace(String learningplace) {
		this.learningplace = learningplace;
	}
	public int getLcount() {
		return lcount;
	}
	public void setLcount(int lcount) {
		this.lcount = lcount;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public int getCc() {
		return cc;
	}
	public void setCc(int cc) {
		this.cc = cc;
	}
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
	public String getDashtime() {
		return dashtime;
	}
	public void setDashtime(String dashtime) {
		this.dashtime = dashtime;
	}
}
