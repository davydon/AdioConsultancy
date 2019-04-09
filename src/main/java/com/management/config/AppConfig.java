package com.management.config;



import org.springframework.beans.factory.annotation.Value;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;


@Configuration
public class AppConfig {

    @Value("${resttemplate.timeout}")
    private int restTemplateTimeout;  //timeout in milliseconds

    @Value("${resttemplate.connection.timeout}")
    private int restTemplateConnectionTimeout; //connection timeout in milliseconds

    @Value("${maximum.file.upload}")  //MAXIMUM FILE SIZE FOR UPLOAD FOR THE APP
    private Long maximumFileUpload;

    //private int maxUploadSizeInMb = 5 * 1024 * 1024; // 5 MB




    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(5);
    }


/*
    private ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(restTemplateTimeout);
        factory.setConnectTimeout(restTemplateConnectionTimeout);
        return factory;
    }

    @Bean
    public RestTemplate createRestTemplate() {
        return  new RestTemplate(clientHttpRequestFactory());
    }
*/

/*
    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory factory =
                new TomcatEmbeddedServletContainerFactory();
        return factory;
    }*/


    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
/*
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver=new CommonsMultipartResolver();
        resolver.setDefaultEncoding("utf-8");
        resolver.setMaxUploadSize(maximumFileUpload);
        return resolver;
    }*/
/*
    @Bean
    @Order(0)
    public MultipartFilter multipartFilter() {
        MultipartFilter multipartFilter = new MultipartFilter();
        multipartFilter.setMultipartResolverBeanName("multipartResolver");
        return multipartFilter;
    }*/








    }






