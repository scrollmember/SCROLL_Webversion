package jp.ac.tokushima_u.is.ll.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.MessagingException;

import jp.ac.tokushima_u.is.ll.entity.Category;
import jp.ac.tokushima_u.is.ll.entity.Interest;
import jp.ac.tokushima_u.is.ll.entity.Language;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.entity.Users.UsersAuth;
import jp.ac.tokushima_u.is.ll.exception.NotFoundException;
import jp.ac.tokushima_u.is.ll.form.SignupForm;
import jp.ac.tokushima_u.is.ll.util.TextUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import freemarker.template.TemplateException;

/**
 *
 * @author houbin
 */
@Service
@Transactional
public class SignupService {

    @Autowired
    private MailService mailService;
    @Autowired
    private LanguageService languageService;
    @Autowired
    private UserService userService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private InterestService interestService;

    @Transactional
    public void registerNewUser(String email) {
        Users users = userService.findUniqueBy("pcEmail", email);
        if (users == null) {
            Date current = Calendar.getInstance().getTime();
            users = new Users();
            users.setPcEmail(email);
            users.setEnabled(false);
            users.setAccountNonLocked(false);
            users.setCreateTime(current);
            users.setUpdateTime(current);
            users.setPassword("");


            // ■wakebe レベルと経験値の初期化
            users.setExperiencePoint(0);
            users.setUserLevel(1);


            users.setAuth(UsersAuth.MEMBER);
            users.setActivecode(UUID.randomUUID().toString().replaceAll("-", ""));
            userService.save(users);
        } else {
//            users.setActivecode(UUID.randomUUID().toString().replaceAll("-", ""));
//            usersDao.save(users);
        }
        //Send mail
        sendActivationMail(email, users.getActivecode());
    }

    public void sendActivationMail(String sendTo, String activecode) {
        try {
            ModelMap model = new ModelMap();
            model.addAttribute("activationUrl", propertyService.getSystemUrl() + "/signup/" + activecode);
            model.addAttribute("siteUrl", propertyService.getSystemUrl());
            model.addAttribute("sendFrom", propertyService.getSystemMailAddress());
            mailService.sendSysMail(sendTo, "[Learning Log]Please activate your account", "activationMail", model);
        } catch (MessagingException ex) {
            ex.printStackTrace();
            Logger.getLogger(SignupService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(SignupService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TemplateException ex) {
            ex.printStackTrace();
            Logger.getLogger(SignupService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    public void addProfile(Users u, SignupForm signupForm) {
        Date current = Calendar.getInstance().getTime();
        Users user = userService.getById(u.getId());
        user.setNickname(signupForm.getNickname());
        user.setFirstName(signupForm.getFirstName());
        user.setLastName(signupForm.getLastName());
        user.setMyLangs(new ArrayList<Language>());
        for (String code : signupForm.getMyLangs()) {
            if (StringUtils.isBlank(code)) {
                continue;
            }
            Language lang = languageService.findUniqueLangByCode(code);
            if (lang == null) {
                //TODO
                throw new RuntimeException("Language Not Exist(code:[" + code + "])");
            }
            user.addToMyLangs(lang);
        }

        user.setStudyLangs(new ArrayList<Language>());
        for (String code : signupForm.getStudyLangs()) {
            if (StringUtils.isBlank(code)) {
                continue;
            }
            Language lang = languageService.findUniqueLangByCode(code);
            if (lang == null) {
                throw new RuntimeException("Language Not Exist(code:[" + code + "])");
            }
            user.addToStudyLangs(lang);
        }
        user.setPassword(signupForm.getPassword());
        user.setActivecode(UUID.randomUUID().toString().replaceAll("-", ""));
        user.setUpdateTime(current);
        user.setEnabled(true);
        user.setAccountNonLocked(true);

        List<Category> categoryList = user.getMyCategoryList();
        if(categoryList==null || categoryList.size()==0){
        	categoryList = new ArrayList<Category>();
        	categoryList.addAll(categoryService.findByName("Languages", null));
        	user.setMyCategoryList(categoryList);
        }

        List<String> interestings = TextUtils.splitString(signupForm.getInteresting());
        List<Interest> interests = new ArrayList<Interest>();
        for(String i: interestings){
        	interests.add(interestService.findOrAdd(i));
        }

        user.setInterests(interests);
        userService.save(user);
    }

    @Transactional(readOnly = true)
    public Users getUserByActivecode(String activecode) {
        return this.userService.findUniqueBy("activecode", activecode);
    }

	public void sendResetPasswordMail(String email) throws NotFoundException {
		Users user = userService.findByEmail(email);
		if(user==null)throw new NotFoundException("email");
		user.setActivecode(UUID.randomUUID().toString().replaceAll("-", ""));
		userService.save(user);
        try {
            ModelMap model = new ModelMap();
            model.addAttribute("activationUrl", propertyService.getSystemUrl() + "/signup/resetpassword/" + user.getActivecode());
            model.addAttribute("siteUrl", propertyService.getSystemUrl());
            model.addAttribute("sendFrom", propertyService.getSystemMailAddress());
            model.addAttribute("nickname", user.getNickname());
            mailService.sendSysMail(user.getPcEmail(), "Reset your Learning Log password", "resetpasswordMail", model);
        } catch (MessagingException ex) {
            ex.printStackTrace();
            Logger.getLogger(SignupService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(SignupService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TemplateException ex) {
            ex.printStackTrace();
            Logger.getLogger(SignupService.class.getName()).log(Level.SEVERE, null, ex);
        }
	}

	public void resetPassword(Users u, String password) {
		Users user = userService.getById(u.getId());
		user.setPassword(password);
		userService.save(user);
	}
}
