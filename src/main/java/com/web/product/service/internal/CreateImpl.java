package com.web.product.service.internal;

import com.app.base.data.ApiResponseResult;
import com.utils.PrcImpl;
import com.web.product.service.CreateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service(value = "CreateService")
@Transactional(propagation = Propagation.REQUIRED)
public class CreateImpl implements CreateService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据设备号获取信息
     * @param device
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult afterDevice(String device) throws Exception {
        if(StringUtils.isEmpty(device)){
            return ApiResponseResult.failure("设备号不能为空!");
        }

        //List<String> resultList = this.doPrc(device, "prc_eq_getproc");
        List<Object> a  = this.getListRf("10000", "1000",device,"App_Peiliao_Shebei_Select");
		
        if(true){
        	//001[M-FA-PE-515,ZJ002-1,TL0001{
            String s = a.get(0).toString();//resultList.get(0).substring(0);
            String[] strs = s.split("\\[");
            if(strs.length < 1){
                return ApiResponseResult.failure("返回值的格式不正确：" + s);
            }
            //判断取值是否成功
            String str = strs[0];
            if(str.equals("002")){
                return ApiResponseResult.failure("系统错误：" + s);
            }
            //封装数据
            Map<String, Object> map = new HashMap<>();
            //map.put("list", a.get(1));
            //20200424-fyx
            String[] strs_1 = strs[1].split(",",-1);
            if(strs_1.length>=2){
            	map.put("station", strs_1[1]);
            }else{
            	map.put("station", "");
            }
            if(strs_1.length>=3){
            	String[] list_strs = strs_1[2].split("\\{");
            	map.put("order", list_strs);
            }else{
            	map.put("order", "");
            }
            
            return ApiResponseResult.success("获取数据成功！").data(map);
        }

        return ApiResponseResult.failure("获取数据失败！");
    }

    /**
     * 根据工位获取信息
     * @param station
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult afterStation(String station) throws Exception{
        if(StringUtils.isEmpty(station)){
            return ApiResponseResult.failure("设备号不能为空!");
        }

        List<String> resultList = this.doPrc(station, "Prc_Check_ProcLine");

        if(resultList.size() > 0){
            String s = resultList.get(0).substring(0);
            String[] strs = s.split("\\[");
            if(strs.length < 1){
                return ApiResponseResult.failure("返回值的格式不正确：" + resultList);
            }
            //判断取值是否成功
            String str = strs[0];
            if(str.equals("002")){
                return ApiResponseResult.failure("系统错误：" + resultList);
            }
            //获取列表
            //例如：001[1015-15,{ZBK2014042103-2
            //String station = "";
            List<String> list = new ArrayList<>();
            if(StringUtils.isEmpty(strs[1])){
                return ApiResponseResult.failure("获取数据失败：" + resultList);
            }
            String[] orderArray = strs[1].split(",");
            if(orderArray.length < 1){
                return ApiResponseResult.failure("获取数据失败：" + resultList);
            }
            if(orderArray.length < 2){
                //station = orderArray[0];
            }else{
                //station = orderArray[0];
                String[] orderArray1 = replaceNull(orderArray[1].split("\\{"));
                if(orderArray1 != null && orderArray1.length > 0){
                    //数据转换
                    list = Arrays.asList(orderArray1);
                }
            }

            //封装数据
            Map<String, Object> map = new HashMap<>();
            //map.put("station", station);
            map.put("list", list);

            return ApiResponseResult.success("获取数据成功！").data(map);
        }


        return null;
    }

    /**
     * 根据工号获取信息
     * @param userCode
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult afterJob(String userCode) throws Exception{
        if(StringUtils.isEmpty(userCode)){
            return ApiResponseResult.failure("工号不能为空!");
        }

        List<String> resultList = this.doPrc(userCode, "Prc_Check_Gonghao");
        if(resultList.size() > 0){
            String s = resultList.get(0).substring(0);
            String[] strs = s.split("\\[");
            if(strs.length < 1){
                return ApiResponseResult.failure("返回值的格式不正确：" + resultList);
            }
            //判断取值是否成功
            String str = strs[0];
            if(str.equals("002")){
                return ApiResponseResult.failure("系统错误：" + resultList);
            }
            //获取信息
            String classes = "";
            if(StringUtils.isEmpty(strs[1])){
                return ApiResponseResult.failure("获取数据失败：" + resultList);
            }
            classes = strs[1];

            //封装数据
            Map<String, Object> map = new HashMap<>();
            map.put("classes", classes);//班次

            return ApiResponseResult.success("获取数据成功！").data(map);
        }

        return ApiResponseResult.failure("获取数据失败！");
    }

    /**
     * 根据工单获取信息
     * @param order
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult afterOrder(String order) throws Exception{
        if(StringUtils.isEmpty(order)){
            return ApiResponseResult.failure("工单不能为空!");
        }

        List<String> resultList = this.doPrc(order, "Prc_Col_Tl01");

        if(resultList.size() > 0){
            String s = resultList.get(0).substring(0);
            String[] strs = s.split("\\[");
            if(strs.length < 1){
                return ApiResponseResult.failure("返回值的格式不正确：" + resultList);
            }
            //判断取值是否成功
            String str = strs[0];
            if(str.equals("002")){
                return ApiResponseResult.failure("系统错误：" + resultList);
            }
            //获取信息
            String planQty = "";
            if(StringUtils.isEmpty(strs[1])){
                return ApiResponseResult.failure("获取数据失败：" + resultList);
            }
            String[] tempArray = strs[1].split(",");
            if(tempArray.length > 0){
                planQty = tempArray[0];
            }

            //封装数据
            Map<String, Object> map = new HashMap<>();
            map.put("planQty", planQty);//计划数量(用“，”隔开)

            return ApiResponseResult.success("获取数据成功！").data(map);
        }

        return ApiResponseResult.failure("获取数据失败！");
    }

    /**
     * 根批量获取信息
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult afterBatch(String param) throws Exception{
        if(StringUtils.isEmpty(param)){
            return ApiResponseResult.failure("批量不能为空!");
        }

        List<Object> a  = this.getListRf("10000", "1000",param,"App_Peiliao_Jiaoban_Add");
		
        if(true){
            String s = a.get(0).toString();//resultList.get(0).substring(0);
            String[] strs = s.split("\\[");
            if(strs.length < 1){
                return ApiResponseResult.failure("返回值的格式不正确：" + s);
            }
            //判断取值是否成功
            String str = strs[0];
            if(str.equals("002")){
                return ApiResponseResult.failure("系统错误：" + s);
            }
            //封装数据
            Map<String, Object> map = new HashMap<>();
            map.put("list", a.get(1));

            return ApiResponseResult.success("获取数据成功！").data(map);
        }

        return ApiResponseResult.failure("获取数据失败！");
    }

    /**
     * 删除
     * @param order
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult delete(String order) throws Exception{
        if(StringUtils.isEmpty(order)){
            return ApiResponseResult.failure("工单不能为空！");
        }

        List<String> resultList = this.doPrc(order, "Prc_Jiaoban_Del");

        if(resultList.size() > 0){
            String s = resultList.get(0).substring(0);
            String[] strs = s.split("\\[");
            if(strs.length < 1){
                return ApiResponseResult.failure("返回值的格式不正确：" + resultList);
            }
            //判断取值是否成功
            String str = strs[0];
            if(str.equals("002")){
                return ApiResponseResult.failure("删除数据失败：" + resultList);
            }

            return ApiResponseResult.success("删除数据成功！");
        }

        return ApiResponseResult.failure("删除数据失败！");
    }
    @Override
	public ApiResponseResult getList(String device, int page) throws Exception {
		// TODO Auto-generated method stub
		List<Object> a  = this.getAllListRf("10000", "1000",device,5,page,"app_peiliao_shebei_selecthis");
		/*if(!a.get(0).toString().equals("0")){
			return ApiResponseResult.failure(a.get(1).toString());
		}*/
		 String s = a.get(0).toString();//resultList.get(0).substring(0);
         String[] strs = s.split("\\[");
         if(strs.length < 1){
             return ApiResponseResult.failure("返回值的格式不正确：" + s);
         }
         //判断取值是否成功
         String str = strs[0];
         if(str.equals("002")){
             return ApiResponseResult.failure("系统错误：" + s);
         }
		
		return ApiResponseResult.success().data(a.get(1));
	}

    //执行存储获取数据
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
    //去除List数组中为空的元素
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
        if(StringUtils.isEmpty(sb)){
            return null;
        }
        str = sb.toString().split(";");
        return str;
    }
    
  //执行存储获取数据
    public List<Object> getListRf(String factory,String company,String in_str,String prc_name) {
		List<Object> resultList = (List<Object>) jdbcTemplate.execute(new CallableStatementCreator() {
		@Override
		public CallableStatement createCallableStatement(Connection con) throws SQLException {
		String storedProc = "{call "+prc_name+"(?,?,?,?,?,?)}";// 调用的sql
		CallableStatement cs = con.prepareCall(storedProc);
		cs.setString(1, factory);
		cs.setString(2, company);
        cs.setString(3, in_str);
		cs.registerOutParameter(4,java.sql.Types.INTEGER);// 注册输出参数 返回类型
		cs.registerOutParameter(5,java.sql.Types.VARCHAR);// 注册输出参数 返回类型
		cs.registerOutParameter(6,-10);// 注册输出参数 返回类型
		return cs;
		}
		}, new CallableStatementCallback() {
		public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
			List<Object> result = new ArrayList<Object>();
			cs.execute();
			result.add(cs.getString(5));
			
			String[] strs = cs.getString(5).split("\\[");
	        //判断取值是否成功
	        String str = strs[0];
	        if(str.equals("001")){
	        	//游标处理
	    		ResultSet rs = (ResultSet)cs.getObject(6);
	    		List l = new ArrayList();
	
	    		while(rs.next()){
	    			Map m = new HashMap();	
	    			m.put("MID", rs.getString("MID"));
	    			m.put("ID", rs.getString("ID"));
	    			m.put("TASK_NO", rs.getString("TASK_NO"));
	    			m.put("PLAN_QTY", rs.getString("PLAN_QTY"));
	    			m.put("FPUT_QTY", rs.getString("FPUT_QTY"));
	    			l.add(m);
	    			 }
	    		result.add(l);
	        }
	
			return result;
			}
		});
		return resultList;
		}
    
  //执行存储获取数据
    public List<Object> getAllListRf(String factory,String company,String in_str,int page,int size,String prc_name) {
		List<Object> resultList = (List<Object>) jdbcTemplate.execute(new CallableStatementCreator() {
		@Override
		public CallableStatement createCallableStatement(Connection con) throws SQLException {
		String storedProc = "{call "+prc_name+"(?,?,?,?,?,?,?,?)}";// 调用的sql
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
			result.add(cs.getString(7));
			
			String[] strs = cs.getString(7).split("\\[");
	        //判断取值是否成功
	        String str = strs[0];
	        if(str.equals("001")){
	        	//游标处理
	    		ResultSet rs = (ResultSet)cs.getObject(8);
	    		List l = new ArrayList();
	
	    		while(rs.next()){
	    			Map m = new HashMap();	
	    			m.put("MID", rs.getString("MID"));
	    			m.put("ID", rs.getString("ID"));
	    			m.put("TASK_NO", rs.getString("TASK_NO"));
	    			m.put("PLAN_QTY", rs.getString("PLAN_QTY"));
	    			m.put("FPUT_QTY", rs.getString("FPUT_QTY"));
	    			m.put("CREATE_DATE", rs.getString("CREATE_DATE"));
	    			m.put("CREATE_BY", rs.getString("CREATE_BY"));
	    			l.add(m);
	    			 }
	    		result.add(l);
	        }
	
			return result;
			}
		});
		return resultList;
		}

}
