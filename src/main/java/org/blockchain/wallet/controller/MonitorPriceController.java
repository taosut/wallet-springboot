package org.blockchain.wallet.controller;

import org.blockchain.wallet.async.AsyncExcutor;
import org.blockchain.wallet.base.ResultResponse;
import org.blockchain.wallet.entity.DNCCoinPrice;
import org.blockchain.wallet.entity.HuobiMarketDetail;
import org.blockchain.wallet.entity.MonitorCoin;
import org.blockchain.wallet.entity.MonitorPrice;
import org.blockchain.wallet.resttemplate.DNCRestAPI;
import org.blockchain.wallet.resttemplate.HuobiRestAPI;
import org.blockchain.wallet.service.MonitorCoinService;
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
    HuobiRestAPI huobiRestAPI;

    @Autowired
    AsyncExcutor asyncExcutor;

    @Autowired
    MonitorPriceService monitorPriceService;

    @Autowired
    MonitorCoinService monitorCoinService;

    @GetMapping(value ="/coinPrice")
    public ResultResponse<HuobiMarketDetail> getCoinPrice(@RequestParam String symbol) {
        return new ResultResponse<>(huobiRestAPI.getPrice(symbol));
    }

    @PostMapping
    public ResultResponse<Integer> insert(@RequestBody MonitorPrice monitorPrice) {
        return new ResultResponse<>(monitorPriceService.insert(monitorPrice));
    }

    @PutMapping
    public ResultResponse<Integer> update(@RequestBody MonitorPrice monitorPrice) {
        return new ResultResponse<>(monitorPriceService.updateBySelective(monitorPrice));
    }

    @GetMapping
    public ResultResponse<List<MonitorPrice>> selectBySelective(@RequestParam int userId, @RequestParam String notification) {

        MonitorPrice monitorPrice = new MonitorPrice();
        monitorPrice.setUserId(userId);
        monitorPrice.setNotification(notification);

        return new ResultResponse<>(monitorPriceService.selectBySelective(monitorPrice));
    }

    @GetMapping(value = "/coin")
    public ResultResponse<List<MonitorCoin>> selectCoins() {
        return new ResultResponse<>(monitorCoinService.selectBySelective(new MonitorCoin()));
    }

    @GetMapping(value = "/test")
    public void monitorPrice() {
        asyncExcutor.monitorPrice();
    }

}
