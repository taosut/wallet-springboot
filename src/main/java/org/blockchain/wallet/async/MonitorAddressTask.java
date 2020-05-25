package org.blockchain.wallet.async;

import org.blockchain.wallet.entity.TxHistory;
import org.blockchain.wallet.entity.blockchain.BlockChainSingleAdr;
import org.blockchain.wallet.entity.blockchain.BlockChainTxs;
import org.blockchain.wallet.entity.blockchain.BlockChainTxsInput;
import org.blockchain.wallet.entity.blockchain.BlockChainTxsOut;
import org.blockchain.wallet.entity.blockchair.BlockchairAddrAbstract;
import org.blockchain.wallet.entity.blockchair.BlockchairAddrTx;
import org.blockchain.wallet.resttemplate.BlockChainRestAPI;
import org.blockchain.wallet.resttemplate.BlockChairRestAPI;
import org.blockchain.wallet.service.FcmService;
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
    FcmService fcmService;

    @Value("${blockchain.monitor.query.limit}")
    Long limit;

    @Value("${blockchain.monitor.query.offset}")
    Long offset;

    @Value("${blockchain.monitor.warn.value}")
    Double warnValueInBlockChain;

    @Value("${blockchair.monitor.warn.value}")
    Double warnValueInBlockChair;

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

        BlockchairAddrAbstract blockchairAddrAbstract = blockChairRestAPI.getBTCAddress(address);

        Long lastReceiveTime = currentTime - blockchairAddrAbstract.getAddress().getLast_seen_receiving().getTime();
        Long lastSendTime = currentTime-blockchairAddrAbstract.getAddress().getLast_seen_spending().getTime();
        Long timeLimit = timeOffset*60*1000L;

        if(lastReceiveTime > timeLimit && lastSendTime > timeLimit) {
            return;
        }

        List<BlockchairAddrTx> transactionsAll = blockchairAddrAbstract.getTransactions();

        TxHistory txHistoryFind = new TxHistory();
        txHistoryFind.setSymbol("BTC");
        List<TxHistory> txHistoryList = txHistoryService.selectBySelective(txHistoryFind);
        for(TxHistory txHistory : txHistoryList) {
            while(transactionsAll.contains(txHistory.getTxHash())) {
                transactionsAll.remove(txHistory.getTxHash());
            }
        }

        for(BlockchairAddrTx blockchairAddrTx : transactionsAll) {

            Long txTime = 0L;
            try {
                txTime = blockchairAddrTx.getTime().getTime();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                continue;
            }

            if(currentTime - txTime <= timeLimit) {

                if (blockchairAddrTx.getBalance_change() < 0) {
                    Long inValue = 0 - blockchairAddrTx.getBalance_change();
                    inValue = new BigDecimal(inValue).divide(new BigDecimal(100000000), 8, RoundingMode.HALF_DOWN).longValue();
                    if(inValue >= warnValueInBlockChair) {
                        fcmService.sendAllNotification("大额转账预警", "地址：" + address + "\n转出："+ inValue + " BTC");
                        insertTxHistory(blockchairAddrTx.getHash(), "out", address, inValue.toString(), "BTC", blockchairAddrTx.getTime());
                    }
                }
                if (blockchairAddrTx.getBalance_change() > 0) {
                    Long outValue = blockchairAddrTx.getBalance_change();
                    outValue = new BigDecimal(outValue).divide(new BigDecimal(100000000), 8, RoundingMode.HALF_DOWN).longValue();
                    if(outValue >= warnValueInBlockChair) {
                        fcmService.sendAllNotification("大额转账预警", "地址：" + address + "\n转入："+ outValue + " BTC");
                        insertTxHistory(blockchairAddrTx.getHash(), "in", address,  outValue.toString(), "BTC", blockchairAddrTx.getTime());
                    }
                }
            } else {
                return;
            }
        }
    }

    private void insertTxHistory(String hash, String inOrOut, String address, String amount, String symbol, Date create_time) {

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
        }
    }
}
