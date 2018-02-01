package com.qq183311108.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;

import com.qq183311108.common.dto.ApiResDto;


//@ControllerAdvice
public class DefaultExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);
    /**
     * 
     */
    //@ExceptionHandler({Exception.class})
    //@ResponseStatus(HttpStatus.OK)
    public ApiResDto processUnauthenticatedException(NativeWebRequest request, Exception e) {
    	if(e instanceof MyException){
    		MyException ex = (MyException)e;
			logger.debug("MyException: request="+request.toString()+", errCode="+ex.getErrCode());	
		}else{
			logger.error("Exception: request="+request.toString(),e);			
		}
		return new ApiResDto(e);
    }
}
