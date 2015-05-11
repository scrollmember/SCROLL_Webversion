package jp.ac.tokushima_u.is.ll.dto;

import jp.ac.tokushima_u.is.ll.entity.ItemTitle;

public class ItemTitleDTO extends ItemTitle {

	private static final long serialVersionUID = -2576236748193701562L;

	private String langCode;
	private String langName;

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public String getLangName() {
		return langName;
	}

	public void setLangName(String langName) {
		this.langName = langName;
	}

}
