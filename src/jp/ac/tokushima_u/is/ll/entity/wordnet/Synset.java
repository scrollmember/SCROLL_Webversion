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
@Table(name = "wn_synset")
public class Synset implements Serializable {
	private static final long serialVersionUID = 6883505536652090641L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@Column(length = 16)
	private String synset;
	@Column(length = 1)
	private String pos;
	@Column(length = 64)
	private String name;
	@Column(length = 8)
	private String src;
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
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	
	
}
