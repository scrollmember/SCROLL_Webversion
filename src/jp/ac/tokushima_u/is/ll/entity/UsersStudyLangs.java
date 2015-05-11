package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;

public class UsersStudyLangs implements Serializable {

	private static final long serialVersionUID = 5905809612070864543L;

	private String tUsers;
	private String studyLangs;
	private Integer langOrder;

	public String gettUsers() {
		return tUsers;
	}

	public void settUsers(String tUsers) {
		this.tUsers = tUsers;
	}

	public String getStudyLangs() {
		return studyLangs;
	}

	public void setStudyLangs(String studyLangs) {
		this.studyLangs = studyLangs;
	}

	public Integer getLangOrder() {
		return langOrder;
	}

	public void setLangOrder(Integer langOrder) {
		this.langOrder = langOrder;
	}
}
