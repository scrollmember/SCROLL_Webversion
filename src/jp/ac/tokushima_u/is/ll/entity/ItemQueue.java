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
@Table(name = "t_itemqueue")
public class ItemQueue implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 32)
    private String id;
    
	@ManyToOne (cascade=CascadeType.ALL)
	@JoinColumn(name = "AUTHOR_ID")
    private Users author;
    
	@ManyToOne (cascade=CascadeType.ALL)
	@JoinColumn(name = "ITEM_ID")
    private Item item;
    
//    @Column
//    private Integer courseid;
    
    
	@Column
    private Integer weight;
	
	@Column
    private Integer queuetype;
    
	@Column
    private Integer disabled;
    
    @Column(name = "create_time", updatable = false)
	@Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
	private Date createTime;
    
    @Column(name = "update_time")
	@Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
	private Date updateTime;
    
    @Column(name = "start_time")
	@Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
	private Date startTime;

    
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Integer getQueuetype() {
		return queuetype;
	}

	public void setQueuetype(Integer queuetype) {
		this.queuetype = queuetype;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Users getAuthor() {
		return author;
	}

	public void setAuthor(Users author) {
		this.author = author;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

//	public Integer getCourseid() {
//		return courseid;
//	}
//
//	public void setCourseid(Integer courseid) {
//		this.courseid = courseid;
//	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
