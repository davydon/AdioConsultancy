package com.management;




import com.management.model.AbstractModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@EnableEncryptableProperties

@EnableScheduling


@EnableTransactionManagement

public class Application extends AbstractModel {




    public static void main(String ... args){
       SpringApplication.run(Application.class, args);
      //  SpringApplication.run(new Object[]{Application.class, ScheduledTasks.class}, args);
    }


}
