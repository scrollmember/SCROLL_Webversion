package jp.ac.tokushima_u.is.ll.form;

import java.io.Serializable;
import java.sql.Time;

public class ContextAwareForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private Double latitude;
    private Double longitude;
    private Float speed;
    private Time current;
    
    public Time getCurrent() {
		return current;
	}
	public void setCurrent(Time current) {
		this.current = current;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Float getSpeed() {
		return speed;
	}
	public void setSpeed(Float speed) {
		this.speed = speed;
	}
}
