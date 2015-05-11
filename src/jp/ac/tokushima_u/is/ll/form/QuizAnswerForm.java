package jp.ac.tokushima_u.is.ll.form;

/**
 *
 * @author li
 */
public class QuizAnswerForm {
    private String quizid;
    private String answer;
    private Boolean hasimage;
    private String language;
//    private Integer quiztype;
    private Integer alarmtype;
    private Boolean isAnswered;
    private Integer pass;
    private Integer dashboardtype=0;
    
	public Integer getDashboardtype() {
		return dashboardtype;
	}

	public void setDashboardtype(Integer dashboardtype) {
		this.dashboardtype = dashboardtype;
	}
    
	public Integer getAlarmtype() {
		return alarmtype;
	}

	public void setAlarmtype(Integer alarmtype) {
		this.alarmtype = alarmtype;
	}

	public Integer getPass() {
		return pass;
	}

	public void setPass(Integer pass) {
		this.pass = pass;
	}

	public Boolean getIsAnswered() {
        return isAnswered;
    }

    public void setIsAnswered(Boolean isAnswered) {
        this.isAnswered = isAnswered;
    }

//    public Integer getQuiztype() {
//        return quiztype;
//    }
//
//    public void setQuiztype(Integer quiztype) {
//        this.quiztype = quiztype;
//    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Boolean getHasimage() {
        return hasimage;
    }

    public void setHasimage(Boolean hasimage) {
        this.hasimage = hasimage;
    }

    public String getQuizid() {
        return quizid;
    }

    public void setQuizid(String quizid) {
        this.quizid = quizid;
    }
}
