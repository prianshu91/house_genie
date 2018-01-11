package com.promelle.user.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import com.promelle.common.dao.AbstractMariaDao;
import com.promelle.exception.AbstractException;
import com.promelle.user.dao.mapper.SchoolMapper;
import com.promelle.user.dto.School;
import com.promelle.user.filter.SchoolFilter;

/**
 * This interface is intended for providing interactions with schools table.
 * 
 * @author Satish Sharma
 * @version 1.0
 */
@RegisterMapper(SchoolMapper.class)
@UseStringTemplate3StatementLocator
public abstract class SchoolDao extends AbstractMariaDao<School, SchoolFilter, SchoolMapper> {

    public SchoolDao() throws AbstractException {
        super(SchoolMapper.class);
    }

    /**
     * Update status.
     * 
     * @param id
     * @param status
     * @return 1 for success & 0 for failure
     */
    @SqlUpdate("update schools set status= :status where id= :id")
    public abstract int updateStatus(@Bind("id") String id, @Bind("status") int status);
    
    /**
     * Update school-pod logo picture.
     * 
     * @param id
     * @param logo
     * @return 1 for success & 0 for failure
     */
    @SqlUpdate("update schools set schoolPodLogo= :logo where id= :id")
    public abstract int updateSchoolPodLogo(@Bind("id") String id, @Bind("logo") String logo);
}
