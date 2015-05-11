package jp.ac.tokushima_u.is.ll.dto;

import jp.ac.tokushima_u.is.ll.entity.Answer;
import jp.ac.tokushima_u.is.ll.entity.Users;

public class AnswerDTO extends Answer {

	private static final long serialVersionUID = 3211348336540653007L;

	private Users author;

	public Users getAuthor() {
		return author;
	}

	public void setAuthor(Users author) {
		this.author = author;
	}

}
