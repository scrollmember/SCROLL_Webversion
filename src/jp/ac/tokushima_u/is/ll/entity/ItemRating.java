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
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Houbin
 */
@Entity
@Table(name = "T_ITEMRATING")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ItemRating implements Serializable {

    private static final long serialVersionUID = -1516410869167891223L;
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 32)
    private String id;
    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private Users user;
    @Column(name = "rating")
    private Double rating;
    @Column(name = "create_time", updatable = false)
    @Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
