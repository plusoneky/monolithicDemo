package com.qq183311108.common.dto;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.InvalidSessionException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import com.qq183311108.common.exception.MyException;
import com.qq183311108.common.exception.MyException.CommErrCode;
import com.qq183311108.common.i18n.MsgResources;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

/**
 * @author 04
 * API接口返回参数对象
 */
@ApiModel
public class ApiResDto extends BaseDto{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 编码，默认success为成功，非success失败。
	 */
	@ApiModelProperty(value = "返回结果通用编码", allowableValues="{success,...,FrimwareProtocolServiceErrCode.FrimwareIsNotExist}", required = true, example="success") 	
	private String code = "success";
	
	/**
	 * 提示信息，默认为成功
	 */
	@ApiModelProperty(value = "返回结果通用提示信息", allowableValues="{成功,...,此固件不允许升级}", required = true, example="成功") 	
	private String msg = "成功"; 
	
	/**
	 * 返回的数据
	 */
	@ApiModelProperty(value = "返回结果数据集（可选，具体接口会不同）", required = false, example="{'key1':'value1',...,'key9':'value9'}") 	
	private Map<String,Object> resBizMap;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, Object> getResBizMap() {
		return resBizMap;
	}

	public void setResBizMap(Map<String, Object> resBizMap) {
		this.resBizMap = resBizMap;
	}
	
	/**
	 * 向已创建的返回的对象的map中增加元素
	 * @param key
	 * @param obj
	 */
	public void put2ResBizMap(String key,Object obj){
		this.resBizMap.put(key, obj);
	}
	
	/**
	 * 向已创建的返回的对象的map中增加元素
	 * @param key
	 * @param resBizMap
	 */
	public void put2ResBizMap(Map<String,Object> mapValue){
		this.resBizMap.putAll(mapValue);
	}
	
	/**
	 * 只返回成功
	 */
	public ApiResDto(){
	}
	
	
	/**
	 * 返回成功，并附加业务数据
	 * @param errorCode
	 * @param msg
	 */
	public ApiResDto(Map<String, Object> bizDataMap) {
		this.resBizMap = bizDataMap;
	}	
	
	/**
	 * 返回成功，并附加业务数据
	 * @param errorCode
	 * @param msg
	 */
	public ApiResDto(Object data) {
		this.resBizMap = new HashMap<String,Object>();
		this.resBizMap.put("data", data);
	}		
	
	/**
	 * 通过异常构造返回信息，根据异常设置失败错误码和错误提示信息
	 * @param errorCode
	 * @param msg
	 */
	public ApiResDto(Exception e) {
		MyException renrphException = null;
		if(e instanceof MyException){
			renrphException = (MyException)e;
		}else{
			if(e instanceof HttpMessageNotReadableException){
				renrphException = new MyException(CommErrCode.ParamIsInvalid);
			}else if(e instanceof InvalidSessionException){
				renrphException = new MyException(CommErrCode.TokenIsInvalid);				
			}else if(e instanceof UnauthenticatedException){
				renrphException = new MyException(CommErrCode.Unauthenticated);				
			}else if(e instanceof UnauthorizedException){
				renrphException = new MyException(CommErrCode.Unauthorized);				
			}else{
				renrphException = new MyException(CommErrCode.SysErr);
			}
		}
		
		this.code = renrphException.getErrCode();
		this.msg = getErrMsg(this.code,renrphException);
	}
	
	
	/**
	 * 错误提示先读取国际化的文件，读取不到，再使用程序的异常枚举中默认提示
	 * @param errCode
	 * @param renrphException
	 * @return
	 */
	private static String getErrMsg(String errCode, MyException renrphException){
		String returnValue = MsgResources.getMessage(errCode);
		if(returnValue==null){
			returnValue = renrphException.getErrMsg();
		}
		return returnValue;
	}
}
