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
 * @since 2017-12-18
 */
@TableName("firmware_version")
public class FirmwareVersion extends Model<FirmwareVersion> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 设备类型
     */
	@TableField("type_name")
	private String typeName;
    /**
     * 解压密码（明文密码）
     */
	@TableField("unzip_pwd")
	private String unzipPwd;
    /**
     * 新固件版本
     */
	@TableField("new_ver")
	private String newVer;
    /**
     * 固件安装文件路径(相对路径）
     */
	@TableField("firm_file_path")
	private String firmFilePath;
	
    /**
     * 安装文件的MD5编码
     */	
	private String md5;
    /**
     * 状态（0 未投用  1已投用  2已删除）
     */
	private Integer status;
    /**
     * 创建操作员账号（历史）
     */
	@TableField("create_operator")
	private String createOperator;
    /**
     * 创建操作员账号（历史）
     */
	@TableField("create_operator_id")
	private Long createOperatorId;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 修改操作员账号（历史）
     */
	@TableField("modify_operator")
	private String modifyOperator;
    /**
     * 修改操作员账号
     */
	@TableField("modify_operator_id")
	private Long modifyOperatorId;
    /**
     * 修改时间
     */
	@TableField("modify_time")
	private Date modifyTime;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getUnzipPwd() {
		return unzipPwd;
	}

	public void setUnzipPwd(String unzipPwd) {
		this.unzipPwd = unzipPwd;
	}

	public String getNewVer() {
		return newVer;
	}

	public void setNewVer(String newVer) {
		this.newVer = newVer;
	}

	public String getFirmFilePath() {
		return firmFilePath;
	}

	public void setFirmFilePath(String firmFilePath) {
		this.firmFilePath = firmFilePath;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreateOperator() {
		return createOperator;
	}

	public void setCreateOperator(String createOperator) {
		this.createOperator = createOperator;
	}

	public Long getCreateOperatorId() {
		return createOperatorId;
	}

	public void setCreateOperatorId(Long createOperatorId) {
		this.createOperatorId = createOperatorId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifyOperator() {
		return modifyOperator;
	}

	public void setModifyOperator(String modifyOperator) {
		this.modifyOperator = modifyOperator;
	}

	public Long getModifyOperatorId() {
		return modifyOperatorId;
	}

	public void setModifyOperatorId(Long modifyOperatorId) {
		this.modifyOperatorId = modifyOperatorId;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "FirmwareVersion{" +
			"id=" + id +
			", typeName=" + typeName +
			", unzipPwd=" + unzipPwd +
			", newVer=" + newVer +
			", firmFilePath=" + firmFilePath +
			", md5=" + md5 +
			", status=" + status +
			", createOperator=" + createOperator +
			", createOperatorId=" + createOperatorId +
			", createTime=" + createTime +
			", modifyOperator=" + modifyOperator +
			", modifyOperatorId=" + modifyOperatorId +
			", modifyTime=" + modifyTime +
			"}";
	}
}
