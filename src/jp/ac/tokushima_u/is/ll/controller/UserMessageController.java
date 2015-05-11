package jp.ac.tokushima_u.is.ll.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.entity.MymessageList;
import jp.ac.tokushima_u.is.ll.entity.UserMessage;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.UserMessageService;
import jp.ac.tokushima_u.is.ll.service.UserService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
@RequestMapping("/usermessage")
public class UserMessageController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserMessageService userMessageService;

	@RequestMapping
	public String index(ModelMap model){
		return list(model);
	}

	public String list(ModelMap model){

		Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
		List<UserMessage> messageList = userMessageService.searchAll(user.getId());
		model.addAttribute("user", user);
		model.addAttribute("messageList", messageList);
		List<MymessageList> mym= userMessageService.searchMymail(user.getId());
		
		// ■wakebe 次のレベルまでの経験値取得
		model.addAttribute("nextExperiencePoint", this.userService.getNextExperiencePoint(user.getId()));

		// ■wakebe 現在の合計経験値取得
		model.addAttribute("nowExperiencePoint", this.userService.getNowExperiencePoint(user.getId()));
		
		//mysendlist
		model.addAttribute("mysendlist",mym);
		return "usermessage/list";
	}

	@RequestMapping("/send")
	@ResponseBody
	public String send(String umsg_sendto, String content){
		if(StringUtils.isBlank(umsg_sendto)){
			return "umsg_sendto_empty";
		}
		if(StringUtils.isBlank(content)){
			return "umsg_content_empty";
		}
		String sendTo = umsg_sendto;
		Users sendToUser = userService.getById(sendTo);
		if(sendToUser == null){
			return "umsg_sendto_not_exist";
		}
		content = content.trim();
		if(content.length()>512){
			return "umsg_content_too_long";
		}
		Users sendFromUser = SecurityUserHolder.getCurrentUser();
		userMessageService.send(sendFromUser.getId(), sendToUser.getId(), content);

		return "success";
	}

	@RequestMapping("/checkmessage")
	@ResponseBody
	public String checkMessage(){
		Users sendFromUser = SecurityUserHolder.getCurrentUser();
		return userMessageService.checkMessage(sendFromUser.getId());
	}

	@RequestMapping("/readmessage")
	@ResponseBody
	public String readMessage(String msgId){
		if(StringUtils.isBlank(msgId)){
			return "umsg_msgid_empty";
		}
		UserMessage msg = userMessageService.getById(msgId);
		if(msg==null){
			return "umsg_msgid_exist";
		}
		if(!msg.isReadFlag()){
			userMessageService.changeToRead(msg.getId());
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("content", msg.getContent().replace("\n", "\\n"));
		data.put("sendFromId", msg.getSendFrom().getId());
		data.put("sendFromName", msg.getSendFrom().getNickname());
		data.put("sendTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(msg.getCreateTime()));
		map.put("message", data);
		Gson gson = new Gson();

		return gson.toJson(map);
	}
}
