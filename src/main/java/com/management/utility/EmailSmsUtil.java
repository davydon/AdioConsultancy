package com.management.utility;

//
//import com.xpresspayment.model.Settings.CalenderModel;
//import com.xpresspayment.service.EmailService;
//import com.xpresspayment.service.SmsService;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.io.File;
//
///**
// * Created by Lukman.Arogundade on 7/1/2015.
// */
//
//@Service
//public class EmailSmsUtil {
//
//    private static final Logger logger = LoggerFactory.getLogger(EmailSmsUtil.class);
//
//    @Value("${external-resource-location}")
//    String externalResourceLocation;
//
//    @Value("${template.path}")
//    private String templatePath;
//
//    @Value("${image.path}")
//    private String imagePath;
//
//
//    @Value("${from.email}")
//    private String senderEmail;
//
//
//    @Value("${portal.url}")
//    private String portalURL;
//
//
//    @Value("${email.template}")
//    private String emailTemplate;
//
//    @Value("${email.login.template}")
//    private String emailLoginTemplate;
//
//    @Value("${forget.password.template}")
//    private String forgetPassTemplate;
//
//
//
//    @Value("${sms.template}")
//    private String smsTemplate;
//
//    @Value("${sms.sender}")
//    private String smsSender;
//
//
//
//    @Value("${admin.email.template}")
//    private String adminTemplate;
//
//    private Document htmlFile;
//
//
//    @Autowired
//    private EmailService emailService;
//    @Autowired
//    private SmsService smsService;
//
//
//    public boolean SendEmail(String fromEmail, String toEmail, String subject, String templateType, String password, String username,
//                             String name, String url, String loginStatus,String activationCode) {
//        boolean retval = false;
//
////
////        try {
////            String contentType = "text/html";
////           // this.emailService.queueEmail(senderEmail, toEmail, subject, contentType,
////                    htmlParser(password, name, url, templateType, loginStatus, activationCode,username));
////
////            retval = true;
////        } catch (Exception ex) {
////            logger.error("Send Email Exception: " + ex.getMessage());
////            LoggerUtil.logError(logger, ex);
////        }
////
////        return retval;
////    }
//
//
//    private String htmlParser(String password, String name, String url,
//                              String templateType, String loginStatus,String activationCode,String username) {
//        String content = null;
//
//        String emailTemp = "";
//        String page ="";
//        String imageLogo;
//
//
//        try {
//
//            CalenderModel cal = new CalenderModel();
//
//            if (CustomResponseCode.LOGIN.equalsIgnoreCase(templateType))
//                emailTemp = emailLoginTemplate;
//            if (CustomResponseCode.FORGET_PASSWORD.equalsIgnoreCase(templateType)) {
//                emailTemp = forgetPassTemplate;
//                page="updatepassword";
//             }
//            if (CustomResponseCode.CREATE_USER.equalsIgnoreCase(templateType)) {
//                emailTemp = emailTemplate;
//                page="activateUser";
//            }
//
//            String fileDir = externalResourceLocation + templatePath + emailTemp;
//            htmlFile = Jsoup.parse(new File(fileDir), "UTF-8");
//
//            String body = htmlFile.body().toString();
//
//            String pathLogo = url + "/pub" + imagePath + "";
//
//            imageLogo = "<img src=" + "'" + pathLogo + "'" + " alt='xpressPayout logo' width='153' height='67' style='display: block; float:right;'/>";
//
//            //replace images
//
//            content = body.replace("[image_logo]", imageLogo);
//            //replace contents
//            content = content.replace("[name]", name);
//            content = content.replace("[alertMsg]", loginStatus);
//
//           // content  = content.replace("[name]", name);
//            content  = content.replace("[URL]", emailUrlContent(activationCode,url,page,username));
//
//            content = content.replace("[password]", password);
//            content = content.replace("[date]", cal.getDate().toString());
//
//
//        } catch (Exception e) {
//            logger.error("HTML Parser Exception: " + e.getMessage());
//            LoggerUtil.logError(logger, e);
//        }
//        return content;
//    }
//
//
//    public boolean sendSMS(String phoneNo, String username) {
//        boolean retval = false;
//        try {
//
//            if (phoneNo.startsWith("0")) {
//
//                phoneNo = "+234" + phoneNo.substring(1, phoneNo.length());
//            } else if (phoneNo.startsWith("234")) {
//                phoneNo = "+" + phoneNo;
//            }
//
//            String fileDir = externalResourceLocation + templatePath + smsTemplate;
//            String content = Utility.fileContentsToString(fileDir);
//            content = content.replace("[username]", username);
//            //this.smsService.queueSms(smsSender, phoneNo, content);
//
//            retval = true;
//        } catch (Exception e) {
//            logger.error("Send SMS Exception: " + e.getMessage());
//            LoggerUtil.logError(logger, e);
//        }
//
//        return retval;
//    }
//
//    public String emailUrlContent(String activationCode, String uri, String page, String username) {
//        String serverURL = null;
//        try {
//            serverURL = uri + "/" + page + "/" + activationCode + "/" + username;
//        } catch (Exception e) {
//            logger.error("EmailUrlContent  Exception: " + e.getMessage());
//            LoggerUtil.logError(logger, e);
//        }
//        return serverURL;
//    }
//
//
//}
