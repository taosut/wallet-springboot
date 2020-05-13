package org.blockchain.wallet.service;

public interface EmailService {

    void SendSimpleEmail(String toEmailAdr, String subject, String text);
}
