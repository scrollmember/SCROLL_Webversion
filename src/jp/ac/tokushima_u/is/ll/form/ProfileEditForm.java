package jp.ac.tokushima_u.is.ll.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.Interest;
import jp.ac.tokushima_u.is.ll.entity.Language;
import jp.ac.tokushima_u.is.ll.entity.Users;

import org.springframework.web.multipart.MultipartFile;
/**
 *
 * @author HOU Bin
 */
public class ProfileEditForm implements Serializable {
    private static final long serialVersionUID = -4944483509197934833L;

    private String nickname;
    private String firstName;
    private String lastName;
    private String interesting;
    private List<String> myLangs = new ArrayList<String>();
    private List<String> studyLangs = new ArrayList<String>();
    private String oldpassword;
    private String password;
    private String passwordConfirm;
    private String userid;
    private MultipartFile photo;
    private String age;
    private String gender;
    private String major;
    private String nationality;
    private String stay;
    private String j_level;


    public ProfileEditForm(){
    }

    public ProfileEditForm(Users user){
        this.nickname = user.getNickname();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        for(Language lan:user.getMyLangs()){
            this.myLangs.add(lan.getCode());
        }
        for(Language lan:user.getStudyLangs()){
            this.studyLangs.add(lan.getCode());
        }
      
        List<Interest> interests = user.getInterests();
        interesting = "";
        for(Interest i: interests){
        	interesting+=i.getName()+", ";
        }
        if(interesting.length()>1){
        	interesting=interesting.substring(0, interesting.length()-1);
        }
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getInteresting() {
        return interesting;
    }

    public void setInteresting(String interesting) {
        this.interesting = interesting;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public List<String> getMyLangs() {
        return myLangs;
    }

    public void setMyLangs(List<String> myLangs) {
        this.myLangs = myLangs;
    }

    public List<String> getStudyLangs() {
        return studyLangs;
    }

    public void setStudyLangs(List<String> studyLangs) {
        this.studyLangs = studyLangs;
    }

    public String getOldpassword() {
        return oldpassword;
    }

    public void setOldpassword(String oldpassword) {
        this.oldpassword = oldpassword;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getStay() {
		return stay;
	}

	public void setStay(String stay) {
		this.stay = stay;
	}

	public String getJ_level() {
		return j_level;
	}

	public void setJ_level(String j_level) {
		this.j_level = j_level;
	}


	
}
