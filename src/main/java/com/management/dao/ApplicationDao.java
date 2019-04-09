package com.management.dao;


import com.management.model.Recruitment.Application;
import com.management.model.Settings.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
public class ApplicationDao extends AbstractDao<Application> {


    private static final Logger logger = LoggerFactory.getLogger(ApplicationDao.class);

    private SimpleJdbcCall getApplications, isApplicationExists;

    @Autowired
    @Override
    public void setDataSource(@Qualifier(value = "AdioConsultancyDS") DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);


        create = new SimpleJdbcCall(jdbcTemplate).withProcedureName("createApplication").withReturnValue();


        getApplications = new SimpleJdbcCall(jdbcTemplate).withProcedureName("getApplications")
                .returningResultSet(RESULT_COUNT, new RowCountMapper())
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(Application.class));

        isApplicationExists = new SimpleJdbcCall(jdbcTemplate).withProcedureName("isApplicationExists").
                returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(Application.class));

    }



    public Page<Application> getApplications(Long pageNum, Long pageSize) throws DataAccessException {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("page_num", pageNum)
                .addValue("page_size", pageSize);


        Map m = this.getApplications.execute(params);
        List content = (List) m.get("list");
        Long count = (Long) ((List) m.get("count")).get(0);
        Page<Application> page = new Page(count, content);
        return page;
    }


    public Application isApplicationExists(String firstName, String surName) throws DataAccessException {
        MapSqlParameterSource in = (new MapSqlParameterSource())
                .addValue("firstName", firstName)
                .addValue("surName", surName);
        Map m = this.isApplicationExists.execute(in);
        List<Application> result = (List<Application>) m.get(SINGLE_RESULT);
        return !result.isEmpty() ? result.get(0) : null;
    }


}