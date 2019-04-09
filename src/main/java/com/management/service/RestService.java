package com.management.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Omoede.Aihe on 11/11/2015.
 */

@Service
public class RestService {

    private final static Logger logger = LoggerFactory.getLogger(RestService.class);


    protected RestTemplate restTemplate;
    protected HttpHeaders httpHeaders;

    @PostConstruct
    public void init() {
        this.restTemplate = new RestTemplate();
        this.httpHeaders = new HttpHeaders();
        this.httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        this.httpHeaders.set("Accept", "application/json");
        this.setErrorHandler();
    }

    private void setErrorHandler() {
        this.restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                logger.debug("Response Status Code: " + response.getRawStatusCode());
                logger.debug("Response Status Text: " + response.getStatusText());
                if (response.getRawStatusCode() == 404) {//usually there is no response to parse in this case
                    logger.debug("Response Body: " + new String(getResponseBody(response)));
                    return true;
                }
                return false; //we want to be able to parse the error response
            }
        });
    }

    private byte[] getResponseBody(ClientHttpResponse response) {

        try {
            InputStream responseBody = response.getBody();
            if (responseBody != null) {
                return FileCopyUtils.copyToByteArray(responseBody);
            }
        } catch (IOException ex) {
            // ignore
        }
        return new byte[0];
    }

}
