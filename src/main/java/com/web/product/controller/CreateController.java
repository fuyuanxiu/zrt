package com.web.product.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.product.service.CreateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "创建配料作业")
@RestController
@RequestMapping(value= "/create")
public class CreateController extends WebController {
    @Autowired
    private CreateService createService;

    @ApiOperation(value = "根据设备号获取信息", notes = "根据设备号获取信息")
    @RequestMapping(value = "/afterDevice", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult afterDevice(@RequestParam(value = "device") String device){
        try{
            return createService.afterDevice(device);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }

    @ApiOperation(value = "根据工位获取信息", notes = "根据工位获取信息")
    @RequestMapping(value = "/afterStation", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult afterStation(String station) throws Exception{
        try{
            return createService.afterStation(station);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }

    @ApiOperation(value = "根据工号获取信息", notes = "根据工号获取信息")
    @RequestMapping(value = "/afterJob", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult afterJob(String userCode) throws Exception{
        try{
            return createService.afterJob(userCode);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }

    @ApiOperation(value = "根据工单获取信息", notes = "根据工单获取信息")
    @RequestMapping(value = "/afterOrder", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult afterOrder(String order) throws Exception{
        try{
            return createService.afterOrder(order);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }

    @ApiOperation(value = "根据批量获取信息", notes = "根据批量获取信息")
    @RequestMapping(value = "/afterBatch", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult afterBatch(String param) throws Exception{
        try{
            return createService.afterBatch(param);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }

    @ApiOperation(value = "删除工单", notes = "删除工单")
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult delete(String order) throws Exception{
        try{
            return createService.delete(order);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }
    @ApiOperation(value = "获取投料列表", notes = "获取投料列表")
    @RequestMapping(value = "/getList", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult getList(String device,int page) throws Exception{
        try{
            return createService.getList(device,page);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }
}
