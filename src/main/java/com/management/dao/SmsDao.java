package com.management.dao;


import com.management.model.EmailSMS.SmsDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
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
public class SmsDao extends AbstractDao<SmsDetails> {

    private SimpleJdbcCall queueSms, getSmsToSend, updateSmsStatus;


    @Autowired
    @Override
    public void setDataSource(@Qualifier(value = "AdioConsultancyDS") DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);

        this.queueSms = new SimpleJdbcCall(jdbcTemplate).withProcedureName("queueSms").withReturnValue();
        this.updateSmsStatus = new SimpleJdbcCall(jdbcTemplate).withProcedureName("updateSmsStatus").withReturnValue();
        this.getSmsToSend = new SimpleJdbcCall(jdbcTemplate).withProcedureName("getSmsToSend")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(SmsDetails.class));

    }

    public void queueSms(String sender, String recipient, String content) {

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("sender", sender)
                .addValue("recipient", recipient)
                .addValue("content", content);

        this.queueSms.execute(in);
    }

    public void updateSmsStatus(long id, int status) {

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("status", status);

        this.updateSmsStatus.execute(in);
    }

    public List<SmsDetails> getSmsToSend() {

        Map<String, Object> m = this.getSmsToSend.execute();
        List<SmsDetails> list = (List<SmsDetails>) m.get(MULTIPLE_RESULT);

        return list;
    }
}
