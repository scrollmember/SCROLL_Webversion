package jp.ac.tokushima_u.is.ll.entity.pacall;

import java.io.Serializable;
import java.util.Date;

public class Folder implements Serializable {

	private static final long serialVersionUID = -7216284176303640732L;
	private String id;
	private String name;
	private String hash;
	private Date createtime;
	private String userId;
	private int picnum;
	private Date startDate;
	private Date endDate;
	private String csvFileId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getPicnum() {
		return picnum;
	}

	public void setPicnum(int picnum) {
		this.picnum = picnum;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getCsvFileId() {
		return csvFileId;
	}

	public void setCsvFileId(String csvFileId) {
		this.csvFileId = csvFileId;
	}

}
