package jp.ac.tokushima_u.is.ll.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="T_MSG_TASK")
public class MsgTask {
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(length = 32)
	private String id;

	@ManyToOne
	private Users author;
	@Column(length = 500)
	private String params;
	@Column
	private Integer retryCount;
	@Column(length = 500)
	private String retryReason;
	@Column(length = 200)
	private String registerId;
	@Column(length = 200)
	private String collapseKey;
	@Column
	private Boolean delayWhileIdle;
	@Column(name = "create_time")
	@Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
	private Date createTime;
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
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public Integer getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}
	public String getRetryReason() {
		return retryReason;
	}
	public void setRetryReason(String retryReason) {
		this.retryReason = retryReason;
	}
	public String getRegisterId() {
		return registerId;
	}
	public void setRegisterId(String registerId) {
		this.registerId = registerId;
	}
	public String getCollapseKey() {
		return collapseKey;
	}
	public void setCollapseKey(String collapseKey) {
		this.collapseKey = collapseKey;
	}
	public Boolean getDelayWhileIdle() {
		return delayWhileIdle;
	}
	public void setDelayWhileIdle(Boolean delayWhileIdle) {
		this.delayWhileIdle = delayWhileIdle;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
