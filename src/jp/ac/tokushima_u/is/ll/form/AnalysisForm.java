package jp.ac.tokushima_u.is.ll.form;

import java.io.Serializable;
import java.util.List;

public class AnalysisForm implements Serializable{

	private String analysisalgo;
	private String degree;
	private String egofiltername;
	private String egofilterdistance;
	private String layouttype;
	private List<String> nationality;
	private List<String> place;
	private List<String> age;
	private List<String> gender;
	private List<String> knowledge;

	public String getAnalysisalgo() {
		return analysisalgo;
	}

	public void setAnalysisalgo(String analysisalgo) {
		this.analysisalgo = analysisalgo;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getEgofiltername() {
		return egofiltername;
	}

	public void setEgofiltername(String egofiltername) {
		this.egofiltername = egofiltername;
	}

	public String getEgofilterdistance() {
		return egofilterdistance;
	}

	public void setEgofilterdistance(String egofilterdistance) {
		this.egofilterdistance = egofilterdistance;
	}

	public List<String> getNationality() {
		return nationality;
	}

	public void setNationality(List<String> nationality) {
		this.nationality = nationality;
	}

	public List<String> getPlace() {
		return place;
	}

	public void setPlace(List<String> place) {
		this.place = place;
	}

	public List<String> getAge() {
		return age;
	}

	public void setAge(List<String> age) {
		this.age = age;
	}

	public List<String> getGender() {
		return gender;
	}

	public void setGender(List<String> gender) {
		this.gender = gender;
	}

	public List<String> getKnowledge() {
		return knowledge;
	}

	public void setKnowledge(List<String> knowledge) {
		this.knowledge = knowledge;
	}

	public String getLayouttype() {
		return layouttype;
	}

	public void setLayouttype(String layouttype) {
		this.layouttype = layouttype;
	}
}
