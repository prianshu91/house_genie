package com.promelle.user.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;
import org.skife.jdbi.v2.unstable.BindIn;

import com.promelle.common.dao.AbstractMariaDao;
import com.promelle.exception.AbstractException;
import com.promelle.filter.SearchFilter;
import com.promelle.user.dao.mapper.GroupMapper;
import com.promelle.user.dto.Group;

/**
 * This interface is intended for providing interactions with group table.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
@RegisterMapper(GroupMapper.class)
@UseStringTemplate3StatementLocator
public abstract class GroupDao extends AbstractMariaDao<Group, SearchFilter, GroupMapper> {

    public GroupDao() throws AbstractException {
        super(GroupMapper.class);
    }

    /**
     * Find multiple groups by multiple ids.
     * 
     * @param groupIds
     * @return list of matching groups
     */
    @SqlQuery("select * from `group` where id in (<ids>) and status=1")
    public abstract List<Group> findByIds(@BindIn("ids") List<String> ids);

    /**
     * Find all groups for an account id.
     * 
     * @param accountId
     * @return list of groups for the provided account id
     */
    @SqlQuery("select * from `group` where accountId= :accountId and status=1")
    public abstract List<Group> findByAccountId(@Bind("accountId") String accountId);

    /**
     * Find all groups for an account id except the excluded group ids.
     * 
     * @param accountId
     * @param groupIds
     * @return list of groups
     */
    @SqlQuery("select * from `group` where accountId= :accountId and status=1 and id not in (<groupIds>)")
    public abstract List<Group> findByAccountIdMinusIds(@Bind("accountId") String accountId,
            @BindIn("groupIds") List<String> groupIds);

}
