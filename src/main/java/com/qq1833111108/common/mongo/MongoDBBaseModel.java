package com.qq1833111108.common.mongo;

import java.io.Serializable;

public class MongoDBBaseModel implements Serializable{
	
	private static final long serialVersionUID = 1L;

    private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
    
}
