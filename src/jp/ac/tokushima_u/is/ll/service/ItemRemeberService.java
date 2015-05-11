package jp.ac.tokushima_u.is.ll.service;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.Item;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItemRemeberService {

	public List<Item> findToRemeberItem(){
		String hql_new = "from Item item where item.createTime=:yesterday and item.author=:author";
		
		
		return null;
	}
	
	
	
}
