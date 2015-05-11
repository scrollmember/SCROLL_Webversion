package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
import java.util.Date;

public class ItemTitleBatis implements Serializable {

	private static final long serialVersionUID = 6794261676719764732L;
	private String id;
	private String content;
	private String language;
	private String item;
	
	private String itemid;
	private String item_lat;
	private String item_lng;
	private String author_id;
	private String nickname;
	private Date createTime;

//	public  String ItemTitle(Item item2) {
//		// TODO Auto-generated constructor stub
//		return content;
//		
//	}
//	public void ItemTitle2(Item item3) {
//		// TODO Auto-generated constructor stub
//		item3.setContent(content);
//		
//	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getItemid() {
		return itemid;
	}

	public void setItemid(String itemid) {
		this.itemid = itemid;
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

	public String getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(String author_id) {
		this.author_id = author_id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}



	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}



}
