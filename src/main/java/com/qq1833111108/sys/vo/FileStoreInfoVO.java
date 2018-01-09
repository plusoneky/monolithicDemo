package com.qq1833111108.sys.vo;

public class FileStoreInfoVO {
	
	private String absPath;
	
	private String relativePath;
	
	private boolean canDownLoad;
	
	private String downLoadURL;
	
	private String md5;

	public String getAbsPath() {
		return absPath;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public void setAbsPath(String absPath) {
		this.absPath = absPath;
	}
	
	public boolean isCanDownLoad() {
		return canDownLoad;
	}

	public void setCanDownLoad(boolean canDownLoad) {
		this.canDownLoad = canDownLoad;
	}

	
	public String getDownLoadURL() {
		return downLoadURL;
	}

	public void setDownLoadURL(String downLoadURL) {
		this.downLoadURL = downLoadURL;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}
	
	

}
