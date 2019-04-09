package com.management.service;

import com.management.dao.AbstractDao;
import com.management.dao.UtilityDao;
import com.management.model.*;


import com.management.model.Settings.ApproveReject;
import com.management.model.Settings.CorporateSettings;
import com.management.model.Settings.DisableEnable;
import com.management.model.Settings.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UtilityService extends AbstractService<AbstractModel> {

    private static final Logger logger = LoggerFactory.getLogger(UtilityService.class);

    @Autowired
    public UtilityService(@Qualifier("utilityDao") AbstractDao<AbstractModel> dao) {
        super(dao);
    }


    public Long approveReject(ApproveReject data) {
        UtilityDao utilityDao = (UtilityDao) dao;
        return utilityDao.approveReject(data);
    }

    public Long disableEnable(DisableEnable data) {
        UtilityDao utilityDao = (UtilityDao) dao;
        return utilityDao.disableEnable(data);
    }

    public Long addCorporateSettings(CorporateSettings settings) {
        UtilityDao utilityDao = (UtilityDao) dao;
        return utilityDao.addCorporateSettings(settings);
    }

    public Page<CorporateSettings> getCorporateSettings(Long pageNum, Long pageSize) {
        UtilityDao utilityDao = (UtilityDao) dao;
        return utilityDao.getCorporateSettings(pageNum, pageSize);
    }


    public List<CorporateSettings> findSettings(String agentCode) {
        UtilityDao utilityDao = (UtilityDao) dao;
        return utilityDao.findSettings(agentCode);
    }

    public CorporateSettings isSettingExists(String setting, String agentCode) {
        UtilityDao utilityDao = (UtilityDao) dao;
        return utilityDao.isSettingExists(setting, agentCode);
    }

}
