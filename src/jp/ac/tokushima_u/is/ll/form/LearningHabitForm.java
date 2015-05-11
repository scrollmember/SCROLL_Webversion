package jp.ac.tokushima_u.is.ll.form;

import java.io.Serializable;

public class LearningHabitForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer geoscore;
	private Integer timescore;
	private Integer timerecommend;
	private Integer georecommend;
	public Integer getGeoscore() {
		return geoscore;
	}
	public void setGeoscore(Integer geoscore) {
		this.geoscore = geoscore;
	}
	public Integer getTimescore() {
		return timescore;
	}
	public void setTimescore(Integer timescore) {
		this.timescore = timescore;
	}
	public Integer getTimerecommend() {
		return timerecommend;
	}
	public void setTimerecommend(Integer timerecommend) {
		this.timerecommend = timerecommend;
	}
	public Integer getGeorecommend() {
		return georecommend;
	}
	public void setGeorecommend(Integer georecommend) {
		this.georecommend = georecommend;
	}

}
