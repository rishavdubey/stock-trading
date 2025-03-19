package com.example.stocktrading.service;


import com.example.stocktrading.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${smtp.mail.from}")
    private String fromEmail;
    @Value("${smtp.mail.reply}")
    private String replyEmail;

    private static final Map<String,String> templateFileMap=new HashMap<>();


    private final Logger logger= LoggerFactory.getLogger(EmailService.class);

    @Async
    public void  sendSimpleEmail(String toEmail, String subject, String templateFileName,Map<String, String> values) {
        String templateFileData=getTemplateFileData(templateFileName);
        String bodyContent=generateEmailBody(templateFileData,values);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setReplyTo(replyEmail);
        message.setSubject(subject);
        message.setText(bodyContent);
        message.setTo(toEmail);
        logger.debug("message Object :: {}",message);
        mailSender.send(message);
    }

    private String getTemplateFileData(String templateFileName) {
        if( templateFileMap.containsKey(templateFileName)) {
            return templateFileMap.get(templateFileName);
        }
        String currDir = System.getProperty("user.dir");
        logger.debug("Current Directory :: {} ", currDir);

        String templateFilePath = currDir + "/Config/template/" + templateFileName;
        logger.debug("Template File Path:: {} ", templateFilePath);

        try {
            String template = Files.readString(Paths.get(templateFilePath));
            templateFileMap.put(templateFileName,template);
            return templateFileMap.get(templateFileName);
        } catch (IOException e) {
            throw new CustomException("Exception occur while reading email template");
        }
    }
    private String generateEmailBody(String template, Map<String, String> values) {
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String placeholder = "#{" + entry.getKey() + "}";
            logger.info("PlaceHolder :: {}", placeholder);
            template = template.replace(placeholder, entry.getValue());
        }
        return template;
    }

}
