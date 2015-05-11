/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
import java.sql.Time;
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
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author li
 */
@Entity
@Table(name="t_studytime")
public class StudyTime implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 32)
    private String id;

    @Column
    private Time starttime;
    
    @Column
    private Time endtime;

    @Column
    private Integer disabled;
    
    @ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name="author_id")
    private Users author;
    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createDate;
    

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Time getStarttime() {
		return starttime;
	}

	public void setStarttime(Time starttime) {
		this.starttime = starttime;
	}

	public Time getEndtime() {
		return endtime;
	}

	public void setEndtime(Time endtime) {
		this.endtime = endtime;
	}


	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	public Users getAuthor() {
		return author;
	}

	public void setAuthor(Users author) {
		this.author = author;
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
        if (!(object instanceof StudyTime)) {
            return false;
        }
        StudyTime other = (StudyTime) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jp.ac.tokushima_u.is.ll.entity.StudyTime[id=" + id + "]";
    }

}
