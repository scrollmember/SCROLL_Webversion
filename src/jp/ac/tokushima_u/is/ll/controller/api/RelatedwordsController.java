package jp.ac.tokushima_u.is.ll.controller.api;

import jp.ac.tokushima_u.is.ll.service.YahooApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/relatedwords")
public class RelatedwordsController {

	@Autowired
	private YahooApi yahooApi;
	
	@RequestMapping
	@ResponseBody
	public String execute(String w){
		String result = yahooApi.searchWikipediaSummary(w);
		return result;
	}
}
