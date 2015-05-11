package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
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
@Table(name = "t_itemalarm")
public class ItemAlarm implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 32)
    private String id;
	
	@Column(length = 32)
	private String unicode; 
	
	@ManyToOne(cascade = {CascadeType.ALL})
	private Item item;
	
	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="author_id")
	private Users author;
	
    @Column(name = "create_time", updatable = false)
	@Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
	private Date createTime;
    
	
    @Column(name = "update_time")
	@Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
	private Date updateTime;
    
    @Column
    private Integer alarmType;
    
	@Column(name = "lat")
	private Double lat;
	@Column(name = "lng")
	private Double lng;
	@Column(name = "speed")
	private Float speed;
	@Column
	private Integer feedback;

	
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUnicode() {
		return unicode;
	}

	public void setUnicode(String unicode) {
		this.unicode = unicode;
	}

	public Integer getFeedback() {
		return feedback;
	}

	public void setFeedback(Integer feedback) {
		this.feedback = feedback;
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

	public Float getSpeed() {
		return speed;
	}

	public void setSpeed(Float speed) {
		this.speed = speed;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Users getAuthor() {
		return author;
	}

	public void setAuthor(Users author) {
		this.author = author;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(Integer alarmType) {
		this.alarmType = alarmType;
	} 
}
