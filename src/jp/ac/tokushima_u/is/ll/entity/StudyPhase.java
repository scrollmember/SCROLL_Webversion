package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "T_STUDY_PHASE")
public class StudyPhase implements Serializable {
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(length = 32)
	private String id;
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private Users user;
	@Column(name = "start_time", updatable = false)
	@Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
	private Date startTime;
	@Column(name = "end_time", updatable = false)
	@Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
	private Date endTime;
	@Column
	private Double minlat;
	@Column
	private Double minlng;
	@Column
	private Double maxlat;
	@Column
	private Double maxlng;
	@Column
	private Integer quiznum;
	@Column
	private Integer additemnum;
	@Column
	private Integer viewitemnum;
	
	@Column(name = "create_time", updatable = false)
	@Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
	private Date createTime;

	public StudyPhase(Date startTime, Date endTime, Double minlat,
			Double minlng, Double maxlat, Double maxlng, Integer quiznum,
			Integer additemnum, Integer viewitemnum, Users user) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.minlat = minlat;
		this.minlng = minlng;
		this.maxlat = maxlat;
		this.maxlng = maxlng;
		this.quiznum = quiznum;
		this.additemnum = additemnum;
		this.viewitemnum = viewitemnum;
		this.createTime = new Date();
		this.user = user;
	}

	public StudyPhase() {

	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getNum() {
		int n = 0;
		if(this.quiznum != null)
			n = n + this.quiznum;
		if(this.viewitemnum != null)
			n = n + this.viewitemnum;
		if(this.additemnum != null)
			n = n + this.additemnum;
		return n;
	}

	public Double getMinlat() {
		return minlat;
	}

	public void setMinlat(Double minlat) {
		this.minlat = minlat;
	}

	public Double getMinlng() {
		return minlng;
	}

	public void setMinlng(Double minlng) {
		this.minlng = minlng;
	}

	public Double getMaxlat() {
		return maxlat;
	}

	public void setMaxlat(Double maxlat) {
		this.maxlat = maxlat;
	}

	public Double getMaxlng() {
		return maxlng;
	}

	public void setMaxlng(Double maxlng) {
		this.maxlng = maxlng;
	}

	public Integer getQuiznum() {
		return quiznum;
	}

	public void setQuiznum(Integer quiznum) {
		this.quiznum = quiznum;
	}

	public Integer getAdditemnum() {
		return additemnum;
	}

	public void setAdditemnum(Integer additemnum) {
		this.additemnum = additemnum;
	}

	public Integer getViewitemnum() {
		return viewitemnum;
	}

	public void setViewitemnum(Integer viewitemnum) {
		this.viewitemnum = viewitemnum;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
