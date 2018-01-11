package com.promelle.communication.dao;

import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import com.promelle.common.dao.AbstractMariaDao;
import com.promelle.communication.dao.mapper.UserMapper;
import com.promelle.communication.dto.User;
import com.promelle.exception.AbstractException;
import com.promelle.filter.SearchFilter;

/**
 * This interface is intended for providing interactions with user table.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
@RegisterMapper(UserMapper.class)
@UseStringTemplate3StatementLocator
public abstract class UserDao extends AbstractMariaDao<User, SearchFilter, UserMapper> {

    public UserDao() throws AbstractException {
        super(UserMapper.class);
    }

}
