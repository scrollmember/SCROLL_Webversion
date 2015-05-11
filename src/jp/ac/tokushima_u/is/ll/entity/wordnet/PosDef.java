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
@Table(name = "wn_pos_def")
public class PosDef implements Serializable {
	private static final long serialVersionUID = 512561502746149079L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Column(length = 1)
	private String pos;
    @Column(length = 3)
    private String lang;// Language
    @Column(length = 10)
    private String def;
    
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
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
    
}
