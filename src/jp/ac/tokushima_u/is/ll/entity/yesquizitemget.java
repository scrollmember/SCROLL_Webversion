package jp.ac.tokushima_u.is.ll.entity;

import java.util.Date;

public class yesquizitemget {

	private String item;
	private String content;
	private String language;
	private String nickname;
	private String author_id;
	private String image;
	private Date create_time;
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
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
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getAuthor_id() {
		return author_id;
	}
	public void setAuthor_id(String author_id) {
		this.author_id = author_id;
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
}
