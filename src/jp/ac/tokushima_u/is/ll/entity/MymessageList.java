package jp.ac.tokushima_u.is.ll.entity;

import java.util.Date;

public class MymessageList {

	private String id;
	private String content;
	private Date create_time;
	private String send_from;
	private String send_to;
	private String nickname;
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
	public String getSend_from() {
		return send_from;
	}
	public void setSend_from(String send_from) {
		this.send_from = send_from;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getSend_to() {
		return send_to;
	}
	public void setSend_to(String send_to) {
		this.send_to = send_to;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	
}
