package com.management.service;

import com.management.dao.AbstractDao;
import com.management.dao.UserDao;
import com.management.model.Users.LoginRequest;
import com.management.model.Settings.Page;
import com.management.model.Users.User;
//import com.xpresspayment.utility.EmailSmsUtil;
//import net.sf.ehcache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Lukman.Arogundade on 10/18/2016.
 */

@Service
public class UserService extends AbstractService<User> {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    //private EmailSmsUtil emailSmsUtil;

    @Value("${token.time.to.leave}")
    long tokenTimeToLeave;

    @Autowired
    public UserService(@Qualifier("userDao") AbstractDao<User> dao) {
        super(dao);
    }

    @Override
    public User create(User user) {
        return super.create(user);
    }

    public User find(Long userId) {

        UserDao userDao = (UserDao) dao;
        return userDao.find(userId);
    }

    public User findByUsername(String username) {
        UserDao userDao = (UserDao) dao;
        return userDao.findByUsername(username);
    }

    public User getUserByParameter(String username) {
        UserDao userDao = (UserDao) dao;
        return userDao.getUserByParameter(username);
    }


    public Page<User> findUsers(Long pageNum, Long pageSize, Long userId) {
        UserDao userDao = (UserDao) dao;
        return userDao.findUsers(pageNum, pageSize, userId);
    }

    public void updateLogin(String username, Boolean passwordMatched) {
        UserDao userDao = (UserDao) dao;
        userDao.updateLogin(username, passwordMatched);
    }


    public void lockLogin(Long userId) {
        UserDao userDao = (UserDao) dao;
        userDao.lockLogin(userId);
    }

    public void unlockLogin(Long userid) {
        UserDao userDao = (UserDao) dao;
        userDao.unlockLogin(userid);
    }

    public void updateFailedLogin(String username) {
        UserDao userDao = (UserDao) dao;
        userDao.updateFailedLogin(username);
    }


    public Boolean passwordUsedRecently(Long userid, String password) {
        UserDao userDao = (UserDao) dao;
        List<User> passwords = userDao.getUserPreviousPasswords(userid);
        for (User user : passwords) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public Long changePassword(Long userId, String password) {
        UserDao userDao = (UserDao) dao;
        String encoded = passwordEncoder.encode(password);
        return userDao.changePassword(userId, encoded);
    }

    public Long updatePassword(Long userId, String password) {
        UserDao userDao = (UserDao) dao;
        String encoded = passwordEncoder.encode(password);
        return userDao.updatePassword(userId, encoded);
    }


    public boolean validUserAccount(String userName, String password) {
        UserDao userDao = (UserDao) dao;
        User user = userDao.loginUser(userName, userName);
        if (null == user) {
            return false;
        }else {
            if (passwordEncoder.matches(password, user.getPassword())) {
                return true;
            }
        }

        return false;
    }

    public User loginUser(LoginRequest loginRequest) {
        UserDao userDao = (UserDao) dao;
        User user = userDao.loginUser(loginRequest.getUsername(), loginRequest.getHospitalId());


        if (null == user) {
            return null;
        } else {
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {

                user.setLoginStatus(true);
            } else {
                user.setLoginStatus(false);
            }

            return user;
        }


       /* if(null == user){
            return null;
        }
        else if(passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())){
            return user;
        }

        return null;*/
    }


    @Override
    public void delete(Long id) {
        UserDao userDao = (UserDao) dao;
        userDao.delete(id);
    }

    @Override
    public Long update(User user) {
        super.update(user);
        return null;
    }


    public User isUserExists(String username, String email) {
        UserDao userDao = (UserDao) dao;
        return userDao.isUserExists(username, email);
    }

    public void activateDeactivate(String username, int state) {
        UserDao userDao = (UserDao) dao;
        userDao.activateDeactivate(username, state);
    }

    public Page<User> findUserByCategory(Long pageNum, Long pageSize) {
        UserDao userDao = (UserDao) dao;
        return userDao.findUserByCategory(pageNum, pageSize);
    }

    public Page<User> getHospitalUsers(Long pageNum, Long pageSize, String hospitalId) {
        UserDao userDao = (UserDao) dao;
        return userDao.getHospitalUsers(pageNum, pageSize, hospitalId);
    }


    public String activateAccount(String activationCode, String username) {
        UserDao userDao = (UserDao) dao;
        return userDao.activateAccount(activationCode, username);
    }

    public void updateAccountCode(Long userId, String code) {
        UserDao userDao = (UserDao) dao;
        userDao.updateAccountCode(userId, code);
    }

    public long getSessionExpiry() {
        //TODO Token expiry in seconds: 900 = 15mins
        return tokenTimeToLeave / 60;
    }

}
