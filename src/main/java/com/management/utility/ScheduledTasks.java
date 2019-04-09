package com.management.utility;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

/**
 * Created by Lukman.Arogundade on 8/22/2016.
 */
@Component

public class ScheduledTasks {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

//
//    @Autowired
//    AsyncService asyncService;


//    @Scheduled(fixedRateString = "${sms-sender-scheduler-rate}")
//    public void SendQueuedSms() {
//        logger.debug("Sending Queued Sms Scheduler called");
//        this.asyncService.sendQueuedSms();
//    }
//
//    @Scheduled(fixedRateString = "${email-sender-scheduler-rate}")
//    public void SendQueuedEmails() {
//        logger.debug("Sending Queued Emails Scheduler called");
//        this.asyncService.sendQueuedEmails();
//    }

//    @Scheduled(fixedRateString = "${process-upload-scheduler-rate}")
//    public void processBeneficiaryUpload() {
//        logger.debug("Processing Beneficiary Upload Scheduler called");
//        this.asyncService.processBeneficiaryUpload();
//    }

//    @Scheduled(fixedRateString = "${load-data-scheduler-rate}")
//    public void loadData() {
//        logger.debug("Load Sms Scheduler called");
//        this.asyncService.loadData();
//    }
//
//    @Scheduled(fixedRateString = "${process-bulk-transaction-upload-beneficiary-rate}")
//    public void processBulkTransactionUpload() {
//        logger.debug("Processing Bulk Transaction Scheduler called");
//        this.asyncService.processBulkTransactionUpload();
//    }

}
