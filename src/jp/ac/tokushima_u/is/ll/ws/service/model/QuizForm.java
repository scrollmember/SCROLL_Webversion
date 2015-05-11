package jp.ac.tokushima_u.is.ll.ws.service.model;

import java.util.Date;

/**
 * 
 * @author li
 */
public class QuizForm implements java.io.Serializable {
	private static final long serialVersionUID = -6755466064082023340L;
	private String email;
	private String password;
	private String memail;

	private String id;
	private String itemId;
	private String languageId;
	private String answer;
	private Integer answerstate;
	private Integer youranswer;
	private Date createDate;
	private Integer errorCode;
	private String choice1;
	private String choice2;
	private String choice3;
	private String choice4;
	private String filename;
	private String content;
	private String photourl;

    private String note1;
    private String note2;
    private String note3;
    private String note4;
	private String comment;
	private Integer wrongtimes;
	private Double latitude;
	private Double longitude;
	private Boolean hasObjectAround;
	
	private Integer quiztypeid;
	private String imgServerUrl;
	
	
	public String getImgServerUrl() {
		return imgServerUrl;
	}

	public void setImgServerUrl(String imgServerUrl) {
		this.imgServerUrl = imgServerUrl;
	}

	public Integer getQuiztypeid() {
		return quiztypeid;
	}

	public void setQuiztypeid(Integer quiztypeid) {
		this.quiztypeid = quiztypeid;
	}

	public String getPhotourl() {
		return photourl;
	}

	public void setPhotourl(String photourl) {
		this.photourl = photourl;
	}

	public Boolean getHasObjectAround() {
		return hasObjectAround;
	}

	public void setHasObjectAround(Boolean hasObjectAround) {
		this.hasObjectAround = hasObjectAround;
	}

	public String getNote1() {
		return note1;
	}

	public void setNote1(String note1) {
		this.note1 = note1;
	}

	public String getNote2() {
		return note2;
	}

	public void setNote2(String note2) {
		this.note2 = note2;
	}

	public String getNote3() {
		return note3;
	}

	public void setNote3(String note3) {
		this.note3 = note3;
	}

	public String getNote4() {
		return note4;
	}

	public void setNote4(String note4) {
		this.note4 = note4;
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

	public Integer getWrongtimes() {
		return wrongtimes;
	}

	public void setWrongtimes(Integer wrongtimes) {
		this.wrongtimes = wrongtimes;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMemail() {
		return memail;
	}

	public void setMemail(String memail) {
		this.memail = memail;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getChoice1() {
		return choice1;
	}

	public void setChoice1(String choice1) {
		this.choice1 = choice1;
	}

	public String getChoice2() {
		return choice2;
	}

	public void setChoice2(String choice2) {
		this.choice2 = choice2;
	}

	public String getChoice3() {
		return choice3;
	}

	public void setChoice3(String choice3) {
		this.choice3 = choice3;
	}

	public String getChoice4() {
		return choice4;
	}

	public void setChoice4(String choice4) {
		this.choice4 = choice4;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getLanguageId() {
		return languageId;
	}

	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	public Integer getYouranswer() {
		return youranswer;
	}

	public void setYouranswer(Integer youranswer) {
		this.youranswer = youranswer;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

//	public byte[] getPhoto() {
//		return photo;
//	}
//
//	public void setPhoto(byte[] photo) {
//		this.photo = photo;
//	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
}
