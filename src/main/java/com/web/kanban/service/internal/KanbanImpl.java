package com.web.kanban.service.internal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.web.kanban.KanbanDao;
import com.web.kanban.service.KanbanService;
import com.web.report.service.internal.ImpUtils;

@Service(value = "KanbanService")
@Transactional(propagation = Propagation.REQUIRED)
public class KanbanImpl  implements KanbanService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
	KanbanDao kanbanDao;
	
	//获取线体数据源
	@Override
	public ApiResponseResult getLineList()throws Exception{
		return ApiResponseResult.success().data(kanbanDao.getLineList());
	}
	
	//获取SMT看板数据
	@Override
	public ApiResponseResult getSmtKanbanData(String lineNo) throws Exception {
		Map map = new HashMap();
		for(int i=1;i<=5;i++){
			List<Object> list = getKanbanDataPrc("PRC_GET_NEW_KB",String.valueOf(i),lineNo);
			if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
				map.put("DATA_"+i, list.get(1).toString());
			} else {
				map.put("DATA_"+i, list.get(2));
			}
		}
		return ApiResponseResult.success().data(map);
	}
	

	public List getKanbanDataPrc(String prcName,String ftype,String lineNo) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  " + prcName + "(?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, ftype);
				cs.setString(2, lineNo);
				cs.registerOutParameter(3, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(4, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(5, -10);// 输出参数 追溯数据
				return cs;
			}
		}, new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				List<Object> result = new ArrayList<>();
				List<Map<String, Object>> l = new ArrayList();
				cs.execute();
				result.add(cs.getInt(3));
				result.add(cs.getString(4));
				if (cs.getString(3).toString().equals("0")) {
					// 游标处理
					ResultSet rs = (ResultSet) cs.getObject(5);
					try {
						l = fitMap(rs);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					result.add(l);
				}
				System.out.println(l);
				return result;
			}
		});
		return resultList;
	}

	private List<Map<String, Object>> fitMap(ResultSet rs) throws Exception {
		List<Map<String, Object>> list = new ArrayList<>();
		if (null != rs) {
			Map<String, Object> map;
			int colNum = rs.getMetaData().getColumnCount();
			List<String> columnNames = new ArrayList<String>();
			for (int i = 1; i <= colNum; i++) {
				columnNames.add(rs.getMetaData().getColumnName(i));
			}
			while (rs.next()) {
				map = new HashMap<String, Object>();
				for (String columnName : columnNames) {
					map.put(columnName, rs.getString(columnName));
				}
				list.add(map);
			}
		}
		return list;
	}

}
