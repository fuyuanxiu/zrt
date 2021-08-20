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
import com.web.product.service.ZrtService;

@Service(value = "ZrtService")
@Transactional(propagation = Propagation.REQUIRED)
public class Zrtlmpl implements ZrtService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 打开界面、输入线体、输入换料站位执行过程
	 * **/
	public ApiResponseResult getMemo(String pstr) throws Exception {
		List<String> a = this.getReturnValue(pstr,"PRC_RF_GET_RETURN_VALUE");
		if (a.size() > 0) {
			String s = a.get(0).substring(0);
			String[] strs = s.split("\\[");
			if (strs.length < 1) {
				return ApiResponseResult.failure("返回值的格式不正确!" + a);
			}
			// 判断取值是否成功
			String str = strs[0];
			if (str.equals("002")) {
				return ApiResponseResult.failure("取值发生错误!" + a);
			}
			// 拼接字符串

			List<String> list = Arrays.asList(strs);
			List arr = new ArrayList<>();
			for (int i = 1; i < list.size(); i++) {
				//String[] s1 = list.get(i).split("#");
				String s1 = list.get(i).toString();
				arr.add(s1);
			}
			return ApiResponseResult.success("取值成功！").data(arr);
		} else {
			return ApiResponseResult.failure("取值为空，请检测输入的参数是否正确!");
		}
	}
	/**
	 * 输入新条码执行过程
	 * **/
	public ApiResponseResult getMemoByBarcode(String pstr) throws Exception {
		List<String> a = this.getReturnValue(pstr,"Prc_Rf_Qc_MacCItem");
		if (a.size() > 0) {
			String s = a.get(0).substring(0);
			String[] strs = s.split("\\[");
			if (strs.length < 1) {
				return ApiResponseResult.failure("返回值的格式不正确!" + a);
			}
			// 判断取值是否成功
			String str = strs[0];
			if (str.equals("002")) {
				return ApiResponseResult.failure("取值发生错误!" + a);
			}
			// 拼接字符串

			List<String> list = Arrays.asList(strs);
			List arr = new ArrayList<>();
			for (int i = 1; i < list.size(); i++) {
				//String[] s1 = list.get(i).split("#");
				String s1 = list.get(i).toString();
				arr.add(s1);
			}
			return ApiResponseResult.success("取值成功！").data(arr);
		} else {
			return ApiResponseResult.failure("取值为空，请检测输入的参数是否正确!");
		}
	}
	
	public List<String> getReturnValue(String functionName,String prc_name) {
		List<String> resultList = (List<String>) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call "+prc_name+"(?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, functionName);
				cs.registerOutParameter(2, java.sql.Types.VARCHAR);// 注册输出参数
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
}
