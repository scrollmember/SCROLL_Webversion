package jp.ac.tokushima_u.is.ll.dto;

import java.io.Serializable;

public class PacallTagCount implements Serializable {

	private static final long serialVersionUID = -7976272019138595637L;

	private String tag;
	private String tagName;
	private Long number;

	public PacallTagCount(){
	}
	
	public PacallTagCount(String tag, String tagName, Long number) {
		this.tag = tag;
		this.tagName = tagName;
		this.number = number;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

}
