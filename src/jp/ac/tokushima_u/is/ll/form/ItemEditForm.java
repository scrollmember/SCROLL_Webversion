package jp.ac.tokushima_u.is.ll.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.ItemQuestionType;
import jp.ac.tokushima_u.is.ll.entity.Item.ShareLevel;
import jp.ac.tokushima_u.is.ll.entity.ItemTitle;

import org.springframework.web.multipart.MultipartFile;

public class ItemEditForm implements Serializable {

    private static final long serialVersionUID = 5957045426756171964L;
    private String itemId;
    private String tag;
    private HashMap<String, String> titleMap = new HashMap<String, String>();
    private String barcode;
    private String qrcode;
    private String rfid;
    private String note;
    private String place;
    private Double itemLat;
    private Double itemLng;
    private Float speed;
    private Integer itemZoom;
    private MultipartFile image;
    private String question;
    private String quesLan;
    private String categoryId;
    private Item.ShareLevel shareLevel = Item.ShareLevel.PUBLIC;
    private Boolean locationBased;
    private List<Long>questionTypeIds=new ArrayList<Long>();
    
    private List<String> placeattribute = new ArrayList<String>();
    private List<String> placename = new ArrayList<String>();
    private HashMap<String,List<String>> map = new HashMap<String,List<String>>();
    /**
     * Used in update
     */
    private boolean fileExist = false;

    public ItemEditForm() {
    }

    public ItemEditForm(Item item) {
        this.itemId = item.getId();
        this.setBarcode(item.getBarcode());
        this.setQrcode(item.getQrcode());
        this.setRfid(item.getRfid());
        this.setNote(item.getNote());
        this.setPlace(item.getPlace());
        this.setItemLat(item.getItemLat());
        this.setItemLng(item.getItemLng());
        this.setSpeed(item.getSpeed());
        this.setItemZoom(item.getItemZoom());
        if(this.titleMap==null)this.titleMap = new HashMap<String, String>();
        for(ItemTitle title: item.getTitles()){
        	this.titleMap.put(title.getLanguage().getCode(), title.getContent());
        }
        if (item.getQuestion() != null) {
            this.setQuestion(item.getQuestion().getContent());
            if (item.getQuestion().getLanguage() != null) {
                this.setQuesLan(item.getQuestion().getLanguage().getCode());
            }
        }

        this.setLocationBased(item.getLocationBased());
        List<ItemQuestionType>quesTypes=item.getQuestionTypes();
        for(ItemQuestionType qt:quesTypes){
        	this.questionTypeIds.add(qt.getQuestionType().getId());
        }
    }
    
    
    public Float getSpeed() {
		return speed;
	}

	public void setSpeed(Float speed) {
		this.speed = speed;
	}

	public Boolean getLocationBased() {
		return locationBased;
	}

	public void setLocationBased(Boolean locationBased) {
		this.locationBased = locationBased;
	}

	public List<Long> getQuestionTypeIds() {
		return questionTypeIds;
	}

	public void setQuestionTypeIds(List<Long> questionTypeIds) {
		this.questionTypeIds = questionTypeIds;
	}

	public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public Integer getItemZoom() {
        return itemZoom;
    }

    public void setItemZoom(Integer itemZoom) {
        this.itemZoom = itemZoom;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getQuesLan() {
        return quesLan;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuesLan(String quesLan) {
        this.quesLan = quesLan;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isFileExist() {
        return fileExist;
    }

    public void setFileExist(boolean fileExist) {
        this.fileExist = fileExist;
    }

    public ShareLevel getShareLevel() {
        return shareLevel;
    }

    public void setShareLevel(ShareLevel shareLevel) {
        this.shareLevel = shareLevel;
    }

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setTitleMap(HashMap<String, String> titleMap) {
		this.titleMap = titleMap;
	}

	public HashMap<String, String> getTitleMap() {
		return titleMap;
	}

	public List<String> getPlaceattribute() {
		return placeattribute;
	}

	public void setPlaceattribute(List<String> placeattribute) {
		this.placeattribute = placeattribute;
	}

	public HashMap<String,List<String>> getMap() {
		return map;
	}

	public void setMap(HashMap<String,List<String>> map) {
		this.map = map;
	}

	public List<String> getPlacename() {
		return placename;
	}

	public void setPlacename(List<String> placename) {
		this.placename = placename;
	}


}
