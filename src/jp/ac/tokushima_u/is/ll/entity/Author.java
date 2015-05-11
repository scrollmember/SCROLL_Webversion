package jp.ac.tokushima_u.is.ll.entity;

public class Author {
	private String id;
	private String nickname;
	private String user_level;
	private String experience_point;
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getUser_level() {
		return user_level;
	}
	public void setUser_level(String user_level) {
		this.user_level = user_level;
	}
	public String getExperience_point() {
		return experience_point;
	}
	public void setExperience_point(String experience_point) {
		this.experience_point = experience_point;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
