package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author houbin
 */
@Entity
@Table(name = "D_COUNTRY")
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Country implements Serializable {

    private static final long serialVersionUID = -7714583775370107525L;
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 32)
    private String id;
    /**
     * ISO 3166 Country Codes, two Letter code
     */
    @Column(length = 2, nullable = false, unique = true)
    private String code;
    @Column(length = 32, nullable = false)
    private String name;

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
}
