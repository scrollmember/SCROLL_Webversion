package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;

public class Kasetting implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4310776664627696447L;

	
	private Boolean random;
	private Boolean yifan;
//	private int viewdistance;
//	private int kaquality;
	private String id;
	private String authorid;
	private String layout;
	private String viewdistance;
	private String kaquality;
	public Boolean getRandom() {
		return random;
	}
	public void setRandom(Boolean random) {
		this.random = random;
	}
	public Boolean getYifan() {
		return yifan;
	}
	public void setYifan(Boolean yifan) {
		this.yifan = yifan;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAuthorid() {
		return authorid;
	}
	public void setAuthorid(String authorid) {
		this.authorid = authorid;
	}
	public String getLayout() {
		return layout;
	}
	public void setLayout(String layout) {
		this.layout = layout;
	}
	public String getViewdistance() {
		return viewdistance;
	}
	public void setViewdistance(String viewdistance) {
		this.viewdistance = viewdistance;
	}
	public String getKaquality() {
		return kaquality;
	}
	public void setKaquality(String kaquality) {
		this.kaquality = kaquality;
	}
}
