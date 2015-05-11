package jp.ac.tokushima_u.is.ll.form;

import java.io.Serializable;

import jp.ac.tokushima_u.is.ll.entity.Answer;

public class QuestionAnswerForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String question_id;
	private String content;
	private String nickname;
	private Long updatetime;
	private String answer_id;
	private String author_id;

	public QuestionAnswerForm(Answer answer) {
		this.question_id = answer.getQuestion().getId();
		this.content = answer.getAnswer();
		this.answer_id = answer.getId();
		this.nickname = answer.getAuthor().getNickname();
		this.updatetime = answer.getCreateDate().getTime();
		this.author_id = answer.getAuthor().getId();
	}

	public String getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(String author_id) {
		this.author_id = author_id;
	}

	public String getAnswer_id() {
		return answer_id;
	}

	public void setAnswer_id(String answer_id) {
		this.answer_id = answer_id;
	}

	public String getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(String question_id) {
		this.question_id = question_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Long getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Long updatetime) {
		this.updatetime = updatetime;
	}
}
