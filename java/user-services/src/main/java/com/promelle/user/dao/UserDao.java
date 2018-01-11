package com.promelle.user.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import com.promelle.common.dao.AbstractMariaDao;
import com.promelle.exception.AbstractException;
import com.promelle.user.dao.mapper.UserMapper;
import com.promelle.user.dto.User;
import com.promelle.user.filter.UserFilter;

/**
 * This interface is intended for providing interactions with user table.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
@RegisterMapper(UserMapper.class)
@UseStringTemplate3StatementLocator
public abstract class UserDao extends AbstractMariaDao<User, UserFilter, UserMapper> {

    public UserDao() throws AbstractException {
        super(UserMapper.class);
    }

    /**
     * Update status.
     * 
     * @param id
     * @param status
     * @return 1 for success & 0 for failure
     */
    @SqlUpdate("update user set status= :status where id= :id")
    public abstract int updateStatus(@Bind("id") String id, @Bind("status") int status);

    /**
     * Update profile picture.
     * 
     * @param id
     * @param picture
     * @return 1 for success & 0 for failure
     */
    @SqlUpdate("update user set picture= :picture where id= :id")
    public abstract int updateProfilePic(@Bind("id") String id, @Bind("picture") String picture);
    
    /**
     * Update email-id.
     * 
     * @param id
     * @param email
     * @return 1 for success & 0 for failure
     */
    @SqlUpdate("update user set email= :email where id= :id")
    public abstract int updateEmail(@Bind("id") String id, @Bind("email") String email);
    
    /**
     * Update school-id.
     * 
     * @param id
     * @param schoolId
     * @return 1 for success & 0 for failure
     */
    @SqlUpdate("update user set schoolId= :schoolId where id= :id")
    public abstract int updateSchoolId(@Bind("id") String id, @Bind("schoolId") String schoolId);

}
