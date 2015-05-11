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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "T_NOTIFICATION")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Notification implements Serializable {
	private static final long serialVersionUID = 8661557689402136415L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(length = 32)
	private String id;
	@Column(length = 1024)
	private String Content;
	@Column(name = "start_date")
	@Temporal(value = javax.persistence.TemporalType.DATE)
	private Date startDate;
	@Column(name = "end_date")
	@Temporal(value = javax.persistence.TemporalType.DATE)
	private Date endDate;
	@Column(name = "create_time", updatable = false)
	@Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
	private Date createTime;
	@Column(name = "update_time")
	@Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
	private Date updateTime;
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "AUTHOR_ID")
	private Users author;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Users getAuthor() {
		return author;
	}

	public void setAuthor(Users author) {
		this.author = author;
	}

}
