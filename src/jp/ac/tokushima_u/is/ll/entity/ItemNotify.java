package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "t_itemnotify")
public class ItemNotify implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 32)
    private String id;
	
	@ManyToMany(cascade = {CascadeType.ALL})
	private List<Item> items = new ArrayList<Item>();
	
	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="author_id")
	private Users author;
	
    @Column(name = "create_time", updatable = false)
	@Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
	private Date createTime;
    
    @Column
    private Integer feedback;
    
    @Column
    private Integer alarmType; 
    
    @Column(length=32)
	private String quizid;
    
    @Column
	private Integer notifyMode;
    
	public Integer getNotifyMode() {
		return notifyMode;
	}

	public void setNotifyMode(Integer notifyMode) {
		this.notifyMode = notifyMode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
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

	public Integer getFeedback() {
		return feedback;
	}

	public void setFeedback(Integer feedback) {
		this.feedback = feedback;
	}

	public Integer getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(Integer alarmType) {
		this.alarmType = alarmType;
	}

	public String getQuizid() {
		return quizid;
	}

	public void setQuizid(String quizid) {
		this.quizid = quizid;
	}
}
