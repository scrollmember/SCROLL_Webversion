package jp.ac.tokushima_u.is.ll.form;

import java.util.Map;

public class UserInfoForm {
	private String username;
	private String password;
	private String userid;
	private String defcategory;
	private String nickname;
	private	Map<String,String>categorys;
	private Integer login_error_code;
	private Map<String,String> studylans;
	private Map<String,String> mylans;
	private Long update;


	// ■wakebe
	private Integer userlevel;
	private Integer experiencepoint;


	public Long getUpdate() {
		return update;
	}
	public void setUpdate(Long update) {
		this.update = update;
	}
	public Map<String, String> getStudylans() {
		return studylans;
	}
	public void setStudylans(Map<String, String> studylans) {
		this.studylans = studylans;
	}
	public Map<String, String> getMylans() {
		return mylans;
	}
	public void setMylans(Map<String, String> mylans) {
		this.mylans = mylans;
	}
	public Integer getLogin_error_code() {
		return login_error_code;
	}
	public void setLogin_error_code(Integer loginErrorCode) {
		login_error_code = loginErrorCode;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getDefcategory() {
		return defcategory;
	}
	public void setDefcategory(String defcategory) {
		this.defcategory = defcategory;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Map<String, String> getCategorys() {
		return categorys;
	}
	public void setCategorys(Map<String, String> categorys) {
		this.categorys = categorys;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}


	// ■wakebe
	public Integer getExperincePoint() {
		return experiencepoint;
	}

	public void setExperiencePoint(Integer experiencepoint) {
		this.experiencepoint = experiencepoint;
	}

	public Integer getUserLevel() {
		return userlevel;
	}

	public void setLevel(Integer userlevel) {
		this.userlevel = userlevel;
	}
}