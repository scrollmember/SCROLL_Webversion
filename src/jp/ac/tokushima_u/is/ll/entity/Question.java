package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author lemonrain
 */
@Entity
@Table(name = "T_QUESTION")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 32)
    private String id;
    @Column(length = 300)
    private String content;
    @OneToOne(mappedBy = "question", cascade={})
    private Item item;
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @OrderBy("createDate")
    private Set<Answer> answerSet = new HashSet<Answer>();
    @ManyToOne(cascade = {})
    @JoinColumn(name = "language_id")
    private Language language;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<Answer> getAnswerSet() {
        return answerSet;
    }

    public String getContent() {
        return content;
    }

    public Item getItem() {
        return item;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setAnswerSet(Set<Answer> answerSet) {
        this.answerSet = answerSet;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
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
        if (!(object instanceof Question)) {
            return false;
        }
        Question other = (Question) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jp.ac.tokushima_u.is.ll.entity.Question[id=" + id + "]";
    }
}
