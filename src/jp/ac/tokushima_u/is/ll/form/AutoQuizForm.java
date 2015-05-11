package jp.ac.tokushima_u.is.ll.form;

import java.io.Serializable;

public class AutoQuizForm implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String email;
	private String quizid;
	private Integer answer;
	private Integer imagelevel;
	private Integer alarmtype;
	private Integer quiztype;
	
	
	public Integer getQuiztype() {
		return quiztype;
	}
	public void setQuiztype(Integer quiztype) {
		this.quiztype = quiztype;
	}
	public Integer getAlarmtype() {
		return alarmtype;
	}
	public void setAlarmtype(Integer alarmtype) {
		this.alarmtype = alarmtype;
	}
	public Integer getImagelevel() {
		return imagelevel;
	}
	public void setImagelevel(Integer imagelevel) {
		this.imagelevel = imagelevel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getQuizid() {
		return quizid;
	}
	public void setQuizid(String quizid) {
		this.quizid = quizid;
	}
	public Integer getAnswer() {
		return answer;
	}
	public void setAnswer(Integer answer) {
		this.answer = answer;
	}
}
