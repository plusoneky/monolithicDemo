package com.qq183311108.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

@ApiModel
public class BaseApiReqDto extends BaseDto{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 设备类型
	 */
	@ApiModelProperty(value = "设备类型", required = true, example = "no_1") 	
	private String typeName;
	
	/**
	 * 设备序列号
	 */
	@ApiModelProperty(value = "设备序列号", required = true, example = "sn_2") 
	private String sn;
	
	/**
	 * 设备mac地址
	 */
	@ApiModelProperty(value = "设备mac地址", required = true, example = "mac_2") 
	private String mac;

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	
}
