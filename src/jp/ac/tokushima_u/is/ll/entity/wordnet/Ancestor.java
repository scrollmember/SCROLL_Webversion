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
@Table(name = "wn_ancestor")
public class Ancestor implements Serializable {
	private static final long serialVersionUID = 8079817521504236391L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	@Column(length = 16)
	private String synset1;
	@Column(length = 16)
	private String synset2;
	@Column
	private Integer hops;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSynset1() {
		return synset1;
	}
	public void setSynset1(String synset1) {
		this.synset1 = synset1;
	}
	public String getSynset2() {
		return synset2;
	}
	public void setSynset2(String synset2) {
		this.synset2 = synset2;
	}
	public Integer getHops() {
		return hops;
	}
	public void setHops(Integer hops) {
		this.hops = hops;
	}
	
	
}
