package jp.ac.tokushima_u.is.ll.service;

import java.io.InputStream;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.DB;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

@Service
public class StorageService {
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public String storeOneFile(InputStream is, String filename, String contentType){
		DB db = mongoTemplate.getDb();
		db.requestStart();
		GridFSInputFile gf = new GridFS(db).createFile(is);
		gf.setContentType(contentType);
		gf.setFilename(filename);
		gf.save();
		db.requestDone();
		return gf.getId().toString();
	}
	
	public GridFSDBFile findOneFile(String id){
		DB db = mongoTemplate.getDb();
		GridFSDBFile file = new GridFS(db).findOne(new ObjectId(id));
		return file;
	}
}