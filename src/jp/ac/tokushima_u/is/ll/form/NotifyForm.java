package jp.ac.tokushima_u.is.ll.form;

import java.io.Serializable;

public class NotifyForm implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String notifyCode;
	private Integer alarmType; 
	private String itemid;
	private Double lat;
	private Double lng;
	private Float speed;
	private Integer feeback;
	private Long createtime;
	private Long updatetime;

	
	public Long getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Long updatetime) {
		this.updatetime = updatetime;
	}
	public String getNotifyCode() {
		return notifyCode;
	}
	public void setNotifyCode(String notifyCode) {
		this.notifyCode = notifyCode;
	}
	public Long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Long createtime) {
		this.createtime = createtime;
	}
	public Integer getFeeback() {
		return feeback;
	}
	public void setFeeback(Integer feeback) {
		this.feeback = feeback;
	}
	public Float getSpeed() {
		return speed;
	}
	public void setSpeed(Float speed) {
		this.speed = speed;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	public Integer getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(Integer alarmType) {
		this.alarmType = alarmType;
	}
	public String getItemid() {
		return itemid;
	}
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	
}
