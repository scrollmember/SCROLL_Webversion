package jp.ac.tokushima_u.is.ll.entity;

import java.sql.Time;

import jp.ac.tokushima_u.is.ll.util.Utility;

public class TimeNode {
	private Time time;
	private TimeNode left;
	private TimeNode right;
	
	public TimeNode(Time t){
		this.time = t;
	}
	
	public int getLength() {
		return Utility.getSeconds(this.time);
	}
	
	public Time getTime() {
		return time;
	}
	public void setTime(Time time) {
		this.time = time;
	}


	public TimeNode getLeft() {
		return left;
	}


	public void setLeft(TimeNode left) {
		this.left = left;
	}


	public TimeNode getRight() {
		return right;
	}


	public void setRight(TimeNode right) {
		this.right = right;
	}

	
}
