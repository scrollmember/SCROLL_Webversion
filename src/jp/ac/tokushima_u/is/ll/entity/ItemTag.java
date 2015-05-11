package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author houbin
 */
@Entity
@Table(name = "T_ITEM_TAG")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ItemTag implements Serializable {

    private static final long serialVersionUID = 2056775654731149484L;
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 32)
    private String id;
    @Column(name = "tag", length = 32)
    private String tag;
    @ManyToMany(mappedBy="itemTags")
    private Set<Item> items = new HashSet<Item>();
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

	public void setItems(Set<Item> items) {
		this.items = items;
	}

	public Set<Item> getItems() {
		return items;
	}
}
