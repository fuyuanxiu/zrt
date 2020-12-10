package com.system.permission.service.internal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.base.data.ApiResponseResult;
import com.google.common.collect.Lists;
import com.system.permission.dao.SysPermissionDao;
import com.system.permission.entity.SysPermission;
import com.system.permission.service.SysPermissionService;

import io.swagger.annotations.ApiOperation;

/**
 * 菜单
 */
@Service(value = "SysPermissionService")
@Transactional(propagation = Propagation.REQUIRED)
public class SysPermissionImpl implements SysPermissionService {

    @Autowired
    private SysPermissionDao sysPermissionDao;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

	@Override
	public List<SysPermission> permList() {
		// TODO Auto-generated method stub
		/*Iterable<SysPermission> geted = sysPermissionDao.findAll();
		List<SysPermission> list =  Lists.newArrayList(geted);
		return list;*/
		return sysPermissionDao.findByIsDel(0);
	}

	@Override
	public ApiResponseResult delete(Long id) throws Exception {
		// TODO Auto-generated method stub
		SysPermission o = sysPermissionDao.findByIdAndIsDel((long)id,0);
        if (null == o) {
            return ApiResponseResult.failure("记录ID不存在或已被删除").status("error1");
        }
		
		List<SysPermission> list = sysPermissionDao.findByIsDelAndParentId(0, id);
		if(list.size() >0){
			return  ApiResponseResult.failure("删除失败，请您先删除该权限的子节点");
		}

        o.setIsDel(1);
        o.setCreatedTime(new Date());
        o.setModifiedTime(new Date());
        sysPermissionDao.save(o);
        return ApiResponseResult.success("删除成功！");

	}

	@Override
	public ApiResponseResult getPermission(Long id) throws Exception {
		// TODO Auto-generated method stub
		SysPermission o = sysPermissionDao.findByIdAndIsDel((long)id,0);
        if (null == o) {
            return ApiResponseResult.failure("记录ID不存在或已被删除").status("error1");
        }
		return ApiResponseResult.success().data(o);
	}

	@Override
	public ApiResponseResult savePerm(SysPermission perm) throws Exception {
		// TODO Auto-generated method stub
		if(perm.getId() == null){
			//新增
			sysPermissionDao.save(perm);
		}else{
			//修改
			SysPermission s = sysPermissionDao.findByIdAndIsDel(perm.getId(), 0);
			s.setBsCode(perm.getBsCode());
			s.setBsIcon(perm.getBsIcon());
			s.setPageUrl(perm.getPageUrl());
			s.setBsName(perm.getBsName());
			s.setZindex(perm.getZindex());
			s.setDescpt(perm.getDescpt());
			s.setModifiedTime(new Date());
			sysPermissionDao.save(s);
		}
		
		return ApiResponseResult.success("操作成功");
	}

	@Override
	public ApiResponseResult getUserPerms(Long id) throws Exception {
		// TODO Auto-generated method stub
		return ApiResponseResult.success().data(sysPermissionDao.getUserPerms(id));
	}

	@Override
	public ApiResponseResult getUserPermsByPrc(String fcode) throws Exception {
		// TODO Auto-generated method stub
		//获取数据
        List<Object> list = getUserPermsByPrcRf(fcode);
        String f = list.get(0).toString();
        if(!f.equals("True")){
        	return ApiResponseResult.failure();
        }
		return ApiResponseResult.success().data(list.get(1));
	}
	//执行存储获取数据1
    public List<Object> getUserPermsByPrcRf(String fcode) {
        List<Object> resultList = (List<Object>) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call PRC_APP_GETPOWER(?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, fcode);
                cs.registerOutParameter(2,java.sql.Types.VARCHAR);// 注册输出参数 返回类型
                cs.registerOutParameter(3,-10);// 注册输出参数 返回类型
                return cs;
            }
        }, new CallableStatementCallback() {
            public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                List<Object> result = new ArrayList<Object>();
                cs.execute();
                result.add(cs.getString(2));

                String str = cs.getString(2);
                //判断取值是否成功
                if(str.equals("True")){
                    //游标处理
                    ResultSet rs = (ResultSet)cs.getObject(3);
                    List l = new ArrayList();

                    while(rs.next()){
                        Map m = new HashMap();
                        m.put("ID", rs.getString("SID"));
                        m.put("PID", rs.getString("FID"));
                        m.put("FORDER", rs.getString("FORDER"));
                        m.put("BS_CODE", rs.getString("SID"));
                        m.put("BS_NAME", rs.getString("FCAPTION"));
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
