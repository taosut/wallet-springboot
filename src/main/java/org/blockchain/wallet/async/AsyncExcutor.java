package org.blockchain.wallet.async;

import org.blockchain.wallet.entity.MonitorAddress;
import org.blockchain.wallet.service.MonitorAddressService;
import org.blockchain.wallet.service.impl.MonitorAddressServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Component
@EnableScheduling
public class AsyncExcutor {

    @Autowired
    public MonitorAddressTask monitorAddressTask;

    @Autowired
    MonitorAddressService monitorAddressService;

//    @Scheduled(cron = "0 */5 * * * ?")
    public void monitorBTCByBlockChain() {
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
                e.printStackTrace();
            }



        }
    }
}
