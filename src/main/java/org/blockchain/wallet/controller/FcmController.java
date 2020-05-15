package org.blockchain.wallet.controller;

import org.blockchain.wallet.base.BaseResponse;
import org.blockchain.wallet.base.ResultResponse;
import org.blockchain.wallet.entity.Fcm;
import org.blockchain.wallet.fcm.FcmClient;
import org.blockchain.wallet.service.FcmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableAutoConfiguration
@RestController
@RequestMapping(value = "/fcm")
public class FcmController {

    @Autowired
    FcmService fcmService;

    @Autowired
    FcmClient fcmClient;

    @PostMapping
    public BaseResponse<Integer> registerFcm(@RequestBody Fcm fcm) {
        return new ResultResponse<>(fcmService.register(fcm));
    }

    @GetMapping(value = "/test")
    public BaseResponse<Integer> test() {

        fcmService.sendAllNotification("Hi", "Hello world");
        return new ResultResponse<>(1);
    }
}
