package jp.ac.tokushima_u.is.ll.entity;

import java.sql.Time;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "T_TASK_SCRIPT")
public class TaskScript {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(length = 32)
	private String id;
	
	@Column(length = 4000)
	private String script;
	
	@Column
	private Integer num;
	
	@Column
	private Integer zoom;
	
	@Column
	private Double lat;
	
	@Column
	private Double lng;
	
	@Column
	private Boolean locationBased;
	
	@Column(length = 200)
	private String place;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private FileData image;
	
	@Column
	private Boolean timeBased;
	@Column
	private Time starttime;
	@Column
	private Time endtime;
	
	@ManyToOne
	@JoinColumn(name="task_id")
	private Task task;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Boolean getLocationBased() {
		return locationBased;
	}

	public void setLocationBased(Boolean locationBased) {
		this.locationBased = locationBased;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
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

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getZoom() {
		return zoom;
	}

	public void setZoom(Integer zoom) {
		this.zoom = zoom;
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

	public FileData getImage() {
		return image;
	}

	public void setImage(FileData image) {
		this.image = image;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
	
	
}
