package com.web.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.product.service.OutPutService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "生产投料")
@RestController
@RequestMapping(value= "/out")
public class OutPutController extends WebController {
    @Autowired
    private OutPutService outPutService;

    @ApiOperation(value = "根据设备号获取信息", notes = "根据设备号获取信息")
    @RequestMapping(value = "/afterDevice", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult afterDevice(@RequestParam(value = "device") String device){
        try{
            return outPutService.afterDevice(device);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }
    
    @ApiOperation(value = "根据设备号获取信息_卷绕投料", notes = "根据设备号获取信息")
    @RequestMapping(value = "/afterDeviceWinding", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult afterDeviceWinding(@RequestParam(value = "device") String device){
        try{
            return outPutService.afterDeviceWinding(device);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }

    @ApiOperation(value = "根据工号获取信息", notes = "根据工号获取信息")
    @RequestMapping(value = "/afterCode", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult afterCode(String param) throws Exception{
        try{
            return outPutService.afterCode(param);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }
    @ApiOperation(value = "根据单号获取信息", notes = "根据单号获取信息")
    @RequestMapping(value = "/afterOrder", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult afterOrder(String param) throws Exception{
        try{
            return outPutService.afterOrder(param);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }
    
    @ApiOperation(value = "根据物流条码获取信息", notes = "根据物流条码获取信息")
    @RequestMapping(value = "/afterBarcoe", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult afterBarcoe(String barcode) throws Exception{
        try{
            return outPutService.afterBarcoe(barcode);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }

    @ApiOperation(value = "根据数量获取信息", notes = "根据数量获取信息")
    @RequestMapping(value = "/afterQty", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult afterQty(String param) throws Exception{
        try{
            return outPutService.afterQty(param);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }
    
    @ApiOperation(value = "根据物流条码获取信息-装配投料", notes = "根据物流条码获取信息-装配投料")
    @RequestMapping(value = "/afterBarcoeZP", method = RequestMethod.POST, produces = "application/json")
    public ApiResponseResult afterBarcoeZP(String barcode) throws Exception{
        try{
            return outPutService.afterBarcoeZP(barcode);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure(e.toString());
        }
    }
}
