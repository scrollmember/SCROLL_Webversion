package jp.ac.tokushima_u.is.ll.dto;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.MyQuiz;
import jp.ac.tokushima_u.is.ll.entity.MyQuizChoice;

public class MyQuizDTO extends MyQuiz{

	private static final long serialVersionUID = -5288249363610072159L;
	
	private ItemDTO item;
	private List<MyQuizChoice> quizChoices;

	public ItemDTO getItem() {
		return item;
	}

	public void setItem(ItemDTO item) {
		this.item = item;
	}

	public List<MyQuizChoice> getQuizChoices() {
		return quizChoices;
	}

	public void setQuizChoices(List<MyQuizChoice> quizChoices) {
		this.quizChoices = quizChoices;
	}
}
