package com.web.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.product.service.GlueService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "创建制胶作业")
@RestController
@RequestMapping(value= "/glue")
public class GlueController extends WebController {
    @Autowired
    private GlueService glueService;

    @ApiOperation(value = "根据设备号获取信息", notes = "根据设备号获取信息")
    @RequestMapping(value = "/afterDevice", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult afterDevice(@RequestParam(value = "device") String device){
        try{
            return glueService.afterDevice(device);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }

    @ApiOperation(value = "根据工位获取信息", notes = "根据工位获取信息")
    @RequestMapping(value = "/afterStation", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult afterStation(String station) throws Exception{
        try{
            return glueService.afterStation(station);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }

    @ApiOperation(value = "根据工号获取信息", notes = "根据工号获取信息")
    @RequestMapping(value = "/afterJob", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult afterJob(String userCode) throws Exception{
        try{
            return glueService.afterJob(userCode);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }

    @ApiOperation(value = "根据配方获取信息", notes = "根据配方获取信息")
    @RequestMapping(value = "/afterOrder", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult afterOrder(String order) throws Exception{
        try{
            return glueService.afterOrder(order);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }

    @ApiOperation(value = "根据批量获取信息", notes = "根据批量获取信息")
    @RequestMapping(value = "/afterBatch", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult afterBatch(String param) throws Exception{
        try{
            return glueService.afterBatch(param);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }

    @ApiOperation(value = "删除工单", notes = "删除工单")
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult delete(String order) throws Exception{
        try{
            return glueService.delete(order);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }

    @ApiOperation(value = "获取删除列表", notes = "获取删除列表")
    @RequestMapping(value = "/getDelList", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult getDelList(String device) throws Exception{
        try{
            return glueService.getDelList(device);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }

    @ApiOperation(value = "获取制胶列表", notes = "获取制胶列表")
    @RequestMapping(value = "/getList", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult getList(String device,int page) throws Exception{
        try{
            return glueService.getList(device,page);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }
}
