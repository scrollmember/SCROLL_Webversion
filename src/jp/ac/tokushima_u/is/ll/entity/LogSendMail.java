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
@Table(name = "L_SEND_MAIL")
public class LogSendMail implements Serializable {

	private static final long serialVersionUID = 9058086699929156605L;
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(length = 32)
	private String id;
	@Column(name = "send_id")
	private String sendId;
	@Column(name = "address")
	private String address;
	@Column(name = "send_time", updatable = false)
	@Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
	private Date sendTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSendId() {
		return sendId;
	}

	public void setSendId(String sendId) {
		this.sendId = sendId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

}
