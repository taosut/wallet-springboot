package org.blockchain.wallet.async;

import org.blockchain.wallet.entity.MonitorAddress;
import org.blockchain.wallet.entity.MonitorCoin;
import org.blockchain.wallet.service.MonitorAddressService;
import org.blockchain.wallet.service.MonitorCoinService;
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

    @Scheduled(cron = "0 16 */1 * * ?")
    public void monitorTask1() {
        monitorBTCAddress(0);
    }

    @Scheduled(cron = "0 33 */1 * * ?")
    public void monitorTask2() {
        monitorBTCAddress(1);
    }

    @Scheduled(cron = "0 58 */1 * * ?")
    public void monitorTask3() {
        monitorBTCAddress(2);
    }


    private void monitorBTCAddress(int i) {
        MonitorAddress findMonitorAddressCoindition = new MonitorAddress();
        findMonitorAddressCoindition.setSymbol("BTC");
        List<MonitorAddress> monitorAddressList = monitorAddressService.selectBySelective(findMonitorAddressCoindition);


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss.SSS'Z'");
        Instant instant = Instant.now();
        try {
            String dateStr = instant.toString();
            Date date = dateFormat.parse(dateStr);
            monitorAddressTask.monitorBTCByBlockChair(monitorAddressList.get(i).getAddress(), date.getTime());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Scheduled(cron = "50 */5 * * * ?")
    public void monitorPrice() {

        List<MonitorCoin> monitorCoinList = monitorCoinService.selectBySelective(new MonitorCoin());

        for(MonitorCoin monitorCoin : monitorCoinList) {
            try {
                monitorPriceTask.monitorPriceByHuobi(monitorCoin);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }
}
