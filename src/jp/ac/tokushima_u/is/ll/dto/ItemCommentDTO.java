package jp.ac.tokushima_u.is.ll.dto;

import jp.ac.tokushima_u.is.ll.entity.ItemComment;
import jp.ac.tokushima_u.is.ll.entity.Users;

public class ItemCommentDTO extends ItemComment{

	private static final long serialVersionUID = -3115830800900786543L;
	private Users user;
	
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
}