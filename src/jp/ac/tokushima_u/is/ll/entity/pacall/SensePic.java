package jp.ac.tokushima_u.is.ll.entity.pacall;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * SensePic
 * 
 * @author Houbin see also: <a href=
 *         "http://www.clarity-centre.org/claritywiki/images/7/72/SenseCam_User_Guide_v1.4.pdf"
 *         >Documentation</a>
 */
public class SensePic implements Serializable {
	public enum Reason {
		PIR, TIM, MAN, CLR, MAG, ACC, TMP
	}

	private static final long serialVersionUID = -1920781392842722124L;
	

	// Basic Date
	private String id;
	private int index;
	private String folderId;
	private String cam; // Image File Name
	private Reason reason;
	private Date date; // Date
	private String ptypeId;

	// Values

	private Double clr; // CLR
	private Double tmp; // Temperature

	private Double accX;
	private Double accY;
	private Double accZ;

	private Double magX;
	private Double magY;
	private Double magZ;
	
	private String samePicId;
	
	private Date uploadTime;
	
	private String fileId;
	
	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public String getCam() {
		return cam;
	}
	
	public String getName(){
		if(StringUtils.isBlank(this.cam)){
			return "";
		}else{
			int i=this.cam.indexOf('.');
			return cam.substring(0, i);
		}
	}

	public void setCam(String cam) {
		this.cam = cam;
	}

	public Reason getReason() {
		return reason;
	}

	public void setReason(Reason reason) {
		this.reason = reason;
	}

	public Double getAccX() {
		return accX;
	}

	public void setAccX(Double accX) {
		this.accX = accX;
	}

	public Double getAccY() {
		return accY;
	}

	public void setAccY(Double accY) {
		this.accY = accY;
	}

	public Double getAccZ() {
		return accZ;
	}

	public Double getAcc() {
		return Math.sqrt(this.accX * this.accX + this.accY * this.accY);
	}

	public void setAccZ(Double accZ) {
		this.accZ = accZ;
	}

	public Double getClr() {
		return clr;
	}

	public void setClr(Double clr) {
		this.clr = clr;
	}

	public Double getTmp() {
		return tmp;
	}

	public void setTmp(Double tmp) {
		this.tmp = tmp;
	}

	public Double getMagX() {
		return magX;
	}

	public void setMagX(Double magX) {
		this.magX = magX;
	}

	public Double getMagY() {
		return magY;
	}

	public void setMagY(Double magY) {
		this.magY = magY;
	}

	public Double getMagZ() {
		return magZ;
	}

	public void setMagZ(Double magZ) {
		this.magZ = magZ;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cam == null) ? 0 : cam.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SensePic other = (SensePic) obj;
		if (cam == null) {
			if (other.cam != null)
				return false;
		} else if (!cam.equals(other.cam))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		return true;
	}

	public String getSamePicId() {
		return samePicId;
	}

	public void setSamePicId(String samePicId) {
		this.samePicId = samePicId;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPtypeId() {
		return ptypeId;
	}

	public void setPtypeId(String ptypeId) {
		this.ptypeId = ptypeId;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
}
