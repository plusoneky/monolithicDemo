package com.qq1833111108.biz.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.crab2died.ExcelUtils;
import com.qq1833111108.biz.entity.FirmwareVersion;
import com.qq1833111108.biz.entity.FrimwareUpdateLog;
import com.qq1833111108.biz.service.IFirmwareVersionService;
import com.qq1833111108.biz.service.IFrimwareUpdateLogService;
import com.qq1833111108.biz.vo.FrimwareInfoExcelVO;
import com.qq1833111108.common.controller.BaseController;
import com.qq1833111108.common.result.JsonResult;
import com.qq1833111108.common.utils.StringUtil;
import com.qq1833111108.sys.entity.Permission;
import com.qq1833111108.sys.service.IUploadFileService;
import com.qq1833111108.sys.service.impl.UploadFileServiceImpl.UploadFileType;
import com.qq1833111108.sys.vo.FileStoreInfoVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 固件信息导入
 * </p>
 *
 * @author qq183311108
 * @since 2017-12-07
 */
@Api(value = "固件信息导入（后台接口）")
@Controller
@RequestMapping({ "/biz/f_log" })
public class FrimwareUpdateLogController extends BaseController {
	@Autowired
	private IFrimwareUpdateLogService frimwareUpdateLogImpl;
	@Autowired
	private IFirmwareVersionService firmwareVersionServiceImpl;
	@Autowired
	private IUploadFileService uploadFileServiceImpl;
	private static final Logger logger = LoggerFactory.getLogger(FirmwareVersionController.class);

	@RequiresPermissions({ "biz.log:list" })
	@GetMapping({ "/loglist" })
	public String list() {
		return "/biz/firmwareUpdateLog/loglist";
	}

	@RequiresPermissions({ "biz.log:list" })
	@ResponseBody
	@PostMapping({ "/getList" })
	public Map<String, Object> getList(int pageNumber, int pageSize, String searchText) {
		Map<String, Object> result = new HashMap<String, Object>();
		Page<FrimwareUpdateLog> page = new Page<FrimwareUpdateLog>(pageNumber, pageSize);
		EntityWrapper<FrimwareUpdateLog> wrapper = new EntityWrapper<FrimwareUpdateLog>();
		if (StringUtil.isNotEmpty(searchText)) {
			wrapper.like("sn", searchText);
		}
		wrapper.orderBy("id", false);
		Page<FrimwareUpdateLog> userPage = this.frimwareUpdateLogImpl.selectPage(page, wrapper);
		result.put("total", Integer.valueOf(userPage.getTotal()));
		result.put("rows", userPage.getRecords());
		return result;
	}

	@RequiresPermissions({ "biz.log:import" })
	@ResponseBody
	@ApiOperation(value = "导入固件信息", notes = "", response = JsonResult.class, httpMethod = "POST")
	@PostMapping(value = { "/importFrimwareUpdateLog" }, produces = { "application/json;charset=UTF-8" }, consumes = {
			"multipart/form-data" })
	public JsonResult importFrimwareUpdateLog(@ApiParam(required = true, value = "信息安装文件") MultipartFile logFile) {
		if ((logFile == null) || (logFile.getSize() <= 0L)) {
			return renderError("上传文件不能为空");
		}
		FileStoreInfoVO fileStoreInfoVO = null;
		try {
			fileStoreInfoVO = this.uploadFileServiceImpl.uploadFile(logFile, UploadFileType.EXCEL);
		} catch (Exception e) {
			return renderError("上传文件错误");
		}
		if (fileStoreInfoVO.isCanDownLoad()) {
			return renderError("上传文件类型错误，禁止被下载");
		}
		try {
			List<FrimwareInfoExcelVO> excelList = ExcelUtils.getInstance()
					.readExcel2Objects(fileStoreInfoVO.getAbsPath(), FrimwareInfoExcelVO.class, 0, 0);

			Set<String> set = new HashSet<String>();
			String typeNameListStr = "";
			List<FrimwareUpdateLog> insertLogList = new ArrayList<FrimwareUpdateLog>();

			HashSet<String> snSet = new HashSet<String>();
			HashSet<String> macSet = new HashSet<String>();
			for (FrimwareInfoExcelVO excelTerminalVO : excelList) {
				boolean addResult = set.add(excelTerminalVO.getTypeName());
				if (addResult) {
					typeNameListStr = typeNameListStr + "," + excelTerminalVO.getTypeName();
				}
				FrimwareUpdateLog oneFrimwareUpdateLog = new FrimwareUpdateLog();
				oneFrimwareUpdateLog.setSn(excelTerminalVO.getSn().toLowerCase());
				oneFrimwareUpdateLog.setTypeName(excelTerminalVO.getTypeName().toLowerCase());
				oneFrimwareUpdateLog.setMac(excelTerminalVO.getMac().toLowerCase());
				oneFrimwareUpdateLog.setVer(excelTerminalVO.getVer().toLowerCase());
				oneFrimwareUpdateLog.setImportOperator(getCurrentLoginUsername());
				oneFrimwareUpdateLog.setImportOperatorId(getCurrentLoginId());
				oneFrimwareUpdateLog.setImportTime(new Date());
				oneFrimwareUpdateLog.setFlag("yes".equalsIgnoreCase(excelTerminalVO.getFlag()) ? "yes" : "no");

				oneFrimwareUpdateLog.setStatus("offline");
				oneFrimwareUpdateLog.setUpdateResultReport("initial");
				insertLogList.add(oneFrimwareUpdateLog);

				boolean snExistInExcel = snSet.add(oneFrimwareUpdateLog.getSn());
				if (!snExistInExcel) {
					return renderError(
							"终端序列号：" + excelTerminalVO.getSn() + "有重复，请检查Excel文件中的SN设备序列号和物理网卡地址MAC，系统不区分序列号、物理网卡地址、设备类型等大小写。");
				}
				boolean macExistInExcel = macSet.add(oneFrimwareUpdateLog.getMac());
				if (!macExistInExcel) {
					return renderError("终端序列号：" + excelTerminalVO.getSn() + "，其物理网卡地址" + excelTerminalVO.getMac()
							+ "有重复，请检查Excel文件中的SN设备序列号和物理网卡地址MAC，系统不区分序列号、物理网卡地址、设备类型等大小写。");
				}
			}

			{
				Wrapper<FrimwareUpdateLog> snWrapper = new EntityWrapper<FrimwareUpdateLog>();
				snWrapper.in("sn", snSet);
				List<FrimwareUpdateLog> snExistlist = this.frimwareUpdateLogImpl.selectList(snWrapper);
				if (snExistlist != null && snExistlist.size() > 0) {
					String snExistStr = "";
					for (FrimwareUpdateLog existOneSn : snExistlist) {
						snExistStr = snExistStr + existOneSn + ",";
					}
					return renderError(
							"Excel文件中，共" + snExistlist.size() + "个终端与数据库已存在的序列号产生重复，系统不区分序列号、物理网卡地址、设备类型等大小写。重复的序列号如下：" + snExistStr);
				}
			}

			{
				Wrapper<FrimwareUpdateLog> macWrapper = new EntityWrapper<FrimwareUpdateLog>();
				macWrapper.in("mac", macSet);
				List<FrimwareUpdateLog> macExistlist = this.frimwareUpdateLogImpl.selectList(macWrapper);
				if (macExistlist != null && macExistlist.size() > 0) {
					String macExistStr = "";
					for (FrimwareUpdateLog existOneMac : macExistlist) {
						macExistStr = macExistStr + existOneMac + ",";
					}
					return renderError("Excel文件中，共" + macExistlist.size()
							+ "个终端与数据库已存在的物理网卡地址产生重复，系统不区分序列号、物理网卡地址、设备类型等大小写。重复的物理网卡地址如下：" + macExistlist);
				}
			}

			String sqlStr = typeNameListStr.substring(1);
			EntityWrapper<FirmwareVersion> versionWrapper = new EntityWrapper<FirmwareVersion>();
			versionWrapper.in("type_name", sqlStr);
			List<FirmwareVersion> versionlist = this.firmwareVersionServiceImpl.selectList(versionWrapper);

			HashMap<String, FirmwareVersion> verHashMap = new HashMap<String, FirmwareVersion>();
			for (FirmwareVersion vesion : versionlist) {
				verHashMap.put(vesion.getTypeName(), vesion);
			}
			for (FrimwareUpdateLog terminal : insertLogList) {
				FirmwareVersion existFirmwareVersion = (FirmwareVersion) verHashMap.get(terminal.getTypeName());
				if (existFirmwareVersion == null) {
					return renderError("终端序列号：" + terminal.getSn() + "，其设备类型：" + terminal.getTypeName() + "还未在固件版本中定义，请先维护固件版本");
				}
				terminal.setTypeId(existFirmwareVersion.getId());
			}
			boolean insertResult = false;
			try {
				insertResult = this.frimwareUpdateLogImpl.insertBatch(insertLogList);
			} catch (Exception e) {
				logger.error("", e);
				return renderError("导入失败，请检查SN设备序列号，物理网卡地址，是否与数据库已有数据冲突。" + e.toString());
			}
			return insertResult ? renderSuccess("导入成功") : renderError("导入失败");
		} catch (Exception e) {
			logger.error("", e);
		}
		return renderError("解析文件产生错误，请检查文件格式是否与模板一致");
	}

	@RequiresPermissions({ "biz.log:add" })
	@ResponseBody
	@ApiOperation(value = "添加固件", notes = "", response = JsonResult.class, httpMethod = "POST")
	@PostMapping(value = { "/addFrimwareUpdateLog" }, produces = { "application/json;charset=UTF-8" }, consumes = {
			"multipart/form-data" })
	public JsonResult addFrimwareUpdateLog(@ApiParam(required = true, value = "设备序列号") String sn,
			@ApiParam(required = true, value = "设备物理网卡地址") String mac,
			@ApiParam(required = true, value = "设备类型") Long typeId,
			@ApiParam(required = true, value = "设备类型") String typeName,
			@ApiParam(required = true, value = "设备版本") String ver,
			@ApiParam(required = true, value = "是否允许更新") String flag) {
		if ((StringUtils.isBlank(sn)) || (StringUtils.isBlank(mac)) || (StringUtils.isBlank(typeName))
				|| (StringUtils.isBlank(ver))) {
			return renderError("必填的参数不能为空");
		}
		EntityWrapper<FrimwareUpdateLog> wrapper = new EntityWrapper<FrimwareUpdateLog>();
		wrapper.where("sn={0}", new Object[] { sn }).or("mac={0}", new Object[] { mac });
		FrimwareUpdateLog existOne = (FrimwareUpdateLog) this.frimwareUpdateLogImpl.selectOne(wrapper);
		if (existOne != null) {
			if (existOne.getSn().equalsIgnoreCase(sn)) {
				return renderError("序列号与已有数据重复");
			}
			if (existOne.getMac().equalsIgnoreCase(mac)) {
				return renderError("物理网卡与已有数据重复");
			}
		}
		FrimwareUpdateLog frimwareUpdateLog = new FrimwareUpdateLog();
		frimwareUpdateLog.setSn(sn.toLowerCase());
		frimwareUpdateLog.setTypeId(typeId);
		frimwareUpdateLog.setTypeName(typeName.toLowerCase());
		frimwareUpdateLog.setVer(ver.toLowerCase());
		frimwareUpdateLog.setMac(mac.toLowerCase());
		frimwareUpdateLog.setFlag(flag.toLowerCase());
		frimwareUpdateLog.setImportOperatorId(getCurrentLoginId());
		frimwareUpdateLog.setImportOperator(getCurrentLoginUsername());
		frimwareUpdateLog.setImportTime(new Date());

		frimwareUpdateLog.setStatus("offline");
		frimwareUpdateLog.setUpdateResultReport("initial");
		return this.frimwareUpdateLogImpl.insert(frimwareUpdateLog) ? renderSuccess("添加成功") : renderError("添加失败");
	}

	@RequiresPermissions({ "biz.log:update" })
	@ResponseBody
	@ApiOperation(value = "修改固件信息", notes = "", response = JsonResult.class, httpMethod = "POST")
	@PostMapping(value = { "/updateFrimwareUpdateLog" }, produces = { "application/json;charset=UTF-8" }, consumes = {
			"multipart/form-data" })
	public JsonResult updateFrimwareUpdateLog(@ApiParam(required = true, value = "设备id") String id,
			@ApiParam(required = true, value = "设备序列号") String sn,
			@ApiParam(required = true, value = "设备物理网卡地址") String mac,
			@ApiParam(required = true, value = "设备类型") Long typeId,
			@ApiParam(required = true, value = "设备类型") String typeName,
			@ApiParam(required = true, value = "设备信息") String ver,
			@ApiParam(required = true, value = "是否允许更新") String flag) {
		if (StringUtils.isBlank(id)) {
			return renderError("必填的参数id不能为空");
		}
		if ((StringUtils.isBlank(id)) && (StringUtils.isBlank(sn)) && (StringUtils.isBlank(mac))
				&& (StringUtils.isBlank(typeName)) && (StringUtils.isBlank(ver))) {
			return renderError("必填的参数不能为空");
		}
		EntityWrapper<FrimwareUpdateLog> wrapper = new EntityWrapper<FrimwareUpdateLog>();
		wrapper.where("sn={0}", new Object[] { sn }).or("mac={0}", new Object[] { mac });
		wrapper.andNew().ne("id", id);
		FrimwareUpdateLog existOne = (FrimwareUpdateLog) this.frimwareUpdateLogImpl.selectOne(wrapper);
		if (existOne != null) {
			if (existOne.getSn().equalsIgnoreCase(sn)) {
				return renderError("序列号与已有数据重复");
			}
			if (existOne.getMac().equalsIgnoreCase(mac)) {
				return renderError("物理网卡与已有数据重复");
			}
		}
		FrimwareUpdateLog frimwareUpdateLog = new FrimwareUpdateLog();
		frimwareUpdateLog.setId(Long.valueOf(id));
		frimwareUpdateLog.setSn(sn.toLowerCase());
		frimwareUpdateLog.setTypeId(typeId);
		frimwareUpdateLog.setTypeName(typeName.toLowerCase());
		frimwareUpdateLog.setVer(ver.toLowerCase());
		frimwareUpdateLog.setMac(mac.toLowerCase());
		frimwareUpdateLog.setFlag(flag.toLowerCase());
		frimwareUpdateLog.setImportOperatorId(getCurrentLoginId());
		frimwareUpdateLog.setImportOperator(getCurrentLoginUsername());
		frimwareUpdateLog.setImportTime(new Date());

		return this.frimwareUpdateLogImpl.updateById(frimwareUpdateLog) ? renderSuccess("修改成功") : renderError("修改失败");
	}

	@RequiresPermissions({ "biz.log:delete" })
	@ResponseBody
	@PostMapping({ "/delete" })
	public JsonResult delete(@RequestParam(value = "id", required = false) String id) {
		if (StringUtil.isEmpty(id)) {
			return renderError("请选择数据");
		}
		String[] split = id.split(",");
		List<String> userId = new ArrayList<String>();
		for (String item : split) {
			userId.add(item);
		}
		return this.frimwareUpdateLogImpl.deleteBatchIds(userId) ? renderSuccess("删除成功") : renderError("删除失败");
	}
}
