package jp.ac.tokushima_u.is.ll.controller.admin;

import jp.ac.tokushima_u.is.ll.entity.Language;
import jp.ac.tokushima_u.is.ll.service.LanguageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/admin/language")
public class LanguageController {
	
	@Autowired
	private LanguageService languageService;
	
	@RequestMapping
	public String list(ModelMap model){
		model.addAttribute("languageList", languageService.searchAllLanguageOrderedBy("code", true));
		return "admin/language/list";
	}
	
	@RequestMapping(value="/{id}")
	public String show(@PathVariable String id, ModelMap model){
		Language lang = languageService.findById(id);
		model.addAttribute("language", lang);
		return "admin/language/show";
	}
	
	@RequestMapping(value="/add", method = RequestMethod.GET)
	public String add(ModelMap model){
		model.addAttribute("language", new Language());
		return "admin/language/add";
	}
	
	@RequestMapping(value="/add", method = RequestMethod.POST)
	public String create(@ModelAttribute("language")Language language,BindingResult result,  ModelMap model){
		ValidationUtils.rejectIfEmptyOrWhitespace(result, "code", "language.code.empty", "Please input code");
		ValidationUtils.rejectIfEmptyOrWhitespace(result, "name", "language.name.empty", "Please input name");
		if(result.hasErrors()){
			return "admin/language/add";
		}
		Language existLang = languageService.findUniqueLangByCode(language.getCode().toLowerCase());
		if(existLang!=null){
			existLang.setName(language.getName());
			languageService.editByAdmin(existLang);
		}else{
			languageService.createByAdmin(language);
		}
		return "redirect:/admin/language";
	}
	
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable String id, ModelMap model){
		model.addAttribute("language", languageService.findById(id));
		return "admin/language/edit";
	}
	
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
	public String update(@PathVariable String id, @ModelAttribute("language")Language language, BindingResult result, ModelMap model){
		Language lang = languageService.findById(id);
		if(lang==null)return "redirect:/admin/language/" + id;
		ValidationUtils.rejectIfEmptyOrWhitespace(result, "code", "language.code.empty", "Please input code");
		ValidationUtils.rejectIfEmptyOrWhitespace(result, "name", "language.name.empty", "Please input name");
		if(result.hasErrors()){
			return "admin/language/edit";
		}
		Language existLang = languageService.findUniqueLangByCode(language.getCode().toLowerCase());
		if(existLang!=null && !existLang.getId().equals(id)){
			lang = existLang;
		}
		lang.setCode(language.getCode());
		lang.setName(language.getName());
		languageService.editByAdmin(language);
		return "redirect:/admin/language/show/"+id;
	}
	
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
	public String delete(@PathVariable String id){
		languageService.delete(id);
		return "redirect:/admin/language";
	}
}
