package jp.ac.tokushima_u.is.ll.service.pacall;

import java.io.File;
import java.io.IOException;

import jp.ac.tokushima_u.is.ll.entity.pacall.SensePic;
import jp.ac.tokushima_u.is.ll.service.StorageService;
import net.coobird.thumbnailator.Thumbnails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.gridfs.GridFSDBFile;

@Service
public class ShowpicService {
	
	@Autowired
	private SensepicService sensepicService;
	
	@Autowired
	private StorageService storageService;

	public File prepareFile(String sensepicId, int width, int height) throws IOException {
		SensePic sensepic = sensepicService.findById(sensepicId);
		if(sensepic == null){
			return null;
		}
		String cachefilename = sensepic.getFileId()+"_"+width+"_"+height+".jpg";
		File dir = new File(System.getProperty("java.io.tmpdir"), sensepic.getFolderId());
		File outputFile = new File(dir, cachefilename);
		if(!outputFile.exists() || outputFile.length() == 0){
			if(!outputFile.getParentFile().exists()){
				outputFile.getParentFile().mkdirs();
			}
			GridFSDBFile file = storageService.findOneFile(sensepic.getFileId());
			if(file==null)return null;
			Thumbnails.of(file.getInputStream()).size(width, height).outputFormat("jpg").toFile(outputFile);
			file = null;
		}
		return outputFile;
	}

}
