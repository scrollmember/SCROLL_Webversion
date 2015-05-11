package jp.ac.tokushima_u.is.ll.service.helper;

import java.util.Date;

import jp.ac.tokushima_u.is.ll.entity.Users;

public class UserQuizInfo {
	private Users user;
	private Integer righttimes;
	private Integer wrongtimes;
	private Date accesstime;
	private Integer uploadnumber;
	private Integer relognumber;
	private Integer referencenumber;
	private Integer myreferencenumber;
	
	
	public Integer getReferencenumber() {
		return referencenumber;
	}
	public void setReferencenumber(Integer referencenumber) {
		this.referencenumber = referencenumber;
	}
	public Integer getMyreferencenumber() {
		return myreferencenumber;
	}
	public void setMyreferencenumber(Integer myreferencenumber) {
		this.myreferencenumber = myreferencenumber;
	}
	public Integer getUploadnumber() {
		return uploadnumber;
	}
	public void setUploadnumber(Integer uploadnumber) {
		this.uploadnumber = uploadnumber;
	}
	public Integer getRelognumber() {
		return relognumber;
	}
	public void setRelognumber(Integer relognumber) {
		this.relognumber = relognumber;
	}
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
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
	public Date getAccesstime() {
		return accesstime;
	}
	public void setAccesstime(Date accesstime) {
		this.accesstime = accesstime;
	}
}
