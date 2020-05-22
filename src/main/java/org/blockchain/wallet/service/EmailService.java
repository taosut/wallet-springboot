package org.blockchain.wallet.service;

public interface EmailService {

    void sendSimpleEmail(String toEmailAdr, String subject, String text);

    void sendEmailByUid(int userId, String subject, String text);
}
