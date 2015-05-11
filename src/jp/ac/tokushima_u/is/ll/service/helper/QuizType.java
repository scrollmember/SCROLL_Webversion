package jp.ac.tokushima_u.is.ll.service.helper;

public enum QuizType {
	DescribeImageQuiz(1),WordMeaningQuiz(2);

	    private int value;

	    QuizType(int v){
	        this.value = v;
	    }

	    public int getValue() {
	        return value;
	    }

	    public void setValue(int value) {
	        this.value = value;
	    }
}
