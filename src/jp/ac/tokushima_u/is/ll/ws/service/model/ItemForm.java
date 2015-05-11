package jp.ac.tokushima_u.is.ll.ws.service.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.ac.tokushima_u.is.ll.entity.Answer;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.ItemComment;
import jp.ac.tokushima_u.is.ll.entity.ItemTag;
import jp.ac.tokushima_u.is.ll.entity.ItemTitle;
import jp.ac.tokushima_u.is.ll.entity.Question;
import jp.ac.tokushima_u.is.ll.form.QuestionAnswerForm;

/**
 *
 * @author lemonrain
 */
public class ItemForm implements Serializable {
    private static final long serialVersionUID = -1534380200427552924L;
    private String userid;
    private String itemId;
    private String tag;
    private String tagLang;
    private String barcode;
    private String qrcode;
    private String rfid;
    private String note;
    private String place;
    private Double itemLat;
    private Double itemLng;
	private String photourl;
	private String file_type;
    private String nickname;
    private String category;
    private Integer disabled;
    private String questionId;
    private String question;
    private String quesLanCode;
    private List<QuestionAnswerForm> answers;
    private Map<String,String>tags = new HashMap<String,String>();
    private Map<String,String>comments = new HashMap<String,String>();
    private Long updatetime;
    private Map<String,String>titles = new HashMap<String,String>();
    
    public ItemForm(){
    }
    
    public ItemForm(Item item){
    	this.convertFromItem(item);
    }
    
    public void convertFromItem(Item item){
    	this.setItemId(item.getId());
    	if(item.getBarcode()!=null)
    		this.setBarcode(item.getBarcode());
    	this.setItemLat(item.getItemLat());
    	this.setItemLng(item.getItemLng());
    	if(item.getImage()!=null){
    		this.file_type = item.getImage().getFileType();
    		this.setPhotourl(item.getImage().getId());
    	}	
    	if(item.getAuthor()!=null){
    		this.setUserid(item.getAuthor().getId());
    		this.setNickname(item.getAuthor().getNickname());
    	}
    	this.setPlace(item.getPlace());
    	this.setQrcode(item.getQrcode());
    	this.setRfid(item.getRfid());
    	this.setUpdatetime(item.getUpdateTime().getTime());
    	Set<ItemTag> itemtags = item.getItemTags();
    	for(ItemTag tag:itemtags){
    		tags.put(tag.getId(), tag.getTag());
    	}
     	List<ItemComment> itemcomments = item.getCommentList();
    	for(ItemComment comment:itemcomments){
    		if(comment!=null&&comment.getUser()!=null&&comment.getUser().getNickname()!=null)
    			comments.put(comment.getComment(), comment.getUser().getNickname());
    	}
    	List<ItemTitle> itemTitles = item.getTitles();
    	for(ItemTitle title:itemTitles){
    		titles.put(title.getLanguage().getId(), title.getContent());
    	}
    	if(item.getNote()!=null)
    		this.note= item.getNote();
    	if(item.getCategory()!=null)
    		this.category = item.getCategory().getId();
    	if(item.getDisabled()!=null)
    		this.disabled = item.getDisabled();
    	
    	try{
    		if(item.getQuestion()!=null){
    			Question ques =  item.getQuestion();
    			this.questionId = ques.getId();
    			this.question = ques.getContent();
    			if(ques.getLanguage()!=null&&ques.getLanguage().getCode()!=null){
    				this.quesLanCode = ques.getLanguage().getCode();
    				if(ques.getAnswerSet()!=null&&ques.getAnswerSet().size()>0){
    					this.answers = new ArrayList<QuestionAnswerForm>();
    					for(Answer answer:ques.getAnswerSet()){
    						this.answers.add(new QuestionAnswerForm(answer));
    					}
    				}
    			}
    		}
    	}catch(Exception e){
    		
    	}
    	
    }

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getQuesLanCode() {
		return quesLanCode;
	}

	public void setQuesLanCode(String quesLanCode) {
		this.quesLanCode = quesLanCode;
	}

	public List<QuestionAnswerForm> getAnswers() {
		return answers;
	}

	public void setAnswers(List<QuestionAnswerForm> answers) {
		this.answers = answers;
	}

	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Map<String, String> getTitles() {
		return titles;
	}

	public String getFile_type() {
		return file_type;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	public void setTitles(Map<String, String> titles) {
		this.titles = titles;
	}



	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTagLang() {
		return tagLang;
	}

	public void setTagLang(String tagLang) {
		this.tagLang = tagLang;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public String getRfid() {
		return rfid;
	}

	public void setRfid(String rfid) {
		this.rfid = rfid;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public Double getItemLat() {
		return itemLat;
	}

	public void setItemLat(Double itemLat) {
		this.itemLat = itemLat;
	}

	public Double getItemLng() {
		return itemLng;
	}

	public void setItemLng(Double itemLng) {
		this.itemLng = itemLng;
	}

	public String getPhotourl() {
		return photourl;
	}

	public void setPhotourl(String photourl) {
		this.photourl = photourl;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Map<String, String> getTags() {
		return tags;
	}

	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}

	public Map<String, String> getComments() {
		return comments;
	}

	public void setComments(Map<String, String> comments) {
		this.comments = comments;
	}

	public Long getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Long updatetime) {
		this.updatetime = updatetime;
	}
}
