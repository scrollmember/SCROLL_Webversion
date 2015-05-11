package jp.ac.tokushima_u.is.ll.service;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Vstar
 */
@Service
public class StaticServerService {
	private static Logger logger = LoggerFactory.getLogger(StaticServerService.class);

//	private StaticFileUploadService staticFileUploadService;
	
	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private PropertyService propertyService;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
	}
	
	public void uploadFile(final String fileId, final String extName, final byte[] file){
		if(StringUtils.isBlank(fileId) || StringUtils.isBlank(extName) || file==null || file.length<=0){
			logger.error("Error When Upload File: fileId"+fileId+",extName"+extName+", file length:" + file);
			return;
		}
		jmsTemplate.send(propertyService.getJmsQueueNameUploadFile(), new MessageCreator(){
			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage  msg = session.createMapMessage();
				msg.setString("projectName", propertyService.getProjectName());
				msg.setString("fileId", fileId);
				msg.setString("extName", extName);
				msg.setBytes("file", file);
				return msg;
			}
		});
	}
//
//	@Deprecated
//	public boolean uploadImage(String fileName, String extName, byte[] file) {
//		if (StringUtils.isBlank(fileName) || file == null || file.length == 0) {
//			return false;
//		}
//		if (StringUtils.isBlank(extName)) {
//			extName = "";
//		}
//		StaticFileUploadModel fileModel = new StaticFileUploadModel();
//		fileModel.setFileName(fileName);
//		fileModel.setExtName(extName);
//		fileModel.setFile(file);
//		staticFileUploadService.uploadImage(fileModel, propertyService.getProjectName());
//		return true;
//	}
//
//	@Deprecated
//	public boolean uploadImage(String fileName, String extName,
//			MultipartFile file)  {
//
//		try {
//			if (StringUtils.isBlank(fileName) || file == null || file.isEmpty()) {
//				return false;
//			}
//			if (StringUtils.isBlank(extName)) {
//				extName = "";
//			}
//			HttpClient client = new DefaultHttpClient();
//			HttpPost httpPost = new HttpPost(propertyService.getStaticImageUri());
//			MultipartEntity entity = new MultipartEntity();
//			entity.addPart("projectName", new StringBody(propertyService.getProjectName()));
//			entity.addPart("fileName", new StringBody(fileName));
//			entity.addPart("extName", new StringBody(extName));
//			entity.addPart("file", new ByteBody(file.getBytes(), file.getOriginalFilename()));
//			httpPost.setEntity(entity);
//			HttpResponse response = client.execute(httpPost);
//			HttpEntity resEntity = response.getEntity();
//			response.getStatusLine();
//			if (resEntity != null) {
//				resEntity.consumeContent();
//			}
//			client.getConnectionManager().shutdown();
//			
//			if(200 == response.getStatusLine().getStatusCode()){
//				return true;
//			}else{
//				return false;
//			}
//		} catch (Exception e) {
//			logger.error("Error when upload entity", e);
//			return false;
//		}
//	}
//
//	@Deprecated
//	public String getImageFileURL(String id, Integer imagelevel) {
//		String append = "";
//		if (Constants.NormalLevel.equals(imagelevel))
//			append = "_800x600.png";
//		else if (Constants.SmartPhoneLevel.equals(imagelevel))
//			append = "_320x240.png";
//		else if (Constants.MailLevel.equals(imagelevel))
//			append = "_160x120.png";
//		else if (Constants.IconLevel.equals(imagelevel))
//			append = "_80x60.png";
//
//		return propertyService.getStaticserverImageUrl() + id + append;
//	}
//
//	@Deprecated
//	public boolean isImageFileExist(String id, Integer imagelevel) {
//		try {
//			byte[] data = this.getImageFileById(id, imagelevel);
//			if (data != null && data.length > 0)
//				return true;
//		} catch (Exception e) {
//
//		}
//		return false;
//	}
//	
//	@Deprecated
//	public byte[] getImageFileById(String id, Integer imagelevel) {
//		String append = "";
//		if (Constants.NormalLevel.equals(imagelevel))
//			append = "_800x600.png";
//		else if (Constants.SmartPhoneLevel.equals(imagelevel))
//			append = "_320x240.png";
//		else if (Constants.MailLevel.equals(imagelevel))
//			append = "_160x120.png";
//		else if (Constants.IconLevel.equals(imagelevel))
//			append = "_80x60.png";
//
//		DataInputStream in = null;
//		String photoUrl = propertyService.getStaticserverImageUrl() + id + append;
//		try {
//			URL url = new URL(photoUrl);
//			HttpURLConnection connection = (HttpURLConnection) url
//					.openConnection();
//			in = new DataInputStream(connection.getInputStream());
//			ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
//			byte[] buffer = new byte[2048];
//			int count = 0;
//			while ((count = in.read(buffer)) > 0) {
//				out.write(buffer, 0, count);
//			}
//			in.close();
//			out.close();
//			return out.toByteArray();
//		} catch (Exception e) {
//
//		} finally {
//			try {
//				if (in != null)
//					in.close();
//			} catch (Exception e) {
//			}
//		}
//		return null;
//	}
//
//	@Deprecated
//	public boolean uploadVideo(String fileName, String extName, byte[] file) {
//		if (StringUtils.isBlank(fileName) || file == null || file.length == 0) {
//			return false;
//		}
//		if (StringUtils.isBlank(extName)) {
//			extName = "";
//		}
//		StaticFileUploadModel fileModel = new StaticFileUploadModel();
//		fileModel.setFileName(fileName);
//		fileModel.setExtName(extName);
//		fileModel.setFile(file);
//		staticFileUploadService.uploadVideo(fileModel, propertyService.getProjectName());
//		return true;
//	}
//
//	@Deprecated
//	public boolean uploadVideo(String fileName, String extName,
//			MultipartFile file)  {
//
//		try {
//			if (StringUtils.isBlank(fileName) || file == null || file.isEmpty()) {
//				return false;
//			}
//			if (StringUtils.isBlank(extName)) {
//				extName = "";
//			}
//			HttpClient client = new DefaultHttpClient();
//			HttpPost httpPost = new HttpPost(propertyService.getStaticImageUri());
//			MultipartEntity entity = new MultipartEntity();
//			entity.addPart("projectName", new StringBody(propertyService.getProjectName()));
//			entity.addPart("fileName", new StringBody(fileName));
//			entity.addPart("extName", new StringBody(extName));
//			entity.addPart("file", new ByteBody(file.getBytes(), file.getOriginalFilename()));
//			httpPost.setEntity(entity);
//			HttpResponse response = client.execute(httpPost);
//			HttpEntity resEntity = response.getEntity();
//			response.getStatusLine();
//			if (resEntity != null) {
//				resEntity.consumeContent();
//			}
//			client.getConnectionManager().shutdown();
//			
//			if(200 == response.getStatusLine().getStatusCode()){
//				return true;
//			}else{
//				return false;
//			}
//		} catch (Exception e) {
//			logger.error("Error when upload entity", e);
//			return false;
//		}
//	}
}
