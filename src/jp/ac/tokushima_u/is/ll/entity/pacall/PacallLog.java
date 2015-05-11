package jp.ac.tokushima_u.is.ll.entity.pacall;

import java.io.Serializable;
import java.util.Date;

public class PacallLog implements Serializable {

	private static final long serialVersionUID = 7896722358692428877L;

	private String id;
	private String userId;
	private Date createTime;
	private String extra;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

}
