package com.system.role.service.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.permission.dao.SysPermissionDao;
import com.system.permission.entity.SysPermission;
import com.system.role.dao.RolePermissionMapDao;
import com.system.role.entity.RolePermissionMap;
import com.system.user.dao.UserRoleMapDao;
import com.system.user.entity.UserRoleMap;
import com.utils.BaseService;
import com.utils.SearchFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.role.dao.SysRoleDao;
import com.system.role.entity.SysRole;
import com.system.role.service.SysRoleService;
import com.system.user.entity.SysUser;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;

/**
 * 角色
 *
 */
@Service(value = "sysRoleService")
@Transactional(propagation = Propagation.REQUIRED)
public class SysRoleImpl implements SysRoleService {

    @Autowired
    private SysRoleDao sysRoleDao;
    @Autowired
    private RolePermissionMapDao rolePermissionMapDao;
    @Autowired
    private UserRoleMapDao userRoleMapDao;
    @Autowired
    private SysPermissionDao sysPermissionDao;

    /**
     * 新增角色
     * @param sysRole
     * @return
     * @author fyx
     * @serialData 2018-11-21
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult add(SysRole sysRole) throws Exception{
        if(sysRole == null){
            return ApiResponseResult.failure("角色不能为空！");
        }
        if(StringUtils.isEmpty(sysRole.getBsCode())){
            return ApiResponseResult.failure("角色编号不能为空！");
        }
        if(StringUtils.isEmpty(sysRole.getBsName())){
            return ApiResponseResult.failure("角色名称不能为空！");
        }
        int count = sysRoleDao.countByIsDelAndBsCode(0, sysRole.getBsCode());
        if(count > 0){
            return ApiResponseResult.failure("该角色已存在，请填写其他角色编号！");
        }

        sysRole.setCreatedTime(new Date());
        sysRoleDao.save(sysRole);

        return ApiResponseResult.success("角色添加成功！").data(sysRole);
    }

	@Override
    @Transactional
    public ApiResponseResult edit(SysRole sysRole) throws Exception {
        if(sysRole == null){
            return ApiResponseResult.failure("角色不能为空！");
        }
        if(sysRole.getId() == null){
            return ApiResponseResult.failure("角色ID不能为空！");
        }
        if(StringUtils.isEmpty(sysRole.getBsCode())){
            return ApiResponseResult.failure("角色编号不能为空！");
        }
        if(StringUtils.isEmpty(sysRole.getBsName())){
            return ApiResponseResult.failure("角色名称不能为空！");
        }
        SysRole o = sysRoleDao.findById((long) sysRole.getId());
        if(o == null){
            return ApiResponseResult.failure("该角色不存在！");
        }

        //判断角色编号是否有变化，有则修改；没有则不修改
        String originalCode = o.getBsCode();
        if(o.getBsCode().equals(sysRole.getBsCode())){
        }else{
            int count = sysRoleDao.countByIsDelAndBsCode(0, sysRole.getBsCode());
            if(count > 0){
                return ApiResponseResult.failure("角色编号已存在，请填写其他角色编号！");
            }
            o.setBsCode(sysRole.getBsCode().trim());
        }
        o.setModifiedTime(new Date());
        o.setBsName(sysRole.getBsName());
        o.setDescpt(sysRole.getDescpt());
        sysRoleDao.save(o);

        return ApiResponseResult.success("编辑成功！");
	}

    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("角色ID不能为空！");
        }
        SysRole o  = sysRoleDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该角色不存在！");
        }

        o.setModifiedTime(new Date());
        o.setIsDel(1);
        sysRoleDao.save(o);

        return ApiResponseResult.success("删除成功！");
    }

	@Override
    @Transactional
	public ApiResponseResult getList(String keyword, String bsCode,String bsName, Date createdTimeStart, Date createdTimeEnd, PageRequest pageRequest) throws Exception {
        //查询条件1
        List<SearchFilter> filters =new ArrayList<>();
        filters.add(new SearchFilter("isDel", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        if(StringUtils.isNotEmpty(bsCode)){
            filters.add(new SearchFilter("bsCode", SearchFilter.Operator.LIKE, bsCode));
        }
        if(StringUtils.isNotEmpty(bsName)){
            filters.add(new SearchFilter("bsName", SearchFilter.Operator.LIKE, bsName));
        }
        if(createdTimeStart != null){
            filters.add(new SearchFilter("createdTime", SearchFilter.Operator.GTE, createdTimeStart));
        }
        if(createdTimeEnd != null){
            filters.add(new SearchFilter("createdTime", SearchFilter.Operator.LTE, createdTimeEnd));
        }
        //查询2
        List<SearchFilter> filters1 =new ArrayList<>();
        if(StringUtils.isNotEmpty(keyword)){
            filters1.add(new SearchFilter("bsCode", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("bsName", SearchFilter.Operator.LIKE, keyword));
        }
        Specification<SysRole> spec = Specification.where(BaseService.and(filters, SysRole.class));
        Specification<SysRole> spec1 =  spec.and(BaseService.or(filters1, SysRole.class));
        Page<SysRole> page = sysRoleDao.findAll(spec1, pageRequest);

        return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}

	@Override
	public ApiResponseResult getCheckedRoles(long userId) throws Exception {
		Map map = new HashMap();
		return ApiResponseResult.success().data(map);
	}
	@Override
    @Transactional
	public ApiResponseResult saveUserRoles(long userId,String roles) throws Exception {
		return ApiResponseResult.success("修改成功！");
	}

	@Override
	public ApiResponseResult addRouter(String rolecode, String roles) throws Exception {
		return ApiResponseResult.success();
	}

	@Override
	public ApiResponseResult getRouter(String rolecode) throws Exception {
		return null;
	}

    /**
     * 获取当前角色的操作权限
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(readOnly = true)
    public ApiResponseResult getPermission() throws Exception{
        SysUser currUser = UserUtil.getCurrUser();  //获取当前用户
//        if(currUser == null || currUser.getId() == null){
//            return ApiResponseResult.failure("当前用户不存在！");
//        }
        if(currUser == null || currUser.getFid() == null){
            return ApiResponseResult.failure("当前用户不存在！");
        }

        //1.初始化
        int isSuper = 0;
        String perm = "";

        //2.判断是否为管理员用户，是则无需获取操作权限；否则获取操作权限
//        if(currUser.getUserIsSuper() == 1){
//            //2.1
//            isSuper = 1;
//        }else{
//            //2.2获取当前用户所属角色
//            List<UserRolesMap> mapList = userRolesMapDao.findByIsDelAndUserId(0, currUser.getId());
//            if(mapList != null && mapList.size() > 0){
//                for(UserRolesMap map : mapList){
//                    if(map != null && map.getUserId() != null){
//                        //获取角色
//                        SysRole role = sysRoleDao.findById((long) map.getRoleId());
//                        if(role != null && StringUtils.isNotEmpty(role.getBsPermission())){
//                            perm = StringUtils.isNotEmpty(perm) ? (perm+","+role.getBsPermission()) : role.getBsPermission();
//                        }
//                    }
//                }
//            }
//        }

        //3.封装数据
        Map<String, Object> map = new HashMap<>();
        map.put("isSuper", isSuper);
        map.put("perm", perm);

        return ApiResponseResult.success().data(map);
    }

    /**
     * 根据ID获取角色
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getRole(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("角色ID不能为空！");
        }
        SysRole o = sysRoleDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该角色不存在！");
        }
        return ApiResponseResult.success().data(o);
    }

    /**
     * 获取所有角色
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(readOnly = true)
    public ApiResponseResult getRoles() throws Exception{
        List<SearchFilter> filters = new ArrayList<>();
        Specification<SysRole> spec = Specification.where(BaseService.and(filters, SysRole.class));
        List<SysRole> list = sysRoleDao.findAll(spec);
        return ApiResponseResult.success().data(list);
    }

    /**
     * 根据角色ID获取权限信息
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getRolePerm(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("角色ID不能为空！");
        }
        SysRole o = sysRoleDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该角色不存在！");
        }

        //获取当前角色下角色权限关联信息
        List<RolePermissionMap> list = rolePermissionMapDao.findByIsDelAndAndRoleId(0, id);

        //获取所有权限信息
        List<SysPermission> list2 = sysPermissionDao.findByIsDel(0);

        List<Map<String, Object>> mapList = new ArrayList<>();
        for(SysPermission permItem : list2) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", permItem.getId()!=null ? permItem.getId().toString() : "");
            map.put("code", permItem.getBsCode()!=null ? permItem.getBsCode().toString() : "");
            map.put("name", permItem.getBsName()!=null ? permItem.getBsName().toString() : "");
            map.put("pId", permItem.getParentId()!=null ? permItem.getParentId().toString() : "");
            map.put("zindex", permItem.getZindex()!=null ? permItem.getZindex().toString() : "");
            map.put("istype", permItem.getIstype()!=null ? permItem.getIstype().toString() : "");
            map.put("descpt", permItem.getDescpt()!=null ? permItem.getDescpt().toString() : "");
            map.put("icon", permItem.getBsIcon()!=null ? permItem.getBsIcon().toString() : "");
            map.put("page", permItem.getPageUrl()!=null ? permItem.getPageUrl().toString() : "");
            map.put("checked", false);
            map.put("open", true);
            //判断是否有权限
            for(RolePermissionMap rolePermItem : list){
                if(rolePermItem.getPermitId()!=null && rolePermItem.getPermitId().equals(permItem.getId())){
                    map.put("checked", true);
                }
            }
            mapList.add(map);
        }

        return ApiResponseResult.success().data(mapList);
    }

    /**
     * 设置权限
     * @param roleId
     * @param permIds
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult doRolePerm(Long roleId, String permIds) throws Exception{
        if(roleId == null){
            return ApiResponseResult.failure("角色ID不能为空！");
        }
        //转换
        String[] permIdArray = permIds.split(",");
        List<Long> permIdList = new ArrayList<Long>();
        for(int i = 0; i < permIdArray.length; i++){
            if(StringUtils.isNotEmpty(permIdArray[i])) {
                permIdList.add(Long.parseLong(permIdArray[i]));
            }
        }

        //1.删除角色原权限信息
        List<RolePermissionMap> listOld = rolePermissionMapDao.findByIsDelAndAndRoleId(0, roleId);
        if(listOld.size() > 0){
            for(RolePermissionMap item : listOld){
                item.setModifiedTime(new Date());
                item.setIsDel(1);
            }
            rolePermissionMapDao.saveAll(listOld);
        }

        //2.添加角色新权限信息
        List<RolePermissionMap> listNew = new ArrayList<>();
        if(permIdList.size() > 0){
            for(Long permId : permIdList){
                RolePermissionMap item = new RolePermissionMap();
                item.setCreatedTime(new Date());
                item.setRoleId(roleId);
                item.setPermitId(permId);
                listNew.add(item);
            }
            rolePermissionMapDao.saveAll(listNew);
        }

        return ApiResponseResult.success("设置权限成功！");
    }
}
