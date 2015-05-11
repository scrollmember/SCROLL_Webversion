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
@Table(name = "wn_xlink")
public class Xlink implements Serializable {
	private static final long serialVersionUID = 8691400889922462903L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
		
	@Column(length = 16)
	private String synset;
	@Column(length = 8)
	private String resource;
	@Column(length = 64)
	private String xref;
	@Column(length = 4)
	private String misc;
	@Column(length = 32)
	private String mconfidence;
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
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getXref() {
		return xref;
	}
	public void setXref(String xref) {
		this.xref = xref;
	}
	public String getMisc() {
		return misc;
	}
	public void setMisc(String misc) {
		this.misc = misc;
	}
	public String getMconfidence() {
		return mconfidence;
	}
	public void setMconfidence(String mconfidence) {
		this.mconfidence = mconfidence;
	}
	
	
}
