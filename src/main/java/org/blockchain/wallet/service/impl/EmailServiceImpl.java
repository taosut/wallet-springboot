package org.blockchain.wallet.service.impl;

import org.blockchain.wallet.service.EmailService;
import org.blockchain.wallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    String fromEmailAdr;

    @Autowired
    JavaMailSenderImpl javaMailSender;

    @Autowired
    UserService userService;

    @Override
    @Async
    public void sendSimpleEmail(String toEmailAdr, String subject, String text) {

        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            //邮件发送人
            simpleMailMessage.setFrom(fromEmailAdr);
            //邮件接收人
            simpleMailMessage.setTo(toEmailAdr);
            //邮件主题
            subject = "[HD-Wallet] " + subject;
            simpleMailMessage.setSubject(subject);
            //邮件内容
            simpleMailMessage.setText(text);

            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendEmailByUid(int userId, String subject, String text) {

        String email = userService.findUserById(userId).getEmail();
        sendSimpleEmail(email, subject, text);
    }
}
