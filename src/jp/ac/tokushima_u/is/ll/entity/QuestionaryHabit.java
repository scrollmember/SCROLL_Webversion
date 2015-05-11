package jp.ac.tokushima_u.is.ll.entity;

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
@Table(name = "Q_LearningHabit")
public class QuestionaryHabit {
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(length = 32)
	private String id;

	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private Users user;
	@Column(name = "timescore")
	private Integer timescore;
	@Column(name = "geoscore")
	private Integer geoscore;
	@Column(name = "speedscore")
	private Integer speedscore;
	@Column(name = "timerecommend")
	private Integer timerecommend;
	@Column(name = "georecommend")
	private Integer georecommend;
	@Column(name = "create_time", updatable = false)
	@Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
	private Date createTime;

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Integer getTimescore() {
		return timescore;
	}

	public void setTimescore(Integer timescore) {
		this.timescore = timescore;
	}

	public Integer getGeoscore() {
		return geoscore;
	}

	public void setGeoscore(Integer geoscore) {
		this.geoscore = geoscore;
	}

	public Integer getSpeedscore() {
		return speedscore;
	}

	public void setSpeedscore(Integer speedscore) {
		this.speedscore = speedscore;
	}

	public Integer getTimerecommend() {
		return timerecommend;
	}

	public void setTimerecommend(Integer timerecommend) {
		this.timerecommend = timerecommend;
	}

	public Integer getGeorecommend() {
		return georecommend;
	}

	public void setGeorecommend(Integer georecommend) {
		this.georecommend = georecommend;
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
	
}
