package org.blockchain.wallet.controller;

import org.blockchain.wallet.resttemplate.JinseRestAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@RestController
@RequestMapping(value = "/liveNews")
public class LiveNewsController {

    @Autowired
    JinseRestAPI jinseRestAPI;

    @GetMapping
    public Object getLiveNews() {
        return jinseRestAPI.getLive();
    }
}
