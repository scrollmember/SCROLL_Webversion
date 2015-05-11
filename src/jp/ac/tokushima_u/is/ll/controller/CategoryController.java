package jp.ac.tokushima_u.is.ll.controller;

import java.util.ArrayList;
import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.Category;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.form.CategoryEditForm;
import jp.ac.tokushima_u.is.ll.form.validator.CategoryEditFormValidator;
import jp.ac.tokushima_u.is.ll.service.CategoryService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@RequestMapping(method = RequestMethod.GET)
	public String list(ModelMap model) {
		model.addAttribute("categoryList", categoryService.findRoots());
		return "category/list";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addRoot(
			@ModelAttribute("categoryForm") CategoryEditForm form,
			ModelMap model) {
		return "category/add";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String createRoot(
			@ModelAttribute("categoryForm") CategoryEditForm form,
			BindingResult result, ModelMap model) {
		new CategoryEditFormValidator().validate(form, result);
		if (!StringUtils.isBlank(form.getName())
				&& !categoryService.findByName(form.getName().trim(), null).isEmpty()) {
			result.rejectValue("name", "categoryEditForm.name.alreadyExist");
		}
		if (result.hasErrors()) {
			return "category/add";
		}
		this.categoryService.createByForm(form, null);
		return "redirect:/category";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(@PathVariable String id, ModelMap model) {
		Category category = categoryService.findById(id);
		model.addAttribute("category", category);
		model.addAttribute("categoryPath", this.categoryPath(category));
		
//		Map<String, Integer> sizeMap = new HashMap<String, Integer>();
//		sizeMap.put(category.getId(), categoryService.sizeOfCategory(category));
//		for(Category c: category.getChildren()){
//			sizeMap.put(c.getId(), categoryService.sizeOfCategory(category));
//		}
//		model.addAttribute("sizeMap", sizeMap);
		
		/** 2011/06/05 HOU Bin
		List<Item> itemList = itemService.findByCategory(category);
		model.addAttribute("itemList", itemList);
		**/
		model.addAttribute("itemList", new ArrayList<Item>());
		return "category/show";
	}

	@RequestMapping(value = "/{id}/add", method = RequestMethod.GET)
	public String addChild(@PathVariable String id,
			@ModelAttribute("categoryForm") CategoryEditForm form,
			ModelMap model) {
		Category category = categoryService.findById(id);
		model.addAttribute("category", category);
		model.addAttribute("categoryPath", this.categoryPath(category));
		return "category/add";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String createChild(@PathVariable String id,
			@ModelAttribute("categoryForm") CategoryEditForm form,
			BindingResult result, ModelMap model) {
		new CategoryEditFormValidator().validate(form, result);
		Category parent = categoryService.findById(id);
		if(parent==null)return "redirect:/category/"+id;
		if (!StringUtils.isBlank(form.getName())
				&& !categoryService.findByName(form.getName().trim(), parent).isEmpty()) {
			result.rejectValue("name", "categoryEditForm.name.alreadyExist");
		}
		if (result.hasErrors()) {
			return "category/add";
		}
		this.categoryService.createByForm(form, parent);
		return "redirect:/category/" + id;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
	public String delete(@PathVariable String id) {
		Category category = categoryService.findById(id);
		Category parent = category.getParent();
		categoryService.delete(category);
		if (parent == null)
			return "redirect:/category";
		else
			return "redirect:/category/" + parent.getId();
	}

	private List<Category> categoryPath(Category category) {
		Category cat = categoryService.findById(category.getId());
		List<Category> vList = new ArrayList<Category>();
		vList.add(cat);
		while (cat.getParent() != null) {
			cat = cat.getParent();
			vList.add(cat);
		}
		List<Category> resultList = new ArrayList<Category>();
		for (int i = vList.size() - 1; i >= 0; i--) {
			resultList.add(vList.get(i));
		}
		return resultList;
	}
}
