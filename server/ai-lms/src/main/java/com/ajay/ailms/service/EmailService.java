package com.ajay.ailms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private JavaMailSender emailSender;

    public void sendOtp(String email,String otp){
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Password Reset Otp");
        mailMessage.setText("Your Otp: "+otp+"\n This is valid only for 5 minutes");
        emailSender.send(mailMessage);
    }
}
