package com.qq1833111108.frimware.controller;


import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.qq1833111108.biz.entity.FirmwareVersion;
import com.qq1833111108.common.controller.BaseController;
import com.qq1833111108.common.dto.ApiResDto;
import com.qq1833111108.common.dto.FirmwareUpdateLogDto;
import com.qq1833111108.common.dto.GetNewVerDto;
import com.qq1833111108.config.properties.BizProperties;
import com.qq1833111108.frimware.service.IFrimwareProtocolService;
import com.qq1833111108.sys.service.IUploadFileService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
/**
 * <p>
 *  终端接口
 * </p>
 *
 * @author qq183311108
 * @since 2017-12-05
 */
@Api(value = "终端协议接口")
@RestController
@RequestMapping("/tp")
public class FrimwareProtocolController extends BaseController{

    @Autowired
    private IFrimwareProtocolService frimwareProtocolServiceImpl;

    /**
     * 终端拉取固件最新版本（需要对终端序列号进行鉴权，并验证是否有新版本）
     * @return
     */
    @PostMapping(value="/get_new_ver", produces = "application/json; charset=utf-8")
    @ApiOperation(value = "拉取固件最新版本", notes = "服务端需要对终端序列号鉴权是否存在，并且该设备是否允许更新，并检查该设备类型是否有新版本。以上条件不满足时会返回失败的相应错误代码。如果通过，则返回的code为success，并在resBizMap里封装有则返回新版本的下载地址newVerfilePath", tags = "拉取固件最新版本", response = ApiResDto.class, httpMethod = "POST")
    public ApiResDto getNewVer(@ApiParam(name="固件基本信息",value="传入json格式",required=true) @RequestBody GetNewVerDto getNewVerDto, HttpServletRequest request) {
    	FirmwareVersion firmwareVersion = null;
    	try {
    		firmwareVersion = frimwareProtocolServiceImpl.getFirmwareNewVersion(getNewVerDto);
		} catch (Exception e) {
			return new ApiResDto(e);
		}
    	HashMap<String, Object> hashMap = new HashMap<String, Object>();	
		hashMap.put("newVer", firmwareVersion.getNewVer());  
		hashMap.put("newVerfilePath", firmwareVersion.getFirmFilePath());  
		hashMap.put("unzipPwd", firmwareVersion.getUnzipPwd());  
		hashMap.put("md5", firmwareVersion.getMd5());  
    	return new ApiResDto(hashMap);
    }

    /**
     * 上送固件更新结果（需要对终端序列号进行鉴权，并验证是否有新版本）
     * @return
     */
    @PostMapping(value="/save_update_log", produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "上送固件更新结果", notes = "服务端需要对终端序列号鉴权是否存在，并且该设备是否允许更新，如果不通过，将返回相应异常编码，否则，则记录设备升级日志。记录日志成功，则返回的code为success，否则返回相应失败编码", tags = "上送固件更新结果", response = ApiResDto.class, httpMethod = "POST")
    public ApiResDto succDone(@ApiParam(name="固件基本信息",value = "传入json格式",required = true) @RequestBody FirmwareUpdateLogDto firmwareUpdateLogDto, HttpServletRequest request) {
    	try {
    		frimwareProtocolServiceImpl.saveFirmwareUpdateLog(firmwareUpdateLogDto);
		} catch (Exception e) {
			return new ApiResDto(e);
		}
		return new ApiResDto();
    	
    }
}
