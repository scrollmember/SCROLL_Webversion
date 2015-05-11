package jp.ac.tokushima_u.is.ll.controller.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.ac.tokushima_u.is.ll.service.EvalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Lists;

@Controller
@RequestMapping("/admin/wordlistfile")
public class WordlistFileController {
	
	@Autowired
	private EvalService evalService;
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat fileNameFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
	
	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model){
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		model.addAttribute("dateTo", dateformat.format(cal.getTime()));
		cal.add(Calendar.WEEK_OF_YEAR, -1);
		model.addAttribute("dateFrom", dateformat.format(cal.getTime()));
		return "admin/wordlistfile/index";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String download(String dateFrom, String dateTo, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		
		String filename = fileNameFormat.format(new Date())+"-"+UUID.randomUUID().toString().substring(0, 8);
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		Date from, to;
		try {
			from = dateformat.parse(dateFrom);
			to = dateformat.parse(dateTo);
		} catch (ParseException e1) {
			List<String> errors = Lists.newArrayList("Date format error.");
			model.put("errors", errors);
			model.put("dateFrom", dateFrom);
			model.put("dateTo", dateTo);
			return "admin/wordlistfile/index";
		}
		List<Map<String, Object>> wordList = evalService.findWordlist(from, to);
		
		String csv = "userEmail;username;type;actionDate;title;createTime;authorEmail;author\n";
		for(Map<String, Object> word: wordList){
			csv+=word.get("userEmail")+";";
			csv+=word.get("username")+";";
			csv+=word.get("type")+";";
			csv+=format.format((Date)word.get("actionDate"))+";";
			csv+=word.get("title")+";";
			csv+=format.format((Date)word.get("createTime"))+";";
			csv+=word.get("authorEmail")+";";
			csv+=word.get("author")+"\n";
		}
		
		response.reset();	// Reset the response
		response.setContentType("application/octet-stream;charset=UTF-8");	 // the encoding of this example is GB2312 
		response.setHeader("Content-Disposition","attachment; filename=\"" + filename + ".csv\"");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.write(csv);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args){
		String d = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			System.out.println(format.parse(d));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
