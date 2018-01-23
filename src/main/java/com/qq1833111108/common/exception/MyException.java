package com.qq1833111108.common.exception;

public class MyException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 822524209672412825L;

	private String errCode;
	
	private String errMsg;
	
	

	public String getErrCode() {
		return errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	/**
	 * 使用自定义异常枚举中的错误提示
	 * @param errObj
	 */
	public MyException(IErrCode errObj)  
    {  
        super(errObj.getErrMsg());  
		this.errCode = errObj.getErrCode();
		this.errMsg = errObj.getErrMsg();
    }
	
	
	/**
	 * 使用自定义异常枚举中的错误提示，和传入的错误提示相结合
	 * @param errObj
	 * @param args
	 */
	public MyException(IErrCode errObj,String templateStr, Object ... args)  
    {  
        super(errObj.getErrMsg());  
		this.errCode = errObj.getErrCode();
		this.errMsg = String.format(errObj.getErrMsg()+templateStr, args);
    }
	
    @Override
    public String toString() {
        return "errCode=" + this.errCode + ",errMsg=" + this.errMsg;
    }
	
	public enum CommErrCode implements IErrCode{
		CommParamIsMissing("ParamIsMissing","缺少必要参数"),
	    ParamIsInvalid("ParamIsInvalid","参数格式无效"),
	    ParamTyepConvertError("ParamTyepConvertError","参数类型转换异常:%s"),
	    TokenIsInvalid("TokenIsInvalid","Token无效"),
	    TokenIsExpired("TokenIsExpired","Token已过期"),
	    Unauthenticated("Unauthenticated","未通过身份认证"),
	    Unauthorized("Unauthorized","未通过权限认证"),
	    Forbidden("Forbidden","未通过权限认证"),
	    BadReqParam("BadReqParam","请求参数错误"),
	    NotFound("NotFound","请求的资源不存在"),
	    InternalError("InternalError","服务器内部错误"),
	    InvalidSession("InvalidSession","会话已失效，请重新登录"),
	    UnknownDataSource("UnknownRRenPHDataSource","后台系统异常"),
	    SysIoErr("SysIoErr","系统网络异常"),
	    SysErr("SysErr","后台系统异常"),
	    NotSurportedAppVersion("NotSurportedAppVersion","该版本暂不支持此功能操作，请关注新版本更新！"),
	    KickOutByAnotherLogin("KickOutByAnotherLogin","您已经在其他地方登录，请重新登录！"),
	    ;
		
		private String errCode;
		private String errMsg;

	    private CommErrCode(String errCode, String errMsg) {
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
	
	
	

	public static void main(String[] args) {
		RuntimeException e1 = new MyException(CommErrCode.TokenIsInvalid);
		e1.printStackTrace();
		
		RuntimeException e2 = new MyException(CommErrCode.TokenIsInvalid,"：%s？%s！%s？是的","这是哪里来的异常提示信息","是自定义的提示","可以用于透传第三方的错误提示");
		e2.printStackTrace();
	}

}
