package com.web.product.service;

import com.app.base.data.ApiResponseResult;

public interface OutPutService {

    //根据设备号获取信息
    public ApiResponseResult afterDevice(String deviceNo) throws Exception;
    
    public ApiResponseResult afterDeviceWinding(String deviceNo) throws Exception;
    
  //根据工号获取信息
    public ApiResponseResult afterCode(String param) throws Exception;
    
  //根据工号获取信息
    public ApiResponseResult afterOrder(String param) throws Exception;

    //根据物流条码获取信息
    public ApiResponseResult afterBarcoe(String barcode) throws Exception;

    //根据数量获取信息
    public ApiResponseResult afterQty(String param) throws Exception;
    
  //根据物流条码获取信息--装配投料
    public ApiResponseResult afterBarcoeZP(String barcode) throws Exception;
}
