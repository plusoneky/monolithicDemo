package com.qq1833111108.biz.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author qq183311108
 * @since 2017-12-14
 */
@TableName("frimware_update_log")
public class FrimwareUpdateLog extends Model<FrimwareUpdateLog> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 设备序列号
     */
	private String sn;
	@TableField("type_id")
	private Long typeId;
    /**
     * 设备类型（冗余）
     */
	@TableField("type_name")
	private String typeName;
    /**
     * 设备MAC地址
     */
	private String mac;
    /**
     * 设备状态（online   offline）
     */
	private String status;
    /**
     * 最近一次上送更新结果的时间
     */
	@TableField("update_time")
	private Date updateTime;
    /**
     * 当前固件版本
     */
	private String ver;
    /**
     * 更新结果报告（initial success fail）
     */
	@TableField("update_result_report")
	private String updateResultReport;
    /**
     * 更新日志
     */
	@TableField("update_log")
	private String updateLog;
    /**
     * 导入操作员账号（历史）
     */
	@TableField("import_operator")
	private String importOperator;
    /**
     * 导入操作员账号
     */
	@TableField("import_operator_id")
	private Long importOperatorId;
    /**
     * 导入时间
     */
	@TableField("import_time")
	private Date importTime;
    /**
     * 是否有允许更新标识，默认为no（yes  no）
     */
	private String flag;
    /**
     * 最近一次成功拉取新版本的时间
     */
	@TableField("get_new_ver_time")
	private Date getNewVerTime;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public String getUpdateResultReport() {
		return updateResultReport;
	}

	public void setUpdateResultReport(String updateResultReport) {
		this.updateResultReport = updateResultReport;
	}

	public String getUpdateLog() {
		return updateLog;
	}

	public void setUpdateLog(String updateLog) {
		this.updateLog = updateLog;
	}

	public String getImportOperator() {
		return importOperator;
	}

	public void setImportOperator(String importOperator) {
		this.importOperator = importOperator;
	}

	public Long getImportOperatorId() {
		return importOperatorId;
	}

	public void setImportOperatorId(Long importOperatorId) {
		this.importOperatorId = importOperatorId;
	}

	public Date getImportTime() {
		return importTime;
	}

	public void setImportTime(Date importTime) {
		this.importTime = importTime;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Date getGetNewVerTime() {
		return getNewVerTime;
	}

	public void setGetNewVerTime(Date getNewVerTime) {
		this.getNewVerTime = getNewVerTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "FrimwareUpdateLog{" +
			"id=" + id +
			", sn=" + sn +
			", typeId=" + typeId +
			", typeName=" + typeName +
			", mac=" + mac +
			", status=" + status +
			", updateTime=" + updateTime +
			", ver=" + ver +
			", updateResultReport=" + updateResultReport +
			", updateLog=" + updateLog +
			", importOperator=" + importOperator +
			", importOperatorId=" + importOperatorId +
			", importTime=" + importTime +
			", flag=" + flag +
			", getNewVerTime=" + getNewVerTime +
			"}";
	}
}
