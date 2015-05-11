package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.ItemTitle;
import jp.ac.tokushima_u.is.ll.entity.ItemTitleBatis;
import jp.ac.tokushima_u.is.ll.entity.Language;
import jp.ac.tokushima_u.is.ll.entity.NetworkAnalysis;


public interface ItemTitleDao {

	List<ItemTitle> findListByItem(String itemId);

	void insert(ItemTitle title);

	void deleteAllByItemId(String itemId);

	List<ItemTitle> findListByItemAndLanguage(@Param("item")String itemId, @Param("language")String languageId);

	List<ItemTitle> findListByItemAndInLanguages(@Param("item")String itemId,
			@Param("langs")List<Language> langs);

	List<ItemTitle> findsearchRelatedItemContent(@Param("taskId")String taskId, @Param("querystr")String querystr, @Param("limit")Integer limit);

	List<ItemTitle> findContentItem(@Param("taskId")String taskId);
	//network second layer
	List<ItemTitleBatis> findsecondlayer();

	List<NetworkAnalysis> findedge();
}
