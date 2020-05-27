package org.blockchain.wallet.controller;

import org.blockchain.wallet.resttemplate.CryptoRestAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@EnableAutoConfiguration
@RestController
@RequestMapping(value = "/BCHTEST")
public class BchController {

    @Autowired
    CryptoRestAPI cryptoRestAPI;

    @GetMapping(value = "/address/{address}")
    public String getAddressInfo(@PathVariable String address) {
        return cryptoRestAPI.getBCHAddressInfo(address);
    }

    @GetMapping(value = "/address/{address}/transaction")
    public String getTxList(@PathVariable String address) {
        return cryptoRestAPI.getBCHTxList(address);
    }
}
