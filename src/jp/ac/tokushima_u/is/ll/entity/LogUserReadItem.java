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

/**
 *
 * @author Houbin
 */
@Entity
@Table(name = "L_USER_READ_ITEM")
public class LogUserReadItem implements Serializable {

    private static final long serialVersionUID = 732284046230958259L;
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 32)
    private String id;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private Users user;
    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;
    @Column(name = "create_time", updatable = false)
    @Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
    private Date createTime;
    @Column
    private Double latitude;
    @Column
    private Double longitude;
	@Column(name = "speed")
	private Float speed;

	
    public Float getSpeed() {
		return speed;
	}

	public void setSpeed(Float speed) {
		this.speed = speed;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
