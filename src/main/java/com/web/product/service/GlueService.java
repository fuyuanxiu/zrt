package com.web.product.service;

import com.app.base.data.ApiResponseResult;

public interface GlueService {

    //根据设备号获取信息
    public ApiResponseResult afterDevice(String device) throws Exception;

    //根据工位获取信息
    public ApiResponseResult afterStation(String station) throws Exception;

    //根据工号获取信息
    public ApiResponseResult afterJob(String userCode) throws Exception;

    //根据工单获取信息
    public ApiResponseResult afterOrder(String order) throws Exception;

    //根批量获取信息
    public ApiResponseResult afterBatch(String param) throws Exception;

    //删除
    public ApiResponseResult delete(String order) throws Exception;

    //获取删除列表
    public ApiResponseResult getDelList(String device) throws Exception;
    
  //根据投料列表
    public ApiResponseResult getList(String device,int page) throws Exception;
}
