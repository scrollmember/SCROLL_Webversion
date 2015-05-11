/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.ac.tokushima_u.is.ll.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.ac.tokushima_u.is.ll.entity.C2DMessage;
import jp.ac.tokushima_u.is.ll.entity.Cooccurrence;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.Kasetting;
import jp.ac.tokushima_u.is.ll.entity.KnowledgeRanking;
import jp.ac.tokushima_u.is.ll.entity.Language;
import jp.ac.tokushima_u.is.ll.entity.MqQuiz;
import jp.ac.tokushima_u.is.ll.entity.MyQuiz;
import jp.ac.tokushima_u.is.ll.entity.Mychoice;
import jp.ac.tokushima_u.is.ll.entity.PlaceAnalysis;
import jp.ac.tokushima_u.is.ll.entity.PlaceCollocation;
import jp.ac.tokushima_u.is.ll.entity.Quizanalysis;
import jp.ac.tokushima_u.is.ll.entity.Quizstore;
import jp.ac.tokushima_u.is.ll.entity.Results;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.entity.Usertest;
import jp.ac.tokushima_u.is.ll.entity.WordNet;
import jp.ac.tokushima_u.is.ll.entity.yesquizitemget;
import jp.ac.tokushima_u.is.ll.exception.NotFoundException;
import jp.ac.tokushima_u.is.ll.form.AnalysisForm;
import jp.ac.tokushima_u.is.ll.form.QuizAnswerForm;
import jp.ac.tokushima_u.is.ll.quiz.QuizWrapper;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.C2DMService;
import jp.ac.tokushima_u.is.ll.service.C2DMessageService;
import jp.ac.tokushima_u.is.ll.service.GoogleMapService;
import jp.ac.tokushima_u.is.ll.service.ItemQueueService;
import jp.ac.tokushima_u.is.ll.service.ItemService;
import jp.ac.tokushima_u.is.ll.service.LLQuizService;
import jp.ac.tokushima_u.is.ll.service.MyQuizService;
import jp.ac.tokushima_u.is.ll.service.Quizanalysisservice;
import jp.ac.tokushima_u.is.ll.service.UserService;
import jp.ac.tokushima_u.is.ll.service.helper.QuizCondition;
import jp.ac.tokushima_u.is.ll.util.Constants;
import jp.ac.tokushima_u.is.ll.util.KeyGenerateUtil;
import jp.ac.tokushima_u.is.ll.util.SerializeUtil;
import jp.ac.tokushima_u.is.ll.ws.service.model.ItemForm;
import jp.ac.tokushima_u.is.ll.ws.service.model.QuizForm;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.data.attributes.api.AttributeOrigin;
import org.gephi.data.attributes.api.AttributeType;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.spi.GraphExporter;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;
import org.gephi.layout.plugin.random.RandomLayout;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.statistics.plugin.GraphDistance;
import org.hibernate.Hibernate;
import org.openide.util.Lookup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author lemonrain
 */
@Controller
@RequestMapping("/quiz")
public class QuizController {

	@Autowired
	private LLQuizService llquizService;
	@Autowired
	private MyQuizService myquizService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private ItemService itemservice;
	@Autowired
	private ItemQueueService itemQueueService;

	@Autowired
	private UserService userService;
	@Autowired
	private GoogleMapService googleMapService;

	@Autowired
	private C2DMService c2dmService;

	@Autowired
	private C2DMessageService c2dmMessageService;

	@Autowired
	private MyQuizService myQuizService;

	@Autowired
	private Quizanalysisservice quizanalysisservice;

	@Value("${system.staticserverImageUrl}")
	private String staticserverImageUrl;

	private static String RightAnswerComment = "Very good, your answer is right! You can challenge more!";
	private static String WrongAnswerComment = "Sorry, your answer is not right. The right answer is: ";
	private static final String DATE_PATTERN = "yyyy/MM/dd HH:mm";
	// Capturing moving a region to another region
	ArrayList<Cooccurrence> Capturing_Place = new ArrayList<Cooccurrence>();

	 File directory1 = new File(
	 "C:/Users/mouri/work2/learninglogmain/WebContent/js/networkanalysis/");
//	File directory1 = new File("/home/learninglog/NetworkGexf/Gexf");

	@ModelAttribute("googleMapApi")
	public String googleApiKey() {
		return googleMapService.getGoogleMapApi();
	}

	@ModelAttribute("languages")
	public List<Language> getLanguages() {
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		List<Language> langList = user.getStudyLangs();
		Hibernate.initialize(langList);
		return langList;
	}

	public Map<String, Integer> getQuizinfo() {
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		return this.myquizService.searchOneDayQuiz(new Date(), user);
	}

	@RequestMapping
	public String show(@ModelAttribute("form") QuizAnswerForm form,
			ModelMap model) {
		QuizCondition quizCon = new QuizCondition();
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		quizCon.setUser(user);
		if (form.getDashboardtype() != null && form.getDashboardtype() > 0) {
			return "redirect:" + "quiz/" + form.getDashboardtype();
		}
		if (form.getAlarmtype() == null)
			quizCon.setAlarmtype(Constants.WebRequestType);
		quizCon.setVersioncode(Constants.AndroidNewVersion);
		MyQuiz quiz = llquizService.findQuiz(quizCon);

		if (quiz == null) {
			this.itemQueueService.searchUserRecommendedItems(user);
			quiz = llquizService.findQuiz(quizCon);
			if (quiz == null) {
				this.itemQueueService.searchSystemItemQuiz();
				quiz = llquizService.findQuiz(quizCon);
			}
		}
		model.addAttribute("quiz", quiz);

		model.put("quizinfos", this.getQuizinfo());
		// queuetype
		return "quiz/show";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String dashboard(@PathVariable String id,
			@ModelAttribute("form") QuizAnswerForm form, ModelMap model,
			HttpServletRequest request) {
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());

		QuizCondition quizCon = new QuizCondition();

		quizCon.setUser(user);

		if (form.getAlarmtype() == null)
			quizCon.setAlarmtype(Constants.WebRequestType);
		quizCon.setVersioncode(Constants.AndroidNewVersion);

		List<String> items = myQuizService.getItemIdsByIncorrectCount(
				Integer.parseInt(id), user);
		List<String> workedList = (ArrayList<String>) request.getSession()
				.getAttribute("MyItemDashboardWorkList");
		MyQuiz quiz = llquizService.findQuizByDashboard(quizCon, user, items,
				workedList);

		model.addAttribute("quiz", quiz);
		model.addAttribute("dashboardtype", id);

		model.put("quizinfos", this.getQuizinfo());

		return "quiz/show";
	}

	// ■クイズの結果画面へ異動
	@RequestMapping(method = RequestMethod.POST)
	public String answer(@ModelAttribute("form") QuizAnswerForm form,
			ModelMap model, HttpServletRequest request) {
		QuizCondition quizCon = new QuizCondition();
		String quizid = form.getQuizid();
		quizCon.setQuizid(form.getQuizid());
		quizCon.setAnswer(form.getAnswer());
		quizCon.setVersioncode(Constants.AndroidNewVersion);
		quizCon.setAlarmtype(form.getAlarmtype());
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		quizCon.setUser(user);
		// if(form.getPass()!=null&&Constants.QuizPass.equals(form.getPass())){
		if (form.getPass() != null) {
			quizCon.setPass(form.getPass());
			form.setPass(null);
		} else {
			quizCon.setPass(0);
		}

		MyQuiz myquiz = this.llquizService.checkQuiz(quizCon);
		if (form.getDashboardtype() != null && form.getDashboardtype() > 0
				&& quizid != myquiz.getId()) {
			List<String> items = myQuizService.getItemIdsByIncorrectCount(
					form.getDashboardtype(), user);
			List<String> workedList = (ArrayList<String>) request.getSession()
					.getAttribute("MyItemDashboardWorkList");
			myquiz = llquizService.findQuizByDashboard(quizCon, user, items,
					workedList);
		}
		model.addAttribute("quiz", myquiz);
		boolean answered = (myquiz == null || Constants.NotAnsweredState
				.equals(myquiz.getAnswerstate())) ? Boolean.FALSE
				: Boolean.TRUE;
		if (answered) {
			System.out.println("answered" + myquiz.getItem().getId());
			String comment = "";
			if (Constants.CorrectAnsweredState.equals(myquiz.getAnswerstate())) {
				// クイズに正解
				comment = RightAnswerComment;
				model.addAttribute("faceicon",
						"smile" + (RandomUtils.nextInt(5) + 1));
				model.addAttribute("result", true);
			} else {
				// クイズに不正解
				comment = WrongAnswerComment + " " + myquiz.getAnswer();
				model.addAttribute("faceicon", "sad"
						+ (RandomUtils.nextInt(5) + 1));
				model.addAttribute("result", false);
			}
			model.addAttribute("youranswer", myquiz.getMyanswer());
			model.addAttribute("rightanswer", myquiz.getAnswer());
			model.addAttribute("comment", comment);
		}
		model.addAttribute("answered", answered);

		model.addAttribute("dashboardtype", form.getDashboardtype());
		if (form.getDashboardtype() != null && form.getDashboardtype() > 0) {
			List<String> MyItemDashboardWorkList = (ArrayList<String>) request
					.getSession().getAttribute("MyItemDashboardWorkList");
			if (MyItemDashboardWorkList == null) {
				MyItemDashboardWorkList = new ArrayList<String>();
			}
			if (myquiz != null && myquiz.getItem() != null)
				MyItemDashboardWorkList.add(myquiz.getItem().getId());

			request.getSession().setAttribute("MyItemDashboardWorkList",
					MyItemDashboardWorkList);
		}

		model.put("quizinfos", this.getQuizinfo());
		HashMap<String, String[]> params = new HashMap<String, String[]>();
		params.put("quizId", new String[] { quizid });
		C2DMessage c2dmessage = new C2DMessage();
		c2dmessage.setCollapse(Constants.COLLAPSE_KEY_SYNC);
		c2dmessage.setIsDelayIdle(new Integer(0));
		try {
			c2dmessage.setParams(SerializeUtil.serialize(params));
		} catch (Exception e) {

		}
		this.c2dmMessageService.addMessage(c2dmessage, user);

		return "quiz/show";
	}

	// @RequestMapping(method = RequestMethod.POST)
	// public String answer(@ModelAttribute("form") QuizAnswerForm form,
	// ModelMap model, HttpServletRequest request) {
	// QuizCondition quizCon = new QuizCondition();
	// quizCon.setQuizid(form.getQuizid());
	// quizCon.setAnswer(form.getAnswer());
	// quizCon.setVersioncode(Constants.AndroidNewVersion);
	// quizCon.setAlarmtype(form.getAlarmtype());
	// Users user = userService.getById(SecurityUserHolder
	// .getCurrentUser().getId());
	// quizCon.setUser(user);
	// try {
	// if(form.getPass()!=null&&Constants.QuizPass.equals(form.getPass())){
	// quizCon.setPass(Constants.QuizPass);
	// this.llquizService.checkQuiz(quizCon);
	// form.setPass(null);
	// return this.show(form, model);
	// }else{
	// quizCon.setPass(0);
	// MyQuiz myquiz = this.llquizService.checkQuiz(quizCon);
	// model.addAttribute("quiz", myquiz);
	// boolean answered =
	// Constants.NotAnsweredState.equals(myquiz.getAnswerstate())?
	// Boolean.FALSE:Boolean.TRUE;
	// if(answered){
	// String comment = "";
	// if (Constants.CorrectAnsweredState.equals(myquiz.getAnswerstate())) {
	// comment = RightAnswerComment;
	// model.addAttribute("faceicon", "smile"+(RandomUtils.nextInt(5)+1));
	// model.addAttribute("result", true);
	// } else {
	// comment = WrongAnswerComment + " " + myquiz.getAnswer();
	// model.addAttribute("faceicon", "sad"+(RandomUtils.nextInt(5)+1));
	// model.addAttribute("result", false);
	// }
	// model.addAttribute("youranswer", myquiz.getMyanswer());
	// model.addAttribute("rightanswer", myquiz.getAnswer());
	// model.addAttribute("comment", comment);
	// }
	// model.addAttribute("answered", answered);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// model.put("quizinfos", this.getQuizinfo());
	//
	// return "quiz/show";
	// }

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(@ModelAttribute("form") QuizCondition quizCon,
			ModelMap model) {
		model.clear();

		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		quizCon.setUser(user);
		QuizWrapper quizwrapper = this.llquizService.findQuizWrapper(quizCon);
		if (quizwrapper != null) {
			quizCon.setUser(null);
			model.addAttribute("quizform", quizwrapper);
		}
		return "quiz/show";
	}

	@RequestMapping(value = "/myquiz", method = RequestMethod.GET)
	public String mq(@ModelAttribute("form") QuizCondition quizCon,
			ModelMap model) {
		model.clear();

		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		List<MqQuiz> mqquiz = itemService.getquizdata(user.getId());

		// quiz insert
		if (mqquiz.size() != 0) {
			for (int i = 0; i < mqquiz.size(); i++) {
				Date date = new Date();
				mqquiz.get(i).setGid(KeyGenerateUtil.generateIdUUID());
				mqquiz.get(i).setCreate_date(date);
				mqquiz.get(i).setUpdate_date(date);
				itemService.insertmqquiz(mqquiz.get(i));
			}

		}

		Quizautogenerate(user, model);

		return "quiz/myquiz";
	}

	private void Quizautogenerate(Users user, ModelMap model) {
		// TODO Auto-generated method stub
		Random quizchoice = new Random();
		int quiztype = quizchoice.nextInt(10);

		// yes no quiz
		if (quiztype <= 3) {
			List<MqQuiz> gquiz = itemService.getgquiz(user.getId());
			List<Mychoice> selectquiz = new ArrayList<Mychoice>();
			List<Results> results = new ArrayList<Results>();
			if (gquiz.size() != 0) {
				String commonid = KeyGenerateUtil.generateIdUUID();
				Mychoice mc = new Mychoice();
				mc.setId(KeyGenerateUtil.generateIdUUID());
				mc.setContent(gquiz.get(0).getContent());
				mc.setNumber("1");
				mc.setItemid(gquiz.get(0).getItemid());
				mc.setT_mqid(gquiz.get(0).getId());
				mc.setC_id(commonid);
				mc.setAuthorid(user.getId());
				mc.setItem_lat(gquiz.get(0).getItem_lat());
				mc.setItem_lng(gquiz.get(0).getItem_lng());
				mc.setLan_code(gquiz.get(0).getLan_code());
				Calendar now = Calendar.getInstance();
				now.setTime(gquiz.get(0).getUpdate_date());
				now.add(Calendar.DATE, 1);
				Date time = now.getTime();
				mc.setImage(gquiz.get(0).getImage());
				Quizstore quizstore = new Quizstore();
				quizstore.setId(gquiz.get(0).getId());
				quizstore.setTime(time);
				itemService.updatemqquiz(quizstore);
				itemService.insertmqc(mc);
				results = itemService
						.question(mc.getAuthorid(), mc.getItemid());
				// 自分の母国語
				mc.setMycontent(results.get(0).getContent());

				selectquiz.add(mc);
				// common id update
				itemService.updateCommonid(gquiz.get(0).getId(), commonid);
				model.addAttribute("quizzes", selectquiz);
				model.addAttribute("mqtype", "1");

				//補助教材
				List<WordNet> wordnet_sentence = itemService.getwordnet(gquiz.get(0).getContent());
				model.addAttribute("materials",wordnet_sentence);
//				Visualization(gquiz.get(0).getContent());
			} else {
				model.addAttribute("mqtype", "4");

			}

		}
		// image multiple choice quiz
		else if (quiztype > 3 && quiztype < 6) {
			Date cdate = new Date();
			itemService.updatequizdate(cdate);
			List<Results> results = new ArrayList<Results>();
			List<MqQuiz> gquiz = itemService.getimagegquiz(user.getId());
			List<Mychoice> selectquiz = new ArrayList<Mychoice>();
			if (gquiz.size() >= 4) {
				String commonid = KeyGenerateUtil.generateIdUUID();
				for (int i = 0; i < 4; i++) {
					Mychoice mc = new Mychoice();

					mc.setId(KeyGenerateUtil.generateIdUUID());
					mc.setContent(gquiz.get(i).getContent());
					if (i == 0) {
						mc.setNumber("1");
					} else if (i == 1) {
						mc.setNumber("2");
					} else if (i == 2) {
						mc.setNumber("3");
					} else {
						mc.setNumber("4");
					}
					mc.setItemid(gquiz.get(i).getItemid());
					mc.setT_mqid(gquiz.get(i).getId());
					mc.setC_id(commonid);
					mc.setImage(gquiz.get(i).getImage());
					mc.setAuthorid(user.getId());
					Calendar now = Calendar.getInstance();
					now.setTime(gquiz.get(i).getUpdate_date());
					now.add(Calendar.DATE, 1);
					Date time = now.getTime();
					mc.setLan_code(gquiz.get(0).getLan_code());
					Quizstore quizstore = new Quizstore();
					quizstore.setId(gquiz.get(i).getId());
					quizstore.setTime(time);
					itemService.updatemqquiz(quizstore);
					itemService.insertmqc(mc);
					results = itemService.question(mc.getAuthorid(),
							mc.getItemid());
					mc.setMycontent(results.get(0).getContent());

					selectquiz.add(mc);
					// common id update
					itemService.updateCommonid(gquiz.get(i).getId(), commonid);

				}

				Random rnd = new Random();
				int ran = rnd.nextInt(10);
				if (ran <= 1) {
					selectquiz.get(0).setAnswer("1");
				} else if (ran >= 2 && ran <= 4) {
					selectquiz.get(1).setAnswer("1");
				} else if (ran >= 5 && ran <= 7) {
					selectquiz.get(2).setAnswer("1");
				} else {
					selectquiz.get(3).setAnswer("1");
				}

				model.addAttribute("quizzes", selectquiz);
				model.addAttribute("mqtype", "3");
				
			} else {
				model.addAttribute("mqtype", "4");
			}
		}

		// text multiple choice quiz
		else {

			Date cdate = new Date();
			itemService.updatequizdate(cdate);
			List<Results> results = new ArrayList<Results>();
			List<MqQuiz> gquiz = itemService.getgquiz(user.getId());
			List<Mychoice> selectquiz = new ArrayList<Mychoice>();
			if (gquiz.size() >= 4) {

				String commonid = KeyGenerateUtil.generateIdUUID();
				for (int i = 0; i < 4; i++) {
					Mychoice mc = new Mychoice();

					mc.setId(KeyGenerateUtil.generateIdUUID());
					mc.setContent(gquiz.get(i).getContent());
					if (i == 0) {
						mc.setNumber("1");
					} else if (i == 1) {
						mc.setNumber("2");
					} else if (i == 2) {
						mc.setNumber("3");
					} else {
						mc.setNumber("4");
					}
					mc.setItemid(gquiz.get(i).getItemid());
					mc.setT_mqid(gquiz.get(i).getId());
					mc.setC_id(commonid);
					mc.setAuthorid(user.getId());
					mc.setLan_code(gquiz.get(0).getLan_code());
					Calendar now = Calendar.getInstance();
					now.setTime(gquiz.get(i).getUpdate_date());
					now.add(Calendar.DATE, 1);
					Date time = now.getTime();

					Quizstore quizstore = new Quizstore();
					quizstore.setId(gquiz.get(i).getId());
					quizstore.setTime(time);
					itemService.updatemqquiz(quizstore);
					itemService.insertmqc(mc);
					results = itemService.question(mc.getAuthorid(),
							mc.getItemid());
					mc.setMycontent(results.get(0).getContent());

					selectquiz.add(mc);
					// common id update
					itemService.updateCommonid(gquiz.get(i).getId(), commonid);

				}

				Random rnd = new Random();
				int ran = rnd.nextInt(10);
				if (ran <= 1) {
					selectquiz.get(0).setAnswer("1");
				} else if (ran >= 2 && ran <= 4) {
					selectquiz.get(1).setAnswer("1");
				} else if (ran >= 5 && ran <= 7) {
					selectquiz.get(2).setAnswer("1");
				} else {
					selectquiz.get(3).setAnswer("1");
				}

				model.addAttribute("quizzes", selectquiz);
				model.addAttribute("mqtype", "2");
			} else {
				model.addAttribute("mqtype", "4");
			}
		}

	}

	@RequestMapping(value = "/item", method = RequestMethod.POST)
	public String itemRemember(ModelMap model) {
		// Users user = userService.getById(SecurityUserHolder.getCurrentUser()
		// .getId());

		return "quiz/show";
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public @ResponseBody
	QuizForm test(@ModelAttribute("form") QuizCondition quizcon, ModelMap model) {
		QuizForm quizform = new QuizForm();

		quizform.setContent("asdfaf test");

		return quizform;
	}

	@RequestMapping(value = "/check", method = RequestMethod.POST)
	public String check(@ModelAttribute("form") QuizCondition quizcon,
			ModelMap model) {
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		quizcon.setUser(user);

		QuizWrapper quizwrapper = this.llquizService.checkQuizWrapper(quizcon);

		if (quizwrapper != null) {
			model.clear();
			model.addAttribute("quizform", quizwrapper);
		}
		return "quiz/show";
	}

	// TODO dameng

	// @RequestMapping(value="/check", method=RequestMethod.POST)
	// public String check(@ModelAttribute("form") QuizCondition quizcon,
	// ModelMap model) {
	// Users user = userService.getById(SecurityUserHolder.getCurrentUser()
	// .getId());
	//
	// String email = null;
	// if (user.getMobileEmail() != null
	// && user.getMobileEmail().length() > 0) {
	// email = user.getMobileEmail();
	// } else {
	// email = user.getPcEmail();
	// }
	//
	// quizcon.setUser(user);
	//
	// MyQuiz myquiz = this.llquizService.checkAnswer(quizcon);
	//
	// quizcon.setUser(null);
	// if(Constants.QuizPass.equals(quizcon.getPass()))
	// return "quiz/show";
	//
	// QuizForm quizform = null;
	//
	// if (myquiz != null) {
	// quizform = new QuizForm();
	// quizform.setEmail(email);
	// if (Constants.rightAnswerComment.equals(myquiz.getAnswerstate())) {
	// quizform.setComment(Constants.rightAnswerComment);
	// } else {
	// quizform.setComment(Constants.wrongAnswerComment);
	// }
	// quizform.setId(myquiz.getId());
	// List<MyQuizChoice> choices = myquiz.getLlquiz().getChoices();
	// if (choices != null && choices.size() >= 4) {
	// if (choices.get(0) != null) {
	// quizform.setChoice1(choices.get(0).getContent());
	// quizform.setNote1(choices.get(0).getNote());
	// }
	// if (choices.get(1) != null) {
	// quizform.setChoice2(choices.get(1).getContent());
	// quizform.setNote2(choices.get(1).getNote());
	// }
	// if (choices.get(2) != null) {
	// quizform.setChoice3(choices.get(2).getContent());
	// quizform.setNote3(choices.get(2).getNote());
	// }
	// if (choices.get(3) != null) {
	// quizform.setChoice4(choices.get(3).getContent());
	// quizform.setNote4(choices.get(3).getNote());
	// }
	// }
	// quizform.setQuiztypeid(myquiz.getLlquiz().getQuiztypeid());
	// quizform.setPhotourl(myquiz.getLlquiz().getPhotoUrl());
	// quizform.setContent(myquiz.getLlquiz().getQuizcontent());
	// quizform.setAnswer(Integer.valueOf(myquiz.getLlquiz().getAnswer()));
	// quizform.setAnswerstate(myquiz.getAnswerstate());
	// model.clear();
	// model.addAttribute("quizform", quizform);
	// }
	// return "quiz/show";
	// }

	@RequestMapping(value = "/menu", method = RequestMethod.GET)
	public String menu(@ModelAttribute("form") AnalysisForm form,
			ModelMap model, HttpServletRequest request) {
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		List<Quizanalysis> quizhistory = this.quizanalysisservice
				.getwronghistory(user.getId());
		model.addAttribute("quizwrongitems", quizhistory);

		List<Object[]> quizRankingList = this.myQuizService
				.getQuizScoreRanking();
		model.addAttribute("quizRanking", quizRankingList);

		// ■wakebe クイズ自分の順位
		int myQuizRank = 0;
		for (int i = 0; i < quizRankingList.size(); i++) {
			if (user.getId()
					.equals(((Users) quizRankingList.get(i)[0]).getId())) {
				myQuizRank = i;
				break;
			}
		}
		int myQuizRankStart = myQuizRank - 2;
		int myQuizRankEnd = myQuizRank + 2;
		if (myQuizRankStart < 0)
			myQuizRankStart = 0;
		if (myQuizRankEnd < 4)
			myQuizRankEnd = 4;
		model.addAttribute("myQuizRank", myQuizRank);
		model.addAttribute("myQuizRankStart", myQuizRankStart);
		model.addAttribute("myQuizRankEnd", myQuizRankEnd);
		return "quiz/menu";
	}

	@RequestMapping(value = "/logs", method = RequestMethod.GET)
	public String log(@ModelAttribute("form") AnalysisForm form,
			ModelMap model, HttpServletRequest request) {
		// Users user = userService.getById(SecurityUserHolder.getCurrentUser()
		// .getId());
		List<Quizanalysis> quizdata = this.quizanalysisservice
				.getalluserquizdata();
		model.addAttribute("quizdata", quizdata);

		return "quiz/logs";
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String level1(@ModelAttribute("form") AnalysisForm form,
			ModelMap model, HttpServletRequest request) {
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		List<Quizanalysis> quizhistory = this.quizanalysisservice
				.getwronghistory(user.getId());
		model.addAttribute("quizwrongitems", quizhistory);

		return "quiz/create";
	}

	@RequestMapping(value = "/yesnocreate", method = RequestMethod.POST)
	public String level2(String itemname, ModelMap model,
			HttpServletRequest request) {
		Users user = userService.getById(SecurityUserHolder.getCurrentUser()
				.getId());
		System.out.println(itemname);
		return "quiz/menu";
	}

	@RequestMapping(value = "/searchItem", method = RequestMethod.GET)
	public String test(@ModelAttribute("analysisCond") AnalysisForm form,
			String queryvalue, String taskId, Integer num, ModelMap model,
			HttpServletResponse response) {
		model.clear();
		List<Item> items = itemService.searchRelatedItemForTask(taskId,
				queryvalue, num);
		if (items != null && items.size() > 0) {
			List<ItemForm> forms = new ArrayList<ItemForm>();
			for (Item item : items) {
				ItemForm itemform = new ItemForm(item);
				forms.add(itemform);
			}
			model.addAttribute("items", forms);

		}
		return "index";
	}

	@RequestMapping(value = "/createitem", method = RequestMethod.GET)
	public String getitems(String itemid, ModelMap model,
			HttpServletResponse response) {
		model.clear();
		List<yesquizitemget> items = itemService.getyesquizitems(itemid);
		if (items != null && items.size() > 0) {
			model.addAttribute("items", items);

			String imageurl = "http://ll.artsci.kyushu-u.ac.jp/static/learninglog/"
					+ items.get(0).getImage() + "_320x240.png";
			// System.out.println(imageurl);
			model.addAttribute("imageurl", imageurl);

		}
		return "quiz/create";
	}

	@RequestMapping(value = "/additem", method = RequestMethod.POST)
	public String addRelatedItem(@PathVariable String id, String[] itemIds,
			ModelMap model) {

		model.put("result", "success");
		return "";
	}

	private void Visualization(String content) {
		int nodecount = 0;

		// UserID取得
		Usertest userid = userService.findById(SecurityUserHolder
				.getCurrentUser().getId());
		Kasetting ka = userService.getka(SecurityUserHolder.getCurrentUser()
				.getId());

		// KALens network graph
		// create*************************************************************************************************************//
		ProjectController pc = Lookup.getDefault().lookup(
				ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();

		// import file//
		ImportController importController = Lookup.getDefault().lookup(
				ImportController.class);
		Container container = null;

		GraphModel graphModel = Lookup.getDefault()
				.lookup(GraphController.class).getModel();
		GraphModel graphModel2 = Lookup.getDefault()
				.lookup(GraphController.class).getModel();
		AttributeModel attributeModel = Lookup.getDefault()
				.lookup(AttributeController.class).getModel();
		// Add Node column
		AttributeColumn Hyper = attributeModel.getNodeTable().addColumn(
				"Authorid", AttributeType.STRING);
		AttributeColumn node_tagerItem = attributeModel.getNodeTable()
				.addColumn("Targetitem", AttributeType.STRING);
		AttributeColumn node_tagerItemid = attributeModel.getNodeTable()
				.addColumn("Targetitemid", AttributeType.STRING);
		AttributeColumn node_NextItem = attributeModel.getNodeTable()
				.addColumn("Nextitem", AttributeType.STRING);
		AttributeColumn node_NextItemid = attributeModel.getNodeTable()
				.addColumn("Nextitemid", AttributeType.STRING);
		AttributeColumn target_author_name = attributeModel.getNodeTable()
				.addColumn("TAname", AttributeType.STRING);
		AttributeColumn next_author_name = attributeModel.getNodeTable()
				.addColumn("NAname", AttributeType.STRING);
		AttributeColumn knowledgerank = attributeModel.getNodeTable()
				.addColumn("KnowledgeRanking", AttributeType.STRING);
		AttributeColumn knowledgenumber = attributeModel.getNodeTable()
				.addColumn("Knowledgenumber", AttributeType.STRING);
		AttributeColumn nickname = attributeModel.getNodeTable().addColumn(
				"nickname", AttributeType.STRING);
		AttributeColumn userlevel = attributeModel.getNodeTable().addColumn(
				"userlevel", AttributeType.STRING);
		AttributeColumn checknumber = attributeModel.getNodeTable().addColumn(
				"checknumber", AttributeType.STRING);
		AttributeColumn createtime = attributeModel.getNodeTable().addColumn(
				"createtime", AttributeType.STRING);
		AttributeColumn item_lat = attributeModel.getNodeTable().addColumn(
				"item_lat", AttributeType.STRING);
		AttributeColumn item_lng = attributeModel.getNodeTable().addColumn(
				"item_lng", AttributeType.STRING);
		AttributeColumn author_id = attributeModel.getNodeTable().addColumn(
				"author_id", AttributeType.STRING);

		AttributeColumn place = attributeModel.getNodeTable().addColumn(
				"place", AttributeType.STRING);
		AttributeColumn placeId = attributeModel.getNodeTable().addColumn(
				"placeId", AttributeType.STRING);
		AttributeColumn place_attribute = attributeModel.getNodeTable()
				.addColumn("place_attribute", AttributeType.STRING);

		AttributeColumn checklearner1 = attributeModel.getNodeTable()
				.addColumn("checklearner1", AttributeType.STRING);

		AttributeColumn checkword1 = attributeModel.getNodeTable().addColumn(
				"checkword1", AttributeType.STRING);

		AttributeColumn checkplace1 = attributeModel.getNodeTable().addColumn(
				"checkplace1", AttributeType.STRING);

		AttributeColumn checktime1 = attributeModel.getNodeTable().addColumn(
				"checktime1", AttributeType.STRING);
		AttributeColumn checkknowledge2 = attributeModel.getNodeTable()
				.addColumn("checkknowledge2", AttributeType.STRING);
		// degrees = attributeModel.getNodeTable().addColumn("degree", "Degree",
		// AttributeType.INT, AttributeOrigin.COMPUTED, 0);
		GraphController gc = Lookup.getDefault().lookup(GraphController.class);
		GraphController gc2 = Lookup.getDefault().lookup(GraphController.class);
		graphModel = gc.getModel();
		graphModel2 = gc2.getModel();
		Graph graph = gc.getModel().getDirectedGraph();
		Graph graph3 = gc.getModel().getUndirectedGraph();
		Graph graph2 = gc2.getModel().getDirectedGraph();
		Node[] n = new Node[10000];
		Node[] n2 = new Node[10000];
		Node[] n3 = new Node[10000];
		Date[] timer1 = new Date[10];
		int timer_1 = 0;
		List<Users> user = userService.findBynetworkuser();
		ArrayList<Cooccurrence> Currence = new ArrayList<Cooccurrence>();
		ArrayList<Cooccurrence> PlaceCurrence = new ArrayList<Cooccurrence>();
		ArrayList<Cooccurrence> Place_distinct_Currence = new ArrayList<Cooccurrence>();

		List<Cooccurrence> coocurrence = new ArrayList<Cooccurrence>();
		List<Cooccurrence> timelist = new ArrayList<Cooccurrence>();
		List<Cooccurrence> placecoocurrence = new ArrayList<Cooccurrence>();
		List<Cooccurrence> place_distinct_coocurrence = new ArrayList<Cooccurrence>();

		List<PlaceCollocation> placecollocation = new ArrayList<PlaceCollocation>();
		// user ranking and knowledge
		List<KnowledgeRanking> knowledgeranking = userService
				.findknowledgeranking();

		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
		// String datedata = sdf.format(element.getCreateTime());

		// Word Collocational Networks
		for (int u = 0; u < user.size(); u++) {
			// abc userを除く
			if (user.get(u).getId().equals("16bb9432301b850401301bf6abcc0308")) {

			} else {
				coocurrence = itemservice.cooccurrence(user.get(u).getId());
				int j = 1;

				// 共起検索************************************************************************************************************************************//
				for (int i = 0; i < coocurrence.size(); i++) {

					if (coocurrence.size() > j) {
						Cooccurrence currenceentity = new Cooccurrence();
						currenceentity.setTarget_content(coocurrence.get(i)
								.getContent());
						currenceentity.setNext_content(coocurrence.get(j)
								.getContent());
						currenceentity.setTarget_itemid(coocurrence.get(i)
								.getItemid());
						currenceentity.setNext_itemid(coocurrence.get(j)
								.getItemid());
						currenceentity.setLanguage(coocurrence.get(i)
								.getLanguage());
						currenceentity.setTarget_authorid(coocurrence.get(i)
								.getAuthorid());
						currenceentity.setNext_authorid(coocurrence.get(j)
								.getAuthorid());

						currenceentity.setNickname(coocurrence.get(i)
								.getNickname());
						currenceentity.setUser_level(coocurrence.get(i)
								.getUser_level());
						currenceentity.setItemcreate(coocurrence.get(i)
								.getItemcreate());
						currenceentity.setPlace(coocurrence.get(i).getPlace());
						// for(int
						// knowledgecount=0;knowledgecount<knowledgeranking.size();knowledgecount++){
						//
						// }
						// System.out.println(coocurrence.get(i).getContent() +
						// "->"
						// + coocurrence.get(j).getContent());
						if (content.equalsIgnoreCase(coocurrence.get(i)
								.getContent())) {
							Cooccurrence timelister = new Cooccurrence();

							timer1[timer_1] = coocurrence.get(i)
									.getItemcreate();
							timer_1++;
						}
						Currence.add(currenceentity);

						j++;
					}
				}

			}
		}

		// Place Collocational Networks 全体の共起ネットワークの場所を作成
		for (int u = 0; u < user.size(); u++) {
			// abc userを除く
			if (user.get(u).getId().equals("16bb9432301b850401301bf6abcc0308")) {

			} else {
				placecoocurrence = itemservice.placecooccurrence(user.get(u)
						.getId());
				int j = 1;

				// 共起検索************************************************************************************************************************************//
				for (int i = 0; i < placecoocurrence.size(); i++) {

					if (placecoocurrence.size() > j) {
						Cooccurrence currenceentity = new Cooccurrence();
						currenceentity.setTarget_content(placecoocurrence
								.get(i).getContent());
						currenceentity.setNext_content(placecoocurrence.get(j)
								.getContent());
						currenceentity.setTarget_place(placecoocurrence.get(i)
								.getPlace());
						currenceentity.setNext_place(placecoocurrence.get(j)
								.getPlace());
						currenceentity.setTarget_place_id(placecoocurrence.get(
								i).getPlaceId());
						currenceentity.setNext_place_id(placecoocurrence.get(j)
								.getPlaceId());
						currenceentity.setTarget_place_itemid(placecoocurrence
								.get(i).getItem());

						currenceentity.setNext_place_itemid(placecoocurrence
								.get(j).getItem());

						PlaceCurrence.add(currenceentity);
						j++;
					}
				}

			}
		}

		// Place Collocational Networks 全体の共起ネットワークの場所を作成(重複無)

		for (int u = 0; u < user.size(); u++) {
			// abc userを除く
			if (user.get(u).getId().equals("16bb9432301b850401301bf6abcc0308")) {

			} else {
				// Capturing moving a region to another region
				if (user.get(u).getId()
						.equals(SecurityUserHolder.getCurrentUser().getId())) {
					place_distinct_coocurrence = itemservice
							.place_distinct_cooccurrence(user.get(u).getId());
					for (int i = 0; i < place_distinct_coocurrence.size(); i++) {
						int j = 1;
						if (place_distinct_coocurrence.size() > j) {
							Cooccurrence currenceentity = new Cooccurrence();
							currenceentity
									.setTarget_content(place_distinct_coocurrence
											.get(i).getContent());
							currenceentity
									.setNext_content(place_distinct_coocurrence
											.get(j).getContent());
							currenceentity
									.setTarget_place(place_distinct_coocurrence
											.get(i).getPlace());
							currenceentity
									.setNext_place(place_distinct_coocurrence
											.get(j).getPlace());
							currenceentity
									.setTarget_place_id(place_distinct_coocurrence
											.get(i).getPlaceId());
							currenceentity
									.setNext_place_id(place_distinct_coocurrence
											.get(j).getPlaceId());
							currenceentity
									.setTarget_place_itemid(place_distinct_coocurrence
											.get(i).getItem());

							currenceentity
									.setNext_place_itemid(place_distinct_coocurrence
											.get(j).getItem());
							SimpleDateFormat sdf10 = new SimpleDateFormat(
									DATE_PATTERN);
							String ptime = sdf10
									.format(place_distinct_coocurrence.get(i)
											.getItemcreate());
							currenceentity.setItemtime(ptime);
							currenceentity.setItem(place_distinct_coocurrence
									.get(i).getItem());
							currenceentity.setLat(place_distinct_coocurrence
									.get(i).getLat());
							currenceentity.setLng(place_distinct_coocurrence
									.get(i).getLng());
							Capturing_Place.add(currenceentity);

							j++;
						}
					}

				}
				// Capturing end ********************************************
				place_distinct_coocurrence = itemservice
						.place_distinct_cooccurrence(user.get(u).getId());
				int j = 1;

				// 共起検索************************************************************************************************************************************//
				for (int i = 0; i < place_distinct_coocurrence.size(); i++) {

					if (place_distinct_coocurrence.size() > j) {
						Cooccurrence currenceentity = new Cooccurrence();
						currenceentity
								.setTarget_content(place_distinct_coocurrence
										.get(i).getContent());
						currenceentity
								.setNext_content(place_distinct_coocurrence
										.get(j).getContent());
						currenceentity
								.setTarget_place(place_distinct_coocurrence
										.get(i).getPlace());
						currenceentity.setNext_place(place_distinct_coocurrence
								.get(j).getPlace());
						currenceentity
								.setTarget_place_id(place_distinct_coocurrence
										.get(i).getPlaceId());
						currenceentity
								.setNext_place_id(place_distinct_coocurrence
										.get(j).getPlaceId());
						currenceentity
								.setTarget_place_itemid(place_distinct_coocurrence
										.get(i).getItem());

						currenceentity
								.setNext_place_itemid(place_distinct_coocurrence
										.get(j).getItem());

						Place_distinct_Currence.add(currenceentity);

						j++;
					}
				}

			}
		}

		for (int i3 = 0; i3 < Currence.size(); i3++) {
			for (int knowledgecount = 0; knowledgecount < knowledgeranking
					.size(); knowledgecount++) {
				if (Currence.get(i3).getTarget_authorid() != null
						&& knowledgeranking.get(knowledgecount).getAuthor_id() != null) {
					if (Currence
							.get(i3)
							.getTarget_authorid()
							.equals(knowledgeranking.get(knowledgecount)
									.getAuthor_id())) {
						Cooccurrence currenceentity = new Cooccurrence();
						currenceentity.setTarget_content(Currence.get(i3)
								.getTarget_content());
						currenceentity.setNext_content(Currence.get(i3)
								.getNext_content());
						currenceentity.setTarget_itemid(Currence.get(i3)
								.getTarget_itemid());
						currenceentity.setNext_itemid(Currence.get(i3)
								.getNext_itemid());
						currenceentity.setLanguage(Currence.get(i3)
								.getLanguage());
						currenceentity.setTarget_authorid(Currence.get(i3)
								.getTarget_authorid());
						currenceentity.setNext_authorid(Currence.get(i3)
								.getNext_authorid());
						currenceentity.setKnowledgeranking(knowledgeranking
								.get(knowledgecount).getRanking());
						currenceentity.setKnowledgnumber(knowledgeranking.get(
								knowledgecount).getN());
						currenceentity.setPlace(Currence.get(i3).getPlace());
						currenceentity.setNickname(Currence.get(i3)
								.getNickname());
						currenceentity.setUser_level(Currence.get(i3)
								.getUser_level());
						currenceentity.setItemcreate(Currence.get(i3)
								.getItemcreate());

						Currence.set(i3, currenceentity);
					}
				}
			}

		}

		// Knowledge
		int Yrange = 0;
		int Ycount = 0;
		int Y = 500;
		int Y2 = 500;
		// 共起ネットワーク生成************************************************************************************************************************************//
		// learners 1
		for (int i = 0; i < Currence.size(); i++) {
			// form.getTitleMap().get("en")
			if (Currence.get(i).getTarget_content().equalsIgnoreCase(content)) {
				n[nodecount] = graphModel.factory().newNode(
						String.valueOf(nodecount));
				n[nodecount].getNodeData().setLabel(
						Currence.get(i).getNickname());
				// Bule
				if (Currence.get(i).getKnowledgeranking() == null
						|| Currence.get(i).getKnowledgeranking().equals("null")) {
					n[nodecount].getNodeData().setColor(0.0f, 0.0f, 1.0f);
					n[nodecount].getNodeData().setSize(18f);
				} else if (Integer.parseInt(Currence.get(i)
						.getKnowledgeranking()) <= 5) {
					n[nodecount].getNodeData().setColor(0.0f, 1.0f, 0.0f);
					n[nodecount].getNodeData().setSize(25f);
				} else {
					n[nodecount].getNodeData().setColor(0.0f, 0.0f, 1.0f);
					n[nodecount].getNodeData().setSize(18f);
				}

				n[nodecount].getNodeData().setX(-300);

				Ycount++;
				if ((Ycount % 2) == 0) {
					Y = Y + 100;
					n[nodecount].getNodeData().setY(Y);

				} else {
					Y = Y + 100;
					n[nodecount].getNodeData().setY(Y);
				}
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(Hyper.getIndex(),
								Currence.get(i).getTarget_authorid());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(node_tagerItem.getIndex(),
								Currence.get(i).getTarget_content());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(node_tagerItemid.getIndex(),
								Currence.get(i).getTarget_itemid());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(node_NextItem.getIndex(),
								Currence.get(i).getNext_content());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(node_NextItemid.getIndex(),
								Currence.get(i).getNext_itemid());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(knowledgerank.getIndex(),
								Currence.get(i).getKnowledgeranking());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(knowledgenumber.getIndex(),
								Currence.get(i).getKnowledgnumber());

				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(nickname.getIndex(),
								Currence.get(i).getNickname());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(userlevel.getIndex(),
								Currence.get(i).getUser_level());
				String datedata = sdf.format(Currence.get(i).getItemcreate());
				n[nodecount].getNodeData().getAttributes()
						.setValue(createtime.getIndex(), datedata);
				n[nodecount].getNodeData().getAttributes()
						.setValue(checknumber.getIndex(), "1");
				n[nodecount].getNodeData().getAttributes()
						.setValue(checklearner1.getIndex(), "1");
				graph.addNode(n[nodecount]);
				// System.out.println("First:" + nodecount);
				nodecount++;

			}

		}

		// knowledge1 generate 最初の単語
		String targetword = "";
		String knowledge1_timer = "";
		// String targettime="";
		for (Node no : graph.getNodes().toArray()) {
			targetword = String.valueOf(no.getNodeData().getAttributes()
					.getValue(node_tagerItem.getIndex()));
			knowledge1_timer = String.valueOf(no.getNodeData().getAttributes()
					.getValue(createtime.getIndex()));
			// targettime=String.valueOf(no.getNodeData().getAttributes()
			// .getValue(createtime.getIndex()));
			// System.out.println("TargetTime:"+knowledge1_timer);

		}
		n[nodecount] = graphModel.factory().newNode(String.valueOf(nodecount));
		n[nodecount].getNodeData().setLabel(targetword);
		// Yellow
		n[nodecount].getNodeData().setColor(1.0f, 1.0f, 0.0f);
		n[nodecount].getNodeData().setSize(130f);
		n[nodecount].getNodeData().setX(0);
		n[nodecount].getNodeData().setY(500);
		n[nodecount].getNodeData().getAttributes()
				.setValue(checknumber.getIndex(), "knowledge");
		graph.addNode(n[nodecount]);

		// ************TimeCount 定数値
		// 3500が初期**********************************************************************************************
		int timecount = 3500;
		int YT = 500;
		for (int t1 = 0; t1 < timer1.length; t1++) {
			// if(!StringUtils.isBlank(sdf.format(timer1[t1]))){
			if (timer1[t1] != null) {
				n2[timecount] = graphModel.factory().newNode(
						String.valueOf(timecount));
				n2[timecount].getNodeData().setLabel(sdf.format(timer1[t1]));
				n2[timecount].getNodeData().setColor(0.7f, 0.5f, 0.3f);
				n2[timecount].getNodeData().setSize(35f);
				n2[timecount].getNodeData().setX(300);
				YT = YT + 100;
				n2[timecount].getNodeData().setY(YT);
				n2[timecount].getNodeData().getAttributes()
						.setValue(checknumber.getIndex(), "2");
				graph.addNode(n2[timecount]);
				Edge e10 = graphModel.factory().newEdge(
						KeyGenerateUtil.generateIdUUID(), n[nodecount],
						n2[timecount], 1f, true);

				graph.addEdge(e10);
				timecount++;
				int YAT = 500;
				int YAT2 = 500;
				// 春型のユビキタスラーニングログ
				if (timer1[t1].getMonth() + 1 >= 3
						&& timer1[t1].getMonth() + 1 <= 5) {
					n2[timecount] = graphModel.factory().newNode(
							String.valueOf(timecount));
					n2[timecount].getNodeData().setLabel("Spring");
					n2[timecount].getNodeData().setColor(0.7f, 0.5f, 0.3f);
					n2[timecount].getNodeData().setSize(25f);
					n2[timecount].getNodeData().setX(600);
					YAT = YAT;
					n2[timecount].getNodeData().setY(YAT);
					n2[timecount].getNodeData().getAttributes()
							.setValue(checknumber.getIndex(), "2");
					graph.addNode(n2[timecount]);
					Edge e = graphModel.factory().newEdge(
							KeyGenerateUtil.generateIdUUID(),
							n2[timecount - 1], n2[timecount], 1f, true);

					graph.addEdge(e);
					timecount++;

				}
				// 夏型のユビキタスラーニングログ
				if (timer1[t1].getMonth() + 1 >= 6
						&& timer1[t1].getMonth() + 1 <= 8) {

					n2[timecount] = graphModel.factory().newNode(
							String.valueOf(timecount));
					n2[timecount].getNodeData().setLabel("Summer");
					n2[timecount].getNodeData().setColor(0.7f, 0.5f, 0.3f);
					n2[timecount].getNodeData().setSize(25f);
					n2[timecount].getNodeData().setX(600);
					YAT = YAT + 200;
					n2[timecount].getNodeData().setY(YAT);
					n2[timecount].getNodeData().getAttributes()
							.setValue(checknumber.getIndex(), "2");
					graph.addNode(n2[timecount]);
					Edge e = graphModel.factory().newEdge(
							KeyGenerateUtil.generateIdUUID(),
							n2[timecount - 1], n2[timecount], 1f, true);

					graph.addEdge(e);
					timecount++;
				}

				// 秋型のユビキタスラーニングログ
				if (timer1[t1].getMonth() + 1 >= 9
						&& timer1[t1].getMonth() + 1 <= 11) {

					n2[timecount] = graphModel.factory().newNode(
							String.valueOf(timecount));
					n2[timecount].getNodeData().setLabel("Fall");
					n2[timecount].getNodeData().setColor(0.7f, 0.5f, 0.3f);
					n2[timecount].getNodeData().setSize(25f);
					n2[timecount].getNodeData().setX(600);
					YAT = YAT + 300;
					n2[timecount].getNodeData().setY(YAT);
					n2[timecount].getNodeData().getAttributes()
							.setValue(checknumber.getIndex(), "2");
					graph.addNode(n2[timecount]);
					Edge e = graphModel.factory().newEdge(
							KeyGenerateUtil.generateIdUUID(),
							n2[timecount - 1], n2[timecount], 1f, true);

					graph.addEdge(e);
					timecount++;
				}

				// 冬型のユビキタスラーニングログ
				if (timer1[t1].getMonth() + 1 >= 0
						&& timer1[t1].getMonth() + 1 <= 2) {

					n2[timecount] = graphModel.factory().newNode(
							String.valueOf(timecount));
					n2[timecount].getNodeData().setLabel("Winter");
					n2[timecount].getNodeData().setColor(0.7f, 0.5f, 0.3f);
					n2[timecount].getNodeData().setSize(25f);
					n2[timecount].getNodeData().setX(600);
					YAT = YAT + 400;
					n2[timecount].getNodeData().setY(YAT);
					n2[timecount].getNodeData().getAttributes()
							.setValue(checknumber.getIndex(), "2");
					graph.addNode(n2[timecount]);
					Edge e = graphModel.factory().newEdge(
							KeyGenerateUtil.generateIdUUID(),
							n2[timecount - 1], n2[timecount], 1f, true);

					graph.addEdge(e);
					timecount++;
				}

				// 朝型のユビキタスラーニングログ
				if (timer1[t1].getHours() >= 4 && timer1[t1].getHours() <= 10) {

					n2[timecount] = graphModel.factory().newNode(
							String.valueOf(timecount));
					n2[timecount].getNodeData().setLabel("Morning");
					n2[timecount].getNodeData().setColor(0.7f, 0.5f, 0.3f);
					n2[timecount].getNodeData().setSize(25f);
					n2[timecount].getNodeData().setX(900);
					YAT2 = YAT2 + 500;
					n2[timecount].getNodeData().setY(YAT2);
					n2[timecount].getNodeData().getAttributes()
							.setValue(checknumber.getIndex(), "2");
					graph.addNode(n2[timecount]);
					Edge e = graphModel.factory().newEdge(
							KeyGenerateUtil.generateIdUUID(),
							n2[timecount - 2], n2[timecount], 1f, true);

					graph.addEdge(e);
					timecount++;
				}
				// 昼型のユビキタスラーニングログ
				if (timer1[t1].getHours() >= 11 && timer1[t1].getHours() <= 18) {
					n2[timecount] = graphModel.factory().newNode(
							String.valueOf(timecount));
					n2[timecount].getNodeData().setLabel("Day");
					n2[timecount].getNodeData().setColor(0.7f, 0.5f, 0.3f);
					n2[timecount].getNodeData().setSize(25f);
					n2[timecount].getNodeData().setX(900);
					YAT2 = YAT2 + 600;
					n2[timecount].getNodeData().setY(YAT2);
					n2[timecount].getNodeData().getAttributes()
							.setValue(checknumber.getIndex(), "2");
					graph.addNode(n2[timecount]);
					Edge e = graphModel.factory().newEdge(
							KeyGenerateUtil.generateIdUUID(),
							n2[timecount - 2], n2[timecount], 1f, true);

					graph.addEdge(e);
					timecount++;
				}
				// 夜型のユビキタスラーニングログ
				if ((timer1[t1].getHours() >= 19 && timer1[t1].getHours() <= 24)
						|| (timer1[t1].getHours() >= 0 && timer1[t1].getHours() <= 3)) {

					n2[timecount] = graphModel.factory().newNode(
							String.valueOf(timecount));
					n2[timecount].getNodeData().setLabel("Night");
					n2[timecount].getNodeData().setColor(0.7f, 0.5f, 0.3f);
					n2[timecount].getNodeData().setSize(25f);
					n2[timecount].getNodeData().setX(900);
					YAT2 = YAT2 + 700;
					n2[timecount].getNodeData().setY(YAT2);
					n2[timecount].getNodeData().getAttributes()
							.setValue(checknumber.getIndex(), "2");
					graph.addNode(n2[timecount]);
					Edge e = graphModel.factory().newEdge(
							KeyGenerateUtil.generateIdUUID(),
							n2[timecount - 2], n2[timecount], 1f, true);

					graph.addEdge(e);
					timecount++;
				}

			}
		}

		// for (Node no : graph.getNodes().toArray()) {
		// if(no.getNodeData().getAttributes().getValue(checklearner1.getIndex()).equals("1")){
		// Edge e10 = graphModel.factory().newEdge(
		// KeyGenerateUtil.generateIdUUID(), no, n2[timecount], 1f,
		// true);
		//
		// graph.addEdge(e10);
		// }
		// targetword = String.valueOf(no.getNodeData().getAttributes()
		// .getValue(node_tagerItem.getIndex()));
		// // targettime=String.valueOf(no.getNodeData().getAttributes()
		// // .getValue(createtime.getIndex()));
		// // System.out.println(targettime);
		//
		// }
		// Edge e10 = graphModel.factory().newEdge(
		// KeyGenerateUtil.generateIdUUID(), n[nodecount], n[timecount], 1f,
		// true);
		//
		// graph.addEdge(e10);
		//

		// ************PlaceCount 定数値
		// 1000が初期**********************************************************************************************
		// ターゲットの場所
		int YP = 500;
		// ターゲットの属性
		int YPA = 500;
		int YSPA = 500;
		// 次の場所
		int NYP = 500;
		int Placecount = 1000;
		int placecount = 1000;
		// 一番目の場所属性
		int place_attribute_count = 1500;
		// 二番目の場所
		int placesecondcount = 2000;
		// 二番目の場所属性
		int placesecond_attribute_count = 2500;
		// Place 共起ネットワーク　第一場所の作成
		for (int i3 = 0; i3 < PlaceCurrence.size(); i3++) {
			if (PlaceCurrence.get(i3).getTarget_content()
					.equalsIgnoreCase(targetword)) {

				n3[placecount] = graphModel.factory().newNode(
						String.valueOf(placecount));
				n3[placecount].getNodeData().setLabel(
						PlaceCurrence.get(i3).getTarget_place());
				// 白
				n3[placecount].getNodeData().setColor(1f, 1f, 1f);
				n3[placecount].getNodeData().setSize(30f);
				n3[placecount].getNodeData().setX(300);
				YP = YP - 100;
				n3[placecount].getNodeData().setY(YP);
				n3[placecount].getNodeData().getAttributes()
						.setValue(checkplace1.getIndex(), "1");
				n3[placecount].getNodeData().getAttributes()
						.setValue(checknumber.getIndex(), "place");
				graph.addNode(n3[placecount]);
				Edge e1 = graphModel.factory().newEdge(
						KeyGenerateUtil.generateIdUUID(), n[nodecount],
						n3[placecount], 1f, true);
				graph.addEdge(e1);

				List<PlaceAnalysis> place_Attribute = new ArrayList<PlaceAnalysis>();
				place_Attribute = itemservice.getPlace_attribute(PlaceCurrence
						.get(i3).getTarget_place_id());
				for (int attribute = 0; attribute < place_Attribute.size(); attribute++) {
					n3[place_attribute_count] = graphModel.factory().newNode(
							String.valueOf(place_attribute_count));
					n3[place_attribute_count].getNodeData().setLabel(
							place_Attribute.get(attribute).getAttribute());
					// 白
					n3[place_attribute_count].getNodeData().setColor(1f, 1f,
							0.8f);
					n3[place_attribute_count].getNodeData().setSize(20f);
					n3[place_attribute_count].getNodeData().setX(600);
					YPA = YPA - 100;
					n3[place_attribute_count].getNodeData().setY(YPA);
					graph.addNode(n3[place_attribute_count]);

					Edge e2 = graphModel.factory()
							.newEdge(KeyGenerateUtil.generateIdUUID(),
									n3[place_attribute_count], n3[placecount],
									1f, true);
					graph.addEdge(e2);
					place_attribute_count++;
				}

				int j = 1;
				for (int i4 = 0; i4 < Place_distinct_Currence.size(); i4++) {
					if (Place_distinct_Currence.get(i4).getTarget_place()
							.equals(PlaceCurrence.get(i3).getTarget_place())) {

						n3[placesecondcount] = graphModel.factory().newNode(
								String.valueOf(placesecondcount));
						n3[placesecondcount].getNodeData()
								.setLabel(
										Place_distinct_Currence.get(i4)
												.getNext_place());
						// 白
						n3[placesecondcount].getNodeData().setColor(1f, 1f, 1f);
						n3[placesecondcount].getNodeData().setSize(30f);
						n3[placesecondcount].getNodeData().setX(900);
						NYP = NYP - 100;
						n3[placesecondcount].getNodeData().setY(NYP);
						graph.addNode(n3[placesecondcount]);
						Edge e3 = graphModel.factory().newEdge(
								KeyGenerateUtil.generateIdUUID(),
								n3[placecount], n3[placesecondcount], 1f, true);
						graph.addEdge(e3);

						List<PlaceAnalysis> placesecond_Attribute = new ArrayList<PlaceAnalysis>();
						placesecond_Attribute = itemservice
								.getPlace_attribute(PlaceCurrence.get(i4)
										.getTarget_place_id());
						for (int attribute = 0; attribute < placesecond_Attribute
								.size(); attribute++) {
							n3[placesecond_attribute_count] = graphModel
									.factory()
									.newNode(
											String.valueOf(placesecond_attribute_count));
							n3[placesecond_attribute_count].getNodeData()
									.setLabel(
											placesecond_Attribute
													.get(attribute)
													.getAttribute());
							// 白
							n3[placesecond_attribute_count].getNodeData()
									.setColor(1f, 1f, 0.8f);
							n3[placesecond_attribute_count].getNodeData()
									.setSize(20f);
							n3[placesecond_attribute_count].getNodeData().setX(
									1200);
							YSPA = YSPA - 100;
							n3[placesecond_attribute_count].getNodeData().setY(
									YSPA);
							graph.addNode(n3[placesecond_attribute_count]);

							Edge e2 = graphModel.factory().newEdge(
									KeyGenerateUtil.generateIdUUID(),
									n3[placesecondcount],
									n3[placesecond_attribute_count], 1f, true);
							graph.addEdge(e2);
							placesecond_attribute_count++;
						}

						placesecondcount++;
					}
				}

				placecount++;
			}
		}
		// 第一場所の配列最終Index
		int first_place_index = placecount;

		// learners1 to knowledges1 edge
		for (int i = 0; i < nodecount; i++) {
			Edge e1 = graphModel.factory().newEdge(
					KeyGenerateUtil.generateIdUUID(), n[i], n[nodecount], 1f,
					true);
			// System.out.println("Second" + nodecount);
			graph.addEdge(e1);
		}

		int knowledge1 = 0;
		// knowledge1 最終ID
		knowledge1 = nodecount;
		// System.out.println("Third:" + knowledge1);
		nodecount++;

		Yrange = 500;
		Ycount = 0;
		Y = 500;
		Y2 = 500;
		// knowledges2 generate
		for (int i = 0; i < Currence.size(); i++) {
			// form.getTitleMap().get("en")
			if (Currence.get(i).getTarget_content().equalsIgnoreCase(content)) {
				n[nodecount] = graphModel.factory().newNode(
						String.valueOf(nodecount));
				n[nodecount].getNodeData().setLabel(
						Currence.get(i).getNext_content());
				n[nodecount].getNodeData().setColor(1.0f, 1.0f, 0.0f);
				n[nodecount].getNodeData().setSize(30f);
				n[nodecount].getNodeData().setX(-300);
				Ycount++;
				if ((Ycount % 2) == 0) {
					Y = Y - 100;
					n[nodecount].getNodeData().setY(Y);

				} else {
					Y = Y - 100;
					n[nodecount].getNodeData().setY(Y);
				}

				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(Hyper.getIndex(),
								Currence.get(i).getTarget_authorid());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(node_tagerItem.getIndex(),
								Currence.get(i).getTarget_content());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(node_tagerItemid.getIndex(),
								Currence.get(i).getTarget_itemid());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(node_NextItem.getIndex(),
								Currence.get(i).getNext_content());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(node_NextItemid.getIndex(),
								Currence.get(i).getNext_itemid());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(knowledgerank.getIndex(),
								Currence.get(i).getKnowledgeranking());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(knowledgenumber.getIndex(),
								Currence.get(i).getKnowledgnumber());

				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(nickname.getIndex(),
								Currence.get(i).getNickname());
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(userlevel.getIndex(),
								Currence.get(i).getUser_level());
				String datedata = sdf.format(Currence.get(i).getItemcreate());
				n[nodecount].getNodeData().getAttributes()
						.setValue(createtime.getIndex(), datedata);
				n[nodecount].getNodeData().getAttributes()
						.setValue(checknumber.getIndex(), "knowledge");
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(checkknowledge2.getIndex(), "checkknowledge2");
				n[nodecount].getNodeData().getAttributes()
						.setValue(place.getIndex(), Currence.get(i).getPlace());

				graph.addNode(n[nodecount]);
				nodecount++;
				// System.out.println("Fourth:" + nodecount);

			}
		}
		// knowledge1 to knowledge2
		// knowledge2 最終ID
		int knowledge1stop = nodecount - 1;
		for (int i = nodecount - 1; i > knowledge1; i--) {
			// System.out.println("Final:" + i + "more" + nodecount + "know"
			// + knowledge1);
			Edge e1 = graphModel.factory().newEdge(
					KeyGenerateUtil.generateIdUUID(), n[knowledge1], n[i], 1f,
					true);

			graph.addEdge(e1);
		}

		Y = 500;
		int c = 0;
		for (int i = 0; i < Currence.size(); i++) {
			// form.getTitleMap().get("en")
			if (Currence.get(i).getTarget_content().equalsIgnoreCase(content)) {
				n2[c] = graphModel.factory().newNode(String.valueOf(nodecount));
				n2[c].getNodeData().setLabel(Currence.get(i).getNext_content());
				n2[c].getNodeData().setColor(0.3f, 0.2f, 0.5f);
				n2[c].getNodeData().setSize(20f);
				n2[c].getNodeData().setX(-600);
				Y = Y + 100;
				n2[c].getNodeData().setY(Y);
				n2[c].getNodeData()
						.getAttributes()
						.setValue(Hyper.getIndex(),
								Currence.get(i).getTarget_authorid());
				n2[c].getNodeData()
						.getAttributes()
						.setValue(node_tagerItem.getIndex(),
								Currence.get(i).getTarget_content());
				n2[c].getNodeData()
						.getAttributes()
						.setValue(node_tagerItemid.getIndex(),
								Currence.get(i).getTarget_itemid());
				n2[c].getNodeData()
						.getAttributes()
						.setValue(node_NextItem.getIndex(),
								Currence.get(i).getNext_content());
				n2[c].getNodeData()
						.getAttributes()
						.setValue(node_NextItemid.getIndex(),
								Currence.get(i).getNext_itemid());
				n2[c].getNodeData()
						.getAttributes()
						.setValue(knowledgerank.getIndex(),
								Currence.get(i).getKnowledgeranking());
				n2[c].getNodeData()
						.getAttributes()
						.setValue(knowledgenumber.getIndex(),
								Currence.get(i).getKnowledgnumber());

				n2[c].getNodeData()
						.getAttributes()
						.setValue(nickname.getIndex(),
								Currence.get(i).getNickname());
				n2[c].getNodeData()
						.getAttributes()
						.setValue(userlevel.getIndex(),
								Currence.get(i).getUser_level());

				c++;
			}
		}

		Yrange = 0;
		Ycount = 0;
		Y = 500;
		Y2 = 500;
		// knowledge3 generate
		int knowledge2 = nodecount;
		for (int i = 0; i < n2.length; i++) {
			if (n2[i] == null) {
				break;
			} else {
				for (int j = 0; j < Currence.size(); j++) {
					if (n2[i].getNodeData().getAttributes()
							.getValue("Nextitem")
							.equals(Currence.get(j).getTarget_content())) {
						n[nodecount] = graphModel.factory().newNode(
								String.valueOf(nodecount));
						n[nodecount].getNodeData().setLabel(
								Currence.get(j).getNext_content());
						n[nodecount].getNodeData().setColor(1.0f, 1.0f, 0.0f);
						n[nodecount].getNodeData().setSize(30f);
						n[nodecount].getNodeData().setX(-600);
						Ycount++;
						if ((Ycount % 2) == 0) {
							Y = Y - 100;
							n[nodecount].getNodeData().setY(Y);

						} else {
							Y = Y - 100;
							n[nodecount].getNodeData().setY(Y);
						}
						n[nodecount]
								.getNodeData()
								.getAttributes()
								.setValue(Hyper.getIndex(),
										Currence.get(j).getTarget_authorid());
						n[nodecount]
								.getNodeData()
								.getAttributes()
								.setValue(node_tagerItem.getIndex(),
										Currence.get(j).getTarget_content());
						n[nodecount]
								.getNodeData()
								.getAttributes()
								.setValue(node_tagerItemid.getIndex(),
										Currence.get(j).getTarget_itemid());
						n[nodecount]
								.getNodeData()
								.getAttributes()
								.setValue(node_NextItem.getIndex(),
										Currence.get(j).getNext_content());
						n[nodecount]
								.getNodeData()
								.getAttributes()
								.setValue(node_NextItemid.getIndex(),
										Currence.get(j).getNext_itemid());
						n[nodecount]
								.getNodeData()
								.getAttributes()
								.setValue(knowledgerank.getIndex(),
										Currence.get(j).getKnowledgeranking());
						n[nodecount]
								.getNodeData()
								.getAttributes()
								.setValue(knowledgenumber.getIndex(),
										Currence.get(j).getKnowledgnumber());

						n[nodecount]
								.getNodeData()
								.getAttributes()
								.setValue(nickname.getIndex(),
										Currence.get(j).getNickname());
						n[nodecount]
								.getNodeData()
								.getAttributes()
								.setValue(userlevel.getIndex(),
										Currence.get(j).getUser_level());
						String datedata = sdf.format(Currence.get(j)
								.getItemcreate());
						n[nodecount].getNodeData().getAttributes()
								.setValue(createtime.getIndex(), datedata);
						n[nodecount].getNodeData().getAttributes()
								.setValue(checknumber.getIndex(), "knowledge");

						graph.addNode(n[nodecount]);
						nodecount++;
						// Yrange = Yrange + 100;
						// Yrange = Yrange / 2;

					}
				}

			}
		}
		// System.out.println("knowledge2:" + knowledge2 + "nodecount" +
		// nodecount
		// + "knowledge1" + knowledge1);
		// knowledge 3 最終ID
		int knowledge2stop = nodecount - 1;
		// knowledge2 to knowledge3
		for (int i = knowledge2; i <= nodecount - 1; i++) {
			for (int j = knowledge1; j < knowledge1stop; j++) {
				// System.out.println("nextitem:"
				// + n[j].getNodeData().getAttributes()
				// .getValue(node_NextItem.getIndex())
				// + "  targetitem:"
				// + n[i].getNodeData().getAttributes()
				// .getValue("Targetitem"));
				// System.out.println("nextitem:" +
				// n[j].getNodeData().getLabel());

				if (n[j].getNodeData()
						.getLabel()
						.equals(n[i].getNodeData().getAttributes()
								.getValue("Targetitem"))) {
					Edge e1 = graphModel.factory().newEdge(
							KeyGenerateUtil.generateIdUUID(), n[j], n[i], 1f,
							true);

					graph.addEdge(e1);
				}

			}

		}

		int Y3 = 500;
		// learner2 to knowledge2
		for (Node no : graph.getNodes().toArray()) {

			// System.out.println("NodeID:"+Integer.parseInt(no.getNodeData().getId())+" Knowledge1:"+knowledge1+"knowledgestop1:"+knowledge1stop);
			if (Integer.parseInt(no.getNodeData().getId()) > knowledge1
					&& Integer.parseInt(no.getNodeData().getId()) <= knowledge1stop) {
				n[nodecount] = graphModel.factory().newNode(
						String.valueOf(nodecount));
				n[nodecount].getNodeData().setLabel(
						String.valueOf(no.getNodeData().getAttributes()
								.getValue("nickname")));
				n[nodecount].getNodeData().setColor(0.0f, 0.0f, 1.0f);

				if (no.getNodeData().getAttributes()
						.getValue("KnowledgeRanking") == null
						|| no.getNodeData().getAttributes()
								.getValue("KnowledgeRanking").equals("null")) {
					n[nodecount].getNodeData().setSize(17f);
					// System.out.println("Size:17");
				} else if (Integer.parseInt(String.valueOf(no.getNodeData()
						.getAttributes().getValue("KnowledgeRanking"))) <= 5) {
					n[nodecount].getNodeData().setSize(25f);
					n[nodecount].getNodeData().setColor(0.0f, 1.0f, 0.0f);
					// System.out.println("Size:25");
				} else {
					n[nodecount].getNodeData().setSize(17f);
				}
				n[nodecount].getNodeData().setX(-600);
				Y3 = Y3 + 100;
				n[nodecount].getNodeData().setY(Y3);

				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								Hyper.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("Authorid")));
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								node_tagerItem.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("Targetitem")));
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								node_tagerItemid.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("Targetitemid")));
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								node_NextItem.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("Nextitem")));
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								node_NextItemid.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("Nextitemid")));
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								knowledgerank.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("KnowledgeRanking")));
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								knowledgenumber.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("Knowledgenumber")));

				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								nickname.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("nickname")));
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								userlevel.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("userlevel")));

				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								createtime.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("createtime")));
				n[nodecount].getNodeData().getAttributes()
						.setValue(checknumber.getIndex(), "1");

				graph.addNode(n[nodecount]);

				Edge e1 = graphModel.factory()
						.newEdge(KeyGenerateUtil.generateIdUUID(),
								n[nodecount],
								n[Integer.parseInt(no.getNodeData().getId())],
								1f, true);

				graph.addEdge(e1);
				nodecount++;
			}
		}
		Y3 = 500;
		Set<String> set = new HashSet<String>();
		// learner3 to knowledge3
		for (Node no : graph.getNodes().toArray()) {
			if (Integer.parseInt(no.getNodeData().getId()) > knowledge2
					&& Integer.parseInt(no.getNodeData().getId()) <= knowledge2stop) {

				set.add(String.valueOf(no.getNodeData().getAttributes()
						.getValue("nickname")));
				n[nodecount] = graphModel.factory().newNode(
						String.valueOf(nodecount));
				n[nodecount].getNodeData().setLabel(
						String.valueOf(no.getNodeData().getAttributes()
								.getValue("nickname")));
				n[nodecount].getNodeData().setColor(0.0f, 0.0f, 1.0f);

				if (no.getNodeData().getAttributes()
						.getValue("KnowledgeRanking") == null
						|| no.getNodeData().getAttributes()
								.getValue("KnowledgeRanking").equals("null")) {
					n[nodecount].getNodeData().setSize(17f);
					// System.out.println("Size:17");
				} else if (Integer.parseInt(String.valueOf(no.getNodeData()
						.getAttributes().getValue("KnowledgeRanking"))) <= 5) {
					n[nodecount].getNodeData().setSize(25f);
					n[nodecount].getNodeData().setColor(0.0f, 1.0f, 0.0f);
					// System.out.println("Size:25");
				} else {
					n[nodecount].getNodeData().setSize(17f);
				}
				n[nodecount].getNodeData().setX(-900);
				Y3 = Y3 + 100;
				n[nodecount].getNodeData().setY(Y3);

				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								Hyper.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("Authorid")));
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								node_tagerItem.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("Targetitem")));
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								node_tagerItemid.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("Targetitemid")));
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								node_NextItem.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("Nextitem")));
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								node_NextItemid.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("Nextitemid")));
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								knowledgerank.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("KnowledgeRanking")));
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								knowledgenumber.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("Knowledgenumber")));

				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								nickname.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("nickname")));
				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								userlevel.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("userlevel")));

				n[nodecount]
						.getNodeData()
						.getAttributes()
						.setValue(
								createtime.getIndex(),
								String.valueOf(no.getNodeData().getAttributes()
										.getValue("createtime")));
				n[nodecount].getNodeData().getAttributes()
						.setValue(checknumber.getIndex(), "1");
				graph.addNode(n[nodecount]);

				Edge e1 = graphModel.factory()
						.newEdge(KeyGenerateUtil.generateIdUUID(),
								n[nodecount],
								n[Integer.parseInt(no.getNodeData().getId())],
								1f, true);

				graph.addEdge(e1);
				nodecount++;

			}
		}

		GraphDistance distance = new GraphDistance();
		distance.setDirected(true);
		distance.execute(graphModel, attributeModel);

		AttributeColumn between = attributeModel.getNodeTable().getColumn(
				GraphDistance.BETWEENNESS);
		AttributeColumn closeness = attributeModel.getNodeTable().getColumn(
				GraphDistance.CLOSENESS);
		AttributeColumn degCol = attributeModel.getNodeTable().getColumn(
				"degree");
		AttributeColumn degColin = attributeModel.getNodeTable().getColumn(
				"degreein");
		AttributeColumn degColout = attributeModel.getNodeTable().getColumn(
				"degreeout");
		degCol = attributeModel.getNodeTable().addColumn("degree", "Degree",
				AttributeType.STRING, AttributeOrigin.COMPUTED, "0");
		degColin = attributeModel.getNodeTable()
				.addColumn("degreein", "Degreein", AttributeType.STRING,
						AttributeOrigin.COMPUTED, "0");
		degColout = attributeModel.getNodeTable().addColumn("degreeout",
				"Degreeout", AttributeType.STRING, AttributeOrigin.COMPUTED,
				"0");

		graph = gc.getModel().getDirectedGraph();
		for (Node no : graph.getNodes().toArray()) {
			Double centrality = (Double) no.getNodeData().getAttributes()
					.getValue(between.getIndex());
			// System.out.println(centrality);
			no.getNodeData().getAttributes()
					.setValue(degCol.getIndex(), graph.getDegree(no));
			no.getNodeData()
					.getAttributes()
					.setValue(
							degColin.getIndex(),
							graph.getGraphModel().getDirectedGraph()
									.getInDegree(no));
			no.getNodeData()
					.getAttributes()
					.setValue(
							degColout.getIndex(),
							graph.getGraphModel().getDirectedGraph()
									.getOutDegree(no));
			// no.getNodeData().getAttributes().setValue(de.getIndex(),Degree.DEGREE);

		}

		// Word Recommendation
		// ListRecommend = new ArrayList<Cooccurrence>();
		// ListRecommend2 = new ArrayList<String>();
		// double degreechecker = 0;
		// double index = 0;
		// for (int i = knowledge1 + 1; i <= knowledge1stop; i++) {
		//
		// if (String.valueOf(
		// n[i].getNodeData().getAttributes()
		// .getValue(checknumber.getIndex())).equals(
		// "knowledge")) {
		// Cooccurrence Recomendation = new Cooccurrence();
		// Recomendation.setLabel(String.valueOf(n[i].getNodeData()
		// .getAttributes().getValue(node_NextItem.getIndex())));
		// Recomendation.setTarget_itemid(String.valueOf(n[i]
		// .getNodeData().getAttributes()
		// .getValue(node_NextItemid.getIndex())));
		// Recomendation.setDegreein(String.valueOf(n[i].getNodeData()
		// .getAttributes().getValue(degColin.getIndex())));
		// Recomendation.setDegreeout(String.valueOf(n[i].getNodeData()
		// .getAttributes().getValue(degColout.getIndex())));
		// //
		// System.out.println(String.valueOf(n[i].getNodeData().getAttributes().getValue(degColout.getIndex())));
		//
		// ListRecommend.add(Recomendation);
		// }
		//
		// }
		// // Place Recommendations
		// for (Node no : graph.getNodes().toArray()) {
		// if (!StringUtils.isBlank(String.valueOf(no.getNodeData()
		// .getAttributes().getValue(checknumber.getIndex())))) {
		// if (String.valueOf(
		// no.getNodeData().getAttributes()
		// .getValue(checknumber.getIndex())).equals(
		// "place")) {
		// Cooccurrence Recomendation = new Cooccurrence();
		// Recomendation.setLabel(no.getNodeData().getLabel());
		// //
		// Recomendation.setTarget_itemid(String.valueOf(n[i].getNodeData().getAttributes().getValue(node_NextItemid.getIndex())));
		// Recomendation.setDegreein(String.valueOf(no.getNodeData()
		// .getAttributes().getValue(degColin.getIndex())));
		// Recomendation.setDegreeout(String.valueOf(no.getNodeData()
		// .getAttributes().getValue(degColout.getIndex())));
		//
		// PlaceRecommend.add(Recomendation);
		//
		// }
		// }
		// }
		//
		// for (Node no : graph.getNodes().toArray()) {
		// if (no.getNodeData().getAttributes().getValue("KnowledgeRanking") ==
		// null
		// || no.getNodeData().getAttributes()
		// .getValue("KnowledgeRanking").equals("null")) {
		// } else if (Integer.parseInt(String.valueOf(no.getNodeData()
		// .getAttributes().getValue("KnowledgeRanking"))) <= 5) {
		//
		// if (ListRecommend2.contains(String.valueOf(no.getNodeData()
		// .getAttributes().getValue("nickname")))) {
		//
		// } else {
		//
		// ListRecommend2.add(String.valueOf(no.getNodeData()
		// .getAttributes().getValue("nickname")));
		// }
		//
		// } else {
		//
		// }
		// }
		//
		// Collections.sort(ListRecommend, new Comparator<Cooccurrence>() {
		// public int compare(Cooccurrence t1, Cooccurrence t2) {
		// return +((int) Integer.valueOf(t2.getDegreeout()) - Integer
		// .valueOf(t1.getDegreeout()));
		//
		// }
		// });
		//
		// Collections.sort(PlaceRecommend, new Comparator<Cooccurrence>() {
		// public int compare(Cooccurrence t1, Cooccurrence t2) {
		// return +((int) Integer.valueOf(t2.getDegreeout()) - Integer
		// .valueOf(t1.getDegreeout()));
		//
		// }
		// });
		// for (int i11 = 0; i11 < ListRecommend.size(); i11++) {
		// Cooccurrence Recomendation = new Cooccurrence();
		// Recomendation.setRanking(i11);
		// }
		//
		// for (int i11 = 0; i11 < PlaceRecommend.size(); i11++) {
		// // System.out.println("PlaceSize:"+PlaceRecommend.size());
		// // PlaceRecommend.get(i11).getLabel()
		// placecollocation = itemservice.p_collocation(PlaceRecommend
		// .get(i11).getLabel());
		// Place_C.add(placecollocation);
		// }

		// // Filtering Algrithm
		// String[] CheckP = new String[5];
		// int checkp = 0;
		// for (Node no : graph.getNodes().toArray()) {
		// if (no.getNodeData().getAttributes().getValue("checkplace1") != null)
		// {
		// if (no.getNodeData().getAttributes().getValue("checkplace1")
		// .equals("1")) {
		// CheckP[checkp] = String.valueOf(no.getNodeData()
		// .getAttributes().getValue("place"));
		// checkp++;
		//
		// }
		// }
		// }
		//
		// for (Node no : graph.getNodes().toArray()) {
		// if (no.getNodeData().getAttributes().getValue("checkknowledge2") !=
		// null) {
		//
		// if (no.getNodeData().getAttributes()
		// .getValue("checkknowledge2").equals("checkknowledge2")) {
		// if (no.getNodeData().getAttributes().getValue("place") != null) {
		// //
		// System.out.println(String.valueOf(no.getNodeData().getAttributes().getValue("place")));
		//
		// }
		// }
		// }
		// }

		// export file//
		ExportController ec = Lookup.getDefault()
				.lookup(ExportController.class);
		try {

			ec.exportFile(new File("quiz.gexf"));
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

		GraphExporter exporter = (GraphExporter) ec.getExporter("gexf");
		exporter.setExportVisible(true);
		try {
			File path = new File(Thread.currentThread().getContextClassLoader()
					.getResource("").toString());
			String abPath = path.getParentFile().getPath();
			abPath = abPath.substring(4, abPath.length());
			ec.exportFile(new File(directory1, "quiz.gexf"), exporter);

		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

	}
	


}
