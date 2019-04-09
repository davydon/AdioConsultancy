package com.management.api;


import com.management.exceptions.ConflictException;
import com.management.model.Recruitment.Application;
import com.management.model.Settings.Page;
import com.management.model.Settings.Response;
import com.management.model.Users.User;
import com.management.service.Recruitment.ApplicationService;
import com.management.service.TokenService;
import com.management.utility.CustomResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api")
public class ApplicationController {

    private final static Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @Autowired
    ApplicationService service;

    @RequestMapping(value = "/application", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Response> create(@RequestBody @Validated Application application, HttpServletRequest request) {
        HttpStatus httpCode;
        Response resp = new Response();

        Application isExist = service.isApplicationExists(application.getFirstName(),application.getSurName());

        User user = TokenService.getCurrentUserFromSecurityContext();
        if (isExist != null) {
            throw new ConflictException(CustomResponseCode.CONFLICT_EXCEPTION, " Application already exist");
        } else {

            service.create(application);
            resp.setCode(CustomResponseCode.SUCCESS);
            resp.setDescription("Successful");
            httpCode = HttpStatus.CREATED;

        }
        return new ResponseEntity<>(resp, httpCode);
    }

    @RequestMapping(value = "/applications", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public Page<Application> getApplications(
            @RequestParam(value = "pageNum", defaultValue = "1") Long pageNum,
            @RequestParam(value = "pageSize", defaultValue = "20") Long pageSize) {
        return service.getApplications(pageNum, pageSize);
    }


}
