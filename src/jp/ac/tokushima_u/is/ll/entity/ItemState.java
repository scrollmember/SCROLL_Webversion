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
@Table(name = "T_ITEMSTATE")
public class ItemState implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(length = 32)
	private String id;

	@ManyToOne
	@JoinColumn(name = "author_id")
	private Users user;
	@ManyToOne
	@JoinColumn(name = "item_id")
	private Item item;
	
	private Integer rememberState;
	
	private Integer experState;
	
	@Column(length=50)
	private String quizState;

	@Column(name = "create_date")
    @Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
    private Date createDate;
	
	@Column(name = "update_date")
    @Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
    private Date updateDate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public Integer getRememberState() {
		return rememberState;
	}
	public void setRememberState(Integer rememberState) {
		this.rememberState = rememberState;
	}
	public Integer getExperState() {
		return experState;
	}
	public void setExperState(Integer experState) {
		this.experState = experState;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getQuizState() {
		return quizState;
	}
	public void setQuizState(String quizState) {
		this.quizState = quizState;
	}
}
