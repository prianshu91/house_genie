package com.promelle.user.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import com.promelle.common.dao.AbstractMariaDao;
import com.promelle.exception.AbstractException;
import com.promelle.filter.SearchFilter;
import com.promelle.user.dao.mapper.PermissionMapper;
import com.promelle.user.dto.Permission;
import com.promelle.user.dto.PermissionExclusion;

/**
 * This interface is intended for providing interactions with permissionExclusion table.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
@RegisterMapper(PermissionMapper.class)
@UseStringTemplate3StatementLocator
public abstract class PermissionExclusionDao extends AbstractMariaDao<Permission, SearchFilter, PermissionMapper> {

    public PermissionExclusionDao() throws AbstractException {
        super(PermissionMapper.class);
    }

    /**
     * Add a new permission exclusion.
     * 
     * @param permissionExclusion
     * @return 1 for success & 0 for failure
     */
    @SqlUpdate("insert into permissionExclusion(id,type,permissionId,refId) values (:id,:type,:permissionId,:refId)")
    public abstract int save(@BindBean PermissionExclusion permissionExclusion);

    /**
     * Get a list of all permissions of provided type & reference id.
     * 
     * @param type
     * @param refId
     * @return list of permission id
     */
    @SqlQuery("select permissionId from permissionExclusion where type= :type and refId= :refId")
    public abstract List<String> getExcludedPermissionIds(@Bind("type") String type, @Bind("refId") String refId);

}
