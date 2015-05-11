package jp.ac.tokushima_u.is.ll.dto;

import java.io.Serializable;
import java.util.List;

public class PacallExtraInfo implements Serializable {

	private static final long serialVersionUID = 1734870178680142448L;

	private boolean isDark = false;
	private boolean isDefocused = false;
	private boolean isFeature = false;
	private boolean isText = false;
	private boolean isFace = false;
	private boolean isManual = false;
	private String groupId;
	private List<ItemDTO> uploadedByOthers;
	private List<ItemDTO> uploadedBySelf;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public List<ItemDTO> getUploadedByOthers() {
		return uploadedByOthers;
	}

	public void setUploadedByOthers(List<ItemDTO> uploadedByOthers) {
		this.uploadedByOthers = uploadedByOthers;
	}

	public List<ItemDTO> getUploadedBySelf() {
		return uploadedBySelf;
	}

	public void setUploadedBySelf(List<ItemDTO> uploadedBySelf) {
		this.uploadedBySelf = uploadedBySelf;
	}

	public boolean isDark() {
		return isDark;
	}

	public void setDark(boolean isDark) {
		this.isDark = isDark;
	}

	public boolean isDefocused() {
		return isDefocused;
	}

	public void setDefocused(boolean isDefocused) {
		this.isDefocused = isDefocused;
	}

	public boolean isFeature() {
		return isFeature;
	}

	public void setFeature(boolean isFeature) {
		this.isFeature = isFeature;
	}

	public boolean isText() {
		return isText;
	}

	public void setText(boolean isText) {
		this.isText = isText;
	}

	public boolean isFace() {
		return isFace;
	}

	public void setFace(boolean isFace) {
		this.isFace = isFace;
	}

	public boolean isManual() {
		return isManual;
	}

	public void setManual(boolean isManual) {
		this.isManual = isManual;
	}

}
