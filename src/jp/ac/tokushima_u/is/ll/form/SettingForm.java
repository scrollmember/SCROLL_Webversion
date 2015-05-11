package jp.ac.tokushima_u.is.ll.form;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author li
 */
public class SettingForm implements java.io.Serializable {

	private static final long serialVersionUID = -7694472411998569388L;

	private Integer handsetcd;

    private String monstime1;
    private String monsmin1;
    private String monfperiod1;
    private String montperiod1;
    private String monfperiod2;
    private String montperiod2;
    private String monfperiod3;
    private String montperiod3;

    private String tuestime1;
    private String tuesmin1;
    private String tuefperiod1;
    private String tuetperiod1;
    private String tuefperiod2;
    private String tuetperiod2;
    private String tuefperiod3;
    private String tuetperiod3;
    
    private String wedstime1;
    private String wedsmin1;
    private String wedfperiod1;
    private String wedtperiod1;
    private String wedfperiod2;
    private String wedtperiod2;
    private String wedfperiod3;
    private String wedtperiod3;

    private String thurstime1;
    private String thursmin1;
    private String thurfperiod1;
    private String thurtperiod1;
    private String thurfperiod2;
    private String thurtperiod2;
    private String thurfperiod3;
    private String thurtperiod3;

    private String fristime1;
    private String frismin1;
    private String frifperiod1;
    private String fritperiod1;
    private String frifperiod2;
    private String fritperiod2;
    private String frifperiod3;
    private String fritperiod3;

    private String satstime1;
    private String satsmin1;
    private String satfperiod1;
    private String sattperiod1;
    private String satfperiod2;
    private String sattperiod2;
    private String satfperiod3;
    private String sattperiod3;

    private String sunstime1;
    private String sunsmin1;
    private String sunfperiod1;
    private String suntperiod1;
    private String sunfperiod2;
    private String suntperiod2;
    private String sunfperiod3;
    private String suntperiod3;
    
    private boolean receiveMailNotification;
    
    private List<String> categoryIdList = new ArrayList<String>();
    private String defaultCategoryId;

    private Boolean random;
	private String yifan;
	private int viewdistance;
	private int kaquality;
	
	private String groupname;
	private String privacy;
    
    
    public Integer getHandsetcd() {
        return handsetcd;
    }

    public void setHandsetcd(Integer handsetcd) {
        this.handsetcd = handsetcd;
    }

    public String getFrismin1() {
        return frismin1;
    }

    public void setFrismin1(String frismin1) {
        this.frismin1 = frismin1;
    }

    public String getMonsmin1() {
        return monsmin1;
    }

    public void setMonsmin1(String monsmin1) {
        this.monsmin1 = monsmin1;
    }

    public String getSatsmin1() {
        return satsmin1;
    }

    public void setSatsmin1(String satsmin1) {
        this.satsmin1 = satsmin1;
    }

    public String getSunsmin1() {
        return sunsmin1;
    }

    public void setSunsmin1(String sunsmin1) {
        this.sunsmin1 = sunsmin1;
    }

    public String getThursmin1() {
        return thursmin1;
    }

    public void setThursmin1(String thursmin1) {
        this.thursmin1 = thursmin1;
    }

    public String getTuesmin1() {
        return tuesmin1;
    }

    public void setTuesmin1(String tuesmin1) {
        this.tuesmin1 = tuesmin1;
    }

    public String getWedsmin1() {
        return wedsmin1;
    }

    public void setWedsmin1(String wedsmin1) {
        this.wedsmin1 = wedsmin1;
    }

    public String getFrifperiod1() {
        return frifperiod1;
    }

    public void setFrifperiod1(String frifperiod1) {
        this.frifperiod1 = frifperiod1;
    }

    public String getFrifperiod2() {
        return frifperiod2;
    }

    public void setFrifperiod2(String frifperiod2) {
        this.frifperiod2 = frifperiod2;
    }

    public String getFrifperiod3() {
        return frifperiod3;
    }

    public void setFrifperiod3(String frifperiod3) {
        this.frifperiod3 = frifperiod3;
    }

    public String getFristime1() {
        return fristime1;
    }

    public void setFristime1(String fristime1) {
        this.fristime1 = fristime1;
    }

    public String getFritperiod1() {
        return fritperiod1;
    }

    public void setFritperiod1(String fritperiod1) {
        this.fritperiod1 = fritperiod1;
    }

    public String getFritperiod2() {
        return fritperiod2;
    }

    public void setFritperiod2(String fritperiod2) {
        this.fritperiod2 = fritperiod2;
    }

    public String getFritperiod3() {
        return fritperiod3;
    }

    public void setFritperiod3(String fritperiod3) {
        this.fritperiod3 = fritperiod3;
    }

    public String getMonfperiod1() {
        return monfperiod1;
    }

    public void setMonfperiod1(String monfperiod1) {
        this.monfperiod1 = monfperiod1;
    }

    public String getMonfperiod2() {
        return monfperiod2;
    }

    public void setMonfperiod2(String monfperiod2) {
        this.monfperiod2 = monfperiod2;
    }

    public String getMonfperiod3() {
        return monfperiod3;
    }

    public void setMonfperiod3(String monfperiod3) {
        this.monfperiod3 = monfperiod3;
    }

    public String getMonstime1() {
        return monstime1;
    }

    public void setMonstime1(String monstime1) {
        this.monstime1 = monstime1;
    }

    public String getMontperiod1() {
        return montperiod1;
    }

    public void setMontperiod1(String montperiod1) {
        this.montperiod1 = montperiod1;
    }

    public String getMontperiod2() {
        return montperiod2;
    }

    public void setMontperiod2(String montperiod2) {
        this.montperiod2 = montperiod2;
    }

    public String getMontperiod3() {
        return montperiod3;
    }

    public void setMontperiod3(String montperiod3) {
        this.montperiod3 = montperiod3;
    }

    public String getSatfperiod1() {
        return satfperiod1;
    }

    public void setSatfperiod1(String satfperiod1) {
        this.satfperiod1 = satfperiod1;
    }

    public String getSatfperiod2() {
        return satfperiod2;
    }

    public void setSatfperiod2(String satfperiod2) {
        this.satfperiod2 = satfperiod2;
    }

    public String getSatfperiod3() {
        return satfperiod3;
    }

    public void setSatfperiod3(String satfperiod3) {
        this.satfperiod3 = satfperiod3;
    }

    public String getSatstime1() {
        return satstime1;
    }

    public void setSatstime1(String satstime1) {
        this.satstime1 = satstime1;
    }

    public String getSattperiod1() {
        return sattperiod1;
    }

    public void setSattperiod1(String sattperiod1) {
        this.sattperiod1 = sattperiod1;
    }

    public String getSattperiod2() {
        return sattperiod2;
    }

    public void setSattperiod2(String sattperiod2) {
        this.sattperiod2 = sattperiod2;
    }

    public String getSattperiod3() {
        return sattperiod3;
    }

    public void setSattperiod3(String sattperiod3) {
        this.sattperiod3 = sattperiod3;
    }

    public String getSunfperiod1() {
        return sunfperiod1;
    }

    public void setSunfperiod1(String sunfperiod1) {
        this.sunfperiod1 = sunfperiod1;
    }

    public String getSunfperiod2() {
        return sunfperiod2;
    }

    public void setSunfperiod2(String sunfperiod2) {
        this.sunfperiod2 = sunfperiod2;
    }

    public String getSunfperiod3() {
        return sunfperiod3;
    }

    public void setSunfperiod3(String sunfperiod3) {
        this.sunfperiod3 = sunfperiod3;
    }

    public String getSunstime1() {
        return sunstime1;
    }

    public void setSunstime1(String sunstime1) {
        this.sunstime1 = sunstime1;
    }

    public String getSuntperiod1() {
        return suntperiod1;
    }

    public void setSuntperiod1(String suntperiod1) {
        this.suntperiod1 = suntperiod1;
    }

    public String getSuntperiod2() {
        return suntperiod2;
    }

    public void setSuntperiod2(String suntperiod2) {
        this.suntperiod2 = suntperiod2;
    }

    public String getSuntperiod3() {
        return suntperiod3;
    }

    public void setSuntperiod3(String suntperiod3) {
        this.suntperiod3 = suntperiod3;
    }

    public String getThurfperiod1() {
        return thurfperiod1;
    }

    public void setThurfperiod1(String thurfperiod1) {
        this.thurfperiod1 = thurfperiod1;
    }

    public String getThurfperiod2() {
        return thurfperiod2;
    }

    public void setThurfperiod2(String thurfperiod2) {
        this.thurfperiod2 = thurfperiod2;
    }

    public String getThurfperiod3() {
        return thurfperiod3;
    }

    public void setThurfperiod3(String thurfperiod3) {
        this.thurfperiod3 = thurfperiod3;
    }

    public String getThurstime1() {
        return thurstime1;
    }

    public void setThurstime1(String thurstime1) {
        this.thurstime1 = thurstime1;
    }

    public String getThurtperiod1() {
        return thurtperiod1;
    }

    public void setThurtperiod1(String thurtperiod1) {
        this.thurtperiod1 = thurtperiod1;
    }

    public String getThurtperiod2() {
        return thurtperiod2;
    }

    public void setThurtperiod2(String thurtperiod2) {
        this.thurtperiod2 = thurtperiod2;
    }

    public String getThurtperiod3() {
        return thurtperiod3;
    }

    public void setThurtperiod3(String thurtperiod3) {
        this.thurtperiod3 = thurtperiod3;
    }

    public String getTuefperiod1() {
        return tuefperiod1;
    }

    public void setTuefperiod1(String tuefperiod1) {
        this.tuefperiod1 = tuefperiod1;
    }

    public String getTuefperiod2() {
        return tuefperiod2;
    }

    public void setTuefperiod2(String tuefperiod2) {
        this.tuefperiod2 = tuefperiod2;
    }

    public String getTuefperiod3() {
        return tuefperiod3;
    }

    public void setTuefperiod3(String tuefperiod3) {
        this.tuefperiod3 = tuefperiod3;
    }

    public String getTuestime1() {
        return tuestime1;
    }

    public void setTuestime1(String tuestime1) {
        this.tuestime1 = tuestime1;
    }

    public String getTuetperiod1() {
        return tuetperiod1;
    }

    public void setTuetperiod1(String tuetperiod1) {
        this.tuetperiod1 = tuetperiod1;
    }

    public String getTuetperiod2() {
        return tuetperiod2;
    }

    public void setTuetperiod2(String tuetperiod2) {
        this.tuetperiod2 = tuetperiod2;
    }

    public String getTuetperiod3() {
        return tuetperiod3;
    }

    public void setTuetperiod3(String tuetperiod3) {
        this.tuetperiod3 = tuetperiod3;
    }

    public String getWedfperiod1() {
        return wedfperiod1;
    }

    public void setWedfperiod1(String wedfperiod1) {
        this.wedfperiod1 = wedfperiod1;
    }

    public String getWedfperiod2() {
        return wedfperiod2;
    }

    public void setWedfperiod2(String wedfperiod2) {
        this.wedfperiod2 = wedfperiod2;
    }

    public String getWedfperiod3() {
        return wedfperiod3;
    }

    public void setWedfperiod3(String wedfperiod3) {
        this.wedfperiod3 = wedfperiod3;
    }

    public String getWedstime1() {
        return wedstime1;
    }

    public void setWedstime1(String wedstime1) {
        this.wedstime1 = wedstime1;
    }

    public String getWedtperiod1() {
        return wedtperiod1;
    }

    public void setWedtperiod1(String wedtperiod1) {
        this.wedtperiod1 = wedtperiod1;
    }

    public String getWedtperiod2() {
        return wedtperiod2;
    }

    public void setWedtperiod2(String wedtperiod2) {
        this.wedtperiod2 = wedtperiod2;
    }

    public String getWedtperiod3() {
        return wedtperiod3;
    }

    public void setWedtperiod3(String wedtperiod3) {
        this.wedtperiod3 = wedtperiod3;
    }

	public void setCategoryIdList(List<String> categoryIdList) {
		this.categoryIdList = categoryIdList;
	}

	public List<String> getCategoryIdList() {
		return categoryIdList;
	}

	public void setDefaultCategoryId(String defaultCategoryId) {
		this.defaultCategoryId = defaultCategoryId;
	}

	public String getDefaultCategoryId() {
		return defaultCategoryId;
	}

	public void setReceiveMailNotification(boolean receiveMailNotification) {
		this.receiveMailNotification = receiveMailNotification;
	}

	public boolean isReceiveMailNotification() {
		return receiveMailNotification;
	}

	public Boolean getRandom() {
		return random;
	}

	public void setRandom(Boolean random) {
		this.random = random;
	}



	public int getViewdistance() {
		return viewdistance;
	}

	public void setViewdistance(int viewdistance) {
		this.viewdistance = viewdistance;
	}

	public int getKaquality() {
		return kaquality;
	}

	public void setKaquality(int kaquality) {
		this.kaquality = kaquality;
	}

	public String getYifan() {
		return yifan;
	}

	public void setYifan(String yifan) {
		this.yifan = yifan;
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
}
