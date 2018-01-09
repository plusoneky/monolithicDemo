package com.qq1833111108.common.dto;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseDto implements Serializable,Cloneable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(BaseDto.class);
	
	@Override
	public String toString(){
		String returnValue = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			returnValue = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			logger.error("",e);
		}
		return returnValue;
	}

	
	/**
	 * 克隆实例
	 * @return
	 * @throws CloneNotSupportedException
	 */
	public Object cloneThis() throws CloneNotSupportedException {
		return super.clone();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
