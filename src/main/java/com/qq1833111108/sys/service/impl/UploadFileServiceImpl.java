package com.qq1833111108.sys.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.qq1833111108.common.exception.MyException;
import com.qq1833111108.common.exception.MyException.CommErrCode;
import com.qq1833111108.config.properties.BizProperties;
import com.qq1833111108.sys.service.IUploadFileService;
import com.qq1833111108.sys.vo.FileStoreInfoVO;

import java.nio.MappedByteBuffer;  

/**
 * @author Administrator
 *
 */
@Service
public class UploadFileServiceImpl implements IUploadFileService {
    
    private BizProperties bizProperties;
    
    @Autowired
    UploadFileServiceImpl(BizProperties bizProperties){
    	this.bizProperties = bizProperties;
    }
	
	public enum UploadFileType {
		IMG("img","下载图片",true),
	    EXCEL("excel","内部文件",false),
	    APK("apk","安装包",true),
	    OTHER("other","其他资料",false),
	    ;
		private String uploadFileTypeCode;
		
		private boolean canDownLoad;

	    private UploadFileType(String fileTypeCode, String note,boolean canDownLoad) {
	    	this.uploadFileTypeCode = fileTypeCode;
	    	this.canDownLoad = canDownLoad;
	    }
	    
	    public String getUploadFileTypeCode(){
	    	return this.uploadFileTypeCode;
	    }
	    
	    public boolean getCanDownLoad(){
	    	return this.canDownLoad;
	    }
	}
	
	public static String getFileExtendName(String fileName){
		//获取后缀
		String fileExtendName = fileName.substring(fileName.indexOf(".")+1);
		return fileExtendName;
	}
	
    public static String getMd5ByFile(File file) throws FileNotFoundException {  
        String value = null;  
        FileInputStream in = new FileInputStream(file);  
		try {  
		    MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());  
		    MessageDigest md5 = MessageDigest.getInstance("MD5");  
		    md5.update(byteBuffer);  
		    BigInteger bi = new BigInteger(1, md5.digest());  
		    value = bi.toString(16);  
		} catch (Exception e) {  
		    e.printStackTrace();  
		} finally {  
		        if(null != in) {  
		            try {  
		            in.close();  
		        } catch (IOException e) {  
		            e.printStackTrace();  
		        }  
		    }  
		}  
		return value;  
    } 	
	
	/**
	 * 根据上传文件的后缀名，返回文件的存储根目录（不包括日期等中间目录和应用目录）
	 * @param fileExtendName
	 * @return
	 */
	public String getAbsDir(String fileExtendName,UploadFileType uploadFileType){
		String uploadFilAbsDir = null;
		if(uploadFileType.getCanDownLoad()){
			uploadFilAbsDir=bizProperties.getUpdownFileAbsDir();
		}else{
			uploadFilAbsDir=bizProperties.getUploadFileAbsDir();
		}
		return uploadFilAbsDir;
	}
	
	/**
	 * 上传文件
	 * @param multipartFile,
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@Override
	public FileStoreInfoVO uploadFile(MultipartFile multipartFile,UploadFileType uploadFileType) throws Exception {
		if (null == multipartFile) {
			throw new MyException(CommErrCode.CommParamIsMissing);
		}
		
		//获取扩展名
		String fileExtendName = getFileExtendName(multipartFile.getOriginalFilename());

		//创建中间目录
		SimpleDateFormat y_m_sdf = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat d_sdf = new SimpleDateFormat("dd");		
		Date now = new Date();
		
		String uploadFileRelativeDir = new StringBuilder("/")
				.append(uploadFileType.getUploadFileTypeCode())
				.append("/").append(y_m_sdf.format(now))
				.append("/").append(d_sdf.format(now))
				.append("/").toString();
		
		//获得绝路目录
		String uploadFilAbsDir = getAbsDir(fileExtendName,uploadFileType)+uploadFileRelativeDir;

		//判断存储文件的绝路路径目录是否存在，没有则创建
		File dir = new File(uploadFilAbsDir);
		if(!dir.exists()){
			boolean mkdirResult = dir.mkdirs();
			if(!mkdirResult){
				throw new MyException(UploadFileServiceErrCode.FileIsNotSupport);
			}
		}
		
		//产生新的文件名
		String newFileNamePrefix = UUID.randomUUID().toString().replace("-", "");
		String newfileName = newFileNamePrefix + "-" + multipartFile.getOriginalFilename();

		//创建文件的存储路径
		String uploadFilAbsPath = uploadFilAbsDir + newfileName;
		File uploadFileAbsPath = new File(uploadFilAbsPath);
		
		// 转存文件
		multipartFile.transferTo(uploadFileAbsPath);
		//设置文件权限为可读可执行，使远程可以下载
		uploadFileAbsPath.setReadable(true, false);
		uploadFileAbsPath.setExecutable(true, false);
		
		String fileMd5 = Files.hash(uploadFileAbsPath, Hashing.md5()).toString();
		
		FileStoreInfoVO returnVo = new FileStoreInfoVO(); 
		returnVo.setAbsPath(uploadFilAbsPath);
		if(uploadFileType.canDownLoad){
			returnVo.setCanDownLoad(true);
			returnVo.setDownLoadURL(bizProperties.getUpdownPrefixURL()+uploadFileRelativeDir+newfileName);
		}else{
			returnVo.setCanDownLoad(false);
		}
		returnVo.setRelativePath(uploadFileRelativeDir+newfileName);
		returnVo.setMd5(fileMd5);

		return returnVo;
	}	
	

	
}
