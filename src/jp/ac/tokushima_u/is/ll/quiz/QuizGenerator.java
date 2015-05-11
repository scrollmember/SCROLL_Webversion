package jp.ac.tokushima_u.is.ll.quiz;

import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.ItemQuestionType;
import jp.ac.tokushima_u.is.ll.entity.MyQuiz;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.service.helper.QuizCondition;

public interface QuizGenerator {
	public MyQuiz generate(Item item, ItemQuestionType questionType, Users user, QuizCondition qc);
	public MyQuiz checkAnswer(MyQuiz quiz);
	public QuizWrapper convertQuiz(MyQuiz quiz);
}
