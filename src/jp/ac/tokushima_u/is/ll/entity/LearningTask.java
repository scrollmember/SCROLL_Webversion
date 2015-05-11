/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author houbin
 */
@Entity
@Table(name = "T_LEARNING_TASK")
public class LearningTask implements Serializable {

    private static final long serialVersionUID = -3165888418479945907L;
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 32)
    private String id;
    @Column(name = "title", length = 32)
    private String title;
    @Column(name = "learn_place", length = 32)
    private String learnPlace;
    @Column(name = "learn_time")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date learnTime;
    @Column(name = "learn_object", length = 32)
    private String learnObject;
    @Column(name = "create_time", updatable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createTime;
    @Column(name = "update_time")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date updateTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLearnObject() {
        return learnObject;
    }

    public void setLearnObject(String learnObject) {
        this.learnObject = learnObject;
    }

    public String getLearnPlace() {
        return learnPlace;
    }

    public void setLearnPlace(String learnPlace) {
        this.learnPlace = learnPlace;
    }

    public Date getLearnTime() {
        return learnTime;
    }

    public void setLearnTime(Date learnTime) {
        this.learnTime = learnTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
