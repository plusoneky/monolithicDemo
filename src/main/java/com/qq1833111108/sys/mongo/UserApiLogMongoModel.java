package com.qq1833111108.sys.mongo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qq1833111108.common.mongo.MongoDBBaseModel;

public class UserApiLogMongoModel extends MongoDBBaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private String mobile;

    private Long userInfoId;

    private String apiName;

    private String apiUriPath;

    private String errMsg;

    private String devType;

    private String devId;

    private String reqIp;

    private Date createTime;

    private String reqBody;

    private String reqParams;

    private String reqHeaders;

    private String resSuccJson;

    private Date reqTime;

    private Date resTime;

    private String errCode;

    private Long spendTimeMs;

    private Long methodSpendTimeMs;

    private String userAgentInfo;

    private String classMethodName;

    private Integer status;
    
    private String devMail;
    
    private String servAddr;
    
    private double[] posGps;

    public double[] getPosGps() {
		return posGps;
	}

	public void setPosGps(double lat,double lon) {
		if(lat !=0 && lon !=0){
			this.posGps = new double[2];
			this.posGps[0] = lat;
			this.posGps[1] = lon;			
		}
	}

	public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(Long userInfoId) {
        this.userInfoId = userInfoId;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiUriPath() {
        return apiUriPath;
    }

    public void setApiUriPath(String apiUriPath) {
        this.apiUriPath = apiUriPath;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public double getLon() {
    	if(this.posGps!=null && this.posGps.length==2){
    		return this.posGps[1];	
    	}else{
    		return 0;
    	}
        
    }

    public double getLat() {
    	if(this.posGps!=null && this.posGps.length==2){
    		return this.posGps[0];
    	}else{
    		return 0;
    	}
        
    }

    public String getReqIp() {
        return reqIp;
    }

    public void setReqIp(String reqIp) {
        this.reqIp = reqIp;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getReqBody() {
        return reqBody;
    }

    public void setReqBody(String reqBody) {
    	this.reqBody = reqBody;      
    }

    public String getReqParams() {
        return reqParams;
    }

    public void setReqParams(String reqParams) {
    	this.reqParams = reqParams;       
    }

    public String getReqHeaders() {
        return reqHeaders;
    }

    public void setReqHeaders(String reqHeaders) {
    	this.reqHeaders = reqHeaders;        
    }

    public String getResSuccJson() {
        return resSuccJson;
    }

    public void setResSuccJson(String resSuccJson) {
    	this.resSuccJson = resSuccJson;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getReqTime() {
        return reqTime;
    }

    public void setReqTime(Date reqTime) {
        this.reqTime = reqTime;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getResTime() {
        return resTime;
    }

    public void setResTime(Date resTime) {
        this.resTime = resTime;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public Long getSpendTimeMs() {
        return spendTimeMs;
    }

    public void setSpendTimeMs(Long spendTimeMs) {
        this.spendTimeMs = spendTimeMs;
    }

    public Long getMethodSpendTimeMs() {
        return methodSpendTimeMs;
    }

    public void setMethodSpendTimeMs(Long methodSpendTimeMs) {
        this.methodSpendTimeMs = methodSpendTimeMs;
    }

    public String getUserAgentInfo() {
        return userAgentInfo;
    }

    public void setUserAgentInfo(String userAgentInfo) {
        this.userAgentInfo = userAgentInfo;
    }

    public String getClassMethodName() {
        return classMethodName;
    }

    public void setClassMethodName(String classMethodName) {
        this.classMethodName = classMethodName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

	public String getDevMail() {
		return devMail;
	}

	public void setDevMail(String devMail) {
		this.devMail = devMail;
	}

	public String getServAddr() {
		return servAddr;
	}

	public void setServAddr(String servAddr) {
		this.servAddr = servAddr;
	}
}