package jp.ac.tokushima_u.is.ll.controller;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.Itemlatlng;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.entity.Usertest;
import jp.ac.tokushima_u.is.ll.form.ItemSearchCondForm;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.ItemService;
import jp.ac.tokushima_u.is.ll.service.MyQuizService;
import jp.ac.tokushima_u.is.ll.service.UserService;

import org.opencyc.api.CycAccess;
import org.opencyc.api.CycApiException;
import org.opencyc.cycobject.CycConstant;
import org.opencyc.cycobject.CycFormulaSentence;
import org.opencyc.cycobject.CycFort;
import org.opencyc.cycobject.CycList;
import org.opencyc.cycobject.CycNart;
import org.opencyc.cycobject.CycObject;
import org.opencyc.cycobject.CycSymbol;
import org.opencyc.cycobject.DefaultCycObject;
import org.opencyc.cycobject.ELMt;
import org.opencyc.cycobject.Guid;
import org.opencyc.inference.DefaultInferenceParameters;
import org.opencyc.inference.DefaultInferenceWorker;
import org.opencyc.inference.DefaultInferenceWorkerSynch;
import org.opencyc.inference.InferenceParameters;
import org.opencyc.inference.InferenceStatus;
import org.opencyc.inference.InferenceWorker;
import org.opencyc.inference.InferenceWorkerListener;
import org.opencyc.inference.InferenceWorkerSynch;
import org.opencyc.parser.CycLParserUtil;
import org.opencyc.util.Log;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//import org.opencyc.cycobject.CycConstant;
//import org.opencyc.cycobject.CycFormulaSentence;
//import org.opencyc.cycobject.CycList;
//import org.opencyc.cycobject.CycNart;
//import org.opencyc.cycobject.CycObject;
//import org.opencyc.cycobject.CycSymbol;
//import org.opencyc.cycobject.DefaultCycObject;
//import org.opencyc.cycobject.ELMt;
//import org.opencyc.cycobject.Guid;
//import org.opencyc.inference.DefaultInferenceParameters;
//import org.opencyc.inference.DefaultInferenceWorker;
//import org.opencyc.inference.DefaultInferenceWorkerSynch;
//import org.opencyc.inference.InferenceParameters;
//import org.opencyc.inference.InferenceStatus;
//import org.opencyc.inference.InferenceWorker;
//import org.opencyc.inference.InferenceWorkerListener;
//import org.opencyc.inference.InferenceWorkerSuspendReason;
//import org.opencyc.inference.InferenceWorkerSynch;
//import org.opencyc.parser.CycLParserUtil;
//import com.google.gson.Gson;

/**
 * 
 * @author houbin
 */
@Controller
public class IndexController {

	@Autowired
	private UserService userService;
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private MyQuizService myQuizService;

	// Personal_timedata 配列データ
	String[] learningarray = new String[12];
	// Alllearners_data 配列データ
	String[] learningarray2 = new String[12];
	// Personal one day data 配列
	Integer[] learningarray3 = new Integer[24];
	// All one day data 配列
	Integer[] learningarray4 = new Integer[24];
	// 朝昼夜型判定
	int morning = 0;
	int midday = 0;
	int night = 0;
	String creteria;
	Users user;

	protected CycAccess cycAccess;
	protected CycAccess access;

	static public final String DATE_PATTERN = "yyyy-MM-dd";

	@RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, ModelMap model)
			throws UnknownHostException, CycApiException, IOException {
		user = userService.getById(SecurityUserHolder.getCurrentUser().getId());
		model.addAttribute("user", user);
		model.addAttribute("uploadItemRanking",
				this.itemService.uploadRanking());
		model.addAttribute("answerRanking", this.itemService.answerRanking());

		// Entries awaiting your answers
		ItemSearchCondForm awaitCond = new ItemSearchCondForm();
		awaitCond.setToAnswerQuesLangs(user.getMyLangs());
		model.addAttribute("toAnswerItems",
				itemService.searchItemPageByCond(awaitCond));

		// New entries written in the language you are learning
		ItemSearchCondForm studyCond = new ItemSearchCondForm();
		studyCond.setToStudyQuesLangs(user.getStudyLangs());
		model.addAttribute("toStudyItems",
				itemService.searchItemPageByCond(studyCond));

		// Latest answered questions for you
		ItemSearchCondForm answeredCond = new ItemSearchCondForm();
		answeredCond.setHasAnswers(true);
		answeredCond.setUserId(user.getId());
		model.addAttribute("answeredItems",
				itemService.searchItemPageByCond(answeredCond));

		// Add Statistics
		Map<String, Long> stat = new LinkedHashMap<String, Long>();
		stat.put("Members", userService.findAllUserCount());
		stat.put("Learning Logs", itemService.findAllItemCount());
		stat.put("Views", itemService.findAllReadCount());
		stat.put("Tags", itemService.findAllTagCount());
		model.addAttribute("stat", stat);

		// ■wakebe 次のレベルまでの経験値取得
		model.addAttribute("nextExperiencePoint",
				this.userService.getNextExperiencePoint(user.getId()));

		// ■wakebe 現在の合計経験値取得
		model.addAttribute("nowExperiencePoint",
				this.userService.getNowExperiencePoint(user.getId()));

		// 個々の学習者の月ごとの登録したユビキタスラーニングログの個数
		Personal_timedata();
		AllLearners_data();
		model.addAttribute("learningnumbers", learningarray);
		model.addAttribute("learningnumbers2", learningarray2);
		// 個々の学習者の時間帯
		Personal_Oneday_data();
		model.addAttribute("learningnumbers3", learningarray3);
		model.addAttribute("learningType", creteria);
		// 全部の学習者の時間帯
		All_Oneday_data(model);
		model.addAttribute("learningnumbers4", learningarray4);

		// Place Ranking
		All_Place_data(model);
		// Filtering Word and Place and Time Zone
		// Filtering_Word_Place_Time()
		// OpenCyc();
		
		
		//Dashboard functions

		Dashboard(model);
		
//		REXP x ;
//		RConnection c;
//		try {
//		 c = new RConnection();
//		 
//		 //直接コマンドで代入
//		 c.eval("x<-10");
//		 //代入はこの書き方がたぶん正解
////		 String xx=10;
////		 c.assign("x",xx);
//		 
//		 x = c.eval("x/2");
//		 System.out.println(x.asString());
//		}catch (RserveException e) {
//		 e.printStackTrace();
//		} catch (REXPMismatchException e) {
//		 e.printStackTrace();
//		}
		
		return "index";
	}

//	@RequestMapping(value = "/{id}/{page}", method = RequestMethod.GET)
//	public String show(@PathVariable String id,@PathVariable String page, ModelMap model) {
//
//		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
//				.getId());
//		ItemSearchCondForm form=new ItemSearchCondForm();
//		//form.setUsername("Saikhanaa");
//		form.setPage(Integer.parseInt(page));		
//		
//		form.setItemIds(myQuizService.getItemIdsByIncorrectCount(Integer.parseInt(id), user));
//		model.addAttribute("itemPage", itemService.searchItemPageByCond(form));
//		model.addAttribute("itemType", id);
//
////		addTagCloud(model);
//		model.addAttribute("itemList", new ArrayList<Item>());
//		return "dashboard/logs";
//	}
	
	// private void Filtering_Word_Place_Time() {
	// // TODO Auto-generated method stub
	//
	// }

	private void OpenCyc() throws UnknownHostException, CycApiException,
			IOException {
		// TODO Auto-generated method stub
		// try {
		// cycAccess = new CycAccess();
		// }
		// catch (Exception e) {
		// Log.current.errorPrintln(e.getMessage());
		// Log.current.printStackTrace(e);
		// }

		// demo1();
		// demo2();
		// demo3();
		// demo4();
		// demo5();
		// demo6();
		// demo7();
		// demo8();
		// demo9();
		// demo10();
		// demo11();
		// demo12();
		// demo13();
		// demo14();
		// demo15();
		// demo16();
		// demo17();
		// sample();
	}

	// Opencyc algrism***************************************************************************************************************************************************//
	private void sample() {
		// TODO Auto-generated method stub
		System.out.println("Starting Cyc NART examples.");
		try {
			access = new CycAccess();
			// Log.current.println("Demonstrating getConstantGuid api function.\n");
			// Guid a = access.getConstantGuid("apple");

			CycConstant cycAdministrator = access
					.getKnownConstantByName("CycAdministrator");
			System.out.println("Unique id ?" + cycAdministrator.guid);
			System.out.println("Unique id ?"
					+ DefaultCycObject.toCompactExternalId("AppleInc"));
			CycList isas = access.getIsas(access
					.getKnownConstantByName("AppleInc"));
			Log.current.println("\nThe obtained isas are:\n" + isas.cyclify());
			CycConstant generalCycKE = access
					.getKnownConstantByName("GeneralCycKE");
			access.setCyclist(cycAdministrator); // needed to maintain
													// bookeeping information
			access.setKePurpose(generalCycKE); // needed to maintain bookeeping
												// information

			// find nart by external id (preferred lookup mechanism)
			CycNart apple = (CycNart) DefaultCycObject.fromCompactExternalId(
					"Mx8Ngh4rvVipdpwpEbGdrcN5Y29ycB4rvVjBnZwpEbGdrcN5Y29ycA",
					access);

			// access.getConstantId("");
			// find nart by name (dispreferred because names in the KB can
			// change)
			CycNart apple2 = access.getCycNartFromCons((CycList) CycLParserUtil
					.parseCycLDenotationalTerm("(FruitFn AppleTree)", true,
							access));

			assert apple.equals(apple2) : "Lookup failed to produce equal results.";

			// getting the external id DefaultCycObject.tofor a NART
			String appleEID = DefaultCycObject.toCompactExternalId(apple,
					access);

			// creating a nart
			// There is no direct way of creating NARTs, they are an
			// implementation detail
			// of the inference engine. However, if you make an assertion using
			// arguments
			// to a reifiable function that the inference engine hasn't seen
			// before, then it will be create
			// automatically.
			CycNart elmFruit = new CycNart(
					access.getKnownConstantByName("FruitFn"),
					access.getKnownConstantByName("ElmTree"));
			// NOTE: the previous call only makes the NART locally and not in
			// the KB

			// Asserting the isa and genls relations
			// every new term should have at least 1 isa assertion made on it
			access.assertIsa(elmFruit, CycAccess.collection, CycAccess.baseKB);
			// Note: the previous line causes the new NART to be created in the
			// KB!

			// Every new collection should have at least 1 genls assertion made
			// on it,
			// however, in this case, the inference engine has made it for you
			// since
			// any new terms involving FruitFn's must be types of fruits.
			// access.assertGenls(elmFruit,
			// access.getKnownConstantByName("Fruit"), CycAccess.baseKB);

			// verify genls relation
			assert access.isSpecOf(elmFruit,
					access.getKnownConstantByName("Fruit"), CycAccess.baseKB) : "Good grief! Elm fruit isn't known to be a type of fruit.";

			// find everything that is an apple
			System.out.println("Got instances of Apple: "
					+ access.getInstances(apple));
			System.out.println("Got instances of Apple: "
					+ access.getAllInstances(apple, CycAccess.baseKB));

			// find everything that a apple is a type of
			System.out.println("Got generalizations of Apple: "
					+ access.getAllGenls(apple, CycAccess.baseKB));

			// find everything that is a type of apple
			System.out.println("Got specializations of Apple: "
					+ access.getAllSpecs(apple, CycAccess.baseKB));

			// generating NL
			// System.out.println("The concept " + apple.cyclify()
			// + " can be referred to in English as '" +
			// access.getGeneratedPhrase(apple) + "'.");

			// Killing a NART -- removing a NART and all assertions involving
			// that NART from the KB
			// Warning: you can potentially do serious harm to the KB if you
			// remove critical information

			access.kill(elmFruit);

			// CycConstant dog2 = access.getKnownConstantByName("Dog");
			// CycFort snowSkiing = access.getKnownConstantByName("SnowSkiing");

		} catch (UnknownHostException nohost) {
			nohost.printStackTrace();
		} catch (IOException io) {
			io.printStackTrace();
		} catch (CycApiException cyc_e) {
			cyc_e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Finished.");

	}

	private void All_Place_data(ModelMap model) {
		// TODO Auto-generated method stub
		List<Itemlatlng> itemlatlng = itemService.getAllplace();
		List<Itemlatlng> categoryplace = itemService.getCategoryplace();
		// Alllearners_data 配列データ
		String[] placearray = new String[itemlatlng.size()];
		// Personal one day data 配列
		Integer[] placecount = new Integer[itemlatlng.size()];
		for (int a = 0; a < itemlatlng.size(); a++) {
			placearray[a] = itemlatlng.get(a).getLearningplace();
			placecount[a] = itemlatlng.get(a).getLcount();
		}
		model.addAttribute("placename", placearray);
		model.addAttribute("placecount", placecount);
		model.addAttribute("Placerank", itemlatlng);
		model.addAttribute("PlaceAttribute", categoryplace);
	}

	private void All_Oneday_data(ModelMap model) {
		// TODO Auto-generated method stub
		int one = 0;
		int two = 0;
		int three = 0;
		int four = 0;
		int five = 0;
		int six = 0;
		int seven = 0;
		int eight = 0;
		int nine = 0;
		int ten = 0;
		int eleven = 0;
		int twelve = 0;
		int thirteen = 0;
		int fourteen = 0;
		int fifteen = 0;
		int sixteen = 0;
		int seventeen = 0;
		int eighteen = 0;
		int nineteen = 0;
		int twenty = 0;
		int twenty_one = 0;
		int twenty_two = 0;
		int twenty_three = 0;
		int twenty_four = 0;
		List<Itemlatlng> itemlatlng = itemService.getAllOneday();
		List<Itemlatlng> Recommend_Time_Log = itemService.getRecommendTime();
		List<Itemlatlng> Spring_Type = new ArrayList<Itemlatlng>();
		List<Itemlatlng> Summer_Type = new ArrayList<Itemlatlng>();
		List<Itemlatlng> Fall_Type = new ArrayList<Itemlatlng>();
		List<Itemlatlng> Winter_Type = new ArrayList<Itemlatlng>();

		List<Itemlatlng> Morning_Type = new ArrayList<Itemlatlng>();
		List<Itemlatlng> Day_Type = new ArrayList<Itemlatlng>();
		List<Itemlatlng> Night_Type = new ArrayList<Itemlatlng>();

		int spring = 0;
		int summer = 0;
		int fall = 0;
		int winter = 0;
		for (int a = 0; a < Recommend_Time_Log.size(); a++) {
			// 春型のユビキタスラーニングログ
			if (Recommend_Time_Log.get(a).getCreate_time().getMonth() + 1 >= 3
					&& Recommend_Time_Log.get(a).getCreate_time().getMonth() + 1 <= 5) {
				Itemlatlng Spring_Entity = new Itemlatlng();
				Spring_Entity.setId(Recommend_Time_Log.get(a).getId());
				Spring_Entity.setCreate_time(Recommend_Time_Log.get(a)
						.getCreate_time());
				Spring_Entity
						.setContent(Recommend_Time_Log.get(a).getContent());
				Spring_Entity.setDashtime((new SimpleDateFormat(DATE_PATTERN))
						.format(Recommend_Time_Log.get(a).getCreate_time()));
				Spring_Entity.setNickname(Recommend_Time_Log.get(a)
						.getNickname());
				Spring_Type.add(Spring_Entity);
			}
			// 夏型のユビキタスラーニングログ
			if (Recommend_Time_Log.get(a).getCreate_time().getMonth() + 1 >= 6
					&& Recommend_Time_Log.get(a).getCreate_time().getMonth() + 1 <= 8) {
				Itemlatlng Summer_Entity = new Itemlatlng();
				Summer_Entity.setId(Recommend_Time_Log.get(a).getId());
				Summer_Entity.setCreate_time(Recommend_Time_Log.get(a)
						.getCreate_time());
				Summer_Entity
						.setContent(Recommend_Time_Log.get(a).getContent());
				Summer_Entity.setDashtime((new SimpleDateFormat(DATE_PATTERN))
						.format(Recommend_Time_Log.get(a).getCreate_time()));
				Summer_Entity.setNickname(Recommend_Time_Log.get(a)
						.getNickname());
				Summer_Type.add(Summer_Entity);

			}

			// 秋型のユビキタスラーニングログ
			if (Recommend_Time_Log.get(a).getCreate_time().getMonth() + 1 >= 9
					&& Recommend_Time_Log.get(a).getCreate_time().getMonth() + 1 <= 11) {
				Itemlatlng Fall_Entity = new Itemlatlng();
				Fall_Entity.setId(Recommend_Time_Log.get(a).getId());
				Fall_Entity.setCreate_time(Recommend_Time_Log.get(a)
						.getCreate_time());
				Fall_Entity.setContent(Recommend_Time_Log.get(a).getContent());
				Fall_Entity.setDashtime((new SimpleDateFormat(DATE_PATTERN))
						.format(Recommend_Time_Log.get(a).getCreate_time()));
				Fall_Entity
						.setNickname(Recommend_Time_Log.get(a).getNickname());
				Fall_Type.add(Fall_Entity);
			}

			// 冬型のユビキタスラーニングログ
			if (Recommend_Time_Log.get(a).getCreate_time().getMonth() + 1 >= 0
					&& Recommend_Time_Log.get(a).getCreate_time().getMonth() + 1 <= 2) {
				Itemlatlng Winter_Entity = new Itemlatlng();
				Winter_Entity.setId(Recommend_Time_Log.get(a).getId());
				Winter_Entity.setCreate_time(Recommend_Time_Log.get(a)
						.getCreate_time());
				Winter_Entity
						.setContent(Recommend_Time_Log.get(a).getContent());
				Winter_Entity.setDashtime((new SimpleDateFormat(DATE_PATTERN))
						.format(Recommend_Time_Log.get(a).getCreate_time()));
				Winter_Entity.setNickname(Recommend_Time_Log.get(a)
						.getNickname());
				Winter_Type.add(Winter_Entity);

			}

			// 朝型のユビキタスラーニングログ
			if (Recommend_Time_Log.get(a).getCreate_time().getHours() >= 4
					&& Recommend_Time_Log.get(a).getCreate_time().getHours() <= 10) {
				Itemlatlng Morning_Entity = new Itemlatlng();
				Morning_Entity.setId(Recommend_Time_Log.get(a).getId());
				Morning_Entity.setCreate_time(Recommend_Time_Log.get(a)
						.getCreate_time());
				Morning_Entity.setContent(Recommend_Time_Log.get(a)
						.getContent());
				Morning_Entity.setDashtime((new SimpleDateFormat(DATE_PATTERN))
						.format(Recommend_Time_Log.get(a).getCreate_time()));
				Morning_Entity.setNickname(Recommend_Time_Log.get(a)
						.getNickname());
				Morning_Type.add(Morning_Entity);
			}
			// 昼型のユビキタスラーニングログ
			if (Recommend_Time_Log.get(a).getCreate_time().getHours() >= 11
					&& Recommend_Time_Log.get(a).getCreate_time().getHours() <= 18) {
				Itemlatlng Day_Entity = new Itemlatlng();
				Day_Entity.setId(Recommend_Time_Log.get(a).getId());
				Day_Entity.setCreate_time(Recommend_Time_Log.get(a)
						.getCreate_time());
				Day_Entity.setContent(Recommend_Time_Log.get(a).getContent());
				Day_Entity.setDashtime((new SimpleDateFormat(DATE_PATTERN))
						.format(Recommend_Time_Log.get(a).getCreate_time()));
				Day_Entity.setNickname(Recommend_Time_Log.get(a).getNickname());
				Day_Type.add(Day_Entity);
			}
			// 夜型のユビキタスラーニングログ
			if ((Recommend_Time_Log.get(a).getCreate_time().getHours() >= 19 && Recommend_Time_Log
					.get(a).getCreate_time().getHours() <= 24)
					|| (Recommend_Time_Log.get(a).getCreate_time().getHours() >= 0 && Recommend_Time_Log
							.get(a).getCreate_time().getHours() <= 3)) {
				Itemlatlng Night_Entity = new Itemlatlng();
				Night_Entity.setId(Recommend_Time_Log.get(a).getId());
				Night_Entity.setCreate_time(Recommend_Time_Log.get(a)
						.getCreate_time());
				Night_Entity.setContent(Recommend_Time_Log.get(a).getContent());
				Night_Entity.setDashtime((new SimpleDateFormat(DATE_PATTERN))
						.format(Recommend_Time_Log.get(a).getCreate_time()));
				Night_Entity.setNickname(Recommend_Time_Log.get(a)
						.getNickname());
				Night_Type.add(Night_Entity);
			}

		}

		model.addAttribute("Spring", Spring_Type);
		model.addAttribute("Summer", Summer_Type);
		model.addAttribute("Fall", Fall_Type);
		model.addAttribute("Winter", Winter_Type);

		model.addAttribute("Morning", Morning_Type);
		model.addAttribute("Day", Day_Type);
		model.addAttribute("Night", Night_Type);

		for (int a = 0; a < itemlatlng.size(); a++) {

			switch (itemlatlng.get(a).getCreate_time().getHours()) {

			case 0:
				twenty_four++;
				break;
			case 1:
				one++;
				break;
			case 2:
				two++;
				break;
			case 3:
				three++;
				break;
			case 4:
				four++;
				break;
			case 5:
				five++;
				break;
			case 6:
				six++;
				break;
			case 7:
				seven++;
				break;
			case 8:
				eight++;
				break;
			case 9:
				nine++;
				break;
			case 10:
				ten++;
				break;
			case 11:
				eleven++;
				break;
			case 12:
				twelve++;
				break;
			case 13:
				thirteen++;
				break;
			case 14:
				fourteen++;
				break;
			case 15:
				fifteen++;
				break;
			case 16:
				sixteen++;
				break;
			case 17:
				seventeen++;
				break;
			case 18:
				eighteen++;
				break;
			case 19:
				nineteen++;
				break;
			case 20:
				twenty++;
				break;
			case 21:
				twenty_one++;
				break;
			case 22:
				twenty_two++;
				break;
			case 23:
				twenty_three++;
				break;
			case 24:
				twenty_four++;
				break;
			}
		}

		learningarray4[0] = twenty_four;
		learningarray4[1] = one;
		learningarray4[2] = two;
		learningarray4[3] = three;
		learningarray4[4] = four;
		learningarray4[5] = five;
		learningarray4[6] = six;
		learningarray4[7] = seven;
		learningarray4[8] = eight;
		learningarray4[9] = nine;
		learningarray4[10] = ten;
		learningarray4[11] = eleven;
		learningarray4[12] = twelve;
		learningarray4[13] = thirteen;
		learningarray4[14] = fourteen;
		learningarray4[15] = fifteen;
		learningarray4[16] = sixteen;
		learningarray4[17] = seventeen;
		learningarray4[18] = eighteen;
		learningarray4[19] = nineteen;
		learningarray4[20] = twenty;
		learningarray4[21] = twenty_one;
		learningarray4[22] = twenty_two;
		learningarray4[23] = twenty_three;
	}

	private void Personal_Oneday_data() {
		// TODO Auto-generated method stub
		int one = 0;
		int two = 0;
		int three = 0;
		int four = 0;
		int five = 0;
		int six = 0;
		int seven = 0;
		int eight = 0;
		int nine = 0;
		int ten = 0;
		int eleven = 0;
		int twelve = 0;
		int thirteen = 0;
		int fourteen = 0;
		int fifteen = 0;
		int sixteen = 0;
		int seventeen = 0;
		int eighteen = 0;
		int nineteen = 0;
		int twenty = 0;
		int twenty_one = 0;
		int twenty_two = 0;
		int twenty_three = 0;
		int twenty_four = 0;
		List<Itemlatlng> itemlatlng = itemService.getPersonalOneday(user
				.getId());

		for (int a = 0; a < itemlatlng.size(); a++) {

			switch (itemlatlng.get(a).getCreate_time().getHours()) {

			case 0:
				twenty_four++;
				break;
			case 1:
				one++;
				break;
			case 2:
				two++;
				break;
			case 3:
				three++;
				break;
			case 4:
				four++;
				break;
			case 5:
				five++;
				break;
			case 6:
				six++;
				break;
			case 7:
				seven++;
				break;
			case 8:
				eight++;
				break;
			case 9:
				nine++;
				break;
			case 10:
				ten++;
				break;
			case 11:
				eleven++;
				break;
			case 12:
				twelve++;
				break;
			case 13:
				thirteen++;
				break;
			case 14:
				fourteen++;
				break;
			case 15:
				fifteen++;
				break;
			case 16:
				sixteen++;
				break;
			case 17:
				seventeen++;
				break;
			case 18:
				eighteen++;
				break;
			case 19:
				nineteen++;
				break;
			case 20:
				twenty++;
				break;
			case 21:
				twenty_one++;
				break;
			case 22:
				twenty_two++;
				break;
			case 23:
				twenty_three++;
				break;
			case 24:
				twenty_four++;
				break;
			}
		}

		learningarray3[0] = twenty_four;
		learningarray3[1] = one;
		learningarray3[2] = two;
		learningarray3[3] = three;
		learningarray3[4] = four;
		learningarray3[5] = five;
		learningarray3[6] = six;
		learningarray3[7] = seven;
		learningarray3[8] = eight;
		learningarray3[9] = nine;
		learningarray3[10] = ten;
		learningarray3[11] = eleven;
		learningarray3[12] = twelve;
		learningarray3[13] = thirteen;
		learningarray3[14] = fourteen;
		learningarray3[15] = fifteen;
		learningarray3[16] = sixteen;
		learningarray3[17] = seventeen;
		learningarray3[18] = eighteen;
		learningarray3[19] = nineteen;
		learningarray3[20] = twenty;
		learningarray3[21] = twenty_one;
		learningarray3[22] = twenty_two;
		learningarray3[23] = twenty_three;

		// 朝昼夜

		// morningタイム 4時から11時まで
		morning = four + five + six + seven + eight + nine + ten;
		// 昼型　11時から18時まで
		midday = eleven + twelve + thirteen + fourteen + fifteen + sixteen
				+ seventeen + eighteen;
		// 夜型 18時から4時まで
		night = nineteen + twenty + twenty_one + twenty_two + twenty_three
				+ twenty_four + one + two + three;

		if (morning > midday) {
			if (morning > night) {
				creteria = "Morning Type";
			}
		}

		if (midday > morning) {
			if (midday > night) {
				creteria = "Day Type";
			}
		}

		if (night > morning) {
			if (night > midday) {
				creteria = "Night Type";
			}
		}
	}

	private void AllLearners_data() {
		// TODO Auto-generated method stub
		int j = 1;
		int count = 0;
		for (int i = 0; i < 12; i++) {
			// 現在の日時取得
			Calendar cal1 = Calendar.getInstance();
			Calendar querycalstart = Calendar.getInstance();
			querycalstart.set(cal1.get(Calendar.YEAR), i, 1, 0, 0);
			Calendar querycalstart2 = Calendar.getInstance();
			querycalstart2.set(cal1.get(Calendar.YEAR), j, 1, 0, 0);
			Date dstart = querycalstart.getTime();
			Date dend = querycalstart2.getTime();
			learningarray2[count] = itemService.findcount2(dstart, dend);
			j++;
			count++;
		}
	}

	private void Personal_timedata() {
		// TODO Auto-generated method stub
		int j = 1;
		int count = 0;
		for (int i = 0; i < 12; i++) {
			// 現在の日時取得
			Calendar cal1 = Calendar.getInstance();
			Calendar querycalstart = Calendar.getInstance();
			querycalstart.set(cal1.get(Calendar.YEAR), i, 1, 0, 0);
			Calendar querycalstart2 = Calendar.getInstance();
			querycalstart2.set(cal1.get(Calendar.YEAR), j, 1, 0, 0);
			Date dstart = querycalstart.getTime();
//			System.out.println(dstart);
			Date dend = querycalstart2.getTime();
//			System.out.println(dend);
			learningarray[count] = itemService.findcount(dstart, user.getId(),
					dend);
//			System.out.println(learningarray[count]);
			j++;
			count++;
		}
	}

	private void demo1() throws IOException, UnknownHostException,
			CycApiException {
		Log.current
				.println("Demonstrating getKnownConstantByName api function.\n");
		CycFort snowSkiing = cycAccess.getKnownConstantByName("SnowSkiing");
		Log.current.println("\nThe obtained constant is "
				+ snowSkiing.cyclify());

		System.out.println(snowSkiing.cyclify());
	}

	/**
	 * Demonstrates getConstantGuid api function.
	 */
	protected void demo2() throws IOException, UnknownHostException,
			CycApiException {
		Log.current.println("Demonstrating getConstantGuid api function.\n");
		Guid unitedStatesOfAmericaGuid = cycAccess
				.getConstantGuid("UnitedStatesOfAmerica");

		Log.current.println("\nThe obtained guid is "
				+ unitedStatesOfAmericaGuid);
	}

	/**
	 * Demonstrates getComment api function.
	 */
	protected void demo3() throws IOException, UnknownHostException,
			CycApiException {
		Log.current.println("Demonstrating getComment api function.\n");
		String comment = cycAccess.getComment(cycAccess
				.getKnownConstantByName("bordersOn"));
		Log.current.println("\nThe obtained comment is:\n" + comment);
	}

	/**
	 * Demonstrates getIsas api function.
	 */
	protected void demo4() throws IOException, UnknownHostException,
			CycApiException {
		Log.current.println("Demonstrating getIsas api function.\n");
		CycList isas = cycAccess.getIsas(cycAccess
				.getKnownConstantByName("BillClinton"));
		Log.current.println("\nThe obtained isas are:\n" + isas.cyclify());
	}

	/**
	 * Demonstrates getGenls api function. //Type 取得
	 */
	protected void demo5() throws IOException, UnknownHostException,
			CycApiException {
		Log.current.println("Demonstrating getGenls api function.\n");
		CycList genls = cycAccess.getGenls(cycAccess
				.getKnownConstantByName("Dog"));
		Log.current.println("\nThe obtained direct genls are:\n"
				+ genls.cyclify());
	}

	/**
	 * Demonstrates getArity api function.
	 */
	protected void demo6() throws IOException, UnknownHostException,
			CycApiException {
		Log.current.println("Demonstrating getArity api function.\n");
		int arity = cycAccess.getArity(cycAccess
				.getKnownConstantByName("likesAsFriend"));
		Log.current.println("\nThe obtained arity is " + arity);
	}

	/**
	 * Demonstrates arg1Isas api function.　//subject Type取得
	 */
	protected void demo7() throws IOException, UnknownHostException,
			CycApiException {
		Log.current.println("Demonstrating arg1Isas api function.\n");
		CycList arg1Isas = cycAccess.getArg1Isas(cycAccess
				.getKnownConstantByName("performedBy"));
		Log.current.println("\nThe obtained arg1Isas are:\n"
				+ arg1Isas.cyclify());
	}

	/**
	 * Demonstrates getArgNGenls api function.
	 */
	protected void demo8() throws IOException, UnknownHostException,
			CycApiException {
		Log.current.println("Demonstrating getArgNGenls api function.\n");
		CycList argNGenls = cycAccess.getArgNGenls(
				cycAccess.getKnownConstantByName("skillCapableOf"), 2);
		Log.current.println("\nThe obtained getArgNGenls are:\n"
				+ argNGenls.cyclify());
	}

	/**
	 * Demonstrates getParaphrase (with quantified formula) api function.
	 */
	protected void demo9() throws IOException, UnknownHostException,
			CycApiException {
		// Log.current.println("Demonstrating getParaphrase api function.\n");
		// CycFormulaSentence formula =
		// cycAccess.makeCycSentence("(#$forAll ?THING (#$isa ?Thing #$Thing))");
		// String paraphrase = cycAccess.getParaphrase(formula);
		// Log.current.println("\nThe obtained paraphrase for\n" + formula +
		// "\nis:\n" + paraphrase);
	}

	/**
	 * Demonstrates getParaphrase (with quantified formula) api function.
	 */
	protected void demo10() throws IOException, UnknownHostException,
			CycApiException {
		if (cycAccess.isOpenCyc()) {
			Log.current.println("\nThis demo is not available in OpenCyc");
		} else {
			Log.current.println("Demonstrating getParaphrase api function.\n");
			CycFormulaSentence formula = cycAccess
					.makeCycSentence("(#$thereExists ?PLANET\n" + "  (#$and\n"
							+ "    (#$isa ?PLANET #$Planet)\n"
							+ "    (#$orbits ?PLANET #$Sun)))");
			String paraphrase = cycAccess.getParaphrase(formula);
			Log.current.println("\nThe obtained paraphrase for\n" + formula
					+ "\nis:\n" + paraphrase);
		}
	}

	/**
	 * Demonstrates getImpreciseParaphrase (with quantified formula) api
	 * function.
	 */
	protected void demo11() throws IOException, UnknownHostException,
			CycApiException {
		if (cycAccess.isOpenCyc()) {
			Log.current.println("\nThis demo is not available in OpenCyc");
		} else {
			Log.current
					.println("Demonstrating getImpreciseParaphrase api function.\n");
			CycFormulaSentence formula = cycAccess
					.makeCycSentence("(#$forAll ?PERSON1\n" + "  (#$implies\n"
							+ "    (#$isa ?PERSON1 #$Person)\n"
							+ "    (#$thereExists ?PERSON\n" + "      (#$and\n"
							+ "        (#$isa ?PERSON2 #$Person)\n"
							+ "        (#$loves ?PERSON1 ?PERSON2)))))");
			String paraphrase = cycAccess.getImpreciseParaphrase(formula);
			Log.current.println("\nThe obtained imprecise paraphrase for\n"
					+ formula + "\nis:\n" + paraphrase);
		}
	}

	/**
	 * Demonstrates usage of CycNart and getInstanceSiblings api function.
	 */
	protected void demo12() throws IOException, UnknownHostException,
			CycApiException {
		Log.current
				.println("Demonstrating CycNart and getInstanceSiblings api function.\n");
		CycNart usGovernment = new CycNart(
				cycAccess.getKnownConstantByName("GovernmentFn"),
				cycAccess.getKnownConstantByName("UnitedStatesOfAmerica"));
		CycList siblings = cycAccess.getInstanceSiblings(usGovernment);
		Log.current.println("\nThe obtained instance sibling terms of "
				+ usGovernment + "\nare:\n" + siblings.cyclify());
	}

	/**
	 * Demonstrates usage of isQueryTrue api function.
	 */
	protected void demo13() throws IOException, UnknownHostException,
			CycApiException {
		Log.current.println("Demonstrating isQueryTrue api function.\n");
		CycFormulaSentence gaf = cycAccess
				.makeCycSentence("(#$likesAsFriend #$BillClinton #$JimmyCarter)");
		CycFort mt = cycAccess.getKnownConstantByName("PeopleDataMt");
		InferenceParameters queryProperties = new DefaultInferenceParameters(
				cycAccess);
		boolean isQueryTrue = cycAccess.isQueryTrue(gaf, mt, queryProperties);
		if (isQueryTrue)
			Log.current.println("\nThe assertion\n" + gaf + "\nis true in the "
					+ mt.cyclify());
		else
			Log.current.println("\nThe assertion\n" + gaf
					+ "\nis not known to be true in the " + mt.cyclify());
	}

	/**
	 * Demonstrates usage of the assertGaf api function.
	 */
	protected void demo14() throws IOException, UnknownHostException,
			CycApiException {
		Log.current
				.println("Demonstrating usage of the assertGaf api function.\n");
		CycFort mt = cycAccess.getKnownConstantByName("PeopleDataMt");
		CycFormulaSentence gaf = cycAccess
				.makeCycSentence("(#$likesAsFriend #$BillClinton #$JimmyCarter)");
		cycAccess.assertGaf(gaf, mt);
	}

	/**
	 * Demonstrates usage of the unassertGaf api function.
	 */
	protected void demo15() throws IOException, UnknownHostException,
			CycApiException {
		Log.current
				.println("Demonstrating usage of the unassertGaf api function.\n");
		CycFort mt = cycAccess.getKnownConstantByName("PeopleDataMt");
		CycFormulaSentence gaf = cycAccess
				.makeCycSentence("(#$likesAsFriend #$BillClinton #$JimmyCarter)");
		cycAccess.unassertGaf(gaf, mt);
	}

	/**
	 * Demonstrates usage of the rkfPhraseReader api function.
	 */
	protected void demo16() throws IOException, UnknownHostException,
			CycApiException {
		if (cycAccess.isOpenCyc()) {
			Log.current.println("\nThis demo is not available in OpenCyc");
		} else {
			Log.current
					.println("Demonstrating usage of the rkfPhraseReader api function.\n");
			String phrase = "penguins";
			CycFort inferencePsc = cycAccess
					.getKnownConstantByGuid("bd58915a-9c29-11b1-9dad-c379636f7270");
			CycFort rkfEnglishLexicalMicrotheoryPsc = cycAccess
					.getKnownConstantByGuid("bf6df6e3-9c29-11b1-9dad-c379636f7270");
			CycList parsingExpression = cycAccess.rkfPhraseReader(phrase,
					rkfEnglishLexicalMicrotheoryPsc, inferencePsc);
			Log.current.println("the result of parsing the phrase \"" + phrase
					+ "\" is\n" + parsingExpression);
		}
	}

	/**
	 * Demonstrates usage of the generateDisambiguationPhraseAndTypes api
	 * function.
	 */
	protected void demo17() throws IOException, UnknownHostException,
			CycApiException {
		if (cycAccess.isOpenCyc()) {
			Log.current.println("\nThis demo is not available in OpenCyc");
		} else {
			Log.current
					.println("Demonstrating usage of the generateDisambiguationPhraseAndTypes api function.\n");
			CycFort mt = cycAccess.getKnownConstantByName("PeopleDataMt");
			CycList objects = cycAccess
					.makeCycList("(#$Penguin #$PittsburghPenguins)");
			CycList disambiguationExpression = cycAccess
					.generateDisambiguationPhraseAndTypes(objects);
			Log.current.println("the result of disambiguating the objects \""
					+ objects.cyclify() + "\" is\n" + disambiguationExpression);
		}
	}
// Dash board function***********************************************************************************************************************************************************
	private void Dashboard(ModelMap model){
	model.addAttribute("items", myQuizService.findMyQuizWrongCount(user));
	model.addAttribute("correct_items", myQuizService.findMyQuizCorrectCount(user));
	model.addAttribute("uploadItemRanking",this.itemService.uploadRanking(user.getId()));
	model.addAttribute("numberLogsViews",this.itemService.findAllReadCount(user.getId()));
	model.addAttribute("numberCompletedQuizzes",this.myQuizService.findMyQuizCompletedQuizzesCount(user));
	}

}
