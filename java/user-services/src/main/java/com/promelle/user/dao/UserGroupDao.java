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
import com.promelle.user.dao.mapper.UserGroupMapper;
import com.promelle.user.dto.UserGroup;

/**
 * This interface is intended for providing interactions with userGroup table.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
@RegisterMapper(UserGroupMapper.class)
@UseStringTemplate3StatementLocator
public abstract class UserGroupDao extends
		AbstractMariaDao<UserGroup, SearchFilter, UserGroupMapper> {

	public UserGroupDao() throws AbstractException {
		super(UserGroupMapper.class);
	}

	/**
	 * Find all user group associations for provided user id.
	 * 
	 * @param userId
	 * @return list of matching user group associations.
	 */
	@SqlQuery("select * from userGroup where userId= :userId")
	public abstract List<UserGroup> findByUserId(@Bind("userId") String userId);

	/**
	 * Find all user group associations for provided group id.
	 * 
	 * @param groupId
	 * @return list of matching user group associations.
	 */
	@SqlQuery("select * from userGroup where groupId= :groupId")
	public abstract List<UserGroup> findByGroupId(
			@Bind("groupId") String groupId);

	/**
	 * Find all group ids for provided user id.
	 * 
	 * @param userId
	 * @return list of group ids
	 */
	@SqlQuery("select groupId from userGroup where userId= :userId")
	public abstract List<String> getGroupIdsByUserId(
			@Bind("userId") String userId);

	/**
	 * Delete all user group association for provided user id.
	 * 
	 * @param userId
	 * @return count of associations deleted
	 */
	@SqlUpdate("delete from userGroup where userId= :userId")
	public abstract int deleteByUserId(@Bind("userId") String userId);

	/**
	 * Delete all user group association for provided group id.
	 * 
	 * @param groupId
	 * @return count of associations deleted
	 */
	@SqlUpdate("delete from userGroup where groupId= :groupId")
	public abstract int deleteByGroupId(@Bind("groupId") String groupId);

	/**
	 * Delete a user group association for provided user id & group id.
	 * 
	 * @param userId
	 * @param groupId
	 * @return count of associations deleted
	 */
	@SqlUpdate("delete from userGroup where userId= :userId and groupId= :groupId")
	public abstract int deleteUserGroup(@Bind("userId") String userId,
			@Bind("groupId") String groupId);

	/**
	 * Find user group for provided user id and group id.
	 * 
	 * @param userId
	 * @param groupId
	 * 
	 * @return user group
	 */
	@SqlQuery("select * from userGroup where userId= :userId and groupId= :groupId")
	public abstract UserGroup findByUserIdGroupId(
			@Bind("userId") String userId, @Bind("groupId") String groupId);

}
