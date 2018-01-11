package com.promelle.user.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;
import org.skife.jdbi.v2.unstable.BindIn;

import com.promelle.common.dao.AbstractMariaDao;
import com.promelle.exception.AbstractException;
import com.promelle.filter.SearchFilter;
import com.promelle.user.dao.mapper.GroupRoleMapper;
import com.promelle.user.dto.GroupRole;

/**
 * This interface is intended for providing interactions with groupRole table.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
@RegisterMapper(GroupRoleMapper.class)
@UseStringTemplate3StatementLocator
public abstract class GroupRoleDao extends AbstractMariaDao<GroupRole, SearchFilter, GroupRoleMapper> {

    public GroupRoleDao() throws AbstractException {
        super(GroupRoleMapper.class);
    }

    /**
     * Find all group role associations for provided group id.
     * 
     * @param groupId
     * @return list of matching group role associations.
     */
    @SqlQuery("select * from groupRole where groupId= :groupId")
    public abstract List<GroupRole> findByGroupId(@Bind("groupId") String groupId);

    /**
     * Find all group role associations for provided role id.
     * 
     * @param roleId
     * @return list of matching group role associations.
     */
    @SqlQuery("select * from groupRole where roleId= :roleId")
    public abstract List<GroupRole> findByRoleId(@Bind("roleId") String roleId);

    /**
     * Find all role ids for provided group id.
     * 
     * @param groupId
     * @return list of role ids
     */
    @SqlQuery("select roleId from groupRole where groupId= :groupId")
    public abstract List<String> getRoleIdsByGroupId(@Bind("groupId") String groupId);

    /**
     * Find all role ids for provided group ids.
     * 
     * @param groupIds
     * @return list of role ids
     */
    @SqlQuery("select roleId from groupRole where groupId in (<groupIds>)")
    public abstract List<String> getRoleIdsByGroupIds(@BindIn("groupIds") List<String> groupIds);

    /**
     * Delete all group role association for provided group id.
     * 
     * @param groupId
     * @return count of associations deleted
     */
    @SqlUpdate("delete from groupRole where groupId= :groupId")
    public abstract int deleteByGroupId(@Bind("groupId") String groupId);

    /**
     * Delete all group role association for provided role id.
     * 
     * @param roleId
     * @return count of associations deleted
     */
    @SqlUpdate("delete from groupRole where roleId= :roleId")
    public abstract int deleteByRoleId(@Bind("roleId") String roleId);

    /**
     * Delete a group role association for provided group id & role id.
     * 
     * @param groupId
     * @param roleId
     * @return count of associations deleted
     */
    @SqlUpdate("delete from groupRole where groupId= :groupId and roleId= :roleId")
    public abstract int deleteGroupRole(@Bind("groupId") String groupId, @Bind("roleId") String roleId);

}
