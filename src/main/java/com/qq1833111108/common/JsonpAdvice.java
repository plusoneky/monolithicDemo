package com.qq1833111108.common;

import org.springframework.web.bind.annotation.ControllerAdvice;  
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;  
  

@ControllerAdvice //(basePackages = "com.qq1833111108.biz.controller")  
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice{  
  
    public JsonpAdvice() {  
        super("callback","jsonp");  
    }  
} 
