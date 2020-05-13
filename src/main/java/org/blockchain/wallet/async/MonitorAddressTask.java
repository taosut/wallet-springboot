package org.blockchain.wallet.async;

import org.blockchain.wallet.entity.blockchain.BlockChainSingleAdr;
import org.blockchain.wallet.entity.blockchain.BlockChainTxs;
import org.blockchain.wallet.entity.blockchain.BlockChainTxsInput;
import org.blockchain.wallet.entity.blockchain.BlockChainTxsOut;
import org.blockchain.wallet.entity.blockchair.BlockchairBTCAddrObj;
import org.blockchain.wallet.entity.blockchair.BlockchairBTCTxObj;
import org.blockchain.wallet.entity.blockchair.BlockchairInputOrOutput;
import org.blockchain.wallet.entity.blockchair.BlockchairTxAbstract;
import org.blockchain.wallet.resttemplate.BlockChainRestAPI;
import org.blockchain.wallet.resttemplate.BlockChairRestAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class MonitorAddressTask {

    @Autowired
    BlockChainRestAPI blockChainRestAPI;

    @Autowired
    BlockChairRestAPI blockChairRestAPI;

    @Value("${blockchain.monitor.query.limit}")
    Long limit;

    @Value("${blockchain.monitor.query.offset}")
    Long offset;

    @Value("${blockchain.monitor.warn.value}")
    Double warnValueInBlockChain;

    @Value("${blockchair.monitor.warn.value}")
    Double warnValueInBlockChair;

    @Value("${blochchair.monitor.address.dest.value}")
    Double destValue;

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Async
    public void monitorBTCByBlockChain(String address, Long currentTime) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BlockChainSingleAdr blockChainSingleAdr = blockChainRestAPI.getSingleAddress(address, limit, offset);
        for(BlockChainTxs blockChainTx : blockChainSingleAdr.getTxs()) {
            if(currentTime - blockChainTx.getTime()*1000 <= 21*1000) {
                Long inValue = 0L;
                for(BlockChainTxsInput blockChainTxsInput : blockChainTx.getInputs()) {
                    if(blockChainTxsInput.getPrev_out().getAddr().equals(address)) {
                        inValue += blockChainTxsInput.getPrev_out().getValue();
                    }
                }
                Double inValueDouble = new BigDecimal(inValue).divide(new BigDecimal(100000000), 8, RoundingMode.HALF_DOWN).doubleValue();
                if(inValueDouble > warnValueInBlockChain) {
//                    for(EmailInfo toEmail : toEmailList) {
//                        String txUrl = "Txid: https://www.blockchain.com/btc/tx/" + blockChainTx.getHash();
//                        String dateStr = simpleDateFormat.format(new Date(blockChainTx.getTime()*1000));
//                        String subject = "链上异动提醒";
//                        String text = "BTC地址：" + address+ "，转出：" + inValueDouble  + "，于" + dateStr + "\n" + txUrl;
//                        sendEmail.SendSimpleEmail(toEmail.getEmail(), subject, text);
//                    }
                }

                Long outValue = 0L;
                for(BlockChainTxsOut blockChainTxsOut : blockChainTx.getOut()) {
                    if(blockChainTxsOut.getAddr().equals(address)) {
                        outValue += blockChainTxsOut.getValue();
                    }
                }
                Double outValueDouble = new BigDecimal(outValue).divide(new BigDecimal(100000000), 8, RoundingMode.HALF_DOWN).doubleValue();
                if(outValueDouble > warnValueInBlockChain) {
//                    for(EmailInfo toEmail : toEmailList) {
//                        String txUrl = "Txid: https://www.blockchain.com/btc/tx/" + blockChainTx.getHash();
//                        String dateStr = simpleDateFormat.format(new Date(blockChainTx.getTime()*1000));
//                        String subject = "链上异动提醒";
//                        String text = "BTC地址：" + address+ "，转入：" + inValueDouble  + "，于" + dateStr + "\n" + txUrl;
//                        sendEmail.SendSimpleEmail(toEmail.getEmail(), subject, text);
//                    }
                }
            }
        }
    }

    @Async
    public void monitorBTCByBlockChair(String address, Long currentTime) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BlockchairBTCAddrObj blockchairBTCAddrObj = blockChairRestAPI.getBTCAddress(address);
        List<String> transactionsAll = blockchairBTCAddrObj.getBlockchairAddrAbstract().getTransactions();

        for(int i=0;i<transactionsAll.size();i+=10) {
            List<String> transactionsPart;
            if(transactionsAll.size() - i >= 10) {
                transactionsPart = transactionsAll.subList(i,i+10);
            } else {
                transactionsPart = transactionsAll.subList(i,transactionsAll.size());
            }

            BlockchairBTCTxObj blockchairBTCTxObj = blockChairRestAPI.getBTCTx(transactionsPart);

            for(BlockchairTxAbstract blockchairTxAbstract : blockchairBTCTxObj.getBlockchairTxAbstractList()) {
                Long txTime = 0L;
                try {
                    txTime = simpleDateFormat.parse(blockchairTxAbstract.getTransaction().getTime()).getTime();
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                    continue;
                }

                if(currentTime - txTime <= 2*60*1000) {
                    Long inValue = 0L;
                    StringBuilder inAddressListAndValue = new StringBuilder("\n主要接收地址：\n");
                    for(BlockchairInputOrOutput blockchairInput : blockchairTxAbstract.getInputs()) {
                        if(blockchairInput.getRecipient().equals(address)) {
                            inValue += blockchairInput.getValue();
                        }
                        else if(blockchairInput.getValue() >= destValue*100000000) {
                            inAddressListAndValue.append(blockchairInput.getRecipient() + "，数目：" + new BigDecimal(blockchairInput.getValue()).divide(new BigDecimal(100000000), 8, RoundingMode.HALF_DOWN).doubleValue() + "\n");
                        }
                    }
                    Double inValueDouble = new BigDecimal(inValue).divide(new BigDecimal(100000000), 8, RoundingMode.HALF_DOWN).doubleValue();

                    Long outValue = 0L;
                    StringBuilder outAddressListAndValue = new StringBuilder("\n主要发送地址：\n");
                    for(BlockchairInputOrOutput blockchairOutput : blockchairTxAbstract.getOutputs()) {
                        if(blockchairOutput.getRecipient().equals(address)) {
                            outValue += blockchairOutput.getValue();
                        }
                        else if(blockchairOutput.getValue() >= destValue*100000000) {
                            outAddressListAndValue.append(blockchairOutput.getRecipient() + "，数目：" + new BigDecimal(blockchairOutput.getValue()).divide(new BigDecimal(100000000), 8, RoundingMode.HALF_DOWN).doubleValue() + "\n");
                        }
                    }
                    Double outValueDouble = new BigDecimal(outValue).divide(new BigDecimal(100000000), 8, RoundingMode.HALF_DOWN).doubleValue();


                    //编写邮件内容
                    if(inValueDouble > warnValueInBlockChair) {

                    }
                    if(outValueDouble > warnValueInBlockChair) {

                    }

                } else {
                    return;
                }
            }
        }
    }
}
