package jp.ac.tokushima_u.is.ll.entity;

import java.util.Date;

//@Entity
//@Table(name = "T_CORRECTRATEHISTORY")
public class CorrectRateHistory {
	private Date quizDay;
	private Integer righttimes;
	private Integer wrongtimes;
	private Users user;
	public Date getQuizDay() {
		return quizDay;
	}
	public void setQuizDay(Date quizDay) {
		this.quizDay = quizDay;
	}
	public Integer getRighttimes() {
		return righttimes;
	}
	public void setRighttimes(Integer righttimes) {
		this.righttimes = righttimes;
	}
	public Integer getWrongtimes() {
		return wrongtimes;
	}
	public void setWrongtimes(Integer wrongtimes) {
		this.wrongtimes = wrongtimes;
	}
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
}
