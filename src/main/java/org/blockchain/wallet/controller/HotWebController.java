package org.blockchain.wallet.controller;

import org.blockchain.wallet.base.ResultResponse;
import org.blockchain.wallet.entity.HotWeb;
import org.blockchain.wallet.service.HotWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@EnableAutoConfiguration
@RestController
@RequestMapping(value = "/hotWeb")
public class HotWebController {

    @Autowired
    HotWebService hotWebService;

    @GetMapping
    public ResultResponse<List<HotWeb>> getHotWeb() {

        return new ResultResponse<>(hotWebService.selectBySelective(new HotWeb()));
    }
}
