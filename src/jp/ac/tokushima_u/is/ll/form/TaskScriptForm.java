package jp.ac.tokushima_u.is.ll.form;

import java.io.Serializable;
import java.sql.Time;

import org.springframework.web.multipart.MultipartFile;

public class TaskScriptForm implements Serializable {

    private static final long serialVersionUID = 5957045426756171964L;
    private String taskId;
    private Double lat;
    private Double lng;
    private Integer zoom;
	 private String place;
    private Boolean locationBased = Boolean.TRUE;
    private Boolean timeBased = Boolean.TRUE;
    private Time starttime;
    private Time endtime;
    private String script;
    private MultipartFile image;
    private Integer num;
    
    private String starthour;
    private String startmin;
    private String endhour;
    private String endmin;
    
    public void clear(){
    	this.lat = null;
    	this.taskId = null;
    	this.lng = null;
    	this.zoom = null;
    	this.locationBased = Boolean.TRUE;
    	this.script = null;
    	this.image = null;
    	this.num = null;
    };
    
    
	public String getPlace() {
		return place;
	}



	public void setPlace(String place) {
		this.place = place;
	}


	public String getStarthour() {
		return starthour;
	}


	public void setStarthour(String starthour) {
		this.starthour = starthour;
	}


	public String getStartmin() {
		return startmin;
	}


	public void setStartmin(String startmin) {
		this.startmin = startmin;
	}


	public String getEndhour() {
		return endhour;
	}


	public void setEndhour(String endhour) {
		this.endhour = endhour;
	}


	public String getEndmin() {
		return endmin;
	}


	public void setEndmin(String endmin) {
		this.endmin = endmin;
	}


	public Boolean getTimeBased() {
		return timeBased;
	}



	public void setTimeBased(Boolean timeBased) {
		this.timeBased = timeBased;
	}



	public Time getStarttime() {
		return starttime;
	}



	public void setStarttime(Time starttime) {
		this.starttime = starttime;
	}



	public Time getEndtime() {
		return endtime;
	}



	public void setEndtime(Time endtime) {
		this.endtime = endtime;
	}



	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Boolean getLocationBased() {
		return locationBased;
	}
	public void setLocationBased(Boolean locationBased) {
		this.locationBased = locationBased;
	}
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	public MultipartFile getImage() {
		return image;
	}
	public void setImage(MultipartFile image) {
		this.image = image;
	}
	public Integer getZoom() {
		return zoom;
	}
	public void setZoom(Integer zoom) {
		this.zoom = zoom;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
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

}
