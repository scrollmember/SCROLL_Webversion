package jp.ac.tokushima_u.is.ll.service;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Houbin
 * 
 */
@Service
public class PropertyService {
	private static Logger logger = LoggerFactory
			.getLogger(PropertyService.class);

	@Value("${system.projectName}")
	private String projectName = "learninglog_dev";

	@Value("${system.staticserverUrl}")
	private String staticserverUrl;
	
	@Value("${system.langrid_username}")
	private String langridUsername;
	
	@Value("${system.langrid_password}")
	private String langridPassword;

	@Value("${url.apk_learninglog}")
	private String urlApkLearningLog;

	@Value("${url.apk_navigator}")
	private String urlApkNavigator;

	@Value("${system.mail.systemMailAddress}")
	private String systemMailAddress;

	@Value("${system.url}")
	private String systemUrl;

	@Value("${system.send_weekly_flag}")
	private Boolean sendWeeklyFlag;

	@Value("${lucene.index_path}")
	private String luceneIndexPath;
	
	@Value("${system.staticFileDir}")
	private String staticFileDir;

	@Value("${system.staticserverImageUrl}")
	private String staticserverImageUrl;

	@Value("${staticserver.file_uri}")
	private String staticImageUri;

	@Value("${pacall.tmp_dir}")
	private String pacallTmpDir;
	
	@Value("${jms.queue.name.uploadfile}")
	private String jmsQueueNameUploadFile;

	@Value("${jms.queue.name.sendMail}")
	private String jmsQueueNameSendMail;
	
	@Value("${google.api}")
	private String googleApi;
	@Value("${microsoft.api}")
	private String microsoftApi;
	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getStaticserverUrl() {
		return staticserverUrl;
	}

	public void setStaticserverUrl(String staticserverUrl) {
		this.staticserverUrl = staticserverUrl;
	}

	public String getUrlApkLearningLog() {
		return urlApkLearningLog;
	}

	public void setUrlApkLearningLog(String urlApkLearningLog) {
		this.urlApkLearningLog = urlApkLearningLog;
	}

	public String getUrlApkNavigator() {
		return urlApkNavigator;
	}

	public void setUrlApkNavigator(String urlApkNavigator) {
		this.urlApkNavigator = urlApkNavigator;
	}

	public String getSystemMailAddress() {
		return systemMailAddress;
	}

	public void setSystemMailAddress(String systemMailAddress) {
		this.systemMailAddress = systemMailAddress;
	}

	public String getSystemUrl() {
		return systemUrl;
	}

	public void setSystemUrl(String systemUrl) {
		this.systemUrl = systemUrl;
	}

	public Boolean getSendWeeklyFlag() {
		return sendWeeklyFlag;
	}

	public void setSendWeeklyFlag(Boolean sendWeeklyFlag) {
		this.sendWeeklyFlag = sendWeeklyFlag;
	}

	public String getLuceneIndexPath() {
		try {
			return new File(luceneIndexPath, projectName).getCanonicalPath();
		} catch (IOException e) {
			logger.error("Path Error", e);
			if (!luceneIndexPath.endsWith("/")) {
				luceneIndexPath += "/";
			}
			return luceneIndexPath + projectName;
		}
	}

	public String getStaticImageUri() {
		return staticImageUri;
	}

	public void setStaticImageUri(String staticImageUri) {
		this.staticImageUri = staticImageUri;
	}

	public String getStaticserverImageUrl() {
		return staticserverImageUrl;
	}

	public void setStaticserverImageUrl(String staticserverImageUrl) {
		this.staticserverImageUrl = staticserverImageUrl;
	}

	public String getPacallTmpDir() {
		return pacallTmpDir;
	}

	public void setPacallTmpDir(String pacallTmpDir) {
		this.pacallTmpDir = pacallTmpDir;
	}

	public String getStaticFileDir() {
		return staticFileDir;
	}

	public void setStaticFileDir(String staticFileDir) {
		this.staticFileDir = staticFileDir;
	}
	
	public String findPacallStaticDir() {
		return this.staticFileDir+"/pacall/"+this.projectName;
	}

	public String getJmsQueueNameUploadFile() {
		return jmsQueueNameUploadFile;
	}

	public void setJmsQueueNameUploadFile(String jmsQueueNameUploadFile) {
		this.jmsQueueNameUploadFile = jmsQueueNameUploadFile;
	}

	public String getJmsQueueNameSendMail() {
		return jmsQueueNameSendMail;
	}

	public void setJmsQueueNameSendMail(String jmsQueueNameSendMail) {
		this.jmsQueueNameSendMail = jmsQueueNameSendMail;
	}

	public String getGoogleApi() {
		return googleApi;
	}

	public void setGoogleApi(String googleApi) {
		this.googleApi = googleApi;
	}

	public String getMicrosoftApi() {
		return microsoftApi;
	}

	public void setMicrosoftApi(String microsoftApi) {
		this.microsoftApi = microsoftApi;
	}

	public String getLangridUsername() {
		return langridUsername;
	}

	public void setLangridUsername(String langridUsername) {
		this.langridUsername = langridUsername;
	}

	public String getLangridPassword() {
		return langridPassword;
	}

	public void setLangridPassword(String langridPassword) {
		this.langridPassword = langridPassword;
	}
}
