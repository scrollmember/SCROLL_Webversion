package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * Message from one user to another user
 * @author HOUBin
 *
 */
@Entity
@Table(name = "T_USER_MESSAGE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserMessage implements Serializable{

	private static final long serialVersionUID = 8292485003439780560L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(length = 32)
	private String id;
	
	@Column(name = "content", length = 1024)
	private String content;
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Users sendFrom;
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Users sendTo;
	
    @Column(name = "create_time", updatable = false)
    @Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
    private Date createTime;
    
    private boolean readFlag=false;

    @Column(name = "read_time", nullable=true)
    @Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
    private Date readTime;
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Users getSendFrom() {
		return sendFrom;
	}

	public void setSendFrom(Users sendFrom) {
		this.sendFrom = sendFrom;
	}

	public Users getSendTo() {
		return sendTo;
	}

	public void setSendTo(Users sendTo) {
		this.sendTo = sendTo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}

	public Date getReadTime() {
		return readTime;
	}

	public void setReadFlag(boolean readFlag) {
		this.readFlag = readFlag;
	}

	public boolean isReadFlag() {
		return readFlag;
	}
}
