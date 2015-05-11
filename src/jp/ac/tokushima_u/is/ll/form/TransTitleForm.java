package jp.ac.tokushima_u.is.ll.form;

import java.io.Serializable;

public class TransTitleForm implements Serializable {

	private static final long serialVersionUID = 1927735293984335790L;
	private String target;
	private String userId;
	private String titles;

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setTitles(String titles) {
		this.titles = titles;
	}

	public String getTitles() {
		return titles;
	}

}
