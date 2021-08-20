package com.web.kanban;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.system.permission.entity.SysPermission;
import com.system.role.entity.RolePermissionMap;
import com.system.role.entity.SysRole;
import com.system.user.entity.UserRoleMap;

public interface KanbanDao extends CrudRepository<SysRole, Long>, JpaSpecificationExecutor<SysRole> {

	@Query(value = "SELECT A.LINE_NO,A.LINE_NAME FROM MES_LINE A ORDER BY A.MEMO", nativeQuery = true)
    public List<Map<String, Object>> getLineList();
}
