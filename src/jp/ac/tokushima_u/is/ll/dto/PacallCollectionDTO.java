package jp.ac.tokushima_u.is.ll.dto;

import java.util.Date;

//import jp.ac.tokushima_u.is.ll.entity.PacallCollection;

public class PacallCollectionDTO {

	private static final long serialVersionUID = 9184651824993684096L;
	private Date startDate;
	private Date endDate;
	private Long total;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

}
