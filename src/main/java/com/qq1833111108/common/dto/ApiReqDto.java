package com.qq1833111108.common.dto;

import java.util.Map;

import io.swagger.annotations.ApiModel;

/**
 * @author 04
 * API接口请求参数
 */
@ApiModel
public class ApiReqDto<T,M> extends BaseApiReqDto{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 请求业务数据对象放入Map中，适合有多个对象但又不想继承此类，创建单独的请求对象时使用
	 */
	private Map<String,T> reqBizMap;
	
	/**
	 * 请求业务数据对象，如果只有一个参数对象，推荐用这种方式
	 */	
	private M reqBizObj;
	
	
	public M getReqBizObj() {
		return reqBizObj;
	}
	public void setReqBizObj(M reqBizObj) {
		this.reqBizObj = reqBizObj;
	}


	public Map<String, T> getReqBizMap() {
		return reqBizMap;
	}

	public void setReqBizMap(Map<String, T> reqBizMap) {
		this.reqBizMap = reqBizMap;
	}


	
	
}
