package jp.ac.tokushima_u.is.ll.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;
@Entity
@Table(name = "T_PLACE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ALLPlace implements Serializable{
	private static final long serialVersionUID = -3583581414285655593L;
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(length = 128)
	private String id;
	
//	@Column(name = "itemid",length = 128)
//	private String itemid;
	
	@Column(name = "place",length = 128)
	private String place;
	

//
	@ManyToOne
	@JoinColumn(name = "itemid")
    private Item item;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
//	public String getItemid() {
//		return itemid;
//	}
//
//	public void setItemid(String itemid) {
//		this.itemid = itemid;
//	}
	
	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}
	

	
	


}
