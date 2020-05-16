package org.blockchain.wallet.controller;

import org.blockchain.wallet.resttemplate.CoinMarketCapRestAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@RestController
@RequestMapping(value = "/listingLatest")
public class ListingLatestController {

    @Autowired
    CoinMarketCapRestAPI coinMarketCapRestAPI;

    @GetMapping
    public Object getListingLatest() {
        return coinMarketCapRestAPI.getListingLatest();
    }
}
