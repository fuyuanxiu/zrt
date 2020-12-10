package com.web.product.service.internal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.web.product.service.OutPutService;

@Service(value = "outPutService")
@Transactional(propagation = Propagation.REQUIRED)
public class OutPutImpl  implements OutPutService {

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
    /**
     * 根据设备号获取信息
     * @param deviceNo
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult afterDevice(String deviceNo) throws Exception {
        if(StringUtils.isEmpty(deviceNo)){
            return ApiResponseResult.failure("设备号不能为空!");
        }
        List<String> resultList = this.doPrc(deviceNo,"prc_eq_getproc1");
        System.out.println(resultList);//001[1011-11,{ZBK2014042103-2{ZBK2014042103-2,1,admin,20000,
        if(resultList.size() > 0){
            String s = resultList.get(0).substring(0);
            String[] strs = s.split("\\[");
            if(strs.length<1){
                return ApiResponseResult.failure("返回值的格式不正确!"+resultList);
            }
            //判断取值是否成功
            String str = strs[0];
            if(str.equals("002")){
                return ApiResponseResult.failure(resultList+"");
            }
            //获取列表
            if(strs.length <1 ){
            	return ApiResponseResult.failure("长度解析发生错误:"+resultList);
            }
            String[] list_strs = strs[1].split(",",-1);
          //封装数据
            Map<String, Object> map = new HashMap<>();
            if(list_strs.length > 4){
            	map.put("station", list_strs[0]);
            	map.put("classes", list_strs[2]);
            	map.put("code", list_strs[3]);
            	map.put("tqry", list_strs[4]);
            	map.put("pqry", list_strs[5]);
            	List arr = new ArrayList<>();
            	String[] s1 = replaceNull(list_strs[1].split("\\{"));
				map.put("list", Arrays.asList(s1));
            }else{
            	return ApiResponseResult.failure("返回参数格式不对:"+resultList);
            }
            return ApiResponseResult.success("获取数据成功！").data(map);
        }
        return ApiResponseResult.failure("没有数据！");
    }
    
    /**
     * 根据设备号获取信息
     * @param deviceNo
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult afterDeviceWinding(String deviceNo) throws Exception {
        if(StringUtils.isEmpty(deviceNo)){
            return ApiResponseResult.failure("设备号不能为空!");
        }
        List<String> resultList = this.doPrc(deviceNo,"prc_eq_getproc");
        System.out.println(resultList);//001[设备信息 ,
        if(resultList.size() > 0){
            String s = resultList.get(0).substring(0);
            String[] strs = s.split("\\[");
            if(strs.length<1){
                return ApiResponseResult.failure("返回值的格式不正确!"+resultList);
            }
            //判断取值是否成功
            String str = strs[0];
            if(str.equals("002")){
                return ApiResponseResult.failure(resultList+"");
            }
            //获取列表
            if(strs.length <1 ){
            	return ApiResponseResult.failure("长度解析发生错误:"+resultList);
            }
            
            return ApiResponseResult.success("获取数据成功！");
        }
        return ApiResponseResult.failure("没有数据！");
    }
    private String[] replaceNull(String[] str){
        //用StringBuffer来存放数组中的非空元素，用“;”分隔
           StringBuffer sb = new StringBuffer();
           for(int i=0; i<str.length; i++) {
               if("".equals(str[i])) {
                   continue;
               }
               sb.append(str[i]);
               if(i != str.length - 1) {
                   sb.append(";");
               }
           }
           //用String的split方法分割，得到数组
           str = sb.toString().split(";");
           return str;
    }
    
    @Override
	public ApiResponseResult afterCode(String param) throws Exception {
		// TODO Auto-generated method stub
    	if(StringUtils.isEmpty(param)){
            return ApiResponseResult.failure("工号不能为空!");
        }
        List<String> resultList = this.doPrc(param,"Prc_Check_Gonghao");
        System.out.println(resultList);//
        if(resultList.size() > 0){
            String s = resultList.get(0).substring(0);
            String[] strs = s.split("\\[");
            if(strs.length<1){
                return ApiResponseResult.failure("返回值的格式不正确!"+resultList);
            }
           //判断取值是否成功
            String str = strs[0];
            if(str.equals("002")){
                return ApiResponseResult.failure(resultList+"");
            }
            return ApiResponseResult.success().data(strs[1]);
        }
		return null;
	}
    
    @Override
	public ApiResponseResult afterOrder(String param) throws Exception {
		// TODO Auto-generated method stub
    	if(StringUtils.isEmpty(param)){
            return ApiResponseResult.failure("单号不能为空!");
        }
        List<String> resultList = this.doPrc(param,"Prc_Col_Tl01");
        System.out.println(resultList);//
        if(resultList.size() > 0){
            String s = resultList.get(0).substring(0);
            String[] strs = s.split("\\[");
            if(strs.length<1){
                return ApiResponseResult.failure("返回值的格式不正确!"+resultList);
            }
           //判断取值是否成功
            String str = strs[0];
            if(str.equals("002")){
                return ApiResponseResult.failure(resultList+"");
            }
            return ApiResponseResult.success().data(Arrays.asList(strs[1].split(",",-1)));
        }
		return null;
	}

    /**
     * 根据设备号获取信息
     * @param barcode
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult afterBarcoe(String barcode) throws Exception{
    	if(StringUtils.isEmpty(barcode)){
            return ApiResponseResult.failure("物料号不能为空!");
        }
        List<String> resultList = this.doPrc(barcode,"Prc_Check_Maratb");
        System.out.println(resultList);
        if(resultList.size() > 0){
            String s = resultList.get(0).substring(0);
            String[] strs = s.split("\\[");
            if(strs.length<1){
                return ApiResponseResult.failure("返回值的格式不正确!"+resultList);
            }
            //判断取值是否成功
            String str = strs[0];
            if(str.equals("002")){
                return ApiResponseResult.failure(resultList+"");
            }
            return ApiResponseResult.success().data(Arrays.asList(strs[1].split(",",-1)));
        	
        }
        return ApiResponseResult.failure("没有数据！");
    }


    /**
     * 根据工单获取信息
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult afterQty(String param) throws Exception{
    	//模块名称[字段名[登录帐号[工位[工号[班次[工单号[批量[设备[物料[用量
    	//工单号[批量为空
    	if(StringUtils.isEmpty(param)){
            return ApiResponseResult.failure("数量不能为空!");
        }
        List<String> resultList = this.doPrc(param,"prc_tubu_put");
        System.out.println(resultList);
        if(resultList.size() > 0){
        	String s = resultList.get(0).substring(0);
            String[] strs = s.split("\\[");
            if(strs.length<1){
                return ApiResponseResult.failure("返回值的格式不正确!"+resultList);
            }
            //判断取值是否成功
            String str = strs[0];
            if(str.equals("002")){
                return ApiResponseResult.failure(resultList+"");
            }
            return ApiResponseResult.success().data(strs[1]);
        }
        
        return ApiResponseResult.failure("没有数据！");
    }

	
	 public List<String> doPrc(String param,String prc_name){
	        List<String> resultList = (List<String>) jdbcTemplate.execute(new CallableStatementCreator() {
	            @Override
	            public CallableStatement createCallableStatement(Connection con) throws SQLException {
	                String storedProc = "{call "+prc_name+"(?,?)}";// 调用的sql
	                CallableStatement cs = con.prepareCall(storedProc);
	                cs.setString(1, param);
	                cs.registerOutParameter(2,java.sql.Types.VARCHAR);// 注册输出参数 返回类型
	                return cs;
	            }
	        }, new CallableStatementCallback() {
	            public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
	                List<String> result = new ArrayList<String>();
	                cs.execute();
	                result.add(cs.getString(2));
	                return result;
	            }
	        });

	        return resultList;
	    }

	@Override
	public ApiResponseResult afterBarcoeZP(String barcode) throws Exception {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(barcode)){
            return ApiResponseResult.failure("物料号不能为空!");
        }
        List<String> resultList = this.doPrc(barcode,"Prc_Check_Marazp");
        System.out.println(resultList);
        if(resultList.size() > 0){
            String s = resultList.get(0).substring(0);
            String[] strs = s.split("\\[");
            if(strs.length<1){
                return ApiResponseResult.failure("返回值的格式不正确!"+resultList);
            }
            //判断取值是否成功
            String str = strs[0];
            if(str.equals("002")){
                return ApiResponseResult.failure(resultList+"");
            }
            return ApiResponseResult.success().data(Arrays.asList(strs[1].split(",",-1)));
        	
        }
        return ApiResponseResult.failure("没有数据！");
	}
	


}
