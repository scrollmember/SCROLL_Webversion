package jp.ac.tokushima_u.is.ll.form;

import jp.ac.tokushima_u.is.ll.entity.StudyArea;

public class StudyAreaForm {
	 private Double maxlat;
	    private Double maxlng;
	    private Double minlat;
	    private Double minlng;    
	    private String author_id;
	    private Long createDate;
	    
	    public StudyAreaForm(StudyArea area){
	    	this.maxlat = area.getMaxlat();
	    	this.maxlng = area.getMaxlng();
	    	this.minlat = area.getMinlat();
	    	this.minlng = area.getMinlng();
	    	this.author_id = area.getAuthor().getId();
	    	this.createDate = area.getCreateDate().getTime();
	    }
	    
		public Double getMaxlat() {
			return maxlat;
		}
		public void setMaxlat(Double maxlat) {
			this.maxlat = maxlat;
		}
		public Double getMaxlng() {
			return maxlng;
		}
		public void setMaxlng(Double maxlng) {
			this.maxlng = maxlng;
		}
		public Double getMinlat() {
			return minlat;
		}
		public void setMinlat(Double minlat) {
			this.minlat = minlat;
		}
		public Double getMinlng() {
			return minlng;
		}
		public void setMinlng(Double minlng) {
			this.minlng = minlng;
		}
		public String getAuthor_id() {
			return author_id;
		}
		public void setAuthor_id(String author_id) {
			this.author_id = author_id;
		}
		public Long getCreateDate() {
			return createDate;
		}
		public void setCreateDate(Long createDate) {
			this.createDate = createDate;
		}
}
