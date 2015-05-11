package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author houbin
 */
@Entity
@Table(name = "D_LANGUAGE")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Language implements Serializable {
	
    private static final long serialVersionUID = -175586803239488933L;
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 32)
    private String id;
    /**
     * ISO 639 Language Codes, two Letter code
     */
    @Column(length = 5, nullable = false, unique = true)
    private String code;
    @Column(length = 32, nullable = false)
    private String name;


    public Language() {
    }

    public Language(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Language && this.getId()!=null){
			return this.getId().equals(((Language)obj).getId());
		}else{
			return false;
		}
	}
}
