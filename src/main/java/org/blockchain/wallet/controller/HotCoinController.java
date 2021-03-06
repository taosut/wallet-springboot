package org.blockchain.wallet.controller;

import org.blockchain.wallet.resttemplate.DNCRestAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@RestController
@RequestMapping(value = "/hotcoin")
public class HotCoinController {

    @Autowired
    DNCRestAPI dncRestAPI;

    @GetMapping
    public String getHotCoin() {

        return dncRestAPI.getHotCoin();
    }
}
