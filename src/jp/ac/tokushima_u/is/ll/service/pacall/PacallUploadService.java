package jp.ac.tokushima_u.is.ll.service.pacall;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.entity.pacall.Folder;
import jp.ac.tokushima_u.is.ll.entity.pacall.SenseData;
import jp.ac.tokushima_u.is.ll.entity.pacall.SensePic;
import jp.ac.tokushima_u.is.ll.form.pacall.UploadForm;
import jp.ac.tokushima_u.is.ll.service.PropertyService;
import jp.ac.tokushima_u.is.ll.service.StorageService;
import jp.ac.tokushima_u.is.ll.util.Utility;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class PacallUploadService {
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private PropertyService propertyService;
	
	@Autowired
	private SensedataService sensedataService;
	
	@Autowired
	private FolderService folderService;
	
	@Autowired
	private SensepicService sensepicService;
	
	@Autowired
	private StorageService storageService;

	/**
	 * Upload Sensor CSV, create new folder, process sensor csv file
	 * @param UploadForm
	 * @return {folderId, fileId}
	 */
	public Map<String, Object> uploadSensorCsv(UploadForm form, String userid) {
		MultipartFile file = form.getFile();
		String hash = "";
		try {
			hash = DigestUtils.shaHex(file.getInputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//Create Folder/Sensorcsv
		String filename = file.getOriginalFilename();
		String folderName = filename;
		if(filename.contains("/")){
			String[] names = filename.split("/");
			if(names.length > 1){
				folderName = names[0];
				filename = names[names.length-1];
			}
		}
		Folder folder = folderService.findByHash(hash);
		if(folder == null){
			try {
				String fileId = storageService.storeOneFile(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
				folder = new Folder();
				folder.setId(Utility.getUuidKey());
				folder.setName(folderName);
				folder.setHash(hash);
				folder.setCreatetime(Utility.getCurrentTime());
				folder.setUserId(userid);
				folder.setCsvFileId(fileId);
				folderService.createFolder(folder);
				List<SenseData> sensedataList = sensedataService.uploadSenseCsvToDatabase(folder.getId(), file);
				folderService.updateStartEndDate(folder.getId(), sensedataList);
				List<SensePic> sensePicList = sensepicService.addSensepicToDatabase(folder.getId(), sensedataList);
				sensepicService.classifySensePic(sensePicList);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("folderId", folder.getId());
		return result;
	}

	/**
	 * 
	 * @param form
	 * @param userid
	 * @return sensePicId
	 */
	public String uploadPhoto(UploadForm form, String userid) {
		String folderId = form.getFolderId();
		if(StringUtils.isBlank(folderId)){
			throw new RuntimeException("Folder id is empty");
		}
		Folder folder = folderService.findById(folderId);
		if(folder == null){
			throw new RuntimeException("Folder is not exist");
		}
		MultipartFile file = form.getFile();
		String filename = file.getOriginalFilename();
		if(filename.contains("/")){
			String[] names = filename.split("/");
			if(names.length > 1){
				filename = names[names.length-1];
			}
		}
		SensePic sensePic = sensepicService.findByName(folder.getId(), filename);
		if(sensePic!=null){
			try {
				String fileId = storageService.storeOneFile(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
				sensepicService.updateFileId(sensePic.getId(), fileId);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return sensePic.getId();
		}
		return null;
	}
}
