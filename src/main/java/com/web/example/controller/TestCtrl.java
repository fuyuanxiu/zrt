package com.web.example.controller;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/example")
@Api("测试api")
public class TestCtrl {

	   // private static final Logger LOGGER = LoggerFactory.getLogger(TestCtrl.class);
	 
	    @ApiOperation(value="获取用户详细信息", notes="根据url的id来获取用户详细信息")
	    @GetMapping("/test")
	    public String test(String message) throws IOException {
	        return "OK" + new Date();
	    }
	    
	    @RequestMapping("/Hi")
	    public String sayHello() {
	    	return "swagger-ui.html"; 
	    }

}
