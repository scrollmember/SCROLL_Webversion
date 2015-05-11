package jp.ac.tokushima_u.is.ll.ws.service;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.Language;
import jp.ac.tokushima_u.is.ll.ws.service.model.ItemForm;
import jp.ac.tokushima_u.is.ll.ws.service.model.ItemModel;

public interface ItemWebService {
	public Integer insertItem(ItemForm form) throws Exception;
    List<ItemModel> findItemByLocation(String userEmail, String password, Double lat, Double lng) throws Exception;
    List<ItemModel> findItemByLocation(String userEmail, String password, Double lat, Double lng, Boolean onlyForUser) throws Exception;
    List<ItemModel> findItemByLocationAndStudyLan(Double lat, Double lng, List<Language> lanset) throws Exception;
    List<ItemModel> findItemByLocationAndStudyLan(Double lat, Double lng, List<Language> lanset, String userId, Boolean onlyForUser) throws Exception;
}
