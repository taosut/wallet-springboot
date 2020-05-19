package org.blockchain.wallet.async;

import org.blockchain.wallet.entity.TxDest;
import org.blockchain.wallet.entity.TxHistory;
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
import org.blockchain.wallet.service.FcmService;
import org.blockchain.wallet.service.TxDestService;
import org.blockchain.wallet.service.TxHistoryService;
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

    @Autowired
    TxHistoryService txHistoryService;

    @Autowired
    TxDestService txDestService;

    @Autowired
    FcmService fcmService;

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

    @Value("${blockchair.monitor.time.offset}")
    int timeOffset;

    @Value("${blockchair.monitor.txHistory.maxsize}")
    int txMaxSize;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

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

        Long lastReceiveTime = currentTime - blockchairBTCAddrObj.getBlockchairAddrAbstract().getAddress().getLast_seen_receiving().getTime();
        Long lastSendTime = currentTime-blockchairBTCAddrObj.getBlockchairAddrAbstract().getAddress().getLast_seen_spending().getTime();
        Long timeLimit = timeOffset*60*1000L;

        if(lastReceiveTime > timeLimit && lastSendTime > timeLimit) {
            return;
        }

        List<String> transactionsAll = blockchairBTCAddrObj.getBlockchairAddrAbstract().getTransactions();
        List<String> transactionsPart;

        transactionsPart = transactionsAll.subList(0,10);

        TxHistory txHistoryFind = new TxHistory();
        txHistoryFind.setSymbol("BTC");
        List<TxHistory> txHistoryList = txHistoryService.selectBySelective(txHistoryFind);
        for(TxHistory txHistory : txHistoryList) {
            while(transactionsPart.contains(txHistory.getTxHash())) {
                transactionsPart.remove(txHistory.getTxHash());
            }
        }

        BlockchairBTCTxObj blockchairBTCTxObj = blockChairRestAPI.getBTCTx(transactionsPart);

        for(BlockchairTxAbstract blockchairTxAbstract : blockchairBTCTxObj.getBlockchairTxAbstractList()) {

            Long txTime = 0L;
            try {
                txTime = blockchairTxAbstract.getTransaction().getTime().getTime();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                continue;
            }

            if(currentTime - txTime <= timeLimit) {
                Long inValue = 0L;
                StringBuilder inAddressListAndValue = new StringBuilder();
                for(BlockchairInputOrOutput blockchairInput : blockchairTxAbstract.getInputs()) {
                    if(blockchairInput.getRecipient().equals(address)) {
                        inValue += blockchairInput.getValue();
                    }
                    else if(blockchairInput.getValue() >= destValue*100000000) {

                        double destInValue = new BigDecimal(blockchairInput.getValue()).divide(new BigDecimal(100000000), 8, RoundingMode.HALF_DOWN).doubleValue();
                        insertTxDest(blockchairTxAbstract.getTransaction().getHash(), "BTC", blockchairInput.getRecipient(), String.valueOf(destInValue));
                        inAddressListAndValue.append("地址：" + blockchairInput.getRecipient() + "\r\n数目：-" + destInValue + "\r\n");
                    }
                }
                Double inValueDouble = new BigDecimal(inValue).divide(new BigDecimal(100000000), 8, RoundingMode.HALF_DOWN).doubleValue();

                Long outValue = 0L;
                StringBuilder outAddressListAndValue = new StringBuilder();
                for(BlockchairInputOrOutput blockchairOutput : blockchairTxAbstract.getOutputs()) {
                    if(blockchairOutput.getRecipient().equals(address)) {
                        outValue += blockchairOutput.getValue();
                    }
                    else if(blockchairOutput.getValue() >= destValue*100000000) {
                        double destOutValue = new BigDecimal(blockchairOutput.getValue()).divide(new BigDecimal(100000000), 8, RoundingMode.HALF_DOWN).doubleValue();
                        insertTxDest(blockchairTxAbstract.getTransaction().getHash(), "BTC", blockchairOutput.getRecipient(), String.valueOf(destOutValue));
                        outAddressListAndValue.append("地址：" + blockchairOutput.getRecipient() + "\r\n数目：+" + destOutValue + "\r\n");
                    }
                }
                Double outValueDouble = new BigDecimal(outValue).divide(new BigDecimal(100000000), 8, RoundingMode.HALF_DOWN).doubleValue();

                inValue = new BigDecimal(inValue).divide(new BigDecimal(100000000), 8, RoundingMode.HALF_DOWN).longValue();
                outValue = new BigDecimal(outValue).divide(new BigDecimal(100000000), 8, RoundingMode.HALF_DOWN).longValue();

                //编写邮件内容
                if(inValueDouble >= warnValueInBlockChair) {
                    fcmService.sendAllNotification("大额转账预警", "地址：" + address + "\n转出："+ inValue + " BTC");
                    insertTxHistory(blockchairTxAbstract.getTransaction().getHash(), "out", address, outAddressListAndValue.toString(), inValue.toString(), "BTC", blockchairTxAbstract.getTransaction().getTime());
                }
                if(outValueDouble >= warnValueInBlockChair) {
                    fcmService.sendAllNotification("大额转账预警", "地址：" + address + "\n转入："+ outValue + " BTC");
                    insertTxHistory(blockchairTxAbstract.getTransaction().getHash(), "in", address, inAddressListAndValue.toString(), outValue.toString(), "BTC", blockchairTxAbstract.getTransaction().getTime());
                }

                if(inValueDouble < warnValueInBlockChair && outValueDouble < warnValueInBlockChair)
                {
                    TxDest txDest = new TxDest();
                    txDest.setTxHash(blockchairTxAbstract.getTransaction().getHash());
                    txDestService.deleteTxDestBySelective(txDest);
                }

            } else {
                return;
            }
        }
    }

    private void insertTxHistory(String hash, String inOrOut, String address, String dest, String amount, String symbol, Date create_time) {

        TxHistory txHistory = new TxHistory();
        txHistory.setTxHash(hash);
        txHistory.setSymbol(symbol);
        if(txHistoryService.selectBySelective(txHistory).size() != 0) {
            return;
        }

        txHistory.setInOrOut(inOrOut);
        txHistory.setAddress(address);
        txHistory.setAmount(amount);
        txHistory.setCreateTime(create_time);

        txHistoryService.insertTxHistory(txHistory);

        while (txHistoryService.selectCount() > txMaxSize) {

            TxHistory oldestHistory = new TxHistory();
            oldestHistory.setSymbol("BTC");
            oldestHistory = txHistoryService.selectOldest(oldestHistory);
            txHistoryService.deleteByPrimaryKey(oldestHistory.getId());

            TxDest txDest = new TxDest();
            txDest.setTxHash(oldestHistory.getTxHash());
            txDestService.deleteTxDestBySelective(txDest);
        }
    }

    private void insertTxDest(String hash, String symbol, String address, String amount) {

        TxDest txDest = new TxDest();

        txDest.setTxHash(hash);
        txDest.setSymbol(symbol);
        txDest.setAddress(address);
        txDest.setAmount(amount);

        txDestService.insertTxDest(txDest);

    }
}
