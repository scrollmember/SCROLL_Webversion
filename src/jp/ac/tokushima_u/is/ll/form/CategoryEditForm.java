package jp.ac.tokushima_u.is.ll.form;

import java.io.Serializable;

public class CategoryEditForm implements Serializable {

	private static final long serialVersionUID = -5945250921704125816L;

	private String name;
	private String note;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
