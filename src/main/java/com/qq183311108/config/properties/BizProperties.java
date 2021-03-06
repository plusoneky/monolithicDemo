package com.qq183311108.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component  
@ConfigurationProperties(prefix="bizProperties") //接收application.yml中的bizProperties下面的属性
public class BizProperties {
	
	/**
	 * 上传文件临时目录
	 */	
	private String uploadFileTmpDir;

	/**
	 * 上传文件目录，不允许远程下载，如Excel文件
	 */
	private String uploadFileAbsDir;
	
	/**
	 * 上传文件目录，并需要远程下载，如安装包、图片等文件
	 */
	private String updownFileAbsDir;
	
	/**
	 * 下载地址域名前缀部分
	 */
	private String updownPrefixURL;
	
	/**
	 * 服务器一级域名
	 */	
	private String firstClassDomainName;
	

	public String getUploadFileAbsDir() {
		return uploadFileAbsDir;
	}

	public void setUploadFileAbsDir(String uploadFileAbsDir) {
		this.uploadFileAbsDir = uploadFileAbsDir;
	}

	public String getUpdownFileAbsDir() {
		return updownFileAbsDir;
	}

	public void setUpdownFileAbsDir(String updownFileAbsDir) {
		this.updownFileAbsDir = updownFileAbsDir;
	}

	public String getUpdownPrefixURL() {
		return updownPrefixURL;
	}

	public void setUpdownPrefixURL(String updownPrefixURL) {
		this.updownPrefixURL = updownPrefixURL;
	}

	public String getFirstClassDomainName() {
		return firstClassDomainName;
	}

	public void setFirstClassDomainName(String firstClassDomainName) {
		this.firstClassDomainName = firstClassDomainName;
	}

	public String getUploadFileTmpDir() {
		return uploadFileTmpDir;
	}

	public void setUploadFileTmpDir(String uploadFileTmpDir) {
		this.uploadFileTmpDir = uploadFileTmpDir;
	}
	
	
}
