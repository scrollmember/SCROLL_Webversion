package jp.ac.tokushima_u.is.ll.entity;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="T_TASK")
public class Task {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 32)
    private String id;
    
    @Column(length = 500)
	private String title;
    @Column
	private Double lat;
    @Column
	private Double lng;
	@Column(name = "zoom")
	private Integer zoom;
	@Column
	private Integer level;
	@Column
	private Boolean isPublished;

//	To Be deleted
//	@Column
//	private Boolean locationBased;
//    @Column(length = 200)
//	private String place;
	
//	@Column
//	private Time starttime;
//	@Column
//	private Time endtime;
	
    @ManyToOne
    @JoinColumn(name="author_id")
	private Users author;
    @ManyToOne
    @JoinColumn(name="language_id")
	private Language language;
	@Column(name = "create_time", updatable = false)
	@Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
	private Date createtime;
	@Column(name = "update_time")
	@Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
	private Date updatetime;
	@OneToMany(mappedBy = "task")
	private List<TaskScript> taskScripts = new ArrayList<TaskScript>();
	
	
	public Boolean getIsPublished() {
		return isPublished;
	}
	public void setIsPublished(Boolean isPublished) {
		this.isPublished = isPublished;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
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
	public Users getAuthor() {
		return author;
	}
	public void setAuthor(Users author) {
		this.author = author;
	}
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public List<TaskScript> getTaskScripts() {
		return taskScripts;
	}
	public void setTaskScripts(List<TaskScript> taskScripts) {
		this.taskScripts = taskScripts;
	}
	public Integer getZoom() {
		return zoom;
	}
	public void setZoom(Integer zoom) {
		this.zoom = zoom;
	}
	
}
