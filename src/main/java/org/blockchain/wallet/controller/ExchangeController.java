package org.blockchain.wallet.controller;

import org.blockchain.wallet.resttemplate.DNCRestAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@RestController
@RequestMapping(value = "/exchange")
public class ExchangeController {

    @Autowired
    DNCRestAPI dncRestAPI;

    @GetMapping
    public String getExchangeList() {
        return dncRestAPI.getExchangeList();
    }

    @GetMapping(value = "/detail")
    public String getExchangeDetail(@RequestParam String code) {
        return dncRestAPI.getExchangeDetail(code);
    }
}
