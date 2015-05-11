package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "D_QUESTIONTYPE")
public class QuestionType implements Serializable {
    private static final long serialVersionUID = -7714583775370107525L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  
    @Column
    private Long id;
    @Column(length = 32)
    private String title;
    @Column(length=200)
    private String info;
    @Column
    private Integer orderby;
    @Column
    private Boolean checked;
    
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	public Integer getOrderby() {
		return orderby;
	}
	public void setOrderby(Integer orderby) {
		this.orderby = orderby;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
}
