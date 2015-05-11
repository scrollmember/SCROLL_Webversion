package jp.ac.tokushima_u.is.ll.entity.pacall;

import java.io.Serializable;

public class PicPtype implements Serializable {

	private static final long serialVersionUID = 5523096528449991854L;

	public PicPtype(String sensepicId, String ptypeId){
		this.sensepicId  = sensepicId;
		this.ptypeId = ptypeId;
	}
	
	private String sensepicId;
	private String ptypeId;

	public String getSensepicId() {
		return sensepicId;
	}

	public void setSensepicId(String sensepicId) {
		this.sensepicId = sensepicId;
	}

	public String getPtypeId() {
		return ptypeId;
	}

	public void setPtypeId(String ptypeId) {
		this.ptypeId = ptypeId;
	}

}
