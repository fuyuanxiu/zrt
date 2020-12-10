package com.web.product.service.internal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
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
import com.utils.PrcImpl;
import com.web.product.service.LittleInputService;

@Service(value = "littleInputService")
@Transactional(propagation = Propagation.REQUIRED)
public class LittleInputImpl  implements LittleInputService {

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
        List<String> resultList = this.doPrc(deviceNo,"Prc_Jiaoban_eq");
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
            //获取列表
            if(strs.length <1 ){
            	return ApiResponseResult.failure("长度解析发生错误:"+resultList);
            }
            List arr = new ArrayList<>();
            String[] list_strs = strs[1].split("‖");
            for(int i=0;i<list_strs.length-1;i++){
            	String[] map_strs = list_strs[i].split("＠");
            	if(map_strs.length > 1){
            		Map map = new HashMap();
                	map.put("order", map_strs[0]);
                	map.put("qty", map_strs[1]);
                	map.put("pqty", map_strs[2]);
                	arr.add(map);
            	}
            }

            //封装数据
            Map<String, Object> map = new HashMap<>();
            map.put("list", arr);
            if(list_strs.length >0){
            	String[] last = list_strs[list_strs.length-1].split("＠",-1);
            	map.put("station", last[0]);
            	map.put("code", last[1]);
            	map.put("classes", last[2]);
            	map.put("remark", last[3]);
            }

            return ApiResponseResult.success("获取数据成功！").data(map);
        }

        return ApiResponseResult.failure("没有数据！");
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
        List<String> resultList = this.doPrc(barcode,"Prc_Zhijiao_Mara");
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
        List<String> resultList = this.doPrc(param,"Prc_Zhijiang_Put");
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
	public ApiResponseResult getList(String device, int page) throws Exception {
		// TODO Auto-generated method stub
		List<Object> a  = this.getListRf("10000", "1000",device,5,page);
		if(!a.get(0).toString().equals("0")){
			return ApiResponseResult.failure(a.get(1).toString());
		}
		
		return ApiResponseResult.success().data(a.get(2));
	}
	
	public List<Object> getListRf(String factory,String company,String in_str,int page,int size) {
		List<Object> resultList = (List<Object>) jdbcTemplate.execute(new CallableStatementCreator() {
		@Override
		public CallableStatement createCallableStatement(Connection con) throws SQLException {
		String storedProc = "{call app_prc_zhijiang_put(?,?,?,?,?,?,?,?)}";// 调用的sql
		CallableStatement cs = con.prepareCall(storedProc);
		cs.setString(1, factory);
		cs.setString(2, company);
        cs.setString(3, in_str);
        cs.setInt(4, page);
        cs.setInt(5, size);
		cs.registerOutParameter(6,java.sql.Types.INTEGER);// 注册输出参数 返回类型
		cs.registerOutParameter(7,java.sql.Types.VARCHAR);// 注册输出参数 返回类型
		cs.registerOutParameter(8,-10);// 注册输出参数 返回类型
		return cs;
		}
		}, new CallableStatementCallback() {
		public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
		List<Object> result = new ArrayList<Object>();
		cs.execute();
		result.add(cs.getString(6));
		result.add(cs.getString(7));
		if(cs.getString(6).toString().endsWith("0")){
			//游标处理
			ResultSet rs = (ResultSet)cs.getObject(8);
			List l = new ArrayList();

			while(rs.next()){
				Map m = new HashMap();	
				m.put("ITEM_BARCODE", rs.getString("ITEM_BARCODE"));
				m.put("ITEM_NO", rs.getString("ITEM_NO"));
				m.put("ITEM_NAME", rs.getString("ITEM_NAME"));
				m.put("ITEM_MODEL", rs.getString("ITEM_MODEL"));
				m.put("QUANTITY", rs.getString("QUANTITY"));
				m.put("ITEM_UNIT", rs.getString("ITEM_UNIT"));
				m.put("CREATE_DATE", rs.getString("CREATE_DATE"));
				m.put("ID", rs.getString("ID"));
				l.add(m);
				 }
			result.add(l);
		}
		

		return result;
		}
		});
		return resultList;

		}

	@Override
	public ApiResponseResult delete(String pid) throws Exception {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(pid)){
            return ApiResponseResult.failure("ID不能为空!");
        }
		List<String> a = this.doDeletePrc("10000", "1000", Integer.parseInt(pid));
		if(a.get(0).equals("1")){
			return ApiResponseResult.failure(a.get(1));
		}
		
		return ApiResponseResult.success(a.get(1));
	}
	 public List<String> doDeletePrc(String factory,String company,int pid){
	        List<String> resultList = (List<String>) jdbcTemplate.execute(new CallableStatementCreator() {
	            @Override
	            public CallableStatement createCallableStatement(Connection con) throws SQLException {
	                String storedProc = "{call Prc_Batch_Del_Barcode_peiliao(?,?,?,?,?)}";// 调用的sql
	                CallableStatement cs = con.prepareCall(storedProc);
	                cs.setString(1, factory);
	                cs.setString(2, company);
	                cs.setInt(3, pid);
	                cs.registerOutParameter(4,java.sql.Types.INTEGER);// 注册输出参数 返回类型
	                cs.registerOutParameter(5,java.sql.Types.VARCHAR);// 注册输出参数 返回类型
	                return cs;
	            }
	        }, new CallableStatementCallback() {
	            public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
	                List<String> result = new ArrayList<String>();
	                cs.execute();
	                result.add(cs.getString(4));
	                result.add(cs.getString(5));
	                return result;
	            }
	        });

	        return resultList;
	    }


}
