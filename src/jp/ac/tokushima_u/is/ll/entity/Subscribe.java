/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author lemonrain
 */
@Entity
@Table(name="M_SUBSCRIBE")
public class Subscribe implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long subscribeid;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="userid")
    private Users user;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="testtypeid")
    private TestType testtype;
    @Column
    private Integer level;
    @Column
    private Integer dynamiclevel;
    @Column
    private Integer questionnumber;
    @Column
    private Integer state;

    public Integer getDynamiclevel() {
        return dynamiclevel;
    }

    public void setDynamiclevel(Integer dynamiclevel) {
        this.dynamiclevel = dynamiclevel;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getQuestionnumber() {
        return questionnumber;
    }

    public void setQuestionnumber(Integer questionnumber) {
        this.questionnumber = questionnumber;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getSubscribeid() {
        return subscribeid;
    }

    public void setSubscribeid(Long subscribeid) {
        this.subscribeid = subscribeid;
    }

    public TestType getTesttype() {
        return testtype;
    }

    public void setTesttype(TestType testtype) {
        this.testtype = testtype;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (subscribeid != null ? subscribeid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Subscribe)) {
            return false;
        }
        Subscribe other = (Subscribe) object;
        if ((this.subscribeid == null && other.subscribeid != null) || (this.subscribeid != null && !this.subscribeid.equals(other.subscribeid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jp.ac.tokushima_u.is.ll.entity.Subscribe[id=" + subscribeid + "]";
    }

}
