package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.entity.Item;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

public interface ItemRelationDao {

	/**
	 * 
	 * @param name
	 * @param page
	 * @param perpage
	 * @param deep
	 * @param mode
	 * @param itemId
	 * @return Map{item:, synset:, synset0:, dist:}
	 */
	List<Map<String, Object>> findListRelatedItemRAW(@Param("name")String name, @Param("page")int page,
			@Param("perpage")int perpage, @Param("deep")int deep, @Param("mode")String mode, @Param("id")String itemId);

	/**
	 * 
	 * @param itemId
	 * @param pageRequest
	 * @return Map{item:, dist:}
	 */
	List<Map<String, Object>> findListSimilarTimeRAW(@Param("id")String itemId, @Param("pageRequest")Pageable pageRequest);

	/**
	 * 
	 * @param id
	 * @param pageRequest
	 * @return Map{item:, sum:}
	 */
	List<Map<String, Object>> findListSimilarImageByItersecRAW(@Param("id")String id,
			@Param("pageRequest")Pageable pageRequest);

	List<Item> findListNotRegisterredItems();

}
