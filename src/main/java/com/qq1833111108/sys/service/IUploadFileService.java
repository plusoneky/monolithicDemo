package com.qq1833111108.sys.service;

import org.springframework.web.multipart.MultipartFile;

import com.qq1833111108.common.exception.IErrCode;
import com.qq1833111108.sys.service.impl.UploadFileServiceImpl.UploadFileType;
import com.qq1833111108.sys.vo.FileStoreInfoVO;

public interface IUploadFileService {
	
	public enum UploadFileServiceErrCode implements IErrCode{
		FileIsNotSupport("FileIsNotSupport","不支持此类型的文件"),
		FileMkDirFail("FileMkDirFail","创建文件目录失败"),
		;

		private String errCode;
		private String errMsg;

	    private UploadFileServiceErrCode(String errCode, String errMsg) {
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
	
	FileStoreInfoVO uploadFile(MultipartFile multipartFile,UploadFileType uploadFileType) throws Exception;
}
