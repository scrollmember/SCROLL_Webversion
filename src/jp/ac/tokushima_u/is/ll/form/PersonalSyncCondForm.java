package jp.ac.tokushima_u.is.ll.form;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author Lemonrain
 */
public class PersonalSyncCondForm implements Serializable {
	private static final long serialVersionUID = 5560865875193026838L;
	private Date areaUpdateTime;
	private Date timeUpdateTime;
	private Date sendUpdateTime;
	
	public Date getSendUpdateTime() {
		return sendUpdateTime;
	}
	public void setSendUpdateTime(Date sendUpdateTime) {
		this.sendUpdateTime = sendUpdateTime;
	}
	public Date getAreaUpdateTime() {
		return areaUpdateTime;
	}
	public void setAreaUpdateTime(Date areaUpdateTime) {
		this.areaUpdateTime = areaUpdateTime;
	}
	public Date getTimeUpdateTime() {
		return timeUpdateTime;
	}
	public void setTimeUpdateTime(Date timeUpdateTime) {
		this.timeUpdateTime = timeUpdateTime;
	}
}
