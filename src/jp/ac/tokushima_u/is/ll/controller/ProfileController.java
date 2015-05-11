package jp.ac.tokushima_u.is.ll.controller;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.Groupmember;
import jp.ac.tokushima_u.is.ll.entity.Kasetting;
import jp.ac.tokushima_u.is.ll.entity.Language;
import jp.ac.tokushima_u.is.ll.entity.Profile;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.form.ProfileEditForm;
import jp.ac.tokushima_u.is.ll.form.validator.ProfileEditFormValidator;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.LanguageService;
import jp.ac.tokushima_u.is.ll.service.ProfileService;
import jp.ac.tokushima_u.is.ll.service.UserService;
import jp.ac.tokushima_u.is.ll.util.KeyGenerateUtil;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private LanguageService languageService;

    @ModelAttribute("langList")
    public List<Language> populateLanguageList() {
        return languageService.searchAllLanguage();
    }

    @RequestMapping
    public String index(ModelMap model) {
        Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
        model.addAttribute("user", user);


		// ■wakebe 次のレベルまでの経験値取得
		model.addAttribute("nextExperiencePoint", this.userService.getNextExperiencePoint(user.getId()));

		// ■wakebe 現在の合計経験値取得
		model.addAttribute("nowExperiencePoint", this.userService.getNowExperiencePoint(user.getId()));
		Kasetting ka = userService.getka(SecurityUserHolder.getCurrentUser().getId());
 		model.addAttribute("kadata",ka);
 		
 		List<Groupmember> groupmember = userService.getgroupmember(user.getId());
 		model.addAttribute("groupdata",groupmember);
 		List<Groupmember> groupname =userService.getgroupname(user.getId());
 		model.addAttribute("groupname",groupname);
 		
 		Profile userability =userService.findability(user.getId());
 		
		 model.addAttribute("ability",userability);

 		
        return "profile/index";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(@ModelAttribute("form") ProfileEditForm form, ModelMap model) {
        Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
        model.addAttribute("user", user);
        form = new ProfileEditForm(user);
        model.addAttribute("form", form);


		// ■wakebe 次のレベルまでの経験値取得
		model.addAttribute("nextExperiencePoint", this.userService.getNextExperiencePoint(user.getId()));

		// ■wakebe 現在の合計経験値取得
		model.addAttribute("nowExperiencePoint", this.userService.getNowExperiencePoint(user.getId()));
		Profile userability =userService.findability(user.getId());
	 		
 		 model.addAttribute("ability",userability);

        return "profile/edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String update(@ModelAttribute("form") ProfileEditForm form, BindingResult result, ModelMap model) {
        Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
       
        
      
        model.addAttribute("user", user);
        new ProfileEditFormValidator().validate(form, result);
        if(!StringUtils.isBlank(form.getOldpassword())){
	        if(userService.validateUser(user.getPcEmail(),form.getOldpassword())==null){
	        	result.reject("oldpassword", "profileEditForm.oldpassword.error");
	        }
        }
        if (result.hasErrors()) {
            return "profile/edit";
        }
        profileService.editProfile(form);
        List<Profile> check_user =userService.checkid(user.getId());
        System.out.println(check_user.size());
        if(check_user.size()==0){
        	Profile p = new Profile();
        	p.setAge(form.getAge());
        	p.setGender(form.getGender());
        	p.setJ_level(form.getJ_level());
        	p.setNationality(form.getNationality());
        	p.setStay(form.getStay());
        	p.setUserid(user.getId());
        	p.setId(KeyGenerateUtil.generateIdUUID());
        	
        
        userService.getByability(p);
        }
        else {
        	Profile p = new Profile();
        	p.setAge(form.getAge());
        	p.setGender(form.getGender());
        	p.setJ_level(form.getJ_level());
        	p.setNationality(form.getNationality());
        	p.setStay(form.getStay());
        	p.setUserid(user.getId());
        	p.setId(KeyGenerateUtil.generateIdUUID());
        	
        	userService.updateability(p);
        	
        }
        
        return this.index(model);
    }

//    @RequestMapping(value = "/{id}/avatar", method = RequestMethod.GET)
//    public void avatar(@PathVariable String id, HttpServletResponse response) throws IOException {
//        Users user = this.userService.getById(id);
//        if (user == null) {
//            return;
//        }
//        FileData fileData = userService.getAvatarByUser(user.getId());
//        if (fileData == null) {
//            return;
//        }
//        OutputStream out = response.getOutputStream();
//        if(fileData.getFileBin()!=null)
//        	out.write(fileData.getFileBin().getBin());
//        out.flush();
//        out.close();
//    }

    @RequestMapping(value = "/avataredit", method = RequestMethod.GET)
    public String avatarEdit(ModelMap model) {
        Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
        model.addAttribute("user", user);
        return "profile/avataredit";
    }
    private static long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private static String[] ALLOW_TYPES = new String[]{
        "image/pjpeg",
        "image/jpeg",
        "image/x-png",
        "image/png",
        "image/gif"
    };

    @RequestMapping(value = "/avataredit", method = RequestMethod.POST)
    public String avatarUpload(@ModelAttribute("form") ProfileEditForm form, BindingResult result, ModelMap model) {
        Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
        model.addAttribute("user", user);

        MultipartFile file = form.getPhoto();
        if (file.isEmpty() || file.getSize() == 0) {
            result.rejectValue("photo", "form.photo.empty");
        }else if (file.getSize() > MAX_FILE_SIZE) {
            result.rejectValue("photo", "form.photo.fileSizeTooBig", new String[]{"5M"}, "Max file size is 5M!");
        }else if (!ArrayUtils.contains(ALLOW_TYPES, file.getContentType())) {
            result.rejectValue("photo", "form.photo.invalidFormat");
        }
        if (result.hasErrors()) {
            return "profile/avataredit";
        }
        try {
            this.profileService.uploadAvatar(file);
            user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "profile/avataredit";
    }

    @RequestMapping(value="/changepassword", method=RequestMethod.GET)
    public String changePassword(@ModelAttribute("form") ProfileEditForm form, ModelMap model){
        Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
        model.addAttribute("user", user);
        form = new ProfileEditForm(user);
        model.addAttribute("form", form);


		// ■wakebe 次のレベルまでの経験値取得
		model.addAttribute("nextExperiencePoint", this.userService.getNextExperiencePoint(user.getId()));

		// ■wakebe 現在の合計経験値取得
		model.addAttribute("nowExperiencePoint", this.userService.getNowExperiencePoint(user.getId()));


    	return "profile/changepassword";
    }

    @RequestMapping(value="/changepassword", method=RequestMethod.POST)
    public String changePasswordUpdate(@ModelAttribute("form") ProfileEditForm form, BindingResult result, ModelMap model){
        Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
        model.addAttribute("user", user);
        new ProfileEditFormValidator().validatePassword(form, result);
        if(!StringUtils.isBlank(form.getOldpassword())){
	        if(userService.validateUser(user.getPcEmail(),form.getOldpassword())==null){
	        	result.rejectValue("oldpassword", "profileEditForm.oldpassword.error");
	        }
        }


		// ■wakebe 次のレベルまでの経験値取得
		model.addAttribute("nextExperiencePoint", this.userService.getNextExperiencePoint(user.getId()));

		// ■wakebe 現在の合計経験値取得
		model.addAttribute("nowExperiencePoint", this.userService.getNowExperiencePoint(user.getId()));


        if (result.hasErrors()) {
            return "profile/changepassword";
        }
        profileService.editPassword(form);
    	return "redirect:/profile";
    }
}
