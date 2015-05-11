package jp.ac.tokushima_u.is.ll.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.entity.ItemTitle;
import jp.ac.tokushima_u.is.ll.entity.wordnet.Ancestor;
import jp.ac.tokushima_u.is.ll.entity.wordnet.LinkDef;
import jp.ac.tokushima_u.is.ll.entity.wordnet.PosDef;
import jp.ac.tokushima_u.is.ll.entity.wordnet.Sense;
import jp.ac.tokushima_u.is.ll.entity.wordnet.Synlink;
import jp.ac.tokushima_u.is.ll.entity.wordnet.Synset;
import jp.ac.tokushima_u.is.ll.entity.wordnet.SynsetDef;
import jp.ac.tokushima_u.is.ll.entity.wordnet.SynsetEx;
import jp.ac.tokushima_u.is.ll.entity.wordnet.Variant;
import jp.ac.tokushima_u.is.ll.entity.wordnet.Word;
import jp.ac.tokushima_u.is.ll.entity.wordnet.Xlink;

import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author maou
 */
@Service
@Transactional
public class WordNetService {

    @Autowired
    private GoogleTranslateService googleTranslateService;
    
	private HibernateDao<Item, String> itemDao;
	private HibernateDao<Ancestor, String> ancestorDao;
	private HibernateDao<LinkDef, String> linkdefDao;
	private HibernateDao<PosDef, String> posdefDao;
	private HibernateDao<Sense, String> senseDao;
	private HibernateDao<Synlink, String> synlinkDao;
	private HibernateDao<Synset, String> synsetDao;
	private HibernateDao<SynsetDef, String> synsetdefDao;
	private HibernateDao<SynsetEx, String> synsetexDao;
	private HibernateDao<Variant, String> variantDao;
	private HibernateDao<Word, String> wordDao;
	private HibernateDao<Xlink, String> xlinkDao;

	private Map<String, String>  _translateCache = new HashMap<String, String>();
	
	private SessionFactory sessionFactory;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		itemDao = new HibernateDao<Item, String>(sessionFactory, Item.class);
		ancestorDao = new HibernateDao<Ancestor, String>(sessionFactory, Ancestor.class);
		linkdefDao = new HibernateDao<LinkDef, String>(sessionFactory, LinkDef.class);
		posdefDao = new HibernateDao<PosDef, String>(sessionFactory, PosDef.class);
		senseDao = new HibernateDao<Sense, String>(sessionFactory, Sense.class);
		synlinkDao = new HibernateDao<Synlink, String>(sessionFactory, Synlink.class);
		synsetDao = new HibernateDao<Synset, String>(sessionFactory, Synset.class);
		synsetdefDao = new HibernateDao<SynsetDef, String>(sessionFactory, SynsetDef.class);
		synsetexDao = new HibernateDao<SynsetEx, String>(sessionFactory, SynsetEx.class);
		variantDao = new HibernateDao<Variant, String>(sessionFactory, Variant.class);
		wordDao = new HibernateDao<Word, String>(sessionFactory, Word.class);
		xlinkDao = new HibernateDao<Xlink, String>(sessionFactory, Xlink.class);
	}

	/**
	 * getSynonym
	 * 類義語+学習フラグを得る
	 * 
	 * @param word 英単語
	 * @param userid ユーザID
	 * @return 英単語のリスト構造
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Object[]> getSynonym(String word, String userid) {
		String sql = "SELECT DISTINCT "
			+" W2.lemma, W2.pos, W2.lang"//W2.*
			+"	, CASE WHEN item.author_id IS NULL THEN '0' ELSE '1' END AS learned"
			+"	from wn_word AS W"
			+"	LEFT JOIN wn_sense AS S USING(`wordid`)"
			+"	LEFT JOIN wn_sense AS S2 USING(`synset`)"
			+"	LEFT JOIN wn_word AS W2 ON W2.wordid = S2.wordid"
			+"	LEFT JOIN ("
			+"		SELECT DISTINCT author_id,en_title"
			+"		FROM t_item WHERE author_id = :author_id"
			+"	) AS item ON item.en_title = W2.lemma"
			+"	WHERE W.lemma = :word AND S2.lexid < 10 AND S2.freq < 10"
			+"	AND W2.lemma != :word AND W2.lang = 'eng'"
			+"	ORDER BY W2.wordid ASC"
			+"	";//LIMIT 0, 20
		Session session = this.sessionFactory.getCurrentSession();
		return session.createSQLQuery(sql)
		//.addEntity("W2", Word.class).addEntity("learned", String.class)
		.setParameter("word", word).setParameter("author_id", userid).list();
	}

	/**
	 * getSynonym
	 * 類義語を得る
	 * 
	 * @param word 英単語
	 * @return 英単語のリスト構造
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Word> getSynonym(String word) {
		String sql = "SELECT DISTINCT "
			+"	W2.*"//W2.wordid,W2.lemma,W2.pos
			+"	from wn_word AS W"
			+"	LEFT JOIN wn_sense AS S USING(`wordid`)"
			+"	LEFT JOIN wn_sense AS S2 USING(`synset`)"
			+"	LEFT JOIN wn_word AS W2 ON W2.wordid = S2.wordid"
			+"	WHERE W.lemma = :word AND S.lang = 'eng'"
			+"	AND W2.lemma != :word AND W2.lang = 'eng'"
			+"	ORDER BY W2.wordid ASC"
			+"	";//LIMIT 0, 20
		Session session = this.sessionFactory.getCurrentSession();
		return session.createSQLQuery(sql).addEntity(Word.class).setParameter("word", word).list();
	}

	/**
	 * getSynonym
	 * 類義語を得る
	 * 
	 * @param titles itemのもつtitles(自動で英単語を選択)
	 * @return 英単語のリスト構造
	 */
	@Transactional(readOnly = true)
	public List<Word> getSynonym(List<ItemTitle> titles) {
		String title = getEnTitle(titles);
		return getSynonym(title);
	}
	
	
	/**
	 * getSynonymBySynset
	 * 類義語を得る
	 * 
	 * @param titles itemのもつtitles(自動で英単語を選択)
	 * @return 英単語のリスト構造
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Word> getSynonymBySynset(String synset) {
		String sql = "SELECT DISTINCT "
			+"	W2.*"//W2.wordid,W2.lemma,W2.pos
			+"	from wn_sense AS S"
			+"	LEFT JOIN wn_word AS W2 ON W2.wordid = S.wordid"
			+"	WHERE S.synset = :synset "
			+"	ORDER BY W2.wordid ASC"
			+"	";//LIMIT 0, 20
		Session session = this.sessionFactory.getCurrentSession();
		return session.createSQLQuery(sql).addEntity(Word.class).setParameter("synset", synset).list();
		
	}
	

	/**
	 * getSynset
	 * 単語のSynsetを得る
	 * 
	 * @param word 単語
	 * @return Synsetのリスト構造
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Sense> getSynsetList(String word) {
		String sql = "SELECT DISTINCT " +
				"	{S.*} " +
				"	FROM (SELECT * FROM wn_word WHERE lemma = :word) AS W " +
				"	INNER JOIN wn_sense AS S USING(`wordid`)" +
				"	ORDER BY S.wordid,S.lexid,S.freq";

		Session session = this.sessionFactory.getCurrentSession();
		return session.createSQLQuery(sql)
				.addEntity("S", Sense.class)
				.setParameter("word", word).list();
	}
	/**
	 * getSynset
	 * 単語のSynsetを得る
	 * 
	 * @param word 単語
	 * @return Synsetのリスト構造
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<SynsetDef> getSynset(String word) {
		String sql = "SELECT DISTINCT " +
				"	{SD.*} " +
				"	FROM (SELECT * FROM wn_word WHERE lemma = :word) AS W " +
				"	INNER JOIN wn_sense AS S USING(`wordid`)" +
				"	INNER JOIN wn_synset_def AS SD" +
				"		 ON (S.synset = SD.synset AND binary SD.lang IN ('eng', 'jpn')) " +
				"	ORDER BY S.wordid,S.lexid,S.freq";

		Session session = this.sessionFactory.getCurrentSession();
		return session.createSQLQuery(sql)
				.addEntity("SD", SynsetDef.class)
				.setParameter("word", word).list();
	}


	/**
	 * getSynsetDef
	 * 類義語を得る
	 * 
	 * @param titles itemのもつtitles(自動で英単語を選択)
	 * @return 英単語のリスト構造
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<SynsetDef> getSynsetDef(String synset) {
		String sql = "SELECT DISTINCT "
			+"	* "
			+"	FROM wn_synset_def "
			+"	WHERE synset = :synset AND binary lang IN ('eng', 'jpn') "
			+"	ORDER BY sid ";//LIMIT 0, 20
		Session session = this.sessionFactory.getCurrentSession();
		return session.createSQLQuery(sql)
			.addEntity(SynsetDef.class)
			.setParameter("synset", synset).list();
		
	}
	
	/**
	 * getSynlink
	 * SynsetのSynlinkを得る
	 * 
	 * @param synset 単語
	 * @return getSynlinkのリスト構造
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Object[]> getSynlink(String synset) {
		String sql = "SELECT DISTINCT " +
				"	{SL.*}, {W.*} " +
				"	from wn_synlink AS SL " +
				"	LEFT JOIN wn_sense AS S ON SL.synset2 = S.synset " +
				"	LEFT JOIN wn_word AS W ON S.wordid = W.wordid " +
				"	WHERE SL.synset1 = :synset AND S.lang = 'eng' " +
				"	ORDER BY SL.id,W.wordid";

		Session session = this.sessionFactory.getCurrentSession();
		return session.createSQLQuery(sql)
				.addEntity("SL", Synlink.class)
				.addEntity("W", Word.class)
				.setParameter("synset", synset).list();
	}

	
	/**
	 * getSynsetByLink
	 * 単語から上位語、下位語等を得る
	 * 
	 * @param word 単語
	 * @param link (hype,hypo)
	 * @return 英単語のリスト構造
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Object[]> getSynsetByLink(String word, String link, String userid) {
		String sql = "SELECT DISTINCT "
			+"	SY.name, SY.pos, SY.src"// SY.*
			+"	, CASE WHEN item.author_id IS NULL THEN '0' ELSE '1' END AS learned"
			+"	from wn_word AS W"
			+"	LEFT JOIN wn_sense AS S USING(`wordid`)"
			+"	LEFT JOIN wn_synlink AS SL ON SL.synset1 = S.synset"
			+"	LEFT JOIN wn_synset AS SY ON SL.synset2 = SY.synset"
			+"	LEFT JOIN ("
			+"		SELECT DISTINCT author_id,en_title"
			+"		FROM t_item WHERE author_id = :author_id"
			+"	) AS item ON item.en_title = SY.name"
			+"	WHERE W.lemma = :word AND S.lexid < 10 AND S.freq < 10 AND SL.link = :link"
			+"	ORDER BY SY.id ASC"
			+"	";//LIMIT 0, 20

		Session session = this.sessionFactory.getCurrentSession();
		return session.createSQLQuery(sql)
				.setParameter("word", word)
				.setParameter("link", link)
				.setParameter("author_id", userid).list();
	}

	/**
	 * getSynsetByLink
	 * 単語から上位語、下位語等を得る
	 * 
	 * @param word 単語
	 * @param link (hype,hypo)
	 * @return 英単語のリスト構造
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Synset> getSynsetByLink(String word, String link) {
		String sql = "SELECT DISTINCT "
			+"	SY.*"
			+"	from wn_word AS W"
			+"	LEFT JOIN wn_sense AS S USING(`wordid`)"
			+"	LEFT JOIN wn_synlink AS SL ON SL.synset1 = S.synset"
			+"	LEFT JOIN wn_synset AS SY ON SL.synset2 = SY.synset"
			+"	WHERE W.lemma = :word AND S.lexid < 10 AND S.freq < 10 AND SL.link = :link"
			+"	ORDER BY SY.id ASC"
			+"	";//LIMIT 0, 20

		Session session = this.sessionFactory.getCurrentSession();
		return session.createSQLQuery(sql).addEntity(Synset.class)
				.setParameter("word", word).setParameter("link", link).list();
	}

	/**
	 * getHypenym
	 * 上位語を得る
	 * 
	 * @param word 単語
	 * @return 英単語のリスト構造
	 */
	public List<Synset> getHypenym(String word) {
		return getSynsetByLink(word, "hype");
	}


	/**
	 * getHypenym
	 * 上位語を得る
	 * 
	 * @param titles itemのもつtitles(自動で英単語を選択)
	 * @return 英単語のリスト構造
	 */
	public List<Synset> getHypenym(List<ItemTitle> titles) {
		String title = getEnTitle(titles);
		return getHypenym(title);
	}


	/**
	 * getHyponym
	 * 下位語を得る
	 * 
	 * @param word 英単語
	 * @return 英単語のリスト構造
	 */
	public List<Synset> getHyponym(String word) {
		return getSynsetByLink(word, "hypo");
	}


	/**
	 * getHyponym
	 * 上位語を得る
	 * 
	 * @param titles itemのもつtitles(自動で英単語を選択)
	 * @return 英単語のリスト構造
	 */
	@Transactional(readOnly = true)
	public List<Synset> getHyponym(List<ItemTitle> titles) {
		String title = getEnTitle(titles);
		return getHyponym(title);
	}

	
	/**
	 * getSUMO
	 * SUMO: Suggested Upper Merged Ontologyを得る（カテゴリ的な）
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Xlink> getSUMO(String word) {
		String sql = "SELECT DISTINCT "
			+" X.*"
			+"	from wn_word AS W"
			+"	LEFT JOIN wn_sense AS S USING(`wordid`)"
			+"	LEFT JOIN wn_xlink AS X USING(`synset`)"
			+"	WHERE W.lemma = :word AND W.lang = 'eng'"
			+"	ORDER BY S.synset ASC"
			+"	";//LIMIT 0, 20
		Session session = this.sessionFactory.getCurrentSession();
		return session.createSQLQuery(sql).addEntity(Xlink.class).setParameter("word", word).list();
	}
	
	/**
	 * getEnTitle
	 * itemのタイトルリストから英語のタイトルを得る、なければGoogleTranslate
	 * 
	 * @param titles itemのもつtitles(自動で英単語を選択)
	 * @return 英単語
	 */
	@Transactional(readOnly = true)
	public String getEnTitle(List<ItemTitle> titles) {
		String baseTitle = "";
		String baseLang = "";
		for (ItemTitle title : titles) {
			if ((title.getLanguage().getCode().equals("en"))) {
				return title.getContent().trim().replace(" ", "_");
			} else {
				baseTitle = title.getContent();
				baseLang = title.getLanguage().getCode();
			}
		}
		if (baseTitle.length() != 0 && baseLang.length() != 0 ) {
			return _getEntitle(baseTitle, baseLang).replace(" ", "_");
//			String word = microsoftTranslateService.translateByCode(baseTitle+"}", baseLang, "en");
//			return word.substring(0, word.length()-1).trim().replace(" ", "_");//
//			return googleTranslateService.translateByCode(baseTitle, baseLang, "en").replace(" ", "_");
		}
		return "";
	}

	/**
	 * _getEnTitle
	 * 単語を英語に翻訳＋キャッシュにする
	 * 
	 * @param word 単語
	 * @param lang 言語
	 * @return 英単語
	 */
	private String _getEntitle(String word, String lang) {
		word = word.trim();
		
		// キャッシュをチェック
		if (_translateCache.containsKey(word)) {
			return _translateCache.get(word);
		}

		// 翻訳(※ }は単数形にするため)
		String data = googleTranslateService.translateByCode(word+"}}}", lang, "en");
//		String data = googleTranslateService.translateByCode(word, lang, "en");
		data = data.replace("}", "").trim();
		_translateCache.put(word, data);
		
		return data;
	}
	

	/**
	 * getHypernymBySynset
	 * Synsetから上位語を得る
	 * 
	 * @param synset
	 * @return 英単語
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Synlink> getHypernymBySynset(String synset) {
		synset =  StringEscapeUtils.escapeSql(synset);
		String sql = "SELECT DISTINCT " +
				"	* " +
				"	from wn_synlink " +
				"	WHERE synset1 = :synset " +
				"	AND link IN ('hype','hmem','holo','hprt','hsub') " +
				"	ORDER BY synset1 ASC " +
				"	LIMIT 2";
		Session session = this.sessionFactory.getCurrentSession();
		return session.createSQLQuery(sql).addEntity(Synlink.class).setParameter("synset", synset).list();
	}
	
	

	/**
	 * getRelatedWordRAW
	 * 類似する単語のリスト(そのままの)
	 * 
	 * @param id ItemID
	 * @param col カラム名(total,word,pos,time)
	 * @param page ページ番号(0〜)
	 * @param perpage 一ページの取得数
	 * @return Itemリスト構造
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Object[]> getRelatedWordRAW(Item item, int page, int perpage) {
		String name = getEnTitle(item.getTitles());

		return this.sessionFactory.getCurrentSession()
				.createSQLQuery("CALL get_word_dist_list(:name, :page, :perpage, 5, 'word', :id)")
				.addScalar("synset").addScalar("synset0").addScalar("name").addScalar("dist")
				.setParameter("name", name)
				.setParameter("page", page)
				.setParameter("perpage", perpage)
				.setParameter("id", item.getId())
				.list();

	}
	
	/**
	 * getRelatedWord
	 * 類似する単語のリスト
	 * 
	 * @param id ItemID
	 * @param col カラム名(total,word,pos,time)
	 * @param page ページ番号(0〜)
	 * @param perpage 一ページの取得数
	 * @return Itemリスト構造
	 */
	@Transactional(readOnly = true)
	public List<Map<String, String>> getRelatedWord(Item item, int page, int perpage) {
		List<Object[]> list = getRelatedWordRAW(item, page, perpage);
		String name = getEnTitle(item.getTitles());

		List<Map<String, String>> infolist = new ArrayList<Map<String, String>>();
		for (Object[] data : list) {
			String title = (String)data[2];
			if (!name.equals(title)) {
				Map<String, String> infomap = new LinkedHashMap<String, String>();
				infomap.put("synset", (String)data[0]);
				infomap.put("synset0", (String)data[1]);
				infomap.put("name", (String)data[2]);
				infomap.put("dist", ((Byte)data[3]).toString());
				infolist.add(infomap);
			}
		}
		return infolist;
	}

	
//	
//	/**
//	 * getHypernymDistances
//	 * Synsetの上位語の距離リストを得る
//	 * 
//	 * @param synset
//	 * @param distance　距離
//	 * @return 英単語
//	 */
//	//@SuppressWarnings("unchecked")
//	private List<String> getHypernymDistances(String synset, Integer distance) {
//		List<String> distances = new ArrayList<String>();
//		//distances.set(distance, synset);
//		//distances.add(distance, synset);
//		distances.add(synset);
//		String hypernym  = getHypernymBySynset(synset);
//		
//		if (!hypernym.equals("")) {
//			distances.addAll(getHypernymDistances(hypernym, distance+1));
//		}
//		return distances;
//	}
	/**
	 * getHypernymDistances
	 * Synsetの上位語の距離リストを得る
	 * 
	 * @param synset
	 * @param distance　距離
	 * @return Synsetと距離のマップ(String,Integer)
	 */
	//@SuppressWarnings("unchecked")
	private LinkedHashMap<String,Integer> getHypernymDistances(String synset, Integer distance) {
		LinkedHashMap<String,Integer> distances = new LinkedHashMap<String,Integer>();
		distances.put(synset, distance);
		
		System.out.print("["+distance+"]"+synset);
		
		// 多い時は終わる
		if (distance > 15) {
			return distances; 
		}
		
		// Hypernymのリスト
		List<Synlink> hypernym  = getHypernymBySynset(synset);
		
		// Hypenymの距離取得
		for (Synlink synlink : hypernym) {
			distances.putAll(getHypernymDistances(synlink.getSynset2(), distance+1));
		}
		return distances;
	}

	
	
	/**
	 * getSynsetDistance
	 * Synset同士の距離を得る
	 * 
	 * @param synset1 
	 * @param synset1 
	 * @return 距離
	 */
	public Integer getSynsetDistance(String synset1, String synset2) {
		if (synset1.equals(synset2)) {
			return 0;
		}
		// 距離
		Integer path_distance = -1;
		LinkedHashMap<String,Integer> dist_list1 = getHypernymDistances(synset1, 0);
		System.out.println();
		LinkedHashMap<String,Integer> dist_list2 = getHypernymDistances(synset2, 0);
		System.out.println();
		
		for( String syn1 : dist_list1.keySet() ){
			Integer dist1 = dist_list1.get(syn1);
			if (dist_list2.containsKey(syn1)) {// 含んでいるか
				path_distance = dist1 + dist_list2.get(syn1);
				break;
			}
		}
//		List<String> dist_list1 = getHypernymDistances(synset1, 0);
//		List<String> dist_list2 = getHypernymDistances(synset2, 0);
//		
//		for( String syn1 : dist_list1 ){
//			Integer index1 = dist_list1.indexOf(syn1);
//			Integer index2 = dist_list2.indexOf(syn1);
//			if (index2 != -1) {
//				path_distance = index1 + index2 - 1;// 差
//			}
//		}
		
		return path_distance;
	}

	
	/**
	 * getSynsetSimilarity
	 * Synset同士の類似度を得る
	 * 
	 * @param synset1 
	 * @param synset1 
	 * @return 類似度
	 */
	public Double getSynsetSimilarity(String synset1, String synset2) {
		Double dist = (double)getSynsetDistance(synset1, synset2);
		if (dist != -1) {
			return 1. / (1. + Math.pow(dist,2)/10.);
		}
		return -1.;
	}

	/**
	 * getNounSynset
	 * 単語のSynset(名詞)の一覧を得る
	 * 
	 * @param word
	 * @return 類似度
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	private List<Synlink> getNounSynset(String word) {
		String sql = "SELECT DISTINCT " +
				"	SL.* " +
				"	from wn_word AS w " +
				"	LEFT JOIN wn_sense AS s USING(`wordid`) " +
				"	LEFT JOIN wn_sense AS s2 USING(`synset`) " +
				"	LEFT JOIN wn_synlink AS SL ON s2.synset = SL.synset1 " +
				"	WHERE w.lemma = :word AND s.synset LIKE '%n' " +
				"	AND SL.link IN ('hype','hmem','holo','hprt','hsub') " +
				"	GROUP BY s2.synset,SL.synset2 " +
				"	ORDER BY s2.id,s2.lexid ASC " +
				"	LIMIT 5";
		Session session = this.sessionFactory.getCurrentSession();
		return session.createSQLQuery(sql).addEntity(Synlink.class).setParameter("word", word).list();
	}
	
	
	/**
	 * getWordSimilarity
	 * 単語同士の類似度を得る
	 * 
	 * @param word1
	 * @param word2
	 * @return 類似度(word1とword2のSynsetの中で一番大きいやつ)
	 */
	public Double getWordSimilarity(String word1, String word2) {
		if (word1.equals(word2)) {
			return 1.;
		}
		List<Synlink> list1 = getNounSynset(word1);
		List<Synlink> list2 = getNounSynset(word2);

		Double similarity = 0.;
		for( Synlink syn1 : list1 ){
			for( Synlink syn2 : list2 ){
				Double tmp = getSynsetSimilarity(syn1.getSynset1(), syn2.getSynset1());// 類似度を得る
				if (similarity < tmp) {
					similarity = tmp;
				}
				if (similarity == 1.) {
					return similarity;
				}
			}
		}
		
		if (similarity <= 0.) {
			return -1.;
		} else {
			return similarity;
		}
	}
	
	
	
	
	
}







