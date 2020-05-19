package org.blockchain.wallet.async;

import org.blockchain.wallet.constant.Constant;
import org.blockchain.wallet.entity.CoinPrice;
import org.blockchain.wallet.entity.MonitorPrice;
import org.blockchain.wallet.resttemplate.DNCRestAPI;
import org.blockchain.wallet.service.FcmService;
import org.blockchain.wallet.service.MonitorPriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class MonitorPriceTask {

    @Autowired
    DNCRestAPI dncRestAPI;
    @Autowired
    MonitorPriceService monitorPriceService;
    @Autowired
    FcmService fcmService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Async
    public void monitorPrice(String code) {

        CoinPrice coinPrice = dncRestAPI.getCoinPrice(code);

        MonitorPrice monitorPriceCondition = new MonitorPrice();
        monitorPriceCondition.setCode(code);
        List<MonitorPrice> monitorPriceList = monitorPriceService.selectBySelective(monitorPriceCondition);
        for(MonitorPrice monitorPrice : monitorPriceList) {

            if(monitorPrice.getNotification().equals(Constant.NOTIFICATION_ON)) {
                if(monitorPrice.getUpPrice() != null && monitorPrice.getUpPrice() <= Double.valueOf(coinPrice.getPrice())) {
                    fcmService.sendPersonalNotification(monitorPrice.getUserId(), "价格预警",
                            monitorPrice.getCode() + "的价格已上涨至" + monitorPrice.getUpPrice());
                    monitorPrice.setNotification(Constant.NOTIFICATION_OFF);
                    monitorPrice.setUpdateTime(new Date());
                    monitorPriceService.updateBySelective(monitorPrice);
                }
                if(monitorPrice.getDownPrice() != null && monitorPrice.getDownPrice() >= Double.valueOf(coinPrice.getPrice())) {
                    fcmService.sendPersonalNotification(monitorPrice.getUserId(), "价格预警",
                            monitorPrice.getCode() + "的价格已下跌至" + monitorPrice.getDownPrice());
                    monitorPrice.setNotification(Constant.NOTIFICATION_OFF);
                    monitorPrice.setUpdateTime(new Date());
                    monitorPriceService.updateBySelective(monitorPrice);
                }
                if(monitorPrice.getUpChangePercent() != null && monitorPrice.getUpChangePercent() <= Double.valueOf(coinPrice.getChange_percent())) {
                    fcmService.sendPersonalNotification(monitorPrice.getUserId(), "价格预警",
                            monitorPrice.getCode() + "的价格24h上涨幅度已达到" + monitorPrice.getUpChangePercent());
                    monitorPrice.setNotification(Constant.NOTIFICATION_OFF);
                    monitorPrice.setUpdateTime(new Date());
                    monitorPriceService.updateBySelective(monitorPrice);
                }
                if(monitorPrice.getDownChangePercent() != null && monitorPrice.getDownChangePercent() <= Double.valueOf(coinPrice.getPrice())) {
                    fcmService.sendPersonalNotification(monitorPrice.getUserId(), "价格预警",
                            monitorPrice.getCode() + "的价格24h下跌幅度已达到" + monitorPrice.getDownChangePercent());
                    monitorPrice.setNotification(Constant.NOTIFICATION_OFF);
                    monitorPrice.setUpdateTime(new Date());
                    monitorPriceService.updateBySelective(monitorPrice);
                }
            }
        }

        monitorPriceService.deleteByOneMonth();
    }
}
