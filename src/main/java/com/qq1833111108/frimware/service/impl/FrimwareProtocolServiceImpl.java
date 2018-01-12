package com.qq1833111108.frimware.service.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.qq1833111108.biz.entity.FirmwareVersion;
import com.qq1833111108.biz.entity.FrimwareUpdateLog;
import com.qq1833111108.biz.service.IFirmwareVersionService;
import com.qq1833111108.biz.service.IFrimwareUpdateLogService;
import com.qq1833111108.common.dto.BaseApiReqDto;
import com.qq1833111108.common.dto.FirmwareUpdateLogDto;
import com.qq1833111108.common.dto.GetNewVerDto;
import com.qq1833111108.common.exception.MyException;
import com.qq1833111108.common.exception.MyException.CommErrCode;
import com.qq1833111108.frimware.service.IFrimwareProtocolService;

@Service
public class FrimwareProtocolServiceImpl implements IFrimwareProtocolService{

	private static final Logger logger = LoggerFactory.getLogger(FrimwareProtocolServiceImpl.class);
	
    @Autowired
    private IFirmwareVersionService firmwareVersionServiceImpl;
    
    @Autowired
    private IFrimwareUpdateLogService frimwareUpdateLogServiceImpl;
    
	@Override
	public FirmwareVersion getFirmwareNewVersion(GetNewVerDto getNewVerDto) {
		// 验证固件是否存在
		FrimwareUpdateLog frimwareUpdateLog = getFirmwareUpdateLog(getNewVerDto);
    	if(frimwareUpdateLog==null){
    		throw new MyException(FrimwareProtocolServiceErrCode.FrimwareIsNotExist);
    	}		
    	
    	// 验证固件上送的当前版本与服务器记录的固件当前版本号是否一致，不抛出异常，仅记录日志，允许这样的设备升级
    	if(!getNewVerDto.getCurrVer().equalsIgnoreCase(frimwareUpdateLog.getVer())){
    		logger.error("frimwareUpdateLog's currVer may be wrong! terminal's currVer="+getNewVerDto.getCurrVer()+" and mysql's currVer="+frimwareUpdateLog.getVer());
    	}
		
		// 验证固件是否允许升级
		if("no".equalsIgnoreCase(frimwareUpdateLog.getFlag())){
			throw new MyException(FrimwareProtocolServiceErrCode.FrimwareIsNotEnableUpdate);
		}
		// 验证固件新版本是否存在
		FirmwareVersion firmwareVersion = getFirmwareVersion(frimwareUpdateLog.getTypeName());
		if(getNewVerDto.getCurrVer().equals(firmwareVersion.getNewVer())){
			throw new MyException(FrimwareProtocolServiceErrCode.FrimwareNewVersionIsNotExist);
		}
		// 记录固件更新日志
    	FrimwareUpdateLog log = new FrimwareUpdateLog();
    	log.setId(frimwareUpdateLog.getId());
    	log.setUpdateResultReport("Initial");
    	log.setStatus("Online");
    	log.setGetNewVerTime(new Date());
		frimwareUpdateLogServiceImpl.updateById(log);
		
		return firmwareVersion;
	}

	@Override
	public void saveFirmwareUpdateLog(FirmwareUpdateLogDto firmwareUpdateLogDto) {
		if(StringUtils.isBlank(firmwareUpdateLogDto.getCurrVer())){
			throw new MyException(CommErrCode.CommParamIsMissing);
		}		
		// 验证固件是否存在
		FrimwareUpdateLog frimwareUpdateLog = getFirmwareUpdateLog(firmwareUpdateLogDto);
		// 记录固件更新日志
    	FrimwareUpdateLog log = new FrimwareUpdateLog();
    	log.setId(frimwareUpdateLog.getId());
    	log.setUpdateResultReport(1==firmwareUpdateLogDto.getUpdateReport()?"Success":"Fail");
    	log.setStatus("Offline");
    	log.setVer(firmwareUpdateLogDto.getCurrVer());
    	log.setUpdateTime(new Date());
    	log.setUpdateLog("lastVer="+frimwareUpdateLog.getVer());
		frimwareUpdateLogServiceImpl.updateById(log);
	}

	
	private FrimwareUpdateLog getFirmwareUpdateLog(BaseApiReqDto apiReqDto) {
		if(StringUtils.isBlank(apiReqDto.getMac()) || StringUtils.isBlank(apiReqDto.getSn()) || StringUtils.isBlank(apiReqDto.getTypeName())){
			throw new MyException(CommErrCode.CommParamIsMissing);
		}
		
    	Wrapper<FrimwareUpdateLog> wrapper = new EntityWrapper<FrimwareUpdateLog>();
    	wrapper.eq("sn", apiReqDto.getSn());
    	wrapper.eq("type_name", apiReqDto.getTypeName());
    	wrapper.eq("mac", apiReqDto.getMac());
    	FrimwareUpdateLog frimwareUpdateLog = frimwareUpdateLogServiceImpl.selectOne(wrapper);
    	if(frimwareUpdateLog==null){
    		throw new MyException(FrimwareProtocolServiceErrCode.FrimwareIsNotExist);
    	}    	
		return frimwareUpdateLog;
	}
	
	private FirmwareVersion getFirmwareVersion(String typeName) {
    	Wrapper<FirmwareVersion> wrapper = new EntityWrapper<FirmwareVersion>();
    	wrapper.eq("type_name", typeName);
    	wrapper.eq("status", 1);
    	FirmwareVersion firmwareVersion = firmwareVersionServiceImpl.selectOne(wrapper);
    	if(firmwareVersion==null){
    		throw new MyException(FrimwareProtocolServiceErrCode.FrimwareTypeVersionIsNotExist);
    	}
		return firmwareVersion;
	}	
	
}
