package jp.ac.tokushima_u.is.ll.service;

import java.io.PrintStream;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.GeoPoint;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.LogUserReadItem;
import jp.ac.tokushima_u.is.ll.entity.MyQuiz;
import jp.ac.tokushima_u.is.ll.entity.SendTime;
import jp.ac.tokushima_u.is.ll.entity.Setting;
import jp.ac.tokushima_u.is.ll.entity.StudyActivity;
import jp.ac.tokushima_u.is.ll.entity.StudyArea;
import jp.ac.tokushima_u.is.ll.entity.StudyPhase;
import jp.ac.tokushima_u.is.ll.entity.StudySpeed;
import jp.ac.tokushima_u.is.ll.entity.StudyTime;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.form.PersonalSyncCondForm;
import jp.ac.tokushima_u.is.ll.form.SendTimeForm;
import jp.ac.tokushima_u.is.ll.form.StudyAreaForm;
import jp.ac.tokushima_u.is.ll.form.StudyTimeForm;
import jp.ac.tokushima_u.is.ll.util.Constants;
import jp.ac.tokushima_u.is.ll.util.FormatUtil;
import jp.ac.tokushima_u.is.ll.util.GeoClustering;
import jp.ac.tokushima_u.is.ll.util.GeoUtils;
import jp.ac.tokushima_u.is.ll.util.SpeedClustering;
import jp.ac.tokushima_u.is.ll.util.Utility;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("personalizeService")
@Transactional
public class PersonalizeSerivce {
	private HibernateDao<StudyArea, String> studyAreaDao;
	private HibernateDao<StudyTime, String> studyTimeDao;
	 private HibernateDao<SendTime, String> sendTimeDao;
	private HibernateDao<StudySpeed, String> studySpeedDao;
	private HibernateDao<Item, String> itemDao;
	private HibernateDao<MyQuiz, String> myquizDao;
	private HibernateDao<Users, String> userDao;
	private HibernateDao<Setting, String> settingDao;
	private HibernateDao<LogUserReadItem, String> itemlogDao;
	private SessionFactory sessionFactory;
	private HibernateDao<StudyActivity, String> studyActivityDao;
	private HibernateDao<StudyPhase, String> studyPhaseDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.studyTimeDao = new HibernateDao<StudyTime, String>(sessionFactory,
				StudyTime.class);
		this.studyAreaDao = new HibernateDao<StudyArea, String>(sessionFactory,
				StudyArea.class);
		this.studySpeedDao = new HibernateDao<StudySpeed, String>(sessionFactory,
				StudySpeed.class);
		this.userDao = new HibernateDao<Users, String>(sessionFactory,
				Users.class);
		this.settingDao = new HibernateDao<Setting, String>(sessionFactory,
				Setting.class);
		this.myquizDao = new HibernateDao<MyQuiz, String>(sessionFactory,
				MyQuiz.class);
		this.itemDao = new HibernateDao<Item, String>(sessionFactory,
				Item.class);
		this.itemlogDao = new HibernateDao<LogUserReadItem, String>(
				sessionFactory, LogUserReadItem.class);
		this.sendTimeDao = new HibernateDao<SendTime, String>(sessionFactory, SendTime.class);
		this.studyActivityDao = new HibernateDao<StudyActivity, String>(sessionFactory, StudyActivity.class); 
		this.studyPhaseDao = new HibernateDao<StudyPhase, String>(sessionFactory, StudyPhase.class); 
	}

	public void personlize() {
		PrintStream out = System.out;
		Date start = new Date();
		List<Users> users = this.userDao.getAll();
//		List<Users> users = this.userDao.findBy("id", "ff80818125899fb501258acaadc10001");
		for (Users user : users) {
			out.println("user analyzing: "+user.getNickname());
			
			List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
			List<Float> speedPoints = new ArrayList<Float>();
			DetachedCriteria activityCriteria = DetachedCriteria.forClass(StudyActivity.class);
			activityCriteria.add(Restrictions.eq("author", user));
			List<StudyActivity> activities = this.studyActivityDao.find(activityCriteria);
			
			for(StudyActivity sa:activities){
				Double lat = sa.getLatitude();
				Double lng = sa.getLongitude();
				if(GeoUtils.validateLatLng(lat, lng)&&GeoUtils.isHighPrecision(lat, lng)){
					GeoPoint gp = new GeoPoint(lat,lng);
					geoPoints.add(gp);
				}
				if(sa.getSpeed()!=null&&sa.getSpeed()>0){
					speedPoints.add(sa.getSpeed());
				}
			}
			
			DetachedCriteria areaCriteria = DetachedCriteria.forClass(StudyArea.class);
			areaCriteria.add(Restrictions.eq("author", user));
			areaCriteria.add(Restrictions.eq("disabled",0));
			areaCriteria.add(Restrictions.gt("createDate", Utility.getYesterday()));
			List<StudyArea> oldAreas = this.studyAreaDao.find(areaCriteria);
			if(oldAreas==null||oldAreas.size()==0){
				String areaHql = "update StudyArea sa set sa.disabled = 1 where sa.author=:author";
				Map<String,Object> areaValues = new HashMap<String,Object>();
				areaValues.put("author", user);
				this.studyAreaDao.batchExecute(areaHql, areaValues);
				
				List<Map<String, Double>> geoRanges = GeoClustering.getRange(geoPoints);
				for(Map<String, Double> geoRange:geoRanges){
					if (geoRange != null && geoRange.get(Constants.MAX_LAT) != null
							&& geoRange.get(Constants.MAX_LNG) != null
							&& geoRange.get(Constants.MIN_LAT) != null
							&& geoRange.get(Constants.MIN_LNG) != null) {
						StudyArea studyarea = new StudyArea();
						studyarea.setAuthor(user);
						studyarea.setCreateDate(new Date());
						studyarea.setDisabled(0);
						if(geoRange.get(Constants.MAX_LAT).equals(geoRange.get(Constants.MIN_LAT))){
							studyarea.setMinlat(geoRange.get(Constants.MIN_LAT));
							studyarea.setMaxlat(geoRange.get(Constants.MAX_LAT)+0.0002);
						}else{
							studyarea.setMinlat(geoRange.get(Constants.MIN_LAT));
							studyarea.setMaxlat(geoRange.get(Constants.MAX_LAT));
						}
						if(geoRange.get(Constants.MAX_LNG).equals(geoRange.get(Constants.MIN_LNG))){
							studyarea.setMaxlng(geoRange.get(Constants.MAX_LNG)+0.0002);
							studyarea.setMinlng(geoRange.get(Constants.MIN_LNG));
						}else{
							studyarea.setMaxlng(geoRange.get(Constants.MAX_LNG));
							studyarea.setMinlng(geoRange.get(Constants.MIN_LNG));
						}
						this.studyAreaDao.save(studyarea);
					}
				}
			}
			
			DetachedCriteria speedCriteria = DetachedCriteria.forClass(StudySpeed.class);
			speedCriteria.add(Restrictions.eq("author", user));
			speedCriteria.add(Restrictions.eq("disabled",0));
			speedCriteria.add(Restrictions.gt("createDate", Utility.getYesterday()));
			List<StudySpeed> oldSpeeds = this.studySpeedDao.find(speedCriteria);
			if(oldSpeeds==null||oldSpeeds.size()==0){
				String speedHql = "update StudySpeed ss set ss.disabled = 1 where ss.author=:author";
				Map<String,Object> speedValues = new HashMap<String,Object>();
				speedValues.put("author", user);
				this.studySpeedDao.batchExecute(speedHql, speedValues);
				
				List<Map<String, Float>> speedRanges = SpeedClustering
				.findSpeedPhase(speedPoints);
				if(speedRanges!=null){
					for(Map<String,Float>speedRange:speedRanges){
						if(speedRange.get(Constants.MAX_SPEED)!=null&&speedRange.get(Constants.MIN_SPEED)!=null){
							StudySpeed studyspeed = new StudySpeed();
							studyspeed.setAuthor(user);
							studyspeed.setCreateDate(new Date());
							studyspeed.setDisabled(0);
							studyspeed.setMaxspeed(speedRange.get(Constants.MAX_SPEED));
							studyspeed.setMixspeed(speedRange.get(Constants.MIN_SPEED));
							this.studySpeedDao.save(studyspeed);
						}
					}
				}
			}
		}
		Date over = new Date();
		out.println("It costs "+(over.getTime()-start.getTime())/1000+" seconds");
	}

	public void findLearningHabit(){
		PrintStream out = System.out;
		Date start = new Date();
		List<Users> users = this.userDao.getAll();
//		List<Users> users = this.userDao.findBy("id", "ff80818125899fb501258acaadc10001");
		for (Users user : users) {
			out.println("user analyzing: "+user.getNickname());
			
			DetachedCriteria activityCriteria = DetachedCriteria.forClass(StudyActivity.class);
			activityCriteria.add(Restrictions.eq("author", user));
			activityCriteria.addOrder(Order.desc("createtime"));
			List<StudyActivity> activities = this.studyActivityDao.find(activityCriteria);
			Date lastDate = null;
			if(activities!=null&&activities.size()>0)
				lastDate = activities.get(0).getCreatetime();
			
			
//			List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
//			List<TimeNode> timePoints = new ArrayList<TimeNode>();
//			List<Float> speedPoints = new ArrayList<Float>();

			DetachedCriteria itemCrit = DetachedCriteria.forClass(Item.class);
			itemCrit.add(Restrictions.eq("author", user));
			itemCrit.add(Restrictions.isNull("relogItem"));
			if(lastDate != null)
				itemCrit.add(Restrictions.gt("createTime", lastDate));
			List<Item> items = this.itemDao.find(itemCrit);

			for (Item item : items) {
//				if (GeoUtils.isHighPrecision(item.getItemLat(), item
//						.getItemLng())) {
//					geoPoints.add(new GeoPoint(item.getItemLat(), item
//							.getItemLng()));
//				}
//				if (item.getSpeed() != null)
//					speedPoints.add(item.getSpeed());
//				if (item.getCreateTime() != null) {
//					timePoints.add(new TimeNode(Utility.getTime(item
//							.getCreateTime())));
//				}
				StudyActivity sa = new StudyActivity();
				sa.setAuthor(user);
				sa.setCreatetime(item.getCreateTime());
				if(GeoUtils.validateLatLng(item.getItemLat(), item.getItemLng())){
					sa.setLatitude(item.getItemLat());
					sa.setLongitude(item.getItemLng());
				}
				sa.setSpeed(item.getSpeed());
				sa.setActivity_type(Constants.StudyActivityAddItem);
				this.studyActivityDao.save(sa);
			}

			DetachedCriteria itemlogCrit = DetachedCriteria
					.forClass(LogUserReadItem.class);
			itemlogCrit.add(Restrictions.eq("user", user));
			if(lastDate != null)
				itemlogCrit.add(Restrictions.gt("createTime", lastDate));
			List<LogUserReadItem> itemlogs = this.itemlogDao.find(itemlogCrit);

			for (LogUserReadItem itemlog : itemlogs) {
//				if (GeoUtils.isHighPrecision(itemlog.getLatitude(), itemlog
//						.getLongitude())) {
//					geoPoints.add(new GeoPoint(itemlog.getLatitude(), itemlog
//							.getLongitude()));
//				}
//				if (itemlog.getSpeed() != null)
//					speedPoints.add(itemlog.getSpeed());
//				if (itemlog.getCreateTime() != null) {
//					timePoints.add(new TimeNode(Utility.getTime(itemlog
//							.getCreateTime())));
//				}
				
				StudyActivity sa = new StudyActivity();
				sa.setAuthor(user);
				sa.setCreatetime(itemlog.getCreateTime());
				
				if(GeoUtils.validateLatLng(itemlog.getLatitude(), itemlog.getLongitude())){
					sa.setLatitude(itemlog.getLatitude());
					sa.setLongitude(itemlog.getLongitude());
				}
				
				sa.setSpeed(itemlog.getSpeed());
				sa.setActivity_type(Constants.StudyActivityViewItem);
				this.studyActivityDao.save(sa);
			}

			DetachedCriteria myquizCrit = DetachedCriteria
					.forClass(MyQuiz.class);
			myquizCrit.add(Restrictions.eq("author", user));
			myquizCrit.add(Restrictions.ne("answerstate",
					Constants.NotAnsweredState));
			if(lastDate != null)
				myquizCrit.add(Restrictions.gt("updateDate", lastDate));
			List<MyQuiz> myquizzes = this.myquizDao.find(myquizCrit);
			for (MyQuiz myquiz : myquizzes) {
//				if (GeoUtils.isHighPrecision(myquiz.getLatitude(), myquiz
//						.getLongitude())) {
//					geoPoints.add(new GeoPoint(myquiz.getLatitude(), myquiz
//							.getLongitude()));
//				}
//				if (myquiz.getSpeed() != null)
//					speedPoints.add(myquiz.getSpeed());
//				if (myquiz.getUpdateDate() != null) {
//					timePoints.add(new TimeNode(Utility.getTime(myquiz
//							.getUpdateDate())));
//				}

				StudyActivity sa = new StudyActivity();
				sa.setAuthor(user);
				if(myquiz.getUpdateDate()!=null)
					sa.setCreatetime(myquiz.getUpdateDate());
				else
					sa.setCreatetime(myquiz.getCreateDate());
				if(GeoUtils.validateLatLng(myquiz.getLatitude(), myquiz.getLongitude())){
					sa.setLatitude(myquiz.getLatitude());
					sa.setLongitude(myquiz.getLongitude());
				}
				
				sa.setSpeed(myquiz.getSpeed());
				sa.setActivity_type(Constants.StudyActivityDoQuiz);
				this.studyActivityDao.save(sa);
			}
			
			DetachedCriteria phaseCriteria = DetachedCriteria.forClass(StudyPhase.class);
			phaseCriteria.add(Restrictions.eq("user", user));
			phaseCriteria.addOrder(Order.desc("endTime"));
			List<StudyPhase>oldPhases =  this.studyPhaseDao.find(phaseCriteria);
			Date lastPhaseDate = null;
			if(oldPhases!=null && oldPhases.size()>0){
				lastPhaseDate = oldPhases.get(0).getEndTime();
			}
			
			activityCriteria = DetachedCriteria.forClass(StudyActivity.class);
			activityCriteria.add(Restrictions.eq("author", user));
			if(lastPhaseDate != null)
				activityCriteria.add(Restrictions.gt("createtime", lastPhaseDate));
			activityCriteria.addOrder(Order.asc("createtime"));
			List<StudyActivity> allactivities = this.studyActivityDao.find(activityCriteria);
			List<StudyPhase> phases = findSameActivity(allactivities);
			for(StudyPhase phase:phases){
				this.studyPhaseDao.save(phase);
			}
			
		}
		Date over = new Date();
		out.println("It costs "+(over.getTime()-start.getTime())/1000+" seconds");
	}
	
	public void findTimeHabits(){
		List<Users> users = this.userDao.getAll();
//		List<Users> users = this.userDao.findBy("id", userid);
		Date start = new Date();
		for (Users user : users) {
			findUserTimeHabit(user);
		}
		Date end = new Date();
		System.out.println((end.getTime()-start.getTime())/1000+" seconds");
	}
	
	public void findUserTimeHabit(Users user){
		DetachedCriteria phaseCriteria = DetachedCriteria.forClass(StudyPhase.class);
		phaseCriteria.add(Restrictions.eq("user", user));
		List<StudyPhase> allPhases = this.studyPhaseDao.find(phaseCriteria);
		Map<Integer, Double> phaseValues = new HashMap<Integer, Double>();
		for(StudyPhase sp:allPhases){
			Time startTime = new Time(sp.getStartTime().getTime());
			Time endTime = new Time(sp.getEndTime().getTime());
			addPhaseValue(phaseValues, startTime, endTime, sp.getNum());
		}
		
		System.out.println("user phase weight "+user.getNickname());
		
		int maxkey = 0;
		double maxvalue = 0.0;
		
		for(Integer key:phaseValues.keySet()){
			if(phaseValues.get(key)>maxvalue){
				maxvalue = phaseValues.get(key);
				maxkey = key;
			}
		}
		
		DetachedCriteria timeCriteria = DetachedCriteria.forClass(StudyTime.class);
		timeCriteria.add(Restrictions.eq("author", user));
		timeCriteria.add(Restrictions.eq("disabled",0));
		timeCriteria.add(Restrictions.gt("createDate", Utility.getYesterday()));
		List<StudyTime> oldTimes = this.studyTimeDao.find(timeCriteria);
		if(oldTimes==null||oldTimes.size()==0){
			String timeHql = "update StudyTime st set st.disabled = 1 where st.author=:author";
			Map<String,Object> timeValues = new HashMap<String,Object>();
			timeValues.put("author", user);
			this.studyTimeDao.batchExecute(timeHql, timeValues);
			
			
			StudyTime studytime = new StudyTime();
			studytime.setAuthor(user);
			studytime.setCreateDate(new Date());
			studytime.setDisabled(0);
			studytime.setStarttime(getStartTimeFromKey(maxkey));
			studytime.setEndtime(getEndTimeFromKey(maxkey));
			this.studyTimeDao.save(studytime);
			
//			List<Map<String, Time>> timeRanges = TimeRankClustering.findRange(timePoints);
//			for(Map<String, Time> timeRange:timeRanges){
//				if(timeRange!=null&&timeRange.get(Constants.MAX_TIME)!=null&&timeRange.get(Constants.MIN_TIME)!=null){
//					StudyTime studytime = new StudyTime();
//					studytime.setAuthor(user);
//					studytime.setCreateDate(new Date());
//					studytime.setDisabled(0);
//					studytime.setStarttime(timeRange.get(Constants.MIN_TIME));
//					studytime.setEndtime(timeRange.get(Constants.MAX_TIME));
//					this.studyTimeDao.save(studytime);
//				}
//			}
		}
	}
	
	private void addPhaseValue(Map<Integer, Double>phaseValues, Time startTime, Time endTime, Integer number){
		double length = FormatUtil.getSeconds(endTime)
				- FormatUtil.getSeconds(startTime);
		int phaseLegth = 15*60;
		if(length < phaseLegth)
			length = phaseLegth;
//		double weight = number/length;
		double weight = 1;
		
		Integer startIndex = getKye(startTime);
		Integer endIndex = getKye(endTime);
		
		if(startIndex == endIndex){
			Double value = phaseValues.get(startIndex);
			if(value == null)
				phaseValues.put(startIndex, weight);
			else
				phaseValues.put(new Integer(startIndex), value+weight);
		}else{
			Time sTime = startTime;
			for(int i = startIndex;i<=endIndex;i++){
				int leg = 0;
				if(i == getKye(endTime))
					leg = getSeconds(endTime)- getSeconds(sTime);
				else
					 leg = i*30*60-getSeconds(sTime);
				Double value = phaseValues.get(new Integer(i));
				if(value == null)
					phaseValues.put(new Integer(i), leg*weight/length);
				else
					phaseValues.put(new Integer(i), value+leg*weight/length);
				sTime = getKyeTime(i);
			}
		}
	}
	
	public static Time getKyeTime(int key){
		Time t = Time.valueOf("00:00:00");
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(t.getTime());
		cal.add(Calendar.SECOND, key*30*60);
		return new Time(cal.getTimeInMillis());
	}
	
	public static Integer getKye(Time t){
		int division = 30*60;
		Integer l = getSeconds(t);
		int key = l/division;
		if(l%division > 0)
			key++;
		return key;
	}
	
	public static Time getStartTimeFromKey(int key){
		int min = key*30;
		int h = min/60;
		int m = min%60;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, h);
		cal.set(Calendar.MINUTE, m);
		cal.set(Calendar.SECOND, 0);
		return new Time(cal.getTimeInMillis());
	}
	
	public static Time getEndTimeFromKey(int key){
		int min = (key+1)*30;
		int h = min/60;
		int m = min%60;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, h);
		cal.set(Calendar.MINUTE, m);
		cal.set(Calendar.SECOND, 0);
		return new Time(cal.getTimeInMillis());
	}
	
	public static Integer getSeconds(Time t1){
		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(t1.getTime());
		int h1 = c1.get(Calendar.HOUR_OF_DAY);
		int m1 = c1.get(Calendar.MINUTE);
		int s1 = c1.get(Calendar.SECOND);
		return h1*60*60+m1*60+s1;
	}
	
	public Map<String,List> syncPersonlize(PersonalSyncCondForm form, Users user){
		Map<String,List> map = new HashMap<String,List>();
		DetachedCriteria area_dc = DetachedCriteria.forClass(StudyArea.class);
		area_dc.add(Restrictions.eq("author", user));
		area_dc.add(Restrictions.ne("disabled", new Integer(1)));
		if(form.getAreaUpdateTime()!=null)
			area_dc.add(Restrictions.gt("createDate", form.getAreaUpdateTime()));
		List<StudyArea> areas = this.studyAreaDao.find(area_dc);
		if(areas!=null&&areas.size()>0){
			List<StudyAreaForm> areaForms = new ArrayList<StudyAreaForm>(); 
			for(StudyArea area:areas){
				areaForms.add(new StudyAreaForm(area));
			}
			map.put("areas", areaForms);
		}
		
		DetachedCriteria time_dc = DetachedCriteria.forClass(StudyTime.class);
		time_dc.add(Restrictions.eq("author", user));
		time_dc.add(Restrictions.ne("disabled", new Integer(1)));
		if(form.getTimeUpdateTime()!=null)
			time_dc.add(Restrictions.gt("createDate", form.getTimeUpdateTime()));
		List<StudyTime> times = this.studyTimeDao.find(time_dc);
		if(times!=null&&times.size()>0){
			List<StudyTimeForm> timeForms = new ArrayList<StudyTimeForm>(); 
			for(StudyTime time:times){
				timeForms.add(new StudyTimeForm(time));
			}
			map.put("times", timeForms);
		}
		
		DetachedCriteria send_dc = DetachedCriteria.forClass(SendTime.class);
		send_dc.add(Restrictions.eq("author", user));
		if(form.getSendUpdateTime()!=null)
			send_dc.add(Restrictions.gt("createDate", form.getSendUpdateTime()));
		List<SendTime> sends = this.sendTimeDao.find(send_dc);
		if(sends!=null){
			List<SendTimeForm> sendForms = new ArrayList<SendTimeForm>();
			for(SendTime send:sends){
				sendForms.add(new SendTimeForm(send));
			}
			map.put("sends", sendForms);
		}
		
		return map;
	}
	
	
	public List<StudyPhase> findSameActivity(List<StudyActivity> allactivities ){
		List<StudyPhase> phases = new ArrayList<StudyPhase>();
		if(allactivities == null || allactivities.size()==0)
			return phases;
		
		StudyActivity fsa = allactivities.get(0);
		Date startTime = fsa.getCreatetime();
		Double minLat =  fsa.getLatitude();
		Double minLng =  fsa.getLongitude();
		
		Date endTime = startTime;
		Double maxLat =  fsa.getLatitude();
		Double maxLng =  fsa.getLongitude();
		
		int quiznumber = 0;
		int viewitemnumber = 0;
		int additemnumber = 0;
		
		if(Constants.StudyActivityAddItem.equals(fsa.getActivity_type()))
			additemnumber++;
		if(Constants.StudyActivityDoQuiz.equals(fsa.getActivity_type()))
			quiznumber++;
		if(Constants.StudyActivityViewItem.equals(fsa.getActivity_type()))
			viewitemnumber++;
		
		int phaseLength = 1000*60*15;
		
		
		for(int i=1; i<allactivities.size();i++){
			StudyActivity sa = allactivities.get(i);
			if(Math.abs((startTime.getTime()-sa.getCreatetime().getTime()))<phaseLength){
				endTime = sa.getCreatetime();
				if(minLat == null){
					minLat = sa.getLatitude();
				}else if(sa.getLatitude()!=null && minLat>sa.getLatitude())
					minLat = sa.getLatitude();
				if(maxLat == null){
					maxLat = sa.getLatitude();
				}else if(sa.getLatitude()!=null && maxLat<sa.getLatitude())
					maxLat = sa.getLatitude();
				
				if(minLng == null)
					minLng = sa.getLongitude();
				else if(sa.getLongitude()!=null && minLng>sa.getLongitude())
					minLng = sa.getLongitude();
				if(maxLng == null)
					maxLng = sa.getLongitude();
				else if(sa.getLongitude()!=null && maxLng<sa.getLongitude())
					maxLng = sa.getLongitude();
				
				if(Constants.StudyActivityAddItem.equals(sa.getActivity_type()))
					additemnumber++;
				if(Constants.StudyActivityDoQuiz.equals(sa.getActivity_type()))
					quiznumber++;
				if(Constants.StudyActivityViewItem.equals(sa.getActivity_type()))
					viewitemnumber++;
			}else{
				StudyPhase sp = new StudyPhase(startTime,endTime,minLat, minLng, maxLat, maxLng, quiznumber, additemnumber, viewitemnumber, sa.getAuthor());
				phases.add(sp);
				
				startTime = sa.getCreatetime();
				minLat =  sa.getLatitude();
				minLng =  sa.getLongitude();
				
				endTime = startTime;
				maxLat =  sa.getLatitude();
				maxLng =  sa.getLongitude();
				
				quiznumber = 0;
				viewitemnumber = 0;
				additemnumber = 0;
				
				if(Constants.StudyActivityAddItem.equals(sa.getActivity_type()))
					additemnumber++;
				if(Constants.StudyActivityDoQuiz.equals(sa.getActivity_type()))
					quiznumber++;
				if(Constants.StudyActivityViewItem.equals(sa.getActivity_type()))
					viewitemnumber++;
			}
		}
		
		StudyPhase sp = new StudyPhase(startTime,endTime,minLat, minLng, maxLat, maxLng, quiznumber, additemnumber, viewitemnumber, fsa.getAuthor());
		phases.add(sp);
		
		return phases;
	}
	
}
