package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "T_ACCESS_LOCK")
public class AccessLock implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5468144261143526136L;
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(length = 32)
	private String id;

	@Column(length = 32)
	private String accesskey;
	@Column
	private Integer state;
	@Column
	@Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
	private Date update_time;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccesskey() {
		return accesskey;
	}
	public void setAccesskey(String accesskey) {
		this.accesskey = accesskey;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
}
