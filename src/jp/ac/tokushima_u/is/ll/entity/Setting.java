/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author li
 */
@Entity
@Table(name="t_setting")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Setting implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 32)
    private String id;

    @Column
    private Integer handsetcd;

    @Column
    private Integer wrongdays;
    
    @Column
    private Integer rightdays;
    
    @Column
    private Integer adddays;
    
    @Column
    private Boolean mylog;
    
    @OneToOne()
    @JoinColumn(name="author_id")
    private Users author;

    
    public Boolean getMylog() {
		return mylog;
	}

	public void setMylog(Boolean mylog) {
		this.mylog = mylog;
	}

	public Users getAuthor() {
        return author;
    }

    public void setAuthor(Users author) {
        this.author = author;
    }

    public Integer getHandsetcd() {
        return handsetcd;
    }

    public void setHandsetcd(Integer handsetcd) {
        this.handsetcd = handsetcd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public Integer getWrongdays() {
		return wrongdays;
	}

	public void setWrongdays(Integer wrongdays) {
		this.wrongdays = wrongdays;
	}

	public Integer getRightdays() {
		return rightdays;
	}

	public void setRightdays(Integer rightdays) {
		this.rightdays = rightdays;
	}

	public Integer getAdddays() {
		return adddays;
	}

	public void setAdddays(Integer adddays) {
		this.adddays = adddays;
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
        if (!(object instanceof Setting)) {
            return false;
        }
        Setting other = (Setting) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jp.ac.tokushima_u.is.ll.entity.Setting[id=" + id + "]";
    }

}
