package com.management.NotificationService;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Service
public class EmailMessageService {



    @Value("${mail.username}")
    private String username;
    @Value("${mail.password}")
    private String password;



    public void sendMail(EmailMessage emailMessage) throws MessagingException {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.port","587");


        Session session = Session.getInstance(props,
                new javax.mail.Authenticator(){
                    protected PasswordAuthentication getPasswordAuthentication(){
                        return new PasswordAuthentication(username,password);
                    }

                });
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(username,false));
        msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(emailMessage.getTo_address()));
        msg.setSubject(emailMessage.getSubject());
        msg.setContent(emailMessage.getBody(),"text/html");
        msg.setSentDate(new Date());




//        MimeBodyPart messageBodyPart = new MimeBodyPart();
//        messageBodyPart.setContent(emailMessage.getBody(),"text/html");
//
//        Multipart multipart = new MimeMultipart();
//        multipart.addBodyPart(messageBodyPart);
//        MimeBodyPart attachPart = new MimeBodyPart();
//        attachPart.attachFile();
//
//        Multipart.addBodyPart(attachPart);
//        msg.setContent(multipart);

        Transport.send(msg);




    }
}
