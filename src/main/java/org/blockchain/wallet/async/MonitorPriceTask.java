package org.blockchain.wallet.async;

import org.blockchain.wallet.constant.Constant;
import org.blockchain.wallet.entity.HuobiMarketDetail;
import org.blockchain.wallet.entity.MonitorCoin;
import org.blockchain.wallet.entity.MonitorPrice;
import org.blockchain.wallet.resttemplate.DNCRestAPI;
import org.blockchain.wallet.resttemplate.HuobiRestAPI;
import org.blockchain.wallet.service.EmailService;
import org.blockchain.wallet.service.FcmService;
import org.blockchain.wallet.service.MonitorPriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class MonitorPriceTask {

    @Autowired
    DNCRestAPI dncRestAPI;
    @Autowired
    HuobiRestAPI huobiRestAPI;
    @Autowired
    MonitorPriceService monitorPriceService;
    @Autowired
    FcmService fcmService;
    @Autowired
    EmailService emailService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Async
    public void monitorPriceByHuobi(MonitorCoin monitorCoin) {

        HuobiMarketDetail huobiMarketDetail = huobiRestAPI.getPrice(monitorCoin.getSymbol());

        MonitorPrice monitorPriceCondition = new MonitorPrice();
        monitorPriceCondition.setCode(monitorCoin.getCode());
        List<MonitorPrice> monitorPriceList = monitorPriceService.selectBySelective(monitorPriceCondition);
        for(MonitorPrice monitorPrice : monitorPriceList) {

            BigDecimal closePrice = new BigDecimal(huobiMarketDetail.getClose());
            BigDecimal openPrice = new BigDecimal(huobiMarketDetail.getOpen());
            Double changePercent = closePrice.subtract(openPrice).divide(openPrice,4, RoundingMode.HALF_DOWN).doubleValue()*100.00;

            if(monitorPrice.getNotification().equals(Constant.NOTIFICATION_ON)) {
                if(monitorPrice.getUpPrice() != null && monitorPrice.getUpPrice() <= Double.valueOf(huobiMarketDetail.getClose())) {
                    fcmService.sendPersonalNotification(monitorPrice.getUserId(), "价格预警",
                            monitorPrice.getCode() + "的价格已上涨至" + monitorPrice.getUpPrice());
                    monitorPrice.setNotification(Constant.NOTIFICATION_OFF);
                }
                else if(monitorPrice.getDownPrice() != null && monitorPrice.getDownPrice() >= Double.valueOf(huobiMarketDetail.getClose())) {
                    fcmService.sendPersonalNotification(monitorPrice.getUserId(), "价格预警",
                            monitorPrice.getCode() + "的价格已下跌至" + monitorPrice.getDownPrice());
                    monitorPrice.setNotification(Constant.NOTIFICATION_OFF);
                }
                else if(monitorPrice.getUpChangePercent() != null && monitorPrice.getUpChangePercent() <= changePercent) {
                    fcmService.sendPersonalNotification(monitorPrice.getUserId(), "价格预警",
                            monitorPrice.getCode() + "的价格24h上涨幅度已达到" + monitorPrice.getUpChangePercent() + "%");
                    monitorPrice.setNotification(Constant.NOTIFICATION_OFF);
                }
                else if(monitorPrice.getDownChangePercent() != null && monitorPrice.getDownChangePercent() >= changePercent) {
                    fcmService.sendPersonalNotification(monitorPrice.getUserId(), "价格预警",
                            monitorPrice.getCode() + "的价格24h下跌幅度已达到" + monitorPrice.getDownChangePercent() + "%");
                    monitorPrice.setNotification(Constant.NOTIFICATION_OFF);
                }
                monitorPriceService.updateBySelective(monitorPrice);
            }
            if(monitorPrice.getEmail().equals(Constant.NOTIFICATION_EMAIL_TRUE)) {
                if(monitorPrice.getUpPrice() != null && monitorPrice.getUpPrice() <= Double.valueOf(huobiMarketDetail.getClose())) {
                    emailService.sendEmailByUid(monitorPrice.getUserId(), "价格预警",
                            monitorPrice.getCode() + "的价格已上涨至" + monitorPrice.getUpPrice());
                    monitorPrice.setEmail(Constant.NOTIFICATION_EMAIL_FALSE);
                }
                else if(monitorPrice.getDownPrice() != null && monitorPrice.getDownPrice() >= Double.valueOf(huobiMarketDetail.getClose())) {
                    emailService.sendEmailByUid(monitorPrice.getUserId(), "价格预警",
                            monitorPrice.getCode() + "的价格已下跌至" + monitorPrice.getDownPrice());
                    monitorPrice.setEmail(Constant.NOTIFICATION_EMAIL_FALSE);
                }
                else if(monitorPrice.getUpChangePercent() != null && monitorPrice.getUpChangePercent() <= changePercent) {
                    emailService.sendEmailByUid(monitorPrice.getUserId(), "价格预警",
                            monitorPrice.getCode() + "的价格24h上涨幅度已达到" + monitorPrice.getUpChangePercent() + "%");
                    monitorPrice.setEmail(Constant.NOTIFICATION_EMAIL_FALSE);
                }
                else if(monitorPrice.getDownChangePercent() != null && monitorPrice.getDownChangePercent() >= changePercent) {
                    emailService.sendEmailByUid(monitorPrice.getUserId(), "价格预警",
                            monitorPrice.getCode() + "的价格24h下跌幅度已达到" + monitorPrice.getDownChangePercent() + "%");
                    monitorPrice.setEmail(Constant.NOTIFICATION_EMAIL_FALSE);
                }
                monitorPriceService.updateBySelective(monitorPrice);
            }

        }

        monitorPriceService.deleteByOneMonth();
    }
}
