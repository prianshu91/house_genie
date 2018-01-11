package com.promelle.user.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import com.promelle.common.dao.AbstractMariaDao;
import com.promelle.exception.AbstractException;
import com.promelle.filter.SearchFilter;
import com.promelle.user.dao.mapper.UserRoleMapper;
import com.promelle.user.dto.UserRole;

/**
 * This interface is intended for providing interactions with userRole table.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
@RegisterMapper(UserRoleMapper.class)
@UseStringTemplate3StatementLocator
public abstract class UserRoleDao extends AbstractMariaDao<UserRole, SearchFilter, UserRoleMapper> {

    public UserRoleDao() throws AbstractException {
        super(UserRoleMapper.class);
    }

    /**
     * Find user role for provided user id and role id.
     * 
     * @param userId
     * @param roleId
     * 
     * @return user role
     */
    @SqlQuery("select * from userRole where userId= :userId and roleId= :roleId")
    public abstract UserRole findByUserIdRoleId(@Bind("userId") String userId, @Bind("roleId") String roleId);

    /**
     * Find all user role associations for provided user id.
     * 
     * @param userId
     * @return list of matching user role associations.
     */
    @SqlQuery("select * from userRole where userId= :userId")
    public abstract List<UserRole> findByUserId(@Bind("userId") String userId);

    /**
     * Find all user role associations for provided role id.
     * 
     * @param roleId
     * @return list of matching user role associations.
     */
    @SqlQuery("select * from userRole where roleId= :roleId")
    public abstract List<UserRole> findByRoleId(@Bind("roleId") String roleId);

    /**
     * Find all role ids for provided user id.
     * 
     * @param userId
     * @return list of role ids
     */
    @SqlQuery("select roleId from userRole where userId= :userId")
    public abstract List<String> getRoleIdsByUserId(@Bind("userId") String userId);

    /**
     * Delete all user role association for provided user id.
     * 
     * @param userId
     * @return count of associations deleted
     */
    @SqlUpdate("delete from userRole where userId= :userId")
    public abstract int deleteByUserId(@Bind("userId") String userId);

    /**
     * Delete all user role association for provided role id.
     * 
     * @param roleId
     * @return count of associations deleted
     */
    @SqlUpdate("delete from userRole where roleId= :roleId")
    public abstract int deleteByRoleId(@Bind("roleId") String roleId);

    /**
     * Delete a user role association for provided user id & role id.
     * 
     * @param userId
     * @param roleId
     * @return count of associations deleted
     */
    @SqlUpdate("delete from userRole where userId= :userId and roleId= :roleId")
    public abstract int deleteUserRole(@Bind("userId") String userId, @Bind("roleId") String roleId);

}
