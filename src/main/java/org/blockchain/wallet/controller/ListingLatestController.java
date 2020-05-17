package org.blockchain.wallet.controller;

import org.blockchain.wallet.resttemplate.CoinMarketCapRestAPI;
import org.blockchain.wallet.resttemplate.DNCRestAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@RestController
@RequestMapping(value = "/listingLatest")
public class ListingLatestController {

    @Autowired
    CoinMarketCapRestAPI coinMarketCapRestAPI;

    @Autowired
    DNCRestAPI dncRestAPI;

    @GetMapping
    public Object getListingLatest() {

//        return coinMarketCapRestAPI.getListingLatest();
        return dncRestAPI.getListingLatest();
    }

    @GetMapping(value = "/search")
    public Object searchCoin(@RequestParam String coin) {
        return dncRestAPI.searchCoin(coin);
    }

    @GetMapping(value = "/coinInfo")
    public Object getCoinInfo(@RequestParam String code) {
        return dncRestAPI.coinDetail(code);
    }
}
