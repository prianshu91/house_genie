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
import com.promelle.user.dao.mapper.UserPermissionMapper;
import com.promelle.user.dto.UserPermission;

/**
 * This interface is intended for providing interactions with userPermission table.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
@RegisterMapper(UserPermissionMapper.class)
@UseStringTemplate3StatementLocator
public abstract class UserPermissionDao extends AbstractMariaDao<UserPermission, SearchFilter, UserPermissionMapper> {

    public UserPermissionDao() throws AbstractException {
        super(UserPermissionMapper.class);
    }

    /**
     * Find user permission for provided user id and permission id.
     * 
     * @param userId
     * @param permissionId
     * 
     * @return user permission
     */
    @SqlQuery("select * from userPermission where userId= :userId and permissionId= :permissionId")
    public abstract UserPermission findByUserIdPermissionId(@Bind("userId") String userId,
            @Bind("permissionId") String permissionId);

    /**
     * Find all user permission associations for provided user id.
     * 
     * @param userId
     * @return list of matching user permission associations.
     */
    @SqlQuery("select * from userPermission where userId= :userId")
    public abstract List<UserPermission> findByUserId(@Bind("userId") String userId);

    /**
     * Find all user permission associations for provided permission id.
     * 
     * @param permissionId
     * @return list of matching user permission associations.
     */
    @SqlQuery("select * from userPermission where permissionId= :permissionId")
    public abstract List<UserPermission> findByPermissionId(@Bind("permissionId") String permissionId);

    /**
     * Find all permission ids for provided user id.
     * 
     * @param userId
     * @return list of permission ids
     */
    @SqlQuery("select permissionId from userPermission where userId= :userId")
    public abstract List<String> getPermissionIdsByUserId(@Bind("userId") String userId);

    /**
     * Delete all user permission association for provided user id.
     * 
     * @param userId
     * @return count of associations deleted
     */
    @SqlUpdate("delete from userPermission where userId= :userId")
    public abstract int deleteByUserId(@Bind("userId") String userId);

    /**
     * Delete all user permission association for provided permission id.
     * 
     * @param permissionId
     * @return count of associations deleted
     */
    @SqlUpdate("delete from userPermission where permissionId= :permissionId")
    public abstract int deleteByPermissionId(@Bind("permissionId") String permissionId);

    /**
     * Delete a user permission association for provided user id & permission id.
     * 
     * @param userId
     * @param permissionId
     * @return count of associations deleted
     */
    @SqlUpdate("delete from userPermission where userId= :userId and permissionId= :permissionId")
    public abstract int deleteUserPermission(@Bind("userId") String userId, @Bind("permissionId") String permissionId);

}
