package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Taskcollaborative implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6744330347786261795L;

	private String id;
    
	private String title;
	private String place;
	private Double lat;
	private Double lng;
	private Integer zoom;
	private Integer level;
	private Boolean locationBased;
	private Boolean is_Published;
	private Time starttime;
	private Time endtime;
	private String author_id;
	private String language_id;
	private Date create_time;
	private Date update_time;
	
	 private String number;
	 private String time_limit;
	private List<TaskScript> taskScripts = new ArrayList<TaskScript>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
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
	public Integer getZoom() {
		return zoom;
	}
	public void setZoom(Integer zoom) {
		this.zoom = zoom;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Boolean getLocationBased() {
		return locationBased;
	}
	public void setLocationBased(Boolean locationBased) {
		this.locationBased = locationBased;
	}
	public Boolean getIsPublished() {
		return is_Published;
	}
	public void setIsPublished(Boolean isPublished) {
		this.is_Published = is_Published;
	}
	public String getAuthor_id() {
		return author_id;
	}
	public void setAuthor_id(String author_id) {
		this.author_id = author_id;
	}
	public String getLanguage_id() {
		return language_id;
	}
	public void setLanguage_id(String language_id) {
		this.language_id = language_id;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getTime_limit() {
		return time_limit;
	}
	public void setTime_limit(String time_limit) {
		this.time_limit = time_limit;
	}

	
}

