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
@Table(name = "wn_variant")
public class Variant implements Serializable {
	private static final long serialVersionUID = -2982206180200217327L;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long varid;
	
	@Column
	private Long wordid;
    @Column(length = 3)
    private String lang;// Language
    @Column(length = 64)
    private String lemma;
    @Column(length = 32)
    private String vartype;
	public Long getVarid() {
		return varid;
	}
	public void setVarid(Long varid) {
		this.varid = varid;
	}
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
	public String getVartype() {
		return vartype;
	}
	public void setVartype(String vartype) {
		this.vartype = vartype;
	}
    
    

}
