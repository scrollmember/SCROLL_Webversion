package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author lemonrain
 */
@Entity
@Table(name = "T_MYQUIZ_CHOICE")
public class MyQuizChoice implements Serializable {

    private static final long serialVersionUID = 1432226372228624840L;
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 32)
    private String id;
    @Column(name = "content", length = 500)
    private String content;
    @Column(name = "number")
    private Integer number;

    @Column(length=500)
    private String note;
    
    @Column(length=500)
    private String lanCode;
    
    @ManyToOne
    @JoinColumn(name = "myquiz_id")
    private MyQuiz myquiz;
    
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    
    
	public String getLanCode() {
		return lanCode;
	}

	public void setLanCode(String lanCode) {
		this.lanCode = lanCode;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

	public MyQuiz getMyquiz() {
		return myquiz;
	}

	public void setMyquiz(MyQuiz myquiz) {
		this.myquiz = myquiz;
	}

}
