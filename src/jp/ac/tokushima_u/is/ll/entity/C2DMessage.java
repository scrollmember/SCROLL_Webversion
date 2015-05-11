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
@Table(name = "T_MESSAGE")
public class C2DMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(length = 32)
	private String id;

	@ManyToOne()
	@JoinColumn(name = "author_id")
	private Users author;

	@Column(name="msg_type")
	private Integer messageType;
	
	@Column(length=100)
	private String collapse;
	
	@Column
	private Integer isDelayIdle;
	
	@Column(length=300)
	private String registerId;
	
	@Column(length=5000)
	private String params;
	
	@Column
	private Integer disabled;

	@Column(name = "create_time")
	@Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
	private Date createTime;
	
	@Column(name = "update_time")
	@Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
	private Date updateTime;

	public String getRegisterId() {
		return registerId;
	}

	public void setRegisterId(String registerId) {
		this.registerId = registerId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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

	public Integer getMessageType() {
		return messageType;
	}

	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
	}

	public String getCollapse() {
		return collapse;
	}

	public void setCollapse(String collapse) {
		this.collapse = collapse;
	}

	public Integer getIsDelayIdle() {
		return isDelayIdle;
	}

	public void setIsDelayIdle(Integer isDelayIdle) {
		this.isDelayIdle = isDelayIdle;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
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
