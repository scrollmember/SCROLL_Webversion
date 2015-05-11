package jp.ac.tokushima_u.is.ll.dao;

import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.entity.wordnet.Sense;
import jp.ac.tokushima_u.is.ll.entity.wordnet.Synlink;
import jp.ac.tokushima_u.is.ll.entity.wordnet.Synset;
import jp.ac.tokushima_u.is.ll.entity.wordnet.SynsetDef;
import jp.ac.tokushima_u.is.ll.entity.wordnet.Word;
import jp.ac.tokushima_u.is.ll.entity.wordnet.Xlink;

import org.apache.ibatis.annotations.Param;

public interface WordNetDao {

	List<Map<String, Object>> findListNearSynonymAndLearningFlag(@Param("word")String word, @Param("userId")String userid);

	List<Word> findListNearSynonym(String word);

	List<Word> findListNearSynonymBySynset(String synset);

	List<Sense> findListSenseListByWord(String word);

	List<SynsetDef> findListSynsetDefByWord(String word);

	List<SynsetDef> findListSynsetDefBySynset(String synset);

	List<Map<String, Object>> findSynlinkAndWordBySynset(String synset);

	List<Map<String, Object>> findListSynsetByLink(@Param("word")String word, @Param("link")String link,
			@Param("userId") String userId);

	List<Synset> findSynsetByLink(@Param("word")String word, @Param("link")String link);

	List<Xlink> findListXlinkSUMOByWord(String word);

	List<Synlink> findListSynlinkHypernymBySynset(String synset);

	List<Synlink> findListSynlinkNounSynsetByWord(String word);

	/**
	 * @param name
	 * @param page
	 * @param perpage
	 * @param deep
	 * @param id
	 * @return List<Map> keySet{synset, synset0, name, dist}
	 */
	List<Map<String, Object>> findListRelatedItemRAWInWordMode(@Param("name")String name,
			@Param("page")int page, @Param("perpage")int perpage, @Param("deep")int deep, @Param("itemId")String id);

}
