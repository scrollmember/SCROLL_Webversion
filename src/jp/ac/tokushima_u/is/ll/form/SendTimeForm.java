package jp.ac.tokushima_u.is.ll.form;

import jp.ac.tokushima_u.is.ll.entity.SendTime;
import jp.ac.tokushima_u.is.ll.util.Utility;

public class SendTimeForm {
	
	public SendTimeForm(SendTime time){
		this.sendtime = Utility.getSeconds(time.getSendtime());
		this.typ = time.getTyp();
		this.author_id = time.getAuthor().getId();
		if(time!=null && time.getCreateDate()!=null)
			this.createDate = time.getCreateDate().getTime();
	}
	
	private Integer sendtime;
	private Integer typ;
	private String author_id;
	private Long createDate;
	
	public Integer getSendtime() {
		return sendtime;
	}
	public void setSendtime(Integer sendtime) {
		this.sendtime = sendtime;
	}
	public Integer getTyp() {
		return typ;
	}
	public void setTyp(Integer typ) {
		this.typ = typ;
	}
	public String getAuthor_id() {
		return author_id;
	}
	public void setAuthor_id(String author_id) {
		this.author_id = author_id;
	}
	public Long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}
}
