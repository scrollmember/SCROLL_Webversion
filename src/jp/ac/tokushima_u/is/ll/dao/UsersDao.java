package jp.ac.tokushima_u.is.ll.dao;

import java.util.Date;
import java.util.List;

import jp.ac.tokushima_u.is.ll.entity.Author;
import jp.ac.tokushima_u.is.ll.entity.Groupmember;
import jp.ac.tokushima_u.is.ll.entity.Kasetting;
import jp.ac.tokushima_u.is.ll.entity.KnowledgeRanking;
import jp.ac.tokushima_u.is.ll.entity.PlaceAnalysis;
import jp.ac.tokushima_u.is.ll.entity.Profile;
import jp.ac.tokushima_u.is.ll.entity.TDAfirstlayer;
import jp.ac.tokushima_u.is.ll.entity.Users;
import jp.ac.tokushima_u.is.ll.entity.Usertest;
import jp.ac.tokushima_u.is.ll.form.ProfileEditForm;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.PageRequest;

public interface UsersDao {

	void insert(Users user);
	int update(Users user);
	
	Long count();
	Usertest findById(@Param("id") String id);
	Users findByEmail(String email);
	Users findByActiveCode(String activecode);
	List<Users> findAll();
	List<Users> findPage(@Param("user") Users user, @Param("pageRequest") PageRequest pageRequest);
	Long countPage(Users user);
	List<Users> searchList(Users user);
	List<Users> findUsersHaveItemsFrom(Date startDate);
	//Network analysis
	List<Users> findByNickname();
	String check(@Param("id")String id);
	void set(@Param("id")String generateIdUUID, @Param("author") String id, @Param("layout") String yifan,@Param("viewdistance")  int viewdistance,
			@Param("kaquality") int kaquality);
	void kaupdate(@Param("id")String generateIdUUID, @Param("author") String id, @Param("layout") String yifan,@Param("viewdistance")  int viewdistance,
			@Param("kaquality") int kaquality);
	Kasetting findka(@Param("id")String id);
	void creategroup(@Param("id")String generateIdUUID, @Param("author")String id,@Param("groupname") String groupname,
			@Param("privacy")String privacy);
	List<Groupmember> findgroupmember(@Param("yourid")String groupname);
	void addmember(@Param("id")String generateIdUUID, @Param("groupname")String groupname, @Param("yourid")String yourid,
			@Param("groupid")String id);
	List<Groupmember> getjoingroup(@Param("id")String id);
	List<Groupmember> getallgroup(@Param("id")String id);
	void memberdelete(@Param("author")String groupid, @Param("other")String id);
	List<Author> getauthor(@Param("author")String groupid);
	List<Author> getallauthor();
	List<KnowledgeRanking> getknowledgeranking();
	List<PlaceAnalysis> findbymayplace(@Param("author")String id);
	
	List<Profile> checkuser(@Param("check")String id);
	void insertability(Profile form);
	void updateability(Profile p);
	Profile findability(@Param("userid")String id);
	List<TDAfirstlayer> findtdafirstlayer();
	
}
