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

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "T_LANGUAGE_ABILITY")
public class LanguageAbility implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(length = 32)
	private String id;
	@ManyToOne (cascade=CascadeType.ALL)
	@JoinColumn(name="language_id")
	private Language language;
	
	@ManyToOne (cascade=CascadeType.ALL)
	@JoinColumn(name="author_id")
	private Users author;

	@Column
	private Double ability;
	
	@Column
	private Integer righttimes;
	@Column
	private Integer totaltimes;
	
	@Column(name = "disabled")
	private Integer disabled;
	
	@Column(name = "create_time", updatable = false)
	@Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
	private Date createTime;

	
	public Integer getRighttimes() {
		return righttimes;
	}

	public void setRighttimes(Integer righttimes) {
		this.righttimes = righttimes;
	}

	public Integer getTotaltimes() {
		return totaltimes;
	}

	public void setTotaltimes(Integer totaltimes) {
		this.totaltimes = totaltimes;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public Users getAuthor() {
		return author;
	}

	public void setAuthor(Users author) {
		this.author = author;
	}

	public Double getAbility() {
		return ability;
	}

	public void setAbility(Double ability) {
		this.ability = ability;
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
