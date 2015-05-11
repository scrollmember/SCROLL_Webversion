package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import jp.ac.tokushima_u.is.ll.util.FilenameUtil;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;

/**
 *
 * @author houbin
 */
@Entity
@Table(name = "T_FILE_DATA")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FileData implements Serializable {

    private static final long serialVersionUID = -7509406549928484484L;
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 32)
    private String id;
    @Column(name = "orig_name", length = 64)
    private String origName;
    @Column(name = "file_type", length = 20)
    private String fileType;
    @Column(name = "create_at")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createdAt;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mail_id")
    private Mail mail;
    @Column(name = "md5", length = 32)
    @Index(name = "index_file_data_md5")
    private String md5;

    
    public String getFileType() {
    	if(fileType==null||fileType.length()==0){
            if(!StringUtils.isBlank(this.getOrigName())){
            	fileType = FilenameUtil.checkMediaType(this.getOrigName());
            }
    	}
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrigName() {
        return origName;
    }

    public void setOrigName(String origName) {
        this.origName = origName;
    }

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}
}
