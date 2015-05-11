package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "T_CATEGORY")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Category implements Serializable {

	private static final long serialVersionUID = 7395483729271687885L;
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(length = 32)
	private String id;
	@Column(length = 32)
	private String name;
	@Column(length = 256)
	private String note; // 備考
	@ManyToOne
	private Category parent;
	@OneToMany(mappedBy = "parent", cascade={CascadeType.REMOVE})
	@OrderBy("name")
	private Set<Category> children;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	@JsonIgnore
	public Set<Category> getChildren() {
		return children;
	}

	public void setChildren(Set<Category> children) {
		this.children = children;
	}

}
