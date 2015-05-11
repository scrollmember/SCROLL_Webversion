package jp.ac.tokushima_u.is.ll.form.pacall;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class UploadForm implements Serializable {

	private static final long serialVersionUID = 4446668293037505938L;
	
	private String folderId;
	private MultipartFile file;

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}
}
