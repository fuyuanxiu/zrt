package com.web.product.controller;

import java.net.URLDecoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.product.service.ZrtService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(description = "zrt")
@RestController
@RequestMapping(value= "/zrt")
public class ZrtController extends WebController{

	@Autowired
	ZrtService zrtService;
	
	@ApiOperation(value = "执行过程获取信息", notes = "执行过程获取信息")
	@ApiImplicitParam(name = "pstr", value = "执行过程获取信息", paramType = "Query", required = true, dataType = "String")
	@RequestMapping(value = "/getMemo", method = RequestMethod.POST, produces = "application/json")
	public ApiResponseResult  getMemo(@RequestParam(value = "pstr") String pstr) {		
     try {
   	  System.out.println(pstr);
   	 return zrtService.getMemo(pstr);
       } catch (Exception e) {
       	System.out.println(e.toString());
           return ApiResponseResult.failure("执行过程获取信息失败！");
       }
	}
	
	@ApiOperation(value = "输入新条码执行过程获取信息", notes = "输入新条码执行过程获取信息")
	@ApiImplicitParam(name = "pstr", value = "输入新条码执行过程获取信息", paramType = "Query", required = true, dataType = "String")
    @RequestMapping(value = "/getMemoByBarcode", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult getMemoByBarcode(@RequestParam(value = "pstr") String pstr){
        try{
        	pstr = pstr.replaceAll("%", "%25");//替换百分号
        	pstr = URLDecoder.decode(pstr,"UTF-8");
            return zrtService.getMemoByBarcode(pstr);
        }catch (Exception e){
        	System.out.println(e.toString());
            return ApiResponseResult.failure("执行过程获取信息失败！");
        }
    }
}
