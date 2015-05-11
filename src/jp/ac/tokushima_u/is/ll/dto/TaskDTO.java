package jp.ac.tokushima_u.is.ll.dto;

import jp.ac.tokushima_u.is.ll.entity.Task;
import jp.ac.tokushima_u.is.ll.entity.Users;

import com.google.api.translate.Language;

public class TaskDTO {
	private Task task;
	private Users author;
	private Language language;
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public Users getAuthor() {
		return author;
	}
	public void setAuthor(Users author) {
		this.author = author;
	}
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}
}
