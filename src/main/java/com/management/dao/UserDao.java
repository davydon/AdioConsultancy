package com.management.dao;


import com.management.model.Settings.Page;
import com.management.model.Users.User;
import com.management.utility.LoadData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Ekenedirichukwu Amaechi on 12/26/2018.
 */

@Repository
public class UserDao extends AbstractDao<User> {


   @Autowired
   private LoadData loadData;

    @Value("${xpress.login.attempts}")
    private String loginAttempts;
    private SimpleJdbcCall findByUsername, updateLogin, getUserPreviousPasswords, changeUserPassword,
            loginUser,loginMerchantUser, isUserExists, findUsers, lockLogin, update_failed_login, unlockLogin, updateAccountCode,
            getUserByParameter, updatePassword, activateDeactivate, findUserByCategory,activateAccount,getHospitalUsers;


    @Autowired
    @Override
    public void setDataSource(@Qualifier(value = "AdioConsultancyDS") DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);

        create = new SimpleJdbcCall(jdbcTemplate).withProcedureName("createUser").withReturnValue();
        update = new SimpleJdbcCall(jdbcTemplate).withProcedureName("updateUser").withReturnValue();
        delete = new SimpleJdbcCall(jdbcTemplate).withProcedureName("delete_user").withReturnValue();

        loginUser = new SimpleJdbcCall(jdbcTemplate).withProcedureName("loginUser").
                returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(User.class));

        loginMerchantUser  = new SimpleJdbcCall(jdbcTemplate).withProcedureName("loginMerchantUser").
                returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(User.class));

        activateAccount = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("activateAccount").returningResultSet("singleRow", new RowMapper() {
            @Override
            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("activate");
            }
        });

        updateAccountCode=new SimpleJdbcCall(jdbcTemplate).withProcedureName("updateAccountCode").withReturnValue();

        find = new SimpleJdbcCall(jdbcTemplate).withProcedureName("getUser")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(User.class));

        updateLogin = new SimpleJdbcCall(jdbcTemplate).withProcedureName("updateLogin").withReturnValue();


        lockLogin = new SimpleJdbcCall(jdbcTemplate).withProcedureName("loginLock").withReturnValue();
        update_failed_login = new SimpleJdbcCall(jdbcTemplate).withProcedureName("updateFailedLogin").withReturnValue();



        unlockLogin = new SimpleJdbcCall(jdbcTemplate).withProcedureName("loginUnlock").withReturnValue();


        getUserPreviousPasswords = new SimpleJdbcCall(jdbcTemplate).withProcedureName("getUserPreviousPasswords")
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(User.class));

        changeUserPassword = new SimpleJdbcCall(jdbcTemplate).withProcedureName("changeUserPassword").withReturnValue();
        updatePassword = new SimpleJdbcCall(jdbcTemplate).withProcedureName("updatePassword").withReturnValue();
        isUserExists = new SimpleJdbcCall(jdbcTemplate).withProcedureName("isUserExists").
                returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(User.class));



        findUsers = new SimpleJdbcCall(jdbcTemplate).withProcedureName("get_users")
                .returningResultSet(RESULT_COUNT, new RowCountMapper())
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(User.class));

        findByUsername = new SimpleJdbcCall(jdbcTemplate).withProcedureName("getUserByUsername")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(User.class));


        getUserByParameter = new SimpleJdbcCall(jdbcTemplate).withProcedureName("getUserByParameter")
                .returningResultSet(SINGLE_RESULT, BeanPropertyRowMapper.newInstance(User.class));


        activateDeactivate = new SimpleJdbcCall(jdbcTemplate).withProcedureName("changeUserStatus").withReturnValue();

        findUserByCategory = new SimpleJdbcCall(jdbcTemplate).withProcedureName("getUsersCategory")
                .returningResultSet(RESULT_COUNT, new RowCountMapper())
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(User.class));
        getHospitalUsers = new SimpleJdbcCall(jdbcTemplate).withProcedureName("getHospitalUsers")
                .returningResultSet(RESULT_COUNT, new RowCountMapper())
                .returningResultSet(MULTIPLE_RESULT, BeanPropertyRowMapper.newInstance(User.class));


    }

    public void updateAccountCode(Long userId, String code) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("code", code);
        updateAccountCode.execute(in);
    }
    public String activateAccount(String activationCode,String username) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("activationCode", activationCode)
                .addValue("username", username);
        Map<String, Object> m = activateAccount.execute(in);
        List<String> list = (List<String>) m.get("SingleRow");
        return !list.isEmpty()? list.get(0) :null;
    }

    public User find(Long userId) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource().addValue("id", userId);
        Map<String, Object> m = find.execute(in);
        List<User> result = (List<User>) m.get(SINGLE_RESULT);
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public void updateLogin(String username, Boolean passwordMatched) throws DataAccessException {
        int loginAttemptsConfig = Integer.parseInt(loginAttempts);
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("username", username)
                .addValue("passwordMatched", passwordMatched)
                .addValue("loginAttemptsConfig", loginAttemptsConfig);
        updateLogin.execute(in);
    }


    public void lockLogin(Long userId) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource().addValue("userId", userId);
        lockLogin.execute(in);
    }

    public void unlockLogin(Long userId) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource().addValue("userId", userId);
        unlockLogin.execute(in);
    }

    public User findByUsername(String username) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource().addValue("username", username);
        Map<String, Object> m = findByUsername.execute(in);
        List<User> result = (List<User>) m.get(SINGLE_RESULT);
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public User getUserByParameter(String username) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource().addValue("username", username);
        Map<String, Object> m = getUserByParameter.execute(in);
        List<User> result = (List<User>) m.get(SINGLE_RESULT);
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public Page<User> findUsers(Long pageNum, Long pageSize, Long userId) throws DataAccessException {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("page_num", pageNum)
                .addValue("page_size", pageSize)
                .addValue("userId", userId);

        Map m = this.findUsers.execute(params);
        List content = (List) m.get("list");
        Long count = (Long) ((List) m.get("count")).get(0);
        Page<User> page = new Page(count, content);
        return page;
    }


    public void updateFailedLogin(String username) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource().addValue("username", username);
        update_failed_login.execute(in);
    }

    public List<User> getUserPreviousPasswords(Long userId) throws DataAccessException {
        SqlParameterSource params = new MapSqlParameterSource().addValue("userid", userId);
        Map<String, Object> m = getUserPreviousPasswords.execute(params);
        return (List<User>) m.get(MULTIPLE_RESULT);
    }

    public Long changePassword(Long userId, String password) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("password", password);
        Map<String, Object> m = changeUserPassword.execute(in);
        return (Long) m.get("userId");
    }


    public Long updatePassword(Long userId, String password) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource().addValue("userId", userId).addValue("password", password);
        Map<String, Object> m = updatePassword.execute(in);
        return (Long) m.get("userId");
    }

    public User loginUser(String username, String hospitalId) throws DataAccessException {

        Map<String, Object> m ;
        SqlParameterSource in =
                new MapSqlParameterSource()
                        .addValue("username", username)
                        .addValue("hospitalId", hospitalId);
//        if (agentCode == null)
//           m = loginUser.execute(in);
//        else
           m = loginMerchantUser.execute(in);

        List<User> result = (List<User>) m.get(SINGLE_RESULT);

        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }




    public User isUserExists(String username, String email) throws DataAccessException {
        MapSqlParameterSource in = (new MapSqlParameterSource())
                .addValue("username", username)
                .addValue("email", email);
        Map m = this.isUserExists.execute(in);
        List<User> result = (List<User>) m.get(SINGLE_RESULT);
        return !result.isEmpty() ? result.get(0) : null;
    }

    public void activateDeactivate(String username, int state) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource().addValue("username", username).addValue("active", state);
        activateDeactivate.execute(in);
    }


    public Page<User> findUserByCategory(Long pageNum, Long pageSize) throws DataAccessException {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("page_num", pageNum)
                .addValue("page_size", pageSize);


        Map m = this.findUserByCategory.execute(params);
        List content = (List) m.get("list");
        Long count = (Long) ((List) m.get("count")).get(0);
        Page<User> page = new Page(count, content);
        return page;
    }

    public Page<User> getHospitalUsers(Long pageNum, Long pageSize, String hospitalId) throws DataAccessException {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("page_num", pageNum)
                .addValue("page_size", pageSize)
                .addValue("hospitalId", hospitalId);

        Map m = this.getHospitalUsers.execute(params);
        List content = (List) m.get("list");
        Long count = (Long) ((List) m.get("count")).get(0);
        Page<User> page = new Page(count, content);
        return page;
    }

}
