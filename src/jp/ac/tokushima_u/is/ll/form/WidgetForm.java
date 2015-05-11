package jp.ac.tokushima_u.is.ll.form;

import java.io.Serializable;
import java.util.Map;

public class WidgetForm implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String image;
	private String lang1name = "";
	private String lang2name = "";
	private String lang3name = "";
	private String lang1word = "";
	private String lang2word = "";
	private String lang3word = "";
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getLang1name() {
		return lang1name;
	}
	public void setLang1name(String lang1name) {
		this.lang1name = lang1name;
	}
	public String getLang2name() {
		return lang2name;
	}
	public void setLang2name(String lang2name) {
		this.lang2name = lang2name;
	}
	public String getLang1word() {
		return lang1word;
	}
	public void setLang1word(String lang1word) {
		this.lang1word = lang1word;
	}
	public String getLang2word() {
		return lang2word;
	}
	public void setLang2word(String lang2word) {
		this.lang2word = lang2word;
	}
	@Override
	public String toString() {
		return "WidgetForm [image=" + image + ", lang1name=" + lang1name
				+ ", lang2name=" + lang2name + ", lang1word=" + lang1word
				+ ", lang2word=" + lang2word + "]";
	}
	public String getLang3name() {
		return lang3name;
	}
	public void setLang3name(String lang3name) {
		this.lang3name = lang3name;
	}
	public String getLang3word() {
		return lang3word;
	}
	public void setLang3word(String lang3word) {
		this.lang3word = lang3word;
	}
	
	
}
