package jp.ac.tokushima_u.is.ll.controller;

import java.util.ArrayList;
import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.SendTime;
import jp.ac.tokushima_u.is.ll.entity.Setting;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.form.AutoQuizForm;
import jp.ac.tokushima_u.is.ll.service.AutoQuizSendService;
import jp.ac.tokushima_u.is.ll.service.SettingService;
import jp.ac.tokushima_u.is.ll.service.UserService;
import jp.ac.tokushima_u.is.ll.service.helper.HandSet;
import jp.ac.tokushima_u.is.ll.service.helper.QuizCondition;
import jp.ac.tokushima_u.is.ll.util.Constants;
import jp.ac.tokushima_u.is.ll.ws.service.model.QuizForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/autoquiz")
public class AutoQuizController {
	@Autowired
	private SettingService settingService;
	@Autowired
	private UserService userService;
	@Autowired
	private AutoQuizSendService autoQuizSendService;
	@RequestMapping(value = "/mailsystem" ,method = RequestMethod.GET)
	public String autoquiz(ModelMap model) {
		List<Setting> settinglist = this.settingService.findSetting(HandSet.CellMobile.getValue());

		List<QuizForm> quizforms = new ArrayList<QuizForm>();
		for (Setting setting: settinglist) {
			Users user = setting.getAuthor();
			List<SendTime> results = this.autoQuizSendService
					.findUserSendTime(user);
			 if (results != null && results.size() > 0) {
				QuizCondition quizCon = new QuizCondition();
				quizCon.setUser(user);
				quizCon.setImageLevel(Constants.MailLevel);
				quizCon.setAlarmtype(Constants.MailPushType);
				QuizForm quizform = this.createQuizForm(quizCon);
				quizforms.add(quizform);
			 }
		}

		model.addAttribute("quizforms", quizforms);
		return "autoquiz/index";
	}
	
	@RequestMapping(value = "/requestquiz" ,method = RequestMethod.POST)
	public String requestquiz(@ModelAttribute("autoquizform") AutoQuizForm form, ModelMap model){
		if(form!=null){
			Users user = this.userService.findByEmail(form.getEmail());
			if(user!=null){
				QuizCondition quizcon = new QuizCondition();
				quizcon.setUser(user);
				quizcon.setAlarmtype(form.getAlarmtype());
				quizcon.setImageLevel(form.getImagelevel());
//				if(form.getQuiztype()!=null)
//					quizcon.setQuiztypeid(form.getQuiztype());
				QuizForm quizform = this.createQuizForm(quizcon);
				model.put("quizform", quizform);
			}
		}
		return "autoquiz/index";
	}
	//TODO dameng
	@RequestMapping(value = "/checkquiz" ,method = RequestMethod.POST)
	public String checkquiz(@ModelAttribute("autoquizform") AutoQuizForm form, ModelMap model){
//		if(form!=null){
//			Users user = this.userService.findByMobileEmail(form.getEmail());
//			if(user==null)
//				user = this.userService.findByEmail(form.getEmail());
//			if(user!=null){
//				QuizCondition quizcon = new QuizCondition();
//				quizcon.setUser(user);
//				quizcon.setAnswer(form.getAnswer());
//				quizcon.setQuizid(form.getQuizid());
//				quizcon.setAlarmtype(form.getAlarmtype());
//				quizcon.setImageLevel(form.getImagelevel());
//				QuizForm quizform = this.llquizRemoteService.checkQuizAnswer(quizcon);
//				model.put("quizform", quizform);
//			}
//		}
		return "autoquiz/index";
	}
	
	//TODO dameng
	private QuizForm createQuizForm(QuizCondition quizcon) {
		QuizForm quizform = new QuizForm();
//		if (quizcon.getUser().getMobileEmail() != null
//				&& quizcon.getUser().getMobileEmail().length() > 0)
//			quizform.setMemail(quizcon.getUser().getMobileEmail());
//		else
//			quizform.setMemail(quizcon.getUser().getPcEmail());
//
//		LLQuizDTO quizdto = this.llquizService.findQuiz(quizcon);
//		if (quizdto != null && quizdto.getLlquiz()!=null) {
//			LLQuiz quiz = quizdto.getLlquiz();
//			quizform.setAnswer(Integer.valueOf(quiz.getAnswer()));
//			quizform.setId(quiz.getId());
//			List<MyQuizChoice> choices = quiz.getChoices();
//			if (choices != null && choices.size() >= 4) {
//				if (choices.get(0) != null) {
//					quizform.setChoice1(choices.get(0).getContent());
//					quizform.setNote1(choices.get(0).getNote());
//				}
//				if (choices.get(1) != null) {
//					quizform.setChoice2(choices.get(1).getContent());
//					quizform.setNote2(choices.get(1).getNote());
//				}
//				if (choices.get(2) != null) {
//					quizform.setChoice3(choices.get(2).getContent());
//					quizform.setNote3(choices.get(2).getNote());
//				}
//				if (choices.get(3) != null) {
//					quizform.setChoice4(choices.get(3).getContent());
//					quizform.setNote4(choices.get(3).getNote());
//				}
//
//			}
//			if (quiz.getItem().getImage() != null) {
////				byte[] photos = staticServerService.getImageFileById(quiz
////						.getItem().getImage().getId(), quizcon.getImageLevel());
////				if (photos != null) {
////					quizform.setPhoto(photos);
////					quizform.setPhotourl(staticServerService.getImageFileURL(quiz.getItem().getImage().getId(), quizcon.getImageLevel()));
////				}
//				quizform.setPhotourl(staticServerService.getImageFileURL(quiz.getItem().getImage().getId(), quizcon.getImageLevel()));
//			}
//			quizform.setContent(quiz.getQuizcontent());
//		} else {
//			quizform.setErrorCode(Constants.ErrorCode_No_Quiz);
//		}

		return quizform;
	}
}
