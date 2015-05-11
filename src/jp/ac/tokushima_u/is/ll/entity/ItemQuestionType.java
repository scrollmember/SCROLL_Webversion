package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "T_ITEM_QUESTION_TYPE")
public class ItemQuestionType  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 32)
    private String id;
    
    @ManyToOne
//    @Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	@JoinColumn(name = "ITEM_ID")
    private Item item;
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "QUESTIONTYPE_ID")
    private QuestionType questionType;
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "LANGUAGE_ID")
    private Language language;
	
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
	public QuestionType getQuestionType() {
		return questionType;
	}
	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}
}
