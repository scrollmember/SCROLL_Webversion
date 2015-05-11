package jp.ac.tokushima_u.is.ll.quiz;

import java.util.Date;

import jp.ac.tokushima_u.is.ll.ws.service.model.ItemForm;

public class QuizWrapper implements java.io.Serializable {
	private static final long serialVersionUID = -6755466064082023340L;
	private String quizid;
	private String answer;
	private Integer answerstate;
	private Integer youranswer;
	private Date createDate;
	private Integer errorCode;
	private String[] choices;
	private String content;
    private String[] notes;
	private Double latitude;
	private Double longitude;
	private String imgServerUrl;
	private Long questionTypeId;
	private ItemForm itemform;
	
	
	
	public ItemForm getItemform() {
		return itemform;
	}
	public void setItemform(ItemForm itemform) {
		this.itemform = itemform;
	}
	public Long getQuestionTypeId() {
		return questionTypeId;
	}
	public void setQuestionTypeId(Long questionTypeId) {
		this.questionTypeId = questionTypeId;
	}
	public String getQuizid() {
		return quizid;
	}
	public void setQuizid(String quizid) {
		this.quizid = quizid;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public Integer getAnswerstate() {
		return answerstate;
	}
	public void setAnswerstate(Integer answerstate) {
		this.answerstate = answerstate;
	}
	public Integer getYouranswer() {
		return youranswer;
	}
	public void setYouranswer(Integer youranswer) {
		this.youranswer = youranswer;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public String[] getChoices() {
		return choices;
	}
	public void setChoices(String[] choices) {
		this.choices = choices;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String[] getNotes() {
		return notes;
	}
	public void setNotes(String[] notes) {
		this.notes = notes;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public String getImgServerUrl() {
		return imgServerUrl;
	}
	public void setImgServerUrl(String imgServerUrl) {
		this.imgServerUrl = imgServerUrl;
	}
}
