package jp.ac.tokushima_u.is.ll.service.pacall;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.pacall.SenseData;
import jp.ac.tokushima_u.is.ll.mapper.pacall.SensedataMapper;
import jp.ac.tokushima_u.is.ll.service.PropertyService;
import jp.ac.tokushima_u.is.ll.util.FileFinder;
import jp.ac.tokushima_u.is.ll.util.Utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class SensedataService {
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm:ss");

	private static final int KEYWORD_COL = 0;
	private static final int DATE_COL = 1;
	private static final int VAL1_COL = 2;
	private static final int VAL2_COL = 3;
	private static final int VAL3_COL = 4;
	private static final int VAL4_COL = 5;
	
	@Autowired
	private PropertyService propertyService;
	@Autowired
	private SensedataMapper sensedataMapper;
	@Autowired
	private FolderService folderService;

	public List<SenseData> uploadSenseCsvToDatabase(String folderId, MultipartFile file){
		List<SenseData> dataList = new ArrayList<SenseData>();

		// Save Sensor Data
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
			String line = "";
			int i = 0;
			while ((line = reader.readLine()) != null) {
				String[] values = line.split(",");
				if (values == null || values.length <= 1)
					continue;

				SenseData senseData = new SenseData();
				senseData.setId(Utility.getUuidKey());
				senseData.setIndex(i++);
				senseData.setKey(values[KEYWORD_COL]);
				senseData.setDate(dateFormat.parse(values[DATE_COL]));
				senseData.setFolderId(folderId);
				if (values.length > 2)
					senseData.setV1(values[VAL1_COL]);
				if (values.length > 3)
					senseData.setV2(values[VAL2_COL]);
				if (values.length > 4)
					senseData.setV3(values[VAL3_COL]);
				if (values.length > 5)
					senseData.setV4(values[VAL4_COL]);
				dataList.add(senseData);
			}
			//for(SenseData senseData: dataList){
			//	sensedataMapper.insert(senseData);
			//}
			return dataList;
		} catch (Exception e) {
			throw new RuntimeException("Error when processing SENSOR.CSV", e);
		} 
	}
	
	@Deprecated
	public List<SenseData> addSenseCsvToDatabse(String folderId) throws RuntimeException{
		FileFinder finder = new FileFinder();
		List<File> files = finder.find(new File(propertyService.findPacallStaticDir(), folderId), "SENSOR.CSV");
		File csv = files.get(0);
		Date lastDate = new Date(0l);
		
		List<SenseData> dataList = new ArrayList<SenseData>();

		// Save Sensor Data
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(csv));
			String line = "";
			int i = 0;
			while ((line = reader.readLine()) != null) {
				String[] values = line.split(",");
				if (values == null || values.length <= 1)
					continue;

				SenseData senseData = new SenseData();
				senseData.setId(Utility.getUuidKey());
				senseData.setIndex(i++);
				senseData.setKey(values[KEYWORD_COL]);
				senseData.setDate(dateFormat.parse(values[DATE_COL]));
				if(lastDate.before(senseData.getDate())){
					lastDate = new Date(senseData.getDate().getTime());
				}
				senseData.setFolderId(folderId);
				if (values.length > 2)
					senseData.setV1(values[VAL1_COL]);
				if (values.length > 3)
					senseData.setV2(values[VAL2_COL]);
				if (values.length > 4)
					senseData.setV3(values[VAL3_COL]);
				if (values.length > 5)
					senseData.setV4(values[VAL4_COL]);
				dataList.add(senseData);
			}
			for(SenseData senseData: dataList){
//				TODO Using JMS 
//				sensedataMapper.insert(senseData);
			}
			if(lastDate!=null && !lastDate.equals(new Date(0l))){
				folderService.updateLastModified(folderId, lastDate);
			}
			return dataList;
		} catch (Exception e) {
			throw new RuntimeException("Error when processing SENSOR.CSV", e);
		} 
		
	}
}
