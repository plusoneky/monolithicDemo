package com.qq1833111108.biz.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.qq1833111108.biz.entity.FirmwareVersion;
import com.qq1833111108.biz.service.IFirmwareVersionService;
import com.qq1833111108.common.controller.BaseController;
import com.qq1833111108.common.result.JsonResult;
import com.qq1833111108.common.utils.StringUtil;
import com.qq1833111108.sys.service.IUploadFileService;
import com.qq1833111108.sys.service.impl.UploadFileServiceImpl.UploadFileType;
import com.qq1833111108.sys.vo.FileStoreInfoVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 *  固件版本上传
 * </p>
 *
 * @author qq183311108
 * @since 2017-12-05
 */
@Api(value = "固件版本上传（后台接口）")
@Controller
@RequestMapping("/biz/f_ver")
public class FirmwareVersionController extends BaseController{
	

    @Autowired
    private IFirmwareVersionService firmwareVersionServiceImpl;
    
    @Autowired
    private IUploadFileService uploadFileServiceImpl;
    
	//private static final Logger logger = LoggerFactory.getLogger(FirmwareVersionController.class);

    /**
     * 进入固件版本上传
     * @return
     */
	  @RequiresPermissions({"biz.version:list"})
	  @GetMapping({"/versionlist"})
	  public String list()
	  {
	    return "/biz/firmwareVersion/versionlist";
	  }

    /**
     * 获取固件版本列表
     * @return
     */
    @RequiresPermissions({"biz.version:list"})
    @ResponseBody
    @GetMapping({"/getTypeList"})
    public List<FirmwareVersion> getTypeList(String searchText)
    {
      EntityWrapper<FirmwareVersion> wrapper = new EntityWrapper<FirmwareVersion>();
      wrapper.setSqlSelect(new String[] { "id", "type_name" });
      wrapper.orderBy("id", false);
      if (StringUtil.isNotEmpty(searchText)) {
        wrapper.like("type_name", searchText);
      }
      List<FirmwareVersion> list = this.firmwareVersionServiceImpl.selectList(wrapper);
      return list;
    }
    
    @RequiresPermissions({"biz.version:list"})
    @ResponseBody
    @PostMapping({"/getList"})
    public Map<String, Object> getVersionList(int pageNumber, int pageSize, String searchText)
    {
      Map<String, Object> result = new HashMap<String, Object>();
      Page<FirmwareVersion> page = new Page<FirmwareVersion>(pageNumber, pageSize);
      EntityWrapper<FirmwareVersion> wrapper = new EntityWrapper<FirmwareVersion>();
      if (StringUtil.isNotEmpty(searchText)) {
        wrapper.like("new_ver", searchText);
      }
      wrapper.orderBy("id", false);
      Page<FirmwareVersion> userPage = this.firmwareVersionServiceImpl.selectPage(page, wrapper);
      result.put("total", Integer.valueOf(userPage.getTotal()));
      result.put("rows", userPage.getRecords());
      return result;
    }    

    @RequiresPermissions({"biz.version:add"})
    @ResponseBody
    @ApiOperation(value="添加固件版本", notes="上传的文件后面也要限制大小", response=JsonResult.class, httpMethod="POST")
    @PostMapping(value={"/addFirmwareVersion"}, produces={MediaType.APPLICATION_JSON_UTF8_VALUE} , consumes={MediaType.MULTIPART_FORM_DATA_VALUE})
    public JsonResult addFirmwareVersion(@ApiParam(required=true, value="版本号标识") String newVer, @ApiParam(required=true, value="解压密码") String unzipPwd, @ApiParam(required=true, value="设备类型") String typeName, @ApiParam(required=true, value="版本安装文件") MultipartFile verFile)
    {
      if ((StringUtils.isBlank(newVer)) || (StringUtils.isBlank(unzipPwd)) || (StringUtils.isBlank(typeName))) {
        return renderError("必填的参数不能为空");
      }
      if ((verFile == null) || (verFile.getSize() <= 0L)) {
        return renderError("上传文件不能为空");
      }
      FileStoreInfoVO fileStoreInfoVO = null;
      try
      {
        fileStoreInfoVO = this.uploadFileServiceImpl.uploadFile(verFile, UploadFileType.APK);
      }
      catch (Exception e)
      {
        return renderError("上传文件错误");
      }
      if (!fileStoreInfoVO.isCanDownLoad()) {
        return renderError("上传文件类型错误，必须可以下载");
      }
      Date now = new Date();
      FirmwareVersion firmwareVersion = new FirmwareVersion();
      firmwareVersion.setCreateOperatorId(getCurrentLoginId());
      firmwareVersion.setCreateOperator(getCurrentLoginUsername());
      firmwareVersion.setCreateTime(now);
      firmwareVersion.setModifyOperator(getCurrentLoginUsername());
      firmwareVersion.setModifyOperatorId(getCurrentLoginId());
      firmwareVersion.setModifyTime(now);
      firmwareVersion.setFirmFilePath(fileStoreInfoVO.getDownLoadURL());
      firmwareVersion.setNewVer(newVer.toLowerCase());
      firmwareVersion.setTypeName(typeName.toLowerCase());
      firmwareVersion.setMd5(fileStoreInfoVO.getMd5());
      firmwareVersion.setUnzipPwd(unzipPwd);
      
      return this.firmwareVersionServiceImpl.insert(firmwareVersion) ? renderSuccess("添加成功") : renderError("添加失败");
    }
    
    @RequiresPermissions({"biz.version:update"})
    @ResponseBody
    @ApiOperation(value="修改固件版本", notes="上传的文件后面也要限制大小", response=JsonResult.class, httpMethod="POST")
    @PostMapping(value={"/updateFirmwareVersion"}, produces={MediaType.APPLICATION_JSON_UTF8_VALUE} , consumes={MediaType.MULTIPART_FORM_DATA_VALUE})
    public JsonResult updateFirmwareVersion(@ApiParam(required=true, value="版本ID") String id, @ApiParam(required=false, value="版本号标识") String newVer, @ApiParam(required=false, value="解压密码") String unzipPwd, @ApiParam(required=false, value="版本安装文件") MultipartFile verFile)
    {
      if (StringUtils.isBlank(id)) {
        return renderError("必填的参数id不能为空");
      }
      if ((StringUtils.isBlank(newVer)) && (StringUtils.isBlank(unzipPwd)) && (verFile == null)) {
        return renderError("要修改的数据不能全部为空");
      }
      FirmwareVersion firmwareVersion = new FirmwareVersion();
      firmwareVersion.setId(Long.valueOf(id));
      FileStoreInfoVO fileStoreInfoVO = null;
      if ((verFile != null) && (StringUtils.isNotBlank(verFile.getOriginalFilename())) && (verFile.getSize() > 0L))
      {
        try
        {
          fileStoreInfoVO = this.uploadFileServiceImpl.uploadFile(verFile, UploadFileType.APK);
        }
        catch (Exception e)
        {
          return renderError("上传文件错误");
        }
        if (!fileStoreInfoVO.isCanDownLoad()) {
          return renderError("上传文件类型错误");
        }
        firmwareVersion.setFirmFilePath(fileStoreInfoVO.getDownLoadURL());
        firmwareVersion.setMd5(fileStoreInfoVO.getMd5());
      }
      firmwareVersion.setModifyOperatorId(getCurrentLoginId());
      firmwareVersion.setModifyOperator(getCurrentLoginUsername());
      firmwareVersion.setModifyTime(new Date());
      if (StringUtils.isNotBlank(newVer)) {
        firmwareVersion.setNewVer(newVer.toLowerCase());
      }
      if (StringUtils.isNotBlank(unzipPwd)) {
        firmwareVersion.setUnzipPwd(unzipPwd);
      }
      return this.firmwareVersionServiceImpl.updateById(firmwareVersion) ? renderSuccess("修改成功") : renderError("修改失败");
    }
    
    @RequiresPermissions({"biz.version:delete"})
    @ResponseBody
    @PostMapping({"/delete"})
    public JsonResult delete(@RequestParam(value="id", required=false) String id)
    {
      if (StringUtil.isEmpty(id)) {
        return renderError("请选择数据");
      }
      String[] split = id.split(",");
      List<String> userId = new ArrayList<String>();
      for (String item : split) {
        userId.add(item);
      }
      return this.firmwareVersionServiceImpl.deleteBatchIds(userId) ? renderSuccess("删除成功") : renderError("删除失败");
    }
    
    @ResponseBody
    @ApiOperation(value="测试上传文件", notes="上传的文件后面也要限制大小", tags={"上传文件"}, response=JsonResult.class, httpMethod="POST")
    @RequestMapping(value={"/test_upload"}, produces={MediaType.APPLICATION_JSON_UTF8_VALUE} , consumes={MediaType.MULTIPART_FORM_DATA_VALUE})
    public JsonResult testUpload(@ApiParam(required=true, value="test") String test, @ApiParam(required=true, value="通过上传文件") MultipartFile multipartFile)
    {
      FileStoreInfoVO fileStoreInfoVO = null;
      try
      {
        System.out.println("test========================================================" + test);
        fileStoreInfoVO = this.uploadFileServiceImpl.uploadFile(multipartFile, UploadFileType.IMG);
      }
      catch (Exception e)
      {
        return renderError(e.getMessage());
      }
      return renderSuccess(fileStoreInfoVO.isCanDownLoad() ? fileStoreInfoVO.getDownLoadURL() : fileStoreInfoVO.getAbsPath());
    }    
    
}
