package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * @author houbin
 */
@Entity
@Table(name = "T_ITEM")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Item implements Serializable {

	public enum ShareLevel {

		PUBLIC, PRIVATE
	}

	private static final long serialVersionUID = -3583581414285655593L;
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(length = 32)
	private String id;
	// @OneToMany(cascade = CascadeType.ALL, mappedBy = "item",
	// orphanRemoval=true, fetch = FetchType.EAGER)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "item", orphanRemoval = true)
	@OrderBy("language asc")
	private List<ItemTitle> titles;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "item", orphanRemoval = true)
	 private List<ALLPlace> placename;
	@Column(length = 1024)
	private String note;
	@Column(length = 128)
	private String barcode;
	@Column(length = 4096)
	private String qrcode;
	@Column(length = 4096)
	private String rfid;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private FileData image;
	@Column(length = 32)
	private String place;

	@Column(name = "item_lat")
	private Double itemLat;
	@Column(name = "item_lng")
	private Double itemLng;
	@Column(name = "speed")
	private Float speed;
	@Column(name = "item_zoom")
	private Integer itemZoom;
	@Column(name = "disabled")
	private Integer disabled;
	@Column(name="locationbased")
	private Boolean locationBased;
	@ManyToMany
	private Set<ItemTag> itemTags = new HashSet<ItemTag>();
	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
	private List<ItemComment> commentList = new ArrayList<ItemComment>();
	@Column(name = "create_time", updatable = false)
	@Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
	private Date createTime;
	@Column(name = "update_time")
	@Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
	private Date updateTime;
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "AUTHOR_ID")
	private Users author;
	private ShareLevel shareLevel;
	@Column(name = "rating")
	private Double rating;
	private Boolean teacherConfirm;
	
	@Column
    private Integer wrongtimes=0;
    @Column
    private Integer righttimes=0;
    @Column
    private Integer pass=0;
	
	@ManyToMany
	private List<Language> languageSet = new ArrayList<Language>();
	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
	private Set<ItemRating> itemRatingList;
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "relog_item")
	private Item relogItem;
	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH, CascadeType.REMOVE }, mappedBy = "relogItem")
	private Set<Item> relogedItems;
	@OneToMany(cascade = { CascadeType.REMOVE }, mappedBy = "item")
	private Set<LogUserReadItem> logUserReadItem;
	@ManyToOne
	private Category category;
	
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "item")
    private List<ItemState> itemStateList = new ArrayList<ItemState>();

//	@ManyToMany
//	private Set<QuestionType>questionTypes= new HashSet<QuestionType>();
	@OneToMany(mappedBy="item")
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})
	private List<ItemQuestionType> questionTypes = new ArrayList<ItemQuestionType>();
	
	/*-------------Quiz------------*/
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id")
	private Question question;
//	@OneToMany(mappedBy = "item", cascade = { CascadeType.PERSIST,
//			CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE })
//	private Set<LLQuiz> llQuizSet = new HashSet<LLQuiz>();
	private Boolean questionResolved;

	// @OneToMany(mappedBy = "item", cascade = { CascadeType.PERSIST,
	// CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE })

	/*-------------Quiz end------------*/
//	@OneToOne
//	@JoinColumn(name = "id")
//	    private List<ALLPlace> Place = new ArrayList<ALLPlace>();
	
	
	public Question getQuestion() {
		return question;
	}

	public Integer getWrongtimes() {
		return wrongtimes;
	}

	public void setWrongtimes(Integer wrongtimes) {
		this.wrongtimes = wrongtimes;
	}

	public Integer getRighttimes() {
		return righttimes;
	}

	public void setRighttimes(Integer righttimes) {
		this.righttimes = righttimes;
	}

	public Integer getPass() {
		return pass;
	}

	public void setPass(Integer pass) {
		this.pass = pass;
	}

	public Boolean getLocationBased() {
		return locationBased;
	}

	public void setLocationBased(Boolean locationBased) {
		this.locationBased = locationBased;
	}


	public List<ItemQuestionType> getQuestionTypes() {
		return questionTypes;
	}

	public void setQuestionTypes(List<ItemQuestionType> questionTypes) {
		this.questionTypes = questionTypes;
	}
//	public ALLPlace setAllPlace() {
//		return Place;
//	}
//
//	public void setAllPlace(ALLPlace place) {
//		this.Place = Place;
//	}
//	public List<ALLPlace> setPlace() {
//		return Place;
//	}
//
//	public void setPlace(List<ALLPlace> Place) {
//		this.Place = Place;
//	}
//	
//	@JsonIgnore
//	public Set<LLQuiz> getLlQuizSet() {
//		return llQuizSet;
//	}
//
//	public void setLlQuizSet(Set<LLQuiz> llQuizSet) {
//		this.llQuizSet = llQuizSet;
//	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public void addToCommentList(ItemComment comment) {
		this.getCommentList().add(comment);
		comment.setItem(this);
	}

	public String getDefaultTitle() {
		String defaultTitle = "";
		if (this.titles == null || this.titles.size() == 0)
			return defaultTitle;
		/*
		List<Language> myLangs = author.getMyLangs();
		for (Language lang : myLangs) {
			for (ItemTitle title : this.titles) {
				if (lang.equals(title.getLanguage()))
					return title.getContent();
				else
					defaultTitle = title.getContent();
			}
		}
		List<Language> studyLangs = author.getStudyLangs();
		for (Language lang : studyLangs) {
			for (ItemTitle title : this.titles) {
				if (lang.equals(title.getLanguage()))
					return title.getContent();
				else
					defaultTitle = title.getContent();
			}
		}*/
		List<String> titleList = new ArrayList<String>();
		for(ItemTitle title:this.titles){
			titleList.add(title.getContent());
		}
		return StringUtils.join(titleList, "|");
	}

	public String getTitleByCode(String code) {
		if(code==null)return "";
		for(ItemTitle title: this.titles){
			if(code.equals(title.getLanguage().getCode())){
				return title.getContent();
			}
		}
		return "";
	}

	public String getTitleByLanList(List<Language> studylanguages,
			List<Language> mylanguages) {
		if (studylanguages != null && studylanguages.size() > 0) {
			Iterator<Language> iter = studylanguages.iterator();
			while (iter.hasNext()) {
				Language lan = iter.next();
				if (mylanguages.contains(lan)) {
					continue;
				}
				String title = this.getTitleByCode(lan.getCode());
				if (StringUtils.isBlank(title)) {
					continue;
				} else {
					return title;
				}
			}
		}
		return "";
	}

	public Float getSpeed() {
		return speed;
	}

	public void setSpeed(Float speed) {
		this.speed = speed;
	}

	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	public FileData getImage() {
		return image;
	}

	public void setImage(FileData image) {
		this.image = image;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

	public Set<ItemTag> getItemTags() {
		return itemTags;
	}

	public void setItemTags(Set<ItemTag> itemTags) {
		this.itemTags = itemTags;
	}

	@JsonIgnore
	public List<ItemComment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<ItemComment> commentList) {
		this.commentList = commentList;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}


	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Users getAuthor() {
		return author;
	}

	public void setAuthor(Users author) {
		this.author = author;
	}

	public Integer getItemZoom() {
		return itemZoom;
	}

	public void setItemZoom(Integer itemZoom) {
		this.itemZoom = itemZoom;
	}

	public List<Language> getLanguageSet() {
		return languageSet;
	}

	public void setLanguageSet(List<Language> languageSet) {
		this.languageSet = languageSet;
	}

	public ShareLevel getShareLevel() {
		return shareLevel;
	}

	public void setShareLevel(ShareLevel shareLevel) {
		this.shareLevel = shareLevel;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	@JsonIgnore
	public Set<ItemRating> getItemRatingList() {
		return itemRatingList;
	}

	public void setItemRatingList(Set<ItemRating> itemRatingList) {
		this.itemRatingList = itemRatingList;
	}

	public Item getRelogItem() {
		return relogItem;
	}

	public void setRelogItem(Item relogItem) {
		this.relogItem = relogItem;
	}

	public Boolean getQuestionResolved() {
		return questionResolved;
	}

	public void setQuestionResolved(Boolean questionResolved) {
		this.questionResolved = questionResolved;
	}

	public Boolean getTeacherConfirm() {
		return teacherConfirm;
	}

	public void setTeacherConfirm(Boolean teacherConfirm) {
		this.teacherConfirm = teacherConfirm;
	}

	@JsonIgnore
	public void setLogUserReadItem(Set<LogUserReadItem> logUserReadItem) {
		this.logUserReadItem = logUserReadItem;
	}

	public Set<LogUserReadItem> getLogUserReadItem() {
		return logUserReadItem;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Item)) {
			return false;
		}
		Item other = (Item) object;
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Category getCategory() {
		return category;
	}

	public void setRelogedItems(Set<Item> relogedItems) {
		this.relogedItems = relogedItems;
	}

	public Set<Item> getRelogedItems() {
		return relogedItems;
	}

	public List<ItemTitle> getTitles() {
		return titles;
	}

	public void setTitles(List<ItemTitle> titles) {
		this.titles = titles;
	}

	public List<ItemState> getItemStateList() {
		return itemStateList;
	}

	public void setItemStateList(List<ItemState> itemStateList) {
		this.itemStateList = itemStateList;
	}

	public List<ALLPlace> getPlacename() {
		return placename;
	}

	public void setPlacename(List<ALLPlace> placename) {
		this.placename = placename;
	}
}
