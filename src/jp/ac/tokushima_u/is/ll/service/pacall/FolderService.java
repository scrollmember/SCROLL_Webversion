package jp.ac.tokushima_u.is.ll.service.pacall;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.pacall.Folder;
import jp.ac.tokushima_u.is.ll.entity.pacall.SenseData;
import jp.ac.tokushima_u.is.ll.mapper.pacall.FolderMapper;
import jp.ac.tokushima_u.is.ll.mapper.pacall.SensedataMapper;
import jp.ac.tokushima_u.is.ll.service.PropertyService;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FolderService {
	
	@Autowired
	private PropertyService propertyService;
	
	@Autowired
	private FolderMapper folderMapper;
	
	@Autowired
	private SensedataMapper sensedataMapper;

	@Transactional(readOnly = true)
	public List<Folder> findAll() {
		return folderMapper.selectAll();
	}

	public void deleteByKey(String name) {
		folderMapper.deleteByKey(name);
	}

	public void insert(Folder f) {
		Folder folder = folderMapper.selectByKey(f.getName());
		if(folder==null){
			folderMapper.insert(f);
		}
	}
	
	public void createFolder(Folder folder){
		folderMapper.insert(folder);
	}

	public void updateLastModified(String folderId, Date lastModified) {
		folderMapper.updateLastModified(folderId, lastModified);
	}

	public Folder findByHash(String hash) {
		return folderMapper.findByHash(hash);
	}

	public void removeFolder(Folder folder) {
		int count = folderMapper.deleteById(folder.getId());
		if(count > 0){
			File file = new File(propertyService.findPacallStaticDir(), folder.getId());
			if(file.exists()){
				try {
					FileUtils.deleteDirectory(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Transactional(readOnly=true)
	public List<Folder> selectByUserid(String id) {
		return folderMapper.selectByUserId(id);
	}

	public Folder findById(String id) {
		return folderMapper.selectById(id);
	}

	public void updateStartEndDate(String id, List<SenseData> sensedataList) {
		Date start = sensedataList.get(0).getDate();
		Date end = sensedataList.get(sensedataList.size()-1).getDate();
		//Date start = sensedataMapper.findFirstDataByFolder(id);
		//Date end = sensedataMapper.findLastDataByFolder(id);
		
		folderMapper.updateStartEndDate(id, start, end);
	}
}
