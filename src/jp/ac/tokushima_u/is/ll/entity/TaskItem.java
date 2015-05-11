package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="T_TASK_ITEM")
public class TaskItem implements Serializable {
    private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(length = 32)
	private String id;
	
	@ManyToOne
	@JoinColumn(name="task_id")
	private Task task;

	
	@ManyToOne
	@JoinColumn(name="item_id")
	private Item item;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public Task getTask() {
		return task;
	}


	public void setTask(Task task) {
		this.task = task;
	}


	public Item getItem() {
		return item;
	}


	public void setItem(Item item) {
		this.item = item;
	}

}
