package jp.ac.tokushima_u.is.ll.service.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.FileData;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.service.LuceneIndexService;

import org.apache.http.client.ClientProtocolException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("imageIndexCreateTask")
@Transactional(readOnly=true)
public class ImageIndexCreateTask {
	
	private HibernateDao<Item, String> itemDao;
	
	@Autowired
	private LuceneIndexService luceneIndexService;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		itemDao = new HibernateDao<Item, String>(sessionFactory, Item.class);
	}
	
	public boolean execute() throws ClientProtocolException, IOException{
		List<Item> itemList = itemDao.find("select item from Item item join item.image i where i.fileType=?", "image");
		List<FileData> dataList = new ArrayList<FileData>();
		for(Item item:itemList){
			FileData data = item.getImage();
			dataList.add(data);
		}
		luceneIndexService.addFiledataToIndex(dataList);
		return true;
	}
}
