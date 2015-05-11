package jp.ac.tokushima_u.is.ll.form;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.Category;
import jp.ac.tokushima_u.is.ll.entity.Language;

/**
 * 
 * @author Vstar
 */
public class ItemSearchCondForm implements Serializable {

	private static final long serialVersionUID = 5560865875193026838L;
	private String title;
	private String lang;
	private String username;
	private String nickname;
	private String answeruser;
	private String answeruserId;
	private String qrcode;
	private String group;
	private Double x1;
	private Double y1;
	private Double x2;
	private Double y2;
	private Date dateFrom;
	private Date dateTo;
	private Date updateDate;
	private boolean mapenabled;
	private String tag;
	private Integer page;
	private String questionStatus;
	private String teacherConfirm;
	private boolean includeRelog;
	private List<Language> targetlang;
	private String userId;
	private String notuserId;
	private Boolean onlyForAuthor;

	private List<String> toAnswerQuesLangsCode;
	private List<Language> toAnswerQuesLangs;
	private List<String> toStudyQuesLangsCode;
	private List<Language> toStudyQuesLangs;
	private Boolean hasAnswers;
	private Integer imageLevel;
	private List<Category> categorySet; 
	private List<String> itemids; 
	private String itemid;
	private String place;

	
	public String getNotuserId() {
		return notuserId;
	}

	public void setNotuserId(String notuserId) {
		this.notuserId = notuserId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public List<Category> getCategorySet() {
		return categorySet;
	}

	public void setCategorySet(List<Category> categorySet) {
		this.categorySet = categorySet;
	}
	public List<String> getItemIds() {
		return itemids;
	}

	public void setItemIds(List<String> itemids) {
		this.itemids = itemids;
	}

	public Integer getImageLevel() {
		return imageLevel;
	}

	public void setImageLevel(Integer imageLevel) {
		this.imageLevel = imageLevel;
	}

	public List<Language> getTargetlang() {
		return targetlang;
	}

	public void setTargetlang(List<Language> targetlang) {
		this.targetlang = targetlang;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Double getX1() {
		return x1;
	}

	public void setX1(Double x1) {
		this.x1 = x1;
	}

	public Double getX2() {
		return x2;
	}

	public void setX2(Double x2) {
		this.x2 = x2;
	}

	public Double getY1() {
		return y1;
	}

	public void setY1(Double y1) {
		this.y1 = y1;
	}

	public Double getY2() {
		return y2;
	}

	public void setY2(Double y2) {
		this.y2 = y2;
	}

	public boolean isMapenabled() {
		return mapenabled;
	}

	public void setMapenabled(boolean mapenabled) {
		this.mapenabled = mapenabled;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getAnsweruser() {
		return answeruser;
	}

	public void setAnsweruser(String answeruser) {
		this.answeruser = answeruser;
	}

	public String getAnsweruserId() {
		return answeruserId;
	}

	public void setAnsweruserId(String answeruserId) {
		this.answeruserId = answeruserId;
	}

	public String getQuestionStatus() {
		return questionStatus;
	}

	public void setQuestionStatus(String questionStatus) {
		this.questionStatus = questionStatus;
	}

	public String getTeacherConfirm() {
		return teacherConfirm;
	}

	public void setTeacherConfirm(String teacherConfirm) {
		this.teacherConfirm = teacherConfirm;
	}

	public boolean isIncludeRelog() {
		return includeRelog;
	}

	public void setIncludeRelog(boolean includeRelog) {
		this.includeRelog = includeRelog;
	}

	/**
	 * 
	 * @param onlyForAuthor
	 *            : [true: search all the objects only for specified user,
	 *            false: search all the object only for other users except the
	 *            specified user, null: search all the objects]
	 */
	public void setOnlyForAuthor(Boolean onlyForAuthor) {
		this.onlyForAuthor = onlyForAuthor;
	}

	public Boolean getOnlyForAuthor() {
		return onlyForAuthor;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public List<Language> getToAnswerQuesLangs() {
		return toAnswerQuesLangs;
	}

	public void setToAnswerQuesLangs(List<Language> toAnswerQuesLangs) {
		this.toAnswerQuesLangs = toAnswerQuesLangs;
	}

	public List<Language> getToStudyQuesLangs() {
		return toStudyQuesLangs;
	}

	public void setToStudyQuesLangs(List<Language> toStudyQuesLangs) {
		this.toStudyQuesLangs = toStudyQuesLangs;
	}

	public List<String> getToAnswerQuesLangsCode() {
		return toAnswerQuesLangsCode;
	}

	public void setToAnswerQuesLangsCode(List<String> toAnswerQuesLangsCode) {
		this.toAnswerQuesLangsCode = toAnswerQuesLangsCode;
	}

	public List<String> getToStudyQuesLangsCode() {
		return toStudyQuesLangsCode;
	}

	public void setToStudyQuesLangsCode(List<String> toStudyQuesLangsCode) {
		this.toStudyQuesLangsCode = toStudyQuesLangsCode;
	}

	public Boolean getHasAnswers() {
		return hasAnswers;
	}

	public void setHasAnswers(Boolean hasAnswers) {
		this.hasAnswers = hasAnswers;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public String getQrcode() {
		return qrcode;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getItemid() {
		return itemid;
	}

	public void setItemid(String itemid) {
		this.itemid = itemid;
	}

}
