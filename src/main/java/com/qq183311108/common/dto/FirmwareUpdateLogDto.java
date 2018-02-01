package com.qq183311108.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class FirmwareUpdateLogDto extends GetNewVerDto{
	/**
	 * 更新结果  1成功 
	 */	
	@ApiModelProperty(value = "更新结果 1成功 0失败", required = true, example = "1") 
	private int updateReport;

	public int getUpdateReport() {
		return updateReport;
	}

	public void setUpdateReport(int updateReport) {
		this.updateReport = updateReport;
	}
	
	
}
