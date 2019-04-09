package com.management.service;


import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class AsyncService {


//    @Autowired
//    private SmsService smsService;
//
//    @Autowired
//    private EmailService emailService;
//
//  @Autowired
//  private LoadData loadData;
//
//    @Autowired
//    private TransactionService transactionService;

    @Async
    public void sendQueuedSms() {
       // this.smsService.sendQueuedSms();
    }

    @Async
    public void sendQueuedEmails() {
      //  this.emailService.sendQueuedEmails();
    }

//    @Async
//    public void processBeneficiaryUpload() {
//        this.transactionService.processBeneficiaryUpload();
//    }

//    @Async
//    public void loadData() {
//      this.loadData.loadLookUps();
//    }
//
//
//    @Async
//    public void processBulkTransactionUpload() {
//        this.transactionService.processBulkTransactionUpload();
//    }
}
