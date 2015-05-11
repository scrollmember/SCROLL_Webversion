package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author vstar
 */
@Entity
@Table(name = "T_FILE_BIN")
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class FileBin implements Serializable {

    private static final long serialVersionUID = -8157161763918179866L;
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 32)
    private String id;
    @Lob
    private byte[] bin;
    private FileData fileData;

    public byte[] getBin() {
        return bin;
    }

    public void setBin(byte[] bin) {
        this.bin = bin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FileData getFileData() {
        return fileData;
    }

    public void setFileData(FileData fileData) {
        this.fileData = fileData;
    }
}
