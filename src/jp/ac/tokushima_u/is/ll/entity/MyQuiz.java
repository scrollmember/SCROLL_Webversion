/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.annotations.GenericGenerator;


/**
 *
 * @author lemonrain
 */
@Entity
@Table(name="T_MYQUIZ")
public class MyQuiz implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 32)
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Users author;
    
//    @ManyToOne
//    @JoinColumn(name="llquiz_id")
//    private LLQuiz llquiz;
    @Column(length=1000)
    private String content;
    @Column(length=2000)
    private String quizContent;
    @Column(length=100)
    private String answer;
    @Column(length=100)
    private String myanswer;
    @ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name="questiontypeid")
    private QuestionType questionType;
    @ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name="ITEM_ID")
	private Item item;
    @ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name="LANGUAGE_ID")
    private Language language;
    
    @Column
    private Double latitude;
    @Column
    private Double longitude;
    @Column
    private Float speed;    
    @Column
    private Integer answerstate;
    
    @Column
    private Integer alarmtype; 
    
    // The language of the content
    @Column(length=500)
    private String lanCode;
    
    @Column
    @Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
    private Date createDate;
    
    @Column
    @Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
    private Date updateDate;
    
    @OneToMany(mappedBy="myquiz",cascade=CascadeType.ALL)
    private List<MyQuizChoice> quizChoices = new LinkedList<MyQuizChoice>();

    
	public String getLanCode() {
		return lanCode;
	}

	public void setLanCode(String lanCode) {
		this.lanCode = lanCode;
	}

	public String getQuizContent() {
		return quizContent;
	}

	public void setQuizContent(String quizContent) {
		this.quizContent = quizContent;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public List<MyQuizChoice> getQuizChoices() {
		return quizChoices;
	}

	public void setQuizChoices(List<MyQuizChoice> quizChoices) {
		this.quizChoices = quizChoices;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMyanswer() {
		return myanswer;
	}

	public void setMyanswer(String myanswer) {
		this.myanswer = myanswer;
	}

	public QuestionType getQuestionType() {
		return questionType;
	}

	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Users getAuthor() {
		return author;
	}

	public void setAuthor(Users author) {
		this.author = author;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Integer getAnswerstate() {
		return answerstate;
	}

	public void setAnswerstate(Integer answerstate) {
		this.answerstate = answerstate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Float getSpeed() {
		return speed;
	}

	public void setSpeed(Float speed) {
		this.speed = speed;
	}

	public Integer getAlarmtype() {
		return alarmtype;
	}

	public void setAlarmtype(Integer alarmtype) {
		this.alarmtype = alarmtype;
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
        if (!(object instanceof MyQuiz)) {
            return false;
        }
        MyQuiz other = (MyQuiz) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jp.ac.tokushima_u.is.ll.entity.MyQuiz[id=" + id + "]";
    }



}
