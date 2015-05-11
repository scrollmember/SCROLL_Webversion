package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/*
CREATE TABLE `t_image_histogram` (
  `id` varchar(32) NOT NULL,
  `hist` tinyint(4) NOT NULL,
  `data` smallint(6) NOT NULL,
  PRIMARY KEY (`id`,`hist`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
*/

/**
 * 
 * @author maou
 */
@Entity
@Table(name = "T_IMAGE_HISTOGRAM")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ImageHistogram implements Serializable {
	private static final long serialVersionUID = -582705359554009710L;

	@Id
	@Column
	private ImageHistogramPk id;
	@Column
	private int data;

	public ImageHistogram() {
	}
	
	/**
	 * 初期化用コンストラクタ
	 * 
	 * @param _id
	 * @param _no
	 * @param _hist
	 */
	public ImageHistogram(String _id, int _type, int _data) {
		id = new ImageHistogramPk(_id, _type);
		data = _data;
	}
	
	
	public ImageHistogramPk getId() {
		return id;
	}
	public void setId(ImageHistogramPk id) {
		this.id = id;
	}
	public int getData() {
		return data;
	}
	public void setData(int data) {
		this.data = data;
	}


	@Embeddable
    public static class ImageHistogramPk implements Serializable{
		private static final long serialVersionUID = 3044283971610486834L;
		
		@Column(length = 32)
		private String id;
		@Column(length = 10)
        private int hist;
		

		public ImageHistogramPk(){
		}
		
		/**
		 * 初期化用コンストラクタ
		 * 
		 * @param _id
		 * @param _no
		 */
		public ImageHistogramPk(String _id, int _hist){
			id = _id;
			hist = _hist;
		}
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public int getHist() {
			return hist;
		}
		public void setHist(int hist) {
			this.hist = hist;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + hist;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
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
			ImageHistogramPk other = (ImageHistogramPk) obj;
			if (hist != other.hist)
				return false;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}
		

    }

}
