package jp.ac.tokushima_u.is.ll.service.task;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.FileBin;
import jp.ac.tokushima_u.is.ll.entity.FileData;
import jp.ac.tokushima_u.is.ll.service.StaticServerService;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Houbin
 */
@Service("picBackupService")
@Transactional(readOnly = true)
public class PicBackupService {

	private HibernateDao<FileBin, String> fileBinDao;
	private HibernateDao<FileData, String> fileDataDao;
	private String filePath;
	
	@Autowired
	private StaticServerService staticServerService;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		fileBinDao = new HibernateDao<FileBin, String>(sessionFactory,
				FileBin.class);
		fileDataDao = new HibernateDao<FileData, String>(sessionFactory, FileData.class);
	}

	public HibernateDao<FileBin, String> getFileBinDao() {
		return fileBinDao;
	}

	public void setFileBinDao(HibernateDao<FileBin, String> fileBinDao) {
		this.fileBinDao = fileBinDao;
	}

	public String getFilePath() {
		return filePath;
	}
	
	

	@Value("${system.staticFileDir}")
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public boolean execute() throws Exception {
//		String origFilePath = filePath + "/image/orig/";
//		File path = new File(origFilePath);
//		if (!path.exists()) {
//			path.mkdirs();
//		}
		List<FileData> fileDataList = fileDataDao.getAll();
		for (FileData fileData : fileDataList){
//			FileBin fileBin = fileData.getFileBin();
//			if(fileBin == null || fileBin.getBin()==null || fileBin.getBin().length==0)continue;
//			
//			staticServerService.uploadFile(fileData.getId(), StringUtils.lowerCase(FilenameUtils.getExtension(fileData.getOrigName())), fileBin.getBin());
//			
			/*
			String fileName = fileData.getOrigName();
			if (fileName == null) {
				continue;
			}
			fileName = fileName.toLowerCase();
			if (!fileName.contains(".")) {
				continue;
			}
			fileName = fileData.getId();
			File file = new File(origFilePath + fileName);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			byte[] buffer = fileBin.getBin();
			for (int i = 0; i < buffer.length; i++) {
				out.write(buffer[i]);
			}
			out.flush();
			out.close();
			String autoext = getFormatNames(file);
			if (autoext == null) {
				autoext = "";
			}
			autoext = autoext.toLowerCase();
			if (autoext.equals("jpeg")) {
				autoext = "jpg";
			}
			File renameToFile = new File(origFilePath + fileName + "."
					+ autoext);
			if (renameToFile.exists()) {
				renameToFile.delete();
			}
			file.renameTo(renameToFile);

			File standardFile = new File(filePath + "/image/"
					+ fileData.getId() + "_800x600." + "png");
			File thumbnailFile = new File(filePath + "/image/"
					+ fileData.getId() + "_320x240." + "png");
			File smallFile = new File(filePath + "/image/" + fileData.getId()
					+ "_160x120." + "png");
			File iconFile = new File(filePath + "/image/" + fileData.getId()
					+ "_80x60." + "png");
			ImageZoomUtil.zoomImage(renameToFile, standardFile, 600, 800, true);
			ImageZoomUtil
					.zoomImage(renameToFile, thumbnailFile, 240, 320, true);
			ImageZoomUtil.zoomImage(renameToFile, smallFile, 120, 160, true);
			ImageZoomUtil.zoomImage(renameToFile, iconFile, 60, 80, true);
			*/
		}
		return true;
	}

	@SuppressWarnings("unused")
	private static String getFormatNames(Object o) {
		try {
			ImageInputStream iis = ImageIO.createImageInputStream(o);
			Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
			if (!iter.hasNext()) {
				return null;
			}
			ImageReader reader = (ImageReader) iter.next();
			iis.close();
			return reader.getFormatName();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
