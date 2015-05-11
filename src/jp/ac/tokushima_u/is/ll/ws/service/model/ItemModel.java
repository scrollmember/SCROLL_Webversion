package jp.ac.tokushima_u.is.ll.ws.service.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Houbin
 */
public class ItemModel implements Serializable {

    private static final long serialVersionUID = 2093764446504972458L;
    private String id;
    private String enTitle;
    private String jpTitle;
    private String zhTitle;
    private String note;
    private String barcode;
    private String qrcode;
    private String rfid;
    private String place;
    private Double itemLat;
    private Double itemLng;
    private Integer itemZoom;
    private Integer disabled;
    private Date createTime;
    private Date updateTime;
    private String authorNickname;

    public String getAuthorNickname() {
        return authorNickname;
    }

    public void setAuthorNickname(String authorNickname) {
        this.authorNickname = authorNickname;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getEnTitle() {
        return enTitle;
    }

    public void setEnTitle(String enTitle) {
        this.enTitle = enTitle;
    }

    public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getItemLat() {
        return itemLat;
    }

    public void setItemLat(Double itemLat) {
        this.itemLat = itemLat;
    }

    public Double getItemLng() {
        return itemLng;
    }

    public void setItemLng(Double itemLng) {
        this.itemLng = itemLng;
    }

    public Integer getItemZoom() {
        return itemZoom;
    }

    public void setItemZoom(Integer itemZoom) {
        this.itemZoom = itemZoom;
    }

    public String getJpTitle() {
        return jpTitle;
    }

    public void setJpTitle(String jpTitle) {
        this.jpTitle = jpTitle;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getZhTitle() {
        return zhTitle;
    }

    public void setZhTitle(String zhTitle) {
        this.zhTitle = zhTitle;
    }
}
