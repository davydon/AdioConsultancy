package com.management.utility;


import com.management.model.Settings.LookUp;
import com.management.model.Settings.UrlLookUp;
import com.management.service.LookUpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Created by Lukman.Arogundade on 11/17/2016.
 */

@Service
public class LoadData {

    private static final Logger logger = LoggerFactory.getLogger(LoadData.class);

    @Autowired
    private LookUpService lookUpService;

    private Map<String, List<LookUp>> allLookUps;


    public LoadData() {

    }

    @PostConstruct
    public void init() {
        loadLookUps();
    }

    public void loadLookUps() {

        try {
            logger.debug("START LOADING LOOK UPS");
            allLookUps = lookUpService.lookUpList("0");
            logger.debug("END LOADING LOOK UPS");

        } catch (Exception ex) {
            logger.error("loadLookUps(): " + ex.getMessage());
            LoggerUtil.logError(logger, ex);
        }
    }

    public LookUp getGenericSingleValue(String lookUpType) {
        LookUp values = null;
        try {

            values = allLookUps.get(lookUpType.trim()).get(0);

        } catch (Exception ex) {
            logger.error("getGenericSingleValue ():" + ex.getMessage());
            LoggerUtil.logError(logger, ex);
        }

        return values;
    }

    public List<LookUp> getGeneric(String lookUpType) {
        List<LookUp> values = null;
        try {

            values = allLookUps.get(lookUpType.trim());

        } catch (Exception ex) {
            logger.error("getGeneric ():" + ex.getMessage());
            LoggerUtil.logError(logger, ex);
        }

        return values;
    }

    public UrlLookUp getURlList(String lookUpType) {
        final UrlLookUp urlValues = new UrlLookUp();
        try {

            List<LookUp> urls = getGeneric(lookUpType.trim());
            urls.forEach(item -> {
                if (item.getLookupSort() != null && "s".equalsIgnoreCase(item.getLookupSort())) {
                    urlValues.setUrlNames(item.getLookupDescription().trim());
                    urlValues.setUrls(item.getLookupValue().trim());
                } else {
                    urlValues.setUrlName(item.getLookupDescription().trim());
                    urlValues.setUrl(item.getLookupValue().trim());
                }

            });
        } catch (Exception ex) {
            logger.error("getURlList():" + ex.getMessage());
            LoggerUtil.logError(logger, ex);
        }
        return urlValues;
    }


    public Map<String, List<LookUp>> lookUpListWithGrouping(String param) {

        return allLookUps = lookUpService.lookUpList(param);

    }
}
