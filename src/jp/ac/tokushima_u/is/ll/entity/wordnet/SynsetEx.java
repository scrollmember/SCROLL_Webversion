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
@Table(name = "wn_synset_ex")
public class SynsetEx implements Serializable {
	private static final long serialVersionUID = -8421875942987896453L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
	@Column(length = 16)
	private String synset;
    @Column(length = 3)
    private String lang;// Language
    @Column()
    private String def;
    @Column(length = 8)
    private String sid;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSynset() {
		return synset;
	}
	public void setSynset(String synset) {
		this.synset = synset;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getDef() {
		return def;
	}
	public void setDef(String def) {
		this.def = def;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}

    
}
