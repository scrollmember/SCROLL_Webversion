package jp.ac.tokushima_u.is.ll.entity.pacall;

import java.io.Serializable;

public class Ptype implements Serializable {
	private static final long serialVersionUID = -6376027549302446417L;
	private String id;
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
