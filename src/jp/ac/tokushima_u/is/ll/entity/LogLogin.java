package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
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
@Table(name = "L_LOGIN")
public class LogLogin implements Serializable {

	private static final long serialVersionUID = 4524018797934251311L;

	public enum Device {
		MOBILE, WEB
	}

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 32)
    private String id;
	
	@ManyToOne
	private Users user;

	@Column(name = "login_time", updatable = false)
	@Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
	private Date loginTime;

	private Device LoginDevice;

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Device getLoginDevice() {
		return LoginDevice;
	}

	public void setLoginDevice(Device loginDevice) {
		LoginDevice = loginDevice;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

}
