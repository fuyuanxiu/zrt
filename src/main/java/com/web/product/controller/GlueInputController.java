package com.web.product.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.product.service.GlueInputService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "制胶投料")
@RestController
@RequestMapping(value= "/glueinput")
public class GlueInputController extends WebController {

    @Autowired
    private GlueInputService glueInputService;

    @ApiOperation(value = "根据设备号获取信息", notes = "根据设备号获取信息")
    @RequestMapping(value = "/afterDevice", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult afterDevice(@RequestParam(value = "device") String device){
        try{
            return glueInputService.afterDevice(device);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }

    @ApiOperation(value = "根据物流条码获取信息", notes = "根据物流条码获取信息")
    @RequestMapping(value = "/afterBarcoe", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult afterBarcoe(String barcode) throws Exception{
        try{
            return glueInputService.afterBarcoe(barcode);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }

    @ApiOperation(value = "根据数量获取信息", notes = "根据数量获取信息")
    @RequestMapping(value = "/afterQty", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult afterQty(String param) throws Exception{
        try{
            return glueInputService.afterQty(param);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }

    @ApiOperation(value = "获取投料列表", notes = "获取投料列表")
    @RequestMapping(value = "/getList", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult getList(String device,int page) throws Exception{
        try{
            return glueInputService.getList(device,page);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }
    
    @ApiOperation(value = "获取投料列表", notes = "获取投料列表")
    @RequestMapping(value = "/getDetailList", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult getDetailList(String pid) throws Exception{
        try{
            return glueInputService.getDetailList(pid);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }
    
    @ApiOperation(value = "删除工单", notes = "删除工单")
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult delete(String pid) throws Exception{
        try{
            return glueInputService.delete(pid);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }
}
