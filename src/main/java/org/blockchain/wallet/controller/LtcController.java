package org.blockchain.wallet.controller;

import org.blockchain.wallet.entity.SochainBroadcast;
import org.blockchain.wallet.resttemplate.SochainRestAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@EnableAutoConfiguration
@RestController
@RequestMapping(value = "/LTCTEST")
public class LtcController {

    @Autowired
    SochainRestAPI sochainRestAPI;

    @GetMapping(value = "/address/{address}")
    public String getAddressInfo(@PathVariable String address) {
        return sochainRestAPI.getLTCAddressInfo(address);
    }

    @GetMapping(value = "/tx/{hash}")
    public String getTxInfo(@PathVariable String hash) {
        return sochainRestAPI.getLTCTxInfo(hash);
    }

    @PostMapping(value = "/send_tx")
    public String getTxInfo(@RequestBody SochainBroadcast sochainBroadcast) {
        return sochainRestAPI.broadcastLTC(sochainBroadcast);
    }
}
