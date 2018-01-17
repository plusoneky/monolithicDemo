package com.qq1833111108.sys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
	
    @RequestMapping(value = "/test1")
    public String index(Model model) {
        return "testajax/test1";
    }

}
