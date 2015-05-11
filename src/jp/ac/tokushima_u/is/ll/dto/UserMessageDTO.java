package jp.ac.tokushima_u.is.ll.dto;

import jp.ac.tokushima_u.is.ll.entity.UserMessage;
import jp.ac.tokushima_u.is.ll.entity.Users;

public class UserMessageDTO extends UserMessage{

	private static final long serialVersionUID = 4228136898960917511L;

	private Users sendFromUser;
	private Users sendToUser;
	
	public Users getSendFromUser() {
		return sendFromUser;
	}
	public void setSendFromUser(Users sendFromUser) {
		this.sendFromUser = sendFromUser;
	}
	public Users getSendToUser() {
		return sendToUser;
	}
	public void setSendToUser(Users sendToUser) {
		this.sendToUser = sendToUser;
	}
}
