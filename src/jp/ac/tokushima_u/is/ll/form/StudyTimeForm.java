package jp.ac.tokushima_u.is.ll.form;

import jp.ac.tokushima_u.is.ll.entity.StudyTime;
import jp.ac.tokushima_u.is.ll.util.Utility;

public class StudyTimeForm {
	
	public StudyTimeForm(StudyTime time){
		this.starttime = Utility.getSeconds(time.getStarttime());
		this.endtime = Utility.getSeconds(time.getEndtime());
		this.author_id = time.getAuthor().getId();
		this.createDate = time.getCreateDate().getTime();
	}
	
	private Integer starttime;
	private Integer endtime;
	private String author_id;
	private Long createDate;

	public Integer getStarttime() {
		return starttime;
	}
	public void setStarttime(Integer starttime) {
		this.starttime = starttime;
	}
	public Integer getEndtime() {
		return endtime;
	}
	public void setEndtime(Integer endtime) {
		this.endtime = endtime;
	}
	public String getAuthor_id() {
		return author_id;
	}
	public void setAuthor_id(String author_id) {
		this.author_id = author_id;
	}
	public Long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}
}
