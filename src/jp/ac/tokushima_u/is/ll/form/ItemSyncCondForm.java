package jp.ac.tokushima_u.is.ll.form;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author Lemonrain
 */
public class ItemSyncCondForm implements Serializable {
	private static final long serialVersionUID = 5560865875193026838L;
	private Date updateDateFrom;
	private Date updateDateTo;
	private String userId;
	private String notuserId;
	private Integer num;

	public Date getUpdateDateTo() {
		return updateDateTo;
	}
	public void setUpdateDateTo(Date updateDateTo) {
		this.updateDateTo = updateDateTo;
	}
	public Date getUpdateDateFrom() {
		return updateDateFrom;
	}
	public void setUpdateDateFrom(Date updateDateFrom) {
		this.updateDateFrom = updateDateFrom;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getNotuserId() {
		return notuserId;
	}
	public void setNotuserId(String notuserId) {
		this.notuserId = notuserId;
	}
}
