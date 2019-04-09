package com.management.service.Recruitment;


import com.management.dao.AbstractDao;
import com.management.dao.ApplicationDao;
import com.management.model.Recruitment.Application;
import com.management.model.Settings.Page;
import com.management.service.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService extends AbstractService<Application> {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);
    @Autowired
    public ApplicationService(@Qualifier("applicationDao") AbstractDao<Application> dao) { super(dao); }

    @Override
    public Application create(Application application) {
        return super.create(application);
    }

    public Page<Application> getApplications(Long pageNum, Long pageSize) {
        ApplicationDao applicationDao = (ApplicationDao) dao;
        return applicationDao.getApplications(pageNum, pageSize);
    }


    public Application isApplicationExists(String firstName, String surName) {
        ApplicationDao applicationDao = (ApplicationDao) dao;
        return applicationDao.isApplicationExists(firstName, surName);
    }
}
