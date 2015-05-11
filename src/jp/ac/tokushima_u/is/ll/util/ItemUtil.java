package jp.ac.tokushima_u.is.ll.util;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.ItemTitle;

public class ItemUtil {

	public static boolean itemRealted(Item item1, Item item2){
		item2.getTitles();
		for(ItemTitle it1 :item1.getTitles()){
			for(ItemTitle it2:item2.getTitles()){
				if(it1.getLanguage().equals(it2.getLanguage())&&it1.getContent().equals(it2.getContent()))
					return true;
			}
		}
		return false;
	}
	
	public static boolean inItemList(Item item, List<Item>items){
		for(Item i:items){
			if(itemRealted(i, item))
				return true;
		}
		return false;
	}
	
	
	public static String getItemNote(Item item){
		String note = "";
		for(ItemTitle t:item.getTitles()){
			note+=t.getLanguage().getName()+":"+t.getContent()+"  ";
		}
		return note;
	}
}
