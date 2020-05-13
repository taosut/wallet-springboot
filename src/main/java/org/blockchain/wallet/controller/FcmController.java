package org.blockchain.wallet.controller;

import org.blockchain.wallet.base.BaseResponse;
import org.blockchain.wallet.base.ResultResponse;
import org.blockchain.wallet.entity.Fcm;
import org.blockchain.wallet.service.FcmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@RestController
@RequestMapping(value = "/fcm")
public class FcmController {

    @Autowired
    FcmService fcmService;

    @PostMapping
    public BaseResponse<Integer> registerFcm(@RequestBody Fcm fcm) {
        return new ResultResponse<>(fcmService.register(fcm));
    }
}
