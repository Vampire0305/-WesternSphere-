
package com.jobportal.JobPortal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.jobportal.JobPortal.DTO.EmailRequest;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(EmailRequest request) {
        SimpleMailMessage message =  new SimpleMailMessage();
        message.setTo(request.to);
        message.setSubject(request.subject);
        message.setText(request.body);

        mailSender.send(message);
    }

}
