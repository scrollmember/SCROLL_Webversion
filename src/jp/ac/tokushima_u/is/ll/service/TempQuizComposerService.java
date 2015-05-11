package jp.ac.tokushima_u.is.ll.service;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.Item;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//TODO dameng

@Service("tempQuizComposerService")
@Transactional
public class TempQuizComposerService {
	 private HibernateDao<Item, String> itemDao;
//	 private HibernateDao<LLQuiz, String> quizDao;
//	 
//	 private static final int EnglishCourse = 1;
//	 private static final int JapaneseCourse = 2;
//	 private static final int ChineseCourse = 3;
////	 private static final int WithImage = 1;
////	 private static final int WithoutImage = 2;
//	 
//	 @Autowired
//	 private StaticServerService staticServerService;
//	 
//     @Autowired
//     public void setSessionFactory(SessionFactory sessionFactory) {
//         itemDao = new HibernateDao<Item, String> (sessionFactory, Item.class);
//         quizDao = new HibernateDao<LLQuiz, String> (sessionFactory, LLQuiz.class);
//     }
//     
////     public void composeItemQuiz(){
////    		String hql = "from Item item where item.image is not null";
////    		Map<String, Object> params = new HashMap<String, Object>();
////    		List<Item> results = this.itemDao.find(hql, params);
////    		for(Item item:results){
////    			LLQuiz quiz = new LLQuiz();
////    			quiz.setCourseid(Constants.ObjectMemoryCourse);
////    			quiz.setQuiztypeid(3);
////    			quiz.setCreateDate(new Date());
////      			quiz.setItem(item);
////      			quiz.setLatitude(item.getItemLat());
////      			quiz.setLongitude(item.getItemLng());
////      			quiz.setRighttimes(0);
////      			quiz.setWrongtimes(0);
////      			quiz.setLevel(10);
////      			this.quizDao.save(quiz);
////    		}
////    		
////     }
//     
//     public void composeImageQuiz() {
//    	String hql = "from Item item where item.image is not null";
//   		Map<String, Object> params = new HashMap<String, Object>();
//   		List<Item> results = this.itemDao.find(hql, params);
//   		if(results==null||results.size()<4)
//   			return;
//   		
//   		for (Item item : results) {
//   			if(item.getEnTitle()!=null&&item.getEnTitle().length()>0){
//   				LLQuiz quiz = new LLQuiz();
//   				quiz.setCourseid(EnglishCourse);
//   				quiz.setContent(item.getEnTitle());
//   				this.saveQuiz(quiz, item, results);
//   			}
//   			
//   			if(item.getJpTitle()!=null&&item.getJpTitle().length()>0){
//   				LLQuiz quiz = new LLQuiz();
//   				quiz.setCourseid(JapaneseCourse);
//   				quiz.setContent(item.getJpTitle());
//   				this.saveQuiz(quiz, item, results);
//   			}
//   			
//   			if(item.getZhTitle()!=null&&item.getZhTitle().length()>0){
//   				LLQuiz quiz = new LLQuiz();
//   				quiz.setCourseid(ChineseCourse);
//   				quiz.setContent(item.getZhTitle());
//   				this.saveQuiz(quiz, item, results);
//   			}
//   		}
//     }
//     
//     private void saveQuiz(LLQuiz quiz, Item item, List<Item>results){
//    		int rightanswernum = (int) (Math.random() * 4) + 1;
//			quiz.setQuiztypeid(Constants.QuizTypeImageMutiChoice);
//			quiz.setAnswer(rightanswernum + "");
//			quiz.setCreateDate(new Date());
//			quiz.setItem(item);
//			quiz.setLatitude(item.getItemLat());
//   			quiz.setLongitude(item.getItemLng());
//   			quiz.setRighttimes(0);
//   			quiz.setWrongtimes(0);
//   			quiz.setLevel(10);
//	   			
//   	   		Set<Item>choices= new HashSet<Item>();
//   	   		int c = 0;
// 			while(choices.size()<3){
// 				int ran = (int)(Math.random()*results.size());
// 				Item i = results.get(ran);
// 				if(!item.equals(i)||!item.getEnTitle().equals(i.getEnTitle()))
// 					choices.add(i);
// 				
// 				c++;
// 				if(c>30)
// 					return;
// 			}
//	   			
//   	   		Iterator<Item> itemIterator = choices.iterator();
//  	        int count = 1;
//  	        boolean flg = true;
//  	        while (itemIterator.hasNext()) {
//  	            if (count == rightanswernum) {
//  	            	flg = false;
//  	                MyQuizChoice ac = new MyQuizChoice();
//  	                ac.setContent(item.getImage().getId());
//  	                ac.setLlquiz(quiz);
//  	                ac.setNumber(rightanswernum);
//  	                ac.setNote(this.getNoteFromItem(item));
//  	                ac.setItem_id(item.getId());
//  	                quiz.addLLQuizChoice(ac);
//  	                count++;
//  	            }
//  	            Item tempItem = itemIterator.next();
//  	            MyQuizChoice ac = new MyQuizChoice();
//  	            ac.setContent(tempItem.getImage().getId());
//  	            ac.setLlquiz(quiz);
//  	            ac.setItem_id(tempItem.getId());
//  	            ac.setNote(this.getNoteFromItem(tempItem));
//  	            ac.setNumber(count);
//  	            quiz.addLLQuizChoice(ac);
//  	            count++;
//  	        }
//  			
//			if (flg) {
//				MyQuizChoice ac = new MyQuizChoice();
//				ac.setContent(item.getImage().getId());
//				ac.setLlquiz(quiz);
//				ac.setNumber(rightanswernum);
//				ac.setNote(this.getNoteFromItem(item));
//				ac.setItem_id(item.getId());
//				quiz.addLLQuizChoice(ac);
//			}
//	   			
//			  this.quizDao.save(quiz);
//    	 
//    	 
//     }
//     
//     
//     public void composeZhQuiz() {
//  		String hql = "from Item item where item.zhTitle is not null and item.zhTitle !=''  and item.relogItem is null  and  not exists ( select llquiz from LLQuiz llquiz where llquiz.item = item and llquiz.courseid =  3)";
//  		Map<String, Object> params = new HashMap<String, Object>();
//  		List<Item> results = this.itemDao.find(hql, params);
//  		for (Item item : results) {
//  			LLQuiz quiz = new LLQuiz();
//  			quiz.setCourseid(ChineseCourse);
//  			int rightanswernum = (int) (Math.random() * 4) + 1;
//  			quiz.setAnswer(rightanswernum + "");
//  			quiz.setCreateDate(new Date());
//  			quiz.setItem(item);
////  			if (this.hasImage(item)) {
////  				quiz.setQuiztypeid(WithImage);
////  			}else
////  				quiz.setQuiztypeid(WithoutImage);
//  			quiz.setLatitude(item.getItemLat());
//  			quiz.setLongitude(item.getItemLng());
//  			quiz.setRighttimes(0);
//  			quiz.setWrongtimes(0);
//  			quiz.setLevel(10);
//  			
//  			hql = "from Item item where item.zhTitle is not null and item.zhTitle !=''  and item.relogItem is null and  item !=:item";
//		 	
//	    	Map<String, Object> tempparams = new HashMap<String, Object>();
//	    	tempparams.put("item", item);
//	  		List<Item> tempresults = this.itemDao.find(hql, tempparams);
//			
//	  		if(tempresults==null||tempresults.size()<3)
//	  			continue;
//			
//			Set<Item>choices= new HashSet<Item>();
////			choices.add(item);
//			while(choices.size()<3){
//				int ran = (int)(Math.random()*tempresults.size());
//				Item i = tempresults.get(ran);
//				if(!item.equals(i))
//					choices.add(i);
//			}
//  			
//  		    Iterator<Item> itemIterator = choices.iterator();
//  	        int count = 1;
//  	        boolean flg = true;
//  	        while (itemIterator.hasNext()) {
//  	            if (count == rightanswernum) {
//  	            	flg = false;
//  	                MyQuizChoice ac = new MyQuizChoice();
//  	                ac.setContent(item.getZhTitle());
//  	                ac.setLlquiz(quiz);
//  	                ac.setNumber(rightanswernum);
//  	                ac.setNote(this.getNoteFromItem(item));
//  	                ac.setItem_id(item.getId());
//  	                quiz.addLLQuizChoice(ac);
//  	                count++;
//  	            }
//  	            Item tempItem = itemIterator.next();
//  	            MyQuizChoice ac = new MyQuizChoice();
//  	            ac.setContent(tempItem.getZhTitle());
//  	            ac.setLlquiz(quiz);
//  	            ac.setItem_id(tempItem.getId());
//  	            ac.setNote(this.getNoteFromItem(tempItem));
//  	            ac.setNumber(count);
//  	            quiz.addLLQuizChoice(ac);
//  	            count++;
//  	        }
//  			
//			if (flg) {
//				MyQuizChoice ac = new MyQuizChoice();
//				ac.setContent(item.getZhTitle());
//				ac.setLlquiz(quiz);
//				ac.setNumber(rightanswernum);
//				ac.setNote(this.getNoteFromItem(item));
//				ac.setItem_id(item.getId());
//				quiz.addLLQuizChoice(ac);
//			}
//
//  	        this.quizDao.save(quiz);
//  		}
//  	}
//     
//     
////     private boolean hasImage(Item item){
////    	 if(item==null||item.getImage()==null)
////    		 return false;
////    	 
////    	 byte[] photos = staticServerService.getImageFileById(item.getImage().getId(), Constants.NormalLevel);
////    	 if(photos!=null&&photos.length>0)
////    		 return true;
////    	 else
////    		 return false;
////     }
//
//     
//     public void composeJpQuiz() {
// 		String hql = "from Item item where item.jpTitle is not null and item.jpTitle !='' and item.relogItem is null and  not exists ( select llquiz from LLQuiz llquiz where llquiz.item = item and llquiz.courseid =  2)";
// 		Map<String, Object> params = new HashMap<String, Object>();
// 		List<Item> results = this.itemDao.find(hql, params);
// 		for (Item item : results) {
// 			LLQuiz quiz = new LLQuiz();
// 			quiz.setCourseid(JapaneseCourse);
// 			int rightanswernum = (int) (Math.random() * 4) + 1;
// 			quiz.setAnswer(rightanswernum + "");
// 			quiz.setCreateDate(new Date());
// 			quiz.setItem(item);
//// 			if (this.hasImage(item)) {
//// 				quiz.setQuiztypeid(WithImage);
//// 			}else
//// 				quiz.setQuiztypeid(WithoutImage);
// 			quiz.setLatitude(item.getItemLat());
// 			quiz.setLongitude(item.getItemLng());
// 			quiz.setRighttimes(0);
// 			quiz.setWrongtimes(0);
// 			quiz.setLevel(10);
// 			
// 			hql = "from Item item where item.jpTitle is not null and item.jpTitle !='' and item.relogItem is null and  item !=:item";
//		 	
//	    	Map<String, Object> tempparams = new HashMap<String, Object>();
//	    	tempparams.put("item", item);
//	  		List<Item> tempresults = this.itemDao.find(hql, tempparams);
//			
//	  		if(tempresults==null||tempresults.size()<3)
//	  			continue;
//			
//			Set<Item>choices= new HashSet<Item>();
////			choices.add(item);
//			while(choices.size()<3){
//				int ran = (int)(Math.random()*tempresults.size());
//				Item i = tempresults.get(ran);
//				if(!item.equals(i))
//					choices.add(i);
//			}
// 			
// 		    Iterator<Item> itemIterator = choices.iterator();
// 	        int count = 1;
// 	        boolean flg = true;
// 	        while (itemIterator.hasNext()) {
// 	            if (count == rightanswernum) {
// 	            	flg = false;
// 	                MyQuizChoice ac = new MyQuizChoice();
// 	                ac.setContent(item.getJpTitle());
// 	                ac.setLlquiz(quiz);
// 	                ac.setNumber(rightanswernum);
// 	                ac.setNote(this.getNoteFromItem(item));
// 	                ac.setItem_id(item.getId());
// 	                quiz.addLLQuizChoice(ac);
// 	                count++;
// 	            }
// 	            Item tempItem = itemIterator.next();
// 	            MyQuizChoice ac = new MyQuizChoice();
// 	            ac.setContent(tempItem.getJpTitle());
// 	            ac.setLlquiz(quiz);
// 	            ac.setItem_id(tempItem.getId());
// 	            ac.setNote(this.getNoteFromItem(tempItem));
// 	            ac.setNumber(count);
// 	            quiz.addLLQuizChoice(ac);
// 	            count++;
// 	        }
// 	        
// 	        if(flg){
//				MyQuizChoice ac = new MyQuizChoice();
//				ac.setContent(item.getJpTitle());
//				ac.setLlquiz(quiz);
//				ac.setNumber(rightanswernum);
//				ac.setNote(this.getNoteFromItem(item));
//				ac.setItem_id(item.getId());
//				quiz.addLLQuizChoice(ac);
// 	        }
// 			
// 	        this.quizDao.save(quiz);
// 		}
// 	}
//
//	public void composeQuiz() {
//		String hql = "from Item item where item.enTitle is not null and item.enTitle !='' and item.relogItem is null and  not exists ( select llquiz from LLQuiz llquiz where llquiz.item = item and llquiz.courseid =  1)";
//		Map<String, Object> params = new HashMap<String, Object>();
//		List<Item> results = this.itemDao.find(hql, params);
//		for (Item item : results) {
//			LLQuiz quiz = new LLQuiz();
//			quiz.setCourseid(EnglishCourse);
//			int rightanswernum = (int) (Math.random() * 4) + 1;
//			quiz.setAnswer(rightanswernum + "");
//			quiz.setCreateDate(new Date());
//			quiz.setItem(item);
////			if (this.hasImage(item)) {
////				quiz.setQuiztypeid(WithImage);
////			}else
////				quiz.setQuiztypeid(WithoutImage);
//			quiz.setLatitude(item.getItemLat());
//			quiz.setLongitude(item.getItemLng());
//			quiz.setRighttimes(0);
//			quiz.setWrongtimes(0);
//			quiz.setLevel(10);
//			
//			hql = "from Item item where item.enTitle is not null and item.enTitle !='' and item.relogItem is null and  item !=:item";
//		 	
//	    	Map<String, Object> tempparams = new HashMap<String, Object>();
//	    	tempparams.put("item", item);
//	  		List<Item> tempresults = this.itemDao.find(hql, tempparams);
//			
//	  		if(tempresults==null||tempresults.size()<3)
//	  			continue;
//			
//			Set<Item>choices= new HashSet<Item>();
////			choices.add(item);
//			while(choices.size()<3){
//				int ran = (int)(Math.random()*tempresults.size());
//				Item i = tempresults.get(ran);
//				if(!item.equals(i))
//					choices.add(i);
//			}
//			
//		    Iterator<Item> itemIterator = choices.iterator();
//	        int count = 1;
//	        boolean flg = true;
//	        while (itemIterator.hasNext()) {
//	            if (count == rightanswernum) {
//	            	flg = false;
//	                MyQuizChoice ac = new MyQuizChoice();
//	                ac.setContent(item.getEnTitle());
//	                ac.setLlquiz(quiz);
//	                ac.setNumber(rightanswernum);
//	                ac.setNote(this.getNoteFromItem(item));
//	                ac.setItem_id(item.getId());
//	                quiz.addLLQuizChoice(ac);
//	                count++;
//	            }
//	            Item tempItem = itemIterator.next();
//	            MyQuizChoice ac = new MyQuizChoice();
//	            ac.setContent(tempItem.getEnTitle());
//	            ac.setLlquiz(quiz);
//	            ac.setItem_id(tempItem.getId());
//	            ac.setNote(this.getNoteFromItem(tempItem));
//	            ac.setNumber(count);
//	            quiz.addLLQuizChoice(ac);
//	            count++;
//	        }
//			if(flg){
//				  MyQuizChoice ac = new MyQuizChoice();
//	                ac.setContent(item.getEnTitle());
//	                ac.setLlquiz(quiz);
//	                ac.setNumber(rightanswernum);
//	                ac.setNote(this.getNoteFromItem(item));
//	                ac.setItem_id(item.getId());
//	                quiz.addLLQuizChoice(ac);
//			}
//	        
//	        this.quizDao.save(quiz);
//		}
//	}
//	
//	 private String getNoteFromItem(Item item){
//	        if(item == null)
//	            return "";
//	        String note = "";
//
//	        String t = item.getJpTitle();
//	        if(t!=null&&t.length()>0)
//	            note = note+t+"(jp) ";
//
//	         t = item.getEnTitle();
//	        if(t!=null&&t.length()>0)
//	            note = note+t+"(en) ";
//
//	          t = item.getZhTitle();
//	        if(t!=null&&t.length()>0)
//	            note = note+t+"(zh) ";
//	          return note;
//	    }
//     
//     public void deleteQuiz(){
//    	 LLQuiz q = new LLQuiz();
//    	 q.setQuiztypeid(2);
//    	 this.quizDao.delete(q);
//     }
//
//     
//     public void deleteAllQuiz(){
//    	 String hql1 = "delete from LLQuizChoice choice where ";
//    	 
//    	 String hql = "delete from LLQuiz quiz where quiz.quiztypeid = 2";
//    	 Map<String,Object>p = new HashMap<String,Object>();
//    	 this.quizDao.batchExecute(hql, p);
//     }
     
}
