package jp.ac.tokushima_u.is.ll.dao;

import jp.ac.tokushima_u.is.ll.entity.ItemRating;

import org.apache.ibatis.annotations.Param;

public interface ItemRatingDao {

	boolean findBooleanIfRatingExist(@Param("itemId")String itemId, @Param("userId")String userId);

	Double findDoubleRatingSum(String itemId);
	
	Double findDoubleRatingAvg(String itemId);

	Long findLongRatingCount(String itemId);

	void insert(ItemRating rating);
}
