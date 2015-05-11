package jp.ac.tokushima_u.is.ll.form;

import java.io.Serializable;

public class QuizChoiceForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String choiceid;
	private String myquizid;
	private String content;
	private Integer number;
	private String note;
	private String filetype;
	private String lanCode;
	
	public String getLanCode() {
		return lanCode;
	}
	public void setLanCode(String lanCode) {
		this.lanCode = lanCode;
	}
	public String getChoiceid() {
		return choiceid;
	}
	public void setChoiceid(String choiceid) {
		this.choiceid = choiceid;
	}
	public String getMyquizid() {
		return myquizid;
	}
	public void setMyquizid(String myquizid) {
		this.myquizid = myquizid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getFiletype() {
		return filetype;
	}
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}
	
}
