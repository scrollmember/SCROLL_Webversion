package jp.ac.tokushima_u.is.ll.dto;

import java.util.ArrayList;
import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.ItemComment;
import jp.ac.tokushima_u.is.ll.entity.ItemTag;
import jp.ac.tokushima_u.is.ll.entity.ItemTitle;
import jp.ac.tokushima_u.is.ll.entity.QuestionType;
import jp.ac.tokushima_u.is.ll.entity.Users;

import org.apache.commons.lang.StringUtils;

public class ItemDTO extends Item {

	private static final long serialVersionUID = -8125081247916499239L;
	private Users author;
	private List<ItemTitleDTO> titles;
	private List<ItemTag> tags;
	private List<ItemComment> comments;
	private List<QuestionType> questionTypes;
	private ItemDTO relog;

	public Users getAuthor() {
		return author;
	}

	public void setAuthor(Users author) {
		this.author = author;
	}

//	public List<ItemTitleDTO> getTitles() {
//		return titles;
//	}
//
//	public void setTitles(List<ItemTitleDTO> titles) {
//		this.titles = titles;
//	}

	public List<ItemTag> getTags() {
		return tags;
	}

	public void setTags(List<ItemTag> tags) {
		this.tags = tags;
	}

	public ItemDTO getRelog() {
		return relog;
	}

	public void setRelog(ItemDTO relog) {
		this.relog = relog;
	}

	public List<ItemComment> getComments() {
		return comments;
	}

	public void setComments(List<ItemComment> comments) {
		this.comments = comments;
	}

//	public List<QuestionType> getQuestionTypes() {
//		return questionTypes;
//	}
//
//	public void setQuestionTypes(List<QuestionType> questionTypes) {
//		this.questionTypes = questionTypes;
//	}

//	public String getDefaultTitle() {
//		String defaultTitle = "";
//		if (this.titles == null || this.titles.size() == 0)
//			return defaultTitle;
//		List<String> titleList = new ArrayList<String>();
//		for(ItemTitle title:this.titles){
//			titleList.add(title.getContent());
//		}
//		return StringUtils.join(titleList, "|");
//	}
}
