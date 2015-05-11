package jp.ac.tokushima_u.is.ll.dto;

import java.io.Serializable;

public class ItemListMailDTO implements Serializable {
	private static final long serialVersionUID = 3594988716406885579L;

	private String url;
	private String title;
	private String author;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

}
