package com.qq183311108.frimware.service;

import com.qq183311108.biz.entity.FirmwareVersion;
import com.qq183311108.common.dto.BaseApiReqDto;
import com.qq183311108.common.dto.FirmwareUpdateLogDto;
import com.qq183311108.common.dto.GetNewVerDto;
import com.qq183311108.common.exception.IErrCode;

public interface IFrimwareProtocolService {

	public enum FrimwareProtocolServiceErrCode implements IErrCode{
		FrimwareIsNotExist("FrimwareIsNotExist","此固件不存在。序列号，设备类型，物理网卡地址必须完全匹配"),
		FrimwareIsNotEnableUpdate("FrimwareIsNotEnableUpdate","此固件不允许升级"),
		FrimwareTypeVersionIsNotExist("FrimwareNewVersionIsNotExist","不支持此固件的类型，没有对应的版本"),
		FrimwareNewVersionIsNotExist("FrimwareNewVersionIsNotExist","此固件已经是最新版本，不需要升级"),
		;

		private String errCode;
		private String errMsg;

	    private FrimwareProtocolServiceErrCode(String errCode, String errMsg) {
	        this.setErrCode(errCode);
	        this.setErrMsg(errMsg);
	    }

	    @Override
	    public String getErrCode() {
			return this.getClass().getSimpleName()+"."+errCode;
		}

	    @Override
		public void setErrCode(String errCode) {
			this.errCode = errCode;
		}

	    @Override
		public String getErrMsg() {
			return errMsg;
		}

	    @Override
		public void setErrMsg(String errMsg) {
			this.errMsg = errMsg;
		}
		
	}
	
	/**
	 * 获取终端版本信息
	 * @param apiReqDto
	 * @return
	 */
	FirmwareVersion getFirmwareNewVersion(GetNewVerDto getNewVerDto);
	
	/**
	 * 更新终端升级日志
	 * @param apiReqDto
	 * @return
	 */
	void saveFirmwareUpdateLog(FirmwareUpdateLogDto firmwareUpdateLogDto);	
}
