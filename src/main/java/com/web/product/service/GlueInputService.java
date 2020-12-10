package com.web.product.service;

import com.app.base.data.ApiResponseResult;

public interface GlueInputService {

    //根据设备号获取信息
    public ApiResponseResult afterDevice(String deviceNo) throws Exception;

    //根据物流条码获取信息
    public ApiResponseResult afterBarcoe(String barcode) throws Exception;

    //根据数量获取信息
    public ApiResponseResult afterQty(String param) throws Exception;

    //根据投料列表
    public ApiResponseResult getList(String device,int page) throws Exception;

    //删除
    public ApiResponseResult delete(String order) throws Exception;
    
    public ApiResponseResult getDetailList(String pid) throws Exception;
}
