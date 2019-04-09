package com.management.api;


import com.management.exceptions.BadRequestException;
import com.management.exceptions.ConflictException;
import com.management.exceptions.NotFoundException;

import com.management.model.Settings.*;
import com.management.model.Users.User;
import com.management.service.TokenService;
import com.management.service.UtilityService;
import com.management.utility.CustomResponseCode;
import com.management.utility.LoadData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class UtilityController {

    @Autowired
    private LoadData loadData;

    @Autowired
    private UtilityService utilityService;

    @RequestMapping(value = "/lookup/{lookUpName}", method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
     @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_VIEW_USER')")
    public List<LookUp> getLookUpDetails(@PathVariable String lookUpName) throws Exception {

        if (lookUpName == null || lookUpName.isEmpty())
            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Parameter cannot be empty");

        List<LookUp> lookUp = loadData.getGeneric(lookUpName);




        if (lookUp.size() < 1) {
            throw new NotFoundException(CustomResponseCode.NOT_FOUND_EXCEPTION, "View does not exist.");
        }

        return lookUp;
    }


    @RequestMapping(value = "/corporatesettings", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_CORPORATE_SETTING')")
    public ResponseEntity<?> addCorporateSettings(@RequestBody @Validated CorporateSettings settings) throws Exception {

        User user = TokenService.getCurrentUserFromSecurityContext();
        settings.setMaker(user.getId());
        settings.setCreatedBy(user.getId());

        CorporateSettings check = utilityService.isSettingExists(settings.getSetting(), settings.getCorporateId());

        if (check != null) {

            throw new ConflictException(CustomResponseCode.CONFLICT_EXCEPTION, "Corporate Settings already set up");
        }

        HttpStatus httpCode = HttpStatus.INTERNAL_SERVER_ERROR;
        Response resp = new Response();


        if (user.getId().longValue() < 1L) {
            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid user");
        }

        Long add = utilityService.addCorporateSettings(settings);

        if (add != null && add > 0L) {
            resp.setCode(CustomResponseCode.SUCCESS);
            resp.setDescription("Corporate Setting Successful");
            httpCode = HttpStatus.OK;
        } else {
            resp.setCode(CustomResponseCode.FAILED);
            resp.setDescription("Failed");
        }

        return new ResponseEntity<>(resp, httpCode);

    }


    @RequestMapping(value = "/corporatesettings/paging", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_CORPORATE_SETTING')")
    public Page<CorporateSettings> getCorporateSettings(
            @RequestParam(value = "pageNum", defaultValue = "1") Long pageNum,
            @RequestParam(value = "pageSize", defaultValue = "20") Long pageSize) throws Exception {


        return utilityService.getCorporateSettings(pageNum, pageSize);
    }





}
