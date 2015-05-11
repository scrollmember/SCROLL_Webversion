/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.ac.tokushima_u.is.ll.service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.Category;
import jp.ac.tokushima_u.is.ll.entity.SendTime;
import jp.ac.tokushima_u.is.ll.entity.Setting;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.form.SettingForm;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.util.ConvertUtil;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author li
 */
@Service("settingService")
@Transactional
public class SettingService {

    private HibernateDao<SendTime, String> sendTimeDao;
    private HibernateDao<Users, String> userDao;
    private HibernateDao<Setting, String> settingDao;
    private HibernateDao<Category, String> categoryDao;
    @Autowired
    UserService userService;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        sendTimeDao = new HibernateDao<SendTime, String>(sessionFactory, SendTime.class);
        userDao = new HibernateDao<Users,String>(sessionFactory,Users.class);
        settingDao = new HibernateDao<Setting,String>(sessionFactory,Setting.class);
        categoryDao = new HibernateDao<Category, String>(sessionFactory, Category.class);
    }

    public void initSettingForm(SettingForm form){
        Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
        if(user.getReceiveWeeklyNotification()==null){
        	this.userService.checkFlags(user.getId());
        	user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
        }
        if(user.getReceiveWeeklyNotification()){
        	form.setReceiveMailNotification(true);
        }else{
        	form.setReceiveMailNotification(false);
        }
        String hql = "from SendTime sendtime where sendtime.author =:author and sendtime.typ =:typ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("author", user);

        String formatHour = "HH";
        String formatMin = "mm";


        params.put("typ", Calendar.MONDAY);
        SendTime temp = this.sendTimeDao.findUnique(hql, params);
        if(temp!=null){
            form.setMonstime1(ConvertUtil.timeToString(temp.getSendtime(), formatHour));
            form.setMonsmin1(ConvertUtil.timeToString(temp.getSendtime(), formatMin));
        }

        params.put("typ", Calendar.TUESDAY);
        temp = this.sendTimeDao.findUnique(hql, params);
        if(temp!=null){
            form.setTuestime1(ConvertUtil.timeToString(temp.getSendtime(), formatHour));
            form.setTuesmin1(ConvertUtil.timeToString(temp.getSendtime(), formatMin));
        }

        params.put("typ", Calendar.WEDNESDAY);
        temp = this.sendTimeDao.findUnique(hql, params);
        if(temp!=null){
            form.setWedstime1(ConvertUtil.timeToString(temp.getSendtime(), formatHour));
            form.setWedsmin1(ConvertUtil.timeToString(temp.getSendtime(), formatMin));
        }

         params.put("typ", Calendar.THURSDAY);
        temp = this.sendTimeDao.findUnique(hql, params);
        if(temp!=null){
            form.setThurstime1(ConvertUtil.timeToString(temp.getSendtime(), formatHour));
            form.setThursmin1(ConvertUtil.timeToString(temp.getSendtime(), formatMin));
        }

         params.put("typ", Calendar.FRIDAY);
        temp = this.sendTimeDao.findUnique(hql, params);
        if(temp!=null){
            form.setFristime1(ConvertUtil.timeToString(temp.getSendtime(), formatHour));
             form.setFrismin1(ConvertUtil.timeToString(temp.getSendtime(), formatMin));
        }

         params.put("typ", Calendar.SATURDAY);
        temp = this.sendTimeDao.findUnique(hql, params);
        if(temp!=null){
            form.setSatstime1(ConvertUtil.timeToString(temp.getSendtime(), formatHour));
            form.setSatsmin1(ConvertUtil.timeToString(temp.getSendtime(), formatMin));
        }

         params.put("typ", Calendar.SUNDAY);
        temp = this.sendTimeDao.findUnique(hql, params);
        if(temp!=null){
            form.setSunstime1(ConvertUtil.timeToString(temp.getSendtime(), formatHour));
            form.setSunsmin1(ConvertUtil.timeToString(temp.getSendtime(), formatMin));
        }

       Setting setting = user.getSetting();
       if(setting!=null){
            form.setHandsetcd(setting.getHandsetcd());
       }
    }
    
    public List<Setting> findSetting(Integer handsetcd){
    	String hql = "from Setting where handsetcd =:handsetcd";
    	Map<String, Object> params = new HashMap<String,Object>();
    	params.put("handsetcd", handsetcd);
    	return this.settingDao.find(hql, params);
    }
    
    public void updateSettingForm2(SettingForm form){
        Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
        Time montime = null;
        Time tuetime = null;
        Time wedtime = null;
        Time thurtime = null;
        Time fritime = null;
        Time sattime = null;
        Time suntime = null;
        try {
            if (form.getMonstime1() != null && form.getMonstime1().length() > 0) {
                montime = Time.valueOf(form.getMonstime1()+":"+form.getMonsmin1()+":00");
            }
            if (form.getTuestime1() != null && form.getTuestime1().length() > 0) {
                tuetime = Time.valueOf(form.getTuestime1()+":"+form.getTuesmin1()+":00");
            }
            if (form.getWedstime1() != null && form.getWedstime1().length() > 0) {
                wedtime = Time.valueOf(form.getWedstime1()+":"+form.getWedsmin1()+":00");
            }
            if (form.getThurstime1() != null && form.getThurstime1().length() > 0) {
                thurtime = Time.valueOf(form.getThurstime1()+":"+form.getThursmin1()+":00");
            }
            if (form.getFristime1() != null && form.getFristime1().length() > 0) {
                fritime = Time.valueOf(form.getFristime1()+":"+form.getFrismin1()+":00");
            }
            if (form.getSatstime1() != null && form.getSatstime1().length() > 0) {
                sattime = Time.valueOf(form.getSatstime1()+":"+form.getSatsmin1()+":00");
            }
            if (form.getSunstime1() != null && form.getSunstime1().length() > 0) {
                suntime = Time.valueOf(form.getSunstime1()+":"+form.getSunsmin1()+":00");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<SendTime> resutls = new ArrayList<SendTime>();
        SendTime sendtime = null;
        if (montime != null) {
            sendtime = new SendTime();
            sendtime.setAuthor(user);
            sendtime.setTyp(Calendar.MONDAY);
            sendtime.setSendtime(montime);
            this.sendTimeDao.save(sendtime);
            resutls.add(sendtime);
        }

        if (tuetime != null) {
            sendtime = new SendTime();
            sendtime.setAuthor(user);
            sendtime.setTyp(Calendar.TUESDAY);
            sendtime.setSendtime(tuetime);
            this.sendTimeDao.save(sendtime);
            resutls.add(sendtime);
        }

        if (wedtime != null) {
            sendtime = new SendTime();
            sendtime.setAuthor(user);
            sendtime.setTyp(Calendar.WEDNESDAY);
            sendtime.setSendtime(wedtime);
            this.sendTimeDao.save(sendtime);
            resutls.add(sendtime);
        }

        if (thurtime != null) {
            sendtime = new SendTime();
            sendtime.setAuthor(user);
            sendtime.setTyp(Calendar.THURSDAY);
            sendtime.setSendtime(thurtime);
            this.sendTimeDao.save(sendtime);
            resutls.add(sendtime);
        }

        if (fritime != null) {
            sendtime = new SendTime();
            sendtime.setAuthor(user);
            sendtime.setTyp(Calendar.FRIDAY);
            sendtime.setSendtime(fritime);
            this.sendTimeDao.save(sendtime);
            resutls.add(sendtime);
        }

        if (sattime != null) {
            sendtime = new SendTime();
            sendtime.setAuthor(user);
            sendtime.setTyp(Calendar.SATURDAY);
            sendtime.setSendtime(sattime);
            this.sendTimeDao.save(sendtime);
            resutls.add(sendtime);
        }

        if (suntime != null) {
            sendtime = new SendTime();
            sendtime.setAuthor(user);
            sendtime.setTyp(Calendar.SUNDAY);
            sendtime.setSendtime(suntime);
            this.sendTimeDao.save(sendtime);
            resutls.add(sendtime);
        }
        Setting setting = user.getSetting();
        if(setting == null)
            setting = new Setting();
        setting.setHandsetcd(form.getHandsetcd());

        user.setSendTimeList(resutls);
        this.userDao.save(user);
    }
    public void saveSettingForm(SettingForm form) {
        Time montime = null;
        Time tuetime = null;
        Time wedtime = null;
        Time thurtime = null;
        Time fritime = null;
        Time sattime = null;
        Time suntime = null;
        try {
            if (form.getMonstime1() != null && form.getMonstime1().length() > 0) {
//                montime = Time.valueOf(form.getMonstime1());
                 montime = Time.valueOf(form.getMonstime1()+":"+form.getMonsmin1()+":00");
            }
            if (form.getTuestime1() != null && form.getTuestime1().length() > 0) {
//                tuetime = Time.valueOf(form.getTuestime1());
                tuetime = Time.valueOf(form.getTuestime1()+":"+form.getTuesmin1()+":00");
            }
            if (form.getWedstime1() != null && form.getWedstime1().length() > 0) {
//                wedtime = Time.valueOf(form.getWedstime1());
                wedtime = Time.valueOf(form.getWedstime1()+":"+form.getWedsmin1()+":00");
            }
            if (form.getThurstime1() != null && form.getThurstime1().length() > 0) {
//                thurtime = Time.valueOf(form.getThurstime1());
                thurtime = Time.valueOf(form.getThurstime1()+":"+form.getThursmin1()+":00");
            }
            if (form.getFristime1() != null && form.getFristime1().length() > 0) {
//                fritime = Time.valueOf(form.getFristime1());
                fritime = Time.valueOf(form.getFristime1()+":"+form.getFrismin1()+":00");
            }
            if (form.getSatstime1() != null && form.getSatstime1().length() > 0) {
//                sattime = Time.valueOf(form.getSatstime1());
                 sattime = Time.valueOf(form.getSatstime1()+":"+form.getSatsmin1()+":00");
            }
            if (form.getSunstime1() != null && form.getSunstime1().length() > 0) {
//                suntime = Time.valueOf(form.getSunstime1());
                 suntime = Time.valueOf(form.getSunstime1()+":"+form.getSunsmin1()+":00");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        Users user = userService.getById(SecurityUserHolder.getCurrentUser().getId());

        String hql = "from SendTime sendtime where sendtime.author =:author and sendtime.typ =:typ";
        SendTime sendtime = null;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("author", user);

       params.put("typ", Calendar.MONDAY);
       SendTime temp = this.sendTimeDao.findUnique(hql, params);
        if (montime != null) {
            if(temp==null){
                sendtime = new SendTime();
                sendtime.setAuthor(user);
                sendtime.setTyp(Calendar.MONDAY);
            }else{
                sendtime = temp;
            }
            sendtime.setSendtime(montime);
            sendtime.setCreateDate(new Date());
            this.sendTimeDao.save(sendtime);
        }else if(temp!=null){
           this.sendTimeDao.delete(temp);
        }

        params.put("typ", Calendar.TUESDAY);
        temp = this.sendTimeDao.findUnique(hql, params);
        if (tuetime != null) {
            if (temp == null) {
                sendtime = new SendTime();
                sendtime.setAuthor(user);
                sendtime.setTyp(Calendar.TUESDAY);
            } else {
                sendtime = temp;
            }
            sendtime.setSendtime(tuetime);
            sendtime.setCreateDate(new Date());
            this.sendTimeDao.save(sendtime);
        }else if(temp!=null){
           this.sendTimeDao.delete(temp);
        }

        params.put("typ", Calendar.WEDNESDAY);
        temp = this.sendTimeDao.findUnique(hql, params);
        if (wedtime != null) {
            if (temp == null) {
                sendtime = new SendTime();
                sendtime.setAuthor(user);
                sendtime.setTyp(Calendar.WEDNESDAY);
            } else {
                sendtime = temp;
            }
            sendtime.setSendtime(wedtime);
            sendtime.setCreateDate(new Date());
            this.sendTimeDao.save(sendtime);
        }else if(temp!=null){
           this.sendTimeDao.delete(temp);
        }

        params.put("typ", Calendar.THURSDAY);
        temp = this.sendTimeDao.findUnique(hql, params);
        if (thurtime != null) {
            if (temp == null) {
                sendtime = new SendTime();
                sendtime.setAuthor(user);
                sendtime.setTyp(Calendar.THURSDAY);
            } else {
                sendtime = temp;
            }
            sendtime.setSendtime(thurtime);
            sendtime.setCreateDate(new Date());
            this.sendTimeDao.save(sendtime);
        }else if(temp!=null){
           this.sendTimeDao.delete(temp);
        }

        params.put("typ", Calendar.FRIDAY);
        temp = this.sendTimeDao.findUnique(hql, params);
        if (fritime != null) {
            if (temp == null) {
                sendtime = new SendTime();
                sendtime.setAuthor(user);
                sendtime.setTyp(Calendar.FRIDAY);
            } else {
                sendtime = temp;
            }
            sendtime.setSendtime(fritime);
            sendtime.setCreateDate(new Date());
            this.sendTimeDao.save(sendtime);
        }else if(temp!=null){
           this.sendTimeDao.delete(temp);
        }

        params.put("typ", Calendar.SATURDAY);
        temp = this.sendTimeDao.findUnique(hql, params);
        if (sattime != null) {
            if (temp == null) {
                sendtime = new SendTime();
                sendtime.setAuthor(user);
                sendtime.setTyp(Calendar.SATURDAY);
            } else {
                sendtime = temp;
            }
            sendtime.setSendtime(sattime);
            sendtime.setCreateDate(new Date());
            this.sendTimeDao.save(sendtime);
        }else if(temp!=null){
           this.sendTimeDao.delete(temp);
        }

        params.put("typ", Calendar.SUNDAY);
        temp = this.sendTimeDao.findUnique(hql, params);
        if (suntime != null) {
            if (temp == null) {
                sendtime = new SendTime();
                sendtime.setAuthor(user);
                sendtime.setTyp(Calendar.SUNDAY);
            } else {
                sendtime = temp;
            }
            sendtime.setSendtime(suntime);
            sendtime.setCreateDate(new Date());
            this.sendTimeDao.save(sendtime);
        } else if (temp != null) {
            this.sendTimeDao.delete(temp);
        }


         Setting setting = user.getSetting();
         if(form.getHandsetcd()!=null){
            if(setting == null)
                 setting = new Setting();
             setting.setHandsetcd(form.getHandsetcd());
             setting.setAuthor(user);
             this.settingDao.save(setting);
         }else{
             if(setting!=null)
                this.settingDao.delete(setting);
         }
         
         //Save category
         if(form.getCategoryIdList()==null){
        	 form.setCategoryIdList(new ArrayList<String>());
         }
         List<Category> myCategoryList = new ArrayList<Category>();
         for(String categoryId: form.getCategoryIdList()){
        	 myCategoryList.add(categoryDao.get(categoryId));
         }
         user.setMyCategoryList(myCategoryList);
         if(!StringUtils.isBlank(form.getDefaultCategoryId())){
        	 Category defaultCategory = categoryDao.get(form.getDefaultCategoryId());
        	 user.setDefaultCategory(defaultCategory);
         }else{
        	 user.setDefaultCategory(null);
         }
       	 user.setReceiveWeeklyNotification(form.isReceiveMailNotification());
       	 user.setUpdateTime(new Date());
         userDao.save(user);
    }
}
