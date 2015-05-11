package jp.ac.tokushima_u.is.ll.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.FileData;
import jp.ac.tokushima_u.is.ll.entity.Interest;
import jp.ac.tokushima_u.is.ll.entity.Language;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.form.ProfileEditForm;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.util.FilenameUtil;
import jp.ac.tokushima_u.is.ll.util.TextUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author lemonrain
 */
@Service
public class ProfileService {

    @Autowired
    private LanguageService languageService;
    @Autowired
    private UserService userService;
    @Autowired
    private InterestService interestService;
    @Autowired
    private StaticServerService staticServerService;
    
    private HibernateDao<Users, String> usersDao;
    private HibernateDao<FileData, String> fileDataDao;
    
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        usersDao = new HibernateDao<Users, String>(sessionFactory, Users.class);
        fileDataDao = new HibernateDao<FileData, String>(sessionFactory, FileData.class);
    }

    @Transactional
    public void editProfile(ProfileEditForm form) {
        Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
        if (!user.getId().equals(form.getUserid())) {
            return;
        }
//        Users user = new Users();
        Date current = Calendar.getInstance().getTime();
        user.setNickname(form.getNickname());
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());

        List<Language> myLans = new ArrayList<Language>();
        for (String code : form.getMyLangs()) {
            if (StringUtils.isBlank(code)) {
                continue;
            }
            Language lang = languageService.findUniqueLangByCode(code);
            if (lang == null) {
                throw new RuntimeException("Language Not Exist(code:[" + code + "])");
            }
//            user.addToMyLangs(lang);
            myLans.add(lang);
        }
        user.setMyLangs(myLans);

        List<Language> studyLans = new ArrayList<Language>();

        for (String code : form.getStudyLangs()) {
            if (StringUtils.isBlank(code)) {
                continue;
            }
            Language lang = languageService.findUniqueLangByCode(code);
            if (lang == null) {
                throw new RuntimeException("Language Not Exist(code:[" + code + "])");
            }
            studyLans.add(lang);
        }
        user.setStudyLangs(studyLans);
        user.setUpdateTime(current);
        
        List<String> interestings = TextUtils.splitString(form.getInteresting());
        List<Interest> interests = new ArrayList<Interest>();
        for(String i: interestings){
        	interests.add(interestService.findOrAdd(i));
        }
        user.setInterests(interests);
        userService.save(user);
    }
    
    @Transactional
	public void editPassword(ProfileEditForm form) {
		Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
        if (!user.getId().equals(form.getUserid())) {
            return;
        }
        Date current = Calendar.getInstance().getTime();
        if (!StringUtils.isBlank(form.getPassword()) && !StringUtils.isBlank(form.getOldpassword()) && !StringUtils.isBlank(form.getPasswordConfirm()) && form.getPassword().equals(form.getPasswordConfirm())) {
            user.setPassword(form.getPassword());
        }
        user.setUpdateTime(current);
        userService.save(user);
	}

    @Transactional
    public void uploadAvatar(MultipartFile file) throws Exception {
        Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
        FileData avatar = new FileData();
        avatar.setOrigName(file.getOriginalFilename());
    	String fileType = "";
        if(!StringUtils.isBlank(file.getOriginalFilename())){
        	fileType = FilenameUtil.checkMediaType(file.getOriginalFilename());
        	avatar.setFileType(fileType);
        }
        avatar.setCreatedAt(new Date());
        avatar.setMd5(DigestUtils.md5Hex(file.getInputStream()));
        fileDataDao.save(avatar);
        staticServerService.uploadFile(avatar.getId(), StringUtils.lowerCase(FilenameUtils.getExtension(file.getOriginalFilename())), file.getBytes());
        if(user.getAvatar()!=null){
        	fileDataDao.delete(user.getAvatar());
        }
        user.setAvatar(avatar);
        usersDao.save(user);
    }
}
