package com.qq183311108.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class GetNewVerDto extends BaseApiReqDto{
	/**
	 * 设备当前版本
	 */
	@ApiModelProperty(value = "设备当前版本", required = true, example = "1.2") 
	private String currVer;
	
	public String getCurrVer() {
		return currVer;
	}

	public void setCurrVer(String currVer) {
		this.currVer = currVer;
	}		
	
}
