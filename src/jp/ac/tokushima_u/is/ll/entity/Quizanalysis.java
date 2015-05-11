package jp.ac.tokushima_u.is.ll.entity;

import java.util.Date;

public class Quizanalysis {
	private String id;
	private String answerstate;
	private Date create_date;
	private String latitude;
	private String longitude;
	private String author_id;
	private String myanswer;
	private String item_id;
	private String language;
	private String content;
	private String image;
	private String nickname;
	private String total;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAnswerstate() {
		return answerstate;
	}

	public void setAnswerstate(String answerstate) {
		this.answerstate = answerstate;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(String author_id) {
		this.author_id = author_id;
	}

	public String getMyanswer() {
		return myanswer;
	}

	public void setMyanswer(String myanswer) {
		this.myanswer = myanswer;
	}

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		this.item_id = item_id;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

}
