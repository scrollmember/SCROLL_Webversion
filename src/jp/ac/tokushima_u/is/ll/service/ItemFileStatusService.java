package jp.ac.tokushima_u.is.ll.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.mapper.ItemMapper;
import jp.ac.tokushima_u.is.ll.util.FilenameUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("itemFileStatusService")
@Transactional(readOnly = true)
public class ItemFileStatusService {
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private PropertyService propertyService;
	@Autowired
	private StaticServerService staticServerService;
	
	public boolean repairItemFile(){
		List<Map<String, Object>> datalist =  findItemWhereFileHasErrors();
		for(Map<String, Object> obj: datalist){
			
//			String item_id = (String)obj.get("item_id");
			String file_id = (String)obj.get("file_id");
			String file_name = (String)obj.get("file_name");
			String filetype = FilenameUtil.checkMediaType(file_name);
			if(filetype == FilenameUtil.UNKNOWN){
				continue;
			}
			Boolean hasOrig = objectToBoolean(obj.get("hasOrig"));
			Boolean hasMp3 = objectToBoolean(obj.get("hasMp3"));
			Boolean hasOgg = objectToBoolean(obj.get("hasOgg"));
			Boolean hasMp4 = objectToBoolean(obj.get("hasMp4"));
			Boolean has80_60 = objectToBoolean(obj.get("has80_60"));
			Boolean has160_120 = objectToBoolean(obj.get("has160_120"));
			Boolean has320_240 = objectToBoolean(obj.get("has320_240"));
			Boolean has800_600 = objectToBoolean(obj.get("has800_600"));
			
			
			if(hasOrig!= null && hasOrig){
				String ext = StringUtils.lowerCase(FilenameUtils.getExtension(file_name));
				File dir = new File(this.getStaticFileDir(), "orig");
				if(!dir.exists())return false;
				File file = new File(dir, file_id+"."+ext);
				if(!file.exists()){
					file = new File(dir, file_id+"."+"mp3");
				}
				if(!file.exists()){
					file = new File(dir, file_id+"."+"ogg");
				}
				if(!file.exists()){
					file = new File(dir, file_id+"."+"mp4");
				}
				if(!file.exists()){
					file = new File(dir, file_id+"."+"png");
				}
				if(file.exists()){
					try {
						staticServerService.uploadFile(file_id, StringUtils.lowerCase(FilenameUtils.getExtension(file_name)), FileUtils.readFileToByteArray(file));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				continue;
			}else{
				if(FilenameUtil.AUDIO.equals(filetype)){
					if(hasMp3!=null && hasMp3){
						File file = new File(this.getStaticFileDir(), file_id+".mp3");
						if(file.exists()){
							try {
								staticServerService.uploadFile(file_id, "mp3", FileUtils.readFileToByteArray(file));
							} catch (IOException e) {
								e.printStackTrace();
							}
							continue;
						}
					}else if(hasOgg!=null && hasOgg){
						File file = new File(this.getStaticFileDir(), file_id+".ogg");
						if(file.exists()){
							try {
								staticServerService.uploadFile(file_id, "ogg", FileUtils.readFileToByteArray(file));
							} catch (IOException e) {
								e.printStackTrace();
							}
							continue;
						}
					}
				}
				
				if(FilenameUtil.VIDEO.equals(filetype)){
					if(hasMp4!=null && hasMp4){
						File file = new File(this.getStaticFileDir(), file_id+"_320x240.mp4");
						if(file.exists()){
							try {
								staticServerService.uploadFile(file_id, "mp4", FileUtils.readFileToByteArray(file));
							} catch (IOException e) {
								e.printStackTrace();
							}
							continue;
						}
					}
				}
				
				if(has800_600!=null && has800_600){
					File file = new File(this.getStaticFileDir(), file_id+"_800x600.png");
					if(file.exists()){
						try {
							staticServerService.uploadFile(file_id, "png", FileUtils.readFileToByteArray(file));
						} catch (IOException e) {
							e.printStackTrace();
						}
						continue;
					}
				}else if(has320_240!=null && has320_240){
					File file = new File(this.getStaticFileDir(), file_id+"_320x240.png");
					if(file.exists()){
						try {
							staticServerService.uploadFile(file_id, "png", FileUtils.readFileToByteArray(file));
						} catch (IOException e) {
							e.printStackTrace();
						}
						continue;
					}
				}else if(has160_120!=null && has160_120){
					File file = new File(this.getStaticFileDir(), file_id+"_160x120.png");
					if(file.exists()){
						try {
							staticServerService.uploadFile(file_id, "png", FileUtils.readFileToByteArray(file));
						} catch (IOException e) {
							e.printStackTrace();
						}
						continue;
					}
				}else if(has80_60!=null && has80_60){
					File file = new File(this.getStaticFileDir(), file_id+"_80x60.png");
					if(file.exists()){
						try {
							staticServerService.uploadFile(file_id, "png", FileUtils.readFileToByteArray(file));
						} catch (IOException e) {
							e.printStackTrace();
						}
						continue;
					}
				}
			}
		}
		return true;
	}
	
	private Boolean objectToBoolean(Object obj) {
		if(obj!=null && obj instanceof Boolean){
			return (Boolean)obj;
		}else{
			return null;
		}
	}

	public List<Map<String, Object>> findItemWhereFileHasErrors(){
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> data = itemMapper.selectItemWhereHasFile();
		for(Map<String, Object> obj: data){ 
//			String item_id = (String)obj.get("item_id");
			String file_id = (String)obj.get("file_id");
			String file_name = (String)obj.get("file_name");
			String filetype = FilenameUtil.checkMediaType(file_name);
			if(filetype == FilenameUtil.UNKNOWN){
				continue;
			}
			
			Boolean hasOrig = detectOrig(file_id, file_name);
			Boolean hasMp3 = detectMp3(file_id, file_name);
			Boolean hasOgg = detectOgg(file_id, file_name);
			Boolean hasMp4 = detectMp4(file_id, file_name);
			Boolean has80_60 = detect80_60(file_id, file_name);
			Boolean has160_120 = detect160_120(file_id, file_name);
			Boolean has320_240 = detect320_240(file_id, file_name);
			Boolean has800_600 = detect800_600(file_id, file_name);
			if(FilenameUtil.IMAGE.equals(filetype) && (hasOrig && has80_60 && has160_120 && has320_240 && has800_600)){
				continue;
			}
			if(FilenameUtil.AUDIO.equals(filetype) && (hasOrig && has80_60 && has160_120 && has320_240 && has800_600 && hasMp3 && hasOgg)){
				continue;
			}
			if(FilenameUtil.VIDEO.equals(filetype) && (hasOrig && has80_60 && has160_120 && has320_240 && has800_600 && hasMp4)){
				continue;
			}
			obj.put("hasOrig", hasOrig);
			obj.put("hasMp3", hasMp3);
			obj.put("hasOgg", hasOgg);
			obj.put("hasMp4", hasMp4);
			obj.put("has80_60",has80_60);
			obj.put("has160_120", has160_120);
			obj.put("has320_240", has320_240);
			obj.put("has800_600", has800_600);
			result.add(obj);
		}
		return result;
	}

	private Boolean detect800_600(String file_id, String file_name) {
		File file = new File(this.getStaticFileDir(), file_id+"_800x600.png");
		return file.exists();
	}

	private Boolean detect320_240(String file_id, String file_name) {
		File file = new File(this.getStaticFileDir(), file_id+"_320x240.png");
		return file.exists();
	}

	private Boolean detect160_120(String file_id, String file_name) {
		File file = new File(this.getStaticFileDir(), file_id+"_160x120.png");
		return file.exists();
	}

	private Boolean detect80_60(String file_id, String file_name) {
		File file = new File(this.getStaticFileDir(), file_id+"_80x60.png");
		return file.exists();
	}

	private Boolean detectMp4(String file_id, String file_name) {
		String filetype = FilenameUtil.checkMediaType(file_name);
		if(!FilenameUtil.VIDEO.equals(filetype)){
			return null;
		}
		File file = new File(this.getStaticFileDir(), file_id+"_320x240.mp4");
		return file.exists();
	}

	private Boolean detectOgg(String file_id, String file_name) {
		String filetype = FilenameUtil.checkMediaType(file_name);
		if(!FilenameUtil.AUDIO.equals(filetype)){
			return null;
		}
		File file = new File(this.getStaticFileDir(), file_id+".ogg");
		return file.exists();
	}

	private Boolean detectMp3(String file_id, String file_name) {
		String filetype = FilenameUtil.checkMediaType(file_name);
		if(!FilenameUtil.AUDIO.equals(filetype)){
			return null;
		}
		File file = new File(this.getStaticFileDir(), file_id+".mp3");
		return file.exists();
	}

	private Boolean detectOrig(String file_id, String file_name) {
		String ext = StringUtils.lowerCase(FilenameUtils.getExtension(file_name));
		File dir = new File(this.getStaticFileDir(), "orig");
		if(!dir.exists())return false;
		File file = new File(dir, file_id+"."+ext);
		return file.exists();
	}

	private File getStaticFileDir(){
		File fileDir = new File(propertyService.getStaticFileDir(), propertyService.getProjectName());
		return fileDir;
	}
}
