package jp.ac.tokushima_u.is.ll.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.FileData;
import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.DocumentBuilderFactory;
import net.semanticmetadata.lire.ImageSearchHits;
import net.semanticmetadata.lire.ImageSearcher;
import net.semanticmetadata.lire.ImageSearcherFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LuceneIndexService {
	
	@Autowired
	private PropertyService propertyService = new PropertyService();

	private void addImageToIndex(InputStream is, String id, DocumentBuilder builder, IndexWriter iw) throws IOException {
		Document doc = builder.createDocument(is, id);
		iw.updateDocument(new Term(DocumentBuilder.FIELD_NAME_IDENTIFIER, id), doc);
	}

	public void addFiledataToIndex(List<FileData> fileDataList) throws IOException {
		DocumentBuilder builder = DocumentBuilderFactory.getJCDDocumentBuilder();
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_34, new SimpleAnalyzer(Version.LUCENE_34));
		IndexWriter iw = new IndexWriter(FSDirectory.open(new File(propertyService.getLuceneIndexPath())), config);
		for(FileData fileData:fileDataList){
			String url = propertyService.getStaticserverUrl()+"/"+propertyService.getProjectName()+"/"+fileData.getId()+"_800x600.png";
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = client.execute(httpGet);
			if(HttpStatus.SC_OK==response.getStatusLine().getStatusCode()){  
				HttpEntity entity = response.getEntity();
				if (entity != null) {  
					InputStream input = new BufferedInputStream(entity.getContent());
					addImageToIndex(input, fileData.getId(), builder, iw);
		            input.close();
				}
			}
		}
		iw.optimize();
		iw.close();
	}
	
	public LinkedHashMap<String, Float> searchByImage(InputStream is) throws IOException{
		IndexReader reader = IndexReader.open(FSDirectory.open(new File(propertyService.getLuceneIndexPath())));
		ImageSearcher searcher = ImageSearcherFactory.createJCDImageSearcher(10);
		ImageSearchHits hits = null;
		hits = searcher.search(is, reader);
		LinkedHashMap<String, Float> result = new LinkedHashMap<String, Float>();
		for (int i = 0; i < hits.length(); i++) {
			if(hits.score(i)>0.25){
				result.put(hits.doc(i).getFieldable(DocumentBuilder.FIELD_NAME_IDENTIFIER).stringValue(), hits.score(i));
			}
        }
		return result;
	}
	
	public void searchByImage() throws IOException{
		IndexReader reader = IndexReader.open(FSDirectory.open(new File(propertyService.getLuceneIndexPath())));
		ImageSearcher searcher = ImageSearcherFactory.createDefaultSearcher();
		FileInputStream imageStream = new FileInputStream("D:/User/Documents/Vicon Revue Data/AE9C5623-0C4F-A7EC-AA98-C8470FC1C439/B000075f00000032f20110822104833F.JPG");
		ImageSearchHits hits = null;
		hits = searcher.search(imageStream, reader);
		for (int i = 0; i < hits.length(); i++) {
            System.out.println(hits.score(i) + ": " + 
				hits.doc(i).getFieldable(DocumentBuilder.FIELD_NAME_IDENTIFIER).stringValue());
        }
	}
	
	public static void main(String[] args){
		RemoteCacheManager remoteCacheManager = new RemoteCacheManager("ll.is.tokushima-u.ac.jp");
		
		remoteCacheManager.start();
		try {
			RemoteCache<String, String> cache = remoteCacheManager.getCache("lucene");
			cache.put("name", "Hello World");
			System.out.println(cache.get("name"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			remoteCacheManager.stop();
		}
		
//		LuceneIndexService service = new LuceneIndexService();
//		try {
//			service.startCreateIndex();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}
