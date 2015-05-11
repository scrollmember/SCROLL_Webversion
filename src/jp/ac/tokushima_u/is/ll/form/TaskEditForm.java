package jp.ac.tokushima_u.is.ll.form;

import java.io.Serializable;

public class TaskEditForm implements Serializable {

	private static final long serialVersionUID = 5957045426756171964L;
	private String title;
	private String taskId;
	// private String place;
	// private Double lat;
	// private Double lng;
	// private Integer zoom;
	private Integer level;
	private String languageId;
	private String tag;
	// default setting for checkbox
	// private Boolean locationBased=Boolean.TRUE;
	private Boolean isPublished = Boolean.FALSE;

	public Boolean getIsPublished() {
		return isPublished;
	}

	public void setIsPublished(Boolean isPublished) {
		this.isPublished = isPublished;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getLanguageId() {
		return languageId;
	}

	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
