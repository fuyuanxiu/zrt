package com.web.product.service;

import com.app.base.data.ApiResponseResult;

public interface ZrtService {

	//打开界面、输入线体、输入换料站位执行过程
	public ApiResponseResult getMemo(String pstr)throws Exception;
	
	//输入新条码执行过程
	public ApiResponseResult getMemoByBarcode(String pstr)throws Exception;
	
}
