package org.blockchain.wallet.controller;

import org.blockchain.wallet.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@RestController
@RequestMapping(value = "/email")
public class EmailController {

    @Autowired
    EmailService emailService;

    @GetMapping(value = "/send")
    public void sendEmail(@RequestParam int userId, @RequestParam String subject, @RequestParam String text) {
        emailService.sendEmailByUid(userId, subject, text);

    }
}
