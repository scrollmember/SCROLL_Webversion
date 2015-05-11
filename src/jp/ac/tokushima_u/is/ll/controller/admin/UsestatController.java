package jp.ac.tokushima_u.is.ll.controller.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.service.EvalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * For evaluation experiment
 * @author Houbin
 *
 */
@Controller
@RequestMapping("/admin/usestat")
public class UsestatController {
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat fileNameFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
	
	@Autowired
	private EvalService evalService;
	
	@RequestMapping(value = "/day", method=RequestMethod.POST)
	public void downloadDay(HttpServletRequest request, HttpServletResponse response){
		Date startDate = evalService.findStartDate();
		List<Users> userList = evalService.findUsersHaveItemsFrom(startDate);
		Map<String, Map<String, Integer>> data = evalService.findUseStat(userList, startDate);
		
		
		Collections.sort(userList, new Comparator<Users>(){
			@Override
			public int compare(Users o1, Users o2) {
				if(o1==null){
					return -1;
				}else if(o2 == null){
					return 1;
				}else{
					return o1.getPcEmail().compareTo(o2.getPcEmail());
				}
			}
		});
		
		String csv = "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		Date current = new Date();
		csv = "email, type";
		while(current.after(cal.getTime())){
			csv+= "," + dateFormat.format(cal.getTime());
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}
		csv+="\n";
		
		for(Users user: userList){
			csv+=user.getPcEmail()+",C";
			Map<String, Integer> cStat = data.get(user.getPcEmail()+"_C");
			cal.setTime(startDate);
			while(current.after(cal.getTime())){
				Integer num = 0;
				num = cStat.get(dateFormat.format(cal.getTime()));
				if(num==null)num=0;
				csv+= "," + num;
				cal.add(Calendar.DAY_OF_YEAR, 1);
			}
			csv+="\n";
			
			csv+=user.getPcEmail()+",R";
			Map<String, Integer> rStat = data.get(user.getPcEmail()+"_R");
			cal.setTime(startDate);
			while(current.after(cal.getTime())){
				Integer num = 0;
				num = rStat.get(dateFormat.format(cal.getTime()));
				if(num==null)num=0;
				csv+= "," + num;
				cal.add(Calendar.DAY_OF_YEAR, 1);
			}
			csv+="\n";
			
			csv+=user.getPcEmail()+",Q";
			Map<String, Integer> qStat = data.get(user.getPcEmail()+"_Q");
			cal.setTime(startDate);
			while(current.after(cal.getTime())){
				Integer num = 0;
				num = qStat.get(dateFormat.format(cal.getTime()));
				if(num==null)num=0;
				csv+= "," + num;
				cal.add(Calendar.DAY_OF_YEAR, 1);
			}
			csv+="\n";
		}
		
		String filename = fileNameFormat.format(new Date())+"-"+UUID.randomUUID().toString().substring(0, 8);
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
	}
	
	@RequestMapping(value = "/week", method=RequestMethod.POST)
	public void downloadWeek(HttpServletRequest request, HttpServletResponse response){
		Date startDate = evalService.findStartDate();
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		if(startCal.get(Calendar.DAY_OF_WEEK)!=5){
			startCal.set(Calendar.DAY_OF_WEEK, 5);
		}
		startCal.set(Calendar.HOUR_OF_DAY, 0);
		startCal.set(Calendar.MINUTE, 0);
		startCal.set(Calendar.SECOND, 0);

		List<Users> userList = evalService.findUsersHaveItemsFrom(startDate);
		Map<String, Map<String, Integer>> data = evalService.findUseStatWeek(userList, startCal);
		
		
		Collections.sort(userList, new Comparator<Users>(){
			@Override
			public int compare(Users o1, Users o2) {
				if(o1==null){
					return -1;
				}else if(o2 == null){
					return 1;
				}else{
					return o1.getPcEmail().compareTo(o2.getPcEmail());
				}
			}
		});
		
		String csv = "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		Date current = new Date();
		csv = "email, type";
		while(current.after(cal.getTime())){
			Calendar end = (Calendar)cal.clone();
			end.add(Calendar.DAY_OF_YEAR, 6);
			csv+= "," + dateFormat.format(cal.getTime()) + "~" + dateFormat.format(end.getTime());
			cal.add(Calendar.DAY_OF_YEAR, 7);
		}
		csv+="\n";
		
		for(Users user: userList){
			csv+=user.getPcEmail()+",C";
			Map<String, Integer> cStat = data.get(user.getPcEmail()+"_C");
			cal.setTime(startDate);
			while(current.after(cal.getTime())){
				Integer num = 0;
				num = cStat.get(dateFormat.format(cal.getTime()));
				if(num==null)num=0;
				csv+= "," + num;
				cal.add(Calendar.DAY_OF_YEAR, 7);
			}
			csv+="\n";
			
			csv+=user.getPcEmail()+",R";
			Map<String, Integer> rStat = data.get(user.getPcEmail()+"_R");
			cal.setTime(startDate);
			while(current.after(cal.getTime())){
				Integer num = 0;
				num = rStat.get(dateFormat.format(cal.getTime()));
				if(num==null)num=0;
				csv+= "," + num;
				cal.add(Calendar.DAY_OF_YEAR, 7);
			}
			csv+="\n";
			
			csv+=user.getPcEmail()+",Q";
			Map<String, Integer> qStat = data.get(user.getPcEmail()+"_Q");
			cal.setTime(startDate);
			while(current.after(cal.getTime())){
				Integer num = 0;
				num = qStat.get(dateFormat.format(cal.getTime()));
				if(num==null)num=0;
				csv+= "," + num;
				cal.add(Calendar.DAY_OF_YEAR, 7);
			}
			csv+="\n";
		}
		
		String filename = fileNameFormat.format(new Date())+"-"+UUID.randomUUID().toString().substring(0, 8);
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
	}
	
	public static void main(String[] args){
		Calendar startCal = Calendar.getInstance();
		if(startCal.get(Calendar.DAY_OF_WEEK)!=5){
			startCal.set(Calendar.DAY_OF_WEEK, 5);
		}
		startCal.set(Calendar.HOUR_OF_DAY, 0);
		startCal.set(Calendar.MINUTE, 0);
		startCal.set(Calendar.SECOND, 0);
		Calendar cal = (Calendar)startCal.clone();
		cal.add(Calendar.DAY_OF_YEAR, 7);
		System.out.println(startCal.getTime());
		System.out.println(cal.getTime());
	}
}
