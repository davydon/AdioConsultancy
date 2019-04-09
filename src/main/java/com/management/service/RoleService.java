package com.management.service;


import com.management.dao.AbstractDao;
import com.management.dao.RoleDao;
import com.management.model.Settings.OneParam;
import com.management.model.Settings.Page;
import com.management.model.Settings.Permission;
import com.management.model.Settings.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Ekenedirichukwu Amaechi on 12/26/2018.
 */

@Service
public class RoleService extends AbstractService<Role> {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    public RoleService(@Qualifier("roleDao") AbstractDao<Role> dao) {
        super(dao);
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public Long update(Role model) {
        super.update(model);
        return null;
    }

    @Override
    public Role create(Role role) {
        return super.create(role);
    }

    @Override
    public Role find(Long id) {
        return super.find(id);
    }


    public List<Role> getUserRole(Long roleId) {
        RoleDao roleDao = (RoleDao) dao;
        return roleDao.getUserRole(roleId);
    }


    public void addUserRoles(Long userId, Long roleId,Long createdBy) {
        RoleDao roleDao = (RoleDao) dao;
        roleDao.addUserRoles(userId, roleId,createdBy);
    }


    public void updateUserRole(Long userId, Long roleId,Long lastUpdatedBy) {
        RoleDao roleDao = (RoleDao) dao;
        roleDao.updateUserRole(userId, roleId,lastUpdatedBy);
    }

    public void deleteUserRole(Long userId, Long roleId,Long lastUpdatedBy) {
        RoleDao roleDao = (RoleDao) dao;
        roleDao.deleteUserRole(userId, roleId,lastUpdatedBy);
    }

    public Role getRoleByName(Role role) {
        RoleDao roleDao = (RoleDao) dao;
        return roleDao.getRoleByName(role);
    }

    public Role getRoleByName(String roleName,String roleType) {
        RoleDao roleDao = (RoleDao) dao;
        return roleDao.getRoleByName(roleName,roleType);
    }

    public OneParam isUserRole(Long userId, Long roleId) {
        RoleDao roleDao = (RoleDao) dao;
        return roleDao.isUserRole(userId, roleId);
    }

    public void deletePermission(Long id) {
        RoleDao roleDao = (RoleDao) dao;
        roleDao.deletePermission(id);
    }

    public void removePermission(Long roleId, Long permissionId) {
        RoleDao roleDao = (RoleDao) dao;
        roleDao.removePermission(roleId, permissionId);
    }

    public void updatePermission(Permission pm) {
        RoleDao roleDao = (RoleDao) dao;
        roleDao.updatePermission(pm);
    }

    public void addPermission(Permission pm) {
        RoleDao roleDao = (RoleDao) dao;
        roleDao.addPermission(pm);
    }


    public void assignPermissions(Long roleId, Long permissionIds,Long createdBy) {
        RoleDao roleDao = (RoleDao) dao;
        roleDao.assignPermissions(roleId, permissionIds,createdBy);
    }

    public Permission getPermissions(Long permissionId) {
        RoleDao roleDao = (RoleDao) dao;
        return roleDao.getPermissions(permissionId);
    }

    public List<Permission> getRolePermissions(Long roleId) {
        RoleDao roleDao = (RoleDao) dao;
        return roleDao.getRolePermissions(roleId);
    }

    public Page<Permission> getPermissions(int pageNum, int pageSize, String type) {
        RoleDao roleDao = (RoleDao) dao;
        return roleDao.getPermissions(pageNum, pageSize, type);
    }


    public String getPermissionsByUserId(Long userId) {
        System.out.println(":::::::::::: Permission Service ");
        RoleDao roleDao = (RoleDao) dao;
        return roleDao.getPermissionsByUserId(userId);
    }


    public String getPermissionsByName(Permission pm) {
        RoleDao roleDao = (RoleDao) dao;
        return roleDao.getPermissionsByName(pm);
    }

    public OneParam isRolePermission(Long roleId, Long permissionIds) {
        RoleDao roleDao = (RoleDao) dao;
        return roleDao.isRolePermission(roleId, permissionIds);
    }

    public Page<Role> getRoleByType(Integer pageNum, Integer pageSize, String roleType) {
        RoleDao roleDao = (RoleDao) dao;
        return roleDao.getRolesByType(pageNum,pageSize,roleType);
    }



    @Override
    public List<Role> findAll() {
        return super.findAll();
    }

    @Override
    public Page<Role> findAll(Integer pageNum, Integer pageSize) {
        return super.findAll(pageNum, pageSize);
    }
}
