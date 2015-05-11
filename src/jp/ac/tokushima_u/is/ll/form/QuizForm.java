package jp.ac.tokushima_u.is.ll.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.MyQuiz;
import jp.ac.tokushima_u.is.ll.entity.MyQuizChoice;
import jp.ac.tokushima_u.is.ll.util.Constants;
import jp.ac.tokushima_u.is.ll.ws.service.model.ItemForm;

public class QuizForm implements Serializable {

	private static final long serialVersionUID = 1L;
	private String quizid;
	private String itemid;
	private String authorid;
	private Long quiztype;
	private String content;
	private String answer;
    private Double quizLat;
    private Double quizLng;
	private Long createDate;
	private String filetype;
	private String photourl;
	private Integer weight;
	private String lanCode;
	private ItemForm itemform;
	private List<QuizChoiceForm>choices;
	private Integer disabled;

	public QuizForm(MyQuiz quiz, Integer weight){
		this.quizid = quiz.getId();
		this.authorid = quiz.getAuthor().getId();
		this.quiztype = quiz.getQuestionType().getId();
		this.content = quiz.getContent();
		this.answer = quiz.getAnswer();
		this.itemid = quiz.getItem().getId();
		this.createDate = quiz.getUpdateDate().getTime();
		if(quiz.getItem()!=null&&quiz.getItem().getImage()!=null){
			this.filetype = quiz.getItem().getImage().getFileType();
			this.photourl = quiz.getItem().getImage().getId();
		}
		
		this.lanCode = quiz.getLanCode();
		this.quizLat = quiz.getItem().getItemLat();
		this.quizLng = quiz.getItem().getItemLng();
		this.weight = weight;
		
		if(quiz.getQuizChoices()!=null){
			choices = new ArrayList<QuizChoiceForm>();
			for(MyQuizChoice quizchoice:quiz.getQuizChoices()){
				QuizChoiceForm choice = new QuizChoiceForm();
				choice.setChoiceid(quizchoice.getId());
				choice.setContent(quizchoice.getContent());
				if(quizchoice.getItem()!=null&&quizchoice.getItem().getImage()!=null)
					choice.setFiletype(quizchoice.getItem().getImage().getFileType());
				choice.setNote(quizchoice.getNote());
				if(quizchoice.getLanCode() != null && quizchoice.getLanCode().length()>0)
					choice.setLanCode(quizchoice.getLanCode());
				choice.setNumber(quizchoice.getNumber());
				choices.add(choice);
			}
		}
		
		if(quiz.getQuestionType().getId().equals(Constants.QuizTypeYesNoQuestion)){
			this.itemform = new ItemForm(quiz.getItem());
		}
		
	}
	public QuizForm(){
		
	}
	
	
	public String getLanCode() {
		return lanCode;
	}
	public void setLanCode(String lanCode) {
		this.lanCode = lanCode;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public Double getQuizLat() {
		return quizLat;
	}
	public void setQuizLat(Double quizLat) {
		this.quizLat = quizLat;
	}
	public Double getQuizLng() {
		return quizLng;
	}
	public void setQuizLng(Double quizLng) {
		this.quizLng = quizLng;
	}
	public Integer getDisabled() {
		return disabled;
	}
	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}
	public String getItemid() {
		return itemid;
	}
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	public ItemForm getItemform() {
		return itemform;
	}
	public void setItemform(ItemForm itemform) {
		this.itemform = itemform;
	}
	public Long getQuiztype() {
		return quiztype;
	}
	public void setQuiztype(Long quiztype) {
		this.quiztype = quiztype;
	}
	public List<QuizChoiceForm> getChoices() {
		return choices;
	}
	public void setChoices(List<QuizChoiceForm> choices) {
		this.choices = choices;
	}
	public String getQuizid() {
		return quizid;
	}
	public void setQuizid(String quizid) {
		this.quizid = quizid;
	}
	public String getAuthorid() {
		return authorid;
	}
	public void setAuthorid(String authorid) {
		this.authorid = authorid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}
	public String getFiletype() {
		return filetype;
	}
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}
	public String getPhotourl() {
		return photourl;
	}
	public void setPhotourl(String photourl) {
		this.photourl = photourl;
	}

}
