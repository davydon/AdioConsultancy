package com.management.dao;


import com.management.model.Settings.LookUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * Created by Ekenedirichukwu Amaechi on 12/26/2018.
 */
@Repository
public class LookUpDao extends AbstractDao<LookUp> {


    private SimpleJdbcCall lookUpList, addUpdateLookUp, verifyLookUp;

    @Autowired
    @Override
    public void setDataSource(@Qualifier(value = "AdioConsultancyDS") DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);

        this.lookUpList = new SimpleJdbcCall(jdbcTemplate).withProcedureName("lookUpLists")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(LookUp.class));

        this.addUpdateLookUp = new SimpleJdbcCall(jdbcTemplate).withProcedureName("addUpdateLookUp").withReturnValue();

        this.verifyLookUp = new SimpleJdbcCall(jdbcTemplate).withFunctionName("fnVerifyLookUp");


    }

    public List<LookUp> lookUpList(String param) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("param", param);

        Map<String, Object> m = lookUpList.execute(in);
        List<LookUp> lists = (List<LookUp>) m.get(MULTIPLE_RESULT);

        return lists;

    }


    public Long addUpdateLookUp(LookUp lookUp) throws DataAccessException {
        SqlParameterSource in = new BeanPropertySqlParameterSource(lookUp);
        Map<String, Object> m = this.addUpdateLookUp.execute(in);
        return (Long) m.get("id");
    }


    public Long verifyLookUp(LookUp lookUp) throws DataAccessException {
        SqlParameterSource in = new BeanPropertySqlParameterSource(lookUp);
        Long id = verifyLookUp.executeFunction(Long.class, in);
        return id;
    }
}
