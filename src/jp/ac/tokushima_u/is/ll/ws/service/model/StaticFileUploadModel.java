package jp.ac.tokushima_u.is.ll.ws.service.model;

import java.io.Serializable;

/**
 *
 * @author Houbin
 */
public class StaticFileUploadModel implements Serializable {

    private static final long serialVersionUID = 8959417975435149279L;
    private String fileName;
    private String extName;
    private byte[] file;

    public String getExtName() {
        return extName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
