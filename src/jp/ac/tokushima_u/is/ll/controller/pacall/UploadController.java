package jp.ac.tokushima_u.is.ll.controller.pacall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.form.pacall.UploadForm;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.pacall.PacallUploadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

@Controller
@RequestMapping("/pacall/upload")
public class UploadController {
	
	@Autowired
	private PacallUploadService pacallUploadService;
	
	@RequestMapping(method=RequestMethod.GET)
	public String index(@ModelAttribute("form") UploadForm form, ModelMap model){
		return "pacall/file_upload";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public String upload(UploadForm form){
		Map<String, Object> result = new HashMap<String, Object>();
		List<String> errors = new ArrayList<String>();
		result.put("folderId", form.getFolderId());
		result.put("errors", errors);
		Gson gson = new Gson();
		
		MultipartFile file = form.getFile();
		
		try {
			String fileName = file.getOriginalFilename();
			if(fileName == null) throw new Exception("File name is empty.");
			
			if(fileName.toUpperCase().endsWith("SENSOR.CSV")){
				//Upload Sensor CSV
				Map<String, Object> sensorCsvInfo = pacallUploadService.uploadSensorCsv(form, SecurityUserHolder.getCurrentUser().getId());
				result.put("folderId", sensorCsvInfo.get("folderId"));
			}else{
				//Upload Photos
				if(form.getFolderId()==null) throw new Exception("Folder ID is empty");
				String fileId = pacallUploadService.uploadPhoto(form, SecurityUserHolder.getCurrentUser().getId());
				result.put("folderId", form.getFolderId());
				result.put("fileId", fileId);
			}
		} catch (Exception e) {
			errors.add(e.getMessage());
		}
		return gson.toJson(result);
	}
}
