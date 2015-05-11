package jp.ac.tokushima_u.is.ll.dto;

import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.Category;

public class CategoryDTO extends Category{

	private static final long serialVersionUID = 1626868373931255897L;
	
	private List<CategoryDTO> children;

//	public List<CategoryDTO> getChildren() {
//		return children;
//	}

	public void setChildren(List<CategoryDTO> children) {
		this.children = children;
	}
}
