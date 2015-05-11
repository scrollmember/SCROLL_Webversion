package jp.ac.tokushima_u.is.ll.entity;

import java.io.Serializable;
import java.util.Date;

public class Cooccurrence implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4092067650878945199L;

	private String id;
	private String target_content;
	private String next_content;
	private String target_itemid;
	private String next_itemid;
	private String language;
	private Date create_time;
	private String target_authorid;
	private String next_authorid;
	
	private String content;
	private String item;
	private String itemid;
	private Date itemcreate;
	private String authorid;
	
	private String label;
	private double eccentricity;
	private double closeness;
	private String degree;
	private double betweenness;
	private int ranking;
	
	private String r_betweenness;
	private String r_clossness;
	private String r_degree;
	
	
	private String knowledgeranking;
	private String knowledgnumber;
	
	private String nickname;
	private String user_level;
	
	private String degreein;
	private String degreeout;
	
	private String placeId;
	private String place;
	
	private String target_place;
	private String next_place;
	private String target_place_id;
	private String target_place_itemid;
	private String next_place_id;
	private String next_place_itemid;
	private String lat;
	private String lng;
	
	private String itemtime;
	private double new_algorithm;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTarget_content() {
		return target_content;
	}

	public void setTarget_content(String target_content) {
		this.target_content = target_content;
	}

	public String getNext_content() {
		return next_content;
	}

	public void setNext_content(String next_content) {
		this.next_content = next_content;
	}

	public String getTarget_itemid() {
		return target_itemid;
	}

	public void setTarget_itemid(String target_itemid) {
		this.target_itemid = target_itemid;
	}

	public String getNext_itemid() {
		return next_itemid;
	}

	public void setNext_itemid(String next_itemid) {
		this.next_itemid = next_itemid;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getTarget_authorid() {
		return target_authorid;
	}

	public void setTarget_authorid(String target_authorid) {
		this.target_authorid = target_authorid;
	}

	public String getNext_authorid() {
		return next_authorid;
	}

	public void setNext_authorid(String next_authorid) {
		this.next_authorid = next_authorid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getItemid() {
		return itemid;
	}

	public void setItemid(String itemid) {
		this.itemid = itemid;
	}

	public Date getItemcreate() {
		return itemcreate;
	}

	public void setItemcreate(Date itemcreate) {
		this.itemcreate = itemcreate;
	}

	public String getAuthorid() {
		return authorid;
	}

	public void setAuthorid(String authorid) {
		this.authorid = authorid;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public double getEccentricity() {
		return eccentricity;
	}

	public void setEccentricity(double eccentricity) {
		this.eccentricity = eccentricity;
	}

	public double getCloseness() {
		return closeness;
	}

	public void setCloseness(double closeness) {
		this.closeness = closeness;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String string) {
		this.degree = string;
	}

	public int getRanking() {
		return ranking;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

	public String getKnowledgeranking() {
		return knowledgeranking;
	}

	public void setKnowledgeranking(String knowledgeranking) {
		this.knowledgeranking = knowledgeranking;
	}

	public String getKnowledgnumber() {
		return knowledgnumber;
	}

	public void setKnowledgnumber(String knowledgnumber) {
		this.knowledgnumber = knowledgnumber;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUser_level() {
		return user_level;
	}

	public void setUser_level(String user_level) {
		this.user_level = user_level;
	}

	public String getDegreein() {
		return degreein;
	}

	public void setDegreein(String degreein) {
		this.degreein = degreein;
	}

	public String getDegreeout() {
		return degreeout;
	}

	public void setDegreeout(String degreeout) {
		this.degreeout = degreeout;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getTarget_place() {
		return target_place;
	}

	public void setTarget_place(String target_place) {
		this.target_place = target_place;
	}

	public String getNext_place() {
		return next_place;
	}

	public void setNext_place(String next_place) {
		this.next_place = next_place;
	}

	public String getTarget_place_id() {
		return target_place_id;
	}

	public void setTarget_place_id(String target_place_id) {
		this.target_place_id = target_place_id;
	}

	public String getTarget_place_itemid() {
		return target_place_itemid;
	}

	public void setTarget_place_itemid(String target_place_itemid) {
		this.target_place_itemid = target_place_itemid;
	}

	public String getNext_place_id() {
		return next_place_id;
	}

	public void setNext_place_id(String next_place_id) {
		this.next_place_id = next_place_id;
	}

	public String getNext_place_itemid() {
		return next_place_itemid;
	}

	public void setNext_place_itemid(String next_place_itemid) {
		this.next_place_itemid = next_place_itemid;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getItemtime() {
		return itemtime;
	}

	public void setItemtime(String itemtime) {
		this.itemtime = itemtime;
	}

	public double getBetweenness() {
		return betweenness;
	}

	public void setBetweenness(double betweenness) {
		this.betweenness = betweenness;
	}

	public String getR_betweenness() {
		return r_betweenness;
	}

	public void setR_betweenness(String r_betweenness) {
		this.r_betweenness = r_betweenness;
	}

	public String getR_clossness() {
		return r_clossness;
	}

	public void setR_clossness(String r_clossness) {
		this.r_clossness = r_clossness;
	}

	public String getR_degree() {
		return r_degree;
	}

	public void setR_degree(String r_degree) {
		this.r_degree = r_degree;
	}

	public double getNew_algorithm() {
		return new_algorithm;
	}

	public void setNew_algorithm(double new_algorithm) {
		this.new_algorithm = new_algorithm;
	}

	
}
