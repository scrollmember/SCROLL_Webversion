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
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author lemonrain
 */
@Entity
@Table(name="T_ANSWER")
public class Answer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 32)
    private String id;
    @Column(name = "answer", length =300)

    private String answer;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Users author;
    
    @Column(name = "create_date")
    @Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
    private Date createDate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public Users getAuthor() {
        return author;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Question getQuestion() {
        return question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setAuthor(Users author) {
        this.author = author;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setQuestion(Question question) {
        this.question = question;
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
        if (!(object instanceof Answer)) {
            return false;
        }
        Answer other = (Answer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jp.ac.tokushima_u.is.ll.entity.Answer[id=" + id + "]";
    }

}
