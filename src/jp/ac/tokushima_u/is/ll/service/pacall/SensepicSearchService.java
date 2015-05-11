package jp.ac.tokushima_u.is.ll.service.pacall;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.pacall.SensePic;
import jp.ac.tokushima_u.is.ll.service.ItemService;
import jp.ac.tokushima_u.is.ll.service.LuceneIndexService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class SensepicSearchService {
	@Autowired
	private LuceneIndexService luceneIndexService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private ShowpicService showpicService;
	
	public LinkedHashMap<Item, Float> searchBySensePic(SensePic pic) {
		LinkedHashMap<Item, Float> result = new LinkedHashMap<Item, Float>();
		try {
			File picFile = showpicService.prepareFile(pic.getId(), 240, 180);
			if(!picFile.exists())return result;
			LinkedHashMap<String, Float> imageResult =  luceneIndexService.searchByImage(new FileInputStream(picFile));
			for(String filedataId: imageResult.keySet()){
				List<Item> itemList = itemService.findItemListByFiledataId(filedataId);
				for(Item i: itemList){
					result.put(i, imageResult.get(filedataId));
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		} 
	}
}
