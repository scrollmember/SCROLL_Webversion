package jp.ac.tokushima_u.is.ll.service.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.FileData;
import jp.ac.tokushima_u.is.ll.service.PropertyService;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("filedataHashTaskService")
@Transactional
public class FiledataHashTaskService {
	private Logger logger = LoggerFactory.getLogger(FiledataHashTaskService.class);
	
	private HibernateDao<FileData, String> fileDataDao;
	
	
	@Autowired
	private PropertyService propertyService;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		fileDataDao = new HibernateDao<FileData, String>(sessionFactory, FileData.class);
	}
	
	public void run(){
		List<FileData> list = fileDataDao.getAll();
		for(FileData data: list){
			//Get extension
			String origname = data.getOrigName();
			if(StringUtils.isBlank(origname))continue;
			String filename;
			try {
				filename = data.getId() + ext(origname);
			} catch (Exception e) {
				logger.error("FiledataHashTaskService: orignameerror"+origname);
				continue;
			}
			String fullname = "/home/learnadmin/www/static/"+propertyService.getProjectName()+"/orig/"+filename;
			try {
				data.setMd5(DigestUtils.md5Hex(new FileInputStream(new File(fullname))));
				fileDataDao.save(data);
			} catch (FileNotFoundException e) {
				logger.error("FiledataHashTaskService: filenotfound:"+data.getId());
			} catch (IOException e) {
				logger.error("FiledataHashTaskService: io exception:"+data.getId());
			}
		}
	}

	private String ext(String name) {
		return name.substring(name.lastIndexOf("."));
	}
}
