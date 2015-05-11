package jp.ac.tokushima_u.is.ll.entity;

public class Groupmember {

	private String id;
	private String author;
	private String groupname;
	private String privacy;
	private String other;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public String getPrivacy() {
		return privacy;
	}
	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
}
