package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;
import java.util.Set;

//import jp.ac.tokushima_u.is.ll.dto.CategoryDTO;
import jp.ac.tokushima_u.is.ll.entity.Category;

import org.apache.ibatis.annotations.Param;

public interface CategoryDao {

	void insert(Category category);
	int delete(String id);
	int findCountItemsInCategory(String id);

	Category findById(String id);
//	List<CategoryDTO> findAllRoots();
	List<Category> findListByNameAndParent(@Param("name") String name, @Param("parentId") String parentId);
	Set<Category> findListByParentId(String id);
	List<Category> findListByUserId(String userId);
	void insertUsersMyCategoryList(@Param("userId")String userId, @Param("categoryId")String categoryId);
	void deleteAllUsersMyCategoryListByUserId(String userId);
//	List<CategoryDTO> findListDTOByParent(String id);
//	List<CategoryDTO> findListDTOByUserId(String userId);
//	CategoryDTO findDTOById(String id);
}
