package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author li
 */
@Entity
@Table(name = "t_sendtime")
public class SendTime implements Serializable {

    private static final long serialVersionUID = -8112574179277485749L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Users author;
    @Column
    private Integer typ;
    @Column
    private Time sendtime;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createDate;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getAuthor() {
        return author;
    }

    public void setAuthor(Users author) {
        this.author = author;
    }

    public Time getSendtime() {
        return sendtime;
    }

    public void setSendtime(Time sendtime) {
        this.sendtime = sendtime;
    }

    public Integer getTyp() {
        return typ;
    }

    public void setTyp(Integer typ) {
        this.typ = typ;
    }

    public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SendTime)) {
            return false;
        }
        SendTime other = (SendTime) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jp.ac.tokushima_u.is.ll.entity.SendTime[id=" + id + "]";
    }
}
