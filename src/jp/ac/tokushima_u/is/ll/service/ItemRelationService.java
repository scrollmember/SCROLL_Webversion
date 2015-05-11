package jp.ac.tokushima_u.is.ll.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.common.orm.hibernate.HibernateDao;
import jp.ac.tokushima_u.is.ll.entity.FileData;
import jp.ac.tokushima_u.is.ll.entity.ImageHistogram;
import jp.ac.tokushima_u.is.ll.entity.Item;
import jp.ac.tokushima_u.is.ll.util.ImageSimilarity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author maou
 */
@Service
@Transactional
public class ItemRelationService {

	@Autowired
	private WordNetService wordnetService;
    
	// 画像のあるディレクトリ(URL)
	@Value("${system.staticserverImageUrl}")
	private String staticserverImageUrl;
	
	private HibernateDao<ImageHistogram, String> imageRelationDao;

	private SessionFactory sessionFactory;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		imageRelationDao = new HibernateDao<ImageHistogram, String>(sessionFactory, ImageHistogram.class);
	}
	
	
	/************************************************************************
	 * 
	 * アイテムリスト取得
	 * 
	 ************************************************************************/


	/**
	 * getItemRelationRAW
	 * 類似するアイテムのリスト(そのままの)
	 * 
	 * @param id ItemID
	 * @param col カラム名(total,word,pos,time)
	 * @param page ページ番号(0〜)
	 * @param perpage 一ページの取得数
	 * @return Itemリスト構造
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Object[]> getRelatedItemRAW(Item item, int page, int perpage) {
		String name = wordnetService.getEnTitle(item.getTitles());

		return this.sessionFactory.getCurrentSession()
				.createSQLQuery("CALL get_word_dist_list(:name, :page, :perpage, 5, '', :id)")
				.addEntity(Item.class).addScalar("synset").addScalar("synset0").addScalar("dist")
				.setParameter("name", name)
				.setParameter("page", page)
				.setParameter("perpage", perpage)
				.setParameter("id", item.getId())
				.list();

	}
	
	/**
	 * getItemRelation
	 * 類似するアイテムのリスト
	 * 
	 * @param id ItemID
	 * @param col カラム名(total,word,pos,time)
	 * @param page ページ番号(0〜)
	 * @param perpage 一ページの取得数
	 * @return Itemリスト構造
	 */
	@Transactional(readOnly = true)
	public Map<String, List<Object>> getRelatedItem(Item item, int page, int perpage) {
		List<Object[]> list = getRelatedItemRAW(item, page, perpage);

		Map<String, List<Object>> dataset = new LinkedHashMap<String, List<Object>>();
		List<Object> itemlist = new ArrayList<Object>();
		List<Object> infolist = new ArrayList<Object>();
		for (Object[] data : list) {
			itemlist.add((Item)data[0]);
			Map<String, String> infomap = new LinkedHashMap<String, String>();
			infomap.put("synset", (String)data[1]);
			infomap.put("synset0", (String)data[2]);
			infomap.put("dist", ((Byte)data[3]).toString());
			infolist.add(infomap);
		}
		dataset.put("Item", itemlist);
		dataset.put("Info", infolist);
		return dataset;
	}



	/**
	 * getSimilarTime
	 * 登録時間の近いアイテムと距離のリスト取得
	 * 
	 * @param id ItemID
	 * @param page ページ番号(0〜)
	 * @param perpage 一ページの取得数
	 * @return Map<String, List<Object>>
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Object[]> getSimilarTimeRAW(String id, int page, int perpage) {
		Session session = this.sessionFactory.getCurrentSession();

		// 実行するSQL
		String sql = 
			"SELECT " +
			"	I.*, abs(unix_timestamp(concat('1970-01-01 ',substr(I.create_time,12,8)))-T.mtime) as dist " +
			"	FROM (" +
			"		SELECT " +
			"			id, unix_timestamp(concat('1970-01-01 ',substr(create_time,12,8))) AS mtime," +
			"			(substr(create_time,12,2)+23)%24 AS h1,(substr(create_time,12,2)) AS h2,(substr(create_time,12,2)+1 )%24 AS h3 " +
			"			FROM t_item WHERE id = :id " +
			"		) AS T " +
			"		INNER JOIN t_item AS I ON (substr(I.create_time,12,2) IN (T.h1, T.h2, T.h3) ) " +
			"		AND T.id != I.id " +
			"		ORDER BY dist ASC";
		
		return session.createSQLQuery(sql)
			.addEntity(Item.class).addScalar("dist")
			.setFirstResult(page).setMaxResults(perpage)
			.setParameter("id", id).list();
	}


	/**
	 * getSimilarTime
	 * 登録時間の近いアイテムと距離のリスト取得
	 * 
	 * @param id ItemID
	 * @param page ページ番号(0〜)
	 * @param perpage 一ページの取得数
	 * @return Map<String, List<Object>>
	 */
	@Transactional(readOnly = true)
	public Map<String, List<Object>> getSimilarTime(String id, int page, int perpage) {
		List<Object[]> list = getSimilarTimeRAW(id, page, perpage);
		
		Map<String, List<Object>> dataset = new LinkedHashMap<String, List<Object>>();
		List<Object> itemlist = new ArrayList<Object>();
		List<Object> infolist = new ArrayList<Object>();
		for (Object[] data : list) {
			itemlist.add((Item)data[0]);
			Map<String, String> infomap = new LinkedHashMap<String, String>();
			infomap.put("dist", ((BigInteger)data[1]).toString());
			infolist.add(infomap);
		}
		dataset.put("Item", itemlist);
		dataset.put("Info", infolist);
		
		return dataset;
	}

	/**
	 * getSimilarImageByItersec
	 * HistogramIntersectionを使った類似画像をもつアイテムと距離のリスト取得
	 * 
	 * @param id ItemID
	 * @param page ページ番号(0〜)
	 * @param perpage 一ページの取得数
	 * @return Map<String, List<Object>>
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Object[]> getSimilarImageByItersecRAW(String id, int page, int perpage) {
		Session session = this.sessionFactory.getCurrentSession();

		// 実行するSQL
		String sql = 
			"	SELECT {IT.*}, ID.sum " +
			"	FROM (" +
			"		SELECT" +
			"			IH2.id, " +
			"			SUM(CASE WHEN IH1.data>IH2.data THEN IH2.data ELSE IH1.data END) AS sum " +
			"		FROM 		t_image_histogram AS IH1 " +
			"		INNER JOIN 	t_image_histogram AS IH2 " +
			"			ON (IH2.hist = IH1.hist AND IH2.hist != -1) " +
			"		WHERE IH1.id = :id AND IH2.id != IH1.id " +
			"		GROUP BY IH2.id " +
			"		ORDER BY sum DESC " +
			"		LIMIT " + (page*perpage) + ", " + perpage +
			"	) AS ID " +
			"	LEFT JOIN t_item AS IT ON (IT.id = ID.id)";// 降順
		
		return session.createSQLQuery(sql)
			.addEntity("IT", Item.class).addScalar("sum")
			.setParameter("id", id).list();
	}
	

	/**
	 * getSimilarImageByItersec
	 * HistogramIntersectionを使った類似画像をもつアイテムと距離のリスト取得
	 * 
	 * @param id ItemID
	 * @param page ページ番号(0〜)
	 * @param perpage 一ページの取得数
	 * @return Map<String, List<Object>>
	 */
	@Transactional(readOnly = true)
	public Map<String, List<Object>> getSimilarImageByItersec(String id, int page, int perpage) {
		List<Object[]> list = getSimilarImageByItersecRAW(id, page, perpage);
		
		Map<String, List<Object>> dataset = new LinkedHashMap<String, List<Object>>();
		List<Object> itemlist = new ArrayList<Object>();
		List<Object> infolist = new ArrayList<Object>();
		for (Object[] data : list) {
			itemlist.add((Item)data[0]);
			Map<String, String> infomap = new LinkedHashMap<String, String>();
			infomap.put("sim", "0."+((BigDecimal)data[1]).toString());
			infolist.add(infomap);
		}
		dataset.put("Item", itemlist);
		dataset.put("Info", infolist);
		
		return dataset;
	}
	
	
//	※データベースの構造が違うから使えない
//
//
//	/**
//	 * getSimilarImageByCorrel
//	 * 相関係数を使った類似画像をもつアイテムのリスト取得
//	 * 
//	 * @param id ItemID
//	 * @param page ページ番号(0〜)
//	 * @param perpage 一ページの取得数
//	 * @return List<Item>
//	 */
//	@SuppressWarnings("unchecked")
//	@Transactional(readOnly = true)
//	public List<Object[]> getSimilarImageByCorrel(String id, int page, int perpage) {
//		Session session = this.sessionFactory.getCurrentSession();
//
//		// 実行するSQL
//		String sql = 
//				// 類似度計算してItemをとってくる
//				"	SELECT DISTINCT " +
//				"		{IT.*}, (C.sum / (IH1.data*IH2.data)) AS correl " +// Item, 計算結果
//				"	FROM (" +
//						// 分子の計算(サブクエリとってくる)
//				"		SELECT DISTINCT " +
//				"			F_IH.id AS id1, F_IH2.id AS id2, " +
//				"			SUM((F_IH.data-F_AVG1.data) * (F_IH2.data-F_AVG2.data)) AS sum " +
//				"		FROM t_image_histogram AS F_IH " +
//				"		LEFT JOIN t_image_histogram AS F_IH2  ON (F_IH.type = F_IH2.type) " +// hist[0-63]計算用の結合
//				"		LEFT JOIN t_image_histogram AS F_AVG1 ON (F_IH.id = F_AVG1.id) " +// ヒストグラムの平均値用の結合
//				"		LEFT JOIN t_image_histogram AS F_AVG2 ON (F_IH.id = F_AVG2.id) " +// ヒストグラムの平均値用の結合
//				"		WHERE F_IH.id = :id AND F_IH.type LIKE 'hist%' AND F_IH2.id != F_IH.id " +// ID
//				"		AND F_AVG1.type = 'avg' AND F_AVG2.type = 'avg' " +// ヒストグラムの平均値を指定
//				"		GROUP BY F_IH2.id" +// SUM用
//				"	) AS C " +
//				"	LEFT JOIN t_image_histogram AS IH1 ON IH1.id = C.id1 " +// 分母用の結合
//				"	LEFT JOIN t_image_histogram AS IH2 ON IH2.id = C.id2 " +// 分母用の結合
//				"	LEFT JOIN t_item AS IT ON IT.id = C.id2 " +// Itemを結合
//				"	WHERE IH1.type = 'correl' AND IH2.type = 'correl' " +// Correlの分母計算中(Type)を指定
//				"	AND IT.disabled != 1 " +
//				"	GROUP BY correl " +// 同じ結果のやつはまとめる(≒画像が一緒)
//				"	ORDER BY correl DESC";// 降順
//		
//		return session.createSQLQuery(sql)
//			.addEntity("IT", Item.class)
//			.setFirstResult(page).setMaxResults(perpage)
//			.setParameter("id", id).list();
//	}

	
	/************************************************************************
	 * 
	 * 関連性のデータベース更新
	 * 
	 ************************************************************************/
	
	/**
	 * updateItemRelation
	 * 画像のヒストグラムデータベースを更新
	 */
	@SuppressWarnings("unchecked")
	public void updateImageHistogram() {
		
		// 未登録を抽出
		String sql = "SELECT DISTINCT " +
				"	{I.*} " +
				"	FROM t_item AS I " +
				"	LEFT JOIN t_image_histogram AS IH ON I.id = IH.id " +
				"	WHERE I.image is NOT NULL AND IH.id is NULL " +
				"	ORDER BY I.create_time DESC " +
				"	LIMIT 10";
		Session session = this.sessionFactory.getCurrentSession();
		List<Item> itemList = session.createSQLQuery(sql)
			.addEntity("I", Item.class).list();
		
		
        for (Item item : itemList) {
        	String id = item.getId();
            FileData fd = item.getImage();
            if (fd != null && fd.getFileType() != null && fd.getFileType().equals("image")) {
            	String url = staticserverImageUrl + fd.getId() + "_320x240.png";
            	System.out.println(url);
            	
            	ImageSimilarity is = new ImageSimilarity();
            	int[] hist = is.getHistogram(url);
            	if (hist != null) {
            		for (int i=0; i<64; i++) {
                    	imageRelationDao.save(new ImageHistogram(id, i, hist[i]));// 0-63のヒストグラム値
            		}
                	continue;
            	}
            }
            // 画像がとれない、チェック済みフラグ
        	imageRelationDao.save(new ImageHistogram(id, -1, 0));
        }
		
	}
	
	
	
}




