package jp.ac.tokushima_u.is.ll.form;

import java.io.Serializable;

public class UserSearchCondForm implements Serializable {

	private static final long serialVersionUID = 1887594841894528266L;

	private Integer page;
	private String realname;
	private String nickname;
	private String email;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
