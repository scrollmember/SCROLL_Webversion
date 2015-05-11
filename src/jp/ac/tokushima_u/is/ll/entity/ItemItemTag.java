package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * @author mouri
 */

//@Entity
//@Table(name = "t_item_item_tags")
public class ItemItemTag implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3630425322932724912L;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
		
	@Column(length = 32)
	private String items;
	@Column(length = 32)
	private String item_tags;
	
	public void setItems(String items) {
		this.items = items;
	}
	public String getItems() {
		return items;
	}
	public void setItem_tags(String item_tags) {
		this.item_tags = item_tags;
	}
	public String getItem_tags() {
		return item_tags;
	}

	
	
}
