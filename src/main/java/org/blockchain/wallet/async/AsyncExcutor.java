package org.blockchain.wallet.async;

import org.blockchain.wallet.entity.MonitorAddress;
import org.blockchain.wallet.entity.MonitorCoin;
import org.blockchain.wallet.service.MonitorAddressService;
import org.blockchain.wallet.service.MonitorCoinService;
import org.blockchain.wallet.service.MonitorPriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
@EnableScheduling
public class AsyncExcutor {

    @Autowired
    MonitorAddressTask monitorAddressTask;

    @Autowired
    MonitorPriceTask monitorPriceTask;

    @Autowired
    MonitorAddressService monitorAddressService;

    @Autowired
    MonitorCoinService monitorCoinService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Scheduled(cron = "0 */5 * * * ?")
    public void monitorBTCAddress() {
        MonitorAddress findMonitorAddressCoindition = new MonitorAddress();
        findMonitorAddressCoindition.setSymbol("BTC");
        List<MonitorAddress> monitorAddressList = monitorAddressService.selectBySelective(findMonitorAddressCoindition);

        for(MonitorAddress monitorAddress : monitorAddressList) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss.SSS'Z'");
            Instant instant = Instant.now();
            try {
                String dateStr = instant.toString();
                Date date = dateFormat.parse(dateStr);

                monitorAddressTask.monitorBTCByBlockChair(monitorAddress.getAddress(), date.getTime());
                //            monitorAddressTask.monitorBTCByBlockChain(monitorAddress.getAddress(), new Date().getTime());
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    @Scheduled(cron = "50 */5 * * * ?")
    public void monitorPrice() {

        List<MonitorCoin> monitorCoinList = monitorCoinService.selectBySelective(new MonitorCoin());

        for(MonitorCoin monitorCoin : monitorCoinList) {
            try {
                monitorPriceTask.monitorPrice(monitorCoin.getCode());
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }
}
