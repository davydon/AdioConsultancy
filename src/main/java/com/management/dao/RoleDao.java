package com.management.dao;

import com.management.model.Settings.OneParam;
import com.management.model.Settings.Page;
import com.management.model.Settings.Permission;
import com.management.model.Settings.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Ekenedirichukwu Amaechi on 12/26/2018.
 */
@Repository
public class RoleDao extends AbstractDao<Role> {


    private SimpleJdbcCall assignRolePermissions, getPermissions,getAllPermissions, getRolePermissions, addUserRoles, updateUserRole,
            getPermissionsByUserId, addPermission, updatePermission, deleteUserRole,
            deletePermission, getPermission, getRolebyName, getPermissionsByName, removePermission,
            isRolePermission, isUserRole, getUserRole, menuList,getRolesByType,getRoleByNameAndType;

    @Autowired
    @Override
    public void setDataSource(@Qualifier(value = "AdioConsultancyDS") DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        create = new SimpleJdbcCall(jdbcTemplate).withProcedureName("createRole").withReturnValue();
        update = new SimpleJdbcCall(jdbcTemplate).withProcedureName("updateRole").withReturnValue();
        delete = new SimpleJdbcCall(jdbcTemplate).withProcedureName("deleteRole").withReturnValue();

        addUserRoles = new SimpleJdbcCall(jdbcTemplate).withProcedureName("addRoleUser").withReturnValue();
        updateUserRole = new SimpleJdbcCall(jdbcTemplate).withProcedureName("updateRoleUser").withReturnValue();
        deleteUserRole = new SimpleJdbcCall(jdbcTemplate).withProcedureName("deleteRoleUser").withReturnValue();

        getUserRole = new SimpleJdbcCall(jdbcTemplate).withProcedureName("getUserRole").
                returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(Role.class));
        getRolebyName = new SimpleJdbcCall(jdbcTemplate).withProcedureName("getRoleByName")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(Role.class));
        isUserRole = new SimpleJdbcCall(jdbcTemplate).withProcedureName("isUserRole").
                returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(OneParam.class));

        addPermission = new SimpleJdbcCall(jdbcTemplate).withProcedureName("createPermissions").withReturnValue();
        updatePermission = new SimpleJdbcCall(jdbcTemplate).withProcedureName("updatePermissions").withReturnValue();
        deletePermission = new SimpleJdbcCall(jdbcTemplate).withProcedureName("deletePermissions").withReturnValue();
        removePermission = new SimpleJdbcCall(jdbcTemplate).withProcedureName("removePermissions").withReturnValue();


        assignRolePermissions = new SimpleJdbcCall(jdbcTemplate).withProcedureName("assign_role_permissions").withReturnValue();
        getPermission = new SimpleJdbcCall(jdbcTemplate).withProcedureName("getPermission")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(Permission.class));
        getRoleByNameAndType = new SimpleJdbcCall(jdbcTemplate).withProcedureName("getRoleByNameAndType")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(Role.class));

        getPermissions = new SimpleJdbcCall(jdbcTemplate).withProcedureName("get_permissions")
                .returningResultSet(RESULT_COUNT, new RowCountMapper())

                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(Permission.class));



        getRolePermissions = new SimpleJdbcCall(jdbcTemplate).withProcedureName("getRolePermissions")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(Permission.class));

        getPermissionsByName = new SimpleJdbcCall(jdbcTemplate).withProcedureName("getPermissionName")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(Permission.class));

        getPermissionsByUserId = new SimpleJdbcCall(jdbcTemplate).withProcedureName("getPermissionsByUserId")
                .returningResultSet("singleRow", new RowMapper() {
                    @Override
                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getString("accessList");
                    }
                });
        isRolePermission = new SimpleJdbcCall(jdbcTemplate).withProcedureName("isRolePermission")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(OneParam.class));

        find = new SimpleJdbcCall(jdbcTemplate).withProcedureName("getRole")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(Role.class));
        findAll = new SimpleJdbcCall(jdbcTemplate).withProcedureName("getRoles")
                .returningResultSet(RESULT_COUNT, new RowCountMapper())
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(Role.class));
        getRolesByType = new SimpleJdbcCall(jdbcTemplate).withProcedureName("getRolesByType")
                .returningResultSet(RESULT_COUNT, new RowCountMapper())
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(Role.class));



    }

    public String getPermissionsByUserId(Long userId) throws DataAccessException {
        System.out.println(":::::::::::::::::: Calling Permission List for "+userId);
        MapSqlParameterSource in = (new MapSqlParameterSource())
                .addValue("userId", userId);

        Map<String, Object> m = this.getPermissionsByUserId.execute(in);
        List<String> list = (List<String>) m.get("SingleRow");
        return !list.isEmpty() ? list.get(0) : null;
    }

    public String getPermissionsByName(Permission pm) throws DataAccessException {
        SqlParameterSource in = new BeanPropertySqlParameterSource(pm);
        Map<String, Object> m = getPermissionsByName.execute(in);
        List<Permission> result = (List<Permission>) m.get(SINGLE_RESULT);
        if (!result.isEmpty()) {
            return result.get(0).toString();
        } else {
            return null;
        }
    }

    public OneParam isRolePermission(Long roleid, Long permissionIds) {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("roleid", roleid)
                .addValue("permissionIds", permissionIds);
        Map<String, Object> m = isRolePermission.execute(in);
        List<OneParam> result = (List<OneParam>) m.get(SINGLE_RESULT);
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }

    }

    public Role find(Long role) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource().addValue("id", role);
        Map<String, Object> m = find.execute(in);
        List<Role> result = (List<Role>) m.get(SINGLE_RESULT);
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }


    public List<Role> getUserRole(Long userId) throws DataAccessException {
        SqlParameterSource params = new MapSqlParameterSource().addValue("userId", userId);
        Map<String, Object> m = getUserRole.execute(params);
        List<Role> list = (List<Role>) m.get(MULTIPLE_RESULT);
        return list;
    }

    public void addUserRoles(Long userId, Long roleId,Long createdBy) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("roleId", roleId)
                .addValue("createdBy", createdBy);
        addUserRoles.execute(in);
    }


    public void updateUserRole(Long userId, Long roleId,Long lastUpdatedBy) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("roleId", roleId)
        .addValue("lastUpdatedBy", lastUpdatedBy);
        updateUserRole.execute(in);
    }

    public void deleteUserRole(Long userId, Long roleId,Long lastUpdatedBy) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("roleId", roleId)
                .addValue("lastUpdatedBy", lastUpdatedBy);
        deleteUserRole.execute(in);
    }

    public Role getRoleByName(Role role) throws DataAccessException {
        SqlParameterSource in = new BeanPropertySqlParameterSource(role);
        Map<String, Object> m = getRolebyName.execute(in);
        List<Role> result = (List<Role>) m.get(SINGLE_RESULT);
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public Role getRoleByName(String roleName, String roleType) throws DataAccessException {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", roleName)
                .addValue("roleType", roleType);
        Map<String, Object> m = getRoleByNameAndType.execute(params);
        List<Role> result = (List<Role>) m.get(SINGLE_RESULT);
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }



    public OneParam isUserRole(Long userId, Long roleId) {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("roleId", roleId);
        Map<String, Object> m = isUserRole.execute(in);
        List<OneParam> result = (List<OneParam>) m.get(SINGLE_RESULT);
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }

    }


    public void addPermission(Permission pm) throws DataAccessException {
        SqlParameterSource in = new BeanPropertySqlParameterSource(pm);
        addPermission.execute(in);
    }

    public void updatePermission(Permission pm) throws DataAccessException {
        SqlParameterSource in = new BeanPropertySqlParameterSource(pm);
        updatePermission.execute(in);
    }

    public void deletePermission(Long id) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("id", id);
        deletePermission.execute(in);
    }

    public void removePermission(Long roleId, Long permissionId) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("roleId", roleId)
                .addValue("permissionId", permissionId);
        removePermission.execute(in);
    }

    public void assignPermissions(Long roleId, Long permissionId,Long createdBy) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource().addValue("roleId", roleId)
                .addValue("permissionId", permissionId)
                 .addValue("createdBy", createdBy);
        assignRolePermissions.execute(in);
    }

    public List<Permission> getRolePermissions(Long roleId) throws DataAccessException {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("roleId", roleId);
            //    .addValue("permissionType", permissionType);
        Map<String, Object> m = getRolePermissions.execute(params);
        List<Permission> list = (List<Permission>) m.get(MULTIPLE_RESULT);
        return list;
    }

    public Permission getPermissions(Long permissionId) throws DataAccessException {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", permissionId);
        Map<String, Object> m = getPermission.execute(params);
        List<Permission> result = (List<Permission>) m.get(SINGLE_RESULT);
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public Page<Permission> getPermissions(int pageNum, int pageSize, String type) throws DataAccessException {

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("page_num", pageNum)
                .addValue("page_size", pageSize)
                .addValue("type", type);

        Map m = this.getPermissions.execute(params);
        List content = (List) m.get("list");
        Long count = (Long) ((List) m.get("count")).get(0);
        Page<Permission> page = new Page(count, content);
        return page;
    }






    public Page<Role> getRolesByType(int pageNum, int pageSize, String type) throws DataAccessException {

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("page_num", pageNum)
                .addValue("page_size", pageSize)
                .addValue("roleType", type);

        Map m = this.getRolesByType.execute(params);
        List content = (List) m.get("list");
        Long count = (Long) ((List) m.get("count")).get(0);
        Page<Role> page = new Page(count, content);
        return page;
    }

}
