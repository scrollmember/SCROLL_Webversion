package jp.ac.tokushima_u.is.ll.form;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author lemonrain
 */
public class QuizSearchCondForm implements Serializable {

	private static final long serialVersionUID = 5560865875193026838L;
	private Date createDate;
	private String quizid;
	private Integer quizsize;
	
	public Integer getQuizsize() {
		return quizsize;
	}
	public void setQuizsize(Integer quizsize) {
		this.quizsize = quizsize;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getQuizid() {
		return quizid;
	}
	public void setQuizid(String quizid) {
		this.quizid = quizid;
	}
}
