package com.management.service;


import com.management.dao.AbstractDao;
import com.management.dao.LookUpDao;
import com.management.model.Settings.LookUp;
import com.management.model.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Lukman.Arogundade on 11/17/2016.
 */

@Service
public class LookUpService extends AbstractService<LookUp> {

    @Autowired
    public LookUpService(@Qualifier("lookUpDao") AbstractDao<LookUp> dao) {
        super(dao);
    }


    public Map<String, List<LookUp>> lookUpList(String param) {
        LookUpDao lookUpDao = (LookUpDao) dao;

        Map<String, List<LookUp>> mappedList = lookUpDao.lookUpList(param).stream()
                .collect(Collectors.groupingBy(lookUp -> lookUp.getLookupType()));

        return mappedList;
    }

    public Long addUpdateLookUp(LookUp lookUp) {

        LookUpDao lookUpDao = (LookUpDao) dao;
        return lookUpDao.addUpdateLookUp(lookUp);
    }

    public Long verifyLookUp(LookUp lookUp) {
        User userCurrent = TokenService.getCurrentUserFromSecurityContext();
        lookUp.setCreatedBy(userCurrent.getUsername());
        lookUp.setLastUpdatedBy(userCurrent.getUsername());

        LookUpDao lookUpDao = (LookUpDao) dao;
        return lookUpDao.verifyLookUp(lookUp);
    }
}
