package org.blockchain.wallet.controller;

import org.blockchain.wallet.async.AsyncExcutor;
import org.blockchain.wallet.base.ResultResponse;
import org.blockchain.wallet.entity.CoinPrice;
import org.blockchain.wallet.entity.MonitorPrice;
import org.blockchain.wallet.resttemplate.DNCRestAPI;
import org.blockchain.wallet.service.MonitorPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@EnableAutoConfiguration
@RestController
@RequestMapping(value = "/monitorPrice")
public class MonitorPriceController {

    @Autowired
    DNCRestAPI dncRestAPI;

    @Autowired
    AsyncExcutor asyncExcutor;

    @Autowired
    MonitorPriceService monitorPriceService;

    @GetMapping(value ="/coinPrice")
    public ResultResponse<CoinPrice> getCoinPrice(@RequestParam String code) {
        return new ResultResponse<>(dncRestAPI.getCoinPrice(code));
    }

    @PostMapping
    public ResultResponse<Integer> insert(@RequestBody MonitorPrice monitorPrice) {
        return new ResultResponse<>(monitorPriceService.insert(monitorPrice));
    }

    @GetMapping
    public ResultResponse<List<MonitorPrice>> selectBySelective(@RequestParam int userId, @RequestParam String notification) {

        MonitorPrice monitorPrice = new MonitorPrice();
        monitorPrice.setUserId(userId);
        monitorPrice.setNotification(notification);

        return new ResultResponse<>(monitorPriceService.selectBySelective(monitorPrice));
    }

    @GetMapping(value = "test")
    public void monitorPrice() {
        asyncExcutor.monitorPrice();
    }
}
