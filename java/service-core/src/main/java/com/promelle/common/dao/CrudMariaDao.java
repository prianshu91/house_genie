package com.promelle.common.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Define;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import com.promelle.dto.AbstractDTO;

/**
 * 
 * This class is intended for providing crud dao functions.
 * 
 * @author Hemant Kumar
 * @version 1.0
 *
 * @param <T>
 *            extends {@link AbstractDTO}
 */
@UseStringTemplate3StatementLocator
public interface CrudMariaDao<T extends AbstractDTO> {

	String TABLE = "table";
	String PARAMS = "params";
	String VALUES = "values";
	String QUERY = "query";
	String SET = "set";
	String AND = " and ";
	String WHERE = " where ";
	String OR = " or ";
	String LIKE = " like ";

	/**
	 * Save.
	 * 
	 * @param table
	 * @param params
	 * @param values
	 * @return 1 for success & 0 for failure
	 */
	@SqlUpdate("insert into <table>(<params>) values(<values>)")
	int save(@Define(TABLE) String table, @Define(PARAMS) String params,
			@Define(VALUES) String values);

	/**
	 * Update
	 * 
	 * @param table
	 * @param set
	 * @param query
	 * @return 1 for success & 0 for failure
	 */
	@SqlUpdate("update <table> set <set> <query>")
	int update(@Define(TABLE) String table, @Define(SET) String set,
			@Define(QUERY) String query);

	/**
	 * Find by id
	 * 
	 * @param id
	 * @return find by id
	 */
	@SqlQuery("select * from <table> where id = :id")
	T findById(@Define(TABLE) String table, @Bind("id") String id);

	/**
	 * List
	 * 
	 * @param table
	 * @param query
	 * @param paging
	 * @return list of users for the provided account id
	 */
	@SqlQuery("select * from <table> <query> <orderby> <paging>")
	List<T> list(@Define("table") String table, @Define("query") String query,
			@Define("paging") String paging, @Define("orderby") String orderby);

	/**
	 * Find one
	 * 
	 * @param table
	 * @param query
	 * @return list of users for the provided account id
	 */
	@SqlQuery("select * from <table> <query> limit 0, 1")
	T findOne(@Define(TABLE) String table, @Define(QUERY) String query);

	@SqlUpdate("update <table> set status='0' where id= :id")
	int softDelete(@Define(TABLE) String table, @Bind("id") String id);

	/**
	 * 
	 * @param table
	 * @return Count of records in table
	 * 
	 */
	@SqlQuery("select count(*) from <table> <query>")
	int count(@Define(TABLE) String table, @Define(QUERY) String query);

}
