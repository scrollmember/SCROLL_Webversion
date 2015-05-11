package jp.ac.tokushima_u.is.ll.entity.wordnet;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author uosaki
 */
@Entity
@Table(name = "wn_word")
public class Word implements Serializable {
	private static final long serialVersionUID = -2350714043289310504L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long wordid;

    @Column(length = 3)
    private String lang;// Language
    @Column(length = 64)
    private String lemma;
    @Column(length = 32)
    private String pron;
    @Column(length = 1)
    private String pos;
	public Long getWordid() {
		return wordid;
	}
	public void setWordid(Long wordid) {
		this.wordid = wordid;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getLemma() {
		return lemma;
	}
	public void setLemma(String lemma) {
		this.lemma = lemma;
	}
	public String getPron() {
		return pron;
	}
	public void setPron(String pron) {
		this.pron = pron;
	}
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}

    
}
