package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "T_ITEM_TITLE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ItemTitle implements Serializable {

	private static final long serialVersionUID = 6794261676719764732L;
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(length = 32)
	private String id;
	@Column(name = "content", length = 1024)
	private String content;
	@ManyToOne
	private Language language;
	@ManyToOne
	private Item item;

//	private String itemid;
//	private String item_lat;
//	private String item_lng;
//	private String author_id;
//	private String nickname;
//	private Date createTime;
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

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Item getItem() {
		return item;
	}

//	public String getItemid() {
//		return itemid;
//	}
//
//	public void setItemid(String itemid) {
//		this.itemid = itemid;
//	}
//
//	public String getItem_lat() {
//		return item_lat;
//	}
//
//	public void setItem_lat(String item_lat) {
//		this.item_lat = item_lat;
//	}
//
//	public String getItem_lng() {
//		return item_lng;
//	}
//
//	public void setItem_lng(String item_lng) {
//		this.item_lng = item_lng;
//	}
//
//	public String getAuthor_id() {
//		return author_id;
//	}
//
//	public void setAuthor_id(String author_id) {
//		this.author_id = author_id;
//	}
//
//	public String getNickname() {
//		return nickname;
//	}
//
//	public void setNickname(String nickname) {
//		this.nickname = nickname;
//	}
//
//	public Date getCreateTime() {
//		return createTime;
//	}
//
//	public void setCreateTime(Date createTime) {
//		this.createTime = createTime;
//	}

}
