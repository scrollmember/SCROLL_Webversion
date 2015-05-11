package jp.ac.tokushima_u.is.ll.controller;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.swing.Icon;
import javax.swing.JPanel;
import java.util.Arrays;
import jp.ac.tokushima_u.is.ll.entity.Cooccurrence;
import jp.ac.tokushima_u.is.ll.entity.Author;
//import jp.ac.tokushima_u.is.ll.dto.ItemDTO;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.ItemTags;
import jp.ac.tokushima_u.is.ll.entity.ItemTitle;
import jp.ac.tokushima_u.is.ll.entity.ItemTitleBatis;
import jp.ac.tokushima_u.is.ll.entity.Itemlatlng;
import jp.ac.tokushima_u.is.ll.entity.Kasetting;
import jp.ac.tokushima_u.is.ll.entity.KnowledgeRanking;
import jp.ac.tokushima_u.is.ll.entity.NetworkAnalysis;
import jp.ac.tokushima_u.is.ll.entity.PlaceAnalysis;
import jp.ac.tokushima_u.is.ll.entity.PlaceCollocation;
import jp.ac.tokushima_u.is.ll.entity.TDAfirstlayer;
import jp.ac.tokushima_u.is.ll.entity.TDAsecondlayer;
import jp.ac.tokushima_u.is.ll.entity.TDAthirdlayer;
import jp.ac.tokushima_u.is.ll.entity.Usertest;
//import jp.ac.tokushima_u.is.ll.entity.NetworkItem;
import jp.ac.tokushima_u.is.ll.entity.UserMessage;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.form.AnalysisForm;
import jp.ac.tokushima_u.is.ll.form.ItemEditForm;
import jp.ac.tokushima_u.is.ll.form.ItemSearchCondForm;
import jp.ac.tokushima_u.is.ll.security.SecurityUserHolder;
import jp.ac.tokushima_u.is.ll.service.ItemService;
import jp.ac.tokushima_u.is.ll.service.PropertyService;
import jp.ac.tokushima_u.is.ll.service.UserMessageService;
import jp.ac.tokushima_u.is.ll.service.UserService;
import jp.ac.tokushima_u.is.ll.util.KeyGenerateUtil;
import jp.ac.tokushima_u.is.ll.ws.service.model.ItemForm;

import org.apache.axis.utils.ArrayUtil;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.data.attributes.api.AttributeOrigin;
import org.gephi.data.attributes.api.AttributeType;
import org.gephi.filters.api.FilterController;
import org.gephi.filters.api.Query;
import org.gephi.filters.api.Range;
import org.gephi.filters.plugin.graph.DegreeRangeBuilder.DegreeRangeFilter;
import org.gephi.filters.plugin.graph.EgoBuilder.EgoFilter;
import org.gephi.filters.plugin.partition.PartitionBuilder.NodePartitionFilter;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphFactory;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.gephi.graph.api.Node;
import org.gephi.statistics.plugin.*;
import org.gephi.graph.dhns.node.AbstractNode;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.spi.GraphExporter;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.layout.plugin.AutoLayout;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;
import org.gephi.layout.plugin.multilevel.MaximalMatchingCoarsening;
import org.gephi.layout.plugin.multilevel.MultiLevelLayout;
import org.gephi.layout.plugin.multilevel.YifanHuMultiLevel;
import org.gephi.layout.plugin.random.Random;
import org.gephi.layout.plugin.random.RandomLayout;
import org.gephi.layout.spi.Layout;
import org.gephi.layout.spi.LayoutBuilder;
import org.gephi.layout.spi.LayoutUI;
import org.gephi.partition.api.Partition;
import org.gephi.partition.api.PartitionController;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.statistics.plugin.Degree;
import org.gephi.statistics.plugin.GraphDistance;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import edu.cmu.lti.jawjaw.JAWJAW;
import edu.cmu.lti.jawjaw.pobj.POS;

@Controller
@RequestMapping("/analysis")
public class AnalysisController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserMessageService userMessageService;

	@Autowired
	private ItemService itemservice;

	ItemSearchCondForm itemserachform = new ItemSearchCondForm();
	@Autowired
	private PropertyService propertyService;
	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm:ss");

	ArrayList<Cooccurrence> ListRecommend = new ArrayList<Cooccurrence>();

	ArrayList<Cooccurrence> PlaceRecommend = new ArrayList<Cooccurrence>();
	ArrayList<String> ListRecommend2 = new ArrayList<String>();

	ArrayList Place_C = new ArrayList();

	// Capturing moving a region to another region
	ArrayList<Cooccurrence> Capturing_Place = new ArrayList<Cooccurrence>();

//	 File directory1=new File("/home/learninglog/Desktop/NetworkGexf/");
	// 研究室のパソコン
//	File directory1 = new File(
//			"C:/Users/mouri/work2/learninglogmain/WebContent/js/networkanalysis/");
	 File directory1 = new File("/home/learninglog/NetworkGexf/Gexf");

	// File exitfile = new File(
	// "C:/Users/mouri/work2/learninglogmain/WebContent/js/networkanalysis/graph.gexf");
	File exitfile = new File("/home/learninglog/NetworkGexf/Gexf/graph.gexf");
	private static final String DATE_PATTERN = "yyyy/MM/dd HH:mm";
	String[] learningarray = new String[12];
	File csv = new File(directory1, "scroll.csv");

	// Global folder

	// File directory1= new
	// File("/home/learninglog/Applications/apache-tomcat-7.0.30/webapps/learninglog/js/networkanalysis/");

	@RequestMapping
	public String index(ModelMap model,
			@ModelAttribute("analysisCond") AnalysisForm form, String email,
			String password) {

		return "analysis/show";
	}

	private void Logincheck(String email, String password, boolean rememberMe,
			ModelMap model) {
		// TODO Auto-generated method stub
		AuthenticationToken token = new UsernamePasswordToken(email, password,
				true);
		try {
			SecurityUtils.getSubject().login(token);
		} catch (AuthenticationException e) {
			model.addAttribute("error",
					"The E-mail or the password is incorrect.");
			model.addAttribute("email", email);

		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public String maingetpage(
			@ModelAttribute("analysisCond") AnalysisForm form, ModelMap model,
			HttpServletRequest request) {
		model.clear();
		WordNet("test", POS.n);
		Statistics();
		// testLens();
		// KALens();
		Usertest userid = userService.findById(SecurityUserHolder
				.getCurrentUser().getId());
		model.addAttribute("userid", userid.getId());
		model.addAttribute("analysisCond", form);
		model.addAttribute("learningnumbers", learningarray);
		model.addAttribute("CapturingPlace", Capturing_Place);
		model.addAttribute("Word_Recommend", ListRecommend);
		model.addAttribute("Place_Recommend", PlaceRecommend);

		model.addAttribute("Place_Collocation", Place_C);
		TimeOutput();
		model.addAttribute("test", "This is a test");
		filtercounter(model);
		// TopdownVisualization();
		return "analysis/show";
	}

	private void filtercounter(ModelMap model) {
		// TODO Auto-generated method stub
		List<TDAsecondlayer> counter = new ArrayList<TDAsecondlayer>();
		counter = itemservice.findernationalitycount();
		
		List<TDAsecondlayer> pos_counter = new ArrayList<TDAsecondlayer>();
		pos_counter = itemservice.finderposcount();
		
		model.addAttribute("nationalitycounter", counter);
		model.addAttribute("poscounter", pos_counter);

	}

	@RequestMapping(method = RequestMethod.POST)
	public String test(@ModelAttribute("analysisCond") AnalysisForm form,
			ModelMap model, HttpServletRequest request) {
		PlaceRecommend = new ArrayList<Cooccurrence>();
		Place_C = new ArrayList();
		model.clear();
		CheckedFile();
		Security_user();
		// gephi_create(form);
		K_P_T_Lens(form, model);
		model.addAttribute("analysisCond", form);
		model.addAttribute("learningnumbers", learningarray);
		model.addAttribute("CapturingPlace", Capturing_Place);
		model.addAttribute("Word_Recommend", ListRecommend);
		model.addAttribute("Place_Recommend", PlaceRecommend);
		model.addAttribute("Place_Collocation", Place_C);
		filtercounter(model);
		// return "analysis/show";
		return "redirect:/analysis";
		// return "redirect:/analysis/show";
	}
	@RequestMapping(value = "/topdown", method = RequestMethod.GET)
	public String topdown2(@ModelAttribute("analysisCond") AnalysisForm form,
			ModelMap model, HttpServletRequest request) {
		PlaceRecommend = new ArrayList<Cooccurrence>();
		Place_C = new ArrayList();
		model.clear();

		WordNet("test", POS.n);
		Statistics();
		// testLens();
		// KALens();
		Usertest userid = userService.findById(SecurityUserHolder
				.getCurrentUser().getId());
		model.addAttribute("userid", userid.getId());
		model.addAttribute("analysisCond", form);
		model.addAttribute("learningnumbers", learningarray);
		model.addAttribute("CapturingPlace", Capturing_Place);
		model.addAttribute("Word_Recommend", ListRecommend);
		model.addAttribute("Place_Recommend", PlaceRecommend);

		model.addAttribute("Place_Collocation", Place_C);
		TimeOutput();
		filtercounter(model);
		// gephi_create(form);

		// return "analysis/show";
		TopdownVisualization(form,model);
		model.addAttribute("test", "This is a test");
		return "analysis/show";
		// return "redirect:/analysis/show";
	}
	

	@RequestMapping(value = "/topdown", method = RequestMethod.POST)
	public String topdown(@ModelAttribute("analysisCond") AnalysisForm form,
			ModelMap model, HttpServletRequest request) {
		PlaceRecommend = new ArrayList<Cooccurrence>();
		Place_C = new ArrayList();
		model.clear();

		WordNet("test", POS.n);
		Statistics();
		// testLens();
		// KALens();
		Usertest userid = userService.findById(SecurityUserHolder
				.getCurrentUser().getId());
		model.addAttribute("userid", userid.getId());
		model.addAttribute("analysisCond", form);
		model.addAttribute("learningnumbers", learningarray);
		model.addAttribute("CapturingPlace", Capturing_Place);
		model.addAttribute("Word_Recommend", ListRecommend);
		model.addAttribute("Place_Recommend", PlaceRecommend);

		model.addAttribute("Place_Collocation", Place_C);
		TimeOutput();
		filtercounter(model);
		// gephi_create(form);

		// return "analysis/show";
		TopdownVisualization(form,model);
		model.addAttribute("test", "This is a test");
		return "analysis/show";
		// return "redirect:/analysis/show";
	}

	private void testLens() {
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

		GraphController gc = Lookup.getDefault().lookup(GraphController.class);
		graphModel = gc.getModel();
		Graph graph = gc.getModel().getDirectedGraph();

		// Graph graph = gc.getModel().getDirectedGraph();
		Node[] n = new Node[50000];
		n[0] = graphModel.factory().newNode(String.valueOf(0));
		n[0].getNodeData().setLabel("natto");
		n[0].getNodeData().setColor(1.0f, 0.0f, 1.0f);
		n[0].getNodeData().setSize(15f);
		n[0].getNodeData().setX(0);
		n[0].getNodeData().setY(200);
		graph.addNode(n[0]);

		n[1] = graphModel.factory().newNode(String.valueOf(1));
		n[1].getNodeData().setLabel("Tofu");
		n[1].getNodeData().setColor(1.0f, 0.0f, 1.0f);
		n[1].getNodeData().setSize(15f);
		n[1].getNodeData().setX(50);
		n[1].getNodeData().setY(50);
		graph.addNode(n[1]);

		n[2] = graphModel.factory().newNode(String.valueOf(2));
		n[2].getNodeData().setLabel("coffee");
		n[2].getNodeData().setColor(1.0f, 0.0f, 1.0f);
		n[2].getNodeData().setSize(15f);
		n[2].getNodeData().setX(50);
		n[2].getNodeData().setY(100);
		graph.addNode(n[2]);

		n[3] = graphModel.factory().newNode(String.valueOf(3));
		n[3].getNodeData().setLabel("test");
		n[3].getNodeData().setColor(1.0f, 0.0f, 1.0f);
		n[3].getNodeData().setSize(15f);
		n[3].getNodeData().setX(50);
		n[3].getNodeData().setY(150);
		graph.addNode(n[3]);

		n[4] = graphModel.factory().newNode(String.valueOf(4));
		n[4].getNodeData().setLabel("computer");
		n[4].getNodeData().setColor(1.0f, 0.0f, 1.0f);
		n[4].getNodeData().setSize(15f);
		n[4].getNodeData().setX(50);
		n[4].getNodeData().setY(500);
		graph.addNode(n[4]);
		// for (int i = 0; i < Currence.size(); i++) {
		// System.out.println(Currence.get(i).getAuthorid() + ":"
		// + userid.getId());
		// if (Currence.get(i).getTarget_authorid().equals(userid.getId())) {
		// n[i] = graphModel.factory().newNode(String.valueOf(i));
		// n[i].getNodeData()
		// .setLabel(Currence.get(i).getTarget_content());
		// n[i].getNodeData().setColor(1.0f, 0.0f, 1.0f);
		// n[i].getNodeData().setSize(15f);
		// n[i].getNodeData()
		// .getAttributes()
		// .setValue(Hyper.getIndex(),
		// Currence.get(i).getTarget_authorid());
		// n[i].getNodeData()
		// .getAttributes()
		// .setValue(node_tagerItem.getIndex(),
		// Currence.get(i).getTarget_content());
		// n[i].getNodeData()
		// .getAttributes()
		// .setValue(node_tagerItemid.getIndex(),
		// Currence.get(i).getTarget_itemid());
		// n[i].getNodeData()
		// .getAttributes()
		// .setValue(node_NextItem.getIndex(),
		// Currence.get(i).getNext_content());
		// n[i].getNodeData()
		// .getAttributes()
		// .setValue(node_NextItemid.getIndex(),
		// Currence.get(i).getNext_itemid());
		// graph.addNode(n[i]);
		// } else {
		//
		// n[i] = graphModel.factory().newNode(String.valueOf(i));
		// n[i].getNodeData()
		// .setLabel(Currence.get(i).getTarget_content());
		// n[i].getNodeData().setColor(1.0f, 1.0f, 0.0f);
		// n[i].getNodeData().setSize(13f);
		// n[i].getNodeData()
		// .getAttributes()
		// .setValue(Hyper.getIndex(),
		// Currence.get(i).getTarget_authorid());
		// n[i].getNodeData()
		// .getAttributes()
		// .setValue(node_tagerItem.getIndex(),
		// Currence.get(i).getTarget_content());
		// n[i].getNodeData()
		// .getAttributes()
		// .setValue(node_tagerItemid.getIndex(),
		// Currence.get(i).getTarget_itemid());
		// n[i].getNodeData()
		// .getAttributes()
		// .setValue(node_NextItem.getIndex(),
		// Currence.get(i).getNext_content());
		// n[i].getNodeData()
		// .getAttributes()
		// .setValue(node_NextItemid.getIndex(),
		// Currence.get(i).getNext_itemid());
		// graph.addNode(n[i]);
		// }

		ExportController ec = Lookup.getDefault()
				.lookup(ExportController.class);
		try {

			ec.exportFile(new File("ka2.gexf"));
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
			ec.exportFile(new File(directory1, "ka2.gexf"), exporter);

		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
	}

	private void KALens(AnalysisForm form) {
		// TODO Auto-generated method stub

		// UserID取得
		Usertest userid = userService.findById(SecurityUserHolder
				.getCurrentUser().getId());

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

		GraphController gc = Lookup.getDefault().lookup(GraphController.class);
		graphModel = gc.getModel();
		Graph graph = gc.getModel().getDirectedGraph();

		// Graph graph = gc.getModel().getDirectedGraph();
		Node[] n = new Node[50000];

		List<Users> user = userService.findBynetworkuser();
		ArrayList<Cooccurrence> Currence = new ArrayList<Cooccurrence>();
		List<Cooccurrence> coocurrence = new ArrayList<Cooccurrence>();
		// CSVファイル摘出　学習者ごとの時間列のデータ
		// List<Author> author = userService.getallauthorid();
		// for (int au=0;au<author.size();au++){
		// List<Cooccurrence> coocurrence3 = new ArrayList<Cooccurrence>();
		// coocurrence3 = itemservice.cooccurrence(author.get(au).getId());
		// try{
		// FileOutputStream fos = new
		// FileOutputStream(csv,true);
		// OutputStreamWriter osw = new OutputStreamWriter(fos,
		// "SJIS");
		// BufferedWriter bw = new BufferedWriter(osw);
		// bw.write(author.get(au).getNickname()+",");
		// bw.close();
		// }catch (FileNotFoundException e) {
		// // Fileオブジェクト生成時の例外捕捉
		// e.printStackTrace();
		// } catch (IOException e) {
		// // BufferedWriterオブジェクトのクローズ時の例外捕捉
		// e.printStackTrace();
		// }
		// for(int b=0;b<coocurrence3.size();b++){
		// try{
		// FileOutputStream fos = new
		// FileOutputStream(csv,true);
		// OutputStreamWriter osw = new OutputStreamWriter(fos,
		// "SJIS");
		// BufferedWriter bw = new BufferedWriter(osw);
		// bw.write(coocurrence3.get(b).getContent()+",");
		// bw.close();
		// }catch (FileNotFoundException e) {
		// // Fileオブジェクト生成時の例外捕捉
		// e.printStackTrace();
		// } catch (IOException e) {
		// // BufferedWriterオブジェクトのクローズ時の例外捕捉
		// e.printStackTrace();
		// }
		// }
		//
		// try{
		// FileOutputStream fos = new
		// FileOutputStream(csv,true);
		// OutputStreamWriter osw = new OutputStreamWriter(fos,
		// "SJIS");
		// BufferedWriter bw = new BufferedWriter(osw);
		// bw.newLine();
		// bw.close();
		// }catch (FileNotFoundException e) {
		// // Fileオブジェクト生成時の例外捕捉
		// e.printStackTrace();
		// } catch (IOException e) {
		// // BufferedWriterオブジェクトのクローズ時の例外捕捉
		// e.printStackTrace();
		// }
		// }
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
						// System.out.println(coocurrence.get(i).getContent() +
						// "->"
						// + coocurrence.get(j).getContent());
						Currence.add(currenceentity);
						// try{
						// FileOutputStream fos = new
						// FileOutputStream(csv,true);
						// OutputStreamWriter osw = new OutputStreamWriter(fos,
						// "SJIS");
						// BufferedWriter bw = new BufferedWriter(osw);
						// bw.write(currenceentity.getTarget_content()+","+currenceentity.getNext_content()
						// +
						// ","+currenceentity.getTarget_itemid()+","+currenceentity.getNext_itemid()+","+currenceentity.getTarget_authorid()+","+currenceentity.getNext_authorid());
						// bw.newLine();
						// bw.close();
						// }catch (FileNotFoundException e) {
						// // Fileオブジェクト生成時の例外捕捉
						// e.printStackTrace();
						// } catch (IOException e) {
						// // BufferedWriterオブジェクトのクローズ時の例外捕捉
						// e.printStackTrace();
						// }
						j++;
					}
				}

			}
		}

		// Sort検索************************************************************************************************************************************//
		// Collections.sort(Currence, new Comparator<Cooccurrence>(){
		// public int compare(Cooccurrence t1, Cooccurrence t2) {
		// return t1.getTarget_content().compareTo(t2.getTarget_content());
		// }
		// });
		// for(Cooccurrence c: Currence){
		// System.out.println("Target:"+c.getTarget_content()+"  Authorid:"+c.getTarget_authorid()+"ItmeID:"+c.getTarget_itemid()+" NextContent:"+c.getNext_content());
		// }
		// Knowledge
		// 共起ネットワーク生成************************************************************************************************************************************//
		for (int i = 0; i < Currence.size(); i++) {
			System.out.println(Currence.get(i).getAuthorid() + ":"
					+ userid.getId());
			if (Currence.get(i).getTarget_authorid().equals(userid.getId())) {
				n[i] = graphModel.factory().newNode(String.valueOf(i));
				n[i].getNodeData()
						.setLabel(Currence.get(i).getTarget_content());
				n[i].getNodeData().setColor(1.0f, 0.0f, 1.0f);
				n[i].getNodeData().setSize(15f);
				n[i].getNodeData()
						.getAttributes()
						.setValue(Hyper.getIndex(),
								Currence.get(i).getTarget_authorid());
				n[i].getNodeData()
						.getAttributes()
						.setValue(node_tagerItem.getIndex(),
								Currence.get(i).getTarget_content());
				n[i].getNodeData()
						.getAttributes()
						.setValue(node_tagerItemid.getIndex(),
								Currence.get(i).getTarget_itemid());
				n[i].getNodeData()
						.getAttributes()
						.setValue(node_NextItem.getIndex(),
								Currence.get(i).getNext_content());
				n[i].getNodeData()
						.getAttributes()
						.setValue(node_NextItemid.getIndex(),
								Currence.get(i).getNext_itemid());
				graph.addNode(n[i]);
			} else {

				n[i] = graphModel.factory().newNode(String.valueOf(i));
				n[i].getNodeData()
						.setLabel(Currence.get(i).getTarget_content());
				n[i].getNodeData().setColor(1.0f, 1.0f, 0.0f);
				n[i].getNodeData().setSize(13f);
				n[i].getNodeData()
						.getAttributes()
						.setValue(Hyper.getIndex(),
								Currence.get(i).getTarget_authorid());
				n[i].getNodeData()
						.getAttributes()
						.setValue(node_tagerItem.getIndex(),
								Currence.get(i).getTarget_content());
				n[i].getNodeData()
						.getAttributes()
						.setValue(node_tagerItemid.getIndex(),
								Currence.get(i).getTarget_itemid());
				n[i].getNodeData()
						.getAttributes()
						.setValue(node_NextItem.getIndex(),
								Currence.get(i).getNext_content());
				n[i].getNodeData()
						.getAttributes()
						.setValue(node_NextItemid.getIndex(),
								Currence.get(i).getNext_itemid());
				graph.addNode(n[i]);
			}
			// getAttributes().setValue(dateColumn.getIndex(), randomDataValue);
			// n[i]=graphModel.factory().newNode("0," +
			// String.valueOf(Currence.get(i).getTarget_itemid()));
			// n[i].getNodeData().setLabel(Currence.get(i).getTarget_content());
			// // if(i==0){
			// // n[i].getNodeData().setColor(0.0f, 0.0f, 0.0f);
			// // n[i].getNodeData().setSize(16f);
			// // }
			//
			//
			// n[i].getNodeData().setColor(1.0f, 0.0f, 1.0f);
			// n[i].getNodeData().setSize(13f);
			// graph.addNode(n[i]);
			// n[40000-i]=graphModel.factory().newNode("0," +
			// String.valueOf(Currence.get(i).getNext_itemid()));
			//
			// n[40000-i].getNodeData().setLabel(Currence.get(i).getNext_content());
			// n[40000-i].getNodeData().setColor(0.0f, 0.0f, 1.0f);
			// n[40000-i].getNodeData().setSize(13f);
			// graph.addNode(n[40000-i]);
			//
			// Edge e1 = graphModel.factory().newEdge(
			// n[i].getNodeData().getId() + "-"
			// + n[40000-i].getNodeData().getLabel(), n[i],
			// n[40000-i], 1f, true);
			// graph.addEdge(e1);
			//
		}
		// edge network generate
		for (Node no : graph.getNodes().toArray()) {

			for (Node no2 : graph.getNodes().toArray()) {
				if (no.getNodeData()
						.getAttributes()
						.getValue(Hyper.getIndex())
						.equals(no2.getNodeData().getAttributes()
								.getValue(Hyper.getIndex()))) {
					if (no.getNodeData()
							.getAttributes()
							.getValue(node_NextItemid.getIndex())
							.equals(no2.getNodeData().getAttributes()
									.getValue(node_tagerItemid.getIndex()))) {
						Edge e1 = graphModel.factory().newEdge(
								KeyGenerateUtil.generateIdUUID(), no, no2, 1f,
								true);
						e1.getEdgeData().setLabel(
								String.valueOf(no2.getNodeData()
										.getAttributes()
										.getValue(node_tagerItem.getIndex())));
						graph.addEdge(e1);
					}
				}
			}
		}

		// Connecting learners' own knowledge to next others knowledge
		for (Node no : graph.getNodes().toArray()) {
			for (Node no2 : graph.getNodes().toArray()) {
				if (no.getNodeData()
						.getAttributes()
						.getValue(node_NextItem.getIndex())
						.equals(no2.getNodeData().getAttributes()
								.getValue(node_tagerItem.getIndex()))) {
					Edge e1 = graphModel.factory()
							.newEdge(KeyGenerateUtil.generateIdUUID(), no, no2,
									1f, true);
					e1.getEdgeData().setColor(1.0f, 1.0f, 0.0f);
					e1.getEdgeData().setLabel(
							String.valueOf(no2.getNodeData().getAttributes()
									.getValue(node_tagerItem.getIndex())));
					graph.addEdge(e1);
				}

			}

		}

		// edge linking
		// for (Node no : graph.getNodes().toArray()) {
		// //
		// System.out.println(no.getNodeData().getAttributes().getValue(node_tagerItem.getIndex()));
		// if(userid.getId().equals(no.getNodeData().getAttributes().getValue(Hyper.getIndex()))){
		// for (Node no2 : graph.getNodes().toArray()) {
		// if(no.getNodeData().getAttributes().getValue(node_NextItemid.getIndex()).equals(no2.getNodeData().getAttributes().getValue(node_tagerItemid.getIndex()))){
		// if(userid.getId().equals(no2.getNodeData().getAttributes().getValue(Hyper.getIndex()))){
		// no.getNodeData().setColor(1.0f, 0.0f, 1.0f);
		// no2.getNodeData().setColor(1.0f, 0.0f, 1.0f);
		// Edge e1 = graphModel.factory().newEdge(
		// no.getNodeData().getLabel()+ "-"+ no2.getNodeData().getLabel(),
		// no,no2, 1f, true);
		// graph.addEdge(e1);
		// }
		//
		// }
		// else
		// if(no.getNodeData().getAttributes().getValue(node_tagerItem.getIndex()).equals(no2.getNodeData().getAttributes().getValue(node_tagerItem.getIndex()))){
		// if(!no2.getNodeData().getAttributes().getValue(Hyper.getIndex()).equals(userid.getId())){
		// Edge e1 = graphModel.factory().newEdge(
		// no.getNodeData().getLabel()+ "-"+ no2.getNodeData().getLabel(),
		// no,no2, 1f, true);
		// graph.addEdge(e1);
		// }
		// }
		//
		// }
		// }}
		// Layout setting

		// AutoLayout autoLayout = new AutoLayout(1, TimeUnit.MINUTES);
		// autoLayout.setGraphModel(graphModel);
		// YifanHuLayout firstLayout = new YifanHuLayout(null,
		// new StepDisplacement(1f));
		// ForceAtlasLayout secondLayout = new ForceAtlasLayout(null);
		//
		// AutoLayout.DynamicProperty adjustBySizeProperty = AutoLayout
		// .createDynamicProperty("Adjust by Sizes", Boolean.TRUE,
		// 0.1f);
		// AutoLayout.DynamicProperty repulsionProperty = AutoLayout
		// .createDynamicProperty("Repulsion strength",
		// new Double(500.), 0f);

		// autoLayout.addLayout(firstLayout, 0.5f);
		// // ,new
		// //
		// AutoLayout.DynamicProperty[]{adjustBySizeProperty,repulsionProperty}
		// autoLayout.addLayout(secondLayout, 0.5f);
		//
		if (form.getEgofiltername() != null
				&& form.getEgofilterdistance() != null
				&& !(form.getEgofilterdistance().equals("Target Degree"))
				&& !(form.getEgofiltername().equals(""))) {
			for (Node no : graph.getNodes().toArray()) {
				if (no.getNodeData().getAttributes()
						.getValue(node_tagerItem.getIndex())
						.equals(form.getEgofiltername())) {
					no.getNodeData().setColor(0.1f, 0.1f, 0.9f);
					no.getNodeData().setSize(80f);
					graph.addNode(no);

				}
			}

			FilterController filterController = Lookup.getDefault().lookup(
					FilterController.class);
			EgoFilter egoFilter = new EgoFilter();
			egoFilter.setPattern(form.getEgofiltername());
			egoFilter.setDepth(Integer.parseInt(form.getEgofilterdistance()));
			Query queryEgo = filterController.createQuery(egoFilter);
			GraphView viewEgo = filterController.filter(queryEgo);
			graph = graphModel.getGraph(viewEgo);
			graphModel.setVisibleView(viewEgo);

			GraphDistance distance = new GraphDistance();
			distance.setDirected(true);
			distance.execute(graphModel, attributeModel);

			AttributeColumn between = attributeModel.getNodeTable().getColumn(
					GraphDistance.BETWEENNESS);
			AttributeColumn closeness = attributeModel.getNodeTable()
					.getColumn(GraphDistance.CLOSENESS);
			AttributeColumn degCol = attributeModel.getNodeTable().getColumn(
					"degree");
			degCol = attributeModel.getNodeTable().addColumn("degree",
					"Degree", AttributeType.STRING, AttributeOrigin.COMPUTED,
					"0");
			for (Node no : graph.getNodes().toArray()) {
				Double centrality = (Double) no.getNodeData().getAttributes()
						.getValue(between.getIndex());
				// System.out.println(centrality);

				no.getNodeData().getAttributes()
						.setValue(degCol.getIndex(), graph.getDegree(no));
			}
			double[] t1 = new double[20];
			double id = 0;
			double id2 = 0;
			for (Node no : graph.getNodes().toArray()) {
				if (id < (Double) no.getNodeData().getAttributes()
						.getValue(between.getIndex())) {
					id = (Double) no.getNodeData().getAttributes()
							.getValue(between.getIndex());
				}
			}
			for (Node no : graph.getNodes().toArray()) {

				if ((Double) no.getNodeData().getAttributes()
						.getValue(between.getIndex()) >= id) {
					no.getNodeData().setColor(0.3f, 0.4f, 0.9f);
					no.getNodeData().setSize(80f);
					graph.addNode(no);
				}
			}
			// 水色　近接中心性
			for (Node no : graph.getNodes().toArray()) {
				if (id2 < (Double) no.getNodeData().getAttributes()
						.getValue(closeness.getIndex())) {
					id2 = (Double) no.getNodeData().getAttributes()
							.getValue(closeness.getIndex());
				}
			}
			for (Node no : graph.getNodes().toArray()) {

				if ((Double) no.getNodeData().getAttributes()
						.getValue(closeness.getIndex()) >= id2) {
					no.getNodeData().setColor(0.2f, 0.9f, 0.9f);
					no.getNodeData().setSize(80f);
					graph.addNode(no);
				}
			}

			// Recommendation
			ArrayList<Cooccurrence> ListRecommend = new ArrayList<Cooccurrence>();

			// for (Node no : graph.getNodes().toArray()) {
			//
			// }
			// if(degreechecker==0){
			//
			// }
			// else{
			// if(degreechecker>index){
			// degreechecker=(Double)no2.getNodeData().getAttributes().getValue(degCol.getIndex());
			// index=degreechecker;
			// }
			// }
			// degreechecker=(Double)no2.getNodeData().getAttributes().getValue(degCol.getIndex());
			double degreechecker = 0;
			double index = 0;
			for (Node no : graph.getNodes().toArray()) {
				if (form.getEgofiltername().equalsIgnoreCase(
						String.valueOf(no.getNodeData().getAttributes()
								.getValue(node_tagerItem.getIndex())))) {
					for (Node no2 : graph.getNodes().toArray()) {
						if (no.getNodeData()
								.getAttributes()
								.getValue(node_NextItem.getIndex())
								.equals(no2.getNodeData().getAttributes()
										.getValue(node_tagerItem.getIndex()))) {
							Cooccurrence Recomendation = new Cooccurrence();
							Recomendation.setLabel(String.valueOf(no2
									.getNodeData().getAttributes()
									.getValue(node_tagerItem.getIndex())));
							Recomendation.setDegree(String.valueOf(no2
									.getNodeData().getAttributes()
									.getValue(degCol.getIndex())));
							Recomendation.setTarget_itemid(String.valueOf(no2
									.getNodeData().getAttributes()
									.getValue(node_tagerItemid.getIndex())));
							// System.out.println(String.valueOf(no2.getNodeData()
							// .getAttributes()
							// .getValue(node_tagerItem.getIndex()))
							// + "///"
							// + String.valueOf(no2.getNodeData()
							// .getAttributes()
							// .getValue(degCol.getIndex())));
							ListRecommend.add(Recomendation);
						}
					}
				}
			}

			Collections.sort(ListRecommend, new Comparator<Cooccurrence>() {
				public int compare(Cooccurrence t1, Cooccurrence t2) {
					return +((int) Integer.valueOf(t2.getDegree()) - Integer
							.valueOf(t1.getDegree()));
					// return
					// t1.getTarget_content().compareTo(t2.getTarget_content());
				}
			});
			for (int i = 0; i < ListRecommend.size(); i++) {
				// System.out.println(ListRecommend.get(i).getLabel() + "/"
				// + ListRecommend.get(i).getDegree());
				// for(Cooccurrence c: ListRecommend){
				// System.out.println(c.getDegree());
			}

		}

		// export file//
		ExportController ec = Lookup.getDefault()
				.lookup(ExportController.class);
		try {

			ec.exportFile(new File("ka.gexf"));
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
			ec.exportFile(new File(directory1, "ka.gexf"), exporter);

		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

	}

	private void Statistics() {
		// TODO Auto-generated method stub
		Usertest userid = userService.findById(SecurityUserHolder
				.getCurrentUser().getId());
		int j = 2;
		int count = 0;
		// List<String> learninglogs = new ArrayList<String>();

		for (int i = 1; i <= 12; i++) {

			Calendar querycalstart = Calendar.getInstance();
			querycalstart.set(Calendar.YEAR, 2013);
			querycalstart.set(Calendar.MONTH, i);
			querycalstart.set(Calendar.HOUR_OF_DAY, 0);
			querycalstart.set(Calendar.MINUTE, 0);
			querycalstart.set(Calendar.SECOND, 0);

			Calendar querycalstart2 = Calendar.getInstance();
			querycalstart2.set(Calendar.YEAR, 2013);
			querycalstart2.set(Calendar.MONTH, j);
			querycalstart2.set(Calendar.HOUR_OF_DAY, 0);
			querycalstart2.set(Calendar.MINUTE, 0);
			querycalstart2.set(Calendar.SECOND, 0);

			Date dstart = querycalstart.getTime();

			Date dend = querycalstart2.getTime();

			learningarray[count] = itemservice.findcount(dstart,
					userid.getId(), dend);
			// System.out.println(learningarray[count]);
			// List<String> learnings = itemservice.findcount();
			j++;
			count++;
		}
	}

	private void PostStatistics(String string) {
		// TODO Auto-generated method stub
		Usertest userid = userService.findById(SecurityUserHolder
				.getCurrentUser().getId());
		int j = 2;
		int count = 0;
		// List<String> learninglogs = new ArrayList<String>();

		for (int i = 1; i <= 12; i++) {

			Calendar querycalstart = Calendar.getInstance();
			querycalstart.set(Calendar.YEAR, 2013);
			querycalstart.set(Calendar.MONTH, i);
			querycalstart.set(Calendar.HOUR_OF_DAY, 0);
			querycalstart.set(Calendar.MINUTE, 0);
			querycalstart.set(Calendar.SECOND, 0);

			Calendar querycalstart2 = Calendar.getInstance();
			querycalstart2.set(Calendar.YEAR, 2013);
			querycalstart2.set(Calendar.MONTH, j);
			querycalstart2.set(Calendar.HOUR_OF_DAY, 0);
			querycalstart2.set(Calendar.MINUTE, 0);
			querycalstart2.set(Calendar.SECOND, 0);

			Date dstart = querycalstart.getTime();

			Date dend = querycalstart2.getTime();

			learningarray[count] = itemservice.findcount(dstart,
					userid.getId(), dend);
			// System.out.println(learningarray[count]);
			// List<String> learnings = itemservice.findcount();
			j++;
			count++;
		}
	}

	@RequestMapping(value = "/stastics", method = RequestMethod.POST)
	public String Statistics(@ModelAttribute("analysisCond") AnalysisForm form,
			ModelMap model, HttpServletRequest request) {

		// model.clear();
		// CheckedFile();
		// Security_user();
		// gephi_create(form);
		PostStatistics(form.getDegree());
		Statistics();
		model.addAttribute("analysisCond", form);
		model.addAttribute("learningnumbers", learningarray);
		return "analysis/statistics";
		// return "redirect:/analysis";
		// return "redirect:/analysis/show";
	}

	@RequestMapping(value = "/kal", method = RequestMethod.GET)
	public String kal(@ModelAttribute("analysisCond") AnalysisForm form,
			ModelMap model, HttpServletRequest request) {

		// model.clear();
		// CheckedFile();
		// Security_user();
		// gephi_create(form);
		// KALens();
		Statistics();
		model.addAttribute("analysisCond", form);
		model.addAttribute("learningnumbers", learningarray);

		return "analysis/kal";
		// return "redirect:/analysis";
		// return "redirect:/analysis/show";
	}

	@RequestMapping(value = "/kal", method = RequestMethod.POST)
	public String kal2(@ModelAttribute("analysisCond") AnalysisForm form,
			ModelMap model, HttpServletRequest request) {

		// model.clear();
		// CheckedFile();
		// Security_user();
		// gephi_create(form);
		Statistics();
		KALens(form);
		model.addAttribute("learningnumbers", learningarray);
		return "analysis/kal";
		// return "redirect:/analysis";
		// return "redirect:/analysis/show";
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public String test2(@ModelAttribute("analysisCond") AnalysisForm form,
			ModelMap model, HttpServletRequest request) {
		model.clear();
		Security_user();
		gephi_create(form);
		// Page<ItemDTO> page = itemService.searchItemPageByCond(form);
		// if(page!=null&&page.getContent()!=null&&page.getContent().size()>0)
		// {
		// List<ItemDTO>results = page.getContent();
		// List<ItemForm>forms = new ArrayList<ItemForm>();
		// for(Item item:results){
		// ItemForm itemform = itemService.convertItemToItemForm(item);
		// forms.add(itemform);
		// }
		// model.addAttribute("items", forms);
		// }
		model.addAttribute("analysisCond", form);
		model.addAttribute("learningnumbers", learningarray);
		return "analysis/show";
	}

	@RequestMapping(value = "/show_t", method = RequestMethod.GET)
	public String test4(@ModelAttribute("analysisCond") AnalysisForm form,
			ModelMap model, HttpServletRequest request) {
		model.clear();
		Security_user();
		gephi_create(form);
		// Page<ItemDTO> page = itemService.searchItemPageByCond(form);
		// if(page!=null&&page.getContent()!=null&&page.getContent().size()>0)
		// {
		// List<ItemDTO>results = page.getContent();
		// List<ItemForm>forms = new ArrayList<ItemForm>();
		// for(Item item:results){
		// ItemForm itemform = itemService.convertItemToItemForm(item);
		// forms.add(itemform);
		// }
		// model.addAttribute("items", forms);
		// }
		model.addAttribute("analysisCond", form);
		return "analysis/show";
	}

	@RequestMapping(value = "/show", params = "format=json")
	@ResponseBody
	public String mylogsJson(HttpServletRequest request,
			@RequestParam("username") String username) {
		// Users user = SecurityUserHolder.getCurrentUser();
		// if(username.isEmpty()){
		// List<Item> itemList =
		// itemservice.searchMyitemsForTimemap(user.getId());
		// }
		// List<Item> itemList = itemservice.searchMyitemsForTimemap(username);
		// List<Map<String, Object>> dataset = new ArrayList<Map<String,
		// Object>>();
		// for (Item item : itemList) {
		// Map<String, Object> data = new HashMap<String, Object>();
		// data.put("title", itemservice.getItemNote(item));
		// data.put("start", format.format(item.getCreateTime()));
		// Map<String, Double> point = new HashMap<String, Double>();
		// point.put("lat", item.getItemLat());
		// point.put("lon", item.getItemLng());
		// data.put("point", point);
		// Map<String, Object> options = new HashMap<String, Object>();
		// String url = request.getContextPath() + "/item/" + item.getId();
		// String discription = "<a target=\"_blank\"href=\"" + url + "\">"
		// + itemservice.getItemNote(item) + "</a>";
		// if (item.getImage() != null) {
		// discription += "<br/><a target=\"_blank\"href=\"" + url
		// + "\"><img height=\"70px\" src=\""
		// + request.getContextPath() + "/static/"
		// + item.getImage() + "_160x120.png\" /></a>";
		// }
		// options.put("description", discription);
		// data.put("options", options);
		// dataset.add(data);
		// }
		// Gson gson = new Gson();
		// return gson.toJson(dataset);

		// Users user = SecurityUserHolder.getCurrentUser();
		List<Item> itemList = itemservice.searchMyitemsForTimemap(username);
		List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
		for (Item item : itemList) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("title", item.getDefaultTitle());
			data.put("start", format.format(item.getCreateTime()));
			Map<String, Double> point = new HashMap<String, Double>();
			point.put("lat", item.getItemLat());
			point.put("lon", item.getItemLng());
			data.put("point", point);
			Map<String, Object> options = new HashMap<String, Object>();
			String url = request.getContextPath() + "/item/" + item.getId();
			String discription = "<a target=\"_blank\"href=\"" + url + "\">"
					+ item.getDefaultTitle() + "</a>";
			if (item.getImage() != null) {
				discription += "<br/><a target=\"_blank\"href=\"" + url
						+ "\"><img height=\"70px\" src=\""
						+ propertyService.getStaticserverUrl() + "/"
						+ propertyService.getProjectName() + "/"
						+ item.getImage().getId() + "_160x120.png\" /></a>";
			}
			options.put("description", discription);
			data.put("options", options);
			dataset.add(data);
		}
		Gson gson = new Gson();
		return gson.toJson(dataset);
	}

	// Method_list**************************************************************************************************************************************************
	private void Security_user() {
		// TODO Auto-generated method stub
		Usertest user = userService.findById(SecurityUserHolder
				.getCurrentUser().getId());

		itemserachform.setUserId(user.getId());
		itemserachform.setNickname(user.getNickname());
		itemserachform.setIncludeRelog(true);

	}

	public void gephi_create(AnalysisForm analysisform) {
		int i = 0;
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
		AttributeModel attributeModel = Lookup.getDefault()
				.lookup(AttributeController.class).getModel();
		// Add boolean column
		AttributeColumn Hyper = attributeModel.getNodeTable().addColumn(
				"Hypernym", AttributeType.STRING);

		GraphController gc = Lookup.getDefault().lookup(GraphController.class);
		graphModel = gc.getModel();
		Graph graph = gc.getModel().getUndirectedGraph();

		// YifanHuLayout layout = new YifanHuLayout(null, new
		// StepDisplacement(1f));
		// layout.setGraphModel(graphModel);
		// layout.resetPropertiesValues();
		// layout.setOptimalDistance(200f);
		// layout.initAlgo();
		// for(int c=0;c<100 && layout.canAlgo();c++){
		// layout.goAlgo();
		// }
		Node[] n = new Node[100000];
		// Edge[] e = new Edge[10000];

		List<Users> networkuser = userService.findBynetworkuser();
		List<Users> networkuser2 = userService.findBynetworkuser();
		List<ItemTitleBatis> networkknowledge = itemservice.findsecondlayer();
		List<NetworkAnalysis> networkedge = itemservice
				.findfirstandsecondedge();

		// Iterator<Users> it = networkuser.iterator();
		// while (it.hasNext()) {
		// // Listから要素を取り出す
		// Users element = it.next();
		// System.out.print(element.getId());
		// System.out.println(element.getNickname());
		// // title_id[i2] = String.valueOf(element.getId());
		// // title_content[i2] = String.valueOf(element.getContent());
		// // title_item[i2] = String.valueOf(element.getItem());
		// // title_language[i2] = String.valueOf(element.getLanguage());
		// //
		// // i2++;
		// }

		Iterator<Users> User_layer = networkuser.iterator();
		Iterator<Users> User_layer2 = networkuser2.iterator();
		Iterator<ItemTitleBatis> Knowledge_layer = networkknowledge.iterator();
		Iterator<ItemTitleBatis> Knowledge_layer2 = networkknowledge.iterator();
		Iterator<NetworkAnalysis> edge_layer = networkedge.iterator();
		// for(i=0;i<10;i++){
		// n[i] = graphModel.factory().newNode("test"+String.valueOf(i));
		// n[i].getNodeData().setLabel("test"+String.valueOf(i));
		// n[i].getNodeData().setColor(1.0f, 0.5f, 1.0f);
		// n[i].getNodeData().setSize(22f);
		// graph.addNode(n[i]);
		// }
		while (User_layer.hasNext()) {
			Users element = User_layer.next();
			n[i] = graphModel.factory().newNode(
					"0," + String.valueOf(element.getId()));
			n[i].getNodeData().setLabel(element.getNickname());
			// n[i].getNodeData().setY(Float.valueOf(i+5));
			if (itemserachform.getNickname().equals(element.getNickname())) {
				n[i].getNodeData().setColor(1.0f, 0.0f, 1.0f);
				n[i].getNodeData().setSize(22f);
			} else {
				n[i].getNodeData().setColor(0.0f, 0.0f, 1.0f);
				n[i].getNodeData().setSize(15f);
			}
			graph.addNode(n[i]);
			i++;
		}
		while (Knowledge_layer.hasNext()) {
			ItemTitleBatis element = Knowledge_layer.next();
			String count = null;
			int common_count = 0;
			int common_i = 0;
			Iterator<NetworkAnalysis> edge_layer2 = networkedge.iterator();
			String[] common_array = new String[50];

			SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
			String datedata = sdf.format(element.getCreateTime());
			// System.out.println(datedata);
			while (edge_layer2.hasNext()) {
				NetworkAnalysis networkana = edge_layer2.next();
				if (element.getContent().equals(networkana.getContent())) {
					common_array[common_i] = networkana.getAuthor_id() + "$"
							+ sdf.format(networkana.getCreateTime());
					common_i++;
				}

			}

			for (int count_i = 0; count_i < common_array.length; count_i++) {
				if (common_array[count_i] != null) {
					if (count == null) {
						count = "";
					}
					count = count + common_array[count_i] + "#";
					common_count++;
				}
			}
			if (common_count >= 2) {
				// System.out.println(count);
			}
			n[i] = graphModel.factory().newNode(
					String.valueOf("1," + element.getItem() + ","
							+ element.getAuthor_id() + "," + datedata + ","
							+ common_count + "," + count));
			n[i].getNodeData().setLabel(element.getContent());
			// // n[i].getNodeData().setY(Float.valueOf(i+5));
			n[i].getNodeData().setColor(1.0f, 1.0f, 0.0f);
			n[i].getNodeData().setSize(16f);

			// List<ItemTags> itemtags =
			// itemservice.finditemtags(element.getItem());
			// Iterator<ItemTags> tags_layer=itemtags.iterator();

			graph.addNode(n[i]);
			common_i = 0;
			i++;
			// 上位語検索
			// Set<String> hypernyms =
			// HypernymsWordNet(element.getContent(),POS.n);
			// System.out.print("上位語の数:"+hypernyms.size());
			// Iterator<String> hyperresult = hypernyms.iterator();
			// while(hyperresult.hasNext()){
			//
			// n[i] = graphModel.factory().newNode(
			// String.valueOf("1,"+element.getItem()+","));
			//
			// n[i].getNodeData().setLabel(hyperresult.next());
			// // // n[i].getNodeData().setY(Float.valueOf(i+5));
			// n[i].getNodeData().setColor(0.5f, 1.0f, 0.5f);
			// n[i].getNodeData().setSize(18f);
			// graph.addNode(n[i]);
			// i++;
			// }
			// 下位語検索
			Set<String> hyponyms = HyponymsWordNet(element.getContent(), POS.n);
			// n[i] = graphModel.factory().newNode(
			// String.valueOf("1,"+element.getItem()+","+element.getAuthor_id()+","+datedata+","+common_count+","+count));
			// n[i].getNodeData().setLabel(element.getContent());
			// // // n[i].getNodeData().setY(Float.valueOf(i+5));
			// n[i].getNodeData().setColor(1.0f, 1.0f, 0.0f);
			// n[i].getNodeData().setSize(14f);
			// graph.addNode(n[i]);
			// i++;
		}

		while (edge_layer.hasNext()) {
			NetworkAnalysis element = edge_layer.next();
			for (Node no : graph.getNodes().toArray()) {
				if (no.getNodeData().getLabel().equals(element.getNickname())) {
					for (Node no2 : graph.getNodes().toArray()) {
						if (no2.getNodeData().getLabel()
								.equals(element.getContent())) {

							Edge e1 = graphModel.factory().newEdge(
									no.getNodeData().getId() + "-"
											+ no2.getNodeData().getId(), no,
									no2, 1f, false);
							graph.addEdge(e1);

						}
					}
				}
			}
		}

		// Layout
		if (analysisform.getAnalysisalgo() != null) {
			if (analysisform.getAnalysisalgo().equals("Yifan")) {
				AutoLayout autoLayout = new AutoLayout(1, TimeUnit.MINUTES);
				autoLayout.setGraphModel(graphModel);
				YifanHuLayout firstLayout = new YifanHuLayout(null,
						new StepDisplacement(1f));
				ForceAtlasLayout secondLayout = new ForceAtlasLayout(null);

				AutoLayout.DynamicProperty adjustBySizeProperty = AutoLayout
						.createDynamicProperty("Adjust by Sizes", Boolean.TRUE,
								0.1f);
				AutoLayout.DynamicProperty repulsionProperty = AutoLayout
						.createDynamicProperty("Repulsion strength",
								new Double(500.), 0f);

				autoLayout.addLayout(firstLayout, 0.5f);
				// ,new
				// AutoLayout.DynamicProperty[]{adjustBySizeProperty,repulsionProperty}
				autoLayout.addLayout(secondLayout, 0.5f);
				autoLayout.execute();
			}
		}

		// Filter degree　次数で視覚化
		if (analysisform.getDegree() != null
				&& !(analysisform.getDegree().equals(""))) {
			FilterController filterController2 = Lookup.getDefault().lookup(
					FilterController.class);
			DegreeRangeFilter degreeFilter = new DegreeRangeFilter();
			degreeFilter.init(graph);
			// degreeFilter.setRange(new Range(3,Integer.MAX_VALUE));
			degreeFilter.setRange(new Range(Integer.parseInt(analysisform
					.getDegree()), Integer.MAX_VALUE));
			Query query = filterController2.createQuery(degreeFilter);
			GraphView view = filterController2.filter(query);
			graph = graphModel.getGraph(view);
			graphModel.setVisibleView(view);
		}
		// Filter Blograma
		// PartitionController pa =
		// Lookup.getDefault().lookup(PartitionController.class);
		// Partition p =
		// pa.buildPartition(attributeModel.getNodeTable().getColumn("Label"),graph);
		// NodePartitionFilter partitionFilter = new NodePartitionFilter(p);
		// // partitionFilter.init(graph);
		// partitionFilter.unselectAll();
		// partitionFilter.addPart(p.getPartFromValue("maru"));
		// Query query = filterController.createQuery(partitionFilter);
		// GraphView view = filterController.filter(query);
		// graphModel.setVisibleView(view);

		// EgoFilter パターンからの距離まで視覚化
		if (analysisform.getEgofiltername() != null
				&& analysisform.getEgofilterdistance() != null
				&& !(analysisform.getEgofilterdistance().equals(""))
				&& !(analysisform.getEgofiltername().equals(""))) {
			FilterController filterController = Lookup.getDefault().lookup(
					FilterController.class);
			EgoFilter egoFilter = new EgoFilter();
			egoFilter.setPattern(analysisform.getEgofiltername());
			egoFilter.setDepth(Integer.parseInt(analysisform
					.getEgofilterdistance()));
			Query queryEgo = filterController.createQuery(egoFilter);
			GraphView viewEgo = filterController.filter(queryEgo);
			graph = graphModel.getGraph(viewEgo);
			graphModel.setVisibleView(viewEgo);
		}

		// export file//
		ExportController ec = Lookup.getDefault()
				.lookup(ExportController.class);
		try {
			// ec.exportFile(new
			// File("file:///Users/mouri/Documents/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp2/wtpwebapps/miraikan/js/networkanalysis/graph.gexf"));
			ec.exportFile(new File("graph.gexf"));
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
			// System.out.println(abPath);
			// file:///Users/mouri/Documents/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp2/wtpwebapps/miraikan/js/networkanalysis/graph.gexf
			// File file = new File(getClass().getResource(abPath +
			// "/js/networkanalysis/graph.texf").toURI
			// file:///Users/mouri/Documents/workspace/miraikan/WebContent/js/networkanalysis/
			// file:///C:/Users/mouri/Desktop/
			// ec.exportFile(new
			// File("file:///Users/mouri/Documents/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp2/wtpwebapps/miraikan/js/networkanalysis/graph.gexf"),exporter);
			// File directory1 = new
			// File("/Users/mouri/Documents/workspace/miraikan/WebContent/js/networkanalysis/");

			// File directory1 = new
			// File("C:/Users/mouri/work2/.metadata/.plugins/org.eclipse.wst.server.core/tmp1/wtpwebapps/KyushuLearningLog2/WEB-INF");
			ec.exportFile(new File(directory1, "graph.gexf"), exporter);

		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
		i = 0;
	}

	private Set<String> HyponymsWordNet(String item, POS n) {
		// TODO Auto-generated method stub
		Set<String> hyponyms = JAWJAW.findHyponyms(item, n);
		return hyponyms;
	}

	private Set<String> HypernymsWordNet(String item, POS n) {
		// TODO Auto-generated method stub
		Set<String> hypernyms = JAWJAW.findHypernyms(item, n);
		return hypernyms;
	}

	private void WordNet(String word, POS p) {
		// TODO Auto-generated method stub

		Set<String> hypernyms = JAWJAW.findHypernyms(word, p);

		Set<String> hyponyms = JAWJAW.findHyponyms(word, p);

		Set<String> consequents = JAWJAW.findEntailments(word, p);

		Set<String> translations = JAWJAW.findTranslations(word, p);

		Set<String> definitions = JAWJAW.findDefinitions(word, p);

		// 結果表示（多義語はごっちゃになっています）

		// System.out.println("hypernyms of " + word + " : \t" + hypernyms);
		//
		// System.out.println("hyponyms of " + word + " : \t" + hyponyms);
		//
		// System.out.println(word + " entails : \t\t" + consequents);
		//
		// System.out.println("translations of " + word + " : \t" +
		// translations);
		//
		// System.out.println("definitions of " + word + " : \t" + definitions);
	}

	private void CheckedFile() {
		if (exitfile.exists()) {
			exitfile.delete();
		}
	}

	private void PlaceLens() {

		// UserID取得
		Usertest userid = userService.findById(SecurityUserHolder
				.getCurrentUser().getId());

		// PlaceLens network graph
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
		AttributeModel attributeModel = Lookup.getDefault()
				.lookup(AttributeController.class).getModel();
		// Add Node column
		AttributeColumn itemid = attributeModel.getNodeTable().addColumn(
				"itemid", AttributeType.STRING);
		AttributeColumn create_time = attributeModel.getNodeTable().addColumn(
				"create_time", AttributeType.STRING);
		AttributeColumn item_lat = attributeModel.getNodeTable().addColumn(
				"item_lat", AttributeType.STRING);
		AttributeColumn item_lng = attributeModel.getNodeTable().addColumn(
				"item_lng", AttributeType.STRING);
		AttributeColumn author_id = attributeModel.getNodeTable().addColumn(
				"author_id", AttributeType.STRING);

		AttributeColumn place = attributeModel.getNodeTable().addColumn(
				"place", AttributeType.STRING);
		AttributeColumn place_attribute = attributeModel.getNodeTable()
				.addColumn("place_attribute", AttributeType.STRING);

		GraphController gc = Lookup.getDefault().lookup(GraphController.class);
		graphModel = gc.getModel();
		Graph graph = gc.getModel().getDirectedGraph();

		// Graph graph = gc.getModel().getDirectedGraph();
		Node[] n = new Node[50000];

		List<PlaceAnalysis> user = userService.findbymyplace(userid.getId());

	}

	//Visualization Awareness
	private void V_A(AnalysisForm form, ModelMap model){

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
				coocurrence = itemservice.acorrence(user.get(u).getId());
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
						if (form.getEgofiltername().equalsIgnoreCase(
								coocurrence.get(i).getContent())) {
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
			if (Currence.get(i).getTarget_content()
					.equalsIgnoreCase(form.getEgofiltername())) {
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
			if (Currence.get(i).getTarget_content()
					.equalsIgnoreCase(form.getEgofiltername())) {
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
			if (Currence.get(i).getTarget_content()
					.equalsIgnoreCase(form.getEgofiltername())) {
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
		ListRecommend = new ArrayList<Cooccurrence>();
		ListRecommend2 = new ArrayList<String>();
		double degreechecker = 0;
		double index = 0;
		for (int i = knowledge1 + 1; i <= knowledge1stop; i++) {

			if (String.valueOf(
					n[i].getNodeData().getAttributes()
							.getValue(checknumber.getIndex())).equals(
					"knowledge")) {
				Cooccurrence Recomendation = new Cooccurrence();
				Recomendation.setLabel(String.valueOf(n[i].getNodeData()
						.getAttributes().getValue(node_NextItem.getIndex())));
				Recomendation.setTarget_itemid(String.valueOf(n[i]
						.getNodeData().getAttributes()
						.getValue(node_NextItemid.getIndex())));
				Recomendation.setDegreein(String.valueOf(n[i].getNodeData()
						.getAttributes().getValue(degColin.getIndex())));
				Recomendation.setDegreeout(String.valueOf(n[i].getNodeData()
						.getAttributes().getValue(degColout.getIndex())));
				// System.out.println(String.valueOf(n[i].getNodeData().getAttributes().getValue(degColout.getIndex())));

				ListRecommend.add(Recomendation);
			}

		}
		// Place Recommendations
		for (Node no : graph.getNodes().toArray()) {
			if (!StringUtils.isBlank(String.valueOf(no.getNodeData()
					.getAttributes().getValue(checknumber.getIndex())))) {
				if (String.valueOf(
						no.getNodeData().getAttributes()
								.getValue(checknumber.getIndex())).equals(
						"place")) {
					Cooccurrence Recomendation = new Cooccurrence();
					Recomendation.setLabel(no.getNodeData().getLabel());
					// Recomendation.setTarget_itemid(String.valueOf(n[i].getNodeData().getAttributes().getValue(node_NextItemid.getIndex())));
					Recomendation.setDegreein(String.valueOf(no.getNodeData()
							.getAttributes().getValue(degColin.getIndex())));
					Recomendation.setDegreeout(String.valueOf(no.getNodeData()
							.getAttributes().getValue(degColout.getIndex())));

					PlaceRecommend.add(Recomendation);

				}
			}
		}

		for (Node no : graph.getNodes().toArray()) {
			if (no.getNodeData().getAttributes().getValue("KnowledgeRanking") == null
					|| no.getNodeData().getAttributes()
							.getValue("KnowledgeRanking").equals("null")) {
			} else if (Integer.parseInt(String.valueOf(no.getNodeData()
					.getAttributes().getValue("KnowledgeRanking"))) <= 5) {

				if (ListRecommend2.contains(String.valueOf(no.getNodeData()
						.getAttributes().getValue("nickname")))) {

				} else {

					ListRecommend2.add(String.valueOf(no.getNodeData()
							.getAttributes().getValue("nickname")));
				}

			} else {

			}
		}

		Collections.sort(ListRecommend, new Comparator<Cooccurrence>() {
			public int compare(Cooccurrence t1, Cooccurrence t2) {
				return +((int) Integer.valueOf(t2.getDegreeout()) - Integer
						.valueOf(t1.getDegreeout()));

			}
		});

		Collections.sort(PlaceRecommend, new Comparator<Cooccurrence>() {
			public int compare(Cooccurrence t1, Cooccurrence t2) {
				return +((int) Integer.valueOf(t2.getDegreeout()) - Integer
						.valueOf(t1.getDegreeout()));

			}
		});
		for (int i11 = 0; i11 < ListRecommend.size(); i11++) {
			Cooccurrence Recomendation = new Cooccurrence();
			Recomendation.setRanking(i11);
		}

		for (int i11 = 0; i11 < PlaceRecommend.size(); i11++) {
			// System.out.println("PlaceSize:"+PlaceRecommend.size());
			// PlaceRecommend.get(i11).getLabel()
			placecollocation = itemservice.p_collocation(PlaceRecommend
					.get(i11).getLabel());
			Place_C.add(placecollocation);
		}

		// Filtering Algrithm
		String[] CheckP = new String[5];
		int checkp = 0;
		for (Node no : graph.getNodes().toArray()) {
			if (no.getNodeData().getAttributes().getValue("checkplace1") != null) {
				if (no.getNodeData().getAttributes().getValue("checkplace1")
						.equals("1")) {
					CheckP[checkp] = String.valueOf(no.getNodeData()
							.getAttributes().getValue("place"));
					checkp++;

				}
			}
		}

		for (Node no : graph.getNodes().toArray()) {
			if (no.getNodeData().getAttributes().getValue("checkknowledge2") != null) {

				if (no.getNodeData().getAttributes()
						.getValue("checkknowledge2").equals("checkknowledge2")) {
					if (no.getNodeData().getAttributes().getValue("place") != null) {
						// System.out.println(String.valueOf(no.getNodeData().getAttributes().getValue("place")));

					}
				}
			}
		}

		if (form.getAnalysisalgo().equals("Yifan")) {
			YifanHuLayout layout = new YifanHuLayout(null,
					new StepDisplacement(1f));
			layout.setGraphModel(graphModel);
			layout.resetPropertiesValues();
			layout.setOptimalDistance(200f);
			layout.initAlgo();
			for (int i = 0; i < 100 && layout.canAlgo(); i++) {
				layout.goAlgo();
			}

		} else if (form.getAnalysisalgo().equals("Random")) {

			RandomLayout layout = new RandomLayout(new Random(), 1500);
			layout.setGraphModel(graphModel);
			layout.initAlgo();
			layout.goAlgo();
			layout.endAlgo();
		}

		else if (form.getAnalysisalgo().equals("ForceAtlas")) {
			ForceAtlasLayout forceatlaslyout = new ForceAtlasLayout(null);
			forceatlaslyout.setGraphModel(graphModel);
			forceatlaslyout.resetPropertiesValues();
			forceatlaslyout.initAlgo();
			for (int i = 0; i < 100 && forceatlaslyout.canAlgo(); i++) {
				forceatlaslyout.goAlgo();
			}
		}

		// export file//
		ExportController ec = Lookup.getDefault()
				.lookup(ExportController.class);
		try {

			ec.exportFile(new File("kpt.gexf"));
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
			ec.exportFile(new File(directory1, "kpt.gexf"), exporter);

		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

	
		
	}
	
	
	
	// KAL_method**************************************************************************************************************************************************
	private void K_P_T_Lens(AnalysisForm form, ModelMap model) {
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
						if (form.getEgofiltername().equalsIgnoreCase(
								coocurrence.get(i).getContent())) {
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
			if (Currence.get(i).getTarget_content()
					.equalsIgnoreCase(form.getEgofiltername())) {
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
			if (Currence.get(i).getTarget_content()
					.equalsIgnoreCase(form.getEgofiltername())) {
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
			if (Currence.get(i).getTarget_content()
					.equalsIgnoreCase(form.getEgofiltername())) {
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
		ListRecommend = new ArrayList<Cooccurrence>();
		ListRecommend2 = new ArrayList<String>();
		double degreechecker = 0;
		double index = 0;
		for (int i = knowledge1 + 1; i <= knowledge1stop; i++) {

			if (String.valueOf(
					n[i].getNodeData().getAttributes()
							.getValue(checknumber.getIndex())).equals(
					"knowledge")) {
				Cooccurrence Recomendation = new Cooccurrence();
				Recomendation.setLabel(String.valueOf(n[i].getNodeData()
						.getAttributes().getValue(node_NextItem.getIndex())));
				Recomendation.setTarget_itemid(String.valueOf(n[i]
						.getNodeData().getAttributes()
						.getValue(node_NextItemid.getIndex())));
				Recomendation.setDegreein(String.valueOf(n[i].getNodeData()
						.getAttributes().getValue(degColin.getIndex())));
				Recomendation.setDegreeout(String.valueOf(n[i].getNodeData()
						.getAttributes().getValue(degColout.getIndex())));
				// System.out.println(String.valueOf(n[i].getNodeData().getAttributes().getValue(degColout.getIndex())));

				ListRecommend.add(Recomendation);
			}

		}
		// Place Recommendations
		for (Node no : graph.getNodes().toArray()) {
			if (!StringUtils.isBlank(String.valueOf(no.getNodeData()
					.getAttributes().getValue(checknumber.getIndex())))) {
				if (String.valueOf(
						no.getNodeData().getAttributes()
								.getValue(checknumber.getIndex())).equals(
						"place")) {
					Cooccurrence Recomendation = new Cooccurrence();
					Recomendation.setLabel(no.getNodeData().getLabel());
					// Recomendation.setTarget_itemid(String.valueOf(n[i].getNodeData().getAttributes().getValue(node_NextItemid.getIndex())));
					Recomendation.setDegreein(String.valueOf(no.getNodeData()
							.getAttributes().getValue(degColin.getIndex())));
					Recomendation.setDegreeout(String.valueOf(no.getNodeData()
							.getAttributes().getValue(degColout.getIndex())));

					PlaceRecommend.add(Recomendation);

				}
			}
		}

		for (Node no : graph.getNodes().toArray()) {
			if (no.getNodeData().getAttributes().getValue("KnowledgeRanking") == null
					|| no.getNodeData().getAttributes()
							.getValue("KnowledgeRanking").equals("null")) {
			} else if (Integer.parseInt(String.valueOf(no.getNodeData()
					.getAttributes().getValue("KnowledgeRanking"))) <= 5) {

				if (ListRecommend2.contains(String.valueOf(no.getNodeData()
						.getAttributes().getValue("nickname")))) {

				} else {

					ListRecommend2.add(String.valueOf(no.getNodeData()
							.getAttributes().getValue("nickname")));
				}

			} else {

			}
		}

		Collections.sort(ListRecommend, new Comparator<Cooccurrence>() {
			public int compare(Cooccurrence t1, Cooccurrence t2) {
				return +((int) Integer.valueOf(t2.getDegreeout()) - Integer
						.valueOf(t1.getDegreeout()));

			}
		});

		Collections.sort(PlaceRecommend, new Comparator<Cooccurrence>() {
			public int compare(Cooccurrence t1, Cooccurrence t2) {
				return +((int) Integer.valueOf(t2.getDegreeout()) - Integer
						.valueOf(t1.getDegreeout()));

			}
		});
		for (int i11 = 0; i11 < ListRecommend.size(); i11++) {
			Cooccurrence Recomendation = new Cooccurrence();
			Recomendation.setRanking(i11);
		}

		for (int i11 = 0; i11 < PlaceRecommend.size(); i11++) {
			// System.out.println("PlaceSize:"+PlaceRecommend.size());
			// PlaceRecommend.get(i11).getLabel()
			placecollocation = itemservice.p_collocation(PlaceRecommend
					.get(i11).getLabel());
			Place_C.add(placecollocation);
		}

		// Filtering Algrithm
		String[] CheckP = new String[5];
		int checkp = 0;
		for (Node no : graph.getNodes().toArray()) {
			if (no.getNodeData().getAttributes().getValue("checkplace1") != null) {
				if (no.getNodeData().getAttributes().getValue("checkplace1")
						.equals("1")) {
					CheckP[checkp] = String.valueOf(no.getNodeData()
							.getAttributes().getValue("place"));
					checkp++;

				}
			}
		}

		for (Node no : graph.getNodes().toArray()) {
			if (no.getNodeData().getAttributes().getValue("checkknowledge2") != null) {

				if (no.getNodeData().getAttributes()
						.getValue("checkknowledge2").equals("checkknowledge2")) {
					if (no.getNodeData().getAttributes().getValue("place") != null) {
						// System.out.println(String.valueOf(no.getNodeData().getAttributes().getValue("place")));

					}
				}
			}
		}

		if (form.getAnalysisalgo().equals("Yifan")) {
			YifanHuLayout layout = new YifanHuLayout(null,
					new StepDisplacement(1f));
			layout.setGraphModel(graphModel);
			layout.resetPropertiesValues();
			layout.setOptimalDistance(200f);
			layout.initAlgo();
			for (int i = 0; i < 100 && layout.canAlgo(); i++) {
				layout.goAlgo();
			}

		} else if (form.getAnalysisalgo().equals("Random")) {

			RandomLayout layout = new RandomLayout(new Random(), 1500);
			layout.setGraphModel(graphModel);
			layout.initAlgo();
			layout.goAlgo();
			layout.endAlgo();
		}

		else if (form.getAnalysisalgo().equals("ForceAtlas")) {
			ForceAtlasLayout forceatlaslyout = new ForceAtlasLayout(null);
			forceatlaslyout.setGraphModel(graphModel);
			forceatlaslyout.resetPropertiesValues();
			forceatlaslyout.initAlgo();
			for (int i = 0; i < 100 && forceatlaslyout.canAlgo(); i++) {
				forceatlaslyout.goAlgo();
			}
		}

		// export file//
		ExportController ec = Lookup.getDefault()
				.lookup(ExportController.class);
		try {

			ec.exportFile(new File("kpt.gexf"));
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
			ec.exportFile(new File(directory1, "kpt.gexf"), exporter);

		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

	}

	private void TimeOutput() {
		List<Cooccurrence> coocurrence = new ArrayList<Cooccurrence>();
		// Cooccurrence coocurrence = new Cooccurrence();
		coocurrence = itemservice.getAllTimedata();
		// 月ごと
		int January = 0;
		int February = 0;
		int March = 0;
		int April = 0;
		int May = 0;
		int June = 0;
		int July = 0;
		int August = 0;
		int September = 0;
		int October = 0;
		int November = 0;
		int December = 0;
		// 時間ごと
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

		for (int a = 0; a < coocurrence.size(); a++) {

			switch (coocurrence.get(a).getCreate_time().getMonth()) {
			case 0:
				January++;
				break;
			case 1:
				February++;
				break;
			case 2:
				March++;
				break;
			case 3:
				April++;
				break;
			case 4:
				May++;
				break;
			case 5:
				June++;
				break;
			case 6:
				July++;
				break;
			case 7:
				August++;
				break;
			case 8:
				September++;
				break;
			case 9:
				October++;
				break;
			case 10:
				November++;
				break;
			case 11:
				December++;
				break;

			}

		}
		// System.out.println("January:" + January);
		// System.out.println("February:" + February);
		// System.out.println("March:" + March);
		// System.out.println("April:" + April);
		// System.out.println("May:" + May);
		// System.out.println("June:" + June);
		// System.out.println("July:" + July);
		// System.out.println("August:" + August);
		// System.out.println("September:" + September);
		// System.out.println("October:" + October);
		// System.out.println("November:" + November);
		// System.out.println("December:" + December);

		// System.out.println("配列の大きさ" + coocurrence.size());
		for (int a = 0; a < coocurrence.size(); a++) {

			switch (coocurrence.get(a).getCreate_time().getHours()) {

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

		// System.out.println("0時:" + twenty_four);
		// System.out.println("1時:" + one);
		// System.out.println("2時:" + two);
		// System.out.println("3時:" + three);
		// System.out.println("4時:" + four);
		// System.out.println("5時:" + five);
		// System.out.println("6時:" + six);
		// System.out.println("7時:" + seven);
		// System.out.println("8時:" + eight);
		// System.out.println("9時:" + nine);
		// System.out.println("10時:" + ten);
		// System.out.println("11時:" + eleven);
		//
		// System.out.println("12時:" + twelve);
		// System.out.println("13時:" + thirteen);
		// System.out.println("14時:" + fourteen);
		// System.out.println("15時:" + fifteen);
		// System.out.println("16時:" + sixteen);
		// System.out.println("17時:" + seventeen);
		// System.out.println("18時:" + eighteen);
		// System.out.println("19時:" + nineteen);
		// System.out.println("20時:" + twenty);
		// System.out.println("21時:" + twenty_one);
		// System.out.println("22時:" + twenty_two);
		// System.out.println("23時:" + twenty_three);
		// 春1月~3月

		// 夏4月~6月

		// 秋7月～9月

		// 冬10月～12月

		// 月ごと1月～12月

		// 24時間ごと

	}

	private void TopdownVisualization(AnalysisForm form,ModelMap model) {
		// UserID取得
		Usertest userid = userService.findById(SecurityUserHolder
				.getCurrentUser().getId());
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
		// First layer attributes (id,age,gender,nationality,stay,j_level)

		AttributeColumn F_number = attributeModel.getNodeTable().addColumn(
				"F_number", AttributeType.STRING);
		AttributeColumn F_author_id = attributeModel.getNodeTable().addColumn(
				"F_author_id", AttributeType.STRING);
		AttributeColumn F_nationality = attributeModel.getNodeTable()
				.addColumn("F_nationality", AttributeType.STRING);
		AttributeColumn F_gender = attributeModel.getNodeTable().addColumn(
				"F_gender", AttributeType.STRING);
		AttributeColumn F_age = attributeModel.getNodeTable().addColumn(
				"F_age", AttributeType.STRING);
		AttributeColumn F_stay = attributeModel.getNodeTable().addColumn(
				"F_stay", AttributeType.STRING);
		AttributeColumn F_JPlevel = attributeModel.getNodeTable().addColumn(
				"F_JPlevel", AttributeType.STRING);
		AttributeColumn F_nickname = attributeModel.getNodeTable().addColumn(
				"F_nickname", AttributeType.STRING);

		// Second layer
		// attributes(itemid,create_time,item_lat,item_lng,author_id,image,content,synset,pos,src)

		AttributeColumn S_number = attributeModel.getNodeTable().addColumn(
				"S_number", AttributeType.STRING);
		AttributeColumn S_itemid = attributeModel.getNodeTable().addColumn(
				"S_itemid", AttributeType.STRING);
		AttributeColumn S_create_time = attributeModel.getNodeTable()
				.addColumn("S_create_time", AttributeType.STRING);
		AttributeColumn S_item_lat = attributeModel.getNodeTable().addColumn(
				"S_item_lat", AttributeType.STRING);
		AttributeColumn S_item_lng = attributeModel.getNodeTable().addColumn(
				"S_item_lng", AttributeType.STRING);
		AttributeColumn S_author_id = attributeModel.getNodeTable().addColumn(
				"S_author_id", AttributeType.STRING);
		AttributeColumn S_image = attributeModel.getNodeTable().addColumn(
				"S_image", AttributeType.STRING);
		AttributeColumn S_content = attributeModel.getNodeTable().addColumn(
				"S_content", AttributeType.STRING);
		AttributeColumn S_synset = attributeModel.getNodeTable().addColumn(
				"S_synset", AttributeType.STRING);
		AttributeColumn S_pos = attributeModel.getNodeTable().addColumn(
				"S_pos", AttributeType.STRING);
		AttributeColumn S_src = attributeModel.getNodeTable().addColumn(
				"S_src", AttributeType.STRING);

		// Thirdlayer layer
		AttributeColumn T_itemid = attributeModel.getNodeTable().addColumn(
				"T_itemid", AttributeType.STRING);

		AttributeColumn T_create_time = attributeModel.getNodeTable()
				.addColumn("T_create_time", AttributeType.STRING);

		AttributeColumn T_lat = attributeModel.getNodeTable().addColumn(
				"T_lat", AttributeType.STRING);

		AttributeColumn T_lng = attributeModel.getNodeTable().addColumn(
				"T_lng", AttributeType.STRING);

		AttributeColumn T_authorid = attributeModel.getNodeTable().addColumn(
				"T_authorid", AttributeType.STRING);
		AttributeColumn T_content = attributeModel.getNodeTable().addColumn(
				"T_content", AttributeType.STRING);
		AttributeColumn T_place = attributeModel.getNodeTable().addColumn(
				"T_place", AttributeType.STRING);
		AttributeColumn T_attribute = attributeModel.getNodeTable().addColumn(
				"T_attribute", AttributeType.STRING);

		// Graph Controller 作成
		GraphController gc = Lookup.getDefault().lookup(GraphController.class);
		graphModel = gc.getModel();
		Graph graph = gc.getModel().getDirectedGraph();
		Graph graph3 = gc.getModel().getUndirectedGraph();
		Node[] n = new Node[100000];
		Node[] n2 = new Node[100000];
		// ***********************************First_layer*********************************************************************************************************************
		// First layer node info
		List<TDAfirstlayer> tdafirstlayer = new ArrayList<TDAfirstlayer>();
		tdafirstlayer = userService.findtdafirstlayer();

		System.out.println("値：" + tdafirstlayer.size());
		// firstlayer nodes
		for (int i = 0; i < tdafirstlayer.size(); i++) {

			n[i] = graphModel.factory().newNode(
					String.valueOf(tdafirstlayer.get(i).getNickname()));
			n[i].getNodeData().setLabel(tdafirstlayer.get(i).getNickname());

			n[i].getNodeData().setColor(0.0f, 0.0f, 1.0f);
			n[i].getNodeData().setSize(18f);

			// First layer adding nodes
			n[i].getNodeData()
					.getAttributes()
					.setValue(F_author_id.getIndex(),
							tdafirstlayer.get(i).getId());
			n[i].getNodeData()
					.getAttributes()
					.setValue(F_nationality.getIndex(),
							tdafirstlayer.get(i).getNationality());
			n[i].getNodeData()
					.getAttributes()
					.setValue(F_gender.getIndex(),
							tdafirstlayer.get(i).getGender());
			n[i].getNodeData().getAttributes()
					.setValue(F_age.getIndex(), tdafirstlayer.get(i).getAge());
			n[i].getNodeData()
					.getAttributes()
					.setValue(F_stay.getIndex(), tdafirstlayer.get(i).getStay());
			n[i].getNodeData()
					.getAttributes()
					.setValue(F_JPlevel.getIndex(),
							tdafirstlayer.get(i).getJ_level());
			n[i].getNodeData()
					.getAttributes()
					.setValue(F_nickname.getIndex(),
							tdafirstlayer.get(i).getNickname());
			n[i].getNodeData().getAttributes()
					.setValue(F_number.getIndex(), "1");
			n[i].getNodeData().getAttributes()
					.setValue(S_number.getIndex(), "0");

			graph.addNode(n[i]);

		}
		// First Node remove

		for (Node no : graph.getNodes().toArray()) {
			for (String nationality : form.getNationality()) {
				if (no.getNodeData().getAttributes().getValue("F_nationality")
						.equals(nationality)) {
					graph.removeNode(no);
				}

			}
		}

		for (Node no : graph.getNodes().toArray()) {
			for (Node no2 : graph.getNodes().toArray()) {
				if (no.getNodeData()
						.getAttributes()
						.getValue("F_nationality")
						.equals(no2.getNodeData().getAttributes()
								.getValue("F_nationality"))
						&& !no.getNodeData()
								.getAttributes()
								.getValue("F_author_id")
								.equals(no2.getNodeData().getAttributes()
										.getValue("F_author_id"))) {
					Edge e10 = graphModel.factory()
							.newEdge(KeyGenerateUtil.generateIdUUID(), no, no2,
									1f, true);

					graph.addEdge(e10);
				}
			}

		}

		// ***********************************Second_layer*********************************************************************************************************************

		List<TDAsecondlayer> tdasecondlayer = new ArrayList<TDAsecondlayer>();
		tdasecondlayer = itemservice.findtdasecondlayer();

		// secondlayer nodes
		for (int i = 0; i < tdasecondlayer.size(); i++) {

			n[i] = graphModel.factory().newNode(
					String.valueOf(tdasecondlayer.get(i).getItemid()));
			n[i].getNodeData().setLabel(tdasecondlayer.get(i).getContent());

			n[i].getNodeData().setColor(0.0f, 1.0f, 1.0f);
			n[i].getNodeData().setSize(16f);

			// First layer adding nodes
			n[i].getNodeData()
					.getAttributes()
					.setValue(S_itemid.getIndex(),
							tdasecondlayer.get(i).getItemid());
			n[i].getNodeData()
					.getAttributes()
					.setValue(S_create_time.getIndex(),
							tdasecondlayer.get(i).getCreate_time());
			n[i].getNodeData()
					.getAttributes()
					.setValue(S_item_lat.getIndex(),
							tdasecondlayer.get(i).getItem_lat());
			n[i].getNodeData()
					.getAttributes()
					.setValue(S_item_lng.getIndex(),
							tdasecondlayer.get(i).getItem_lng());
			n[i].getNodeData()
					.getAttributes()
					.setValue(S_author_id.getIndex(),
							tdasecondlayer.get(i).getAuthor_id());
			n[i].getNodeData()
					.getAttributes()
					.setValue(S_image.getIndex(),
							tdasecondlayer.get(i).getImage());
			n[i].getNodeData()
					.getAttributes()
					.setValue(S_content.getIndex(),
							tdasecondlayer.get(i).getContent());
			n[i].getNodeData()
					.getAttributes()
					.setValue(S_synset.getIndex(),
							tdasecondlayer.get(i).getSynset());
			n[i].getNodeData().getAttributes()
					.setValue(S_pos.getIndex(), tdasecondlayer.get(i).getPos());
			n[i].getNodeData().getAttributes()
					.setValue(S_src.getIndex(), tdasecondlayer.get(i).getSrc());
			n[i].getNodeData().getAttributes()
					.setValue(S_number.getIndex(), "2");
			n[i].getNodeData().getAttributes()
					.setValue(F_number.getIndex(), "0");

			graph.addNode(n[i]);

		}

		// ***********************************edge_links*********************************************************************************************************************

		for (Node no : graph.getNodes().toArray()) {
			if (!StringUtils.isBlank(String.valueOf(no.getNodeData()
					.getAttributes().getValue("F_number")))) {

			}
			if (no.getNodeData().getAttributes().getValue("F_number")
					.equals("1")) {
				for (Node no2 : graph.getNodes().toArray()) {
					if (no2.getNodeData().getAttributes().getValue("S_number")
							.equals("2")
							&& no.getNodeData()
									.getAttributes()
									.getValue("F_author_id")
									.equals(no2.getNodeData().getAttributes()
											.getValue("S_author_id"))) {

						Edge e10 = graphModel.factory().newEdge(
								KeyGenerateUtil.generateIdUUID(), no, no2, 1f,
								true);

						graph.addEdge(e10);

					}

				}
			}
		}

		// Degree calucurations and remove nodes

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

		for (Node no : graph.getNodes().toArray()) {
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

			if (String.valueOf(
					no.getNodeData().getAttributes()
							.getValue(degCol.getIndex())).equals("0")) {
				System.out.println(no.getNodeData().getAttributes()
						.getValue(degCol.getIndex()));
				graph.removeNode(no);
			}
			// no.getNodeData().getAttributes().setValue(de.getIndex(),Degree.DEGREE);

		}

		// Common_Knowledge_Connection
		for (Node no : graph.getNodes().toArray()) {
			if (no.getNodeData().getAttributes().getValue("S_number")
					.equals("2")) {
				for (Node no2 : graph.getNodes().toArray()) {
					if (no2.getNodeData().getAttributes().getValue("S_number")
							.equals("2")
							&& !(no.getNodeData().getId().equals(no2
									.getNodeData().getId()))
							&& no.getNodeData().getLabel()
									.equals(no2.getNodeData().getLabel())) {

						Edge e10 = graphModel.factory().newEdge(
								KeyGenerateUtil.generateIdUUID(), no, no2, 1f,
								true);

						graph.addEdge(e10);
					}

				}

			}

		}

		// Second Node remove
		if (form.getGender() != null && form.getGender().size() != 0) {
			for (Node no : graph.getNodes().toArray()) {

				for (String gender : form.getGender()) {
					if (no.getNodeData().getAttributes()
							.getValue("F_gender")!=null && no.getNodeData().getAttributes().getValue("F_gender").equals(gender)) {
						graph.removeNode(no);
					}

				}
			}

		}

		if (form.getAge() != null && form.getAge().size() != 0) {
			for (Node no : graph.getNodes().toArray()) {
				for (String age : form.getAge()) {
					if (no.getNodeData().getAttributes()
							.getValue("F_age")!=null && no.getNodeData().getAttributes().getValue("F_age").equals(age)) {
						graph.removeNode(no);
					}

				}
			}
		}

		if (form.getKnowledge() != null && form.getKnowledge().size() != 0) {
			for (Node no : graph.getNodes().toArray()) {
				for (String knowledge : form.getKnowledge()) {
					if (no.getNodeData().getAttributes()
							.getValue("S_pos")!=null && no.getNodeData().getAttributes().getValue("S_pos").equals(knowledge)) {
						graph.removeNode(no);
					}

				}
			}
		}

		if (form.getPlace() != null && form.getPlace().size() != 0) {
			for (Node no : graph.getNodes().toArray()) {
				for (String place : form.getPlace()) {
					if (no.getNodeData().getAttributes()
							.getValue("T_place")!=null && no.getNodeData().getAttributes().getValue("T_place").equals(place)) {
						graph.removeNode(no);
					}
				}
			}
		}

		// third layer

		List<TDAthirdlayer> tdathirdlayer = new ArrayList<TDAthirdlayer>();
		tdathirdlayer = itemservice.findtdathirdlayer();

		// thirdlayer nodes
		for (int i = 0; i < tdathirdlayer.size(); i++) {

			n2[i] = graphModel.factory().newNode(
					String.valueOf(KeyGenerateUtil.generateIdUUID()));
			n2[i].getNodeData().setLabel(tdathirdlayer.get(i).getAttribute());

			n2[i].getNodeData().setColor(1.0f, 1.0f, 0.1f);
			n2[i].getNodeData().setSize(16f);

			// First layer adding nodes
			n2[i].getNodeData()
					.getAttributes()
					.setValue(T_itemid.getIndex(),
							tdathirdlayer.get(i).getItemid());
			n2[i].getNodeData()
					.getAttributes()
					.setValue(T_create_time.getIndex(),
							tdathirdlayer.get(i).getCreate_time());
			n2[i].getNodeData().getAttributes()
					.setValue(T_lat.getIndex(), tdathirdlayer.get(i).getLat());
			n2[i].getNodeData().getAttributes()
					.setValue(T_lng.getIndex(), tdathirdlayer.get(i).getLng());
			n2[i].getNodeData()
					.getAttributes()
					.setValue(T_authorid.getIndex(),
							tdathirdlayer.get(i).getAuthorid());
			n2[i].getNodeData()
					.getAttributes()
					.setValue(T_content.getIndex(),
							tdathirdlayer.get(i).getContent());
			// n[i].getNodeData()
			// .getAttributes()
			// .setValue(T_place.getIndex(),
			// tdathirdlayer.get(i).getPlace());
			n2[i].getNodeData()
					.getAttributes()
					.setValue(T_attribute.getIndex(),
							tdathirdlayer.get(i).getAttribute());

			n2[i].getNodeData().getAttributes()
					.setValue(S_number.getIndex(), "3");
			n2[i].getNodeData().getAttributes()
					.setValue(F_number.getIndex(), "0");
			n2[i].getNodeData().getAttributes()
					.setValue(S_itemid.getIndex(), "none");
			graph.addNode(n2[i]);

		}

		// Knowledge connect to place attribute
		for (Node no : graph.getNodes().toArray()) {
			if (no.getNodeData().getAttributes().getValue("S_number")
					.equals("2")
					|| no.getNodeData().getAttributes().getValue("S_number")
							.equals("3")) {
				for (Node no2 : graph.getNodes().toArray()) {
					if (no.getNodeData()
							.getAttributes()
							.getValue("S_itemid")
							.equals(no2.getNodeData().getAttributes()
									.getValue("T_itemid"))) {

						Edge e10 = graphModel.factory().newEdge(
								KeyGenerateUtil.generateIdUUID(), no, no2, 1f,
								true);

						graph.addEdge(e10);
					}

				}

			}

		}

		for (Node no : graph.getNodes().toArray()) {
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

			if (String.valueOf(
					no.getNodeData().getAttributes()
							.getValue(degCol.getIndex())).equals("0")) {
				System.out.println(no.getNodeData().getAttributes()
						.getValue(degCol.getIndex()));
				graph.removeNode(no);
			}
			// no.getNodeData().getAttributes().setValue(de.getIndex(),Degree.DEGREE);

		}

		
		//Layout Types
		
		if (form.getLayouttype().equals("Yifan")) {
			YifanHuLayout layout = new YifanHuLayout(null,
					new StepDisplacement(1f));
			layout.setGraphModel(graphModel);
			layout.resetPropertiesValues();
			layout.setOptimalDistance(200f);
			layout.initAlgo();
			for (int i = 0; i < 10000 && layout.canAlgo(); i++) {
				layout.goAlgo();
			}

		} else  {

			RandomLayout layout = new RandomLayout(new Random(), 1500);
			layout.setGraphModel(graphModel);
			layout.initAlgo();
			layout.goAlgo();
			layout.endAlgo();
		}
		
		
		//Recommendation Logs
		
		GraphDistance distance = new GraphDistance();
		distance.setDirected(true);
		distance.execute(graphModel, attributeModel);
		 
		AttributeColumn centralityColumn = attributeModel.getNodeTable().getColumn(GraphDistance.BETWEENNESS);

		
		
		for (Node no : graph.getNodes().toArray()) {
			no.getNodeData().getAttributes()
					.setValue(degCol.getIndex(), graph.getDegree(no));
			no.getNodeData()
					.getAttributes()
					.setValue(
							degColin.getIndex(),
							graph.getGraphModel().getDirectedGraph()
									.getInDegree(no));
		}
		
		
		//betweenness
		List<Cooccurrence> recommend_tda = new ArrayList<Cooccurrence>();
		//clossness
		List<Cooccurrence> recommend_tda2 = new ArrayList<Cooccurrence>();
		//degree
		List<Cooccurrence> recommend_tda3 = new ArrayList<Cooccurrence>();
		//original algorithm
		List<Cooccurrence> recommend_tda4 = new ArrayList<Cooccurrence>();
		
		int between_count=0;
		for (Node no : graph.getNodes().toArray()) {
			Cooccurrence recommend_tda_attribute = new Cooccurrence();
			if(no.getNodeData().getAttributes().getValue("S_number").equals("2")){
			recommend_tda_attribute.setR_betweenness(String.valueOf(no.getNodeData().getAttributes().getValue(GraphDistance.BETWEENNESS)));
			recommend_tda_attribute.setR_clossness(String.valueOf(no.getNodeData().getAttributes().getValue(GraphDistance.CLOSENESS)));
			recommend_tda_attribute.setR_degree(String.valueOf(no.getNodeData().getAttributes().getValue(Degree.DEGREE)));
			
			recommend_tda_attribute.setId(String.valueOf(no.getNodeData().getAttributes().getValue("S_itemid")));
			
			recommend_tda_attribute.setContent(String.valueOf(no.getNodeData().getAttributes().getValue("S_content")));
			recommend_tda_attribute.setAuthorid(String.valueOf(no.getNodeData().getAttributes().getValue("S_author_id")));
			
			
			recommend_tda.add(recommend_tda_attribute);
			}	
		}
	
		for (Node no : graph.getNodes().toArray()) {
			Cooccurrence recommend_tda_attribute = new Cooccurrence();
			if(no.getNodeData().getAttributes().getValue("S_number").equals("2")){
			recommend_tda_attribute.setR_betweenness(String.valueOf(no.getNodeData().getAttributes().getValue(GraphDistance.BETWEENNESS)));
			recommend_tda_attribute.setR_clossness(String.valueOf(Double.parseDouble(String.valueOf(no.getNodeData().getAttributes().getValue(GraphDistance.CLOSENESS)))*10));
			recommend_tda_attribute.setR_degree(String.valueOf(no.getNodeData().getAttributes().getValue(Degree.DEGREE)));
			
			recommend_tda_attribute.setId(String.valueOf(no.getNodeData().getAttributes().getValue("S_itemid")));
			
			recommend_tda_attribute.setContent(String.valueOf(no.getNodeData().getAttributes().getValue("S_content")));
			recommend_tda_attribute.setAuthorid(String.valueOf(no.getNodeData().getAttributes().getValue("S_author_id")));
			
			
			recommend_tda2.add(recommend_tda_attribute);
			}	
		}
	
		for (Node no : graph.getNodes().toArray()) {
			Cooccurrence recommend_tda_attribute = new Cooccurrence();
			if(no.getNodeData().getAttributes().getValue("S_number").equals("2")){
			recommend_tda_attribute.setR_betweenness(String.valueOf(no.getNodeData().getAttributes().getValue(GraphDistance.BETWEENNESS)));
			recommend_tda_attribute.setR_clossness(String.valueOf(no.getNodeData().getAttributes().getValue(GraphDistance.CLOSENESS)));
			recommend_tda_attribute.setR_degree(String.valueOf(no.getNodeData().getAttributes().getValue(Degree.DEGREE)));
			
			recommend_tda_attribute.setId(String.valueOf(no.getNodeData().getAttributes().getValue("S_itemid")));
			
			recommend_tda_attribute.setContent(String.valueOf(no.getNodeData().getAttributes().getValue("S_content")));
			recommend_tda_attribute.setAuthorid(String.valueOf(no.getNodeData().getAttributes().getValue("S_author_id")));
			
			
			recommend_tda3.add(recommend_tda_attribute);
			}	
		}
	
		for (Node no : graph.getNodes().toArray()) {
			Cooccurrence recommend_tda_attribute = new Cooccurrence();
			if(no.getNodeData().getAttributes().getValue("S_number").equals("2")){
			recommend_tda_attribute.setR_betweenness(String.valueOf(Double.parseDouble(String.valueOf(no.getNodeData().getAttributes().getValue(GraphDistance.CLOSENESS)))*10));
			recommend_tda_attribute.setR_clossness(String.valueOf(no.getNodeData().getAttributes().getValue(GraphDistance.CLOSENESS)));
			recommend_tda_attribute.setR_degree(String.valueOf(no.getNodeData().getAttributes().getValue(Degree.DEGREE)));
			
			recommend_tda_attribute.setId(String.valueOf(no.getNodeData().getAttributes().getValue("S_itemid")));
			
			recommend_tda_attribute.setContent(String.valueOf(no.getNodeData().getAttributes().getValue("S_content")));
			recommend_tda_attribute.setAuthorid(String.valueOf(no.getNodeData().getAttributes().getValue("S_author_id")));
			
			
			recommend_tda4.add(recommend_tda_attribute);
			}	
		}
	
		//New algorithm*****************************************************************
		double p_b=0.0;
		double p_c=0.0;
		double p_d=0.0;
		
		for(int i=0;i<recommend_tda4.size();i++){
			p_b=p_b+Double.parseDouble(recommend_tda4.get(i).getR_betweenness());
			p_c=p_c+Double.parseDouble(recommend_tda4.get(i).getR_clossness());
			p_d=p_d+Double.parseDouble(recommend_tda4.get(i).getR_degree());
		}
	
		
		for(int i=0;i<recommend_tda4.size();i++){
			Cooccurrence recommend_tda_attribute = new Cooccurrence();
			double b_index=Double.parseDouble(recommend_tda4.get(i).getR_betweenness());
			double c_index=Double.parseDouble(recommend_tda4.get(i).getR_clossness());
			double d_index=Double.parseDouble(recommend_tda4.get(i).getR_degree());
			
			//caluculating
		
			double r=(((b_index/p_b)+(c_index/p_c)+(d_index/p_d))/3.0)*1000000;
//			System.out.println("rの値:"+r);
			recommend_tda_attribute.setNew_algorithm(r);
			recommend_tda_attribute.setR_betweenness(recommend_tda4.get(i).getR_betweenness());
			recommend_tda_attribute.setR_clossness(recommend_tda4.get(i).getR_clossness());
			recommend_tda_attribute.setR_degree(recommend_tda4.get(i).getR_degree());
			recommend_tda_attribute.setId(recommend_tda4.get(i).getId());
			recommend_tda_attribute.setContent(recommend_tda4.get(i).getContent());
			recommend_tda_attribute.setAuthorid(recommend_tda4.get(i).getAuthorid());
			recommend_tda4.set(i, recommend_tda_attribute);
			
			
		}
		
		//*******************************************************************************
		
		
		Collections.sort(recommend_tda, new Comparator<Cooccurrence>() {
			public int compare(Cooccurrence t1, Cooccurrence t2) {
				return +((int) (Double.parseDouble(t2.getR_betweenness()) - Double.parseDouble(t1.getR_betweenness())));

			}
		});

		model.addAttribute("betweenness_recommendation",recommend_tda);
		
		Collections.sort(recommend_tda2, new Comparator<Cooccurrence>() {
			public int compare(Cooccurrence t1, Cooccurrence t2) {
				return +((int) (Double.parseDouble(t2.getR_clossness()) - Double.parseDouble(t1.getR_clossness())));

			}
		});

		model.addAttribute("closeness_recommendation",recommend_tda2);
		
		Collections.sort(recommend_tda3, new Comparator<Cooccurrence>() {
			public int compare(Cooccurrence t1, Cooccurrence t2) {
				return +((int) (Double.parseDouble(t2.getR_degree()) - Double.parseDouble(t1.getR_degree())));

			}
		});

		model.addAttribute("degree_recommendation",recommend_tda3);
		
		
		Collections.sort(recommend_tda4, new Comparator<Cooccurrence>() {
			public int compare(Cooccurrence t1, Cooccurrence t2) {
				return +((int) (t2.getNew_algorithm() -  t1.getNew_algorithm()));

			}
		});
		
		model.addAttribute("original_tecommendation",recommend_tda4);
		
//		Collections.sort(recommend_tda, new Comparator<Cooccurrence>() {
//			public int compare(Cooccurrence t1, Cooccurrence t2) {
//				return +((int) Integer.valueOf(t2.getR_betweenness()) - Integer
//						.valueOf(t1.getR_betweenness()));
//
//			}
//		});
//		
		
		// export file//
		ExportController ec = Lookup.getDefault()
				.lookup(ExportController.class);
		try {

			ec.exportFile(new File("tdadata.gexf"));
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
			ec.exportFile(new File(directory1, "tdadata.gexf"), exporter);

		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

	}
}
