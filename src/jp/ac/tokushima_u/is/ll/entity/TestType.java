/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author lemonrain
 */
@Entity
@Table(name="M_TESTTYPE")
public class TestType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer testtypeid;
    @Column(length=100)
    private String title;
    @Column(length=100)
    private String begin;
    @Column(length=100)
    private String end;
//    @Column
//    private Integer mailtype;
    @OneToMany(mappedBy="testtype")
    private List<MailSubject> mailsubjects;

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public List<MailSubject> getMailsubjects() {
        return mailsubjects;
    }

    public void setMailsubjects(List<MailSubject> mailsubjects) {
        this.mailsubjects = mailsubjects;
    }

//    public Integer getMailtype() {
//        return mailtype;
//    }
//
//    public void setMailtype(Integer mailtype) {
//        this.mailtype = mailtype;
//    }

    public Integer getTesttypeid() {
        return testtypeid;
    }

    public void setTesttypeid(Integer testtypeid) {
        this.testtypeid = testtypeid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (testtypeid != null ? testtypeid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TestType)) {
            return false;
        }
        TestType other = (TestType) object;
        if ((this.testtypeid == null && other.testtypeid != null) || (this.testtypeid != null && !this.testtypeid.equals(other.testtypeid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jp.ac.tokushima_u.is.ll.entity.TestType[id=" + testtypeid + "]";
    }

}
