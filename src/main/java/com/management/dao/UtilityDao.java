package com.management.dao;

import com.management.model.*;
import com.management.model.Settings.ApproveReject;
import com.management.model.Settings.CorporateSettings;
import com.management.model.Settings.DisableEnable;
import com.management.model.Settings.Page;
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
public class UtilityDao extends AbstractDao<AbstractModel> {

    private SimpleJdbcCall approveReject, disableEnable, addCorporateSettings, getCorporateSettings, findSettings, isSettingExists;

    @Autowired
    @Override

    public void setDataSource(@Qualifier(value = "AdioConsultancyDS") DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);

        this.approveReject = new SimpleJdbcCall(jdbcTemplate).withProcedureName("approveReject")
                .withReturnValue();
        this.disableEnable = new SimpleJdbcCall(jdbcTemplate).withProcedureName("disableEnable")
                .withReturnValue();

        this.addCorporateSettings = new SimpleJdbcCall(jdbcTemplate).withProcedureName("createCorporateSettings")
                .withReturnValue();
        this.getCorporateSettings = new SimpleJdbcCall(jdbcTemplate).withProcedureName("getCorporateSettings")
                .returningResultSet(RESULT_COUNT, new RowCountMapper())
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(CorporateSettings.class));

        this.findSettings = new SimpleJdbcCall(jdbcTemplate).withProcedureName("getCorporateSetting")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(CorporateSettings.class));
        this.isSettingExists = new SimpleJdbcCall(jdbcTemplate).withProcedureName("isSettingExists").
                returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(CorporateSettings.class));

        //return null;
    }

    public CorporateSettings isSettingExists(String setting, String agentCode) throws DataAccessException {
        MapSqlParameterSource in = (new MapSqlParameterSource())
                .addValue("setting", setting)
                .addValue("agentCode", agentCode);
        Map m = this.isSettingExists.execute(in);
        List<CorporateSettings> result = (List<CorporateSettings>) m.get(SINGLE_RESULT);
        return !result.isEmpty() ? result.get(0) : null;
    }

    public Long approveReject(ApproveReject data) throws DataAccessException {
        SqlParameterSource params = new BeanPropertySqlParameterSource(data);
        Map<String, Object> m = approveReject.execute(params);
        return (Long) m.get("id");
        //return 1L;
    }

    public Long disableEnable(DisableEnable data) throws DataAccessException {
        SqlParameterSource params = new BeanPropertySqlParameterSource(data);
        Map<String, Object> m = disableEnable.execute(params);
        return (Long) m.get("id");
        // return 1L;

    }


    public Long addCorporateSettings(CorporateSettings settings) throws DataAccessException {
        SqlParameterSource in = new BeanPropertySqlParameterSource(settings);
        Map<String, Object> m = this.addCorporateSettings.execute(in);
        return (Long) m.get("id");
    }


    public Page<CorporateSettings> getCorporateSettings(Long pageNum, Long pageSize) throws DataAccessException {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("page_num", pageNum)
                .addValue("page_size", pageSize);

        Map m = this.getCorporateSettings.execute(params);
        List content = (List) m.get("list");
        Long count = (Long) ((List) m.get("count")).get(0);
        Page<CorporateSettings> page = new Page(count, content);
        return page;
    }


    public List<CorporateSettings> findSettings(String agentCode) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource().addValue("agentCode", agentCode);
        Map<String, Object> m = findSettings.execute(in);
        List<CorporateSettings> list = (List<CorporateSettings>) m.get(MULTIPLE_RESULT);
        return list;
    }
}
