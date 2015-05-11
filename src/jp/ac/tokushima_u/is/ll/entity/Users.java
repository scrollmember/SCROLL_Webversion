package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.IndexColumn;

/**
 *
 * @author houbin
 */
@Entity
@Table(name = "T_USERS")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Users implements Serializable {

    public enum UsersAuth {

        MEMBER, ADMIN
    }
    private static final long serialVersionUID = 8594687786650062343L;
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 32)
    private String id;
    @Column(name = "pc_email", length = 128, unique = true)
    private String pcEmail;
    @Column(length = 40)
    private String password;
    private UsersAuth auth;
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;
    @Column(name = "account_not_locked", nullable = false)
    private Boolean accountNonLocked;
    @Column(name = "last_login")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastLogin;
    @Column(name = "create_time", updatable = false)
    @Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
    private Date createTime;
    @Column(name = "update_time")
    @Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
    private Date updateTime;
    @Column(name = "activecode", length = 32)
    private String activecode;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
    private List<Item> myItemList = new ArrayList<Item>();
    @OneToMany(mappedBy = "author")
    private List<Answer> answerList = new ArrayList<Answer>();
    //UserInfo
    @Column(length = 32)
    private String nickname;
    @Column(name = "first_name", length = 32)
    private String firstName;
    @Column(name = "last_name", length = 32)
    private String lastName;


    // ■wakebe レベルと経験値の追加
    @Column(name = "user_level")
    private Integer userLevel;
    @Column(name = "experience_point")
    private Integer experiencePoint;


    @ManyToMany(cascade = {CascadeType.ALL},  fetch=FetchType.LAZY)
    @IndexColumn(name="lang_order")
    private List<Language> myLangs = new ArrayList<Language>();
    @IndexColumn(name="lang_order")
    @ManyToMany(cascade = {CascadeType.ALL},  fetch=FetchType.LAZY)
    private List<Language> studyLangs = new ArrayList<Language>();

    @ManyToMany(
    		targetEntity=Interest.class,
    		cascade = {CascadeType.PERSIST, CascadeType.MERGE},
    		fetch=FetchType.LAZY)
    @JoinTable(
    		name="r_users_interest",
    		joinColumns={@JoinColumn(name="user_id")},
    		inverseJoinColumns={@JoinColumn(name="interest_id")}
    		)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OrderBy(value = "name asc")
    private List<Interest> interests;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private FileData avatar;
    @OneToMany(mappedBy = "author")
    private List<Mail> maillist = new ArrayList<Mail>();
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<SendTime> sendTimeList = new ArrayList<SendTime>();
    @OneToMany(mappedBy = "author")
    private List<AutoSender> autoSenderList = new ArrayList<AutoSender>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<ItemRating> itemRatingList;
    @OneToOne(mappedBy = "author", fetch=FetchType.LAZY)
    private Setting setting;
    @Column(name = "receive_weekly_notification")
    private Boolean receiveWeeklyNotification;

    @OneToMany(mappedBy="author", cascade={CascadeType.ALL})
    private List<MyQuiz> myquizList = new ArrayList<MyQuiz>();

    @ManyToMany
    private List<Category> myCategoryList = new ArrayList<Category>();

    @ManyToOne
    private Category defaultCategory;

    @OneToMany(mappedBy="author")
    private List<StudySpeed> speedList = new ArrayList<StudySpeed>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<ItemState> myItemStateList = new ArrayList<ItemState>();

    @JsonIgnore
	public List<MyQuiz> getMyquizList() {
		return myquizList;
	}

	public List<StudySpeed> getSpeedList() {
		return speedList;
	}

	public void setSpeedList(List<StudySpeed> speedList) {
		this.speedList = speedList;
	}

	public void setMyquizList(List<MyQuiz> myquizList) {
		this.myquizList = myquizList;
	}

	public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    @JsonIgnore
    public List<AutoSender> getAutoSenderList() {
        return autoSenderList;
    }

    public void setAutoSenderList(List<AutoSender> autoSenderList) {
        this.autoSenderList = autoSenderList;
    }

    @JsonIgnore
    public List<SendTime> getSendTimeList() {
        return sendTimeList;
    }

    public void setSendTimeList(List<SendTime> sendTimeList) {
        this.sendTimeList = sendTimeList;
    }

    @JsonIgnore
    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPcEmail() {
        return pcEmail;
    }

    public void setPcEmail(String pcEmail) {
        this.pcEmail = pcEmail;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UsersAuth getAuth() {
        return auth;
    }

    public void setAuth(UsersAuth auth) {
        this.auth = auth;
    }

    @JsonIgnore
    public List<Mail> getMaillist() {
        return maillist;
    }

    public void setMaillist(List<Mail> maillist) {
        this.maillist = maillist;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @JsonIgnore
    public List<Item> getMyItemList() {
        return myItemList;
    }

    public void setMyItemList(List<Item> myItemList) {
        this.myItemList = myItemList;
    }

    public void addToMyItemList(Item item) {
        item.setAuthor(this);
        this.getMyItemList().add(item);
    }

    public String getActivecode() {
        return activecode;
    }

    public void setActivecode(String activecode) {
        this.activecode = activecode;
    }

    public FileData getAvatar() {
        return avatar;
    }

    public void setAvatar(FileData avatar) {
        this.avatar = avatar;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    // ■wakebe 経験値とレベルの呼び出し
    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public Integer getExperiencePoint() {
        return experiencePoint;
    }

    public void setExperiencePoint(Integer experiencePoint) {
        this.experiencePoint = experiencePoint;
    }


    public List<Language> getMyLangs() {
        return myLangs;
    }

    public void setMyLangs(List<Language> myLangs) {
        this.myLangs = myLangs;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<Language> getStudyLangs() {
        return studyLangs;
    }

    public void setStudyLangs(List<Language> studyLangs) {
        this.studyLangs = studyLangs;
    }

    public void addToMyLangs(Language lang) {
        this.getMyLangs().add(lang);
    }

    public void addToStudyLangs(Language lang) {
        this.getStudyLangs().add(lang);
    }

    @JsonIgnore
    public Set<ItemRating> getItemRatingList() {
        return itemRatingList;
    }

    public void setItemRatingList(Set<ItemRating> itemRatingList) {
        this.itemRatingList = itemRatingList;
    }

	public void setAccountNonLocked(Boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public Boolean getAccountNonLocked() {
		return accountNonLocked;
	}

	public void setMyCategoryList(List<Category> myCategoryList) {
		this.myCategoryList = myCategoryList;
	}

	public List<Category> getMyCategoryList() {
		return myCategoryList;
	}

	public void setDefaultCategory(Category defaultCategory) {
		this.defaultCategory = defaultCategory;
	}

	public Category getDefaultCategory() {
		return defaultCategory;
	}

	public void setReceiveWeeklyNotification(Boolean receiveWeeklyNotification) {
		this.receiveWeeklyNotification = receiveWeeklyNotification;
	}

	public Boolean getReceiveWeeklyNotification() {
		return receiveWeeklyNotification;
	}

	public List<Interest> getInterests() {
		return interests;
	}

	public void setInterests(List<Interest> interests) {
		this.interests = interests;
	}

	public List<ItemState> getMyItemStateList() {
		return myItemStateList;
	}

	public void setMyItemStateList(List<ItemState> myItemStateList) {
		this.myItemStateList = myItemStateList;
	}
	
}
